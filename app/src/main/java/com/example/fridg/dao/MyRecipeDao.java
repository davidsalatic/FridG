package com.example.fridg.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fridg.models.MyRecipe;

import java.util.List;

@Dao
public interface MyRecipeDao {
    @Insert
    void insert(MyRecipe myRecipe);

    @Delete
    void delete(MyRecipe myRecipe);

    @Query("SELECT * FROM MyRecipe ORDER BY title asc")
    List<MyRecipe> getAllMyRecipes();
}
