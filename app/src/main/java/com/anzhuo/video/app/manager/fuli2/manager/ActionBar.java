package com.anzhuo.video.app.manager.fuli2.manager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;


/**
 * Created by husong on 2017/2/21.
 */

public class ActionBar extends LinearLayout {
    private LinearLayout mRightLayout;
    private LinearLayout mNavigationContainer;
    private TextView mTitle;
    private TextView mCenterTitle;

    public ActionBar(final Context context) {
        this(context, null);
    }

    public ActionBar(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setupViews(context);
    }
    private void setupViews(Context context) {
        final LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.action_bar_view, this);
        mRightLayout = (LinearLayout) findViewById(R.id.llRightLayout);
        mNavigationContainer = (LinearLayout) findViewById(R.id.abBackLl);
        mTitle = (TextView) findViewById(R.id.abBack);
        mCenterTitle = (TextView) findViewById(R.id.tv_center_title);
    }

    public void setTitle(int stringId){
        mTitle.setText(stringId);
    }

    public void setTitle(String string){
        mTitle.setText(string);
    }
    public void setCenterTitle(int stringId){
        mCenterTitle.setText(stringId);
    }

    public void setCenterTitle(String string){
        mCenterTitle.setText(string);
    }

    public LinearLayout getRightLayout() {
        return mRightLayout;
    }

    public void setNavigationShow(boolean isShow){
        mNavigationContainer.setVisibility(isShow? View.VISIBLE:View.GONE);
    }

}
