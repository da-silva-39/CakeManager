package com.bolosdaaxcila.cakemanager.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.bolosdaaxcila.cakemanager.data.local.AppDatabase;
import com.bolosdaaxcila.cakemanager.data.local.IngredientDao;
import com.bolosdaaxcila.cakemanager.data.local.StockMovementDao;
import com.bolosdaaxcila.cakemanager.data.model.Ingredient;
import com.bolosdaaxcila.cakemanager.data.model.StockMovement;

import java.util.List;

public class StockMovementRepository extends BaseRepository {

    private final StockMovementDao movementDao;
    private final IngredientDao ingredientDao;

    public StockMovementRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        movementDao = db.stockMovementDao();
        ingredientDao = db.ingredientDao();
    }


    public LiveData<List<StockMovement>> getAll() {
        return movementDao.getAll();
    }

    public LiveData<List<StockMovement>> getByIngredient(long ingredientId) {
        return movementDao.getByIngredient(ingredientId);
    }

    public void countByIngredient(long ingredientId, Callback<Integer> callback) {
        runAsync(() -> movementDao.countByIngredient(ingredientId), callback);
    }

    public void registerEntry(long ingredientId, double quantity, String description, Callback<Boolean> callback) {
        runAsync(() -> {
            Ingredient ing = ingredientDao.findById(ingredientId);
            if (ing == null || quantity <= 0) return false;
            ing.setQuantity(ing.getQuantity() + quantity);
            ingredientDao.update(ing);
            movementDao.insert(new StockMovement(ingredientId, StockMovement.TYPE_ENTRY, quantity, System.currentTimeMillis(), description));
            return true;
        }, callback);
    }

    public void registerExit(long ingredientId, double quantity, String description, Callback<Boolean> callback) {
        runAsync(() -> {
            Ingredient ing = ingredientDao.findById(ingredientId);
            if (ing == null || quantity <= 0) return false;
            if (ing.getQuantity() - quantity < 0) return false;
            ing.setQuantity(ing.getQuantity() - quantity);
            ingredientDao.update(ing);
            movementDao.insert(new StockMovement(ingredientId, StockMovement.TYPE_EXIT, quantity, System.currentTimeMillis(), description));
            return true;
        }, callback);
    }
}
