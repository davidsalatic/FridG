package com.example.fridg.Recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridg.R;
import com.example.fridg.models.Ingredient;
import com.example.fridg.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecipeItemAdapter extends RecyclerView.Adapter<RecipeItemAdapter.RecipeViewHolder> {

    private ArrayList<Recipe> recipes;
    private OnItemClickListener rListener;

    public interface OnItemClickListener {
        void onSaveRecipeClick(int position);
        void onViewRecipeClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        rListener = listener;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        public Button btnViewRecipe;
        public ImageView recipeImage;
        public TextView tvTitle;
        public TextView tvMissingIngredients;
        public TextView tvServings;
        public TextView tvTime;
        public TextView tvHealthScore;
        public TextView tvSource;
        public TextView tvLikes;
        public ImageView icHealthScore;
        public ImageView icTime;
        public ImageView icServings;
        public ImageView icLikes;
        public Button btnSave;

        public RecipeViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);
            btnViewRecipe = itemView.findViewById(R.id.btnViewSavedRecipe);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvMissingIngredients = itemView.findViewById(R.id.tvMissingIngredients);
            tvServings = itemView.findViewById(R.id.tvServings);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvHealthScore = itemView.findViewById(R.id.tvHealthScore);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvTime = itemView.findViewById(R.id.tvTime);
            icHealthScore = itemView.findViewById(R.id.icHealthScore);
            icTime = itemView.findViewById(R.id.icTime);
            icServings = itemView.findViewById(R.id.icServings);
            icLikes = itemView.findViewById(R.id.icLikes);
            btnSave = itemView.findViewById(R.id.btnSave);

            btnViewRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onViewRecipeClick(position);
                        }
                    }
                }
            });
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                            listener.onSaveRecipeClick(position);
                    }
                }
            });

            addToastMessages();
        }

        private void addToastMessages() {
            icHealthScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Health score", Toast.LENGTH_LONG).show();
                }
            });
            tvHealthScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Health score", Toast.LENGTH_LONG).show();
                }
            });
            icTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Prep time", Toast.LENGTH_LONG).show();
                }
            });
            tvTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Prep time", Toast.LENGTH_LONG).show();
                }
            });
            icServings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Servings", Toast.LENGTH_LONG).show();
                }
            });
            tvServings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Servings", Toast.LENGTH_LONG).show();
                }
            });
            icLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Likes on social media", Toast.LENGTH_LONG).show();
                }
            });
            tvLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Likes on social media", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public RecipeItemAdapter(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        RecipeViewHolder rvh = new RecipeViewHolder(v, rListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        Picasso.get().load(recipe.getImage()).into(holder.recipeImage);
        holder.tvTitle.setText(recipe.getTitle());
        holder.tvServings.setText(String.valueOf(recipe.getServings()));
        holder.tvTime.setText(recipe.getReadyInMinutes() + "min");
        holder.tvHealthScore.setText(String.valueOf(recipe.getHealthScore()));
        holder.tvSource.setText(recipe.getSourceName());
        holder.tvLikes.setText(String.valueOf(recipe.getLikes()));

        ArrayList<Ingredient> missing = recipe.getMissedIngredients();
        String text = "";
        for (Ingredient i : missing) {
            text += i.toString();
            if (missing.indexOf(i) != missing.size() - 1)
                text += ", ";
        }
        if(text.length()>214)
            holder.tvMissingIngredients.setText(text.substring(0, 215)+"...");
        else
            holder.tvMissingIngredients.setText(text);

    }

    @Override
    public int getItemCount() {
        if (recipes == null)
            return 0;
        else
            return recipes.size();
    }
}
