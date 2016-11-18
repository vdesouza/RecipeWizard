package com.recipewizard.recipewizard;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;


public class FavoritesTabManagerActivity extends ListActivity {

    public static final int ADD_FAVORITE_RECIPE_REQUEST = 0;
    public static final int REMOVE_FAVORITE_RECIPE_REQUEST = 1;
    private static final String FILE_NAME = "FavoritesListManagerActivityData.txt";
    private static final String TAG = "Lab-UserInterface";
    private static final String NAME = "name";

    // IDs for menu items
    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_DUMP = Menu.FIRST + 1;

    FavoritesListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Create a new TodoListAdapter for this ListActivity's ListView
        mAdapter = new FavoritesListAdapter(this);

        // Put divider between ToDoItems and FooterView
        getListView().setFooterDividersEnabled(true);

        // TODO - Inflate footerView for footer_view.xml file
        TextView footerView = null;
        TextView deleteButtonView = null;
        LayoutInflater layoutInflator = LayoutInflater.from(FavoritesTabManagerActivity.this);
        /*footerView = (TextView) layoutInflator.inflate(R.layout.footer_view, null);
        deleteButtonView = (TextView) layoutInflator.inflate(R.layout.deletebutton_view,null);


        getListView().addFooterView(footerView);
        getListView().addHeaderView(deleteButtonView);





        // TODO - Attach Listener to FooterView
        footerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent intent = new Intent(BadgesManagerActivity.this, AddBadgeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivityForResult(intent, ADD_BADGE_ITEM_REQUEST);
            }
        });

        deleteButtonView.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick (View v){
                Log.d(TAG, "Delte pressed");
                mAdapter.clear();
            }
        });
        */
        getListView().setAdapter(mAdapter);
        // TODO - Attach the adapter to this ListActivity's ListView


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG,"Entered onActivityResult()");

        // TODO - Check result code and request code
        // if user submitted a new Badge
        // Create a new Badge from the data Intent
        // and then add it to the adapter
        if (requestCode == ADD_FAVORITE_RECIPE_REQUEST && resultCode == RESULT_OK) {
            Recipe recipe = new Recipe(data);
            Log.d(TAG, "Adding recipe");
            mAdapter.add(recipe);
            Log.d(TAG, "Badge recipe");
        } else if(requestCode == REMOVE_FAVORITE_RECIPE_REQUEST && resultCode == RESULT_OK){
            String name = data.getStringExtra(NAME);
            mAdapter.removeRecipeWithName(name);

        }
    }

    // Do not modify below here

    @Override
    public void onResume() {
        Log.d(TAG, "in resume");
        super.onResume();

        // Load saved ToDoItems, if necessary

		if (mAdapter.getCount() == 0)
            Log.d(TAG, "Loading items");
			loadItems();

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "Pausing");
        super.onPause();

        saveItems();

    }
    /*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
		menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_DELETE:
			mAdapter.clear();
			return true;
		case MENU_DUMP:
			dump();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    */
    private void saveImage(Bitmap image, String itemName){
        String filename = itemName + ".png";
        FileOutputStream out = null;
        if(!(new File(filename)).exists()){
            try {
                 out = new FileOutputStream(filename);
                image.compress(Bitmap.CompressFormat.PNG,100, out);
            } catch(FileNotFoundException e){
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
        return BitmapFactory.decodeFile(filename);
    }
    private void dump() {

        for (int i = 0; i < mAdapter.getCount(); i++) {
            String data = ((Recipe) mAdapter.getItem(i)).toString();
            Log.i(TAG,	"Item " + i + ": " + data.replace(Recipe.ITEM_SEP, ","));
        }

    }

    private List<Ingredient> getIngredientsFromLine(String line){
        String linearray[];
        List<Ingredient> ingredients = new ArrayList<>();
        Bitmap picture = null;

        line = line.substring(1);
        line = line.split("]")[0];
        linearray = line.split(", ");
        for(String s: linearray){
             picture = loadImage(s);
            ingredients.add(new Ingredient(s, picture));
        }
        return ingredients;
    }
    private List<String> getDirectionsFromLine(String line){
        String linearray[];
        List<String> directions = new ArrayList<>();

        line = line.substring(1);
        line = line.split("]")[0];
        linearray = line.split(", ");
        for(String s: linearray){
            directions.add(s);
        }
        return directions;
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
            String ingredientsString;
            String directionsString;
            List<Ingredient> ingredients = new ArrayList<>();
            List<String> directions = new ArrayList<>();
            String calorieInformationLine [];
            int calories;
            int protein;
            int carbs;
            int fat;
            int likes;

			while (null != (id = reader.readLine())) {
				name = reader.readLine();
                picture = loadImage(name);
				ingredientsString = reader.readLine();
                ingredients = getIngredientsFromLine(ingredientsString);
                directionsString = reader.readLine();
                directions = getDirectionsFromLine(directionsString);
                calorieInformationLine = reader.readLine().split(",");
                calories = Integer.parseInt(calorieInformationLine[0]);
                protein = Integer.parseInt(calorieInformationLine[1]);
                carbs = Integer.parseInt(calorieInformationLine[2]);
                fat = Integer.parseInt(calorieInformationLine[3]);
                likes = Integer.parseInt(calorieInformationLine[4]);
				mAdapter.add(new Recipe(id, name, picture, ingredients, directions, calories,
                        protein, carbs, fat, likes));
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
                for(Ingredient ingredient : curr.getIngredients())
                    saveImage(ingredient.getPicture(), ingredient.getName());
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