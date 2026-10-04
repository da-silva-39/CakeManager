package com.bolosdaaxcila.cakemanager.ui.orders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.Order;
import com.bolosdaaxcila.cakemanager.data.repository.OrderRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class OrderListActivity extends AppCompatActivity {

    private OrderRepository orderRepository;
    private OrderAdapter adapter;
    private LiveData<List<Order>> current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        orderRepository = new OrderRepository(this);

        RecyclerView recycler = findViewById(R.id.recyclerOrders);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderAdapter(new OrderAdapter.OnAction() {
            @Override
            public void onEdit(Order order) {
                Intent i = new Intent(OrderListActivity.this, OrderFormActivity.class);
                i.putExtra("order_id", order.getId());
                startActivity(i);
            }

            @Override
            public void onDelete(Order order) {
                new AlertDialog.Builder(OrderListActivity.this)
                        .setTitle(R.string.delete)
                        .setMessage(getString(R.string.confirm_delete_order))
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.delete, (d, w) -> orderRepository.delete(order))
                        .show();
            }

            @Override
            public void onOpen(Order order) {
                Intent i = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                i.putExtra("order_id", order.getId());
                startActivity(i);
            }
        });
        recycler.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> startActivity(new Intent(this, OrderFormActivity.class)));

        EditText search = findViewById(R.id.editSearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                observe(orderRepository.search(s.toString()));
            }
            @Override public void afterTextChanged(Editable s) { }
        });

        observe(orderRepository.getAll());
    }

    private void observe(LiveData<List<Order>> liveData) {
        if (current != null) current.removeObservers(this);
        current = liveData;
        current.observe(this, orders -> adapter.setItems(orders));
    }
}
