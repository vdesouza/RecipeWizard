package com.recipewizard.recipewizard;

import android.os.Environment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class FavoritesTabManagerActivity extends AppCompatActivity {

    public static final int ADD_FAVORITE_RECIPE_REQUEST = 0;
    public static final int REMOVE_FAVORITE_RECIPE_REQUEST = 1;
    private static final String FILE_NAME = "FavoritesListManagerActivityData.txt";
    private static final String TAG = "FavoritesList";
    private static final String NAME = "name";

    // IDs for menu items
    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_DUMP = Menu.FIRST + 1;

    ListView listView;
    FavoritesListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Favorites Manager activity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.favorites_list);
        // Create a new TodoListAdapter for this ListActivity's ListView
        mAdapter = new FavoritesListAdapter(this, R.layout.recipe_item);

        // Put divider between ToDoItems and FooterView
        listView.setFooterDividersEnabled(true);
        listView.setHeaderDividersEnabled(true);

        LayoutInflater layoutInflator = LayoutInflater.from(FavoritesTabManagerActivity.this);
        listView.setAdapter(mAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // only show options menu on portrait view
        super.onCreateOptionsMenu(menu);
        Log.d(TAG, "Creating options menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_favorites_actions, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_button:
                Intent homeIntent = new Intent(FavoritesTabManagerActivity.this, MainActivity.class);
                startActivity(homeIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */

    @Override
    public void onResume() {
        Log.d(TAG, "in resume");
        super.onResume();

        // Load saved ToDoItems, if necessary
        Log.d(TAG, "Adapter Count = " + mAdapter.getCount());
        if (mAdapter.getCount() == 0) {
            Log.d(TAG, "Loading items");
            loadItems();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "Pausing");

        super.onPause();

        saveItems();

    }
    private void saveImage(Bitmap image, String itemName){
        String filename = itemName + ".png";
        FileOutputStream out = null;
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root, filename);
        Log.d(TAG, "Saving: " + file.toString());
        if(image != null && !file.exists()){
            try {
                out = new FileOutputStream(file);
                image.compress(Bitmap.CompressFormat.PNG,100, out);
                out.flush();
                out.close();
                Log.d(TAG, "Finished Saving: " + out.toString());
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } finally{
                try{
                    if (out != null)
                        out.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    private Bitmap loadImage(String itemName){
        String filename = itemName + ".png";
        String fullPath = Environment.getExternalStorageDirectory()+"/" + filename;
        Log.d(TAG,"Loading: " + fullPath);
        return BitmapFactory.decodeFile(fullPath);
    }
    private void dump() {

        for (int i = 0; i < mAdapter.getCount(); i++) {
            String data = ((Recipe) mAdapter.getItem(i)).toString();
            Log.i(TAG,	"Item " + i + ": " + data.replace(Recipe.ITEM_SEP, ","));
        }

    }

    private boolean[] getAllergyInformationFromLines (String line){
        String allergiesArray[];
        boolean allergyInformation [] = new boolean [Recipe.NUM_ALLERGIES];
        int i = 0;
        line = line.split("\\[")[1];
        line = line.split("]")[0];
        allergiesArray = line.split(",");

        for(String s : allergiesArray){
            allergyInformation[i] = Boolean.parseBoolean(s);
            i++;
        }
        return allergyInformation;
    }

    private List<Step> getStepsFromLine(String line){
        String stepsArray[], singleStepArray[], singleIngredientArray[] ;
        List<Step> steps = new ArrayList<>();
        List<String> equipment = new ArrayList<>();
        String equipmentString [];
        String name;
        String direction;
        boolean checked;
        String category;
        int amount;
        String unit;

        stepsArray = line.split(";");
        for(String s : stepsArray) {
            singleStepArray = s.split(".");
            List<Ingredient> ingredients = new ArrayList<>();
            if (singleStepArray.length > 0) {
                direction = singleStepArray[0];
                for (int i = 1; i < singleStepArray.length - 1; i++) {
                    singleIngredientArray = singleStepArray[i].split(",");
                    if(singleIngredientArray.length > 1) {
                        name = singleIngredientArray[0];
                        checked = Boolean.parseBoolean(singleIngredientArray[1]);
                        category = singleIngredientArray[2];
                        amount = Integer.parseInt(singleIngredientArray[3]);
                        unit = singleIngredientArray[4];
                        ingredients.add(new Ingredient(name, checked, category, amount, unit));
                    }
                }
                equipmentString = singleStepArray[singleStepArray.length -1].split(",");
                if(equipmentString.length > 0);
                for(String e : equipmentString) {
                    equipment.add(e);
                }

                steps.add(new Step(direction, ingredients, equipment));
            }
        }
        return steps;
    }

    // Load stored ToDoItems
    private void loadItems() {
        BufferedReader reader = null;
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(fis));

            Bitmap picture = null;
            String id;
            String name;
            String stepsString;
            String author;
            List<Step> steps = new ArrayList<>();
            String calorieInformationLine [];
            int calories;
            int protein;
            int carbs;
            int fat;
            int likes;
            int servings;
            int cook_time_in_minutes;
            boolean [] allergyInformation;

            while (null != (id = reader.readLine())) {
                name = reader.readLine();
                picture = loadImage(name);
                author = reader.readLine();
                stepsString = reader.readLine();
                steps = getStepsFromLine(stepsString);
                calorieInformationLine = reader.readLine().split(",");
                calories = Integer.parseInt(calorieInformationLine[0]);
                protein = Integer.parseInt(calorieInformationLine[1]);
                carbs = Integer.parseInt(calorieInformationLine[2]);
                fat = Integer.parseInt(calorieInformationLine[3]);
                likes = Integer.parseInt(calorieInformationLine[4]);
                servings = Integer.parseInt(calorieInformationLine[5]);
                cook_time_in_minutes = Integer.parseInt(calorieInformationLine[6]);
                allergyInformation = getAllergyInformationFromLines(reader.readLine());
                mAdapter.add(new Recipe(id, name, author, picture, steps, calories,
                        protein, carbs, fat, likes, servings, cook_time_in_minutes, allergyInformation));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Save ToDoItems to file
    private void saveItems() {
        PrintWriter writer = null;
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    fos)));

            for (int idx = 0; idx < mAdapter.getCount(); idx++) {
                Recipe curr = (Recipe) mAdapter.getItem(idx);
                saveImage(curr.getPicture(), curr.getName());
                writer.println(curr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }
}