package com.anzhuo.video.app.meinv.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.meinv.load.ImageLoaderManager;
import com.anzhuo.video.app.search.utils.XUtilLog;
import com.anzhuo.video.app.utils.ViewUtil;


/**
 * 自定义Dialog1
 * 
 * @author glj
 * 
 */
public class CollapsibleDialog extends Dialog {
	private Context context;
	private View view;
	private ImageView exit_ad_show;
	private TextView msgTextView1,msgTextView2;
	private TextView negativeTextView1,negativeTextView2;
	private TextView positiveTextView;
	private LinearLayout mCancel_layout1,mCancel_layout2;
	private OnShowListener mOnShowListener;
	private OnDismissListener mOnDismissListener;

	public CollapsibleDialog(Context context) {
		super(context, R.style.Setting_MyDialogTheme);
		this.context = context;
		initView();
	}

	private void initView() {
		view = LayoutInflater.from(context).inflate(R.layout.collapsible_dialog_m,
				null);
		msgTextView1 = (TextView) view.findViewById(R.id.textview_msg1);
		msgTextView2 = (TextView) view.findViewById(R.id.textview_msg2);
		positiveTextView = (TextView) view.findViewById(R.id.textview_positive);
		negativeTextView1 = (TextView) view.findViewById(R.id.textview_negative1);
		negativeTextView2 = (TextView) view.findViewById(R.id.textview_negative2);
		mCancel_layout1 = (LinearLayout) view.findViewById(R.id.cancel_layout1);
		mCancel_layout2 = (LinearLayout) view.findViewById(R.id.cancel_layout2);
		exit_ad_show=(ImageView) view.findViewById(R.id.exit_ad_show);
		this.setContentView(view);
		super.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				WindowManager.LayoutParams lp = ((Activity) context)
						.getWindow().getAttributes();
				lp.alpha = .4f;
				((Activity) context).getWindow().setAttributes(lp);
				if (mOnShowListener != null) {
					mOnShowListener.onShow(dialog);
				}
			}
		});
		super.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				WindowManager.LayoutParams lp = ((Activity) context)
						.getWindow().getAttributes();
				lp.alpha = 1f;
				((Activity) context).getWindow().setAttributes(lp);
				if (mOnDismissListener != null) {
					mOnDismissListener.onDismiss(dialog);
				}
			}
		});
		setNegativeButtonOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CollapsibleDialog.this.dismiss();
			}
		});
		setNegative2ButtonOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CollapsibleDialog.this.dismiss();
			}
		});
	}

	public void setMessage(CharSequence msg) {
		msgTextView1.setText(msg);
		msgTextView1.setVisibility(View.VISIBLE);
		msgTextView1.setPadding(0, 50, 0, 50);
		msgTextView2.setVisibility(View.GONE);
	}
	
	public void setExitMessage(CharSequence msg) {
		msgTextView2.setText(msg);
		msgTextView1.setVisibility(View.GONE);
		msgTextView2.setVisibility(View.VISIBLE);
		msgTextView2.setGravity(Gravity.CENTER_HORIZONTAL);
	}
	
	public void setMessage2(CharSequence msg1,CharSequence msg2) {
		msgTextView1.setText(msg1);
		msgTextView2.setText(msg2);
		msgTextView1.setVisibility(View.VISIBLE);
		msgTextView2.setVisibility(View.VISIBLE);
	}
	public void setNegativeGone() {
		ViewUtil.gone(mCancel_layout1);
		ViewUtil.visible(mCancel_layout2);
	}

	public void setNegativeText(String negativeText) {
		XUtilLog.log_i("wbb", "=====negativeText=====:" + negativeText);
		negativeTextView1.setText(negativeText);
	}

	public void setPositiveText(String positiveText) {
		XUtilLog.log_i("wbb", "=====positiveText=====:"+positiveText);
		positiveTextView.setText(positiveText);
	}
	
	public void setNegativeColor(int color){
		negativeTextView1.setTextColor(color);
	}
	
	public void setNegative2Color(int color){
		negativeTextView2.setTextColor(color);
	}

	public void setNegative2Text(String str){
		negativeTextView2.setText(str);
	}

	public void setPositiveColor(int color){
		positiveTextView.setTextColor(color);
	}
	
	public void setMessageColor(int color) {
		msgTextView1.setTextColor(color);
	}
	
	public void setMessage2Color(int color){
		msgTextView2.setTextColor(color);
	}
	
	public void setMessage1FontSize(int size){
		msgTextView1.setTextSize(size);
	}
	
	public void setMessage2FontSize(int size){
		msgTextView2.setTextSize(size);
	}
	
	public void setMessage2Gone(){
		msgTextView2.setVisibility(View.GONE);
	}
	
	public void setExitImage(Context context,String url) {
		LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) msgTextView2.getLayoutParams(); //取控件textView当前的布局参数
		linearParams.width = linearParams.MATCH_PARENT;
		linearParams.height = linearParams.WRAP_CONTENT;
//		linearParams.topMargin=-10;
//		linearParams.bottomMargin=-10;
		msgTextView2.setLayoutParams(linearParams);
		ImageLoaderManager.displayImageByUrl(VideoApplication.getContext(),exit_ad_show,url);
		exit_ad_show.setVisibility(View.VISIBLE);
	}

	public void setNegativeButtonOnClickListener(
			View.OnClickListener onClickListener) {
		negativeTextView1.setOnClickListener(onClickListener);
	}
	
	public void setNegative2ButtonOnClickListener(
			View.OnClickListener onClickListener) {
		negativeTextView2.setOnClickListener(onClickListener);
	}

	public void setPositiveButtonOnClickListener(
			View.OnClickListener onClickListener) {
		positiveTextView.setOnClickListener(onClickListener);
	}


	
	@Override
	public void setOnShowListener(OnShowListener onShowListener) {
		mOnShowListener = onShowListener;
	}

	@Override
	public void setOnDismissListener(OnDismissListener onDismissListener) {
		mOnDismissListener = onDismissListener;
	}
	
	public void cancelLayout1(){
		mCancel_layout1.setVisibility(View.GONE);
		mCancel_layout2.setVisibility(View.VISIBLE);
	}
	
	public void cancelLayout2(){
		mCancel_layout1.setVisibility(View.VISIBLE);
		mCancel_layout2.setVisibility(View.GONE);
	}
	
	/**
	 * 创建dialog
	 * @param context
	 * @param positiveText
	 * @param negativeText
	 * @param msg
	 * @param onClickListener
	 * @return dialog.
	 */
	public static CollapsibleDialog jfExchangePromptDialog(Context context,String succeed,CharSequence msg2,
			String positiveText, String negativeText, CharSequence msg,
			View.OnClickListener onClickListener) {
		CollapsibleDialog dialog = new CollapsibleDialog(context);
		dialog.setMessage2(msg, msg2);
		if (succeed.equals("yes")) {
			dialog.cancelLayout1();
		}else {
			dialog.cancelLayout2();
			XUtilLog.log_i("wbb", "=====negativeText=====:"+negativeText);
			dialog.setNegativeText(negativeText);
		}
		XUtilLog.log_i("wbb", "=====positiveText=====:" + positiveText);
		dialog.setPositiveText(positiveText);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setPositiveButtonOnClickListener(onClickListener);
		return dialog;
	}
	
	
	/**
	 * 创建dialog
	 * @param context
	 * @param positiveText
	 * @param negativeText
	 * @param msg
	 * @param onClickListener
	 * @return dialog.
	 */
	public static CollapsibleDialog createPromptExchangeDialog(Context context,
			String positiveText, String negativeText, CharSequence msg,
			View.OnClickListener onClickListener) {
		CollapsibleDialog dialog = new CollapsibleDialog(context);
		dialog.setMessage(msg);
		dialog.setPositiveText(positiveText);
		dialog.setNegativeText(negativeText);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setPositiveButtonOnClickListener(onClickListener);
		return dialog;
	}
	
	
	/**
	 * 创建dialog
	 * @param context
	 * @param positiveText
	 * @param negativeText
	 * @param msg
	 * @param onClickListener
	 * @return dialog.
	 */
	public static CollapsibleDialog createPromptDialog(Context context,
			String positiveText, String negativeText, CharSequence msg,
			View.OnClickListener onClickListener) {
		CollapsibleDialog dialog = new CollapsibleDialog(context);
		dialog.setMessage(msg);
		dialog.setPositiveText(positiveText);
		dialog.setNegativeText(negativeText);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setPositiveButtonOnClickListener(onClickListener);
		return dialog;
	}
	
	/**
	 * 创建 退出 的 dialog
	 * @param context
	 * @param positiveText
	 * @param negativeText
	 * @param msg
	 * @param onClickListener
	 * @return dialog.
	 */
	public static CollapsibleDialog createExitPromptDialog(Context context,
			String positiveText, String negativeText, CharSequence msg,
			View.OnClickListener onClickListener) {
		CollapsibleDialog dialog = new CollapsibleDialog(context);
		dialog.setExitMessage(msg);
		dialog.setPositiveText(positiveText);
		dialog.setNegativeText(negativeText);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setPositiveButtonOnClickListener(onClickListener);
		return dialog;
	}

	/**
	 * 创建 一个按钮 dialog
	 * @param context
	 * @param negativeText
	 * @param msg
	 * @param onClickListener
	 * @return dialog.
	 */
	public static CollapsibleDialog createExitPromptDialog(Context context,
														   String negativeText, CharSequence msg,
														   View.OnClickListener onClickListener) {
		CollapsibleDialog dialog = new CollapsibleDialog(context);
		dialog.setExitMessage(msg);
		dialog.setNegativeGone();
		dialog.setNegative2Text(negativeText);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setNegative2ButtonOnClickListener(onClickListener);
		return dialog;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
			return true;
		}else if(keyCode == KeyEvent.KEYCODE_HOME) { //监控/拦截/屏蔽HOME健
			return false;
		}else {
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * 创建dialog
	 * @param context
	 * @param positiveText
	 * @param negativeText
	 * @param msg
	 * @param onClickListener
	 * @return dialog.
	 */
	public CollapsibleDialog showAllDialog(Context context,
			String positiveText, String negativeText, CharSequence msg,
			View.OnClickListener onClickListener) {
		CollapsibleDialog dialog = new CollapsibleDialog(context);
		dialog.setMessage(msg);
		dialog.setPositiveText(positiveText);
		dialog.setNegativeText(negativeText);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setPositiveButtonOnClickListener(onClickListener);
		return dialog;
	}

	/**
	 * 创建dialog
	 * @param context
	 * @param negativeText
	 * @param msg
	 * @param onClickListener
	 * @return dialog.
	 */
	public static CollapsibleDialog createPromptDialog(Context context,
			 String negativeText,ProgressBar progressBar, CharSequence msg,
			View.OnClickListener onClickListener) {
		CollapsibleDialog dialog = new CollapsibleDialog(context);
		dialog.setMessage(msg);
		dialog.setNegativeText(negativeText);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setPositiveButtonOnClickListener(onClickListener);
		progressBar.setProgress(0);
		return dialog;
	}

}