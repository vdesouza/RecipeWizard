package com.recipewizard.recipewizard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Hank on 11/30/2016.
 */

// Takes in the ingredients as a string separated by commas (ex. "apples,cinammon")
// return a minirecipe, which only contains a picture name, ingredient count, and likes.
// use the minirecipe for the listadapter for when recipes are returned.
public class GetRecipesTask extends AsyncTask<String, Void, ArrayList<Recipe>> {

    final String TAG = "GetRecipesTask";
    public static final int NUM_ALLERGIES = 4;

    HashSet<String> blacklist = new HashSet<>();
    HashSet<String> goodlist = new HashSet<>();
    String[] blist = {"Steamy Kitchen", "Crumb", "HowStuffWorks", "Chubby Hubby", "Food.com",
            "Dorie Greenspan", "Deep South Dish", "SF Gate", "Bijouxs",
            "Martha Stewart", "Foodnetwork"};

    // Maybes: Epicurious, Foodista, Anonymous

    // intolerances = dairy, egg, gluten, peanut, sesame, seafood, shellfish, soy, sulfite, tree nut, or wheat
    // separate with commas
    String intolerances;
    // diet = pescetarian, lacto vegetarian, ovo vegetarian, vegan, paleo, primal, and vegetarian.
    String diet;
    // ranking = 1 or 2, maximize used ingredients or minimize missing ingredients
    int ranking;

    public GetRecipesTask(String intolerances, String diet, int ranking) {
        this.intolerances = intolerances;
        this.diet = diet;
        this.ranking = ranking;
//        for (int i = 0; i < blist.length; i++) {
//            blacklist.add(blist[i]);
//        }
    }

    @Override
    protected ArrayList<Recipe> doInBackground(String... args) {

        try {

            return getRecipeIDs(args[0]);

        } catch (JSONException e) {
            Log.i(TAG,"FUCK");
            return new ArrayList<>();
        }

    }
    private ArrayList<Recipe> getRecipeIDs(String s) throws JSONException {

        HttpURLConnection connection = null;
        ArrayList<Recipe> recipes = new ArrayList<>();
        //Log.i(TAG,"SPECIAL INGREDIENTS: " + s);
        String query = s.replace(",","");
        //Log.i(TAG,"QUERY: " + query);
        try {
            StringBuilder urlString = new StringBuilder("https://spoonacular-recipe-food-nutrition-" +
                    "v1.p.mashape.com/recipes/searchComplex?addRecipeInformation=true&ingredients=");
            urlString.append(s + "&query=" + query + "&number=10&ranking=");
            urlString.append(ranking);
            if (!diet.equals("")) urlString.append("&diet=" + diet);
            if (!intolerances.equals("")) urlString.append("&intolerances=" + intolerances);
            //Log.i(TAG, urlString.toString());
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
                }
            }
//            Recipe recipe = getRecipeInfo(39048);
//            if (recipe != null) {
//                recipes.add(recipe);
//            }

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
            if (!blacklist.contains("Foodnetwork") && !parent.getString("instructions").equals("null")) { //&& parent.getString("instructions").charAt(0) != '<') {
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
                boolean[] arr = new boolean[NUM_ALLERGIES];
                arr[0] = parent.getBoolean("vegetarian");
                arr[1] = parent.getBoolean("vegan");
                arr[2] = parent.getBoolean("glutenFree");
                arr[3] = parent.getBoolean("dairyFree");


                // change booleans if needed
                recipe = new Recipe(s + "", name, author, bitmap, getRecipe(s,ingredients), calories, protein, fat,
                        carbs, likes, servings, cook_time, arr);
                //Log.i(TAG,recipe.toString());
                //goodlist.add(author);
            } else {
                //blacklist.add(auth);
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

}





