package com.example.fridg.Me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridg.R;
import com.example.fridg.api.APIUtility;
import com.example.fridg.dao.MyRecipeRepository;
import com.example.fridg.dao.RecipeRepository;
import com.example.fridg.models.MyRecipe;
import com.example.fridg.models.Recipe;
import com.example.fridg.models.RecipeCard;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MeFragment extends Fragment {

    private View view;

    private ArrayList<Recipe> savedRecipes;
    private ArrayList<MyRecipe> myRecipes;

    private RecyclerView rvSavedRecipes;
    private RecyclerView.LayoutManager rvManager;
    private SavedRecipeAdapter savedRecipeAdapter;

    private RecyclerView rvMyRecipes;
    private RecyclerView.LayoutManager rvMyRecipesManager;
    private MyRecipeAdapter myRecipeAdapter;

    private RecipeRepository recipeRepository;
    private MyRecipeRepository myRecipeRepository;

    public static String TAG="david";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.me, null);

        myRecipeRepository = new MyRecipeRepository(getActivity().getApplication());

        loadDB();

        setSavedRecipesComponents();
        setMyRecipesComponents();

        Button btnAddRecipe = view.findViewById(R.id.btnAddRecipe);
        btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyRecipeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        if (myRecipeRepository != null) {
            try {
                if (myRecipeRepository.getAllMyRecipes().size() > myRecipes.size()) {
                    try {
                        myRecipes = (ArrayList<MyRecipe>) myRecipeRepository.getAllMyRecipes();
                    } catch (ExecutionException e) {
                        myRecipes = new ArrayList<>();
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        myRecipes = new ArrayList<>();
                        e.printStackTrace();
                    }
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        super.onResume();
    }

    private void setMyRecipesComponents() {
        rvMyRecipes = view.findViewById(R.id.rvMyRecipes);
        rvMyRecipes.setHasFixedSize(true);
        rvMyRecipesManager = new LinearLayoutManager(this.getContext());
        myRecipeAdapter = new MyRecipeAdapter(myRecipes);
        rvMyRecipes.setLayoutManager(rvMyRecipesManager);
        rvMyRecipes.setAdapter(myRecipeAdapter);
        myRecipeAdapter.setOnItemClickListener(new MyRecipeAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                myRecipeRepository.delete(myRecipes.get(position));
                myRecipes.remove(position);
                myRecipeAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onViewRecipeClick(int position) {
                createRecipeCard(myRecipes.get(position));
            }
        });
    }

    private void createRecipeCard(final MyRecipe myRecipe) {

        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.recipes_background)).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] dataIm = baos.toByteArray();

        File file = new File(getContext().getCacheDir(), "filename");
        try {
            file.createNewFile();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            fos.write(dataIm);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));


        Call<RecipeCard> call = APIUtility.spoonacularAPI.createRecipeCard(myRecipe.getTitle(), filePart, myRecipe.getIngredients(), myRecipe.getInstructions(), myRecipe.getReadyInMinutes(), myRecipe.getServings(), myRecipe.getMask(), myRecipe.getBackgroundImage(), myRecipe.getAuthor(), myRecipe.getBackgroundColor(), myRecipe.getFontColor(), myRecipe.getSource(),APIUtility.apiKey);

        call.enqueue(new Callback<RecipeCard>() {
            @Override
            public void onResponse(Call<RecipeCard> call, Response<RecipeCard> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, response.code() + "," + response.body() + "," + response.errorBody());
                    return;
                }

                RecipeCard res= response.body();
                Log.i(TAG, "code:"+response.code()+"value:"+res.getUrlUrl());

                myRecipe.setLink(res.getUrlUrl());
                Intent viewRecipe = new Intent(Intent.ACTION_VIEW, Uri.parse(myRecipe.getLink()));
                startActivity(viewRecipe);
            }

            @Override
            public void onFailure(Call<RecipeCard> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }

    private void loadDB() {
        if (recipeRepository == null) {
            recipeRepository = new RecipeRepository(getActivity().getApplication());
        }
        try {
            myRecipes = (ArrayList<MyRecipe>) myRecipeRepository.getAllMyRecipes();
        } catch (ExecutionException e) {
            myRecipes = new ArrayList<>();
            e.printStackTrace();
        } catch (InterruptedException e) {
            myRecipes = new ArrayList<>();
            e.printStackTrace();
        }
        try {
            savedRecipes = (ArrayList<Recipe>) recipeRepository.getAllRecipes();
        } catch (ExecutionException e) {
            savedRecipes = new ArrayList<>();
            e.printStackTrace();
        } catch (InterruptedException e) {
            savedRecipes = new ArrayList<>();
            e.printStackTrace();
        }
    }

    private void setSavedRecipesComponents() {
        rvSavedRecipes = view.findViewById(R.id.rvSavedRecipes);
        rvSavedRecipes.setHasFixedSize(true);
        rvManager = new LinearLayoutManager(this.getContext());
        savedRecipeAdapter = new SavedRecipeAdapter(savedRecipes);
        rvSavedRecipes.setLayoutManager(rvManager);
        rvSavedRecipes.setAdapter(savedRecipeAdapter);
        savedRecipeAdapter.setOnItemClickListener(new SavedRecipeAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                recipeRepository.delete(savedRecipes.get(position));
                savedRecipes.remove(position);
                savedRecipeAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onViewRecipeClick(int position) {
                Recipe rec = savedRecipes.get(position);
                Intent gotoRecipe = new Intent(Intent.ACTION_VIEW, Uri.parse(rec.getSourceUrl()));
                startActivity(gotoRecipe);
            }
        });
    }

    public void setRecipeRepository(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
}
