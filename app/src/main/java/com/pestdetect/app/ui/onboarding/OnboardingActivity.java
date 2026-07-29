package com.pestdetect.app.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.pestdetect.app.R;
import com.pestdetect.app.databinding.ActivityOnboardingBinding;
import com.pestdetect.app.ui.adapters.OnboardingAdapter;
import com.pestdetect.app.ui.auth.AuthActivity;
import com.pestdetect.app.utils.EncryptedSessionManager;
import com.pestdetect.app.utils.LocaleHelper;

public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;
    private OnboardingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.onAttach(this);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new OnboardingAdapter();
        binding.viewPager.setAdapter(adapter);

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == adapter.getItemCount() - 1) {
                    binding.btnNext.setText(R.string.get_started);
                } else {
                    binding.btnNext.setText(R.string.next);
                }
            }
        });

        binding.tvSkip.setOnClickListener(v -> finishOnboarding());

        binding.btnNext.setOnClickListener(v -> {
            int current = binding.viewPager.getCurrentItem();
            if (current < adapter.getItemCount() - 1) {
                binding.viewPager.setCurrentItem(current + 1);
            } else {
                finishOnboarding();
            }
        });
    }

    private void finishOnboarding() {
        EncryptedSessionManager session = new EncryptedSessionManager(this);
        session.setOnboardingCompleted(true);
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }
}
