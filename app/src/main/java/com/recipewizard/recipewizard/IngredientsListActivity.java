package com.recipewizard.recipewizard;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by vdesouza on 11/17/16.
 */

public class IngredientsListActivity extends ListActivity {

    private static final String TAG = "Recipe Wizard : IngredientsList";

    private static final int ADD_INGREDIENT_REQUEST = 0;

    IngredientsListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a new IngredientsListAdapter for the ListView
        mAdapter = new IngredientsListAdapter(getApplicationContext());

        // Put divider between ToDoItems and HeaderView
        getListView().setHeaderDividersEnabled(true);

        // Inflate headerView for footer_view.xml file
        // TODO - make header and footer view for lists
        //TextView headerView = (TextView) getLayoutInflater().inflate(R.layout.ingredients_list_header_view, null);

        // Put divider between ToDoItems and FooterView
        getListView().setFooterDividersEnabled(true);

        // Inflate headerView for footer_view.xml file
        // TODO - make header and footer xml for lists
        //TextView footerView = (TextView) getLayoutInflater().inflate(R.layout.ingredients_list_footer_view, null);

        // Add headerView and footerView to ListView
        // TODO - uncomment when header and footer xml are created
        //getListView().addHeaderView(headerView);
        //getListView().addFooterView(footerView);

        // TODO - Attach Listener to FooterView
//        footerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //DONE - TODO - Implement OnClick().
//                Intent explicitIntent = new Intent(ToDoManagerActivity.this, AddToDoActivity.class);
//                startActivityForResult(explicitIntent, ADD_TODO_ITEM_REQUEST);
//            }
//        });

        // Attach the adapter to ListActivity's ListView
        getListView().setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if user submitted a new Ingredient, add to adapter
        if (requestCode == ADD_INGREDIENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Ingredient ingredient = new Ingredient(data);
                mAdapter.add(ingredient);
                // TODO - Add ingredient to JSON
            }
        }
    }

}
