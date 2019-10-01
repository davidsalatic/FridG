package com.example.fridg.Me;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridg.R;
import com.example.fridg.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SavedRecipeAdapter extends RecyclerView.Adapter<SavedRecipeAdapter.SavedRecipeViewHolder> {
    private ArrayList<Recipe> savedRecipes;
    private SavedRecipeAdapter.OnItemClickListener rListener;

    public SavedRecipeAdapter(ArrayList<Recipe> savedRecipes) {
        this.savedRecipes = savedRecipes;
    }

    public void setOnItemClickListener(SavedRecipeAdapter.OnItemClickListener listener) {
        rListener = listener;
    }

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onViewRecipeClick(int position);
    }

    public static class SavedRecipeViewHolder extends RecyclerView.ViewHolder {

        public ImageView savedRecipeImage;
        public TextView tvTitle;
        public Button btnViewSavedRecipe;
        public Button btnDeleteSavedRecipe;

        public SavedRecipeViewHolder(@NonNull View itemView, final SavedRecipeAdapter.OnItemClickListener listener) {
            super(itemView);

            savedRecipeImage = itemView.findViewById(R.id.savedRecipeImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            btnDeleteSavedRecipe = itemView.findViewById(R.id.btnDeleteSavedRecipe);
            btnViewSavedRecipe = itemView.findViewById(R.id.btnViewSavedRecipe);

            btnDeleteSavedRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
            btnViewSavedRecipe.setOnClickListener(new View.OnClickListener() {
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
        }

    }

    @NonNull
    @Override
    public SavedRecipeAdapter.SavedRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_recipe_item, parent, false);
        SavedRecipeAdapter.SavedRecipeViewHolder srvh = new SavedRecipeAdapter.SavedRecipeViewHolder(v, rListener);
        return srvh;
    }

    @Override
    public void onBindViewHolder(@NonNull SavedRecipeAdapter.SavedRecipeViewHolder holder, int position) {
        Recipe recipe = savedRecipes.get(position);
        Picasso.get().load(recipe.getImage()).into(holder.savedRecipeImage);
        if (recipe.getTitle().length() > 17)
            holder.tvTitle.setText(recipe.getTitle().substring(0, 18) + "...");
        else
            holder.tvTitle.setText(recipe.getTitle());
    }

    @Override
    public int getItemCount() {
        if (savedRecipes == null)
            return 0;
        return savedRecipes.size();
    }
}
