package com.recipewizard.recipewizard;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by vdesouza on 11/18/16.
 */

public class MasterIngredientsList implements Serializable {

    // HashMap that is returned
    private HashMap<String, ArrayList<Ingredient>> masterIngredientsList =
            new HashMap<String, ArrayList<Ingredient>>();

    // List default generated categories
    private final String DRY_GOODS = "Dry Goods";
    private final String MEATS = "Meats";
    private final String FRUITS = "Fruits";
    private final String VEGETABLES = "Vegetables";
    private final String SPICES = "Spices and Herbs";
    private final String BAKING = "Baking";
    private final String CONDIMENTS = "Oils and Condiments";

    public MasterIngredientsList() {
        // Dry Goods
        ArrayList<Ingredient> ingredientsListDryGoods = new ArrayList<Ingredient>();
        ingredientsListDryGoods.add(new Ingredient("Pasta", null, false));
        ingredientsListDryGoods.add(new Ingredient("Rice", null, false));
        ingredientsListDryGoods.add(new Ingredient("Oats", null, false));
        masterIngredientsList.put(DRY_GOODS, ingredientsListDryGoods);

        // Meats
        ArrayList<Ingredient> ingredientsListMeats = new ArrayList<Ingredient>();
        ingredientsListMeats.add(new Ingredient("Chicken", null, false));
        ingredientsListMeats.add(new Ingredient("Beef", null, false));
        ingredientsListMeats.add(new Ingredient("Lamb", null, false));
        masterIngredientsList.put(MEATS, ingredientsListMeats);

        // Fruits
        ArrayList<Ingredient> ingredientsListFruits = new ArrayList<Ingredient>();
        ingredientsListFruits.add(new Ingredient("Apple", null, false));
        ingredientsListFruits.add(new Ingredient("Banana", null, false));
        ingredientsListFruits.add(new Ingredient("Grapes", null, false));
        masterIngredientsList.put(FRUITS, ingredientsListFruits);

        // Vegetables
        ArrayList<Ingredient> ingredientsListVegetables = new ArrayList<Ingredient>();
        ingredientsListVegetables.add(new Ingredient("Broccoli", null, false));
        ingredientsListVegetables.add(new Ingredient("Kale", null, false));
        ingredientsListVegetables.add(new Ingredient("Carrots", null, false));
        masterIngredientsList.put(VEGETABLES, ingredientsListVegetables);

        // Spices and Herbs
        ArrayList<Ingredient> ingredientsListSpicesHerbs = new ArrayList<Ingredient>();
        ingredientsListSpicesHerbs.add(new Ingredient("Oregano", null, false));
        ingredientsListSpicesHerbs.add(new Ingredient("Basil", null, false));
        ingredientsListSpicesHerbs.add(new Ingredient("Oats", null, false));
        masterIngredientsList.put(SPICES, ingredientsListSpicesHerbs);

        // Baking
        ArrayList<Ingredient> ingredientsListBaking = new ArrayList<Ingredient>();
        ingredientsListBaking.add(new Ingredient("Flour", null, false));
        ingredientsListBaking.add(new Ingredient("Yeast", null, false));
        ingredientsListBaking.add(new Ingredient("Baking Soda", null, false));
        masterIngredientsList.put(BAKING, ingredientsListBaking);

        // Oils and Condiments
        ArrayList<Ingredient> ingredientsListOilsCondiments = new ArrayList<Ingredient>();
        ingredientsListOilsCondiments.add(new Ingredient("Olive Oil", null, false));
        ingredientsListOilsCondiments.add(new Ingredient("Ketchup", null, false));
        ingredientsListOilsCondiments.add(new Ingredient("Vinegar", null, false));
        masterIngredientsList.put(CONDIMENTS, ingredientsListOilsCondiments);
    }

    public MasterIngredientsList(HashMap list) {
        masterIngredientsList = list;
    }

    public HashMap getMasterList() {
        return masterIngredientsList;
    }

    public Set<String> getAllCategories() {
        return masterIngredientsList.keySet();
    }

    public ArrayList<Ingredient> getCategoryList(String category) {
        return masterIngredientsList.get(category);
    }

    public int getCheckedCount(String category) {
        int checkedCount = 0;
        for (Ingredient i : masterIngredientsList.get(category)) {
            if (i.getCheckedStatus()) {
                checkedCount++;
            }
        }
        return checkedCount;
    }

    public ArrayList<Ingredient> getCheckedIngredients() {
        ArrayList<Ingredient> checkedIngredientsList = new ArrayList<Ingredient>();
        for (String category : masterIngredientsList.keySet()) {
            for (Ingredient i : masterIngredientsList.get(category)) {
                if (i.getCheckedStatus()) {
                    checkedIngredientsList.add(i);
                }
            }
        }
        return checkedIngredientsList;
    }

    public void updateList(String category, ArrayList<Ingredient> ingredientsList) {
        // updates an existing ingredients list if category is present, or creates new category
        masterIngredientsList.put(category, ingredientsList);
    }

    public String toString() {
        String ingredientsString = "Master Ingredient List: \n";

        for (String category : masterIngredientsList.keySet()) {
            ingredientsString += category + ": \n";
            for (Ingredient i : masterIngredientsList.get(category)) {
                ingredientsString += i.toString();
            }
            ingredientsString += "\n";
        }

        return ingredientsString;
    }
}
