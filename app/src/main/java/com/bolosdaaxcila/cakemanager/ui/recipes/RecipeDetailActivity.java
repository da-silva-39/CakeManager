package com.bolosdaaxcila.cakemanager.ui.recipes;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.Ingredient;
import com.bolosdaaxcila.cakemanager.data.model.RecipeIngredient;
import com.bolosdaaxcila.cakemanager.data.repository.IngredientRepository;
import com.bolosdaaxcila.cakemanager.data.repository.ProductRepository;
import com.bolosdaaxcila.cakemanager.data.repository.RecipeRepository;

import java.util.concurrent.atomic.AtomicInteger;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        long recipeId = getIntent().getLongExtra("recipe_id", -1);

        TextView textName = findViewById(R.id.textRecipeName);
        TextView textProduct = findViewById(R.id.textRecipeProduct);
        TextView textDescription = findViewById(R.id.textRecipeDescription);
        TextView textIngredients = findViewById(R.id.textRecipeIngredients);

        RecipeRepository recipeRepository = new RecipeRepository(this);
        ProductRepository productRepository = new ProductRepository(this);
        IngredientRepository ingredientRepository = new IngredientRepository(this);

        recipeRepository.findById(recipeId, recipe -> {
            if (recipe == null) return;
            textName.setText(recipe.getName());
            textDescription.setText(recipe.getDescription());
            productRepository.findById(recipe.getProductId(), product -> {
                if (product != null) textProduct.setText(product.getName());
            });
        });

        recipeRepository.getIngredients(recipeId).observe(this, items -> {
            if (items == null || items.isEmpty()) {
                textIngredients.setText("-");
                return;
            }
            StringBuilder sb = new StringBuilder();
            AtomicInteger remaining = new AtomicInteger(items.size());
            for (RecipeIngredient ri : items) {
                ingredientRepository.findById(ri.getIngredientId(), (Ingredient ingredient) -> {
                    String name = ingredient != null ? ingredient.getName() : ("#" + ri.getIngredientId());
                    sb.append("- ").append(name).append(": ").append(ri.getQuantity())
                            .append(ingredient != null ? " " + ingredient.getUnit() : "").append("\n");
                    if (remaining.decrementAndGet() == 0) {
                        textIngredients.setText(sb.toString());
                    }
                });
            }
        });
    }
}
