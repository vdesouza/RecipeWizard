package com.recipewizard.recipewizard;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yanru Bi on 12/1/2016.
 */

public class RecipeListAdapter extends ArrayAdapter<Recipe> {

    private static final String TAG = "Recipe Wizard : RecipeListAdapter";

    ArrayList<Recipe> recipeItems;
    Context mContext;
    int resource;

    public RecipeListAdapter(Context context, int resource, ArrayList<Recipe> recipeItems) {
        super(context, resource, recipeItems);
        mContext = context;
        this.recipeItems = recipeItems;
        this.resource = resource;
    }
/*
    // Add an Recipe to the adapter
    // Notify observers that the data set has changed

    public void add(Recipe item) {
        mItems.add(item);
        mDisplayedItems.add(item);
        notifyDataSetChanged();
    }

    public void remove(Recipe position) {
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

    // Returns the number of Recipes
    @Override
    public int getCount() {
        return mDisplayedItems.size();
    }

    // Retrieve the number of Recipes
    @Override
    public Object getItem(int pos) {
        return mDisplayedItems.get(pos);
    }*/

    // Get the ID for the Recipes
    // In this case it's just the position
    @Override
    public long getItemId(int pos) {
        return pos;
    }

    // Create a View for the RecipesList at specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the current Recipes
        final Recipe recipe = getItem(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.recipe_item, null, true);
            System.out.println("layout inflated");
        }
        TextView name = (TextView) convertView.findViewById(R.id.recipe_item_name);
        name.setText(recipe.getName());

        TextView time = (TextView) convertView.findViewById(R.id.recipe_item_time);
        // TODO: call the correct method
        // time.setText(recipe.getTime());

        TextView servings = (TextView) convertView.findViewById(R.id.recipe_item_servings);
        // TODO: call the correct method
        //servings.setText(recipe.getServing());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.recipe_item_image);
        imageView.setImageBitmap(recipe.getPicture());

        final ImageButton star = (ImageButton) convertView.findViewById(R.id.recipe_item_star);
        star.setOnClickListener(new View.OnClickListener() {
            boolean off = true;
            @Override
            public void onClick(View view) {
                if (off) {
                    // recipe was not marked as favorite
                    star.setImageResource(R.drawable.star_on);
                    off = false;
                } else {
                    // recipe was marked as favorite
                    star.setImageResource(R.drawable.star_off);
                    off = true;
                }
            }
        });

        return convertView;
    }

}
