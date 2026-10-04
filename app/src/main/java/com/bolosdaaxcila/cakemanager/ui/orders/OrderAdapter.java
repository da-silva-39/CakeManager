package com.bolosdaaxcila.cakemanager.ui.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.Order;
import com.bolosdaaxcila.cakemanager.data.model.OrderStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    public interface OnAction {
        void onEdit(Order order);
        void onDelete(Order order);
        void onOpen(Order order);
    }

    private List<Order> items = new ArrayList<>();
    private final OnAction listener;

    public OrderAdapter(OnAction listener) {
        this.listener = listener;
    }

    public void setItems(List<Order> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order o = items.get(position);
        holder.textCustomer.setText(o.getCustomerName());
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date(o.getDate()));
        OrderStatus status = o.getOrderStatus();
        holder.textStatus.setText(date + " • " + status.getLabel());
        holder.textTotal.setText(String.format(Locale.getDefault(), "%.2f MZN", o.getTotal()));
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(o));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(o));
        holder.itemView.setOnClickListener(v -> listener.onOpen(o));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textCustomer;
        TextView textStatus;
        TextView textTotal;
        Button btnEdit;
        Button btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            textCustomer = itemView.findViewById(R.id.textCustomer);
            textStatus = itemView.findViewById(R.id.textStatus);
            textTotal = itemView.findViewById(R.id.textTotal);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
