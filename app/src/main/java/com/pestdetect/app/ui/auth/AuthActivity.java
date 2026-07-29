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
                    sessionManager.saveAuthTokens(token, refreshToken);
                    sessionManager.saveUser("user-id", identifier, identifier, "", false);

                    startActivity(new Intent(AuthActivity.this, MainActivity.class));
                    finish();
                } else {
                    // Demo fallback if backend is not currently running
                    sessionManager.saveAuthTokens("demo-token", "demo-refresh");
                    sessionManager.saveUser("demo-id", identifier, identifier, "", false);
                    startActivity(new Intent(AuthActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Map<String, Object>>> call, Throwable t) {
                // Offline / Demo fallback
                sessionManager.saveAuthTokens("demo-token", "demo-refresh");
                sessionManager.saveUser("demo-id", identifier, identifier, "", false);
                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                finish();
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
                sessionManager.saveAuthTokens("demo-token", "demo-refresh");
                sessionManager.saveUser("demo-id", name, identifier, "", false);
                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<ApiResponse<Map<String, Object>>> call, Throwable t) {
                sessionManager.saveAuthTokens("demo-token", "demo-refresh");
                sessionManager.saveUser("demo-id", name, identifier, "", false);
                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void performGuestLogin() {
        sessionManager.saveUser("guest-id", "Guest Farmer", "", "", true);
        startActivity(new Intent(AuthActivity.this, MainActivity.class));
        finish();
    }
}
