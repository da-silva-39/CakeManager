package com.bolosdaaxcila.cakemanager.ui.products;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public interface OnAction {
        void onEdit(Product product);
        void onDelete(Product product);
    }

    private List<Product> items = new ArrayList<>();
    private final OnAction listener;

    public ProductAdapter(OnAction listener) {
        this.listener = listener;
    }

    public void setItems(List<Product> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = items.get(position);
        holder.textName.setText(p.getName());
        if (p.getImageUri() != null && !p.getImageUri().isEmpty()) {
            try {
                java.io.InputStream is = holder.itemView.getContext().getContentResolver()
                        .openInputStream(android.net.Uri.parse(p.getImageUri()));
                holder.imageProduct.setImageBitmap(android.graphics.BitmapFactory.decodeStream(is));
            } catch (Exception e) {
                holder.imageProduct.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            holder.imageProduct.setImageResource(android.R.drawable.ic_menu_gallery);
        }
        holder.textPrice.setText(String.format("%.2f MZN", p.getPrice()));
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(p));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(p));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textPrice;
        android.widget.ImageView imageProduct;
        Button btnEdit;
        Button btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textPrice = itemView.findViewById(R.id.textPrice);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
