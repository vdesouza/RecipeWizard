package com.recipewizard.recipewizard;

import android.content.Intent;

/**
 * Created by vdesouza on 11/17/16.
 */

public class Ingredient {

    private static final String TAG = "Recipe Wizard : Ingredient";
    public static final String ITEM_SEP = System.getProperty("line.separator");

    public final static String INGREDIENT_NAME = "ingredient_name";
    public final static String CHECKED = "checked";

    private String mName = new String();
    private boolean mChecked;

    Ingredient(String name, boolean checked) {
        this.mName = name;
        this.mChecked = checked;
    }

    // Create a new Ingredient from data packaged in an Intent
    Ingredient(Intent intent) {
        mName = intent.getStringExtra(Ingredient.INGREDIENT_NAME);
        mChecked = Boolean.valueOf(intent.getStringExtra(Ingredient.CHECKED));
    }

    // Take a set of String data values and package them for transport in an Intent
    public static void packageIntent(Intent intent, String name, String checked) {
        intent.putExtra(Ingredient.INGREDIENT_NAME, name);
        intent.putExtra(Ingredient.CHECKED, checked);
    }

    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }

    public boolean getCheckedStatus() {
        return mChecked;
    }
    public void setCheckedStatus(boolean checked) {
        mChecked = checked;
    }


    public String toString() {
        return mName + ITEM_SEP + mChecked + ITEM_SEP + "\n";
    }

    public String toLog() {
        return "Category Name:" + mName + ITEM_SEP + "Checked Count:" + mChecked + ITEM_SEP + "\n";
    }
}
