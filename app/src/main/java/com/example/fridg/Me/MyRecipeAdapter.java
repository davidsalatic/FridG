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
import com.example.fridg.models.MyRecipe;
import com.example.fridg.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyRecipeAdapter extends RecyclerView.Adapter<MyRecipeAdapter.MyRecipeViewHolder>{
    private ArrayList<MyRecipe> myRecipes;
    private MyRecipeAdapter.OnItemClickListener mListener;

    public MyRecipeAdapter(ArrayList<MyRecipe> myRecipes) {
        this.myRecipes = myRecipes;
    }

    public void setOnItemClickListener(MyRecipeAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onViewRecipeClick(int position);
    }

    public static class MyRecipeViewHolder extends RecyclerView.ViewHolder {

        public ImageView mrImg;
        public TextView mrTvTitle;
        public Button btnMrViewRecipe;
        public Button btnMrDeleteRecipe;

        public MyRecipeViewHolder(@NonNull View itemView, final MyRecipeAdapter.OnItemClickListener listener) {
            super(itemView);

            mrImg = itemView.findViewById(R.id.mrImg);
            mrTvTitle = itemView.findViewById(R.id.mrTvTitle);
            btnMrViewRecipe = itemView.findViewById(R.id.btnMrViewRecipe);
            btnMrDeleteRecipe = itemView.findViewById(R.id.btnMrDeleteRecipe);

            btnMrDeleteRecipe.setOnClickListener(new View.OnClickListener() {
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
            btnMrViewRecipe.setOnClickListener(new View.OnClickListener() {
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
    public MyRecipeAdapter.MyRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_recipe_item, parent, false);
        MyRecipeAdapter.MyRecipeViewHolder mrvh = new MyRecipeAdapter.MyRecipeViewHolder(v, mListener);
        return mrvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipeAdapter.MyRecipeViewHolder holder, int position) {
        MyRecipe myRecipe= myRecipes.get(position);
//        Picasso.get().load(myRecipe.getImage()).into(holder.mrImg);
        if (myRecipe.getTitle().length() > 17)
            holder.mrTvTitle.setText(myRecipe.getTitle().substring(0, 18) + "...");
        else
            holder.mrTvTitle.setText(myRecipe.getTitle());
    }

    @Override
    public int getItemCount() {
        if (myRecipes == null)
            return 0;
        return myRecipes.size();
    }
}
