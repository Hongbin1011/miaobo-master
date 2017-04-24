/*
package com.anzhuo.video.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;


*/
/**
 * 用户相关dialog
 *
 *//*

public class UserInterfixDialog {
    private View view;
    private Dialog contributeDialog;
    private Context mContext;
    private TextView contribute_dialog_text;

    */
/**
     * @param context
     * @param desc    文字描述
     *//*

    public UserInterfixDialog(Context context, String desc) {
        mContext = context;
        initDialog(context, desc);
    }


    private void initDialog(Context context, String desc) {
        //  view = LayoutInflater.from(context).inflate(R.layout.contribute_dilaog_layout2, null);
        contributeDialog = new Dialog(context, R.style.CustomStyle2);
        contributeDialog.setCanceledOnTouchOutside(false);
        contributeDialog.setCancelable(false);
        contributeDialog.onWindowFocusChanged(false);
        contributeDialog.setContentView(R.layout.contribute_dilaog_layout2);
    }

    */
/**
     * 显示
     *//*

    public void showDialog() {
        contributeDialog.show();
    }

    */
/**
     * 关闭dialog
     *//*

    public void dismissDialog() {
        if (contributeDialog.isShowing())
            contributeDialog.dismiss();
    }

    */
/**
     * 是否在运行
     *
     * @return
     *//*

    public boolean isShowing() {
        return contributeDialog.isShowing();
    }

}
*/
