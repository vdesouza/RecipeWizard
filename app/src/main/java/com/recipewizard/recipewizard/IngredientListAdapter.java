package com.recipewizard.recipewizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vdesouza on 11/17/16.
 */

public class IngredientListAdapter extends BaseAdapter {

    private static final String TAG = "Recipe Wizard : IngredientListAdapter";

    private final List<Ingredient> mItems = new ArrayList<Ingredient>();
    private final Context mContext;

    public IngredientListAdapter(Context context) {
        mContext = context;
    }

    // Add an Ingredient to the adapter
    // Notify observers that the data set has changed

    public void add(Ingredient item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    // Clears the list adapter of all items.
    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    // Returns the number of Ingredients
    @Override
    public int getCount() {
        return mItems.size();
    }

    // Retrieve the number of Ingredients
    @Override
    public Object getItem(int pos) {
        return mItems.get(pos);
    }

    // Get the ID for the Ingredients
    // In this case it's just the position
    @Override
    public long getItemId(int pos) {
        return pos;
    }

    static class ViewHolder {
        private CheckBox ingredientCheckBox;
    }

    // Create a View for the IngredientsList at specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the current ingredientsCategory
        final Ingredient ingredient = mItems.get(position);

        RelativeLayout itemLayout = (RelativeLayout) convertView;
        final IngredientListAdapter.ViewHolder holder;

        // Inflate the View for this Ingredients Category from ingredients_category.xml
        if (itemLayout == null) {
            itemLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(
                    R.layout.ingredient, parent, false);
            holder = new IngredientListAdapter.ViewHolder();
            holder.ingredientCheckBox = (CheckBox) itemLayout.findViewById(R.id.ingredientCheckBox);
            itemLayout.setTag(holder);
            holder.ingredientCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View v) {
                    ingredient.setCheckedStatus(holder.ingredientCheckBox.isChecked());
                }

            });
        }
        else {
            itemLayout = (RelativeLayout) convertView;
            holder = (IngredientListAdapter.ViewHolder) itemLayout.getTag();
        }

        // Fill in specific Ingredient data
        // Display Ingredient name in TextView
        holder.ingredientCheckBox.setText(ingredient.getName());

        // Display check status of CheckBox
        holder.ingredientCheckBox.setChecked(ingredient.getCheckedStatus());

        return itemLayout;
    }

}
