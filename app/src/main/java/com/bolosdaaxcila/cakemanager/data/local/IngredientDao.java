package com.bolosdaaxcila.cakemanager.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bolosdaaxcila.cakemanager.data.model.Ingredient;

import java.util.List;

@Dao
public interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Ingredient ingredient);

    @Update
    void update(Ingredient ingredient);

    @Delete
    void delete(Ingredient ingredient);

    @Query("SELECT * FROM ingredients ORDER BY name")
    LiveData<List<Ingredient>> getAll();

    @Query("SELECT * FROM ingredients WHERE id = :id LIMIT 1")
    Ingredient findById(long id);

    @Query("SELECT * FROM ingredients WHERE name LIKE '%' || :query || '%' ORDER BY name")
    LiveData<List<Ingredient>> search(String query);

    @Query("SELECT * FROM ingredients WHERE quantity <= minimumQuantity")
    LiveData<List<Ingredient>> getLowStock();

    @Query("SELECT COUNT(*) FROM ingredients WHERE quantity <= minimumQuantity")
    int countLowStock();

    @Query("SELECT COUNT(*) FROM ingredients")
    int count();
}
