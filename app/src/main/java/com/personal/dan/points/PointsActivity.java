package com.personal.dan.points;

/**
 * Created by Dan on 17/06/2015.
 */
public class PointsActivity {
    private String activityName;
    private int points;
    private int type;
    private String date;


    public final static int POSITIVE_ACTIVITY = 1;
    public final static int NEGATIVE_ACTIVITY = 2;
    public final static int REWARD_ACTIVITY = 3;

    public PointsActivity(){

    }

    public PointsActivity(String activityName, int points, int type){
        setActivityName(activityName);
        setPoints(points);
        setType(type);
    }



    public String getActivityName(){
        return activityName;
    }

    public void setActivityName(String activityName){
        this.activityName = activityName;
    }

    public int getPoints(){
        return points;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public int getType(){
        return type;
    }

    public void setType(int type){
        this.type = type;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return date;
    }

}
