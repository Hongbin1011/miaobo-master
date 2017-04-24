package com.anzhuo.video.app.meinv.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wbb on 2016/8/23.
 */
public class MyViewPager extends ViewPager {

    private boolean isLocked;
    private final int AUTO_HANDLER_WHAT = 2;
    private final Timer mTimer = new Timer(true);
    private boolean timeCondition = true;
    private AutoTimerTask mTimerTask;

    public MyViewPager(Context context) {
        super(context);
        isLocked = false;
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        isLocked = false;
    }

    public void startAuto(int second, int duration) {
        if (mTimerTask == null) {
            mTimerTask = new AutoTimerTask();
        }
        mTimer.schedule(mTimerTask, (second * 1000), (duration * 1000));
    }

    public void setAutoEnable(boolean isAuto) {
        this.timeCondition = isAuto;
    }

    public class AutoTimerTask extends TimerTask {
        public void run() {
            if (timeCondition) {
                Message msg = new Message();
                msg.what = AUTO_HANDLER_WHAT;
                AutoScroolHandler.sendMessage(msg);
            }
        }
    }

    public void setCurrentPosi() {
        this.setCurrentItem(getCurrentItem() + 1, true);
    }

    final Handler AutoScroolHandler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == AUTO_HANDLER_WHAT) {
                setCurrentPosi();
            }
        }
    };

    public void onDestroy() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        this.mTimerTask = null;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isLocked) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setAutoEnable(false);
                break;
            case MotionEvent.ACTION_UP:
                setAutoEnable(true);
                break;
        }
        return super.onTouchEvent(event);
    }

    public void toggleLock() {
        isLocked = !isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isLocked() {
        return isLocked;
    }
}
