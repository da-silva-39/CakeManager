package com.bolosdaaxcila.cakemanager.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.bolosdaaxcila.cakemanager.data.local.AppDatabase;
import com.bolosdaaxcila.cakemanager.data.local.OrderDao;
import com.bolosdaaxcila.cakemanager.data.local.OrderItemDao;
import com.bolosdaaxcila.cakemanager.data.model.Order;
import com.bolosdaaxcila.cakemanager.data.model.OrderItem;

import java.util.List;

public class OrderRepository extends BaseRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        orderDao = db.orderDao();
        orderItemDao = db.orderItemDao();
    }

    public void insert(Order order, Callback<Long> callback) {
        runAsync(() -> orderDao.insert(order), callback);
    }

    public void update(Order order) {
        runAsync(() -> orderDao.update(order));
    }

    public void delete(Order order) {
        runAsync(() -> {
            orderItemDao.deleteByOrder(order.getId());
            orderDao.delete(order);
        });
    }

    public LiveData<List<Order>> getAll() {
        return orderDao.getAll();
    }

    public LiveData<List<Order>> search(String query) {
        return orderDao.search(query);
    }

    public void findById(long id, Callback<Order> callback) {
        runAsync(() -> orderDao.findById(id), callback);
    }

    public void insertItem(OrderItem item) {
        runAsync(() -> orderItemDao.insert(item));
    }

    public LiveData<List<OrderItem>> getItems(long orderId) {
        return orderItemDao.getByOrder(orderId);
    }

    public void clearItems(long orderId) {
        runAsync(() -> orderItemDao.deleteByOrder(orderId));
    }

    public void countByProduct(long productId, Callback<Integer> callback) {
        runAsync(() -> orderItemDao.countByProduct(productId), callback);
    }
}
