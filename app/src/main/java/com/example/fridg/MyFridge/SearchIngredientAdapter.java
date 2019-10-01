package com.example.fridg.MyFridge;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.fridg.R;
import com.example.fridg.models.Ingredient;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class SearchIngredientAdapter extends ArrayAdapter {

    public SearchIngredientAdapter(@NonNull Context context, ArrayList<Ingredient> ingredients) {
        super(context, R.layout.ingredient_list_item,ingredients);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view= inflater.inflate(R.layout.ingredient_list_item,parent,false);

        Ingredient item = (Ingredient) getItem(position);

        String ingredientName = item.getName();

        TextView tvIngredientName = view.findViewById(R.id.tvIngredientName);
        tvIngredientName.setText(ingredientName);

        RoundedImageView ingredientImage = view.findViewById(R.id.ingredientImage);
        ingredientImage.setOval(true);
        ingredientImage.setBorderWidth((float) 1);
        ingredientImage.setBorderColor(Color.LTGRAY);



        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.LTGRAY)
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/"+item.getImage()).fit().transform(transformation).into(ingredientImage);

        return view;
    }


}
