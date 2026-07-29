package com.pestdetect.app.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.pestdetect.app.data.db.AppDatabase;
import com.pestdetect.app.data.db.ScanEntity;
import com.pestdetect.app.databinding.FragmentHomeBinding;
import com.pestdetect.app.ui.adapters.ScanHistoryAdapter;
import com.pestdetect.app.ui.camera.CameraActivity;
import com.pestdetect.app.ui.result.ResultActivity;
import com.pestdetect.app.utils.Constants;
import com.pestdetect.app.utils.EncryptedSessionManager;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ScanHistoryAdapter adapter;
    private EncryptedSessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new EncryptedSessionManager(requireContext());

        binding.tvWelcomeUser.setText("Welcome, " + sessionManager.getUserName() + "!");

        binding.cardScanBtn.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), CameraActivity.class));
        });

        binding.rvRecentScans.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ScanHistoryAdapter(new ArrayList<>(), scan -> {
            Intent intent = new Intent(requireContext(), ResultActivity.class);
            intent.putExtra(Constants.EXTRA_SCAN_ID, scan.getId());
            startActivity(intent);
        });
        binding.rvRecentScans.setAdapter(adapter);

        loadRecentScans();
    }

    private void loadRecentScans() {
        new Thread(() -> {
            List<ScanEntity> scans = AppDatabase.getInstance(requireContext()).scanDao().getAllScans();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (scans.isEmpty()) {
                        binding.layoutEmptyState.setVisibility(View.VISIBLE);
                        binding.rvRecentScans.setVisibility(View.GONE);
                    } else {
                        binding.layoutEmptyState.setVisibility(View.GONE);
                        binding.rvRecentScans.setVisibility(View.VISIBLE);
                        adapter.updateData(scans);
                    }
                });
            }
        }).start();
    }
}
