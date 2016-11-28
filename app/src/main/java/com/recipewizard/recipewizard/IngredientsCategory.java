package com.recipewizard.recipewizard;

import android.content.Intent;
import android.util.Log;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by vdesouza on 11/16/16.
 */

public class IngredientsCategory {

    private static final String TAG = "Recipe Wizard:Category";
    public static final String ITEM_SEP = System.getProperty("line.separator");

    public final static String CATEGORY_NAME = "category_name";
    public final static String CHECKED_COUNT = "checked_count";

    private String mCategoryName = new String();
    private String mCheckedCount;

    IngredientsCategory(String name, String count) {
        this.mCategoryName = name;
        this.mCheckedCount = count;
        // TODO - Category Icon
    }

    IngredientsCategory(String name) {
        this.mCategoryName = name;
        // TODO - Category Icon
    }

    // Create a new IngredientsCategory from data packaged in an Intent
    IngredientsCategory(Intent intent) {
        mCategoryName = intent.getStringExtra(IngredientsCategory.CATEGORY_NAME);
        mCheckedCount = intent.getStringExtra(IngredientsCategory.CHECKED_COUNT);
        // TODO - Category Icon
    }

    // Take a set of String data values and package them for transport in an Intent
    public static void packageIntent(Intent intent, String category_name, String checked_count) {
        intent.putExtra(IngredientsCategory.CATEGORY_NAME, category_name);
        intent.putExtra(IngredientsCategory.CHECKED_COUNT, checked_count);
        // TODO - Category Icon
    }

    public String getCategoryName() {
        return mCategoryName;
    }
    public void setCategoryName(String name) {
        mCategoryName = name;
    }

    public String getCheckedCount() { return mCheckedCount; }
    public void setCheckedCount(String count) {
        mCheckedCount = count;
    }

    // TODO - getter/setter Category Icon

    public String toString() {
        return mCategoryName + ITEM_SEP + mCheckedCount + ITEM_SEP + "\n";
    }

    public String toLog() {
        return "Category Name:" + mCategoryName + ITEM_SEP + "Checked Count:" + mCheckedCount + ITEM_SEP + "\n";
    }

}
