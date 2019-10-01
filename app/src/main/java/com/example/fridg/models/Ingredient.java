package com.example.fridg.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ingredient {

    @PrimaryKey
    private int id;
    private String aisle;
    private String name;
    private String image;

    public Ingredient(int id,String name, String aisle, String image) {
        this.id = id;
        this.aisle = aisle;
        this.name = name;
        this.image = image;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getAisle() {
        return aisle;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    //REMOVE LATER
    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
