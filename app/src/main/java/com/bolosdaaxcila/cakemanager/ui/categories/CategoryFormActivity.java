package com.bolosdaaxcila.cakemanager.ui.categories;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.Category;
import com.bolosdaaxcila.cakemanager.data.repository.CategoryRepository;
import com.google.android.material.textfield.TextInputEditText;

public class CategoryFormActivity extends AppCompatActivity {

    private TextInputEditText editName;
    private TextInputEditText editDescription;
    private CategoryRepository repository;
    private long categoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_form);

        editName = findViewById(R.id.editName);
        editDescription = findViewById(R.id.editDescription);
        Button btnSave = findViewById(R.id.btnSave);

        repository = new CategoryRepository(this);
        categoryId = getIntent().getLongExtra("category_id", -1);

        if (categoryId != -1) {
            repository.findById(categoryId, category -> {
                if (category != null) {
                    editName.setText(category.getName());
                    editDescription.setText(category.getDescription());
                }
            });
        }

        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        String name = editName.getText() != null ? editName.getText().toString().trim() : "";
        String description = editDescription.getText() != null ? editDescription.getText().toString().trim() : "";

        if (TextUtils.isEmpty(name)) {
            editName.setError(getString(R.string.error_name_required));
            return;
        }

        if (categoryId == -1) {
            repository.insert(new Category(name, description), id -> finish());
        } else {
            repository.findById(categoryId, category -> {
                if (category != null) {
                    category.setName(name);
                    category.setDescription(description);
                    repository.update(category);
                }
                finish();
            });
        }
    }
}
