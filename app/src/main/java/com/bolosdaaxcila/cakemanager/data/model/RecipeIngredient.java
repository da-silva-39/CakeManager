package com.bolosdaaxcila.cakemanager.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe_ingredients",
        foreignKeys = {
                @ForeignKey(entity = Recipe.class, parentColumns = "id", childColumns = "recipeId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Ingredient.class, parentColumns = "id", childColumns = "ingredientId", onDelete = ForeignKey.RESTRICT)
        },
        indices = {@Index("recipeId"), @Index("ingredientId")})
public class RecipeIngredient {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long recipeId;
    private long ingredientId;
    private double quantity;

    public RecipeIngredient(long recipeId, long ingredientId, double quantity) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getRecipeId() { return recipeId; }
    public void setRecipeId(long recipeId) { this.recipeId = recipeId; }
    public long getIngredientId() { return ingredientId; }
    public void setIngredientId(long ingredientId) { this.ingredientId = ingredientId; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
}
