package com.pestdetect.app.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.pestdetect.app.databinding.FragmentProfileBinding;
import com.pestdetect.app.ui.auth.AuthActivity;
import com.pestdetect.app.ui.language.LanguageSelectActivity;
import com.pestdetect.app.utils.EncryptedSessionManager;
import com.pestdetect.app.utils.LocaleHelper;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private EncryptedSessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new EncryptedSessionManager(requireContext());

        binding.tvProfileName.setText(sessionManager.getUserName());
        binding.tvProfileContact.setText(sessionManager.getUserEmail().isEmpty() ? "Farmer Account" : sessionManager.getUserEmail());

        String currentLang = LocaleHelper.getLanguage(requireContext());
        binding.tvCurrentLanguage.setText("ur".equals(currentLang) ? "اردو" : "English");

        binding.cardLanguageToggle.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), LanguageSelectActivity.class));
        });

        binding.btnLogout.setOnClickListener(v -> {
            sessionManager.clearSession();
            startActivity(new Intent(requireContext(), AuthActivity.class));
            if (getActivity() != null) getActivity().finish();
        });
    }
}
