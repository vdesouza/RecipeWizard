package com.recipewizard.recipewizard;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vdesouza on 11/17/16.
 */

public class IngredientListActivity extends AppCompatActivity {

    private static final String TAG = "Recipe Wizard : List";

    private static final int INGREDIENTS_LIST_REQUEST = 0;
    private static final String INGREDIENTS_LIST = "IngredientsList";
    private static final String CATEGORY_NAME = "CategoryName";
    private static final String CATEGORY_INDEX = "CategoryIndex";

    private ListView mIngredientsListView;
    private IngredientListAdapter mAdapter;
    private ArrayList<Ingredient> mIngredientsList;
    private String fromCategory;
    private int fromCategoryIndex;

    final protected ArrayList<Ingredient> newIngredientsAddedList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients_list);

        // adds back button to action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIngredientsListView = (ListView) findViewById(R.id.ingredientsListView);

        // Create a new IngredientListAdapter for the ListView
        mAdapter = new IngredientListAdapter(getApplicationContext());

        // Put divider between ToDoItems and HeaderView
        mIngredientsListView.setHeaderDividersEnabled(true);

        // Inflate headerView
        EditText headerView = (EditText) getLayoutInflater().inflate(R.layout.ingredients_list_header_view, null);

        // Put divider between ToDoItems and FooterView
        mIngredientsListView.setFooterDividersEnabled(true);

        // Inflate headerView for footer_view.xml file
        TextView footerView = (TextView) getLayoutInflater().inflate(R.layout.ingredients_list_footer_view, null);
        footerView.setText(R.string.add_new_ingredient);

        // Add headerView and footerView to ListView
        mIngredientsListView.addHeaderView(headerView);
        mIngredientsListView.addFooterView(footerView);


        // Attach Listener to FooterView
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());

                alert.setTitle("Add new ingredient?");
                alert.setMessage("Type in name of new ingredient for this category.\n" +
                        "Caution: not all ingredient names may work with recipe search API.");

                // Set an EditText view to get user input
                final EditText input = new EditText(v.getContext());
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String newIngredientName = (input.getText().toString());
                        Ingredient newIngredient = new Ingredient(newIngredientName, null, false, fromCategory);
                        newIngredientsAddedList.add(newIngredient);
                        mAdapter.add(newIngredient);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });

        // long click to delete an item
        mIngredientsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(parent.getContext());
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setTitle("Delete Ingredient?");
                alert.setMessage("Are you sure? This cannot be undone.");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Ingredient ingredientToRemove = (Ingredient) mAdapter.getItem(position - 1);
                        mIngredientsList.remove(ingredientToRemove);
                        newIngredientsAddedList.remove(ingredientToRemove);
                        mAdapter.remove(ingredientToRemove);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //canceled
                    }
                });
                alert.show();
                return true;
            }
        });

        // build the list adapter from an intent
        if (getIntent().getExtras() != null) {
            mIngredientsList = (ArrayList<Ingredient>) getIntent().getSerializableExtra("IngredientsList");
            for (Ingredient i : mIngredientsList) {
                Log.i(TAG, i.toString());
                mAdapter.add(i);
            }
            fromCategory = getIntent().getStringExtra(CATEGORY_NAME);
            fromCategoryIndex = getIntent().getIntExtra(CATEGORY_INDEX, -1);
            // sets action bar title
            setTitle(fromCategory);
        }

        // set up search function
        // Add Text Change Listener to EditText
        headerView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                mAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Attach the adapter to ListActivity's ListView
        mIngredientsListView.setAdapter(mAdapter);
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


    @Override
    public void onBackPressed() {
        Intent data = new Intent();

        mIngredientsList.addAll(newIngredientsAddedList);
        // Create intent to send back to main activity when back button is pressed.
        data.putExtra(CATEGORY_NAME, fromCategory);
        data.putExtra(INGREDIENTS_LIST, mIngredientsList);
        data.putExtra(CATEGORY_INDEX, fromCategoryIndex);

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INGREDIENTS_LIST_REQUEST) {
            if (resultCode == RESULT_OK) {
                ArrayList<Ingredient> list = (ArrayList<Ingredient>) data.getSerializableExtra("IngredientsList");
                for (Ingredient i : list) {
                    Log.i(TAG, i.toString());
                    mAdapter.add(i);
                }
            }
        }
    }

}
