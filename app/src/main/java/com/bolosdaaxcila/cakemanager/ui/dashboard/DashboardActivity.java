package com.bolosdaaxcila.cakemanager.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bolosdaaxcila.cakemanager.R;

import androidx.appcompat.app.AppCompatActivity;

import com.bolosdaaxcila.cakemanager.data.repository.DashboardRepository;
import com.bolosdaaxcila.cakemanager.ui.auth.LoginActivity;
import com.bolosdaaxcila.cakemanager.utils.SessionManager;

public class DashboardActivity extends AppCompatActivity {

    private DashboardRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        SessionManager session = new SessionManager(this);
        TextView welcome = findViewById(R.id.textWelcome);
        welcome.setText(getString(R.string.welcome_user, session.getUserName()));

        repository = new DashboardRepository(this);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setCardClick(R.id.valueProducts, com.bolosdaaxcila.cakemanager.ui.products.ProductListActivity.class);
        setCardClick(R.id.valueCategories, com.bolosdaaxcila.cakemanager.ui.categories.CategoryListActivity.class);
        setCardClick(R.id.valueIngredients, com.bolosdaaxcila.cakemanager.ui.ingredients.IngredientListActivity.class);
        setCardClick(R.id.valueOrders, com.bolosdaaxcila.cakemanager.ui.orders.OrderListActivity.class);
        setCardClick(R.id.valuePending, com.bolosdaaxcila.cakemanager.ui.orders.OrderListActivity.class);
        setCardClick(R.id.valuePreparing, com.bolosdaaxcila.cakemanager.ui.orders.OrderListActivity.class);
        setCardClick(R.id.valueReady, com.bolosdaaxcila.cakemanager.ui.orders.OrderListActivity.class);
        setCardClick(R.id.valueLowStock, com.bolosdaaxcila.cakemanager.ui.stock.StockActivity.class);

        Button btnCategories = findViewById(R.id.btnCategories);
        btnCategories.setOnClickListener(v -> startActivity(new Intent(this, com.bolosdaaxcila.cakemanager.ui.categories.CategoryListActivity.class)));

        Button btnProducts = findViewById(R.id.btnProducts);
        btnProducts.setOnClickListener(v -> startActivity(new Intent(this, com.bolosdaaxcila.cakemanager.ui.products.ProductListActivity.class)));

        Button btnIngredients = findViewById(R.id.btnIngredients);
        btnIngredients.setOnClickListener(v -> startActivity(new Intent(this, com.bolosdaaxcila.cakemanager.ui.ingredients.IngredientListActivity.class)));

        Button btnRecipes = findViewById(R.id.btnRecipes);
        btnRecipes.setOnClickListener(v -> startActivity(new Intent(this, com.bolosdaaxcila.cakemanager.ui.recipes.RecipeListActivity.class)));

        Button btnStock = findViewById(R.id.btnStock);
        btnStock.setOnClickListener(v -> startActivity(new Intent(this, com.bolosdaaxcila.cakemanager.ui.stock.StockActivity.class)));

        Button btnOrders = findViewById(R.id.btnOrders);
        btnOrders.setOnClickListener(v -> startActivity(new Intent(this, com.bolosdaaxcila.cakemanager.ui.orders.OrderListActivity.class)));

        Button btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTotals();
    }

    private void loadTotals() {
        repository.loadTotals(totals -> {
            if (totals == null) return;
            if (totals.products == 0 && totals.orders == 0 && totals.ingredients == 0) {
                repository.seedDemoData(ok -> loadTotals());
                return;
            }
            setText(R.id.valueProducts, totals.products);
            setText(R.id.valueCategories, totals.categories);
            setText(R.id.valueIngredients, totals.ingredients);
            setText(R.id.valueOrders, totals.orders);
            setText(R.id.valuePending, totals.pendingOrders);
            setText(R.id.valuePreparing, totals.preparingOrders);
            setText(R.id.valueReady, totals.readyOrders);
            setText(R.id.valueLowStock, totals.lowStockIngredients);
        });
    }

    private void setCardClick(int valueId, Class<?> target) {
        android.view.View view = findViewById(valueId);
        if (view == null) return;
        android.view.View card = (android.view.View) view.getParent().getParent();
        card.setOnClickListener(v -> startActivity(new Intent(this, target)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_products) startActivity(new Intent(this, com.bolosdaaxcila.cakemanager.ui.products.ProductListActivity.class));
        else if (id == R.id.menu_categories) startActivity(new Intent(this, com.bolosdaaxcila.cakemanager.ui.categories.CategoryListActivity.class));
        else if (id == R.id.menu_ingredients) startActivity(new Intent(this, com.bolosdaaxcila.cakemanager.ui.ingredients.IngredientListActivity.class));
        else if (id == R.id.menu_recipes) startActivity(new Intent(this, com.bolosdaaxcila.cakemanager.ui.recipes.RecipeListActivity.class));
        else if (id == R.id.menu_stock) startActivity(new Intent(this, com.bolosdaaxcila.cakemanager.ui.stock.StockActivity.class));
        else if (id == R.id.menu_orders) startActivity(new Intent(this, com.bolosdaaxcila.cakemanager.ui.orders.OrderListActivity.class));
        else if (id == R.id.menu_profile) startActivity(new Intent(this, ProfileActivity.class));
        else if (id == R.id.menu_seed) {
            repository.seedDemoData(ok -> {
                android.widget.Toast.makeText(this, ok != null && ok ? "Dados carregados." : "Erro ao carregar dados.", android.widget.Toast.LENGTH_SHORT).show();
                loadTotals();
            });
        }
        else if (id == R.id.menu_logout) {
            new com.bolosdaaxcila.cakemanager.utils.SessionManager(this).logout();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        }
        return true;
    }

    private void setText(int id, int value) {
        TextView tv = findViewById(id);
        if (tv != null) tv.setText(String.valueOf(value));
    }
}
