package com.anzhuo.video.app.manager.fuli2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;


/**
 * Created by husong on 2017/2/21.
 */

public class ProgressDialog {
    public static Dialog createProgressDialog(final Context context, String message, DialogInterface.OnCancelListener mOnCancelListener) {
        // android.view.WindowManager$BadTokenException: Unable to add window --
        // token android.os.BinderProxy@426c22f0 is not valid; is your activity
        // running?
        Activity activity = (Activity) context;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        final Dialog d = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        try {
            d.setContentView(R.layout.tip_dialog_ui);
            final Window w = d.getWindow();
            TextView txtMsg = (TextView) d.findViewById(R.id.tip_dialog_msg);
            if (message != null)
                txtMsg.setText(message);
            d.setCanceledOnTouchOutside(false);
            if (mOnCancelListener != null)
                d.setOnCancelListener(mOnCancelListener);
            final WindowManager.LayoutParams wlps = w.getAttributes();
            wlps.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
            wlps.height = WindowManager.LayoutParams.WRAP_CONTENT;
            wlps.dimAmount = 0.3f;
            wlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            wlps.softInputMode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
            wlps.gravity = Gravity.CENTER;
            w.setAttributes(wlps);
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
