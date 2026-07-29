package com.pestdetect.app.ui.language;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.pestdetect.app.databinding.ActivityLanguageSelectBinding;
import com.pestdetect.app.ui.onboarding.OnboardingActivity;
import com.pestdetect.app.utils.LocaleHelper;

public class LanguageSelectActivity extends AppCompatActivity {

    private ActivityLanguageSelectBinding binding;
    private String selectedLanguage = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.onAttach(this);
        binding = ActivityLanguageSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cardEnglish.setOnClickListener(v -> selectLanguage("en"));
        binding.cardUrdu.setOnClickListener(v -> selectLanguage("ur"));
        binding.rbEnglish.setOnClickListener(v -> selectLanguage("en"));
        binding.rbUrdu.setOnClickListener(v -> selectLanguage("ur"));

        binding.btnContinueLanguage.setOnClickListener(v -> {
            LocaleHelper.setLocale(this, selectedLanguage);
            startActivity(new Intent(LanguageSelectActivity.this, OnboardingActivity.class));
            finish();
        });
    }

    private void selectLanguage(String lang) {
        selectedLanguage = lang;
        if ("en".equals(lang)) {
            binding.rbEnglish.setChecked(true);
            binding.rbUrdu.setChecked(false);
        } else {
            binding.rbEnglish.setChecked(false);
            binding.rbUrdu.setChecked(true);
        }
    }
}
