package com.bolosdaaxcila.cakemanager.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bolosdaaxcila.cakemanager.data.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Recipe recipe);

    @Update
    void update(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Query("SELECT * FROM recipes ORDER BY name")
    LiveData<List<Recipe>> getAll();

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    Recipe findById(long id);

    @Query("SELECT * FROM recipes WHERE productId = :productId LIMIT 1")
    Recipe findByProductId(long productId);

    @Query("SELECT * FROM recipes WHERE name LIKE '%' || :query || '%' ORDER BY name")
    LiveData<List<Recipe>> search(String query);
}
