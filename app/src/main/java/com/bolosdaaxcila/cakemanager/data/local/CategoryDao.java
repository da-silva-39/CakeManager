package com.bolosdaaxcila.cakemanager.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bolosdaaxcila.cakemanager.data.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    @Query("SELECT * FROM categories ORDER BY name")
    LiveData<List<Category>> getAll();

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    Category findById(long id);

    @Query("SELECT * FROM categories WHERE name LIKE '%' || :query || '%' ORDER BY name")
    LiveData<List<Category>> search(String query);

    @Query("SELECT COUNT(*) FROM categories")
    int count();
}
