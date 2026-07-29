package com.pestdetect.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pestdetect.app.R;
import com.pestdetect.app.data.db.ScanEntity;
import java.util.List;

public class ScanHistoryAdapter extends RecyclerView.Adapter<ScanHistoryAdapter.ViewHolder> {

    private List<ScanEntity> scanList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ScanEntity scan);
    }

    public ScanHistoryAdapter(List<ScanEntity> scanList, OnItemClickListener listener) {
        this.scanList = scanList;
        this.listener = listener;
    }

    public void updateData(List<ScanEntity> newList) {
        this.scanList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScanEntity scan = scanList.get(position);
        holder.tvPestName.setText(scan.getPestName());
        holder.tvDate.setText(scan.getCreatedAt() != null ? scan.getCreatedAt().split("T")[0] : "");

        if (scan.isHarmful()) {
            holder.tvStatus.setText(R.string.badge_harmful);
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(R.color.harmful_red));
        } else {
            holder.tvStatus.setText(R.string.badge_safe);
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(R.color.safe_green));
        }

        Glide.with(holder.itemView.getContext())
                .load(scan.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_camera)
                .into(holder.ivPest);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(scan);
        });
    }

    @Override
    public int getItemCount() {
        return scanList != null ? scanList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPest;
        TextView tvPestName, tvDate, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPest = itemView.findViewById(R.id.ivItemPest);
            tvPestName = itemView.findViewById(R.id.tvItemPestName);
            tvDate = itemView.findViewById(R.id.tvItemDate);
            tvStatus = itemView.findViewById(R.id.tvItemStatus);
        }
    }
}
