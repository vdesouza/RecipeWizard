package com.recipewizard.recipewizard;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArraySet;
import android.util.Log;

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

    private static final String TAG = "Recipe Wizard";

    // Arraylist of Ingredient objects
    private ArrayList<Ingredient> masterIngredientsList = new ArrayList<Ingredient>();

    // List default generated categories
    static final String GRAINS = "Grains, Legumes, Nuts, and Seeds";
    static final String MEATS = "Meats";
    static final String FRUITS = "Fruits";
    static final String VEGETABLES = "Vegetables";
    static final String SEASONINGS = "Seasonings";
    static final String BAKING = "Baking";
    static final String CANNED = "Canned and Bottled Goods";
    static final String CONDIMENTS = "Oils, Vinegars, and Condiments";
    static final String DAIRY = "Dairy";



    public MasterIngredientsList() {

        // Oils, Vinegars and Condiments
        masterIngredientsList.add(new Ingredient("Canola Oil", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Extra-Virgin Olive Oil", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Sesame Oil", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Grapeseed Oil", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Coconut Oil", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Toasted Sesame", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Balsamic Vinegar", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Distilled White Vinegar", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Apple Cider Vinegar", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Red Wine Vinegar", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Rice Vinegar", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Ketchup", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Mayonnaise", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Yellow mustard", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Dijon mustard", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Sriracha", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Hoisin sauce", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Soy sauce", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Chili paste", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Hot sauce", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("BBQ sauce", null, false, CONDIMENTS));
        masterIngredientsList.add(new Ingredient("Worcestershire", null, false, CONDIMENTS));
        // Spices and Herbs
        masterIngredientsList.add(new Ingredient("Kosher salt", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Sea salt", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Black peppercorns", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Bay leaves", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Cayenne pepper", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Crushed red pepper", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Garlic", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Basil", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Cumin", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Ground coriander", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Oregano", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Paprika", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Rosemary", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Thyme leaves", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Turmric", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Cinnamon", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Cloves", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Allspice", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Ginger", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Nutmeg", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Chili powder", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Curry powder", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Italian seasoning", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Vanilla extract", null, false, SEASONINGS));
        masterIngredientsList.add(new Ingredient("Cocoa powder", null, false, SEASONINGS));
        // Canned and Bottled Goods
        masterIngredientsList.add(new Ingredient("Black beans", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Cannellini beans", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Chickpeas", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Kidney beans", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Capers", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Olives", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Peanut butter", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Preserves", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Jelly", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Pumpkin Purée", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Stock", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Broth", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Canned tomatoes", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Tomatoe paste", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Pasta Sauce", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Coconut Milk", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Salsa", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Tuna fish", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Anchovies", null, false, CANNED));
        masterIngredientsList.add(new Ingredient("Maple syrup", null, false, CANNED));
        // Grains, Legumes, Nuts, and Seeds
        masterIngredientsList.add(new Ingredient("Breadcrumbs", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Panko", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Couscous", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Corn meal", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Corn starch", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Dried lentils", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Pasta", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Rice", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Rolled oats", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Oatmeal", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Barley", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Millet", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Quinoa", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Wheatberries", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Peanuts", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Almonds", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Hazelnuts", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Sunflower Seeds", null, false, GRAINS));
        masterIngredientsList.add(new Ingredient("Pumpkin Seeds", null, false, GRAINS));
        // Baking
        masterIngredientsList.add(new Ingredient("Baking powder", null, false, BAKING));
        masterIngredientsList.add(new Ingredient("Baking soda", null, false, BAKING));
        masterIngredientsList.add(new Ingredient("Brown sugar", null, false, BAKING));
        masterIngredientsList.add(new Ingredient("Cornstarch", null, false, BAKING));
        masterIngredientsList.add(new Ingredient("All-purpose flour", null, false, BAKING));
        masterIngredientsList.add(new Ingredient("Brown sugar", null, false, BAKING));
        masterIngredientsList.add(new Ingredient("Granulated sugar", null, false, BAKING));
        masterIngredientsList.add(new Ingredient("Confectioners sugar", null, false, BAKING));
        masterIngredientsList.add(new Ingredient("Honey", null, false, BAKING));
        masterIngredientsList.add(new Ingredient("Yeast", null, false, BAKING));
        masterIngredientsList.add(new Ingredient("Chocolate chips", null, false, BAKING));
        // Dairy
        masterIngredientsList.add(new Ingredient("Butter", null, false, DAIRY));
        masterIngredientsList.add(new Ingredient("Cheddar", null, false, DAIRY));
        masterIngredientsList.add(new Ingredient("Feta", null, false, DAIRY));
        masterIngredientsList.add(new Ingredient("Parmesan", null, false, DAIRY));
        masterIngredientsList.add(new Ingredient("Mozzarella", null, false, DAIRY));
        masterIngredientsList.add(new Ingredient("Ice cream", null, false, DAIRY));
        masterIngredientsList.add(new Ingredient("Whipped cream", null, false, DAIRY));
        masterIngredientsList.add(new Ingredient("Sour cream", null, false, DAIRY));
        masterIngredientsList.add(new Ingredient("Milk", null, false, DAIRY));
        masterIngredientsList.add(new Ingredient("Plain yogurt", null, false, DAIRY));
        masterIngredientsList.add(new Ingredient("Greek yogurt", null, false, DAIRY));
        // Fruits
        masterIngredientsList.add(new Ingredient("Apples", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Banana", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Grapes", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Blackberries", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Blueberries", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Raspberries", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Cranberries", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Mangoes", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Peaches", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Strawberries", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Apricots", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Raisins", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Plums", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Prunes", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Lemons", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Limes", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Oranges", null, false, FRUITS));
        masterIngredientsList.add(new Ingredient("Pomegranate", null, false, FRUITS));
        // Vegetables
        masterIngredientsList.add(new Ingredient("Broccoli", null, false, VEGETABLES));
        masterIngredientsList.add(new Ingredient("Kale", null, false, VEGETABLES));
        masterIngredientsList.add(new Ingredient("Carrots", null, false, VEGETABLES));
        masterIngredientsList.add(new Ingredient("Bell pepper", null, false, VEGETABLES));
        masterIngredientsList.add(new Ingredient("Corn", null, false, VEGETABLES));
        masterIngredientsList.add(new Ingredient("Edamame", null, false, VEGETABLES));
        masterIngredientsList.add(new Ingredient("Peas", null, false, VEGETABLES));
        masterIngredientsList.add(new Ingredient("Spinach", null, false, VEGETABLES));
        masterIngredientsList.add(new Ingredient("Lettuce", null, false, VEGETABLES));
        masterIngredientsList.add(new Ingredient("Onions", null, false, VEGETABLES));
        masterIngredientsList.add(new Ingredient("Potatoes", null, false, VEGETABLES));
        masterIngredientsList.add(new Ingredient("Shallots", null, false, VEGETABLES));
        masterIngredientsList.add(new Ingredient("Leeks", null, false, VEGETABLES));
        // Meats
        masterIngredientsList.add(new Ingredient("Chicken", null, false, MEATS));
        masterIngredientsList.add(new Ingredient("Ground beef", null, false, MEATS));
        masterIngredientsList.add(new Ingredient("Lamb", null, false, MEATS));
        masterIngredientsList.add(new Ingredient("Eggs", null, false, MEATS));
        masterIngredientsList.add(new Ingredient("Bacon", null, false, MEATS));
        masterIngredientsList.add(new Ingredient("Shrimp", null, false, MEATS));
        masterIngredientsList.add(new Ingredient("Salmon", null, false, MEATS));

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

    public int getCategoryCount (String category) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        for (Ingredient i : masterIngredientsList) {
            if (i.getCategory().equals(category)) {
                ingredients.add(i);
            }
        }
        return ingredients.size();
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

    public void removeList(ArrayList<Ingredient> ingredientsList) {
        for (Ingredient ingredient : ingredientsList) {
            int currIndex = 0;
            while (currIndex < masterIngredientsList.size()) {
                if (ingredient.getName().equals(masterIngredientsList.get(currIndex).getName())) {
                    masterIngredientsList.remove(currIndex);
                }
                currIndex++;
            }
        }
    }

    public void removeIngredient(Ingredient ingredient) {
        int currIndex = 0;
        while (currIndex < masterIngredientsList.size()) {
            if (ingredient.getName().equals(masterIngredientsList.get(currIndex).getName())) {
                masterIngredientsList.remove(currIndex);
            }
            currIndex++;
        }
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
