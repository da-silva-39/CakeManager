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

    public void seedDemoData(Callback<Boolean> callback) {
        runAsync(() -> {
            if (db.categoryDao().count() > 0) return true;
            long catBolos = db.categoryDao().insert(new com.bolosdaaxcila.cakemanager.data.model.Category("Bolos", "Bolos variados"));
            long catDoces = db.categoryDao().insert(new com.bolosdaaxcila.cakemanager.data.model.Category("Doces", "Doces e sobremesas"));
            long farinha = db.ingredientDao().insert(new com.bolosdaaxcila.cakemanager.data.model.Ingredient("Farinha", 5000, 1000, "g"));
            long acucar = db.ingredientDao().insert(new com.bolosdaaxcila.cakemanager.data.model.Ingredient("Açúcar", 3000, 500, "g"));
            long ovos = db.ingredientDao().insert(new com.bolosdaaxcila.cakemanager.data.model.Ingredient("Ovos", 30, 10, "unid"));
            long chocolate = db.ingredientDao().insert(new com.bolosdaaxcila.cakemanager.data.model.Ingredient("Chocolate", 800, 300, "g"));
            long boloChoc = db.productDao().insert(new com.bolosdaaxcila.cakemanager.data.model.Product("Bolo de Chocolate", "Bolo húmido de chocolate", 1000.0, catBolos));
            db.productDao().insert(new com.bolosdaaxcila.cakemanager.data.model.Product("Cupcake", "Cupcake de baunilha", 100.0, catDoces));
            db.productDao().insert(new com.bolosdaaxcila.cakemanager.data.model.Product("Tarte de Maçã", "Tarte caseira", 800.0, catDoces));
            long receita = db.recipeDao().insert(new com.bolosdaaxcila.cakemanager.data.model.Recipe(boloChoc, "Receita do Bolo de Chocolate", "Bate tudo e leva ao forno"));
            db.recipeIngredientDao().insert(new com.bolosdaaxcila.cakemanager.data.model.RecipeIngredient(receita, farinha, 500));
            db.recipeIngredientDao().insert(new com.bolosdaaxcila.cakemanager.data.model.RecipeIngredient(receita, acucar, 300));
            db.recipeIngredientDao().insert(new com.bolosdaaxcila.cakemanager.data.model.RecipeIngredient(receita, ovos, 4));
            db.recipeIngredientDao().insert(new com.bolosdaaxcila.cakemanager.data.model.RecipeIngredient(receita, chocolate, 200));
            db.stockMovementDao().insert(new com.bolosdaaxcila.cakemanager.data.model.StockMovement(farinha, com.bolosdaaxcila.cakemanager.data.model.StockMovement.TYPE_ENTRY, 5000, System.currentTimeMillis(), "Compra mensal"));
            long orderId = db.orderDao().insert(new com.bolosdaaxcila.cakemanager.data.model.Order("Ana Mussa", "841234567", System.currentTimeMillis(), com.bolosdaaxcila.cakemanager.data.model.OrderStatus.PENDENTE.name(), 2000.0));
            db.orderItemDao().insert(new com.bolosdaaxcila.cakemanager.data.model.OrderItem(orderId, boloChoc, 2, 1000.0, 2000.0));
            return true;
        }, callback);
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
