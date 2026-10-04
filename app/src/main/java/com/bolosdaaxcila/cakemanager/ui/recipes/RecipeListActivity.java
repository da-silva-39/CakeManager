package com.bolosdaaxcila.cakemanager.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.Recipe;
import com.bolosdaaxcila.cakemanager.data.repository.RecipeRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    private RecipeRepository recipeRepository;
    private RecipeAdapter adapter;
    private LiveData<List<Recipe>> current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        recipeRepository = new RecipeRepository(this);

        RecyclerView recycler = findViewById(R.id.recyclerRecipes);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(new RecipeAdapter.OnAction() {
            @Override
            public void onEdit(Recipe recipe) {
                Intent i = new Intent(RecipeListActivity.this, RecipeFormActivity.class);
                i.putExtra("recipe_id", recipe.getId());
                startActivity(i);
            }

            @Override
            public void onOpen(Recipe recipe) {
                Intent i = new Intent(RecipeListActivity.this, RecipeDetailActivity.class);
                i.putExtra("recipe_id", recipe.getId());
                startActivity(i);
            }

            @Override
            public void onDelete(Recipe recipe) {
                new AlertDialog.Builder(RecipeListActivity.this)
                        .setTitle(R.string.delete)
                        .setMessage(getString(R.string.confirm_delete_recipe))
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.delete, (d, w) -> recipeRepository.delete(recipe))
                        .show();
            }
        });
        recycler.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> startActivity(new Intent(this, RecipeFormActivity.class)));

        EditText search = findViewById(R.id.editSearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                observe(recipeRepository.search(s.toString()));
            }
            @Override public void afterTextChanged(Editable s) { }
        });

        observe(recipeRepository.getAll());
    }

    private void observe(LiveData<List<Recipe>> liveData) {
        if (current != null) current.removeObservers(this);
        current = liveData;
        current.observe(this, recipes -> adapter.setItems(recipes));
    }
}
