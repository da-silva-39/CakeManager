package com.bolosdaaxcila.cakemanager.ui.ingredients;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    public interface OnAction {
        void onEdit(Ingredient ingredient);
        void onDelete(Ingredient ingredient);
    }

    private List<Ingredient> items = new ArrayList<>();
    private final OnAction listener;

    public IngredientAdapter(OnAction listener) {
        this.listener = listener;
    }

    public void setItems(List<Ingredient> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient i = items.get(position);
        holder.textName.setText(i.getName());
        String qty = String.format("%.2f %s (mín. %.2f)", i.getQuantity(), i.getUnit(), i.getMinimumQuantity());
        holder.textQuantity.setText(qty);
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(i));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(i));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textQuantity;
        Button btnEdit;
        Button btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
