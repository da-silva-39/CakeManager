package com.bolosdaaxcila.cakemanager.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredients")
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private double quantity;
    private double minimumQuantity;
    private String unit;

    public Ingredient(String name, double quantity, double minimumQuantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.minimumQuantity = minimumQuantity;
        this.unit = unit;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public double getMinimumQuantity() { return minimumQuantity; }
    public void setMinimumQuantity(double minimumQuantity) { this.minimumQuantity = minimumQuantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public boolean isLowStock() {
        return quantity <= minimumQuantity;
    }
}
