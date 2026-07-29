package com.pestdetect.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pestdetect.app.R;
import com.pestdetect.app.data.models.Pesticide;
import java.util.List;

public class PesticideAdapter extends RecyclerView.Adapter<PesticideAdapter.ViewHolder> {

    private List<Pesticide> pesticides;

    public PesticideAdapter(List<Pesticide> pesticides) {
        this.pesticides = pesticides;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pesticide, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pesticide p = pesticides.get(position);
        holder.tvName.setText(p.getName());
        holder.tvIngredient.setText(p.getActiveIngredient());
        holder.tvDosage.setText(holder.itemView.getContext().getString(R.string.dosage, p.getDosage()));
        holder.tvApplication.setText(holder.itemView.getContext().getString(R.string.application_method, p.getApplicationMethod()));
        holder.tvSafety.setText(holder.itemView.getContext().getString(R.string.safety_notes, p.getSafetyNotes()));

        if ("organic".equalsIgnoreCase(p.getType())) {
            holder.tvType.setText(R.string.type_organic);
            holder.tvType.setBackgroundResource(R.color.safe_green_bg);
            holder.tvType.setTextColor(holder.itemView.getContext().getColor(R.color.safe_green));
        } else {
            holder.tvType.setText(R.string.type_chemical);
            holder.tvType.setBackgroundResource(R.color.harmful_red_bg);
            holder.tvType.setTextColor(holder.itemView.getContext().getColor(R.color.harmful_red));
        }
    }

    @Override
    public int getItemCount() {
        return pesticides != null ? pesticides.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvIngredient, tvDosage, tvApplication, tvSafety;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvPesticideName);
            tvType = itemView.findViewById(R.id.tvPesticideType);
            tvIngredient = itemView.findViewById(R.id.tvActiveIngredient);
            tvDosage = itemView.findViewById(R.id.tvDosage);
            tvApplication = itemView.findViewById(R.id.tvApplicationMethod);
            tvSafety = itemView.findViewById(R.id.tvSafetyNotes);
        }
    }
}
