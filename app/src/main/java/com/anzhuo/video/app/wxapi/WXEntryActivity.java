
package com.anzhuo.video.app.wxapi;

import android.os.Bundle;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {
    private String TAG = "WXEntryActivity";

    private String APP_ID = "wx31fccdffa8db014b";//optyAppId=wxf0af3c25410ee37d


    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e("LfWXEntryActivity","微信分享onCreate");
//        CommonUtil.print(TAG, "onCreate()", 2);

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, false);

        // 将该app注册到微信
        api.registerApp(APP_ID);

        api.handleIntent(getIntent(), this);

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        String result ;
        Logger.e("LfWXEntryActivity","微信分享返回"+resp.errCode+resp.errStr);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "微信分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "微信分享被取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "微信分享失败";
                break;
            default:
                result = "微信分享未知";
                break;
        }

//        new AlertDialog.Builder(this).setMessage(result).setPositiveButton("好的", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        }).create().show();
        Logger.e("----------result---------"+result);
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        finish();
    }

}
