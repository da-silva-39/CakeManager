package com.bolosdaaxcila.cakemanager.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.bolosdaaxcila.cakemanager.data.model.StockMovement;

import java.util.List;

@Dao
public interface StockMovementDao {

    @Insert
    long insert(StockMovement movement);

    @Query("SELECT * FROM stock_movements ORDER BY date DESC, id DESC")
    LiveData<List<StockMovement>> getAll();

    @Query("SELECT * FROM stock_movements WHERE ingredientId = :ingredientId ORDER BY date DESC, id DESC")
    LiveData<List<StockMovement>> getByIngredient(long ingredientId);

    @Query("SELECT COUNT(*) FROM stock_movements WHERE ingredientId = :ingredientId")
    int countByIngredient(long ingredientId);
}
