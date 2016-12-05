package com.recipewizard.recipewizard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Yanru Bi on 12/1/2016.
 */

public class RecipeSummaryActivity extends AppCompatActivity {

    private static final String TAG = "Recipe Wizard: Summary";
    private TextView name, ingredients, equipments, summary, calorie, protein, carbs, fat;
    private ImageView vege, vegan, gf, df, pic;
    private static ArrayList<Step> stepsArr;
    String ings = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_intro);

        // adds back button to action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Bundle extra = getIntent().getExtras();
        final Recipe recipe;
        if(extra.getString("UniqueId").compareTo("Recipes") == 0) {
            recipe = RecipeListAdapter.getRecipe(extra.getInt("position"));
        } else{
            recipe = (new RecipeToFile(null, getApplicationContext())).getFavoritesListFromFile().get(extra.getInt("position"));
        }

        stepsArr = (ArrayList<Step>) recipe.getSteps();

        name = (TextView) findViewById(R.id.recipe_intro_name);
        name.setText(recipe.getName());

        List<String> equips = new ArrayList<String> ();
        for (Step step : recipe.getSteps()) {
            if (!step.getIngredients().isEmpty()) {
                String tmp = ingredientsToString(step.getIngredients());
                if (!tmp.isEmpty()) ings += (tmp + ", ");
            }
            if (!step.getEquipment().isEmpty()) {
                for(String e : step.getEquipment())
                    if(!equips.contains(e))
                        equips.add(e);
            }
        }
        ingredients = (TextView) findViewById(R.id.recipe_intro_ingredients);
        if (!ings.isEmpty()) {
            ings = ings.substring(0, ings.length() - 2);
            ingredients.setText(ings.replace(", ", "\n"));
        } else {
            ingredients.setText("NONE");
        }

        equipments = (TextView) findViewById(R.id.recipe_intro_equiments);
        if (!equips.isEmpty()) {
            equipments.setText(TextUtils.join("\n", equips));
        } else {
            equipments.setText("NONE");
        }

        summary = (TextView) findViewById(R.id.recipe_summary);
        try {
            summary.setText(new GetRecipeSummaryTask().execute(Integer.parseInt(recipe.getId())).get());
        } catch (InterruptedException | ExecutionException e) {
            Log.i(TAG, "exception");
        }

        pic = (ImageView) findViewById(R.id.recipe_intro_image);
        pic.setImageBitmap(recipe.getPicture());

        calorie = (TextView)findViewById(R.id.calories);
        if (recipe.getCalories() == -1) {
            calorie.setText("?");
            ImageView cal_bg = (ImageView) findViewById(R.id.calorie_bg);
            cal_bg.setImageResource(R.drawable.info_black);
        } else {
            calorie.setText(Integer.toString(recipe.getCalories()));
        }

        protein = (TextView) findViewById(R.id.protein);
        if (recipe.getProtein() == -1) {
            protein.setText("?");
            ImageView pro_bg = (ImageView) findViewById(R.id.protein_bg);
            pro_bg.setImageResource(R.drawable.info_black);

        } else {
            protein.setText(Integer.toString(recipe.getProtein()));
        }

        carbs = (TextView) findViewById(R.id.carbs);
        if (recipe.getCarbs() == -1) {
            carbs.setText("?");
            ImageView car_bg = (ImageView) findViewById(R.id.carbs_bg);
            car_bg.setImageResource(R.drawable.info_black);
        } else {
            carbs.setText(Integer.toString(recipe.getCarbs()));
        }

        fat = (TextView) findViewById(R.id.fat);
        if (recipe.getFat() == -1) {
            fat.setText("?");
            ImageView fat_bg = (ImageView) findViewById(R.id.fat_bg);
            fat_bg.setImageResource(R.drawable.info_black);
        } else {
            fat.setText(Integer.toString(recipe.getFat()));
        }

        if (recipe.getAllergyInformation()[0] == false) {
            vege = (ImageView) findViewById(R.id.vege_bg);
            vege.setImageResource(R.drawable.nutri_black);
        }
        if (recipe.getAllergyInformation()[1] == false) {
            vegan = (ImageView) findViewById(R.id.vegan_bg);
            vegan.setImageResource(R.drawable.nutri_black);
        }
        if (recipe.getAllergyInformation()[2] == false) {
            gf = (ImageView) findViewById(R.id.gluten_bg);
            gf.setImageResource(R.drawable.nutri_black);
        }
        if (recipe.getAllergyInformation()[3] == false) {
            df = (ImageView) findViewById(R.id.dairy_bg);
            df.setImageResource(R.drawable.nutri_black);
        }

        FloatingActionButton cook = (FloatingActionButton) findViewById(R.id.recipe_intro_fab);
        cook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeSummaryActivity.this, CookingModeLoaderActivity.class);
                Bundle bundle = new Bundle();
                //bundle.putParcelableArrayList("steps", (ArrayList)recipe.getSteps());
                bundle.putString("name", recipe.getName());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String ingredientsToString(List<Ingredient> ingredients){
        String ret = "";
        if (ingredients.isEmpty()) return ret;
        for (Ingredient i : ingredients) {
            if (i != null) {
                if (i.getName().length() > 0 && !(i.getName().equals("null")) && !ings.contains(i.getName())) {
                    ret += (i.toStringDisplay() + ", ");
                }
            }
        }
        if (!ret.isEmpty()) {
            ret = ret.substring(0, ret.length() - 2);
        }
        return ret;
    }
    private String equipmentToString(List<String> equipments) {
        String ret = "";
        if (equipments.isEmpty()) return ret;
        for (String s : equipments) {
            if (!s.isEmpty()) {
                if (s.length() > 0 && !(s.equals("null"))) ret += (s + ", ");
            }
        }
        if (!ret.isEmpty()) {
            ret = ret.substring(0, ret.length() - 2);
        }
        return ret;
    }

    public static ArrayList<Step> getSteps() {
        return stepsArr;
    }

}
