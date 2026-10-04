package com.bolosdaaxcila.cakemanager.ui.products;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.Category;
import com.bolosdaaxcila.cakemanager.data.model.Product;
import com.bolosdaaxcila.cakemanager.data.repository.CategoryRepository;
import com.bolosdaaxcila.cakemanager.data.repository.ProductRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ProductFormActivity extends AppCompatActivity {

    private TextInputEditText editName;
    private TextInputEditText editDescription;
    private TextInputEditText editPrice;
    private Spinner spinnerCategory;

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private final List<Category> categories = new ArrayList<>();
    private long productId = -1;
    private long selectedCategoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        editName = findViewById(R.id.editName);
        editDescription = findViewById(R.id.editDescription);
        editPrice = findViewById(R.id.editPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        Button btnSave = findViewById(R.id.btnSave);

        productRepository = new ProductRepository(this);
        categoryRepository = new CategoryRepository(this);
        productId = getIntent().getLongExtra("product_id", -1);

        categoryRepository.getAll().observe(this, list -> {
            categories.clear();
            if (list != null) categories.addAll(list);
            List<String> names = new ArrayList<>();
            for (Category c : categories) names.add(c.getName());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(adapter);

            if (productId != -1) {
                productRepository.findById(productId, product -> {
                    if (product != null) {
                        editName.setText(product.getName());
                        editDescription.setText(product.getDescription());
                        editPrice.setText(String.valueOf(product.getPrice()));
                        for (int i = 0; i < categories.size(); i++) {
                            if (categories.get(i).getId() == product.getCategoryId()) {
                                spinnerCategory.setSelection(i);
                                break;
                            }
                        }
                    }
                });
            }
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                if (position >= 0 && position < categories.size()) {
                    selectedCategoryId = categories.get(position).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        String name = editName.getText() != null ? editName.getText().toString().trim() : "";
        String description = editDescription.getText() != null ? editDescription.getText().toString().trim() : "";
        String priceText = editPrice.getText() != null ? editPrice.getText().toString().trim() : "";

        if (TextUtils.isEmpty(name)) {
            editName.setError(getString(R.string.error_name_required));
            return;
        }
        double price;
        try {
            price = Double.parseDouble(priceText.replace(',', '.'));
        } catch (NumberFormatException e) {
            editPrice.setError(getString(R.string.error_invalid_price));
            return;
        }
        if (price <= 0) {
            editPrice.setError(getString(R.string.error_invalid_price));
            return;
        }
        if (selectedCategoryId == -1) {
            android.widget.Toast.makeText(this, R.string.error_select_category, android.widget.Toast.LENGTH_LONG).show();
            return;
        }

        if (productId == -1) {
            productRepository.insert(new Product(name, description, price, selectedCategoryId), id -> finish());
        } else {
            long finalSelectedCategoryId = selectedCategoryId;
            productRepository.findById(productId, product -> {
                if (product != null) {
                    product.setName(name);
                    product.setDescription(description);
                    product.setPrice(price);
                    product.setCategoryId(finalSelectedCategoryId);
                    productRepository.update(product);
                }
                finish();
            });
        }
    }
}
