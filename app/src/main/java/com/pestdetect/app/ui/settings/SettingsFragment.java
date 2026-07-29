package com.pestdetect.app.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.pestdetect.app.R;
import com.pestdetect.app.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(requireContext(), isChecked ? "Pest alert notifications enabled" : "Notifications disabled", Toast.LENGTH_SHORT).show();
        });

        binding.cardAbout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.about_app)
                    .setMessage("PestDetect v1.0.0\nSmart Pest Detection App for Farmers\nIntegrated with Hugging Face ML Vision Models.")
                    .setPositiveButton("OK", null)
                    .show();
        });

        binding.cardHelp.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.help_support)
                    .setMessage("Helpline: 0800-FARMER (0800-327637)\nEmail: support@pestdetect.agri")
                    .setPositiveButton("Close", null)
                    .show();
        });
    }
}
