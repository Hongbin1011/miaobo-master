package com.anzhuo.video.app.manager.fuli2.x5blink;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;


public class RefreshActivity extends Activity{
	X5WebView webView;
	TextView title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refresh_layout);
		webView=(X5WebView)findViewById(R.id.web_filechooser);
		title = (TextView) findViewById(R.id.refreshText);
		webView.setTitle(title);
		webView.loadUrl("http://app.html5.qq.com/navi/index");
		this.initBtn();
	}
	
	private void initBtn(){
		Button btnFlush=(Button) findViewById(R.id.bt_filechooser_flush);
		btnFlush.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webView.reload();
				//webView.setDayOrNight(false);
			}
		});
		
		Button btnBackForward=(Button) findViewById(R.id.bt_filechooser_back);
		btnBackForward.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				webView.goBack();
			}
		});
		
		Button btnHome=(Button) findViewById(R.id.bt_filechooser_home);
		btnHome.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				webView.loadUrl("http://app.html5.qq.com/navi/index");
			}
		});
	}
}
