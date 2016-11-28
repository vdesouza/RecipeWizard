package com.recipewizard.recipewizard;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import static android.R.attr.configChanges;
import static android.R.attr.focusable;
import static android.R.attr.tag;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Recipe Wizard";

    // IDs for menu items
    private static final int MENU_RESET_INGREDIENTS = Menu.FIRST;

    // Tabbed main activity and swipe between fragments adapted from these two tutorials:
    // https://www.youtube.com/watch?v=zQekzaAgIlQ
    // https://developer.android.com/training/implementing-navigation/lateral.html#tabs

    // ViewPager that will display each section of the app when swiped
    static ViewPager mViewPager;

    // PageAdapter that will provide fragments to each of the two main sections of the app
    PagerAdapter mPagerAdapter;

    // TabLayout that show the two tabs
    TabLayout tabLayout;

    // Master list of ingredients
    public static MasterIngredientsList mMasterIngredientsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up ViewPager, PagerAdapter, and TabLayout
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new FixedTabsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    // PagerAdapter that displays the two tabs: Ingredients and Recipes
    public static class FixedTabsPagerAdapter extends FragmentPagerAdapter {

        public FixedTabsPagerAdapter(FragmentManager fm) {
           super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new IngredientsListFragment();
                case 1:
                    return new RecipesListFragment();
                default:
                    return null;
            }
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Ingredients";
                case 1:
                    return "Recipes";
                default:
                    return null;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // only show options menu on portrait view
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, MENU_RESET_INGREDIENTS, Menu.NONE, getResources().getString(R.string.reset_ingredient_list));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESET_INGREDIENTS:
                showIngredientsResetDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showIngredientsResetDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Reset all ingredients back to default?");
        alert.setMessage("This will delete all added ingredients and categories. This will also uncheck all ingredients.");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mMasterIngredientsList = new MasterIngredientsList();

                // clear saved ingredients
                SharedPreferences.Editor editor = getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();

                // restart the fragment
                mViewPager.getAdapter().notifyDataSetChanged();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }



    // Fragment view for Ingredients list
    public static class IngredientsListFragment extends Fragment {

        private static final int INGREDIENTS_LIST_REQUEST = 0;
        private static final String INGREDIENTS_LIST = "IngredientsList";
        private static final String CATEGORY_NAME = "CategoryName";
        private static final String CATEGORY_INDEX = "CategoryIndex";

        TextView mIngredientsTextView;
        GridView mIngredientsGridView;

        // Adapter for GridView
        IngredientsCategoryAdapter mAdapter;

        SharedPreferences prefs;

        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            prefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

            View rootView = inflater.inflate(R.layout.ingredients_category_fragment, container, false);
            mIngredientsTextView = (TextView) rootView.findViewById(R.id.ingredientsTextView);
            mIngredientsGridView = (GridView) rootView.findViewById(R.id.ingredientsGridView);

            mIngredientsTextView.setText(R.string.ingredients_header);

            // Create a new IngredientsCategoryAdapter for the GridView
            mAdapter = new IngredientsCategoryAdapter(getActivity());

            // Create the list of ingredients (load a saved one or make a new one)
            String hasSavedList = prefs.getString("HasSavedList", "");
            if (hasSavedList.equals("True")) {
                load();
            } else if (mMasterIngredientsList == null) {
                mMasterIngredientsList = new MasterIngredientsList();
            }

            Log.i(TAG, "List Loaded:\n " + mMasterIngredientsList.toString());

            // make buttons for all ingredients view and for adding new ingredients
            mAdapter.add(new IngredientsCategory("Add New Ingredient"));
            mAdapter.add(new IngredientsCategory("All Ingredients", String.valueOf(mMasterIngredientsList.getAllCheckedCount())));

            // Add to adapter list
            for (String category : mMasterIngredientsList.getAllCategories()) {
                mAdapter.add(new IngredientsCategory(category, String.valueOf(mMasterIngredientsList.getCheckedCount(category))));
            }

            // Attach the adapter to the GridView
            mIngredientsGridView.setAdapter(mAdapter);

            // OnClickListener for categories
            mIngredientsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    String categoryName = ((TextView) v.findViewById(R.id.ingredientsCategoryName)).getText().toString();

                    if (categoryName.equals("Add New Ingredient")) {
                        addNewIngredientDialog();
                    } else {
                        ArrayList<Ingredient> listToPackage;
                        // Package list to build listview of ingredients
                        if (categoryName.equals("All Ingredients")) {
                            listToPackage = mMasterIngredientsList.getMasterList();
                        } else {
                            listToPackage = mMasterIngredientsList.getCategoryList(categoryName);
                        }
                        int index = parent.indexOfChild(v);

                        // Go to Ingredients List Activity for that category
                        Intent explicitIntent = new Intent(getActivity(), IngredientListActivity.class);
                        explicitIntent.putExtra(CATEGORY_NAME, categoryName);
                        explicitIntent.putExtra(INGREDIENTS_LIST, listToPackage);
                        explicitIntent.putExtra(CATEGORY_INDEX, index);
                        startActivityForResult(explicitIntent, INGREDIENTS_LIST_REQUEST);
                    }

                }
            });

            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            // update master ingredient list and category count
            if (requestCode == INGREDIENTS_LIST_REQUEST) {
                if (resultCode == RESULT_OK) {
                    String category = data.getStringExtra(CATEGORY_NAME);
                    ArrayList<Ingredient> list = (ArrayList<Ingredient>) data.getSerializableExtra(INGREDIENTS_LIST);
                    mMasterIngredientsList.updateList(list);

                    // update GridView to show accurate checked items count
                    int index = data.getIntExtra(CATEGORY_INDEX, IngredientsCategoryAdapter.NOT_FOUND);
                    if (index != IngredientsCategoryAdapter.NOT_FOUND) {
                        View v = mIngredientsGridView.getChildAt(index);
                        if (v != null) {
                            TextView categoryCountTextView = (TextView) v.findViewById(R.id.ingredientsCategoryCount);
                            int count;
                            if (category.equals("All Ingredients")) {
                                count = mMasterIngredientsList.getAllCheckedCount();
                            } else {
                                count = mMasterIngredientsList.getCheckedCount(category);
                            }
                            categoryCountTextView.setText(getActivity()
                                    .getString(R.string.checked_count, String.valueOf(count)));

                        }
                    }

                }
            }
        }

        @Override
        public void onResume() {
            super.onResume();
//            Log.i(TAG, "onResume");
//            // Load saved Ingredients, if necessary
//            if (mAdapter.getCount() == 0) {
//                load();
//            }
        }

        @Override
        public void onPause() {
            super.onPause();
            Log.i(TAG, "onPause");
            // Save ingredients
            save();
        }

        private void addNewIngredientDialog() {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

            alert.setTitle("Add new ingredient?");
            alert.setMessage("Type in name of new ingredient and it's category.\n" +
                    "Caution: not all ingredient names may work with recipe search API.");

            // set Linear layout for text fields
            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);

            // Set an EditText view to get user input
            final EditText inputIngredient = new EditText(getContext());
            inputIngredient.setHint("Enter ingredient name");
            layout.addView(inputIngredient);

            final AutoCompleteTextView inputCategory = new AutoCompleteTextView(getContext());
            inputCategory.setHint("Enter category name");
            inputCategory.setThreshold(0);
            // build list of categories for autosuggestion
            Object[] objArray = mMasterIngredientsList.getAllCategories().toArray();
            String[] autoCompleteSuggestions = Arrays.copyOf(objArray, objArray.length, String[].class);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice, autoCompleteSuggestions);
            inputCategory.setAdapter(adapter);
            layout.addView(inputCategory);

            alert.setView(layout);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String newIngredientName = (inputIngredient.getText().toString());
                    String newCategoryName = (inputCategory.getText().toString());
                    Ingredient newIngredient = new Ingredient(newIngredientName, null, false, newCategoryName);
                    boolean newCategory = true;
                    for (String category : mMasterIngredientsList.getAllCategories()) {
                        if (category.equals(newCategoryName)) {
                            newCategory = false;
                        }
                    }
                    boolean added = mMasterIngredientsList.updateList(newIngredient);
                    if (added) {
                        Toast.makeText(getContext(), newIngredientName + " added to ingredients.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Ingredient already exists.", Toast.LENGTH_SHORT).show();
                    }
                    if (newCategory && added) {
                        mAdapter.add(new IngredientsCategory(newCategoryName, String.valueOf(mMasterIngredientsList.getCheckedCount(newCategoryName))));

                    }
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
        }

        // Save Ingredients list to shared preferences
        private void save() {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("MasterIngredientsList", mMasterIngredientsList.toString());
            editor.putString("HasSavedList", "True");
            editor.apply();
        }


        // Load stored Ingredients
        private void load() {
            BufferedReader reader = null;
            try {
                String loadedPrefs = prefs.getString("MasterIngredientsList", "");
                reader = new BufferedReader(new StringReader(loadedPrefs));

                String categoryName;
                String ingredientName;
                String checked;

                Log.i(TAG, loadedPrefs);

                ArrayList<Ingredient> ingredientsList = new ArrayList<Ingredient>();
                while (null != (ingredientName = reader.readLine())) {

                    ingredientName = ingredientName.trim();
                    if (!ingredientName.equals("")) {
                        checked = reader.readLine();
                        categoryName = reader.readLine();
                        Ingredient ingredient = new Ingredient(ingredientName, null, Boolean.valueOf(checked), categoryName);
                        ingredientsList.add(ingredient);
                    }
                }
                Log.i(TAG, ingredientsList.toString());
                if (mMasterIngredientsList == null) {
                    mMasterIngredientsList = new MasterIngredientsList(ingredientsList);
                } else {
                    mMasterIngredientsList.updateList(ingredientsList);
                }
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

    }

    // Fragment view for Recipes list
    public static class RecipesListFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_holder, container, false);
            ((TextView) rootView.findViewById(android.R.id.text1)).setText("Recipes Place Holder");

            return rootView;
        }

        // Test to check that checked ingredients are getting seeing correctly
        // Get all checked ingredients by calling mMasterIngredientsList.getCheckedIngredients()
        // Returns ArrayList<Ingredients>
        @Override
        public void onResume() {
            super.onResume();
            Log.i(TAG, "RECIPE onResume");
            if (mMasterIngredientsList != null) {
                for (Ingredient ingredient : mMasterIngredientsList.getCheckedIngredients()) {
                    Log.i(TAG, "Recipe Fragment: " + ingredient.toString());
                }
            }
        }
    }

}
