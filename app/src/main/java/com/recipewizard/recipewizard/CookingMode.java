package com.example.yanrufish.tmp;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.Image;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.washington.cs.touchfreelibrary.sensors.CameraGestureSensor;

/**
 * Created by Yanru Bi on 11/18/2016.
 */

public class CookingMode extends AppCompatActivity implements CameraGestureSensor.Listener {

    TextView name, stepNum, ingredient, equipment, instruction;
    ImageView image;
    ProgressBar progressBar;
    Button prev, next;
    ArrayList<Step> stepList = new ArrayList<>();
    int totalSteps;
    int lastStep = 0, step = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cooking_mode);

        //final Bundle bundle = getIntent().getExtras();
        //stepList = bundle.getParcelableArrayList("steps");
        //System.out.println("stepSize "+stepList.size());
        stepList = RecipeSummaryActivity.getSteps();
        totalSteps = stepList.size();

        // get the recipe name
        name = (TextView) findViewById(R.id.recipe_name);
        name.setText(RecipeSummaryActivity.getName());

        stepNum = (TextView) findViewById(R.id.step_num);
        image = (ImageView) findViewById(R.id.recipe_item_image_button);
        image.setImageBitmap(RecipeSummaryActivity.getImage());
        ingredient = (TextView) findViewById(R.id.step_ingredient);
        equipment = (TextView) findViewById(R.id.step_equiments);
        instruction = (TextView) findViewById(R.id.instruction);
        instruction.setText(stepList.get(0).getDirection());
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cooking);
        progressBar.setProgress((int) Math.ceil(100.f/totalSteps));

        prev = (Button) findViewById(R.id.prev_button);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastStep != 0) {
                    Toast.makeText(CookingMode.this, "Getting prev step", Toast.LENGTH_SHORT).show();
                    progressBar.setProgress((int) Math.ceil(progressBar.getProgress() - 100.0f / totalSteps));
                    stepNum.setText("Step " + (step-1) + ": ");
                    instruction.setText(stepList.get(lastStep-1).getDirection());
                    ingredient.setText(ingredientsToString(stepList.get(lastStep-1).getIngredients()));
                    equipment.setText(equipmentToString(stepList.get(lastStep-1).getEquipment()));
                    lastStep--;
                    step--;
                } else {
                    Toast.makeText(CookingMode.this, "No prev step", Toast.LENGTH_SHORT).show();
                }
            }
        });

        next = (Button) findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastStep + 1 != stepList.size()) {
                    Toast.makeText(CookingMode.this, "Getting next step", Toast.LENGTH_SHORT).show();
                    progressBar.setProgress((int) Math.ceil(progressBar.getProgress() + 100.0f / totalSteps));
                    stepNum.setText("Step " + (step+1) + ": ");
                    instruction.setText(stepList.get(lastStep+1).getDirection());
                    ingredient.setText(ingredientsToString(stepList.get(lastStep+1).getIngredients()));
                    equipment.setText(equipmentToString(stepList.get(lastStep+1).getEquipment()));
                    lastStep++;
                    step++;
                } else {
                    Toast.makeText(CookingMode.this, "No next step", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        retrieveControls();
        loadOpenCV();

    }

    private String ingredientsToString(List<Ingredient> ingredients){
        String ret = "";
        for (Ingredient i : ingredients) {
            ret += (i.toString() + ", ");
        }
        ret = ret.substring(0, ret.length()-2);
        return ret;
    }
    private String equipmentToString(List<String> equipments) {
        String ret = "";
        for (String s : equipments) {
            ret += (s.toString() + ", ");
        }
        ret = ret.substring(0, ret.length()-2);
        return ret;
    }

    private static final String TAG = "";
    private JavaCameraView mCamera = null;
    CameraGestureSensor mGestureSensor = null;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch(status)
            {
                case LoaderCallbackInterface.SUCCESS:
                {
                    mGestureSensor = new CameraGestureSensor(CookingMode.this);
                    CameraGestureSensor.loadLibrary();
                    mGestureSensor.addGestureListener(CookingMode.this);

                    mGestureSensor.start(mCamera);
                }
                break;
                default:
                    Log.i(TAG, "Some other result than success");
                    break;
            }
        }
    };

    protected void retrieveControls()
    {
        mCamera = (JavaCameraView) findViewById(R.id.camera);
    }

    protected void loadOpenCV()
    {

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
        }
        else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onGestureUp(CameraGestureSensor caller, long gestureLength) {
        /*Log.i(TAG, "Up");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CookingMode.this, "Hand Motion Up", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public void onGestureDown(CameraGestureSensor caller, long gestureLength) {
        /*Log.i(TAG, "Down");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CookingMode.this, "Hand Motion Down", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public void onGestureLeft(CameraGestureSensor caller, long gestureLength) {
        Log.i(TAG, "Left");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                prev.callOnClick();
            }
        });
    }

    @Override
    public void onGestureRight(CameraGestureSensor caller, long gestureLength) {
        Log.i(TAG, "Right");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                next.callOnClick();
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mGestureSensor != null)
            mGestureSensor.stop();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        retrieveControls();
        if (mGestureSensor == null)
            mGestureSensor = new CameraGestureSensor(CookingMode.this);
        mGestureSensor.start(mCamera);
    }

}

