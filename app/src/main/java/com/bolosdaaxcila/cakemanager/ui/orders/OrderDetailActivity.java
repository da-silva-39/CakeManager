package com.bolosdaaxcila.cakemanager.ui.orders;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.Order;
import com.bolosdaaxcila.cakemanager.data.model.OrderItem;
import com.bolosdaaxcila.cakemanager.data.model.OrderStatus;
import com.bolosdaaxcila.cakemanager.data.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private OrderRepository orderRepository;
    private com.bolosdaaxcila.cakemanager.data.repository.ProductRepository productRepository;
    private long orderId;
    private final List<String> itemLines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderRepository = new OrderRepository(this);
        productRepository = new com.bolosdaaxcila.cakemanager.data.repository.ProductRepository(this);
        orderId = getIntent().getLongExtra("order_id", -1);

        TextView textCustomer = findViewById(R.id.textCustomer);
        TextView textItems = findViewById(R.id.textItems);
        TextView textTotal = findViewById(R.id.textTotal);
        Spinner spinnerStatus = findViewById(R.id.spinnerStatus);
        Button btnSaveStatus = findViewById(R.id.btnSaveStatus);

        OrderStatus[] statuses = OrderStatus.values();
        List<String> labels = new ArrayList<>();
        for (OrderStatus s : statuses) labels.add(s.getLabel());
        spinnerStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labels));
        ((ArrayAdapter<?>) spinnerStatus.getAdapter()).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        orderRepository.findById(orderId, (Order order) -> {
            if (order == null) return;
            textCustomer.setText(order.getCustomerName() + " — " + order.getCustomerPhone());
            textTotal.setText(getString(R.string.total) + ": " + String.format(Locale.getDefault(), "%.2f", order.getTotal()) + " MZN");
            OrderStatus current = order.getOrderStatus();
            for (int i = 0; i < statuses.length; i++) {
                if (statuses[i] == current) { spinnerStatus.setSelection(i); break; }
            }
        });

        orderRepository.getItems(orderId).observe(this, items -> {
            if (items == null) return;
            StringBuilder sb = new StringBuilder();
            java.util.concurrent.atomic.AtomicInteger remaining = new java.util.concurrent.atomic.AtomicInteger(items.size());
            for (OrderItem oi : items) {
                productRepository.findById(oi.getProductId(), product -> {
                    String name = product != null ? product.getName() : ("#" + oi.getProductId());
                    sb.append("- ").append(name).append(" x").append(oi.getQuantity())
                            .append(" = ").append(String.format(Locale.getDefault(), "%.2f", oi.getSubtotal())).append(" MZN\n");
                    if (remaining.decrementAndGet() == 0) {
                        textItems.setText(sb.toString());
                    }
                });
            }
        });

        btnSaveStatus.setOnClickListener(v -> {
            int pos = spinnerStatus.getSelectedItemPosition();
            OrderStatus[] vals = OrderStatus.values();
            OrderStatus chosen = vals[Math.max(0, Math.min(pos, vals.length - 1))];
            orderRepository.findById(orderId, order -> {
                if (order != null) {
                    order.setStatus(chosen.name());
                    orderRepository.update(order);
                    Toast.makeText(this, R.string.status_updated, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
