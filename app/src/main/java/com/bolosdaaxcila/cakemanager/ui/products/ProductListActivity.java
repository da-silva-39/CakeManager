package com.bolosdaaxcila.cakemanager.ui.products;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.Product;
import com.bolosdaaxcila.cakemanager.data.repository.OrderRepository;
import com.bolosdaaxcila.cakemanager.data.repository.ProductRepository;
import com.bolosdaaxcila.cakemanager.data.repository.RecipeRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private ProductRepository productRepository;
    private RecipeRepository recipeRepository;
    private OrderRepository orderRepository;
    private ProductAdapter adapter;
    private LiveData<List<Product>> current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        productRepository = new ProductRepository(this);
        recipeRepository = new RecipeRepository(this);
        orderRepository = new OrderRepository(this);

        RecyclerView recycler = findViewById(R.id.recyclerProducts);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(new ProductAdapter.OnAction() {
            @Override
            public void onEdit(Product product) {
                Intent i = new Intent(ProductListActivity.this, ProductFormActivity.class);
                i.putExtra("product_id", product.getId());
                startActivity(i);
            }

            @Override
            public void onDelete(Product product) {
                confirmDelete(product);
            }
        });
        recycler.setAdapter(adapter);

        findViewById(R.id.fabAdd).setOnClickListener(v -> startActivity(new Intent(this, ProductFormActivity.class)));

        EditText search = findViewById(R.id.editSearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                observe(productRepository.search(s.toString()));
            }
            @Override public void afterTextChanged(Editable s) { }
        });

        observe(productRepository.getAll());
    }

    private void observe(LiveData<List<Product>> liveData) {
        if (current != null) current.removeObservers(this);
        current = liveData;
        current.observe(this, products -> adapter.setItems(products));
    }

    private void confirmDelete(Product product) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete)
                .setMessage(getString(R.string.confirm_delete_product))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (d, w) -> {
                    orderRepository.countByProduct(product.getId(), orderCount -> {
                        if (orderCount != null && orderCount > 0) {
                            Toast.makeText(this, R.string.error_product_in_order, Toast.LENGTH_LONG).show();
                            return;
                        }
                        recipeRepository.findByProductId(product.getId(), recipe -> {
                            if (recipe != null) {
                                Toast.makeText(this, R.string.error_product_in_recipe, Toast.LENGTH_LONG).show();
                            } else {
                                productRepository.delete(product);
                            }
                        });
                    });
                })
                .show();
    }
}
