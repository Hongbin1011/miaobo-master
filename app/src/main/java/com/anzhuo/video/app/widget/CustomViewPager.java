package com.anzhuo.video.app.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/4/20.
 */
public class CustomViewPager extends ViewPager {

    private boolean isCanScroll = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     *  设置是否滑动
     * @param isCanScroll
     */
    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll == false) {
            return true;
        } else {
            return super.onTouchEvent(ev);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanScroll == false) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }

    }


}