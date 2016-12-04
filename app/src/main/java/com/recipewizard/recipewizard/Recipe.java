package com.recipewizard.recipewizard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brian on 11/17/2016.
 */

public class Recipe {

    public static final int NUM_ALLERGIES = 4;
    // 0 = vegetarian 1 = vegan 2 = glutenfree 3 = dairyfree
    public static final String ITEM_SEP = System.getProperty("line.separator");
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String AUTHOR = "author";
    public static final String PICTURE = "picture";
    public static final String STEPS = "steps";
    public static final String CALORIES = "calories";
    public static final String PROTEIN = "protein";
    public static final String CARBS = "carbs";
    public static final String FAT = "fat";
    public static final String LIKES = "likes";
    public static final String SERVINGS = "servings";
    public static final String COOKTIME = "cookTime";
    public static final String ALLERGYINFORMATION = "allergyInformation";

    private String id = new String();
    private String name = new String();
    private String author = new String();
    private Bitmap picture;
    private List<Step> steps = new ArrayList<>();
    private int calories;
    private int protein;
    private int carbs;
    private int fat;
    private int likes;
    private int servings;
    private int cook_time_minutes;
    private boolean [] allergyInformation = new boolean [NUM_ALLERGIES];

    public Recipe(String id, String name, String author, Bitmap picture, List<Step> steps, int calories,
                  int protein, int carbs, int fat, int likes, int servings, int cook_time_minutes,
                  boolean [] allergyInformation) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.picture = picture;
        this.steps = steps;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.likes = likes;
        this.servings = servings;
        this.cook_time_minutes = cook_time_minutes;
        this.allergyInformation = allergyInformation;
    }
    // Create a new Badge from data packaged in an Intent

    Recipe(Intent intent) {
        id = intent.getStringExtra(Recipe.ID);
        name = intent.getStringExtra(Recipe.NAME);
        author = intent.getStringExtra(Recipe.AUTHOR);
        picture = intent.getParcelableExtra(Recipe.PICTURE);
        steps = intent.getParcelableArrayListExtra(Recipe.STEPS);
        calories = intent.getIntExtra(Recipe.CALORIES, 0);
        protein = intent.getIntExtra(Recipe.PROTEIN, 0);
        carbs = intent.getIntExtra(Recipe.CARBS, 0);
        fat = intent.getIntExtra(Recipe.FAT, 0);
        likes = intent.getIntExtra(Recipe.LIKES, 0);
        servings = intent.getIntExtra(Recipe.SERVINGS, 0);
        cook_time_minutes = intent.getIntExtra(Recipe.COOKTIME, 0);
        allergyInformation = intent.getBooleanArrayExtra(Recipe.ALLERGYINFORMATION);

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

    public String getAuthor () {return author;}

    public void setAuthor (String author) {this.author = author;}

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public List<Step> getSteps () {return steps;}

    public void setSteps(ArrayList<Step> steps) {this.steps = steps;}

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

    public void setLikes(){this.likes = likes;}

    public int getServings () {return this.servings;}

    public void setServings (int servings) {this.servings = servings;}

    public int getCookTime () {return this.cook_time_minutes;}

    public void setCookTime (int cookTime) {this.cook_time_minutes = cookTime;}

    public boolean [] getAllergyInformation () {return allergyInformation;}

    public void setAllergyinformation (boolean [] allergyInformation) {this.allergyInformation =
            allergyInformation;}



    // Take a set of String data values and
    // package them for transport in an Intent

    public static void packageIntent(Intent intent, String id, String author, String name,
                                     Bitmap picture, ArrayList<Step> steps,
                                     int calories, int protein, int carbs, int fat, int likes,
                                     int servings, int cook_time_minutes,
                                     boolean [] allergyInformation) {

        intent.putExtra(Recipe.ID, id);
        intent.putExtra(Recipe.NAME, name);
        intent.putExtra(Recipe.AUTHOR, author);
        intent.putExtra(Recipe.PICTURE, picture);
        intent.putParcelableArrayListExtra(Recipe.STEPS, steps);
        intent.putExtra(Recipe.CALORIES, calories);
        intent.putExtra(Recipe.PROTEIN, protein);
        intent.putExtra(Recipe.CARBS, carbs);
        intent.putExtra(Recipe.FAT, fat);
        intent.putExtra(Recipe.LIKES, likes);
        intent.putExtra(Recipe.SERVINGS, servings);
        intent.putExtra(Recipe.COOKTIME, cook_time_minutes);
        intent.putExtra(Recipe.ALLERGYINFORMATION, allergyInformation);

    }

    public String recipeNumbersToString() {
        return calories + "," + protein + "," + carbs + "," + fat + "," + likes + "," + servings +
                "," + cook_time_minutes;
    }

    public String toString() {
        return id + ITEM_SEP + name + ITEM_SEP + author + ITEM_SEP + TextUtils.join(";", steps) +
                ITEM_SEP + recipeNumbersToString() + ITEM_SEP + allergyInformation.toString();
    }

}
