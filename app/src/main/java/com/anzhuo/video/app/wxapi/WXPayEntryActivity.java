package com.anzhuo.video.app.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
	private String PAY_ORDER_TYPE_KEY = "";
	private String orderNumber = "";
	private String goodsDetail = "";
	private String orderId = "";

//	@Override
//	protected void initViews(Bundle savedInstanceState) {
//		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
//		api.handleIntent(getIntent(), this);
//
//
//	}


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		api = WXAPIFactory.createWXAPI(this, Constance.WX_APP_ID); //TODO 微信appid
//		api.handleIntent(getIntent(), this);
	}


	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Logger.e("--------------------onNewIntent------------");
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Logger.e(TAG, "onPayFinish, errCode = " + resp.errCode);

		Logger.e("--------------------onResp------------");

//		orderNumber = Constance.WX_ORDER_NO;
		Logger.e("----------微信支付orderNumber--------"+orderNumber);
//		orderNumber =orginData.getString(Constants.PAY_ORDER_ID);
		Logger.e("-------PAY_ORDER_TYPE_KEY---------"+PAY_ORDER_TYPE_KEY);
		Logger.e("-------orderId---------"+orderId);
		Logger.e("-------orderNumber---------"+orderNumber);
		Logger.e("-------goodsDetail---------"+goodsDetail);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			int code = resp.errCode;
			String msg = "";
			Intent intent;
			switch (code) {
				case 0:
					msg = "支付成功！";
					Logger.e("---------0---------------");
					SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
					String date = sDateFormat.format(new java.util.Date());

					//TODO  在此进行支付回调 接口
					break;
				case -1:
					msg = "支付失败！"+resp.errStr;
					break;
				case -2:
					msg = "您取消了支付！";
					break;

				default:
					msg = "支付失败！";
					break;
			}

		}
	}










}