package com.bolosdaaxcila.cakemanager.data.repository;

import android.content.Context;

import com.bolosdaaxcila.cakemanager.data.local.AppDatabase;
import com.bolosdaaxcila.cakemanager.data.model.OrderStatus;

public class DashboardRepository extends BaseRepository {

    public static class Totals {
        public int products;
        public int categories;
        public int ingredients;
        public int orders;
        public int pendingOrders;
        public int preparingOrders;
        public int readyOrders;
        public int lowStockIngredients;
    }

    private final AppDatabase db;

    public DashboardRepository(Context context) {
        db = AppDatabase.getInstance(context);
    }

    public void loadTotals(Callback<Totals> callback) {
        runAsync(() -> {
            Totals t = new Totals();
            t.products = db.productDao().count();
            t.categories = db.categoryDao().count();
            t.ingredients = db.ingredientDao().count();
            t.orders = db.orderDao().count();
            t.pendingOrders = db.orderDao().countByStatus(OrderStatus.PENDENTE.name());
            t.preparingOrders = db.orderDao().countByStatus(OrderStatus.EM_PREPARACAO.name());
            t.readyOrders = db.orderDao().countByStatus(OrderStatus.PRONTA.name());
            t.lowStockIngredients = db.ingredientDao().countLowStock();
            return t;
        }, callback);
    }
}
