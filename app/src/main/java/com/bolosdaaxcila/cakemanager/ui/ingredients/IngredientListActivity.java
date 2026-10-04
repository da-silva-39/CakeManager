package com.bolosdaaxcila.cakemanager.ui.ingredients;

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
import com.bolosdaaxcila.cakemanager.data.model.Ingredient;
import com.bolosdaaxcila.cakemanager.data.repository.IngredientRepository;
import com.bolosdaaxcila.cakemanager.data.repository.RecipeRepository;
import com.bolosdaaxcila.cakemanager.data.repository.StockMovementRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class IngredientListActivity extends AppCompatActivity {

    private IngredientRepository ingredientRepository;
    private RecipeRepository recipeRepository;
    private StockMovementRepository stockRepository;
    private IngredientAdapter adapter;
    private LiveData<List<Ingredient>> current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);

        ingredientRepository = new IngredientRepository(this);
        recipeRepository = new RecipeRepository(this);
        stockRepository = new StockMovementRepository(this);

        RecyclerView recycler = findViewById(R.id.recyclerIngredients);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IngredientAdapter(new IngredientAdapter.OnAction() {
            @Override
            public void onEdit(Ingredient ingredient) {
                Intent i = new Intent(IngredientListActivity.this, IngredientFormActivity.class);
                i.putExtra("ingredient_id", ingredient.getId());
                startActivity(i);
            }

            @Override
            public void onDelete(Ingredient ingredient) {
                confirmDelete(ingredient);
            }
        });
        recycler.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> startActivity(new Intent(this, IngredientFormActivity.class)));

        EditText search = findViewById(R.id.editSearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                observe(ingredientRepository.search(s.toString()));
            }
            @Override public void afterTextChanged(Editable s) { }
        });

        observe(ingredientRepository.getAll());
    }

    private void observe(LiveData<List<Ingredient>> liveData) {
        if (current != null) current.removeObservers(this);
        current = liveData;
        current.observe(this, ingredients -> adapter.setItems(ingredients));
    }

    private void confirmDelete(Ingredient ingredient) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete)
                .setMessage(getString(R.string.confirm_delete_ingredient))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (d, w) -> {
                    recipeRepository.countByIngredient(ingredient.getId(), recipeCount -> {
                        if (recipeCount != null && recipeCount > 0) {
                            Toast.makeText(this, R.string.error_ingredient_in_recipe, Toast.LENGTH_LONG).show();
                            return;
                        }
                        stockRepository.countByIngredient(ingredient.getId(), movementCount -> {
                            if (movementCount != null && movementCount > 0) {
                                Toast.makeText(this, R.string.error_ingredient_has_movements, Toast.LENGTH_LONG).show();
                            } else {
                                ingredientRepository.delete(ingredient);
                            }
                        });
                    });
                })
                .show();
    }
}
