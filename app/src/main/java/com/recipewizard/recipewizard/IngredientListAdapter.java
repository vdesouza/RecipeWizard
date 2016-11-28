package com.recipewizard.recipewizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vdesouza on 11/17/16.
 */

public class IngredientListAdapter extends BaseAdapter implements Filterable {

    private static final String TAG = "Recipe Wizard : IngredientListAdapter";

    private List<Ingredient> mItems = new ArrayList<Ingredient>();
    private List<Ingredient> mDisplayedItems = new ArrayList<Ingredient>();
    private final Context mContext;

    public IngredientListAdapter(Context context) {
        mContext = context;
    }

    // Add an Ingredient to the adapter
    // Notify observers that the data set has changed

    public void add(Ingredient item) {
        mItems.add(item);
        mDisplayedItems.add(item);
        notifyDataSetChanged();
    }

    public void remove(Ingredient position) {
        mItems.remove(position);
        mDisplayedItems.remove(position);
        notifyDataSetChanged();
    }

    // Clears the list adapter of all items.
    public void clear() {
        mItems.clear();
        mDisplayedItems.clear();
        notifyDataSetChanged();
    }

    // Returns the number of Ingredients
    @Override
    public int getCount() {
        return mDisplayedItems.size();
    }

    // Retrieve the number of Ingredients
    @Override
    public Object getItem(int pos) {
        return mDisplayedItems.get(pos);
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
        final Ingredient ingredient = mDisplayedItems.get(position);

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

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedItems = (ArrayList<Ingredient>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Ingredient> FilteredArrList = new ArrayList<Ingredient>();

                if (mItems == null) {
                    mItems = new ArrayList<Ingredient>(mDisplayedItems); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = mItems.size();
                    results.values = mItems;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mItems.size(); i++) {
                        String data = mItems.get(i).getName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new Ingredient(mItems.get(i).getName(), null, mItems.get(i).getCheckedStatus(), mItems.get(i).getCategory()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}


