package com.k3.rtlion.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class XViewPager extends android.support.v4.view.ViewPager {

    private boolean allowSwipe = true;

    public XViewPager(Context context) {
        super(context);
    }

    public XViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void allowSwiping(boolean allow){
        this.allowSwipe = allow;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            if(allowSwipe)
                return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if(allowSwipe)
                return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}