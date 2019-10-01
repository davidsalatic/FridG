package com.example.fridg.MyFridge;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridg.R;
import com.example.fridg.api.APIUtility;
import com.example.fridg.dao.IngredientRepository;
import com.example.fridg.models.Ingredient;
import com.example.fridg.models.Recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFridgeFragment extends Fragment {

    private AutoCompleteTextView actvIngredients;

    private ArrayList<Ingredient> searchedIngredients;
    private ArrayAdapter<Ingredient> searchAdapter;

    private ArrayList<Ingredient> fridge;

    private RecyclerView rvFridge;
    private FridgeItemAdapter fridgeAdapter;
    private RecyclerView.LayoutManager rvManager;

    private IngredientRepository ingredientRepository;
    public static boolean newI = true;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.my_fridge, null);

        loadFridgeDB();

        rvFridge = view.findViewById(R.id.rvFridge);
        rvFridge.setHasFixedSize(true);
        rvManager = new LinearLayoutManager(this.getContext());
        rvFridge.setLayoutManager(rvManager);

        fridgeAdapter = new FridgeItemAdapter(fridge);
        rvFridge.setAdapter(fridgeAdapter);
        fridgeAdapter.setOnItemClickListener(new FridgeItemAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                deleteIngredient(position);
            }
        });

        actvIngredients = view.findViewById(R.id.actvIngredients);
        actvIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                addIngredient(position);
            }
        });
        actvIngredients.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        autoComplete(actvIngredients.getText().toString());
                    }
                }
        );
        sortFridge();
        updateHint();
        return view;
    }

    private void addIngredient(int position) {
        Ingredient selectedIngredient = searchAdapter.getItem(position);
        boolean contains = false;
        for (Ingredient i : fridge) {
            if (i.getId() == selectedIngredient.getId()) {
                contains = true;
                break;
            }
        }

        if (contains == true) {
            Toast.makeText(view.getContext(), "Fridge allready contains that item!", Toast.LENGTH_LONG).show();
        } else {
            fridge.add(selectedIngredient);
            ingredientRepository.insert(selectedIngredient);
            fridgeAdapter.notifyDataSetChanged();
            newI = true;
        }
        updateHint();
        sortFridge();
    }

    private void deleteIngredient(int position) {
        ingredientRepository.delete(fridge.get(position));
        fridge.remove(position);
        fridgeAdapter.notifyItemRemoved(position);
        newI = true;
        updateHint();
        sortFridge();
    }

    private void loadFridgeDB() {
        try {
            fridge = (ArrayList<Ingredient>) ingredientRepository.getAllIngredients();
        } catch (ExecutionException e) {
            fridge = new ArrayList<>();
            e.printStackTrace();
            actvIngredients.setHint(e.getMessage());
        } catch (InterruptedException e) {
            fridge = new ArrayList<>();
            e.printStackTrace();
            actvIngredients.setHint(e.getMessage());
        }
    }

    private void autoComplete(String query) {

        searchedIngredients = new ArrayList<>();

        Call<List<Ingredient>> call = APIUtility.spoonacularAPI.autocompleteIngredientSearch(query, 5

                , APIUtility.apiKey, true);
        call.enqueue(new Callback<List<Ingredient>>() {

            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                if (!response.isSuccessful()) {
                    actvIngredients.setHint("Error code: " + response.code());
                    return;
                }

                List<Ingredient> responseIngredients = response.body();

                searchedIngredients.removeAll(searchedIngredients);
                for (Ingredient i : responseIngredients) {
                    searchedIngredients.add(i);
                }
                searchAdapter = new SearchIngredientAdapter(getView().getContext(), searchedIngredients);
                actvIngredients.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                actvIngredients.setHint("No internet connection");
            }
        });
    }

    private void updateHint() {
        actvIngredients.setText("");
        if (fridge.isEmpty()) {
            actvIngredients.setHint("Add your first ingredient...");
        } else {
            actvIngredients.setHint("Search ingredients...");
        }
    }

    private void sortFridge() {
        Collections.sort(fridge, new Comparator<Ingredient>() {
            @Override
            public int compare(Ingredient i1, Ingredient i2) {
                return i1.getName().compareTo(i2.getName());
            }
        });
    }

    public ArrayList<Ingredient> getFridge() {
        return fridge;
    }

    public void setIngredientRepository(IngredientRepository ingredientRepository)
    {
        this.ingredientRepository=ingredientRepository;
    }

}
