package com.example.fridg.Me;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fridg.MyFridge.FridgeItemAdapter;
import com.example.fridg.MyFridge.SearchIngredientAdapter;
import com.example.fridg.R;
import com.example.fridg.api.APIUtility;
import com.example.fridg.dao.MyRecipeRepository;
import com.example.fridg.models.Ingredient;
import com.example.fridg.models.MyRecipe;
import com.example.fridg.models.RecipeCard;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRecipeActivity extends AppCompatActivity {

    private EditText etTitle;
    private Button btnDone;
    private EditText etInstructions;
    private AutoCompleteTextView actvMyRecipeIngredients;
    private EditText etServings;
    private EditText etPrepTime;
    private EditText etChef;
    private RecyclerView rvMyRecipeIngredients;
    private RecyclerView.LayoutManager rvManager;

    private FridgeItemAdapter ingredientAdapter;
    private ArrayAdapter<Ingredient> searchAdapter;

    private ArrayList<Ingredient> ingredients;
    private ArrayList<Ingredient> searchedIngredients;
    private ArrayList<MyRecipe> myRecipes;

    private MyRecipeRepository myRecipeRepository;
    public static String TAG = "david";

    private MeFragment meFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);

        meFragment= new MeFragment();

        myRecipeRepository=new MyRecipeRepository(getApplication());

        actvMyRecipeIngredients = findViewById(R.id.actvMyRecipeIngredients);
        etTitle = findViewById(R.id.etTitle);
        btnDone = findViewById(R.id.btnDone);
        etInstructions = findViewById(R.id.etInstructions);
        etServings = findViewById(R.id.etServings);
        etPrepTime = findViewById(R.id.etPrepTime);
        etChef = findViewById(R.id.etChef);

        rvMyRecipeIngredients = findViewById(R.id.rvMyRecipesIngredients);
        rvMyRecipeIngredients.setHasFixedSize(true);
        rvManager = new LinearLayoutManager(this.getApplicationContext());
        rvMyRecipeIngredients.setLayoutManager(rvManager);

        ingredients = new ArrayList<>();

        ingredientAdapter = new FridgeItemAdapter(ingredients);
        rvMyRecipeIngredients.setAdapter(ingredientAdapter);
        ingredientAdapter.setOnItemClickListener(new FridgeItemAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                deleteIngredient(position);
            }
        });

        actvMyRecipeIngredients = findViewById(R.id.actvMyRecipeIngredients);
        actvMyRecipeIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                addIngredient(position);
            }
        });
        actvMyRecipeIngredients.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        autoComplete(actvMyRecipeIngredients.getText().toString());
                    }
                }
        );

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etChef.getText().toString().equals("") || etTitle.getText().toString().equals("") || ingredients.size() < 1 || etServings.getText().toString().equals("") || etPrepTime.getText().toString().equals("") || etInstructions.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill out all the fields.", Toast.LENGTH_LONG).show();
                } else {
                    MyRecipe myRecipe = new MyRecipe();
                    myRecipe.setTitle(etTitle.getText().toString());
                    String recipeIngredients = "";
                    for (Ingredient s : ingredients) {
                        recipeIngredients += s.getName().toLowerCase();
                        if (ingredients.indexOf(s) != ingredients.size() - 1)
                            recipeIngredients += "\n";
                    }
                    myRecipe.setIngredients(recipeIngredients);
                    myRecipe.setInstructions(etInstructions.getText().toString());
                    myRecipe.setReadyInMinutes(Integer.parseInt(etPrepTime.getText().toString()));
                    myRecipe.setServings(Integer.parseInt(etServings.getText().toString()));
                    myRecipe.setMask("diamondMask");
                    myRecipe.setBackgroundImage("background1");
                    myRecipe.setAuthor(etChef.getText().toString());
                    myRecipe.setBackgroundColor("#ffffff");
                    myRecipe.setFontColor("#333333");
                    myRecipe.setSource("FridG-by David");

                    myRecipeRepository.insert(myRecipe);

                    finish();
                }
            }
        });

        updateHint();
    }

    private void deleteIngredient(int position) {
        ingredients.remove(position);
        ingredientAdapter.notifyItemRemoved(position);
        updateHint();
    }

    private void addIngredient(int position) {
        Ingredient selectedIngredient = searchAdapter.getItem(position);
        boolean contains = false;
        for (Ingredient i : ingredients) {
            if (i.getId() == selectedIngredient.getId()) {
                contains = true;
                break;
            }
        }

        if (contains == true) {
            Toast.makeText(getApplicationContext(), "Recipe allready contains that item!", Toast.LENGTH_LONG).show();
        } else {
            ingredients.add(selectedIngredient);
            ingredientAdapter.notifyDataSetChanged();
        }
        updateHint();
    }

    private void updateHint() {
        actvMyRecipeIngredients.setText("");
        if (ingredients.isEmpty()) {
            actvMyRecipeIngredients.setHint("Start typing to add ingredients...");
        } else {
            actvMyRecipeIngredients.setHint("Search ingredients...");
        }
    }

    private void autoComplete(String query) {
        searchedIngredients = new ArrayList<>();

        Call<List<Ingredient>> call = APIUtility.spoonacularAPI.autocompleteIngredientSearch(query, 3

                , APIUtility.apiKey, true);
        call.enqueue(new Callback<List<Ingredient>>() {

            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                if (!response.isSuccessful()) {
                    actvMyRecipeIngredients.setHint("Error code: " + response.code());
                    return;
                }

                List<Ingredient> responseIngredients = response.body();

                searchedIngredients.removeAll(searchedIngredients);
                for (Ingredient i : responseIngredients) {
                    searchedIngredients.add(i);
                }
                searchAdapter = new SearchIngredientAdapter(getApplicationContext(), searchedIngredients);
                actvMyRecipeIngredients.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                actvMyRecipeIngredients.setHint("No internet connection");
            }
        });
    }
}
