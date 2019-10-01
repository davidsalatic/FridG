package com.example.fridg.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUtility {
    public static String apiKey="de4156cf6654453082816ada7ab526f3";
    public static String baseUrl="https://api.spoonacular.com/";
    public static Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
    public static SpoonacularAPI spoonacularAPI = retrofit.create(SpoonacularAPI.class);
}
