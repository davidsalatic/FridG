package com.example.fridg.Recipes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridg.MyFridge.MyFridgeFragment;
import com.example.fridg.R;
import com.example.fridg.api.APIUtility;
import com.example.fridg.dao.IngredientRepository;
import com.example.fridg.dao.RecipeRepository;
import com.example.fridg.models.Ingredient;
import com.example.fridg.models.Recipe;
import com.example.fridg.models.RecipeComplex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesFragment extends Fragment {

    private ArrayList<Ingredient> fridge;

    private ArrayList<Recipe> recipes;
    private ArrayList<Recipe> hiddenRecipes;
    private ArrayList<Recipe> savedRecipes;

    private RecyclerView rvRecipes;
    private RecipeItemAdapter recipeAdapter;
    private RecyclerView.LayoutManager rvManager;

    private View view;

    private RecipeRepository recipeRepository;

    private CheckBox cbVegan;
    private CheckBox cbVegeterian;
    private CheckBox cbCheap;
    private CheckBox cbHealthy;
    private CheckBox cbBreakfast;
    private CheckBox cbMainCourse;
    private CheckBox cbDessert;
    private CheckBox cbAppetizer;
    private CheckBox cbSalad;
    private CheckBox cbSideDish;
    private CheckBox cbSnack;
    private CheckBox cbDrink;
    private boolean busy = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.recipes, null);

        savedRecipes = new ArrayList<>();
        hiddenRecipes = new ArrayList<>();
        setCheckBoxes();

        loadSavedRecipesDB();

        rvRecipes = view.findViewById(R.id.rvRecipes);
        rvRecipes.setHasFixedSize(true);
        rvManager = new LinearLayoutManager(this.getContext());
        recipeAdapter = new RecipeItemAdapter(recipes);
        rvRecipes.setLayoutManager(rvManager);
        rvRecipes.setAdapter(recipeAdapter);
        recipeAdapter.setOnItemClickListener(new RecipeItemAdapter.OnItemClickListener() {
            @Override
            public void onSaveRecipeClick(int position) {
                saveRecipe(position);
            }

            @Override
            public void onViewRecipeClick(int position) {
                Recipe rec = recipes.get(position);
                Intent viewRecipe = new Intent(Intent.ACTION_VIEW, Uri.parse(rec.getSourceUrl()));
                startActivity(viewRecipe);
            }
        });

        if (MyFridgeFragment.newI == true)//if the user has changed fridge
            findByIngredients();
        return view;
    }

    public void loadSavedRecipesDB() {
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

    public void setFridge(ArrayList<Ingredient> fridge) {
        this.fridge = fridge;
    }

    public void findByIngredients() {
        busy = true;
        recipes = new ArrayList<>();
        String ingredients = "";
        if (fridge != null) {
            for (Ingredient i : fridge) {
                ingredients += i.getName();
                if (!(fridge.indexOf(i) == fridge.size() - 1)) {
                    ingredients += ",";
                }
            }
        }

        Call<List<Recipe>> call = APIUtility.spoonacularAPI.findByIngredients(ingredients, 30, APIUtility.apiKey);

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                List<Recipe> responseRecipes = response.body();

                for (Recipe r : responseRecipes) {
                    recipes.add(r);
                }
                for (Recipe r : responseRecipes) {
                    addRecipeInfo(r);
                }
                recipeAdapter = new RecipeItemAdapter(recipes);
                recipeAdapter.setOnItemClickListener(new RecipeItemAdapter.OnItemClickListener() {
                    @Override
                    public void onSaveRecipeClick(int position) {
                        saveRecipe(position);
                    }

                    @Override
                    public void onViewRecipeClick(int position) {
                        Recipe rec = recipes.get(position);
                        Intent viewRecipe = new Intent(Intent.ACTION_VIEW, Uri.parse(rec.getSourceUrl()));
                        startActivity(viewRecipe);
                    }
                });
                sortRecipes();
                rvRecipes.setAdapter(recipeAdapter);
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
            }
        });
        MyFridgeFragment.newI = false;
    }

    public void addRecipeInfo(final Recipe r) {
        busy = true;
        int id = r.getId();
        Call<RecipeComplex> call = APIUtility.spoonacularAPI.getRecipeInformation(id, APIUtility.apiKey);

        call.enqueue(new Callback<RecipeComplex>() {
            @Override
            public void onResponse(Call<RecipeComplex> call, Response<RecipeComplex> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                RecipeComplex recipeComplex = response.body();

                r.setServings(recipeComplex.getServings());
                r.setReadyInMinutes(recipeComplex.getReadyInMinutes());
                r.setHealthScore(recipeComplex.getHealthScore());
                if (r.getHealthScore() > 69)
                    r.setVeryHealthy(true);
                r.setSourceName(recipeComplex.getSourceName());
                r.setSourceUrl(recipeComplex.getSourceUrl());
                r.setDishTypes(recipeComplex.getDishTypes());
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<RecipeComplex> call, Throwable t) {

            }
        });
        busy = false;
    }

    private void sortRecipes() {
        Collections.sort(recipes, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe r1, Recipe r2) {
                return r1.getMissedIngredientCount() - r2.getMissedIngredientCount(); // Ascending
            }
        });
    }

    private void saveRecipe(int position) {
        if (savedRecipes != null) {
            if (savedRecipes.size() > 0) {
                int id = recipes.get(position).getId();

                boolean contains = false;

                for (Recipe r : savedRecipes) {
                    if (id == r.getId()) {
                        contains = true;
                        break;
                    }
                }
                if (contains == true) {
                    Toast.makeText(view.getContext(), "Recipe already saved!", Toast.LENGTH_LONG).show();

                } else {
                    savedRecipes.add(recipes.get(position));
                    recipeRepository.insert(recipes.get(position));
                    Toast.makeText(view.getContext(), "Recipe saved", Toast.LENGTH_LONG).show();
                }
            } else {
                savedRecipes.add(recipes.get(position));
                recipeRepository.insert(recipes.get(position));
                Toast.makeText(view.getContext(), "Recipe saved", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setCheckBoxes() {
        cbVegan = view.findViewById(R.id.cbVegan);
        cbVegan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckBoxClick(cbVegan);
            }
        });
        cbVegeterian = view.findViewById(R.id.cbVegeterian);
        cbVegeterian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckBoxClick(cbVegeterian);
            }
        });
        cbCheap = view.findViewById(R.id.cbCheap);
        cbCheap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckBoxClick(cbCheap);
            }
        });
        cbHealthy = view.findViewById(R.id.cbHealthy);
        cbHealthy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckBoxClick(cbHealthy);
            }
        });
        cbBreakfast = view.findViewById(R.id.cbBreakfast);
        cbBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckBoxClick(cbBreakfast);
            }
        });
        cbMainCourse = view.findViewById(R.id.cbMainCourse);
        cbMainCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckBoxClick(cbMainCourse);
            }
        });
        cbDessert = view.findViewById(R.id.cbDessert);
        cbDessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckBoxClick(cbDessert);
            }
        });
        cbAppetizer = view.findViewById(R.id.cbAppetizer);
        cbAppetizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckBoxClick(cbAppetizer);
            }
        });
        cbSalad = view.findViewById(R.id.cbSalad);
        cbSalad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckBoxClick(cbSalad);
            }
        });
        cbSideDish = view.findViewById(R.id.cbSideDish);
        cbSideDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckBoxClick(cbSideDish);
            }
        });
        cbSnack = view.findViewById(R.id.cbSnack);
        cbSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckBoxClick(cbSnack);
            }
        });
        cbDrink = view.findViewById(R.id.cbDrink);
        cbDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckBoxClick(cbDrink);
            }
        });
    }

    private void filterResults() {
        if (recipes != null && hiddenRecipes != null) {
            for (Recipe r : hiddenRecipes)
                if (hiddenRecipes.size() > 0) {
                    if (!recipes.contains(r))
                        recipes.add(r);
                }
        }

        Recipe[] arrayRecipes = recipes.toArray(new Recipe[recipes.size()]);

        if (recipes != null && recipeAdapter != null) {
            for (Recipe r : arrayRecipes) {
                if (cbVegan.isChecked())
                    if (r.isVegan() == false)
                        hideRecipe(r);
                if (cbVegeterian.isChecked())
                    if (r.isVegetarian() == false)
                        hideRecipe(r);
                if (cbCheap.isChecked())
                    if (r.isCheap() == false)
                        hideRecipe(r);
                if (cbHealthy.isChecked())
                    if (r.isVeryHealthy() == false)
                        hideRecipe(r);
                if (cbBreakfast.isChecked())
                    if (!(r.getDishTypes().contains("breakfast")))
                        hideRecipe(r);
                if (cbMainCourse.isChecked())
                    if (!(r.getDishTypes().contains("main course")))
                        hideRecipe(r);
                if (cbSideDish.isChecked())
                    if (!(r.getDishTypes().contains("side dish")))
                        hideRecipe(r);
                if (cbDessert.isChecked())
                    if (!(r.getDishTypes().contains("dessert")))
                        hideRecipe(r);
                if (cbAppetizer.isChecked())
                    if (!(r.getDishTypes().contains("appetizer")))
                        hideRecipe(r);
                if (cbSalad.isChecked())
                    if (!(r.getDishTypes().contains("salad")))
                        hideRecipe(r);
                if (cbSnack.isChecked())
                    if (!(r.getDishTypes().contains("snack")))
                        hideRecipe(r);
                if (cbDrink.isChecked())
                    if (!(r.getDishTypes().contains("drink")))
                        hideRecipe(r);
            }
        }

        recipeAdapter.notifyDataSetChanged();
        busy = false;
    }

    private void hideRecipe(Recipe r) {
        recipes.remove(r);
        hiddenRecipes.add(r);
    }

    private void onCheckBoxClick(CheckBox cb) {
        if (busy == false) {
            busy = true;
            filterResults();
        } else {
            cb.setChecked(false);
        }
        sortRecipes();
    }

    public void setRecipeRepository(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
}
