package com.bolosdaaxcila.cakemanager.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.bolosdaaxcila.cakemanager.data.model.RecipeIngredient;

import java.util.List;

@Dao
public interface RecipeIngredientDao {

    @Insert
    long insert(RecipeIngredient recipeIngredient);

    @Delete
    void delete(RecipeIngredient recipeIngredient);

    @Query("DELETE FROM recipe_ingredients WHERE recipeId = :recipeId")
    void deleteByRecipe(long recipeId);

    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId")
    LiveData<List<RecipeIngredient>> getByRecipe(long recipeId);

    @Query("SELECT COUNT(*) FROM recipe_ingredients WHERE ingredientId = :ingredientId")
    int countByIngredient(long ingredientId);
}
