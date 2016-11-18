package com.recipewizard.recipewizard;

import android.graphics.Bitmap;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brian on 11/17/2016.
 */

public class Recipe {

    public static final String ITEM_SEP = System.getProperty("line.separator");
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PICTURE = "picture";
    public static final String INGREDIENTS = "ingredients";
    public static final String DIRECTIONS = "directions";
    public static final String CALORIES = "calories";
    public static final String PROTEIN = "protein";
    public static final String CARBS = "carbs";
    public static final String FAT = "fat";
    public static final String LIKES = "likes";

    private String id = new String();
    private String name = new String();
    private Bitmap picture;
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<String> directions = new ArrayList<>();
    private int calories;
    private int protein;
    private int carbs;
    private int fat;
    private int likes;

    Recipe(String id, String name, Bitmap picture, List<Ingredient> ingredients,
           List<String> directions, int calories, int protein, int carbs, int fat, int likes) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.ingredients = ingredients;
        this.directions = directions;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.likes = likes;

    }

    // Create a new Badge from data packaged in an Intent

    Recipe(Intent intent) {
        id = intent.getStringExtra(Recipe.ID);
        name = intent.getStringExtra(Recipe.NAME);
        picture = (Bitmap) intent.getParcelableExtra(Recipe.PICTURE);
        ingredients = intent.getParcelableArrayListExtra(Recipe.INGREDIENTS);
        directions = intent.getStringArrayListExtra(Recipe.DIRECTIONS);
        calories = intent.getIntExtra(Recipe.CALORIES, 0);
        protein = intent.getIntExtra(Recipe.PROTEIN, 0);
        carbs = intent.getIntExtra(Recipe.CARBS, 0);
        fat = intent.getIntExtra(Recipe.FAT, 0);
        likes = intent.getIntExtra(Recipe.LIKES, 0);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getDirections() {
        return directions;
    }

    public void setDirections(ArrayList<String> directions) {
        this.directions = directions;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes() {
        this.likes = likes;
    }


    // Take a set of String data values and
    // package them for transport in an Intent

    public static void packageIntent(Intent intent, String id, String name, Bitmap picture,
                                     ArrayList<Ingredient> ingredients, ArrayList<String> directions,
                                     int calories, int protein, int carbs, int fat, int likes) {

        intent.putExtra(Recipe.ID, id);
        intent.putExtra(Recipe.NAME, name);
        intent.putExtra(Recipe.PICTURE, picture);
        intent.putParcelableArrayListExtra(Recipe.INGREDIENTS, ingredients);
        intent.putStringArrayListExtra(Recipe.DIRECTIONS, directions);
        intent.putExtra(Recipe.CALORIES, calories);
        intent.putExtra(Recipe.PROTEIN, protein);
        intent.putExtra(Recipe.CARBS, carbs);
        intent.putExtra(Recipe.FAT, fat);
        intent.putExtra(Recipe.LIKES, likes);

    }

    public String recipeNumbersToString() {
        return calories + "," + protein + "," + carbs + "," + fat + "," + likes;
    }

    public String toString() {
        return id + ITEM_SEP + name + ITEM_SEP + ingredients.toString() + ITEM_SEP + directions.toString() + ITEM_SEP + recipeNumbersToString();
    }

}
