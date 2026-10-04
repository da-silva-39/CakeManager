package com.bolosdaaxcila.cakemanager.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bolosdaaxcila.cakemanager.R;
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

    private void setText(int id, int value) {
        TextView tv = findViewById(id);
        if (tv != null) tv.setText(String.valueOf(value));
    }
}
