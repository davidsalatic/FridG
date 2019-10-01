package com.example.fridg.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fridg.models.Ingredient;

import java.util.List;

@Dao
public interface IngredientDao  {

    @Insert
    void insert(Ingredient ingredient);

    @Delete
    void delete(Ingredient ingredient);

    @Query("SELECT * FROM Ingredient ORDER BY aisle asc")
    List<Ingredient>getAllIngredients();
}