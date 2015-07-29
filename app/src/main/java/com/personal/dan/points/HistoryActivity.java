package com.personal.dan.points;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Dan on 19/06/2015.
 */
public class HistoryActivity extends MyActivity {

    ActivitiesDataSource activitiesDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w("DEBUGGING", "TEST");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        activitiesDS = new ActivitiesDataSource(this);
        try {
            activitiesDS.open();

            List<PointsActivity> pointsActivityList = activitiesDS.getHistoryEntries();

            LinearLayout ll = (LinearLayout)findViewById(R.id.history_activity);


            //
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 20);
            buttonLayoutParams.setMargins(25, 50, 25, 0);


            String currentDate = "";
            if (pointsActivityList.size() > 0){
                currentDate = pointsActivityList.get(0).getDate();
                TextView dateText = new TextView(this);
                dateText.setText(pointsActivityList.get(0).getDate());
                dateText.setGravity(Gravity.CENTER);
                ll.addView(dateText);
            }




            for (int i = 0; i < pointsActivityList.size(); i++){
                TextView text = new TextView(this);
                text.setText(pointsActivityList.get(i).getActivityName());
                int color;
                Log.d("Debugging", Integer.toString(pointsActivityList.get(i).getType()));
                if (pointsActivityList.get(i).getType() == 1){
                    color = Color.GREEN;
                }
                else if (pointsActivityList.get(i).getType() == 2) {
                    color = Color.RED;
                }
                else {
                    color = Color.YELLOW;
                }
                text.setTextColor(color);
                text.setGravity(Gravity.CENTER);

                if (!pointsActivityList.get(i).getDate().equals(currentDate)){
                    currentDate = pointsActivityList.get(i).getDate();
                    TextView dateText = new TextView(this);
                    dateText.setText("\n" + pointsActivityList.get(i).getDate());
                    dateText.setGravity(Gravity.CENTER);
                    ll.addView(dateText);
                }

                ll.addView(text);
            }



        }
        catch (SQLException e){
            Log.d("Debugging", "SQL Exception");
        }





    }

    @Override
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
    }



}
