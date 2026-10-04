package com.bolosdaaxcila.cakemanager.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "stock_movements",
        foreignKeys = @ForeignKey(entity = Ingredient.class,
                parentColumns = "id",
                childColumns = "ingredientId",
                onDelete = ForeignKey.RESTRICT),
        indices = {@Index("ingredientId")})
public class StockMovement {

    public static final String TYPE_ENTRY = "ENTRADA";
    public static final String TYPE_EXIT = "SAIDA";

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long ingredientId;
    private String type;
    private double quantity;
    private long date;
    private String description;

    public StockMovement(long ingredientId, String type, double quantity, long date, String description) {
        this.ingredientId = ingredientId;
        this.type = type;
        this.quantity = quantity;
        this.date = date;
        this.description = description;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getIngredientId() { return ingredientId; }
    public void setIngredientId(long ingredientId) { this.ingredientId = ingredientId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
