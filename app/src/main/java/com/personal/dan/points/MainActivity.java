package com.personal.dan.points;

/*
   Created by Daniel Harasymiw
    July 10, 2015
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewManager;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.List;


public class MainActivity extends Activity {

    public final String pointsPref = "pointsPreferenceFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w("DEBUGGING", "TEST");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        TextView pointsTextView = (TextView)findViewById(R.id.pointsText);

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
    }



    @Override
    public void onResume(){
        super.onResume();
        Log.d("DEBUGGING", "ONRESUME METHOD CALLED");
        SharedPreferences preferences = getSharedPreferences(pointsPref, MODE_PRIVATE);
        int points = preferences.getInt("points", 0);

        //update the points on the main activity
        String updatedPoints = Integer.toString(preferences.getInt("points", 0));
        Log.d("UPDATED POINTS", updatedPoints);
        ((TextView)findViewById(R.id.pointsText)).setText(updatedPoints);

        if (points < 0){
            Log.w("DEBUG", "Changing theme");
            this.setTheme(R.style.NegativeTheme);
        }


    }

    //These methods change the screen to the corresponding activity
    public void changeToAddPoints(View view){
        Intent intent = new Intent(MainActivity.this, ChangePointsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", PointsActivity.POSITIVE_ACTIVITY);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void changeToRemovePoints(View view){
        Intent intent = new Intent(MainActivity.this, ChangePointsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", PointsActivity.NEGATIVE_ACTIVITY);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void changeToRewards(View view){
        Intent intent = new Intent(MainActivity.this, ChangePointsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", PointsActivity.REWARD_ACTIVITY);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void changeToHistory(View view){
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(intent);
    }




}
