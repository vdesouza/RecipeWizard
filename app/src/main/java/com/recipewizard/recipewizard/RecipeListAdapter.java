package com.recipewizard.recipewizard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

    static ArrayList<Recipe> recipeItems;
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
    }*/

    // Retrieve the Recipe
    public static Recipe getRecipe(int pos) {
        return recipeItems.get(pos);
    }

    // Create a View for the RecipesList at specified position
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the current Recipes
        final Recipe recipe = getItem(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.recipe_item, null, true);
        }
        TextView name = (TextView) convertView.findViewById(R.id.recipe_item_name);
        name.setText(recipe.getName());

        TextView time = (TextView) convertView.findViewById(R.id.recipe_item_time);
        time.setText(Integer.toString(recipe.getCookTime())+" min");

        TextView servings = (TextView) convertView.findViewById(R.id.recipe_item_servings);
        servings.setText(Integer.toString(recipe.getServings()) + " servings");

        final ImageButton star = (ImageButton) convertView.findViewById(R.id.recipe_item_star);
        star.setOnClickListener(new View.OnClickListener() {
            boolean off = true;
            @Override
            public void onClick(View view) {
                RecipeToFile recipeToFile = new RecipeToFile(recipe, getContext());
                if (off) {
                    // recipe was not marked as favorite
                    star.setImageResource(R.drawable.star_on);
                    recipeToFile.addRecipeToFavoritesList();
                    off = false;
                } else {
                    // recipe was marked as favorite
                    star.setImageResource(R.drawable.star_off);
                    recipeToFile.removeRecipeFromFavoritesList();
                    off = true;
                }
            }
        });

        /*ImageView imageView = (ImageView) convertView.findViewById(R.id.recipe_item_image_button);
        imageView.setImageBitmap(recipe.getPicture());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RecipeSummaryActivity.class);
                Recipe.packageIntent(intent, recipe.getId(), recipe.getAuthor(), recipe.getName(),
                        recipe.getPicture(), (ArrayList<Step>) recipe.getSteps(), recipe.getCalories(),
                        recipe.getProtein(), recipe.getCarbs(), recipe.getFat(),recipe.getLikes(),
                        recipe.getServings(), recipe.getCookTime(), recipe.getAllergyInformation());
                getContext().startActivity(intent);
            }
        });*/
        ImageButton button = (ImageButton) convertView.findViewById(R.id.recipe_item_image_button);
        button.setImageBitmap(recipe.getPicture());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RecipeSummaryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                /*Recipe.packageIntent(intent, recipe.getId(), recipe.getAuthor(), recipe.getName(),
                        recipe.getPicture(), (ArrayList<Step>) recipe.getSteps(), recipe.getCalories(),
                        recipe.getProtein(), recipe.getCarbs(), recipe.getFat(), recipe.getLikes(),
                        recipe.getServings(), recipe.getCookTime(), recipe.getAllergyInformation());*/
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }

}
