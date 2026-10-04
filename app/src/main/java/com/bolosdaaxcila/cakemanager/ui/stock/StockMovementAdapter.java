package com.bolosdaaxcila.cakemanager.ui.stock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.StockMovement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StockMovementAdapter extends RecyclerView.Adapter<StockMovementAdapter.ViewHolder> {

    private List<StockMovement> items = new ArrayList<>();

    public void setItems(List<StockMovement> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movement, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StockMovement m = items.get(position);
        holder.textType.setText(m.getType().equals(StockMovement.TYPE_ENTRY) ? "Entrada" : "Saída");
        holder.textQuantity.setText(String.format(Locale.getDefault(), "%.2f", m.getQuantity()));
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date(m.getDate()));
        holder.textDate.setText(date + (m.getDescription() != null && !m.getDescription().isEmpty() ? " — " + m.getDescription() : ""));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textType;
        TextView textQuantity;
        TextView textDate;

        ViewHolder(View itemView) {
            super(itemView);
            textType = itemView.findViewById(R.id.textType);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textDate = itemView.findViewById(R.id.textDate);
        }
    }
}
