package com.recipewizard.recipewizard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


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
    private static MasterIngredientsList mMasterIngredientsList;
    // Dietary/Allergy Filters
    final CharSequence[] allergyFilters = {" dairy ", " egg ", " gluten ", " peanut ", " sesame ", " seafood "
            , " shellfish ", " soy ", " sulfite ", " tree nut ", " wheat "};
    final CharSequence[] dietFilters = {" pescetarian ", " lacto vegetarian ", " ovo vegetarian ", " vegan ", " vegetarian "};
    final static ArrayList selectedAllergyFilters = new ArrayList();
    final static ArrayList selectedDietFilters = new ArrayList();

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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        menu.add(Menu.NONE, MENU_RESET_INGREDIENTS, Menu.NONE, getResources().getString(R.string.reset_ingredient_list));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorites:
                Intent favIntent = new Intent(MainActivity.this, FavoritesTabManagerActivity.class);
                startActivity(favIntent);
                return true;
            case R.id.action_filters:
                showFiltersDialog();
                return true;
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

    private void showFiltersDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Select filters for recipes");
        alert.setMessage("Hide ingredients that do not fit these dietary/allergy filters.");
        alert.setPositiveButton("Allergy Filters", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                showAllergyFilterDialog();
            }
        });

        alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.setNegativeButton("Diet Filters", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                showDietFilterDialog();
            }
        });

        alert.show();
    }

    private void showAllergyFilterDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Select Allergy Filters");
        // build the checklist
        boolean[] checkedValues = new boolean[allergyFilters.length];
        for (int i = 0; i < allergyFilters.length; i++) {
            if (selectedAllergyFilters.contains(allergyFilters[i])) {
                checkedValues[i] = true;
            }
        }
        alert.setMultiChoiceItems(allergyFilters, checkedValues, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                if (isChecked) {
                    // If the user checked the item, add it to the selected items
                    selectedAllergyFilters.add(allergyFilters[indexSelected]);
                } else if (selectedAllergyFilters.contains(allergyFilters[indexSelected])) {
                    // Else, if the item is already in the array, remove it
                    selectedAllergyFilters.remove(allergyFilters[indexSelected]);
                }
            }
        });
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // restart the fragment
                dialog.dismiss();
//                mViewPager.getAdapter().notifyDataSetChanged();
                Log.i(TAG, "Selected allergies: " + selectedAllergyFilters.toString());
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private void showDietFilterDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Select Diet Filters");
        // build the checklist
        boolean[] checkedValues = new boolean[dietFilters.length];
        for (int i = 0; i < dietFilters.length; i++) {
            if (selectedDietFilters.contains(dietFilters[i])) {
                checkedValues[i] = true;
            }
        }
        alert.setMultiChoiceItems(dietFilters, checkedValues, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                if (isChecked) {
                    // If the user checked the item, add it to the selected items
                    selectedDietFilters.add(dietFilters[indexSelected]);
                } else if (selectedDietFilters.contains(dietFilters[indexSelected])) {
                    // Else, if the item is already in the array, remove it
                    selectedDietFilters.remove(dietFilters[indexSelected]);
                }
            }
        });
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // restart the fragment
                dialog.dismiss();
                //mViewPager.getAdapter().notifyDataSetChanged();
                Log.i(TAG, "Selected diet: " + selectedDietFilters.toString());
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private static String formatAllergyFilter() {
        String intolerances = "";
        for (int i = 0; i < selectedAllergyFilters.size(); i++) {
            String item = (String) selectedAllergyFilters.get(i);
            item.replace(" ", "+");
            if (intolerances.equals("")) {
                intolerances = item;
            } else {
                intolerances = intolerances + "," + item;
            }
        }
        return intolerances.replace(" ", "");
    }

    private static String formatDietFilter() {
        String diet = "";
        for (int i = 0; i < selectedDietFilters.size(); i++) {
            String item = (String) selectedDietFilters.get(i);
            item.replace(" ", "+");
            if (diet.equals("")) {
                diet = item;
            } else {
                diet = diet + "," + item;
            }
        }
        return diet.replace(" ", "");
    }



    // Fragment view for Ingredients list
    public static class IngredientsListFragment extends Fragment {

        private static final int INGREDIENTS_LIST_REQUEST = 0;
        private static final String INGREDIENTS_LIST = "IngredientsList";
        private static final String CATEGORY_NAME = "CategoryName";
        private static final String CATEGORY_INDEX = "CategoryIndex";
        private static final String REMOVE_LIST = "RemoveList";

        TextView mIngredientsTextView;
        GridView mIngredientsGridView;

        // Adapter for GridView
        IngredientsCategoryAdapter mAdapter;

        SharedPreferences prefs;

        // List default generated categories icon names
        static final String ADD = "add";
        static final String ALL = "all";
        static final String BAKING = "baking";
        static final String CANNED = "canned";
        static final String CONDIMENTS = "condiments";
        static final String DAIRY = "dairy";
        static final String FRUITS = "fruits";
        static final String GRAINS = "grains";
        static final String MEATS = "meats";
        static final String SEASONING = "seasoning";
        static final String USER = "user";
        static final String VEGETABLES = "vegetables";

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
            mAdapter.add(new IngredientsCategory("Add New Ingredient", ADD));
            mAdapter.add(new IngredientsCategory("All Ingredients", ALL));

            // Add to adapter list
            for (String category : mMasterIngredientsList.getAllCategories()) {
                String iconName = getIconName(category);
                mAdapter.add(new IngredientsCategory(category, iconName));
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

            // long click to delete an a category and its items
            mIngredientsGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, final View v, final int position, long id) {
                    final String categoryName = ((TextView) v.findViewById(R.id.ingredientsCategoryName)).getText().toString();
                    if ((!categoryName.equals("Add New Ingredient")) && (!categoryName.equals("All Ingredients"))) {
                        final AlertDialog.Builder alert = new AlertDialog.Builder(parent.getContext());
                        alert.setIcon(android.R.drawable.ic_dialog_alert);
                        alert.setTitle("Delete Category?");
                        alert.setMessage("Are you sure? This cannot be undone. All ingredients in this category will be deleted as well.");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ArrayList<Ingredient> listToRemove = mMasterIngredientsList.getCategoryList(categoryName);
                                mMasterIngredientsList.removeList(listToRemove);
                                mAdapter.remove(categoryName);
                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //canceled
                            }
                        });
                        alert.show();
                    }
                    return true;
                }
            });

            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            // update master ingredient list and category count
            if (requestCode == INGREDIENTS_LIST_REQUEST) {
                if (resultCode == RESULT_OK) {
                    ArrayList<Ingredient> list = (ArrayList<Ingredient>) data.getSerializableExtra(INGREDIENTS_LIST);
                    ArrayList<Ingredient> removeList = (ArrayList<Ingredient>) data.getSerializableExtra(REMOVE_LIST);
                    // add new items and changes
                    mMasterIngredientsList.updateList(list);
                    // remove items that were deleted
                    for (Ingredient ingredient : removeList) {
                        mMasterIngredientsList.removeIngredient(ingredient);
                        if (mMasterIngredientsList.getCategoryCount(ingredient.getCategory()) == 0) {
                            mAdapter.remove(ingredient.getCategory());
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
            inputIngredient.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            inputIngredient.setHint("Enter ingredient name");
            layout.addView(inputIngredient);

            final AutoCompleteTextView inputCategory = new AutoCompleteTextView(getContext());
            inputCategory.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
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
                        mAdapter.add(new IngredientsCategory(newCategoryName, USER));

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
            editor.putString("MasterIngredientsList", mMasterIngredientsList.toStringForSaving());
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

        // gets icon name based on category name
        private String getIconName(String category) {
            String iconName;
            switch (category) {
                case MasterIngredientsList.BAKING:
                    iconName = IngredientsListFragment.BAKING;
                    break;
                case MasterIngredientsList.CANNED:
                    iconName = IngredientsListFragment.CANNED;
                    break;
                case MasterIngredientsList.CONDIMENTS:
                    iconName = IngredientsListFragment.CONDIMENTS;
                    break;
                case MasterIngredientsList.DAIRY:
                    iconName = IngredientsListFragment.DAIRY;
                    break;
                case MasterIngredientsList.FRUITS:
                    iconName = IngredientsListFragment.FRUITS;
                    break;
                case MasterIngredientsList.GRAINS:
                    iconName = IngredientsListFragment.GRAINS;
                    break;
                case MasterIngredientsList.MEATS:
                    iconName = IngredientsListFragment.MEATS;
                    break;
                case MasterIngredientsList.SEASONINGS:
                    iconName = IngredientsListFragment.SEASONING;
                    break;
                case MasterIngredientsList.VEGETABLES:
                    iconName = IngredientsListFragment.VEGETABLES;
                    break;
                default:
                    iconName = USER;
            }
            return iconName;
        }

    }
// Fragment view for Recipes list
    public static class RecipesListFragment extends Fragment {
        ListView listView;
        Button loadMore;
        ArrayList<Ingredient> checkedIngredients;
        int offset;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_holder, container, false);
            listView = (ListView) rootView.findViewById(R.id.recipe_list);
            View footerView = inflater.inflate(R.layout.button_view, null, false);
            loadMore = (Button) footerView.findViewById(R.id.load_more);
            listView.addFooterView(footerView);
            checkedIngredients = mMasterIngredientsList.getCheckedIngredients();
            final String[] checkedIngredientsName = new String[checkedIngredients.size()];
            loadRecipes(checkedIngredientsName);
            loadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkedIngredients = mMasterIngredientsList.getCheckedIngredients();
                    final String[] checked = new String[checkedIngredients.size()];
                    offset = updateData(checked);
                }
            });
            return rootView;
        }

        // reset ingredients/put in new one = fail
        public int updateData(String[] ingredients) {
            for (int i = 0; i < checkedIngredients.size(); i++) {
                ingredients[i] = checkedIngredients.get(i).getName().replace(" ", "+");
            }
            try {
                offset = GetRecipesTask.getCounter();
                ArrayList<Recipe> add = new GetRecipesTask(formatAllergyFilter(), formatDietFilter(), 1, offset, getContext()).execute(ingredients).get();
                for (Recipe recipe : add) {
                    ((RecipeListAdapter) ((HeaderViewListAdapter) listView.getAdapter()).getWrappedAdapter())
                            .insert(recipe, ((RecipeListAdapter) ((HeaderViewListAdapter)
                                    listView.getAdapter()).getWrappedAdapter()).getCount());
                }
            } catch (InterruptedException | ExecutionException e) {
                Log.i(TAG, "exception");
            }
            return offset;
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

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                // When fragment visible to user, update recipes
                ArrayList<Ingredient> currCheckedIngredients = mMasterIngredientsList.getCheckedIngredients();
                // only update recipes if checked ingredients changed.
                if (!checkedIngredients.equals(currCheckedIngredients)) {
                    checkedIngredients = currCheckedIngredients;
                    String[] checkedIngredientsName = new String[checkedIngredients.size()];
                    loadRecipes(checkedIngredientsName);
                }
            } else {
                // fragment is hidden
            }
        }

        public void loadRecipes(String[] ingredients) {
            ArrayList<Recipe> recipes = new ArrayList<>();
            for (int i = 0; i < checkedIngredients.size(); i++) {
                ingredients[i] = checkedIngredients.get(i).getName().replace(" ", "+");
            }
            try {
                if (ingredients.length > 0) {
                    recipes = new GetRecipesTask(formatAllergyFilter(), formatDietFilter(), 1, 0, getContext()).execute(ingredients).get();
                }
            } catch (InterruptedException | ExecutionException e) {
                Log.i(TAG, "exception");
            }
            RecipeListAdapter adapter = new RecipeListAdapter(getActivity(), R.layout.fragment_holder, recipes);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }


    // Takes in the ingredients as a string separated by commas (ex. "apples,cinammon")
// return a minirecipe, which only contains a picture name, ingredient count, and likes.
// use the minirecipe for the listadapter for when recipes are returned.

    private static class GetRecipesTask extends AsyncTask<String, ArrayList<Recipe>, ArrayList<Recipe>> {

        final String TAG = "GetRecipesTask";
        public static final int NUM_ALLERGIES = 4;
        Context context;
        ProgressDialog progressDialog;

        HashSet<String> blacklist = new HashSet<>();
        HashSet<String> goodlist = new HashSet<>();
        String[] blist = {"Steamy Kitchen", "Crumb", "HowStuffWorks", "Chubby Hubby", "Food.com",
                "Dorie Greenspan", "Deep South Dish", "SF Gate", "Bijouxs",
                "Martha Stewart", "Foodnetwork"};

        static int counter = 0;

        // Maybes: Epicurious, Foodista, Anonymous

        // intolerances = dairy, egg, gluten, peanut, sesame, seafood, shellfish, soy, sulfite, tree nut, or wheat
        // separate with commas
        String intolerances;
        // diet = pescetarian, lacto vegetarian, ovo vegetarian, vegan, paleo, primal, and vegetarian.
        String diet;
        // ranking = 1 or 2, maximize used ingredients or minimize missing ingredients
        int ranking;
        @Override
        protected void onPreExecute() {
            this.progressDialog = new ProgressDialog(context, R.style.SpinnerTheme);
            this.progressDialog.setCancelable(false);
            this.progressDialog.setProgressStyle(android.R.attr.progressBarStyleLarge);
            this.progressDialog.show();
        }
        @Override
        protected void onPostExecute(ArrayList <Recipe> recipes) {
            if(this.progressDialog != null){
                this.progressDialog.dismiss();
            }
        }

        public GetRecipesTask(String intolerances, String diet, int ranking, int offset, Context context) {
            this.intolerances = intolerances;
            this.diet = diet;
            this.ranking = ranking;
            this.counter = offset;
            this.context = context;
            Log.i(TAG, "counter: " + this.counter);
//        for (int i = 0; i < blist.length; i++) {
//            blacklist.add(blist[i]);
//        }
        }

        @Override
        protected ArrayList<Recipe> doInBackground(String... args) {

            String ings = args[0];
            for (int i = 1; i < args.length; i ++) {
                ings += "," + args[i];
            }
            ings.replace("", "+");
            Log.i(TAG,ings);

            try {

                return getRecipeIDs(ings);

            } catch (JSONException e) {
                Log.i(TAG,"FUCK");
                return new ArrayList<>();
            }

        }

        private ArrayList<Recipe> getRecipeIDs(String s) throws JSONException {

            HttpURLConnection connection = null;
            ArrayList<Recipe> recipes = new ArrayList<>();
            //Log.i(TAG,"SPECIAL INGREDIENTS: " + s);
            String query = s.split(",")[0];
            //Log.i(TAG,"QUERY: " + query);
            try {
                StringBuilder urlString = new StringBuilder("https://spoonacular-recipe-food-nutrition-" +
                        "v1.p.mashape.com/recipes/searchComplex?addRecipeInformation=true&limitLicense=false&includeIngredients=");
                urlString.append(s + "&number=100&query=&ranking=");
                urlString.append(ranking + "&offset=");
                urlString.append(counter);
                if (!diet.equals("")) urlString.append("&diet=" + diet);
                if (!intolerances.equals("")) urlString.append("&intolerances=" + intolerances);
                Log.i(TAG, urlString.toString());
                URL url = new URL(urlString.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("X-Mashape-Key", "3d1ZGQjt7hmsh2B0zVtq6WnhnvsLp1pwVDbjsnaab1DFJfuV6r");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();
                InputStream stream = connection.getInputStream();
                BufferedReader reader = null;
                StringBuffer jsonBuffer = new StringBuffer("");
                reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    jsonBuffer.append(line);
                }
                String json = jsonBuffer.toString();
                //Log.i(TAG, json + "\n");

                JSONArray parent = new JSONObject(json).getJSONArray("results");
                Log.i(TAG, parent.length() + "");
                for (int i = 0; i < parent.length(); i++) {
                    JSONObject obj = parent.getJSONObject(i);
                    int id = obj.getInt("id");
                    Recipe recipe = getRecipeInfo(id);
                    if (recipe != null) {
                        recipes.add(recipe);
                    } if (recipes.size() == 10) {

//                    Recipe r = getRecipeInfo(5014);
//                    if (r != null) {
//                        Log.i(TAG, "OOPS");
//                        recipes.add(recipe);
//                    }
                        counter += i + 1;
                        return recipes;
                    }
                }


                reader.close();
                stream.close();
                connection.disconnect();

            } catch (MalformedURLException e) {
                Log.i(TAG, "Invalid URL.");
            } catch (IOException e) {
                Log.i(TAG, "Failed");
            } catch (Exception e){
                Log.i(TAG,e.getMessage());
            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
//            Log.i(TAG, "BLACKLIST: ");
//            StringBuilder stringBuilder = new StringBuilder();
//            for (String string : blacklist) {
//                stringBuilder.append("\"" + string + "\", ");
//                //Log.i(TAG, string);
//            }
//            Log.i(TAG, stringBuilder.toString());
//
//            stringBuilder = new StringBuilder();
//            Log.i(TAG, "GOODLIST: ");
//            for (String string : goodlist) {
//                stringBuilder.append("\"" + string + "\", ");
//                //Log.i(TAG, string);
//            }
//            Log.i(TAG, stringBuilder.toString());

                return recipes;
            }
        }

        private Recipe getRecipeInfo(int s) throws JSONException {

            Recipe recipe = null;

            HttpURLConnection connection = null;

            Log.i(TAG, "ID: " + s);
            try {
                URL url = new URL("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" + s +
                        "/information?includeNutrition=true");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("X-Mashape-Key", "3d1ZGQjt7hmsh2B0zVtq6WnhnvsLp1pwVDbjsnaab1DFJfuV6r");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();
                InputStream stream = connection.getInputStream();
                BufferedReader reader = null;
                StringBuffer jsonBuffer = new StringBuffer("");
                reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    jsonBuffer.append(line);
                }
                String json = jsonBuffer.toString();
                //Log.i(TAG, json + "\n");

                JSONObject parent = new JSONObject(json);
                String auth = "Anonymous";
                if (parent.has("sourceName")) auth = parent.getString("sourceName");
                Log.i(TAG, parent.getString("sourceUrl"));
                Log.i(TAG, auth);
                if (!auth.equals("Foodnetwork") && !parent.getString("instructions").equals("null")) { //&& parent.getString("instructions").charAt(0) != '<') {

                    JSONArray ings = parent.getJSONArray("extendedIngredients");
                    HashMap<String, Ingredient> ingredients = new HashMap<>();
                    for (int i = 0; i < ings.length(); i++) {
                        JSONObject temp = ings.getJSONObject(i);
                        String ing = temp.getString("name"); // figure out what to get from ingredient here
                        //Log.i(TAG, ing);
                        String category = temp.getString("aisle");
                        Bitmap bmp = null;
                        double amount = temp.getDouble("amount");
                        String unit = temp.getString("unit");
                        if (temp.has("image")) {
                            try {
                                URL url2 = new URL(temp.getString("image"));
                                bmp = BitmapFactory.decodeStream(url2.openConnection().getInputStream());
                            } catch (MalformedURLException e) {
                                Log.i(TAG, "BADURL");
                            } catch (IOException e) {
                                Log.i(TAG, "BADIO");
                            }
                        }
                        Ingredient ingredient = new Ingredient(ing, bmp, true, category,amount,unit);
                        ingredients.put(ing, ingredient);
                        // retrieve ingredients info u need
                    }

                    ArrayList<Step> steps = getRecipe(s,ingredients);

                    if (steps.size() > 0) {
                        //Log.i(TAG, "GOOOD");
                        String name = parent.getString("title");
                        //Log.i(TAG, name);
                        int likes = parent.getInt("aggregateLikes");
                        int servings = parent.getInt("servings");
                        String author = "Anonymous";
                        if (parent.has("sourceName")) author = parent.getString("sourceName");
                        int cook_time = parent.getInt("readyInMinutes");
                        // retrieve stuff here for recipe page
                        int calories = -1; // checks in case doesn't have nutrition, adding all fat together
                        int fat = -1;
                        int protein = -1;
                        int carbs = -1;
                        if (parent.has("nutrition")) {
                            JSONArray nutrients = parent.getJSONObject("nutrition").getJSONArray("nutrients");
                            for (int i = 0; i < nutrients.length(); i++) {
                                JSONObject n = nutrients.getJSONObject(i);
                                String title = n.getString("title");
                                //Log.i(TAG, title);
                                if (title.equals("Calories")) calories = n.getInt("amount");
                                if (title.equals("Fat")) fat = n.getInt("amount");
                                if (title.equals("Protein")) protein = n.getInt("amount");
                                if (title.equals("Carbohydrates")) carbs = n.getInt("amount");

                            }
                        }

                        Bitmap bitmap = null;
                        try {
                            URL url2 = new URL(parent.getString("image"));
                            bitmap = BitmapFactory.decodeStream(url2.openConnection().getInputStream());
                        } catch (MalformedURLException e) {
                            Log.i(TAG, "BADURL");
                        } catch (IOException e) {
                            Log.i(TAG, "BADIO");
                        }

                        boolean[] arr = new boolean[NUM_ALLERGIES];
                        arr[0] = parent.getBoolean("vegetarian");
                        arr[1] = parent.getBoolean("vegan");
                        arr[2] = parent.getBoolean("glutenFree");
                        arr[3] = parent.getBoolean("dairyFree");


                        // change booleans if needed
                        recipe = new Recipe(s + "", name, author, bitmap, steps, calories, protein, fat,
                                carbs, likes, servings, cook_time, arr);
                        //Log.i(TAG,recipe.toString());
                        //goodlist.add(author);
                    }
                }
                reader.close();
                stream.close();
                connection.disconnect();

            } catch (MalformedURLException e) {
                Log.i(TAG, "Invalid URL.");
            } catch (IOException e) {
                Log.i(TAG, "Failed");
            } catch (Exception e) {
                Log.i(TAG, e.toString());
            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
                return recipe;
            }
        }

        private ArrayList<Step> getRecipe(int s, HashMap<String,Ingredient> ingredients) throws JSONException {

            HttpURLConnection connection = null;
            ArrayList<Step> steps = new ArrayList<>();
            try {
                URL url = new URL("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/"
                        + s + "/analyzedInstructions?stepBreakdown=true");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("X-Mashape-Key", "3d1ZGQjt7hmsh2B0zVtq6WnhnvsLp1pwVDbjsnaab1DFJfuV6r");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();
                InputStream stream = connection.getInputStream();
                BufferedReader reader = null;
                StringBuffer jsonBuffer = new StringBuffer("");
                reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    jsonBuffer.append(line);
                }
                String json = jsonBuffer.toString();
                //Log.i(TAG, json + "\n");


                JSONArray parent = new JSONArray(json).getJSONObject(0).getJSONArray("steps");
                if ((parent.length() != 0) && (parent.length() < 30)) {

                    Log.i(TAG, "Num steps: " + parent.length());
                    for (int i = 0; i < parent.length(); i++) {
                        Log.i(TAG, "Step " + i);
                        JSONObject obj = parent.getJSONObject(i);
                        String direction = obj.getString("step");
                        if (direction.length() > 5) {
                            ArrayList<Ingredient> ings = new ArrayList<>();
                            JSONArray jsonIngs = obj.getJSONArray("ingredients");
                            for (int j = 0; j < jsonIngs.length(); j++) {
                                ings.add(ingredients.get(jsonIngs.getJSONObject(j).getString("name")));
                                //Log.i(TAG, jsonIngs.getJSONObject(j).getString("name"));
                            }
                            JSONArray jsonEquip = obj.getJSONArray("equipment");
                            ArrayList<String> equipment = new ArrayList<>();
                            for (int j = 0; j < jsonEquip.length(); j++) {
                                equipment.add(jsonEquip.getJSONObject(j).getString("name"));
                                //Log.i(TAG, equipment.get(j));
                            }
                            Step step = new Step(direction, ings, equipment);
                            steps.add(step);
                            Log.i(TAG, step.toString());
                            // get ingredients with ingredients JSONArr then each JSONObj has image and name
                            // get equipment with equipment JSONArr then each JSONObj has image and name
                        }
                    }
                }
                reader.close();
                stream.close();
                connection.disconnect();


            } catch (MalformedURLException e) {
                Log.i(TAG, "Invalid URL.");
            } catch (IOException e) {
                Log.i(TAG, "Failed");
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                return steps;
            }
        }

        public static int getCounter() {
            return counter;
        }

        public void setCounter(int counter) {
            this.counter = counter;
        }
    }
}
