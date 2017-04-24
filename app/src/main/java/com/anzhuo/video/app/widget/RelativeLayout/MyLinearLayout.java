package com.anzhuo.video.app.widget.RelativeLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * created on 2016/10/18 0018.
 * 主要是监听整个布局的高度来判断键盘是显示还是隐藏
 */

public class MyLinearLayout extends LinearLayout {
    private InputWindowListener listener;

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldh > h) {
            listener.show();
        } else {
            listener.hidden();
        }
    }

    public void setListener(InputWindowListener listener) {
        this.listener = listener;
    }


}