package org.androidtown.dbproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class MeasuredViewPager extends ViewPager {


    public MeasuredViewPager(Context context) {
        super(context);
    }

    public MeasuredViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        return false;
    }





}
