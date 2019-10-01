package com.example.fridg;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fridg.dao.IngredientDao;
import com.example.fridg.dao.MyRecipeDao;
import com.example.fridg.dao.RecipeDao;
import com.example.fridg.models.Ingredient;
import com.example.fridg.models.MyRecipe;
import com.example.fridg.models.Recipe;

@Database(entities = {Ingredient.class, Recipe.class, MyRecipe.class},version = 1)
public abstract class FridGDatabase extends RoomDatabase {

    private static FridGDatabase instance;

    public abstract IngredientDao ingredientDao();
    public abstract RecipeDao recipeDao();
    public abstract MyRecipeDao myRecipeDao();

    public static synchronized FridGDatabase getInstance(Context context)
    {
        if(instance==null)
            instance= Room.databaseBuilder(context.getApplicationContext(),FridGDatabase.class,"fridGdatabase").fallbackToDestructiveMigration().build();

        return instance;
    }

}
