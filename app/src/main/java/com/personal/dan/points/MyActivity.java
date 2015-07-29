package com.personal.dan.points;

import android.app.Activity;
import android.view.MotionEvent;

/**
 * Created by Dan on 15/07/2015.
 */
public class MyActivity extends Activity {

    private float x1, x2;
    private final float MIN_SWIPE_DISTANCE = 150;

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();

                if (Math.abs(x2 - x1) > MIN_SWIPE_DISTANCE){
                    if (x2 > x1){
                        finish();//go back to the previous activity
                    }

                    
                }

                break;
        }

        return super.onTouchEvent(event);
    }



}
