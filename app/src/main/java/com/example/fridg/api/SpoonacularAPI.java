package com.example.fridg.api;

import com.example.fridg.models.Ingredient;
import com.example.fridg.models.Recipe;
import com.example.fridg.models.RecipeCard;
import com.example.fridg.models.RecipeComplex;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpoonacularAPI {

    @GET("recipes/findByIngredients")
    Call<List<Recipe>>findByIngredients(@Query("ingredients")String ingredients,@Query("number")int number,@Query("apiKey")String apiKey);

    @GET("food/ingredients/autocomplete")
    Call<List<Ingredient>>autocompleteIngredientSearch(@Query("query")String query,@Query("number")int number,@Query("apiKey")String apiKey,@Query("metaInformation")boolean metaInformation);

    @GET("recipes/{id}/information")
    Call<RecipeComplex>getRecipeInformation(@Path("id") int id, @Query("apiKey")String apiKey);

    @Multipart
    @POST("recipes/visualizeRecipe")
    Call<RecipeCard>createRecipeCard(@Part("title") String title, @Part MultipartBody.Part filePart, @Part("ingredients")String ingredients, @Part("instructions")String instructions, @Part("readyInMinutes")int readyInMinutes, @Part("servings")int servings, @Part("mask")String mask, @Part("backgroundImage")String backgroundImage, @Part("author")String author, @Part("backgroundColor")String backgroundColor, @Part("fontColor")String fontColor, @Part("source")String source, @Query("apiKey") String apiKey);
}
