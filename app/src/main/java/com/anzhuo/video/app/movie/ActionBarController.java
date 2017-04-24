package com.anzhuo.video.app.movie;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.utils.ViewUtil;

/**
 * Created by husong on 2017/2/21.
 */

public class ActionBarController {
    private ActionBar mActionBar;
    private Context mContext;
    private Activity mActivity;
    private View mAlphaView;
    private View.OnClickListener mOnClickListener;

    public ActionBarController(Activity activity, View.OnClickListener onClickListener) {
        mActionBar = (ActionBar) activity.findViewById(R.id.abBase);
        mContext = activity;
        mActivity = activity;
        mOnClickListener = onClickListener;
        init();
    }

    public ActionBarController(View view, View.OnClickListener onClickListener) {
        mActionBar = (ActionBar) view.findViewById(R.id.abBase);
        mContext = view.getContext();
        mOnClickListener = onClickListener;
        init();

    }

    private void init() {

//		ViewUtil.findViewAttachOnclick(mActionBar, R.id.abBack, mOnClickListener);
        ViewUtil.findViewAttachOnclick(mActionBar, R.id.abBackLl, mOnClickListener);
        mAlphaView = ViewUtil.findView(mActionBar, R.id.viAlpha);
    }

    /***
     * 添加带图标的item
     *
     * @param iconId
     * @param viewId
     * @return
     */
    public ActionBarController addIconItem(int iconId, int viewId) {
        ImageView imageView = new ImageView(mContext);
        ViewUtil.setViewBackground(imageView, R.drawable.common_view_click_bg);
        imageView.setLayoutParams(createMenuItemLayoutParams());
        imageView.setOnClickListener(mOnClickListener);
        imageView.setId(viewId);
        imageView.setImageResource(iconId);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        mActionBar.getRightLayout().addView(imageView);
        return this;
    }

    /**
     * 添加带文字的item
     *
     * @param stringId @StringRes
     * @param viewId
     * @return
     */
    public ActionBarController addTextItem(@StringRes int stringId, int viewId) {
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        final Resources resources = mContext.getResources();
        textView.setLayoutParams(createMenuItemLayoutParams());
        textView.setPadding(DisplayUtil.dip2px(mContext, 10),0,DisplayUtil.dip2px(mContext,10),0);
        textView.setTextSize(17);
        textView.setTextColor(resources.getColor(android.R.color.white));
        textView.setId(viewId);
        textView.setText(stringId);
        textView.setOnClickListener(mOnClickListener);
        ViewUtil.setViewBackground(textView, R.drawable.common_view_click_bg);
        mActionBar.getRightLayout().addView(textView);
        return this;
    }

    /**
     * 修改带文字item的文字
     *
     * @param stringId
     * @param viewId
     */
    public void changeTextItemText(@StringRes int stringId, int viewId) {
        View rightView = mActionBar.getRightLayout().getChildAt(0);
        if (rightView == null) {
            return;
        }
        if (rightView instanceof TextView && rightView.getId() == viewId) {
            ((TextView) rightView).setText(stringId);
        }
    }

    public String getRightItemText(int viewId) {
        String res = "";
        View rightView = mActionBar.getRightLayout().getChildAt(0);
        if (rightView == null) {
            res = "";
        }
        if (rightView instanceof TextView && rightView.getId() == viewId) {
            res = ((TextView) rightView).getText().toString();
        }
        return res;
    }

    /**
     * 设置右边按钮的显示和隐藏
     *
     * @param isVisible
     */
    public void setRightViewVisible(boolean isVisible) {
        View rightView = mActionBar.getRightLayout().getChildAt(0);
        if (rightView == null) {
            return;
        }
        if (isVisible) {
            rightView.setVisibility(View.VISIBLE);
        } else {
            rightView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题
     *
     * @param stringId
     * @return
     */
    public ActionBarController setBaseTitle(@StringRes int stringId) {
        mActionBar.setTitle(stringId);
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public ActionBarController setBaseTitle(String title) {
        mActionBar.setTitle(title);
        return this;
    }

    /**
     * 设置中心标题
     *
     * @param stringId
     * @return
     */
    public ActionBarController setCenterTitle(@StringRes int stringId) {
        mActionBar.setCenterTitle(stringId);
        return this;
    }

    /**
     * 设置中心标题
     *
     * @param title
     * @return
     */
    public ActionBarController setCenterTitle(String title) {
        mActionBar.setCenterTitle(title);
        return this;
    }
    /**
     * 设置背景透明度
     * @param alpha
     * @return
     */
    public ActionBarController setAlpha(float alpha) {
        mAlphaView.setAlpha(alpha);
        return this;
    }

    public ActionBarController setActivity(Activity activity) {
        mActivity = activity;
        return this;
    }

    public ActionBar getActionBar() {
        return mActionBar;
    }

    private LinearLayout.LayoutParams createMenuItemLayoutParams() {
        int width = (int) mContext.getResources().getDimension(R.dimen.action_bar_width);
        int height = (int) mContext.getResources().getDimension(R.dimen.action_bar_height);
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }
}
