package com.recipewizard.recipewizard;

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

// Takes in the id and returns a list of directions. If you want to make things pretty
// You can also retrieve the pics of each ingredient used at each step.
public class GetRecipeInstructionsTask extends AsyncTask<Integer, Void, ArrayList<String>> {

    final String TAG = "GetRecipeInstructionsTask";

    @Override
    protected ArrayList<String> doInBackground(Integer... args) {

        try {

            return getRecipe(args[0]);

        } catch (JSONException e) {
            Log.i(TAG,"FUCK");
            return null;
        }

    }

    private ArrayList<String> getRecipe(int s) throws JSONException {

        HttpURLConnection connection = null;
        ArrayList<String> steps = new ArrayList<>();
        try {
            URL url = new URL("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/"
                    + s + "/analyzedInstructions?stepBreakdown=true");
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


            JSONArray parent = new JSONArray(json).getJSONObject(0).getJSONArray("steps");
            for (int i = 0; i < parent.length(); i++) {
                JSONObject obj = parent.getJSONObject(i);
                String step = obj.getString("step");
                steps.add(step);
                Log.i(TAG, step);
                // get ingredients with ingredients JSONArr then each JSONObj has image and name
                // get equipment with equipment JSONArr then each JSONObj has image and name
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
