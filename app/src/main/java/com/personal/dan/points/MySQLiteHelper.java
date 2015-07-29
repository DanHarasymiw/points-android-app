package com.personal.dan.points;


/**
 * Created by Dan on 17/06/2015.
 */

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MySQLiteHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "points.db";
    public static final int DATABASE_VERSION = 1;

    public static final String POINTS_TABLE = "pointsTable";
    public static final String ACTIVITIES_TABLE = "activitiesTable";
    public static final String HISTORY_TABLE = "historyTable";

    public static final String COLUMN_ACTIVITY = "activityText";
    public static final String COLUMN_POINTS = "numberOfPoints";
    public static final String COLUMN_TYPE = "typeOfActivity";
    public static final String COLUMN_DATE = "activityDate";
    public static final String COLUMN_ID = "activityID";

    // Database creation sql statement
    private static final String DATABASE_CREATE_ACTIVITIES = "create table "
            + ACTIVITIES_TABLE + "(" + COLUMN_ACTIVITY + " text primary key, "
            + COLUMN_POINTS + " integer not null, "
            + COLUMN_TYPE + " integer not null);";

    private static final String DATABASE_CREATE_HISTORY = "create table "
            + HISTORY_TABLE + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_DATE + " text not null, "
            + COLUMN_ACTIVITY + " text not null, "
            + COLUMN_TYPE + " text not null);";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        Log.w("DEBUGGING", "WAS DATABASE CREATED?");
        database.execSQL(DATABASE_CREATE_ACTIVITIES);
        database.execSQL(DATABASE_CREATE_HISTORY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + ACTIVITIES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE);
        onCreate(db);

    }



}
