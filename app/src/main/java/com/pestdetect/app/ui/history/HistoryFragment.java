package com.pestdetect.app.ui.history;

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
import com.pestdetect.app.databinding.FragmentHistoryBinding;
import com.pestdetect.app.ui.adapters.ScanHistoryAdapter;
import com.pestdetect.app.ui.result.ResultActivity;
import com.pestdetect.app.utils.Constants;
import com.pestdetect.app.utils.NetworkUtils;
import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private ScanHistoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean isOnline = NetworkUtils.isNetworkAvailable(requireContext());
        if (!isOnline) {
            binding.layoutOfflineBanner.setVisibility(View.VISIBLE);
        } else {
            binding.layoutOfflineBanner.setVisibility(View.GONE);
        }

        binding.rvHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ScanHistoryAdapter(new ArrayList<>(), scan -> {
            Intent intent = new Intent(requireContext(), ResultActivity.class);
            intent.putExtra(Constants.EXTRA_SCAN_ID, scan.getId());
            startActivity(intent);
        });
        binding.rvHistory.setAdapter(adapter);

        binding.swipeRefresh.setOnRefreshListener(this::loadScans);

        loadScans();
    }

    private void loadScans() {
        new Thread(() -> {
            List<ScanEntity> scans = AppDatabase.getInstance(requireContext()).scanDao().getAllScans();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    adapter.updateData(scans);
                    binding.swipeRefresh.setRefreshing(false);
                });
            }
        }).start();
    }
}
