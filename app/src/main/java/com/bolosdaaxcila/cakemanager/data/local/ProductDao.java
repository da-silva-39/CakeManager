package com.bolosdaaxcila.cakemanager.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bolosdaaxcila.cakemanager.data.model.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM products ORDER BY name")
    LiveData<List<Product>> getAll();

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    Product findById(long id);

    @Query("SELECT * FROM products WHERE categoryId = :categoryId ORDER BY name")
    LiveData<List<Product>> getByCategory(long categoryId);

    @Query("SELECT COUNT(*) FROM products WHERE categoryId = :categoryId")
    int countByCategory(long categoryId);

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' ORDER BY name")
    LiveData<List<Product>> search(String query);

    @Query("SELECT COUNT(*) FROM products")
    int count();
}
