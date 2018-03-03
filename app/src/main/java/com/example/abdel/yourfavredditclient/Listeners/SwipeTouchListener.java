package com.example.abdel.yourfavredditclient.Listeners;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by abdel on 2/25/2018.
 */

public class SwipeTouchListener implements View.OnTouchListener {

    static int MIN_SWIPE = 50;
    Context context;
    SwipeListenerInterface listenerInterface;

    public SwipeTouchListener(Context context,SwipeListenerInterface listenerInterface) {
        this.context = context;
        this.listenerInterface = listenerInterface;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return new GestureDetector(context,new gestureDetector())
                .onTouchEvent(event);
    }

    private class gestureDetector extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            if (distanceX > distanceY && distanceX >= MIN_SWIPE)
            {
                if (e1.getX() > e2.getX())
                    listenerInterface.SwipeLeft();
                else
                    listenerInterface.SwipeRight();
            }

            return false;
        }
    }
}
