package com.anzhuo.video.app.manager.fuli2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.anzhuo.video.app.manager.fuli.GameActivity;
import com.anzhuo.video.app.manager.fuli2.activity.SubActivity;

/**
 * Created with Android Studio.
 * Authour：Eric_chan
 * Date：2016/9/29 10:12
 * Des：
 */
public class PageSwitcher {
    public final static String INTENT_EXTRA_FRAGMENT_TYPE = "fragment_type";
    public final static String INTENT_EXTRA_FRAGMENT_ARGS = "args";
    public final static String BUNDLE_FRAGMENT_CACHE = "cache";
    public final static String BUNDLE_FRAGMENT_ANIM = "anim";

    public static void switchToPage(Context context, int fragmentType, Bundle bundle, int intentFlag){
        Intent intent = new Intent(context, SubActivity.class);
        if (intentFlag != 0) {
            intent.setFlags(intentFlag);
        }
        intent.putExtra(INTENT_EXTRA_FRAGMENT_TYPE, fragmentType);
        if (bundle != null) {
            intent.putExtra(INTENT_EXTRA_FRAGMENT_ARGS, bundle);
        }

        context.startActivity(intent);
    }

    public static void switchToPage(Context context, int fragmentType){
        switchToPage(context, fragmentType, null, Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public static void switchToPage(Context context, int fragmentType, Bundle bundle) {
        switchToPage(context, fragmentType, bundle, Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public static void switchToGamePage(Context context, int fragmentType, Bundle bundle) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(INTENT_EXTRA_FRAGMENT_TYPE, fragmentType);
        if (bundle != null) {
            intent.putExtra(INTENT_EXTRA_FRAGMENT_ARGS, bundle);
        }

        context.startActivity(intent);
    }
}
