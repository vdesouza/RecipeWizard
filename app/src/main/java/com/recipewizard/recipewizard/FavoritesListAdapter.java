package com.recipewizard.recipewizard;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.Log;

import org.w3c.dom.Text;

public class FavoritesListAdapter extends ArrayAdapter<Recipe> {

    private final List<Recipe> mItems = new ArrayList<Recipe>();
    private final int resource;
    private final Context mContext;

    private static final String TAG = "Lab-UserInterface";

    public FavoritesListAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
        this.resource = resource;

    }

    // Add a Badge to the adapter
    // Notify observers that the data set has changed

    public void add(Recipe recipe) {

        mItems.add(recipe);
        notifyDataSetChanged();

    }
    public void remove(Recipe recipe){
        mItems.remove(recipe);
        notifyDataSetChanged();
    }

    // Clears the list adapter of all items.

    public void clear() {

        mItems.clear();
        notifyDataSetChanged();

    }

    // Returns the number of ToDoItems

    @Override
    public int getCount() {

        return mItems.size();

    }

    // Retrieve the number of ToDoItems

    @Override
    public Recipe getItem(int pos) {

        return mItems.get(pos);

    }

    // Get the ID for the Badge
    // In this case it's just the position

    @Override
    public long getItemId(int pos) {

        return pos;

    }
   public void removeRecipeWithName(String name){
       int i = 0;
       for(Recipe r : mItems){
           if(r.getName().compareTo(name) == 0){
               break;
           }
           i++;
       }
       mItems.remove(i);
   }

    // Create a View for the Badge at specified position
    // Remember to check whether convertView holds an already allocated View
    // before created a new View.
    // Consider using the ViewHolder pattern to make scrolling more efficient
    // See: http://developer.android.com/training/improving-layouts/smooth-scrolling.html

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
        star.setImageResource(R.drawable.star_on);
        star.setOnClickListener(new View.OnClickListener() {
            boolean off = false;
            @Override
            public void onClick(View view) {
                if (off) {
                    // recipe was not marked as favorite
                    Log.d(TAG, "This favorites star shouldn't be off");
                    star.setImageResource(R.drawable.star_on);
                    off = false;
                } else {
                    // recipe was marked as favorite
                    star.setImageResource(R.drawable.star_off);
                    RecipeToFile recipeToFile = new RecipeToFile(mItems.get(position), getContext());
                    recipeToFile.removeRecipeFromFavoritesList();
                    Log.d(TAG, "Removing " + mItems.get(position).getName() + " from favorites list");
                    remove(mItems.get(position));
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
}
