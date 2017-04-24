package com.anzhuo.video.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.utils.MyTextUtils;
import com.anzhuo.video.app.utils.ToastUtil;
import com.orhanobut.logger.Logger;


/**
 * creat on 2017/1/8.
 */

public class ShareDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private String mContent;

    public ShareDialog(Context context, String content) {
        super(context);
        mContext = context;
        mContent = content;
        Logger.i("=======[分享的东西]===========" + content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.share_dialog, null);
        setContentView(view);

        EditText share_edit = (EditText) view.findViewById(R.id.share_edit);
        Button btn_copy = (Button) view.findViewById(R.id.share_copy);

        share_edit.setText(mContent);
        btn_copy.setOnClickListener(this);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = d.widthPixels; // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_copy:
                MyTextUtils.copy(mContent, mContext);
                ToastUtil.showCenterToast("复制成功");
                dismiss();
                break;
        }
    }
}
