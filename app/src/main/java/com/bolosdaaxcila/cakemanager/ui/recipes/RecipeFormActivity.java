package com.bolosdaaxcila.cakemanager.ui.recipes;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.Ingredient;
import com.bolosdaaxcila.cakemanager.data.model.Product;
import com.bolosdaaxcila.cakemanager.data.model.Recipe;
import com.bolosdaaxcila.cakemanager.data.model.RecipeIngredient;
import com.bolosdaaxcila.cakemanager.data.repository.IngredientRepository;
import com.bolosdaaxcila.cakemanager.data.repository.ProductRepository;
import com.bolosdaaxcila.cakemanager.data.repository.RecipeRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class RecipeFormActivity extends AppCompatActivity {

    public static class ChosenIngredient {
        long ingredientId;
        String name;
        double quantity;

        ChosenIngredient(long ingredientId, String name, double quantity) {
            this.ingredientId = ingredientId;
            this.name = name;
            this.quantity = quantity;
        }
    }

    private TextInputEditText editName;
    private TextInputEditText editDescription;
    private Spinner spinnerProduct;
    private Spinner spinnerIngredient;
    private TextInputEditText editIngredientQuantity;
    private android.widget.TextView textChosen;

    private ProductRepository productRepository;
    private IngredientRepository ingredientRepository;
    private RecipeRepository recipeRepository;

    private final List<Product> products = new ArrayList<>();
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final List<ChosenIngredient> chosen = new ArrayList<>();
    private long recipeId = -1;
    private long selectedProductId = -1;
    private long selectedIngredientId = -1;
    private Recipe pendingRecipe;
    private boolean productsReady;
    private boolean ingredientsReady;
    private boolean editPopulated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);

        editName = findViewById(R.id.editName);
        editDescription = findViewById(R.id.editDescription);
        spinnerProduct = findViewById(R.id.spinnerProduct);
        spinnerIngredient = findViewById(R.id.spinnerIngredient);
        editIngredientQuantity = findViewById(R.id.editIngredientQuantity);
        textChosen = findViewById(R.id.textChosen);
        Button btnAddIngredient = findViewById(R.id.btnAddIngredient);
        Button btnSave = findViewById(R.id.btnSave);

        productRepository = new ProductRepository(this);
        ingredientRepository = new IngredientRepository(this);
        recipeRepository = new RecipeRepository(this);
        recipeId = getIntent().getLongExtra("recipe_id", -1);

        productRepository.getAll().observe(this, list -> {
            products.clear();
            if (list != null) products.addAll(list);
            productsReady = true;
            populateEditIfReady();
            List<String> names = new ArrayList<>();
            for (Product p : products) names.add(p.getName());
            spinnerProduct.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names));
            if (((ArrayAdapter<?>) spinnerProduct.getAdapter()) != null) {
                ((ArrayAdapter<?>) spinnerProduct.getAdapter()).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }
        });

        ingredientRepository.getAll().observe(this, list -> {
            ingredients.clear();
            if (list != null) ingredients.addAll(list);
            ingredientsReady = true;
            populateEditIfReady();
            List<String> names = new ArrayList<>();
            for (Ingredient i : ingredients) names.add(i.getName());
            spinnerIngredient.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names));
            ((ArrayAdapter<?>) spinnerIngredient.getAdapter()).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        });

        spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                if (position >= 0 && position < products.size()) selectedProductId = products.get(position).getId();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinnerIngredient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                if (position >= 0 && position < ingredients.size()) selectedIngredientId = ingredients.get(position).getId();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnAddIngredient.setOnClickListener(v -> addIngredient());
        btnSave.setOnClickListener(v -> save());

        if (recipeId != -1) {
            recipeRepository.findById(recipeId, recipe -> {
                pendingRecipe = recipe;
                populateEditIfReady();
            });
            recipeRepository.getIngredients(recipeId).observe(this, list -> {
                chosen.clear();
                if (list != null) {
                    for (RecipeIngredient ri : list) {
                        String name = "?";
                        for (Ingredient i : ingredients) {
                            if (i.getId() == ri.getIngredientId()) { name = i.getName(); break; }
                        }
                        chosen.add(new ChosenIngredient(ri.getIngredientId(), name, ri.getQuantity()));
                    }
                }
                refreshChosen();
            });
        }
    }

    private void populateEditIfReady() {
        if (editPopulated || pendingRecipe == null || !productsReady || !ingredientsReady) return;
        editPopulated = true;
        editName.setText(pendingRecipe.getName());
        editDescription.setText(pendingRecipe.getDescription());
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == pendingRecipe.getProductId()) {
                spinnerProduct.setSelection(i);
                break;
            }
        }
    }

    private void addIngredient() {
        if (selectedIngredientId == -1) {
            Toast.makeText(this, R.string.error_select_ingredient, Toast.LENGTH_LONG).show();
            return;
        }
        String qtyText = editIngredientQuantity.getText() != null ? editIngredientQuantity.getText().toString().trim().replace(',', '.') : "";
        double qty;
        try {
            qty = Double.parseDouble(qtyText);
        } catch (NumberFormatException e) {
            editIngredientQuantity.setError(getString(R.string.error_invalid_quantity));
            return;
        }
        if (qty <= 0) {
            editIngredientQuantity.setError(getString(R.string.error_invalid_quantity));
            return;
        }
        String name = "?";
        for (Ingredient i : ingredients) {
            if (i.getId() == selectedIngredientId) { name = i.getName(); break; }
        }
        chosen.add(new ChosenIngredient(selectedIngredientId, name, qty));
        editIngredientQuantity.setText("");
        refreshChosen();
    }

    private void refreshChosen() {
        StringBuilder sb = new StringBuilder();
        for (ChosenIngredient c : chosen) {
            sb.append("- ").append(c.name).append(": ").append(c.quantity).append("\n");
        }
        textChosen.setText(sb.toString());
    }

    private void save() {
        String name = editName.getText() != null ? editName.getText().toString().trim() : "";
        String description = editDescription.getText() != null ? editDescription.getText().toString().trim() : "";

        if (TextUtils.isEmpty(name)) { editName.setError(getString(R.string.error_name_required)); return; }
        if (selectedProductId == -1) { Toast.makeText(this, R.string.error_select_product, Toast.LENGTH_LONG).show(); return; }
        if (chosen.isEmpty()) { Toast.makeText(this, R.string.error_recipe_needs_ingredients, Toast.LENGTH_LONG).show(); return; }

        if (recipeId == -1) {
            long productId = selectedProductId;
            recipeRepository.insert(new Recipe(productId, name, description), id -> {
                if (id != null) {
                    for (ChosenIngredient c : chosen) {
                        recipeRepository.insertIngredient(new RecipeIngredient(id, c.ingredientId, c.quantity));
                    }
                }
                finish();
            });
        } else {
            Recipe recipe = new Recipe(selectedProductId, name, description);
            recipe.setId(recipeId);
            recipeRepository.update(recipe);
            long rid = recipeId;
            recipeRepository.deleteIngredients(rid);
            for (ChosenIngredient c : chosen) {
                recipeRepository.insertIngredient(new RecipeIngredient(rid, c.ingredientId, c.quantity));
            }
            finish();
        }
    }
}
