package com.bolosdaaxcila.cakemanager.ui.ingredients;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.Ingredient;
import com.bolosdaaxcila.cakemanager.data.repository.IngredientRepository;
import com.google.android.material.textfield.TextInputEditText;

public class IngredientFormActivity extends AppCompatActivity {

    private TextInputEditText editName;
    private TextInputEditText editQuantity;
    private TextInputEditText editMinimum;
    private TextInputEditText editUnit;
    private IngredientRepository repository;
    private long ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_form);

        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editMinimum = findViewById(R.id.editMinimum);
        editUnit = findViewById(R.id.editUnit);
        Button btnSave = findViewById(R.id.btnSave);

        repository = new IngredientRepository(this);
        ingredientId = getIntent().getLongExtra("ingredient_id", -1);

        if (ingredientId != -1) {
            repository.findById(ingredientId, ingredient -> {
                if (ingredient != null) {
                    editName.setText(ingredient.getName());
                    editQuantity.setText(String.valueOf(ingredient.getQuantity()));
                    editMinimum.setText(String.valueOf(ingredient.getMinimumQuantity()));
                    editUnit.setText(ingredient.getUnit());
                }
            });
        }

        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        String name = editName.getText() != null ? editName.getText().toString().trim() : "";
        String unit = editUnit.getText() != null ? editUnit.getText().toString().trim() : "";

        if (TextUtils.isEmpty(name)) { editName.setError(getString(R.string.error_name_required)); return; }
        if (TextUtils.isEmpty(unit)) { editUnit.setError(getString(R.string.error_unit_required)); return; }

        Double quantity = parse(editQuantity.getText());
        Double minimum = parse(editMinimum.getText());
        if (quantity == null || quantity < 0) { editQuantity.setError(getString(R.string.error_invalid_quantity)); return; }
        if (minimum == null || minimum < 0) { editMinimum.setError(getString(R.string.error_invalid_min_quantity)); return; }

        if (ingredientId == -1) {
            repository.insert(new Ingredient(name, quantity, minimum, unit), id -> finish());
        } else {
            double q = quantity; double m = minimum;
            repository.findById(ingredientId, ingredient -> {
                if (ingredient != null) {
                    ingredient.setName(name);
                    ingredient.setQuantity(q);
                    ingredient.setMinimumQuantity(m);
                    ingredient.setUnit(unit);
                    repository.update(ingredient);
                }
                finish();
            });
        }
    }

    private Double parse(CharSequence text) {
        if (text == null) return null;
        String s = text.toString().trim().replace(',', '.');
        if (s.isEmpty()) return null;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
