package com.anzhuo.video.app.movie;

import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.constant.AppServerUrl;
import com.anzhuo.video.app.utils.HttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by husong on 2017/2/21.
 */

public class MovieMainActivity extends BaseActivity{
    private String destDir2 = Environment.getExternalStorageDirectory().toString() + "/1122_new.apk";
    private String patchDir = Environment.getExternalStorageDirectory().toString() + "/patch.patch";

    private long mExitTime = 0;

    @Override
    protected int getLayoutRes() {
        return R.layout.main_page;
    }


    @Override
    protected void bindViews() {
        super.bindViews();
//        startLacation();
        getMovieReferer();
        getMovieType();
        getShareUrl();
    }

    private void getMovieReferer(){
        HttpUtils.getStringAsync(AppServerUrl.MOVIE_REFERER_URL, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {

            }

            @Override
            public void onResponse(String response,int id) {
                JSONObject jsonObject = JSON.parseObject(response);
                String ret = jsonObject.getString("ret");
                if(TextUtils.equals(ret, "200")){
                    String datajson = jsonObject.getString("data");
                    JSONObject j2 = JSON.parseObject(datajson);
                    AppServerUrl.MOVIE_REFERER = j2.getString("url");
                }
            }
        });
    }

    private void getMovieType(){
        HttpUtils.getStringAsync(AppServerUrl.MOVIE_TYPE, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {

            }

            @Override
            public void onResponse(String response,int id) {
                JSONObject jsonObject = JSON.parseObject(response);
                String ret = jsonObject.getString("ret");
                if(TextUtils.equals(ret, "200")){
                    String datajson = jsonObject.getString("data");
                    List<MovieTypeInfo> types = JSON.parseArray(datajson, MovieTypeInfo.class);
                }
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if ((System.currentTimeMillis() - mExitTime) > 2000) {
//                DisplayUtil.showToast(this, R.string.exit_app);
//                mExitTime = System.currentTimeMillis();
//            } else {
//                finish();
//            }
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }


//    private TextView loadingText;
//    private ProgressBar progressBar;
//    private TextView negative;
//    private TextView positive;
//
//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case 1:
//                    int progress = (int)msg.obj;
//                    progressBar.setProgress(progress);
//                    loadingText.setText("正在下载:"+progress+"%");
//                    break;
//            }
//        }
//    };

    private void getShareUrl(){
        HttpUtils.getStringAsync(AppServerUrl.MOVIE_SHARE_URL, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {

            }

            @Override
            public void onResponse(String response,int id) {
                JSONObject jsonObject = JSON.parseObject(response);
                String ret = jsonObject.getString("ret");
                if(TextUtils.equals(ret, "200")){
                    String datajson = jsonObject.getString("data");

                    if(!TextUtils.isEmpty(datajson)) {
                        JSONObject jo = JSON.parseObject(datajson);
                        AppServerUrl.SHARE_URL = jo.getString("share_url");
                        AppServerUrl.BAIDUFROM = jo.getString("baidu_from");
                    }
                }
            }
        });
    }
}
