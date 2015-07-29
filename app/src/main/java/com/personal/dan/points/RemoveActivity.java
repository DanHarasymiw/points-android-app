package com.personal.dan.points;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Dan on 19/06/2015.
 */
public class RemoveActivity extends Activity {
    final Context context = this;
    ActivitiesDataSource activitiesDS;
    int type;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("ADD POINTS DEBUGGING", "ONCREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_activity);

        activitiesDS = new ActivitiesDataSource(this);

        type = getIntent().getExtras().getInt("type");
        Log.d("DEBUGGING", "POINTS SET TO: " + type);
    }

    @Override
    public void onResume(){
        super.onResume();


        try {
            activitiesDS.open();
            final List<PointsActivity> activitiesList = activitiesDS.getActivities(type);

            final LinearLayout ll = (LinearLayout) findViewById(R.id.remove_activity);


            TextView instructions = new TextView(context);
            instructions.setGravity(Gravity.CENTER);
            instructions.setText("Select the activities that you wish to remove");
            ll.addView(instructions);

            final List<PointsActivity> list = activitiesDS.getActivities(type);

            //Create the parameters for the buttons
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 20);
            buttonLayoutParams.setMargins(25, 50, 25, 0);

            for (int i = 0; i < list.size(); i++) {
                final int num = i;
                final Button button = new Button(context);
                button.setText(list.get(i).getActivityName());

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Confirmation popup dialog

                        AlertDialog.Builder confirmDeleteDialogBuilder = new AlertDialog.Builder(context);
                        confirmDeleteDialogBuilder.setTitle("Remove Activity?");
                        confirmDeleteDialogBuilder.setMessage("Are you sure you want to delete this activity?");

                        confirmDeleteDialogBuilder.setPositiveButton("YES",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                activitiesDS.deleteActivity(list.get(num).getActivityName());
                                ll.removeView(button);
                                dialog.cancel();
                            }
                        });

                        confirmDeleteDialogBuilder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog confirmDeleteDialog = confirmDeleteDialogBuilder.create();
                        confirmDeleteDialog.show();

                    }
                });



                ll.addView(button, buttonLayoutParams);

            }

        } catch (SQLException e) {
            Log.d("Debugging", "SQL ERROR");
        }
    }


    public void onPause(){
        super.onPause();
        activitiesDS.close();
    }

}
