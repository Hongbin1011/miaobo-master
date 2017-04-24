package com.anzhuo.video.app.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/10/9 0009.
 */

public class IconFontTextView extends TextView {

    static Typeface iconFont;

    public IconFontTextView(Context context) {
        super(context);
        init(context);
    }

    public IconFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IconFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //字体资源放入assets文件夹中
        if (null == iconFont) {
            AssetManager am = context.getAssets();
            iconFont = Typeface.createFromAsset(am, "iconfont.ttf");
        }
        setTypeface(iconFont);
    }
}