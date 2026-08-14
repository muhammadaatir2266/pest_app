package com.pestdetect.app.ui.analyzing;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.pestdetect.app.R;
import com.pestdetect.app.data.api.ApiClient;
import com.pestdetect.app.data.db.AppDatabase;
import com.pestdetect.app.data.db.ScanEntity;
import com.pestdetect.app.data.models.ApiResponse;
import com.pestdetect.app.data.models.Pest;
import com.pestdetect.app.data.models.ScanResponse;
import com.pestdetect.app.databinding.ActivityAnalyzingBinding;
import com.pestdetect.app.ui.camera.CameraActivity;
import com.pestdetect.app.ui.result.ResultActivity;
import com.pestdetect.app.utils.Constants;
import com.pestdetect.app.utils.EncryptedSessionManager;
import com.pestdetect.app.utils.LocaleHelper;
import com.pestdetect.app.utils.NetworkUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyzingActivity extends AppCompatActivity {

    private static final String TAG = "AnalyzingActivity";
    private ActivityAnalyzingBinding binding;
    private String imagePath;
    private String resolvedFilePath; // Always a real file path (after content:// resolution)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.onAttach(this);
        binding = ActivityAnalyzingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imagePath = getIntent().getStringExtra(Constants.EXTRA_IMAGE_PATH);
        Log.d(TAG, "Received imagePath: " + imagePath);

        if (imagePath == null) {
            Log.e(TAG, "No image path provided!");
            showNoPestDetectedDialog(getString(R.string.no_pest_detected_msg));
            return;
        }

        // Resolve the image path - handle both file paths and content:// URIs
        new Thread(() -> {
            resolvedFilePath = resolveImagePath(imagePath);
            Log.d(TAG, "Resolved file path: " + resolvedFilePath
                    + " (exists=" + (resolvedFilePath != null && new File(resolvedFilePath).exists()) + ")");

            runOnUiThread(() -> {
                if (resolvedFilePath != null && new File(resolvedFilePath).exists()) {
                    performBackendAnalysis(resolvedFilePath);
                } else {
                    Log.w(TAG, "Could not resolve image to valid file");
                    showNoPestDetectedDialog(getString(R.string.no_pest_detected_msg));
                }
            });
        }).start();
    }

    /**
     * Resolve imagePath to a real file path.
     * If it's a content:// URI, copy it to a temp file.
     * If it's already a file path, return as-is.
     */
    private String resolveImagePath(String path) {
        if (path == null) return null;

        // If it's a content:// URI, copy to a temp file
        if (path.startsWith("content://")) {
            try {
                Uri uri = Uri.parse(path);
                InputStream inputStream = getContentResolver().openInputStream(uri);
                if (inputStream == null) {
                    Log.e(TAG, "Could not open content:// URI input stream");
                    return null;
                }

                File tempFile = new File(getCacheDir(), "pest_scan_" + System.currentTimeMillis() + ".jpg");
                FileOutputStream fos = new FileOutputStream(tempFile);
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                fos.close();
                inputStream.close();

                Log.d(TAG, "Copied content:// URI to temp file: " + tempFile.getAbsolutePath()
                        + " (" + tempFile.length() + " bytes)");
                return tempFile.getAbsolutePath();
            } catch (Exception e) {
                Log.e(TAG, "Failed to copy content:// URI to temp file: " + e.getMessage());
                return null;
            }
        }

        // It's a regular file path
        File file = new File(path);
        if (file.exists()) {
            return path;
        }

        Log.w(TAG, "File does not exist: " + path);
        return null;
    }

    private void performBackendAnalysis(String filePath) {
        // Check internet connection before starting analysis
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Log.w(TAG, "No internet connection available. Cancelling backend analysis.");
            showNoInternetDialog(filePath);
            return;
        }

        new Thread(() -> {
            File uploadFile = getCompressedImageFile(filePath);
            if (uploadFile == null) uploadFile = new File(filePath);

            final File finalUploadFile = uploadFile;

            runOnUiThread(() -> {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), finalUploadFile);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", finalUploadFile.getName(), requestFile);

                EncryptedSessionManager sessionManager = new EncryptedSessionManager(AnalyzingActivity.this);
                String token = sessionManager.getAccessToken();
                String authHeader = (token != null && !token.isEmpty() && !token.equals("demo-token")) ? "Bearer " + token : null;

                Log.d(TAG, "Sending image to backend: " + finalUploadFile.getName() + " (" + finalUploadFile.length() + " bytes), authHeader=" + (authHeader != null ? "PRESENT" : "NULL"));

                ApiClient.getApiService().uploadScanImage(authHeader, body).enqueue(new Callback<ApiResponse<ScanResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<ScanResponse>> call, Response<ApiResponse<ScanResponse>> response) {
                        Log.d(TAG, "Backend response code: " + response.code());

                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<ScanResponse> apiResponse = response.body();
                            ScanResponse scanRes = apiResponse.getData();

                            Log.d(TAG, "API success=" + apiResponse.isSuccess() + ", message=" + apiResponse.getMessage());

                            if (scanRes != null && scanRes.isPestDetected() && scanRes.getPest() != null) {
                                Log.d(TAG, "✅ Pest detected via backend! Showing result...");
                                saveScanAndShowResult(scanRes, filePath);
                            } else if (scanRes != null && scanRes.getPest() != null && scanRes.getPest().getName() != null) {
                                // Even if isPestDetected is false due to deserialization, check if pest object exists
                                Log.d(TAG, "Pest object exists despite isPestDetected flag, forcing detection...");
                                scanRes.setPestDetected(true);
                                saveScanAndShowResult(scanRes, filePath);
                            } else {
                                // Backend explicitly reported no pest detected OR parsing failed
                                String msg = (scanRes != null && scanRes.getMessage() != null && !scanRes.getMessage().isEmpty())
                                        ? scanRes.getMessage()
                                        : (apiResponse.getMessage() != null ? apiResponse.getMessage() : getString(R.string.no_pest_detected_msg));
                                Log.w(TAG, "Backend response indicates no pest: " + msg);
                                showNoPestDetectedDialog(msg);
                            }
                        } else {
                            Log.w(TAG, "Backend analysis API non-200 response: " + response.code());
                            String serverMsg = null;
                            try {
                                if (response.errorBody() != null) {
                                    String errorString = response.errorBody().string();
                                    Log.w(TAG, "Backend error body: " + errorString);
                                    com.google.gson.JsonObject jsonObject = com.google.gson.JsonParser.parseString(errorString).getAsJsonObject();
                                    if (jsonObject.has("message")) {
                                        serverMsg = jsonObject.get("message").getAsString();
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Failed to parse error body: " + e.getMessage());
                            }

                            if (serverMsg != null && !serverMsg.isEmpty()) {
                                showCustomErrorDialog(filePath, serverMsg);
                            } else {
                                showServerErrorDialog(filePath);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<ScanResponse>> call, Throwable t) {
                        Log.e(TAG, "Backend analysis request failed: " + t.getMessage(), t);
                        if (NetworkUtils.isNetworkAvailable(AnalyzingActivity.this)) {
                            // Device has network connection, but request timed out or server failed to respond
                            String detail = t != null && t.getMessage() != null ? t.getMessage() : (t != null ? t.toString() : "Unknown network error");
                            showCustomErrorDialog(filePath, getString(R.string.server_error_msg) + "\n\nDetails: " + detail);
                        } else {
                            // Device is actually offline
                            showNoInternetDialog(filePath);
                        }
                    }
                });
            });
        }).start();
    }

    /**
     * Compress and downsample image to max 1280px resolution and JPEG quality 85
     * before uploading to backend. Reduces payload size from ~8MB to ~200KB.
     */
    private File getCompressedImageFile(String originalPath) {
        if (originalPath == null) return null;
        File originalFile = new File(originalPath);
        if (!originalFile.exists()) return null;

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(originalPath, options);

            int width = options.outWidth;
            int height = options.outHeight;
            int maxDimension = 1280;

            int sampleSize = 1;
            if (width > maxDimension || height > maxDimension) {
                int halfWidth = width / 2;
                int halfHeight = height / 2;
                while ((halfWidth / sampleSize) >= maxDimension && (halfHeight / sampleSize) >= maxDimension) {
                    sampleSize *= 2;
                }
            }

            options.inJustDecodeBounds = false;
            options.inSampleSize = sampleSize;
            Bitmap bitmap = BitmapFactory.decodeFile(originalPath, options);

            if (bitmap == null) return originalFile;

            if (bitmap.getWidth() > maxDimension || bitmap.getHeight() > maxDimension) {
                float aspectRatio = (float) bitmap.getWidth() / bitmap.getHeight();
                int newWidth = maxDimension;
                int newHeight = maxDimension;
                if (aspectRatio > 1) {
                    newHeight = Math.round(maxDimension / aspectRatio);
                } else {
                    newWidth = Math.round(maxDimension * aspectRatio);
                }
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
                if (scaledBitmap != bitmap) {
                    bitmap.recycle();
                    bitmap = scaledBitmap;
                }
            }

            File compressedFile = new File(getCacheDir(), "compressed_scan_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(compressedFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
            fos.flush();
            fos.close();
            bitmap.recycle();

            Log.d(TAG, "Compressed image: " + originalFile.length() + " bytes -> " + compressedFile.length() + " bytes");
            return compressedFile;
        } catch (Exception e) {
            Log.w(TAG, "Image compression failed, using original file: " + e.getMessage());
            return originalFile;
        }
    }

    private void showNoInternetDialog(String filePath) {
        if (isFinishing()) return;

        new AlertDialog.Builder(this)
                .setTitle(R.string.no_internet_title)
                .setMessage(R.string.no_internet_msg)
                .setCancelable(false)
                .setPositiveButton(R.string.retry, (dialog, which) -> performBackendAnalysis(filePath))
                .setNegativeButton(R.string.cancel, (dialog, which) -> finish())
                .show();
    }

    private void showServerErrorDialog(String filePath) {
        if (isFinishing()) return;

        new AlertDialog.Builder(this)
                .setTitle(R.string.server_error_title)
                .setMessage(R.string.server_error_msg)
                .setCancelable(false)
                .setPositiveButton(R.string.retry, (dialog, which) -> performBackendAnalysis(filePath))
                .setNegativeButton(R.string.cancel, (dialog, which) -> finish())
                .show();
    }

    private void showCustomErrorDialog(String filePath, String message) {
        if (isFinishing()) return;

        new AlertDialog.Builder(this)
                .setTitle(R.string.server_error_title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.retry, (dialog, which) -> performBackendAnalysis(filePath))
                .setNegativeButton(R.string.cancel, (dialog, which) -> finish())
                .show();
    }

    private void saveScanAndShowResult(ScanResponse scanRes, String filePath) {
        String scanId = scanRes.getScanId() != null ? scanRes.getScanId() : UUID.randomUUID().toString();
        String pestName = scanRes.getPest() != null ? scanRes.getPest().getName() : "Detected Pest";
        String sciName = scanRes.getPest() != null ? scanRes.getPest().getScientificName() : "";
        String desc = scanRes.getPest() != null ? scanRes.getPest().getDescription() : "";

        // Prioritize uploaded server/cloud image URL over temporary local cache file path
        String savedImageUrl = (scanRes.getImageUrl() != null && !scanRes.getImageUrl().isEmpty())
                ? scanRes.getImageUrl()
                : filePath;

        ScanEntity scan = new ScanEntity(
                scanId,
                savedImageUrl,
                pestName,
                sciName,
                desc,
                scanRes.getConfidenceScore(),
                scanRes.isHarmful(),
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()),
                true
        );

        new Thread(() -> {
            AppDatabase.getInstance(getApplicationContext()).scanDao().insertScan(scan);
            runOnUiThread(() -> {
                Intent intent = new Intent(AnalyzingActivity.this, ResultActivity.class);
                intent.putExtra(Constants.EXTRA_SCAN_ID, scanId);
                intent.putExtra(Constants.EXTRA_SCAN_RESULT, scanRes);
                intent.putExtra(Constants.EXTRA_IMAGE_PATH, savedImageUrl);
                startActivity(intent);
                finish();
            });
        }).start();
    }

    private void showNoPestDetectedDialog(String message) {
        if (isFinishing()) return;

        new AlertDialog.Builder(this)
                .setTitle(R.string.no_pest_detected_title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.retake_photo, (dialog, which) -> {
                    startActivity(new Intent(AnalyzingActivity.this, CameraActivity.class));
                    finish();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> finish())
                .show();
    }
}
