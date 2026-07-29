package com.pestdetect.app.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.pestdetect.app.databinding.ActivitySplashBinding;
import com.pestdetect.app.ui.language.LanguageSelectActivity;
import com.pestdetect.app.ui.main.MainActivity;
import com.pestdetect.app.ui.onboarding.OnboardingActivity;
import com.pestdetect.app.utils.EncryptedSessionManager;
import com.pestdetect.app.utils.LocaleHelper;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private EncryptedSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.onAttach(this);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new EncryptedSessionManager(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (sessionManager.isLoggedIn()) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else if (!sessionManager.isOnboardingCompleted()) {
                startActivity(new Intent(SplashActivity.this, LanguageSelectActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LanguageSelectActivity.class));
            }
            finish();
        }, 1800);
    }
}
