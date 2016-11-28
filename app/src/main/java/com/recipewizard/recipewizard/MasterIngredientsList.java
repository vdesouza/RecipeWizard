package com.recipewizard.recipewizard;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArraySet;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by vdesouza on 11/18/16.
 */

public class MasterIngredientsList implements Parcelable {

    // Arraylist of Ingredient objects
    private ArrayList<Ingredient> masterIngredientsList = new ArrayList<Ingredient>();

    // List default generated categories
    static final String DRY_GOODS = "Dry Goods";
    static final String MEATS = "Meats";
    static final String FRUITS = "Fruits";
    static final String VEGETABLES = "Vegetables";
    static final String SPICES = "Spices and Herbs";
    static final String BAKING = "Baking";
    static final String CONDIMENTS = "Oils and Condiments";



    public MasterIngredientsList() {

        // Dry Goods
        masterIngredientsList.add(new Ingredient("Pasta", null, false, DRY_GOODS));
        masterIngredientsList.add(new Ingredient("Rice", null, false, DRY_GOODS));
        masterIngredientsList.add(new Ingredient("Oats", null, false, DRY_GOODS));
        // Meats
        masterIngredientsList.add(new Ingredient("Chicken", null, false, MEATS));
        masterIngredientsList.add(new Ingredient("Beef", null, false, MEATS));
        masterIngredientsList.add(new Ingredient("Lamb", null, false, MEATS));
        // Fruits
        masterIngredientsList.add(new Ingredient("Apple", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Banana", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Grapes", null, false, FRUITS));
        // Vegetables
        masterIngredientsList.add(new Ingredient("Broccoli", null, false, VEGETABLES));
        masterIngredientsList.add(new Ingredient("Kale", null, false, VEGETABLES));
        masterIngredientsList.add(new Ingredient("Carrots", null, false, VEGETABLES));
        // Spices and Herbs
        masterIngredientsList.add(new Ingredient("Oregano", null, false, SPICES));
        masterIngredientsList.add(new Ingredient("Basil", null, false, SPICES));
        masterIngredientsList.add(new Ingredient("Oats", null, false, SPICES));
        // Baking
        masterIngredientsList.add(new Ingredient("Flour", null, false, BAKING));
        masterIngredientsList.add(new Ingredient("Yeast", null, false, BAKING));
        masterIngredientsList.add(new Ingredient("Baking Soda", null, false, BAKING));
        // Oils and Condiments
        masterIngredientsList.add(new Ingredient("Olive Oil", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Ketchup", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Vinegar", null, false, CONDIMENTS));

    }

    public MasterIngredientsList(ArrayList<Ingredient> list) {
        masterIngredientsList = list;
    }

    public ArrayList<Ingredient> getMasterList() {
        return masterIngredientsList;
    }

    public ArrayList<String> getAllCategories() {
        ArrayList<String> categories = new ArrayList<>();
        for (Ingredient i : masterIngredientsList) {
            if (!categories.contains(i.getCategory())) {
                categories.add(i.getCategory());
            }
        }
        return categories;
    }

    public ArrayList<Ingredient> getCategoryList(String category) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        for (Ingredient i : masterIngredientsList) {
            if (i.getCategory().equals(category)) {
                ingredients.add(i);
            }
        }
        return ingredients;
    }

    public int getAllCheckedCount() {
        int checkedCount = 0;
        for (Ingredient i : masterIngredientsList) {
            if (i.getCheckedStatus()) {
                checkedCount++;
            }
        }
        return checkedCount;
    }

    public int getCheckedCount(String category) {
        ArrayList<Ingredient> categoryList = getCategoryList(category);
        int checkedCount = 0;
        for (Ingredient i : categoryList) {
            if (i.getCheckedStatus()) {
                checkedCount++;
            }
        }
        return checkedCount;
    }

    public ArrayList<Ingredient> getCheckedIngredients() {
        ArrayList<Ingredient> checkedIngredientsList = new ArrayList<Ingredient>();
        for (Ingredient i : masterIngredientsList) {
            if (i.getCheckedStatus()) {
                checkedIngredientsList.add(i);
            }
        }
        return checkedIngredientsList;
    }

    public boolean updateList(ArrayList<Ingredient> ingredientsList) {
        // updates an existing ingredients list if category is present, or creates new ingredient
        for (Ingredient ingredient : ingredientsList) {
            boolean found = false;
            int currIndex = 0;
            while (!found && currIndex < masterIngredientsList.size()) {
                if (ingredient.getName().equals(masterIngredientsList.get(currIndex).getName())) {
                    masterIngredientsList.get(currIndex).setCheckedStatus(ingredient.getCheckedStatus());
                    found = true;
                }
                currIndex++;
            }
            if (!found) {
                masterIngredientsList.add(ingredient);
                return true;
            }
        }
        return false;
    }

    public boolean updateList(Ingredient ingredient) {
        // updates an existing ingredient if present, or creates new ingredient
        boolean found = false;
        int currIndex = 0;
        while (!found && currIndex < masterIngredientsList.size()) {
            if (ingredient.getName().equals(masterIngredientsList.get(currIndex).getName())) {
                masterIngredientsList.get(currIndex).setCheckedStatus(ingredient.getCheckedStatus());
                found = true;
            }
            currIndex++;
        }
        if (!found) {
            masterIngredientsList.add(ingredient);
            return true;
        }
        return false;
    }

    public String toString() {
        String ingredientsString = "";
        for (Ingredient i : masterIngredientsList) {
            ingredientsString += i.toString();
        }
        return ingredientsString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeValue(masterIngredientsList);
    }

    private MasterIngredientsList(Parcel in) {
        masterIngredientsList = (ArrayList<Ingredient>) in.readValue(ArrayList.class.getClassLoader());
    }

    public static final Parcelable.Creator<MasterIngredientsList> CREATOR
            = new Parcelable.Creator<MasterIngredientsList>() {

        @Override
        public MasterIngredientsList createFromParcel(Parcel in) {
            return new MasterIngredientsList(in);
        }

        @Override
        public MasterIngredientsList[] newArray(int size) {
            return new MasterIngredientsList[size];
        }
    };
}
