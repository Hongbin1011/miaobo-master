package com.anzhuo.video.app.meinv.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.meinv.base.BaseActivity;
import com.anzhuo.video.app.meinv.manager.ActivityJump;
import com.anzhuo.video.app.meinv.manager.MyInterface;
import com.anzhuo.video.app.meinv.manager.NIManage;
import com.anzhuo.video.app.meinv.utils.FileUtils;
import com.anzhuo.video.app.meinv.view.CollapsibleDialog;
import com.anzhuo.video.app.search.utils.ToastUtil;
import com.anzhuo.video.app.search.utils.XUtilLog;
import com.anzhuo.video.app.utils.FileUtil;
import com.anzhuo.video.app.utils.HttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;


/**
 * Created by wbb on 2016/8/2.
 * app相关 详情界面
 */
public class AppRelevantActivity extends BaseActivity {

    private RelativeLayout versionUpdate;//版本更新
    private LinearLayout userFeedback;//用户反馈
    private LinearLayout myCollection;//我的收藏
    private TextView versionNumber;

    private CollapsibleDialog dialog = null;

    @Override
    protected int getLayoutRes() {
        return R.layout.app_relevant_layout;
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        setupStatus();
        setHeadTitle("设置");
        findViewAttachOnclick(R.id.version_update_layout);
        findViewAttachOnclick(R.id.user_feedback_layout);
        findViewAttachOnclick(R.id.my_collection_layout);
        versionNumber=findView(R.id.version_number_text);
        versionNumber.setText(FileUtil.getVersion(mContext));
    }

    private void getVersionData(){
        NIManage.VersionUpdate(new MyInterface() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.toastLong(mContext, "检测版本数据失败，请稍后重试！");
            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                if (state==200){
                    try{
                        JSONObject jo = JSON.parseObject(data);
                        String versioin1 = jo.getString("number");
                        String appUrl = jo.getString("android");
                        String appName = "mixiu";
                        String versioin2 = FileUtil.getVersion(mContext);
                        int serviceVersion = Integer.parseInt(versioin1.replace(".", ""));
                        int localVersion = Integer.parseInt(versioin2.replace(".", ""));
                        if (serviceVersion > localVersion) {
                            showDialog(appUrl,appName);
                        }
                    }catch (Exception e) {
                        ToastUtil.toastLong(mContext, "暂无新版本");
                        e.printStackTrace();
                    }
                }else {
                    ToastUtil.toastLong(mContext, "检测版本数据失败，请稍后重试！");
                }
            }
        });
    }

    private void downloadApk(String imgUrl,String name) {
        HttpUtils.downloadNewSelf(imgUrl, new FileCallBack(FileUtils.getDownloadApk(), name+".apk") {
            @Override
            public void onError(Call call, Exception e,int id) {
                ToastUtil.toastLong(mContext, "更新失败，请稍后重试！");
            }

            @Override
            public void onResponse(File response,int id) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(response),
                        "application/vnd.android.package-archive");
                startActivity(intent);
                XUtilLog.log_i("wbb", "=====[response]=====:" + response.toString());
            }

//            @Override
//            public void inProgress(float progress, long total) {
//            }
        });
    }

    private void showDialog(final String url, final String name) {
        if (dialog == null) {
            dialog = CollapsibleDialog.createExitPromptDialog(mContext,
                    "确  定","取  消", "有新版本，确认更新吗？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //确定按钮
                            downloadApk(url,name);
                            dialog.dismiss();
                        }
                    });
        }
        //取消按钮
        dialog.setNegativeButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //字体颜色
        dialog.setMessageColor(getResources().getColor(R.color.black));
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_back://返回
                finish();
                break;
            case R.id.version_update_layout://版本更新
                getVersionData();
                break;
            case R.id.user_feedback_layout://用户反馈
                ActivityJump.getInstance().JumpUserFeedback(mContext);
                break;
            case R.id.my_collection_layout://我的收藏
                ActivityJump.getInstance().JumpMyCollection(mContext);
                break;
        }
    }
}
