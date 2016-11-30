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

/**
 * Created by Hank on 11/30/2016.
 */

// Takes in the id and returns a recipe, without the instructions, which can be gotten from the
// getrecipeinstructionstask. I'm not sure how standardized the recipes are, so we may
// run into issues with getting nutrition and pictures of ingredients.
public class GetRecipeInfoTask extends AsyncTask<Integer, Void, Recipe> {
    final String TAG = "GetRecipeInfoTask";

    @Override
    protected Recipe doInBackground(Integer... args) {

        try {

            return getRecipeInfo(args[0]);

        } catch (JSONException e) {
            Log.i(TAG,"FUCK");
            return null;
        }

    }

    private Recipe getRecipeInfo(int s) throws JSONException {

        Recipe recipe = null;

        HttpURLConnection connection = null;

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
            Log.i(TAG, json);

            JSONObject parent = new JSONObject(json);
            JSONArray ings = parent.getJSONArray("extendedIngredients");

            String name = parent.getString("title");
            Log.i(TAG, name);
            int likes = parent.getInt("aggregateLikes");
            // retrieve stuff here for recipe page
            int calories = -1; // checks in case doesn't have nutrition, maybe separated types of fat?
            int fat = -1;
            int protein = -1;
            int carbs = -1;
            JSONArray nutrients = parent.getJSONObject("nutrition").getJSONArray("nutrients");
            for (int i = 0; i < nutrients.length(); i++) {
                JSONObject n = nutrients.getJSONObject(0);
                String title = n.getString("title");

                if (title.equals("Calories")) calories = n.getInt("amount");
                if (title.equals("Fat")) fat = n.getInt("amount");
                if (title.equals("Protein")) protein = n.getInt("amount");
                if (title.equals("Carbohydrates")) carbs = n.getInt("amount");

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

            ArrayList<Ingredient> ingredients = new ArrayList<>();
            for (int i = 0; i < ings.length(); i++) {
                JSONObject temp = ings.getJSONObject(i);
                String ing = temp.getString("name"); // figure out what to get from ingredient here
                Log.i(TAG, ing);
                Bitmap bmp = null;
                if (temp.has("image")) {
                    try {
                        URL url2 = new URL(temp.getString("image"));
                        bmp = BitmapFactory.decodeStream(url2.openConnection().getInputStream());
                    } catch (MalformedURLException e) {
                        Log.i(TAG, "BADURL");
                    } catch (IOException e) {
                        Log.i(TAG, "BADIO");
                    }
                    Ingredient ingredient = new Ingredient(ing, bmp, true, null);
                    ingredients.add(ingredient);
                }
                // retrieve ingredients info u need
            }

            recipe = new Recipe(s + "",name,bitmap,ingredients,calories,protein,fat,carbs,likes);
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
}

