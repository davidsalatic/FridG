package com.example.fridg.models;

import java.util.ArrayList;

public class RecipeComplex {

    private int id;
    private String title;
    private String image;
    private String imageType;
    private int servings;
    private int readyInMinutes;
    private String license;
    private String sourceName;
    private String sourceUrl;
    private String spoonacularSourceUrl;
    private float aggregateLikes;
    private int healthScore;
    private float spoonacularScore;
    private float pricePerServing;
    ArrayList<Object> analyzedInstructions = new ArrayList<Object>();
    private boolean cheap;
    private String creditsText;
    ArrayList<Object> cuisines = new ArrayList<Object>();
    private boolean dairyFree;
    ArrayList<Object> diets = new ArrayList<Object>();
    private String gaps;
    private boolean glutenFree;
    private String instructions;
    private boolean ketogenic;
    private boolean lowFodmap;
    ArrayList<Object> occasions = new ArrayList<Object>();
    private boolean sustainable;
    private boolean vegan;
    private boolean vegetarian;
    private boolean veryHealthy;
    private boolean veryPopular;
    private boolean whole30;
    private float weightWatcherSmartPoints;
    ArrayList<String> dishTypes = new ArrayList<>();
    ArrayList<Object> extendedIngredients = new ArrayList<Object>();

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getImageType() {
        return imageType;
    }

    public int getServings() {
        return servings;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public String getLicense() {
        return license;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getSpoonacularSourceUrl() {
        return spoonacularSourceUrl;
    }

    public float getAggregateLikes() {
        return aggregateLikes;
    }

    public int getHealthScore() {
        return healthScore;
    }

    public float getSpoonacularScore() {
        return spoonacularScore;
    }

    public float getPricePerServing() {
        return pricePerServing;
    }

    public ArrayList<Object> getAnalyzedInstructions() {
        return analyzedInstructions;
    }

    public boolean isCheap() {
        return cheap;
    }

    public String getCreditsText() {
        return creditsText;
    }

    public ArrayList<Object> getCuisines() {
        return cuisines;
    }

    public boolean isDairyFree() {
        return dairyFree;
    }

    public ArrayList<Object> getDiets() {
        return diets;
    }

    public String getGaps() {
        return gaps;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public String getInstructions() {
        return instructions;
    }

    public boolean isKetogenic() {
        return ketogenic;
    }

    public boolean isLowFodmap() {
        return lowFodmap;
    }

    public ArrayList<Object> getOccasions() {
        return occasions;
    }

    public boolean isSustainable() {
        return sustainable;
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

    public boolean isVeryPopular() {
        return veryPopular;
    }

    public boolean isWhole30() {
        return whole30;
    }

    public float getWeightWatcherSmartPoints() {
        return weightWatcherSmartPoints;
    }

    public ArrayList<String> getDishTypes() {
        return dishTypes;
    }

    public ArrayList<Object> getExtendedIngredients() {
        return extendedIngredients;
    }
}
