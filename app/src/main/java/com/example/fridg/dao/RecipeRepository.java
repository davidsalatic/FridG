package com.example.fridg.dao;

import android.app.Application;
import android.os.AsyncTask;

import com.example.fridg.FridGDatabase;
import com.example.fridg.models.Recipe;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeRepository {
    private RecipeDao recipeDao;

    public RecipeRepository(Application application)
    {
        FridGDatabase database = FridGDatabase.getInstance(application);
        recipeDao=database.recipeDao();
    }

    public void insert(Recipe recipe)
    {
        new RecipeRepository.InsertRecipeAsyncTask(recipeDao).execute(recipe);
    }

    public void delete(Recipe recipe)
    {
        new RecipeRepository.DeleteRecipeAsyncTask(recipeDao).execute(recipe);
    }

    public List<Recipe> getAllRecipes() throws ExecutionException, InterruptedException {
        return new RecipeRepository.getAllRecipesAsyncTask(recipeDao).execute().get();
    }

    private static class getAllRecipesAsyncTask extends android.os.AsyncTask<Void, Void, List<Recipe>> {

        private RecipeDao recipeDao;
        List<Recipe> list;

        getAllRecipesAsyncTask(RecipeDao recipeDao) {
            this.recipeDao = recipeDao;
        }

        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            return recipeDao.getAllRecipes();
        }
    }

    private static class InsertRecipeAsyncTask extends AsyncTask<Recipe,Void,Void> {

        private RecipeDao recipeDao;

        private InsertRecipeAsyncTask(RecipeDao recipeDao)
        {
            this.recipeDao=recipeDao;
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            recipeDao.insert(recipes[0]);
            return null;
        }
    }

    private static class DeleteRecipeAsyncTask extends AsyncTask<Recipe,Void,Void>{

        private RecipeDao recipeDao;

        private DeleteRecipeAsyncTask(RecipeDao recipeDao)
        {
            this.recipeDao=recipeDao;
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            recipeDao.delete(recipes[0]);
            return null;
        }
    }
}
