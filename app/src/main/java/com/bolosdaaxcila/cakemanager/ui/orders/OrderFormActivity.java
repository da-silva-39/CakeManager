package com.bolosdaaxcila.cakemanager.ui.orders;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AdapterView;
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
import com.bolosdaaxcila.cakemanager.data.model.Product;
import com.bolosdaaxcila.cakemanager.data.repository.OrderRepository;
import com.bolosdaaxcila.cakemanager.data.repository.ProductRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderFormActivity extends AppCompatActivity {

    public static class ChosenItem {
        long productId;
        String name;
        double unitPrice;
        int quantity;
        BigDecimal subtotal;

        ChosenItem(Product product, int quantity) {
            this.productId = product.getId();
            this.name = product.getName();
            this.unitPrice = product.getPrice();
            this.quantity = quantity;
            this.subtotal = BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(quantity));
        }
    }

    private TextInputEditText editCustomerName;
    private TextInputEditText editCustomerPhone;
    private Spinner spinnerProduct;
    private TextInputEditText editQuantity;
    private TextView textChosen;
    private TextView textTotal;

    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    private final List<Product> products = new ArrayList<>();
    private final List<ChosenItem> chosen = new ArrayList<>();
    private long orderId = -1;
    private long selectedProductId = -1;
    private boolean productsReady;
    private java.util.List<com.bolosdaaxcila.cakemanager.data.model.OrderItem> lastItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);

        editCustomerName = findViewById(R.id.editCustomerName);
        editCustomerPhone = findViewById(R.id.editCustomerPhone);
        spinnerProduct = findViewById(R.id.spinnerProduct);
        editQuantity = findViewById(R.id.editQuantity);
        textChosen = findViewById(R.id.textChosen);
        textTotal = findViewById(R.id.textTotal);
        Button btnAddItem = findViewById(R.id.btnAddItem);
        Button btnSave = findViewById(R.id.btnSave);

        productRepository = new ProductRepository(this);
        orderRepository = new OrderRepository(this);
        orderId = getIntent().getLongExtra("order_id", -1);

        productRepository.getAll().observe(this, list -> {
            products.clear();
            if (list != null) products.addAll(list);
            productsReady = true;
            rebuildChosen();
            List<String> names = new ArrayList<>();
            for (Product p : products) names.add(p.getName());
            spinnerProduct.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names));
            ((ArrayAdapter<?>) spinnerProduct.getAdapter()).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        });

        spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                if (position >= 0 && position < products.size()) selectedProductId = products.get(position).getId();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnAddItem.setOnClickListener(v -> addItem());
        btnSave.setOnClickListener(v -> save());

        if (orderId != -1) {
            orderRepository.findById(orderId, order -> {
                if (order != null) {
                    editCustomerName.setText(order.getCustomerName());
                    editCustomerPhone.setText(order.getCustomerPhone());
                }
            });
            orderRepository.getItems(orderId).observe(this, items -> {
                lastItems = items;
                rebuildChosen();
            });
        }
    }

    private void rebuildChosen() {
        if (lastItems == null || !productsReady) return;
        chosen.clear();
        for (OrderItem oi : lastItems) {
            for (Product p : products) {
                if (p.getId() == oi.getProductId()) {
                    chosen.add(new ChosenItem(p, oi.getQuantity()));
                    break;
                }
            }
        }
        refresh();
    }

    private void addItem() {
        if (selectedProductId == -1) {
            Toast.makeText(this, R.string.error_select_product, Toast.LENGTH_LONG).show();
            return;
        }
        String qtyText = editQuantity.getText() != null ? editQuantity.getText().toString().trim() : "";
        int qty;
        try {
            qty = Integer.parseInt(qtyText);
        } catch (NumberFormatException e) {
            editQuantity.setError(getString(R.string.error_invalid_quantity));
            return;
        }
        if (qty <= 0) {
            editQuantity.setError(getString(R.string.error_invalid_quantity));
            return;
        }
        for (Product p : products) {
            if (p.getId() == selectedProductId) {
                chosen.add(new ChosenItem(p, qty));
                break;
            }
        }
        editQuantity.setText("");
        refresh();
    }

    private void refresh() {
        StringBuilder sb = new StringBuilder();
        BigDecimal total = BigDecimal.ZERO;
        for (ChosenItem c : chosen) {
            sb.append("- ").append(c.name).append(" x").append(c.quantity)
                    .append(" = ").append(c.subtotal.setScale(2, RoundingMode.HALF_UP)).append(" MZN\n");
            total = total.add(c.subtotal);
        }
        textChosen.setText(sb.toString());
        textTotal.setText(getString(R.string.total) + ": " + total.setScale(2, RoundingMode.HALF_UP).toPlainString() + " MZN");
    }

    private void save() {
        String name = editCustomerName.getText() != null ? editCustomerName.getText().toString().trim() : "";
        String phone = editCustomerPhone.getText() != null ? editCustomerPhone.getText().toString().trim() : "";

        if (TextUtils.isEmpty(name)) { editCustomerName.setError(getString(R.string.error_name_required)); return; }
        if (phone.replaceAll("[^0-9]", "").length() < 9) { editCustomerPhone.setError(getString(R.string.error_invalid_phone)); return; }
        if (chosen.isEmpty()) { Toast.makeText(this, R.string.error_order_needs_items, Toast.LENGTH_LONG).show(); return; }

        BigDecimal totalAcc = BigDecimal.ZERO;
        for (ChosenItem c : chosen) totalAcc = totalAcc.add(c.subtotal);
        final BigDecimal total = totalAcc;

        if (orderId == -1) {
            orderRepository.insert(new Order(name, phone, System.currentTimeMillis(), OrderStatus.PENDENTE.name(), total.doubleValue()), id -> {
                if (id != null) {
                    for (ChosenItem c : chosen) {
                        orderRepository.insertItem(new OrderItem(id, c.productId, c.quantity, c.unitPrice, c.subtotal.doubleValue()));
                    }
                }
                finish();
            });
        } else {
            Order order = new Order(name, phone, System.currentTimeMillis(), OrderStatus.PENDENTE.name(), total.doubleValue());
            orderRepository.findById(orderId, existing -> {
                if (existing != null) {
                    existing.setCustomerName(name);
                    existing.setCustomerPhone(phone);
                    existing.setTotal(total.doubleValue());
                    orderRepository.update(existing);
                    long oid = existing.getId();
                    orderRepository.clearItems(oid);
                    for (ChosenItem c : chosen) {
                        orderRepository.insertItem(new OrderItem(oid, c.productId, c.quantity, c.unitPrice, c.subtotal.doubleValue()));
                    }
                }
                finish();
            });
        }
    }
}
