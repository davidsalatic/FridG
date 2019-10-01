package com.example.fridg.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class MyRecipe implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int id;
    String title;
    String link;
    byte [] image;
    String ingredients;
    String instructions;
    int readyInMinutes;
    int servings;
    String mask;
    String backgroundImage;
    String author;
    String backgroundColor;
    String fontColor;
    String source;

    public MyRecipe() {
    }

    public String getTitle() {
        return title;
    }

    public byte[] getImage() {
        return image;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public int getServings() {
        return servings;
    }

    public String getMask() {
        return mask;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getAuthor() {
        return author;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public String getSource() {
        return source;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getLink() {
        return link;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
