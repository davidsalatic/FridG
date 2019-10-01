package com.example.fridg.MyFridge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridg.R;
import com.example.fridg.models.Ingredient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FridgeItemAdapter extends RecyclerView.Adapter<FridgeItemAdapter.FridgeViewHolder> {

    private ArrayList<Ingredient>fridgeItems;
    private OnItemClickListener fListener;

    public FridgeItemAdapter(ArrayList<Ingredient> fridgeItems)
    {
        this.fridgeItems=fridgeItems;
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        fListener=listener;
    }

    public interface OnItemClickListener{
        void onDeleteClick(int position);
    }

    public static class FridgeViewHolder extends RecyclerView.ViewHolder{

        public ImageView itemImage;
        public TextView tvItemName;
        public Button btnDelete;

        public FridgeViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            itemImage=itemView.findViewById(R.id.itemImage);
            tvItemName=itemView.findViewById(R.id.tvItemName);
            btnDelete=itemView.findViewById(R.id.btnDeleteSavedRecipe);

            btnDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }

    }

    @NonNull
    @Override
    public FridgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fridge_item,parent,false);
        FridgeViewHolder fvh = new FridgeViewHolder(v,fListener);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FridgeViewHolder holder, int position) {
        Ingredient ingredient = fridgeItems.get(position);
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/"+ingredient.getImage()).into(holder.itemImage);
        holder.tvItemName.setText(ingredient.getName());
    }

    @Override
    public int getItemCount() {
        return fridgeItems.size();
    }
}
