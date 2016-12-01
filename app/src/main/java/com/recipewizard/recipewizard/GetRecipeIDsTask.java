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

// Takes in the ingredients as a string separated by commas (ex. "apples,cinammon")
// return a minirecipe, which only contains a picture name, ingredient count, and likes.
// use the minirecipe for the listadapter for when recipes are returned.
public class GetRecipeIDsTask extends AsyncTask<String[], Void, ArrayList<MiniRecipe>> {


    final String TAG = "GetRecipeIDsTask";
    @Override
    protected ArrayList<MiniRecipe> doInBackground(String[]... args) {

        try {

            return getRecipeIDs(args[0][0],args[0][1]);

        } catch (JSONException e) {
            Log.i(TAG,"FUCK");
            return new ArrayList<>();
        }

    }
    private ArrayList<MiniRecipe> getRecipeIDs(String s, String ranking) throws JSONException {

        HttpURLConnection connection = null;
        ArrayList<MiniRecipe> recipes = new ArrayList<MiniRecipe>();
        try {
            URL url = new URL("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?" +
                    "ingredients=" + s + "&number=5&ranking=" + ranking);
            Log.i(TAG, url.toString());
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

            JSONArray parent = new JSONArray(json);
            for (int i = 0; i < parent.length(); i++) {
                JSONObject obj = parent.getJSONObject(i);
                int id = obj.getInt("id");
                String name = obj.getString("title");
                Bitmap bmp = null;
                try {
                    URL url2 = new URL(obj.getString("image"));
                    bmp = BitmapFactory.decodeStream(url2.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    Log.i(TAG, "BADURL");
                } catch (IOException e) {
                    Log.i(TAG, "BADIO");
                }
                int usedIng = obj.getInt("usedIngredientCount");
                int missIng = obj.getInt("missedIngredientCount");
                int likes = obj.getInt("likes");
                recipes.add(new MiniRecipe(id,name,bmp,usedIng,missIng,likes));
                Log.i(TAG, id + ": " + name);
                //get data here
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
            return recipes;
        }
    }

}


