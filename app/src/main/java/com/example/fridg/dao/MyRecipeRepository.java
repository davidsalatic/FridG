package com.example.fridg.dao;

import android.app.Application;
import android.os.AsyncTask;

import com.example.fridg.FridGDatabase;
import com.example.fridg.models.MyRecipe;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyRecipeRepository {
    private MyRecipeDao myRecipeDao;

    public MyRecipeRepository(Application application)
    {
        FridGDatabase database = FridGDatabase.getInstance(application);
        myRecipeDao=database.myRecipeDao();
    }

    public void insert(MyRecipe myRecipe)
    {
        new MyRecipeRepository.InsertMyRecipeAsyncTask(myRecipeDao).execute(myRecipe);
    }

    public void delete(MyRecipe myRecipe)
    {
        new MyRecipeRepository.DeleteMyRecipeAsyncTask(myRecipeDao).execute(myRecipe);
    }

    public List<MyRecipe> getAllMyRecipes() throws ExecutionException, InterruptedException {
        return new MyRecipeRepository.getAllMyRecipesAsyncTask(myRecipeDao).execute().get();
    }

    private static class getAllMyRecipesAsyncTask extends android.os.AsyncTask<Void, Void, List<MyRecipe>> {

        private MyRecipeDao myRecipeDao;
        List<MyRecipe> list;

        getAllMyRecipesAsyncTask(MyRecipeDao myRecipeDao) {
            this.myRecipeDao= myRecipeDao;
        }

        @Override
        protected List<MyRecipe> doInBackground(Void... voids) {
            return myRecipeDao.getAllMyRecipes();
        }
    }

    private static class InsertMyRecipeAsyncTask extends AsyncTask<MyRecipe,Void,Void> {

        private MyRecipeDao myRecipeDao;

        private InsertMyRecipeAsyncTask(MyRecipeDao myRecipeDao)
        {
            this.myRecipeDao=myRecipeDao;
        }

        @Override
        protected Void doInBackground(MyRecipe... myRecipes) {
            myRecipeDao.insert(myRecipes[0]);
            return null;
        }
    }

    private static class DeleteMyRecipeAsyncTask extends AsyncTask<MyRecipe,Void,Void>{

        private MyRecipeDao myRecipeDao;

        private DeleteMyRecipeAsyncTask(MyRecipeDao myRecipeDao)
        {
            this.myRecipeDao=myRecipeDao;
        }

        @Override
        protected Void doInBackground(MyRecipe... myRecipes) {
            myRecipeDao.delete(myRecipes[0]);
            return null;
        }
    }
}
