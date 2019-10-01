package com.example.fridg.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Recipe {

    @PrimaryKey
    private int id;
    private String title;
    private String image;
    @Ignore
    private ArrayList<Ingredient> missedIngredients = new ArrayList<>();
    private int likes;
    private int servings;
    private int readyInMinutes;
    private String sourceName;
    private String sourceUrl;
    private int healthScore;
    private boolean cheap;
    private boolean vegan;
    private boolean vegetarian;
    private boolean veryHealthy;
    private int missedIngredientCount;
    @Ignore
    private ArrayList<String> dishTypes = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public ArrayList<Ingredient> getMissedIngredients() {
        return missedIngredients;
    }

    public int getMissedIngredientCount() {
        return missedIngredientCount;
    }

    public boolean isCheap() {
        return cheap;
    }

    public boolean isVegan() {
        return vegan;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public boolean isVeryHealthy() {
        return veryHealthy;
    }

    public ArrayList<String> getDishTypes() {
        return dishTypes;
    }

    public int getLikes() {
        return likes;
    }

    public int getServings() {
        return servings;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public int getHealthScore() {
        return healthScore;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public void setHealthScore(int healthScore) {
        this.healthScore = healthScore;
    }

    public void setDishTypes(ArrayList<String> dishTypes) {
        this.dishTypes = dishTypes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMissedIngredients(ArrayList<Ingredient> missedIngredients) {
        this.missedIngredients = missedIngredients;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setCheap(boolean cheap) {
        this.cheap = cheap;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public void setVeryHealthy(boolean veryHealthy) {
        this.veryHealthy = veryHealthy;
    }

    public void setMissedIngredientCount(int missedIngredientCount) {
        this.missedIngredientCount = missedIngredientCount;
    }
}
