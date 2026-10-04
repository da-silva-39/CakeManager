package com.bolosdaaxcila.cakemanager.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.bolosdaaxcila.cakemanager.data.model.OrderItem;

import java.util.List;

@Dao
public interface OrderItemDao {

    @Insert
    long insert(OrderItem item);

    @Query("DELETE FROM order_items WHERE orderId = :orderId")
    void deleteByOrder(long orderId);

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    LiveData<List<OrderItem>> getByOrder(long orderId);

    @Query("SELECT COUNT(*) FROM order_items WHERE productId = :productId")
    int countByProduct(long productId);
}
