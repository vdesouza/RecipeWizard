package com.recipewizard.recipewizard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vdesouza on 11/16/16.
 */

public class IngredientsCategoryAdapter extends BaseAdapter {

    private static final String TAG = "Recipe Wizard : Adapter";

    static final int NOT_FOUND = -1;

    private final List<IngredientsCategory> mItems = new ArrayList<IngredientsCategory>();
    private final Context mContext;

    public IngredientsCategoryAdapter(Context context) {
        mContext = context;
    }

    // Add an IngredientsCategory to the adapter
    // Notify observers that the data set has changed

    public void add(IngredientsCategory item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    // Clears the list adapter of all items.
    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public int getCategoryIndex(String categoryName) {
        int index;
        for (index = 0; index < mItems.size(); index++) {
            if (mItems.get(index).getCategoryName().equals(categoryName)) {
                return index;
            }
        }
        return NOT_FOUND;
    }



    // Returns the number of IngredientsCategories
    @Override
    public int getCount() {
        return mItems.size();
    }

    // Retrieve the number of IngredientsCategories
    @Override
    public Object getItem(int pos) {
        return mItems.get(pos);
    }

    // Get the ID for the IngredientsCategories
    // In this case it's just the position
    @Override
    public long getItemId(int pos) {
        return pos;
    }

    static class ViewHolder {
        private RelativeLayout ingredientsCategoryRelativeLayout;
        private ImageView ingredientsCategoryIconImageView;
        private TextView ingredientsCategoryNameTextView;
        private TextView ingredientsCategoryCountTextView;
    }

    // Create a View for the IngredientsCategory at specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the current ingredientsCategory
        final IngredientsCategory ingredientsCategory = mItems.get(position);

        RelativeLayout itemLayout = (RelativeLayout) convertView;
        final ViewHolder holder;

        // Inflate the View for this Ingredients Category from ingredients_category.xml
        if (itemLayout == null) {
            itemLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(
                    R.layout.ingredients_category, parent, false);
            holder = new ViewHolder();
            holder.ingredientsCategoryRelativeLayout = (RelativeLayout) itemLayout.findViewById(R.id.ingredientsCategoryRelativeLayout);
            holder.ingredientsCategoryIconImageView = (ImageView) itemLayout.findViewById(R.id.ingredientsCategoryIcon);
            holder.ingredientsCategoryNameTextView = (TextView) itemLayout.findViewById(R.id.ingredientsCategoryName);
            holder.ingredientsCategoryCountTextView = (TextView) itemLayout.findViewById(R.id.ingredientsCategoryCount);
            itemLayout.setTag(holder);
        }
        else {
            itemLayout = (RelativeLayout) convertView;
            holder = (ViewHolder) itemLayout.getTag();
        }

        // Fill in specific IngredientsCategory data
        // Display Ingredients Category name in TextView
        holder.ingredientsCategoryNameTextView.setText(ingredientsCategory.getCategoryName());

        // Set equal Width and Height for Categories
        holder.ingredientsCategoryRelativeLayout.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams mParams;
                mParams = holder.ingredientsCategoryRelativeLayout.getLayoutParams();
                mParams.height = holder.ingredientsCategoryRelativeLayout.getWidth();
                holder.ingredientsCategoryRelativeLayout.setLayoutParams(mParams);
                holder.ingredientsCategoryRelativeLayout.postInvalidate();
            }
        });

        // TODO - get category icon

        return itemLayout;
    }

}
