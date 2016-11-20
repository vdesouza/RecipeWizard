package com.recipewizard.recipewizard;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients_list);

        mIngredientsListView = (ListView) findViewById(R.id.ingredientsListView);

        // Create a new IngredientListAdapter for the ListView
        mAdapter = new IngredientListAdapter(getApplicationContext());

        // Put divider between ToDoItems and HeaderView
        mIngredientsListView.setHeaderDividersEnabled(true);

        // Inflate headerView
        TextView headerView = (TextView) getLayoutInflater().inflate(R.layout.ingredients_list_header_view, null);

        // Put divider between ToDoItems and FooterView
        mIngredientsListView.setFooterDividersEnabled(true);

        // Inflate headerView for footer_view.xml file
        // TODO - make header and footer xml for lists
        //TextView footerView = (TextView) getLayoutInflater().inflate(R.layout.ingredients_list_footer_view, null);

        // Add headerView and footerView to ListView
        // TODO - uncomment when header and footer xml are created
        mIngredientsListView.addHeaderView(headerView);
        //mIngredientsListView.addFooterView(footerView);

        // TODO - Attach Listener to FooterView
//        footerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //DONE - TODO - Implement OnClick().
//                Intent explicitIntent = new Intent(ToDoManagerActivity.this, AddToDoActivity.class);
//                startActivityForResult(explicitIntent, ADD_TODO_ITEM_REQUEST);
//            }
//        });

        if (getIntent().getExtras() != null) {
            mIngredientsList = (ArrayList<Ingredient>) getIntent().getSerializableExtra("IngredientsList");
            for (Ingredient i : mIngredientsList) {
                Log.i(TAG, i.toString());
                mAdapter.add(i);
            }
            fromCategory = getIntent().getStringExtra(CATEGORY_NAME);
            fromCategoryIndex = getIntent().getIntExtra(CATEGORY_INDEX, -1);
            headerView.setText(fromCategory);

        }

        // Attach the adapter to ListActivity's ListView
        mIngredientsListView.setAdapter(mAdapter);
    }


    @Override
    public void onBackPressed() {
        Intent data = new Intent();

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
