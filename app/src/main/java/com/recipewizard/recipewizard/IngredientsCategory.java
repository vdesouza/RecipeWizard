package com.recipewizard.recipewizard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
    public final static String CATEGORY_ICON = "category_name";

    private String mCategoryName = new String();
    private String mIconName;

    IngredientsCategory(String name, String icon) {
        this.mCategoryName = name;
        this.mIconName = icon;
    }

    // Create a new IngredientsCategory from data packaged in an Intent
    IngredientsCategory(Intent intent) {
        mCategoryName = intent.getStringExtra(IngredientsCategory.CATEGORY_NAME);
        mIconName = intent.getStringExtra(IngredientsCategory.CATEGORY_ICON);
    }

    // Take a set of String data values and package them for transport in an Intent
    public static void packageIntent(Intent intent, String category_name, String icon_name) {
        intent.putExtra(IngredientsCategory.CATEGORY_NAME, category_name);
        intent.putExtra(IngredientsCategory.CATEGORY_ICON, icon_name);
    }

    public String getCategoryName() {
        return mCategoryName;
    }
    public void setCategoryName(String name) {
        mCategoryName = name;
    }
    public String getIconName() { return mIconName; };
    public void setIconName(String iconName) { mIconName = iconName; };

    public String toString() {
        return mCategoryName + ITEM_SEP + "\n";
    }

    public String toLog() {
        return "Category Name:" + mCategoryName + ITEM_SEP  + "\n";
    }

}
