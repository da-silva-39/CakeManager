package com.bolosdaaxcila.cakemanager.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.bolosdaaxcila.cakemanager.data.local.AppDatabase;
import com.bolosdaaxcila.cakemanager.data.local.RecipeDao;
import com.bolosdaaxcila.cakemanager.data.local.RecipeIngredientDao;
import com.bolosdaaxcila.cakemanager.data.model.Recipe;
import com.bolosdaaxcila.cakemanager.data.model.RecipeIngredient;

import java.util.List;

public class RecipeRepository extends BaseRepository {

    private final RecipeDao recipeDao;
    private final RecipeIngredientDao recipeIngredientDao;

    public RecipeRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        recipeDao = db.recipeDao();
        recipeIngredientDao = db.recipeIngredientDao();
    }

    public void insert(Recipe recipe, Callback<Long> callback) {
        runAsync(() -> recipeDao.insert(recipe), callback);
    }

    public void update(Recipe recipe) {
        runAsync(() -> recipeDao.update(recipe));
    }

    public void delete(Recipe recipe) {
        runAsync(() -> {
            recipeIngredientDao.deleteByRecipe(recipe.getId());
            recipeDao.delete(recipe);
        });
    }

    public LiveData<List<Recipe>> getAll() {
        return recipeDao.getAll();
    }

    public LiveData<List<Recipe>> search(String query) {
        return recipeDao.search(query);
    }

    public void findByProductId(long productId, Callback<Recipe> callback) {
        runAsync(() -> recipeDao.findByProductId(productId), callback);
    }

    public void findById(long id, Callback<Recipe> callback) {
        runAsync(() -> recipeDao.findById(id), callback);
    }

    public void insertIngredient(RecipeIngredient ri) {
        runAsync(() -> recipeIngredientDao.insert(ri));
    }

    public LiveData<List<RecipeIngredient>> getIngredients(long recipeId) {
        return recipeIngredientDao.getByRecipe(recipeId);
    }

    public void deleteIngredients(long recipeId) {
        runAsync(() -> recipeIngredientDao.deleteByRecipe(recipeId));
    }

    public void countByIngredient(long ingredientId, Callback<Integer> callback) {
        runAsync(() -> recipeIngredientDao.countByIngredient(ingredientId), callback);
    }
}
