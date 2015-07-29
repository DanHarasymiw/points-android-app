package com.personal.dan.points;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.w3c.dom.Comment;

import java.nio.channels.NotYetBoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 17/06/2015.
 */
public class ActivitiesDataSource {
    public SQLiteDatabase database; //public
    public MySQLiteHelper dbHelper; //change to private

    //database fields
    private String [] activitiesColumns = {MySQLiteHelper.COLUMN_ACTIVITY,
            MySQLiteHelper.COLUMN_POINTS, MySQLiteHelper.COLUMN_TYPE };

    private String [] historyColumns = {MySQLiteHelper.COLUMN_DATE,
            MySQLiteHelper.COLUMN_ACTIVITY };

    private String [] pointsColumns = {MySQLiteHelper.COLUMN_POINTS };

    public ActivitiesDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        System.out.println("DATABASE OPENED");
    }

    public void close(){
        dbHelper.close();
    }

    public long createActivity(String activityName, int points, int type){
        activityName = activityName.toUpperCase();
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.COLUMN_ACTIVITY, activityName);
        values.put(MySQLiteHelper.COLUMN_POINTS, points);
        values.put(MySQLiteHelper.COLUMN_TYPE, type);

        return database.insert(MySQLiteHelper.ACTIVITIES_TABLE, null,
                values);

    }

    public void deleteActivity(String activityName){
        activityName = activityName = activityName.toUpperCase();
        database.delete(MySQLiteHelper.ACTIVITIES_TABLE, MySQLiteHelper.COLUMN_ACTIVITY + " =?", new String[] {activityName});
    }


    public List<PointsActivity> getActivities(int type){
        List<PointsActivity> activities = new ArrayList<PointsActivity>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.ACTIVITIES_TABLE
                + " WHERE " + MySQLiteHelper.COLUMN_TYPE + " = ?",
                new String[] {Integer.toString(type)});

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            PointsActivity activity = cursorToActivity(cursor);
            activities.add(activity);
            cursor.moveToNext();
        }
        cursor.close();
        return activities;
    }

    public List<PointsActivity> getAllActivities(){
        List<PointsActivity> pointsActivityList = new ArrayList<PointsActivity>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.ACTIVITIES_TABLE, null);

        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                PointsActivity activity = cursorToActivity(cursor);
                //String date = cursor.getString(3);
                //activity.setDate(date);
                pointsActivityList.add(activity);
                cursor.moveToNext();
            }
        }

        return pointsActivityList;

    }

    //Todo: modify to erase the last history row and update it with new one
    public void addHistoryEntry(PointsActivity activity){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DATE, activity.getDate());
        values.put(MySQLiteHelper.COLUMN_ACTIVITY, activity.getActivityName());
        values.put(MySQLiteHelper.COLUMN_TYPE, activity.getType());
        database.insert(MySQLiteHelper.HISTORY_TABLE, null, values);
    }

    public List<PointsActivity> getHistoryEntries(){
        List<PointsActivity> pointsActivityList = new ArrayList<PointsActivity>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.HISTORY_TABLE
                + " ORDER BY " + MySQLiteHelper.COLUMN_ID + " DESC", null);

        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                PointsActivity activity = cursorToHistoryActivity(cursor);
                pointsActivityList.add(activity);
                cursor.moveToNext();
            }
        }
        return pointsActivityList;
    }


    public PointsActivity cursorToActivity(Cursor cursor){
        PointsActivity pointsActivity = new PointsActivity();
        pointsActivity.setActivityName(cursor.getString(0));
        pointsActivity.setPoints(cursor.getInt(1));
        pointsActivity.setType(cursor.getInt(2));

        return pointsActivity;
    }

    public PointsActivity cursorToHistoryActivity(Cursor cursor){
        PointsActivity pointsActivity = new PointsActivity();
        pointsActivity.setDate(cursor.getString(1));
        pointsActivity.setActivityName(cursor.getString(2));
        pointsActivity.setType(cursor.getInt(3));
        return pointsActivity;
    }

    public void clearHistory(){
        database.execSQL("DELETE FROM " + MySQLiteHelper.HISTORY_TABLE);
    }

    public void createHistoryDummyRows(){
        //TODO: fill history table with ~1000 empty rows
    }






}
