package com.bolosdaaxcila.cakemanager.ui.stock;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.Ingredient;
import com.bolosdaaxcila.cakemanager.data.model.StockMovement;
import com.bolosdaaxcila.cakemanager.data.repository.IngredientRepository;
import com.bolosdaaxcila.cakemanager.data.repository.StockMovementRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class StockMovementFormActivity extends AppCompatActivity {

    private Spinner spinnerIngredient;
    private RadioGroup radioType;
    private TextInputEditText editQuantity;
    private TextInputEditText editDescription;

    private IngredientRepository ingredientRepository;
    private StockMovementRepository stockRepository;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private long selectedIngredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_form);

        spinnerIngredient = findViewById(R.id.spinnerIngredient);
        radioType = findViewById(R.id.radioType);
        editQuantity = findViewById(R.id.editQuantity);
        editDescription = findViewById(R.id.editDescription);
        Button btnSave = findViewById(R.id.btnSave);

        ingredientRepository = new IngredientRepository(this);
        stockRepository = new StockMovementRepository(this);

        ingredientRepository.getAll().observe(this, list -> {
            ingredients.clear();
            if (list != null) ingredients.addAll(list);
            List<String> names = new ArrayList<>();
            for (Ingredient i : ingredients) names.add(i.getName());
            spinnerIngredient.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names));
            ((ArrayAdapter<?>) spinnerIngredient.getAdapter()).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        });

        spinnerIngredient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                if (position >= 0 && position < ingredients.size()) selectedIngredientId = ingredients.get(position).getId();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        if (selectedIngredientId == -1) {
            Toast.makeText(this, R.string.error_select_ingredient, Toast.LENGTH_LONG).show();
            return;
        }
        String qtyText = editQuantity.getText() != null ? editQuantity.getText().toString().trim().replace(',', '.') : "";
        double qty;
        try {
            qty = Double.parseDouble(qtyText);
        } catch (NumberFormatException e) {
            editQuantity.setError(getString(R.string.error_invalid_quantity));
            return;
        }
        if (qty <= 0) {
            editQuantity.setError(getString(R.string.error_invalid_quantity));
            return;
        }
        String description = editDescription.getText() != null ? editDescription.getText().toString().trim() : "";

        boolean isEntry = radioType.getCheckedRadioButtonId() == R.id.radioEntry;
        StockMovementRepository.Callback<Boolean> cb = success -> {
            if (success != null && success) {
                Toast.makeText(this, R.string.movement_saved, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, isEntry ? R.string.error_not_possible_save : R.string.error_insufficient_stock, Toast.LENGTH_LONG).show();
            }
        };

        if (isEntry) {
            stockRepository.registerEntry(selectedIngredientId, qty, description, cb);
        } else {
            stockRepository.registerExit(selectedIngredientId, qty, description, cb);
        }
    }
}
