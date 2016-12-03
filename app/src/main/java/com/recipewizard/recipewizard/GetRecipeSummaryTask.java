package com.recipewizard.recipewizard;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Hank on 12/3/2016.
 */

public class GetRecipeSummaryTask extends AsyncTask<Integer, Void, String> {

    private final String TAG = "GetRecipeSummaryTask";
    @Override
    protected String doInBackground(Integer... args) {

        return getRecipeSummary(args[0]);

    }

    private String getRecipeSummary(int id) {
        HttpURLConnection connection = null;
        StringBuilder summary = new StringBuilder("");
        try {
            URL url = new URL("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" + id + "/summary");
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

            summary.append(new JSONObject(json).getString("summary"));

            //Log.i(TAG, summary.toString());
            boolean bracket = false;
            for (int i = summary.length() - 1; i >= 0; i--) {
                if (summary.charAt(i) == '>') {
                    bracket = true;
                    summary.deleteCharAt(i);
                } else if (summary.charAt(i) == '<') {
                    bracket = false;
                    summary.deleteCharAt(i);
                } else if (bracket) {
                    summary.deleteCharAt(i);
                }
            }
            //Log.i(TAG, summary.toString());

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
            return summary.toString();
        }
    }
}
