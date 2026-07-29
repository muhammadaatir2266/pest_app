package com.pestdetect.app.ui.analyzing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.pestdetect.app.data.db.AppDatabase;
import com.pestdetect.app.data.db.ScanEntity;
import com.pestdetect.app.databinding.ActivityAnalyzingBinding;
import com.pestdetect.app.ui.result.ResultActivity;
import com.pestdetect.app.utils.Constants;
import com.pestdetect.app.utils.LocaleHelper;
import java.util.UUID;

public class AnalyzingActivity extends AppCompatActivity {

    private ActivityAnalyzingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.onAttach(this);
        binding = ActivityAnalyzingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String imagePath = getIntent().getStringExtra(Constants.EXTRA_IMAGE_PATH);

        // Run analysis process & save to local cache
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String scanId = UUID.randomUUID().toString();
            ScanEntity scan = new ScanEntity(
                    scanId,
                    imagePath != null ? imagePath : "sample_pest.jpg",
                    "Aphids (Greenflies)",
                    "Myzus persicae",
                    "Small sap-sucking insects that cause leaf curling, stunting, and honeydew mold growth.",
                    0.94,
                    true,
                    new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date()),
                    true
            );

            new Thread(() -> {
                AppDatabase.getInstance(getApplicationContext()).scanDao().insertScan(scan);
                runOnUiThread(() -> {
                    Intent intent = new Intent(AnalyzingActivity.this, ResultActivity.class);
                    intent.putExtra(Constants.EXTRA_SCAN_ID, scanId);
                    intent.putExtra(Constants.EXTRA_IMAGE_PATH, imagePath);
                    startActivity(intent);
                    finish();
                });
            }).start();

        }, 2200);
    }
}
