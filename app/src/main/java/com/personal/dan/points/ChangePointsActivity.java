package com.personal.dan.points;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Dan on 17/06/2015.
 */
public class ChangePointsActivity extends MyActivity {

    final Context context = this;
    ActivitiesDataSource activitiesDS;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ADD POINTS DEBUGGING", "ONCREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_points_activity);

        activitiesDS = new ActivitiesDataSource(this);
        type = this.getIntent().getExtras().getInt("type");

    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            activitiesDS.open();
            //activitiesDS.dbHelper.onUpgrade(activitiesDS.database, 1, 1);
            final List<PointsActivity> activitiesList = activitiesDS.getActivities(type);

            LinearLayout ll = (LinearLayout)findViewById(R.id.change_points_activity);
            ll.removeAllViews();


            //Add the points to the top of the screen
            SharedPreferences sharedPreferences = getSharedPreferences("pointsPreferenceFile", MODE_PRIVATE);
            int pointsNum = sharedPreferences.getInt("points", 0);
            final TextView pointsTextView = new TextView(this);
            pointsTextView.setText(Integer.toString(pointsNum));
            pointsTextView.setTextAppearance(this, R.style.PointsText);
            pointsTextView.setGravity(Gravity.CENTER);
            //Typeface pointsFont = Typeface.createFromAsset(getAssets(), "alarm clock.tff");
            //pointsTextView.setTypeface(pointsFont);
            ll.addView(pointsTextView);

            //Create the parameters for the activity buttons
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonLayoutParams.setMargins(25, 50, 25, 0);

            for (int i = 0; i < activitiesList.size(); i++){
                final int num = i;
                final Button button = new Button(this);

                String buttonText = activitiesList.get(i).getActivityName() + "\t(";

                if (type != PointsActivity.POSITIVE_ACTIVITY){
                    buttonText += Integer.toString(activitiesList.get(i).getPoints() * -1);
                }
                else {
                    buttonText += Integer.toString(activitiesList.get(i).getPoints());
                }
                buttonText += ")";

                button.setText(buttonText);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //add the points
                        SharedPreferences sharedPreferences = getSharedPreferences("pointsPreferenceFile", MODE_PRIVATE);
                        int points = sharedPreferences.getInt("points", 0);

                        //if it is a positive activity or if we have points to spend, change the points
                        if (type == PointsActivity.POSITIVE_ACTIVITY || points - activitiesList.get(num).getPoints() > 0){
                            points += activitiesList.get(num).getPoints();
                            SharedPreferences.Editor editor = getSharedPreferences("pointsPreferenceFile", MODE_PRIVATE).edit();
                            editor.putInt("points", points);
                            editor.commit();

                            //update the points edit text
                            pointsTextView.setText(Integer.toString(points));


                            //add entry to history table
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DATE, 1);
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            String date = format.format(cal.getTime());
                            activitiesList.get(num).setDate(date);
                            activitiesDS.addHistoryEntry(activitiesList.get(num));
                        }
                        //if we don't have the points to spend create a toast alerting the user
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(), "You don't have enough points to spend!", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }
                });


                ll.addView(button, buttonLayoutParams);

                //ll.addView(button);
            }


            Button createButton = new Button(this);
            createButton.setText("CREATE");
            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder createDialogBuilder = new AlertDialog.Builder(context);
                    createDialogBuilder.setTitle("Create new Activity");

                    //input for dialog box
                    final EditText name = new EditText(context);
                    name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});

                    final NumberPicker pointValuePicker = new NumberPicker(context);
                    pointValuePicker.setMinValue(1);
                    pointValuePicker.setMaxValue(10);

                    LinearLayout createDialogLL = new LinearLayout(context);
                    createDialogLL.setOrientation(LinearLayout.VERTICAL);
                    createDialogLL.addView(name);
                    createDialogLL.addView(pointValuePicker);

                    createDialogBuilder.setView(createDialogLL);

                    createDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (!name.getText().toString().trim().equals("")){
                                int pointValue = pointValuePicker.getValue();
                                if (type != PointsActivity.POSITIVE_ACTIVITY) {
                                    pointValue *= -1;
                                }
                                long rowsChanged = activitiesDS.createActivity(name.getText().toString(), pointValue, type);
                                Log.d("Debugg", "TYPE OF ACTIVITY: " + Integer.toString(type));
                                if (rowsChanged == -1) {
                                    AlertDialog.Builder successDialogBuilder = new AlertDialog.Builder(context);
                                    successDialogBuilder.setTitle("Uh Oh!");
                                    successDialogBuilder.setMessage("Activity already exists!");

                                    AlertDialog successDialog = successDialogBuilder.create();
                                    successDialog.show();

                                } else {
                                    onResume();
                                }

                            }

                            dialog.cancel();


                        }
                    });
                    createDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

                    AlertDialog createDialog = createDialogBuilder.create();
                    createDialog.show();

                }


            });



            Button deleteButton = new Button(this);
            deleteButton.setText("DELETE");
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ChangePointsActivity.this, RemoveActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", type);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            });


            LinearLayout addRemoveLayout = new LinearLayout(context);
            addRemoveLayout.setOrientation(LinearLayout.HORIZONTAL);
            //addRemoveLayout.setGravity(Gravity.CENTER);

            //add margin to left and right of buttons
            LinearLayout.LayoutParams createRemoveLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 20);
            createRemoveLayoutParams.setMargins(25, 50, 25, 0);

            addRemoveLayout.addView(deleteButton, createRemoveLayoutParams);
            addRemoveLayout.addView(createButton, createRemoveLayoutParams);







            //add the add/remove layout to the layout
            LinearLayout.LayoutParams addRemoveLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            addRemoveLayoutParams.setMargins(0, 50, 0, 0);

            ll.addView(addRemoveLayout);

        }
        catch (SQLException e){
            Log.d("Debugging", "Error with database");
        }


    }


    @Override
    public void onPause(){
        super.onPause();
        activitiesDS.close();
    }


}
