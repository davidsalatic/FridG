package com.example.fridg;

import android.os.Bundle;

import com.example.fridg.Me.MeFragment;
import com.example.fridg.MyFridge.MyFridgeFragment;
import com.example.fridg.Recipes.RecipesFragment;
import com.example.fridg.dao.IngredientRepository;
import com.example.fridg.dao.MyRecipeRepository;
import com.example.fridg.dao.RecipeRepository;
import com.example.fridg.models.Recipe;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private MyFridgeFragment myFridgeFragment;
    private RecipesFragment recipesFragment;
    private MeFragment meFragment;

    private IngredientRepository ingredientRepository;
    private RecipeRepository recipeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);

        ingredientRepository= new IngredientRepository(this.getApplication());
        recipeRepository = new RecipeRepository(this.getApplication());

        myFridgeFragment=new MyFridgeFragment();
        recipesFragment=new RecipesFragment();
        meFragment=new MeFragment();

        myFridgeFragment.setIngredientRepository(ingredientRepository);
        loadFragment(myFridgeFragment);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.navigation_myfridge)
        {
            myFridgeFragment.setIngredientRepository(this.ingredientRepository);
            return  loadFragment(myFridgeFragment);
        }
        else if (item.getItemId()==R.id.navigation_recipes)
        {
            recipesFragment.setRecipeRepository(recipeRepository);
            recipesFragment.setFridge(myFridgeFragment.getFridge());
            return loadFragment(recipesFragment);
        }
        else//me fragment clicked
        {
            meFragment.setRecipeRepository(recipeRepository);
            return loadFragment(meFragment);
        }

    }

    private boolean loadFragment(Fragment fragment)
    {
        if(fragment!=null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout,fragment).commit();
            return true;
        }

        return false;
    }
}
