package com.bolosdaaxcila.cakemanager.ui.categories;

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
import com.bolosdaaxcila.cakemanager.data.model.Category;
import com.bolosdaaxcila.cakemanager.data.repository.CategoryRepository;
import com.bolosdaaxcila.cakemanager.data.repository.ProductRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CategoryListActivity extends AppCompatActivity {

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private CategoryAdapter adapter;
    private LiveData<List<Category>> current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        categoryRepository = new CategoryRepository(this);
        productRepository = new ProductRepository(this);

        RecyclerView recycler = findViewById(R.id.recyclerCategories);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter(new CategoryAdapter.OnAction() {
            @Override
            public void onEdit(Category category) {
                Intent i = new Intent(CategoryListActivity.this, CategoryFormActivity.class);
                i.putExtra("category_id", category.getId());
                startActivity(i);
            }

            @Override
            public void onDelete(Category category) {
                confirmDelete(category);
            }
        });
        recycler.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> startActivity(new Intent(this, CategoryFormActivity.class)));

        EditText search = findViewById(R.id.editSearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                observe(categoryRepository.search(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        observe(categoryRepository.getAll());
    }

    private void observe(LiveData<List<Category>> liveData) {
        if (current != null) current.removeObservers(this);
        current = liveData;
        current.observe(this, categories -> adapter.setItems(categories));
    }

    private void confirmDelete(Category category) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete)
                .setMessage(getString(R.string.confirm_delete_category))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (d, w) -> {
                    productRepository.countByCategory(category.getId(), count -> {
                        if (count != null && count > 0) {
                            Toast.makeText(this, R.string.error_category_in_use, Toast.LENGTH_LONG).show();
                        } else {
                            categoryRepository.delete(category);
                        }
                    });
                })
                .show();
    }
}
