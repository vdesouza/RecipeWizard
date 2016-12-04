package com.recipewizard.recipewizard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

/**
 * Created by Brian on 12/4/2016.
 */

public class CookingModeLoaderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SummaryLoader", "Creating splash view for summary");
        getSupportActionBar().setTitle("Recipe Wizard");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ProgressDialog pd = new ProgressDialog(this,R.style.SpinnerTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.attr.progressBarStyleLarge);
        pd.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Bundle extras = getIntent().getExtras();
                Intent intent = new Intent(CookingModeLoaderActivity.this, CookingMode.class);
                intent.putExtras(extras);
                startActivity(intent);
                if(pd != null){
                    pd.dismiss();
                }
                finish();
            }
        }, 3000);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}