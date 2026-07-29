package com.pestdetect.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pestdetect.app.R;
import com.pestdetect.app.data.models.Crop;
import java.util.List;

public class AffectedCropAdapter extends RecyclerView.Adapter<AffectedCropAdapter.ViewHolder> {

    private List<Crop> crops;

    public AffectedCropAdapter(List<Crop> crops) {
        this.crops = crops;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_affected_crop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Crop c = crops.get(position);
        holder.tvName.setText(c.getCropName());
        holder.tvDamage.setText(c.getDamageDescription());
        holder.tvSeverity.setText("Severity: " + (c.getSeverity() != null ? c.getSeverity() : "High"));
    }

    @Override
    public int getItemCount() {
        return crops != null ? crops.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSeverity, tvDamage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCropName);
            tvSeverity = itemView.findViewById(R.id.tvSeverity);
            tvDamage = itemView.findViewById(R.id.tvDamageDesc);
        }
    }
}
