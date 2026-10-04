package com.bolosdaaxcila.cakemanager.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bolosdaaxcila.cakemanager.data.model.Category;
import com.bolosdaaxcila.cakemanager.data.model.Ingredient;
import com.bolosdaaxcila.cakemanager.data.model.Order;
import com.bolosdaaxcila.cakemanager.data.model.OrderItem;
import com.bolosdaaxcila.cakemanager.data.model.Product;
import com.bolosdaaxcila.cakemanager.data.model.Recipe;
import com.bolosdaaxcila.cakemanager.data.model.RecipeIngredient;
import com.bolosdaaxcila.cakemanager.data.model.StockMovement;
import com.bolosdaaxcila.cakemanager.data.model.User;

@Database(entities = {User.class, Category.class, Product.class, Ingredient.class,
        Recipe.class, RecipeIngredient.class, StockMovement.class, Order.class, OrderItem.class},
        version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract ProductDao productDao();
    public abstract IngredientDao ingredientDao();
    public abstract RecipeDao recipeDao();
    public abstract RecipeIngredientDao recipeIngredientDao();
    public abstract StockMovementDao stockMovementDao();
    public abstract OrderDao orderDao();
    public abstract OrderItemDao orderItemDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "cakemanager_db")
                            .build();
                }
            }
        }
        return instance;
    }
}
