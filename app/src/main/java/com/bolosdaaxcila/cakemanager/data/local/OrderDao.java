package com.bolosdaaxcila.cakemanager.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bolosdaaxcila.cakemanager.data.model.Order;

import java.util.List;

@Dao
public interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Order order);

    @Update
    void update(Order order);

    @Delete
    void delete(Order order);

    @Query("SELECT * FROM orders ORDER BY date DESC, id DESC")
    LiveData<List<Order>> getAll();

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    Order findById(long id);

    @Query("SELECT * FROM orders WHERE customerName LIKE '%' || :query || '%' OR CAST(id AS TEXT) LIKE '%' || :query || '%' ORDER BY date DESC")
    LiveData<List<Order>> search(String query);

    @Query("SELECT COUNT(*) FROM orders")
    int count();

    @Query("SELECT COUNT(*) FROM orders WHERE status = :status")
    int countByStatus(String status);
}
