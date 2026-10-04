package com.bolosdaaxcila.cakemanager.ui.stock;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.Ingredient;
import com.bolosdaaxcila.cakemanager.data.repository.IngredientRepository;
import com.bolosdaaxcila.cakemanager.data.repository.StockMovementRepository;

public class StockActivity extends AppCompatActivity {

    private StockMovementRepository stockRepository;
    private IngredientRepository ingredientRepository;
    private StockMovementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        stockRepository = new StockMovementRepository(this);
        ingredientRepository = new IngredientRepository(this);

        RecyclerView recycler = findViewById(R.id.recyclerMovements);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StockMovementAdapter();
        recycler.setAdapter(adapter);

        stockRepository.getAll().observe(this, movements -> adapter.setItems(movements));

        TextView textLowStock = findViewById(R.id.textLowStock);
        ingredientRepository.getLowStock().observe(this, ingredients -> {
            if (ingredients == null || ingredients.isEmpty()) {
                textLowStock.setText(getString(R.string.no_low_stock));
            } else {
                StringBuilder sb = new StringBuilder(getString(R.string.low_stock_alert) + "\n");
                for (Ingredient i : ingredients) {
                    sb.append("• ").append(i.getName()).append(": ")
                            .append(i.getQuantity()).append(" ").append(i.getUnit())
                            .append(" (mín. ").append(i.getMinimumQuantity()).append(")\n");
                }
                textLowStock.setText(sb.toString());
            }
        });

        Button btnNew = findViewById(R.id.btnNewMovement);
        btnNew.setOnClickListener(v -> startActivity(new Intent(this, StockMovementFormActivity.class)));
    }
}
