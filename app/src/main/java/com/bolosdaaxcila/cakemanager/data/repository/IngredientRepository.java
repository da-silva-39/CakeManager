package com.bolosdaaxcila.cakemanager.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.bolosdaaxcila.cakemanager.data.local.AppDatabase;
import com.bolosdaaxcila.cakemanager.data.local.IngredientDao;
import com.bolosdaaxcila.cakemanager.data.model.Ingredient;

import java.util.List;

public class IngredientRepository extends BaseRepository {

    private final IngredientDao ingredientDao;

    public IngredientRepository(Context context) {
        ingredientDao = AppDatabase.getInstance(context).ingredientDao();
    }

    public void insert(Ingredient ingredient, Callback<Long> callback) {
        runAsync(() -> ingredientDao.insert(ingredient), callback);
    }

    public void update(Ingredient ingredient) {
        runAsync(() -> ingredientDao.update(ingredient));
    }

    public void delete(Ingredient ingredient) {
        runAsync(() -> ingredientDao.delete(ingredient));
    }

    public LiveData<List<Ingredient>> getAll() {
        return ingredientDao.getAll();
    }

    public LiveData<List<Ingredient>> search(String query) {
        return ingredientDao.search(query);
    }

    public LiveData<List<Ingredient>> getLowStock() {
        return ingredientDao.getLowStock();
    }

    public void findById(long id, Callback<Ingredient> callback) {
        runAsync(() -> ingredientDao.findById(id), callback);
    }
}
