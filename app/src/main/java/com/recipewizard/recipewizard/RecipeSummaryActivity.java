package com.recipewizard.recipewizard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Yanru Bi on 12/1/2016.
 */

public class RecipeSummaryActivity extends AppCompatActivity {

    private static final String TAG = "Recipe Wizard: Summary";
    TextView name, ingredients, equipments, summary, calorie, protein, carbs, fat;
    ImageView pic;
    static Recipe recipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_intro);

        // adds back button to action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Bundle extra = getIntent().getExtras();
        recipe = RecipeListAdapter.getRecipe(extra.getInt("position"));

        name = (TextView) findViewById(R.id.recipe_intro_name);
        name.setText(recipe.getName());

        String ings = "";
        String equips = "";
        for (Step step : recipe.getSteps()) {
            ings += (step.getIngredients() + ",");
            equips += (step.getEquipment() + ",");
        }
        ings = ings.substring(0, ings.length()-1);
        ingredients = (TextView) findViewById(R.id.recipe_intro_ingredients);
        ingredients.setText(ings);

        equips = equips.substring(0, equips.length()-1);
        equipments = (TextView) findViewById(R.id.recipe_intro_equiments);
        equipments.setText(equips);

        summary = (TextView) findViewById(R.id.recipe_summary);
        try {
            summary.setText(new GetRecipeSummaryTask().execute(Integer.parseInt(recipe.getId())).get());
        } catch (InterruptedException | ExecutionException e) {
            Log.i(TAG, "exception");
        }

        pic = (ImageView) findViewById(R.id.recipe_intro_image);
        pic.setImageBitmap(recipe.getPicture());

        calorie = (TextView)findViewById(R.id.calories);
        calorie.setText(Integer.toString(recipe.getCalories()));

        protein = (TextView) findViewById(R.id.protein);
        protein.setText(Integer.toString(recipe.getProtein()));

        carbs = (TextView) findViewById(R.id.carbs);
        carbs.setText(Integer.toString(recipe.getCarbs()));

        fat = (TextView) findViewById(R.id.fat);
        fat.setText(Integer.toString(recipe.getFat()));

        FloatingActionButton cook = (FloatingActionButton) findViewById(R.id.recipe_intro_fab);
        cook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeSummaryActivity.this, CookingMode.class);
                //Bundle bundle = new Bundle();
                //bundle.putParcelableArrayList("steps", (ArrayList)recipe.getSteps());
                //bundle.putParcelable("image", recipe.getPicture());
                //intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private String ingredientsToString(List<Ingredient> ingredients){
        String ret = "";
        for (Ingredient i : ingredients) {
            ret += (i.toString() + ", ");
        }
        ret = ret.substring(0, ret.length()-2);
        return ret;
    }
    private String equipmentToString(List<String> equipments) {
        String ret = "";
        for (String s : equipments) {
            ret += (s.toString() + ", ");
        }
        ret = ret.substring(0, ret.length()-2);
        return ret;
    }

    public static ArrayList<Step> getSteps() {
        return (ArrayList<Step>) recipe.getSteps();
    }

    public static Bitmap getImage() {
        return recipe.getPicture();
    }

    public static String getName(){
        return recipe.getName();
    }

}
