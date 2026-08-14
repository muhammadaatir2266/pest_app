package com.pestdetect.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.pestdetect.app.R;
import com.pestdetect.app.data.api.ApiClient;
import com.pestdetect.app.data.models.ApiResponse;
import com.pestdetect.app.databinding.ActivityAuthBinding;
import com.pestdetect.app.ui.main.MainActivity;
import com.pestdetect.app.utils.EncryptedSessionManager;
import com.pestdetect.app.utils.LocaleHelper;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;
    private boolean isLoginMode = true;
    private EncryptedSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.onAttach(this);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new EncryptedSessionManager(this);

        binding.tvToggleMode.setOnClickListener(v -> toggleMode());

        binding.btnSubmit.setOnClickListener(v -> {
            if (isLoginMode) performLogin();
            else performSignup();
        });

        binding.btnGuest.setOnClickListener(v -> performGuestLogin());
    }

    private void toggleMode() {
        isLoginMode = !isLoginMode;
        if (isLoginMode) {
            binding.tvAuthTitle.setText(R.string.login_title);
            binding.tilName.setVisibility(View.GONE);
            binding.btnSubmit.setText(R.string.login);
            binding.tvToggleMode.setText(R.string.signup_title);
        } else {
            binding.tvAuthTitle.setText(R.string.signup_title);
            binding.tilName.setVisibility(View.VISIBLE);
            binding.btnSubmit.setText(R.string.signup);
            binding.tvToggleMode.setText(R.string.login_title);
        }
    }

    private void performLogin() {
        String identifier = binding.etIdentifier.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (identifier.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your email/phone and password", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> body = new HashMap<>();
        body.put("loginIdentifier", identifier);
        body.put("password", password);

        ApiClient.getApiService().login(body).enqueue(new Callback<ApiResponse<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<Map<String, Object>>> call, Response<ApiResponse<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Map<String, Object> data = response.body().getData();
                    String token = (String) data.get("accessToken");
                    String refreshToken = (String) data.get("refreshToken");
                    
                    @SuppressWarnings("unchecked")
                    Map<String, Object> user = data.get("user") instanceof Map ? (Map<String, Object>) data.get("user") : null;
                    String userId = user != null && user.get("id") != null ? (String) user.get("id") : "user-id";
                    String name = user != null && user.get("name") != null ? (String) user.get("name") : identifier;
                    String email = user != null && user.get("email") != null ? (String) user.get("email") : "";
                    String phone = user != null && user.get("phone") != null ? (String) user.get("phone") : "";

                    sessionManager.saveAuthTokens(token, refreshToken);
                    sessionManager.saveUser(userId, name, email, phone, false);

                    Toast.makeText(AuthActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AuthActivity.this, MainActivity.class));
                    finish();
                } else {
                    String errorMsg = "Invalid email/phone or password";
                    try {
                        if (response.errorBody() != null) {
                            String errorString = response.errorBody().string();
                            com.google.gson.JsonObject jsonObject = com.google.gson.JsonParser.parseString(errorString).getAsJsonObject();
                            if (jsonObject.has("message")) {
                                errorMsg = jsonObject.get("message").getAsString();
                            }
                        }
                    } catch (Exception e) {
                        // ignore parse error
                    }
                    Toast.makeText(AuthActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Map<String, Object>>> call, Throwable t) {
                Toast.makeText(AuthActivity.this, "Connection failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void performSignup() {
        String name = binding.etName.getText().toString().trim();
        String identifier = binding.etIdentifier.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (name.isEmpty() || identifier.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("email", identifier.contains("@") ? identifier : "");
        body.put("phone", !identifier.contains("@") ? identifier : "");
        body.put("password", password);

        ApiClient.getApiService().signup(body).enqueue(new Callback<ApiResponse<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<Map<String, Object>>> call, Response<ApiResponse<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Map<String, Object> data = response.body().getData();
                    String token = (String) data.get("accessToken");
                    String refreshToken = (String) data.get("refreshToken");
                    
                    @SuppressWarnings("unchecked")
                    Map<String, Object> user = data.get("user") instanceof Map ? (Map<String, Object>) data.get("user") : null;
                    String userId = user != null && user.get("id") != null ? (String) user.get("id") : "user-id";

                    sessionManager.saveAuthTokens(token, refreshToken);
                    sessionManager.saveUser(userId, name, identifier.contains("@") ? identifier : "", !identifier.contains("@") ? identifier : "", false);

                    Toast.makeText(AuthActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AuthActivity.this, MainActivity.class));
                    finish();
                } else {
                    String errorMsg = "Registration failed";
                    try {
                        if (response.errorBody() != null) {
                            String errorString = response.errorBody().string();
                            com.google.gson.JsonObject jsonObject = com.google.gson.JsonParser.parseString(errorString).getAsJsonObject();
                            if (jsonObject.has("message")) {
                                errorMsg = jsonObject.get("message").getAsString();
                            }
                        }
                    } catch (Exception e) {
                        // ignore parse error
                    }
                    Toast.makeText(AuthActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Map<String, Object>>> call, Throwable t) {
                Toast.makeText(AuthActivity.this, "Connection failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void performGuestLogin() {
        ApiClient.getApiService().guestLogin().enqueue(new Callback<ApiResponse<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<Map<String, Object>>> call, Response<ApiResponse<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Map<String, Object> data = response.body().getData();
                    String token = (String) data.get("accessToken");
                    String refreshToken = (String) data.get("refreshToken");
                    sessionManager.saveAuthTokens(token, refreshToken);
                }
                sessionManager.saveUser("guest-id", "Guest Farmer", "", "", true);
                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<ApiResponse<Map<String, Object>>> call, Throwable t) {
                sessionManager.saveUser("guest-id", "Guest Farmer", "", "", true);
                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
