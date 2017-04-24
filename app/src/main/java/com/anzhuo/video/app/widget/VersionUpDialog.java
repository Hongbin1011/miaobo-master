package com.anzhuo.video.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.utils.ActivityManager;
import com.anzhuo.video.app.utils.DialogUtils;
import com.anzhuo.video.app.utils.SharedUtil;


/**
 * creat on 2016/7/8.
 * QQ-632671653
 */
public class VersionUpDialog implements View.OnClickListener {
    Dialog versionUpDialog;
    View view;
    Context mContext;
    TextView version_up_cancel, version_up_start;
    String updateContent;
//    VersionInfo mVersionInfo;

    public VersionUpDialog(Context mContext) {
        this.mContext = mContext;
//        this.mVersionInfo = versionInfo;
        view = LayoutInflater.from(mContext).inflate(R.layout.version_up_dialog_layout, null);
        onCreateDialog(mContext);
    }

    private void onCreateDialog(Context context) {
        versionUpDialog = new Dialog(context);
        versionUpDialog.setCanceledOnTouchOutside(false);
        version_up_cancel = (TextView) view.findViewById(R.id.version_up_cancel);
        version_up_cancel.setOnClickListener(this);
        version_up_start = (TextView) view.findViewById(R.id.version_up_start);
        version_up_start.setOnClickListener(this);

        String versionCode = SharedUtil.getString("versionCode");
        String download_url = SharedUtil.getString("download_url");
        versionUpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        versionUpDialog.setContentView(view);
    }

    public void show() {
        versionUpDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.version_up_cancel:
                versionUpDialog.cancel();
                ActivityManager.getActivityManager().AppExit();
                break;
            case R.id.version_up_start:
                DialogUtils.showVersionDownDialog(mContext);
                versionUpDialog.cancel();
                break;
            default:
                break;
        }
    }
}
