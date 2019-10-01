package com.example.fridg.dao;

import android.app.Application;
import android.os.AsyncTask;

import com.example.fridg.FridGDatabase;
import com.example.fridg.models.Ingredient;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class IngredientRepository {
    private IngredientDao ingredientDao;

    public IngredientRepository(Application application)
    {
        FridGDatabase database = FridGDatabase.getInstance(application);
        ingredientDao=database.ingredientDao();
    }

    public void insert(Ingredient ingredient)
    {
        new InsertIngredientAsyncTask(ingredientDao).execute(ingredient);
    }

    public void delete(Ingredient ingredient)
    {
        new DeleteIngredientAsyncTask(ingredientDao).execute(ingredient);
    }

    public List<Ingredient> getAllIngredients() throws ExecutionException, InterruptedException {
        return new getAllIngredientsAsyncTask(ingredientDao).execute().get();
    }

    private static class getAllIngredientsAsyncTask extends android.os.AsyncTask<Void, Void, List<Ingredient>> {

        private IngredientDao ingredientDao;
        List<Ingredient> list;

        getAllIngredientsAsyncTask(IngredientDao ingredientDao) {
            this.ingredientDao = ingredientDao;
        }

        @Override
        protected List<Ingredient> doInBackground(Void... voids) {
            return ingredientDao.getAllIngredients();
        }
    }

    private static class InsertIngredientAsyncTask extends AsyncTask<Ingredient,Void,Void>{

        private IngredientDao ingredientDao;

        private InsertIngredientAsyncTask(IngredientDao ingredientDao)
        {
            this.ingredientDao=ingredientDao;
        }

        @Override
        protected Void doInBackground(Ingredient... ingredients) {
            ingredientDao.insert(ingredients[0]);
            return null;
        }
    }

    private static class DeleteIngredientAsyncTask extends AsyncTask<Ingredient,Void,Void>{

        private IngredientDao ingredientDao;

        private DeleteIngredientAsyncTask(IngredientDao ingredientDao)
        {
            this.ingredientDao=ingredientDao;
        }

        @Override
        protected Void doInBackground(Ingredient... ingredients) {
            ingredientDao.delete(ingredients[0]);
            return null;
        }
    }
}
