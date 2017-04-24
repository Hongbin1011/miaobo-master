package com.anzhuo.video.app.movie;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.anzhuo.fulishipin.app.R;

import java.util.HashMap;


/**
 * Created by husong on 2017/2/21.
 */

public class HomeLayout extends LinearLayout implements View.OnClickListener{

    private int mTabs[] = {
            R.id.btnMainCommon, R.id.btnMainGame, R.id.btnMainFind, R.id.btnMainManager
    };
    private int mLastId = -1;
    private FragmentHelper mFragmentHelper;
    private int position = 0;
    public HomeLayout(Context context) {
        super(context);
    }

    public HomeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        createFragments();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.home_view, this);
        for (int id : mTabs) {
            findViewById(id).setOnClickListener(this);
        }

    }

    private void createFragments() {
        FragmentManager fragmentManager = ((BaseActivity) getContext()).getSupportFragmentManager();
        if (mFragmentHelper == null){
            mFragmentHelper = new FragmentHelper(fragmentManager, R.id.baseFragmentContainer);
            mFragmentHelper.addFragmentItem(new FragmentHelper.OperationInfo(getContext(), mTabs[0], RecommendFragment.class));
            mFragmentHelper.addFragmentItem(new FragmentHelper.OperationInfo(getContext(), mTabs[1], FilmFragment.class));
            mFragmentHelper.addFragmentItem(new FragmentHelper.OperationInfo(getContext(), mTabs[2], TVplayFragment.class));
            mFragmentHelper.addFragmentItem(new FragmentHelper.OperationInfo(getContext(), mTabs[3], USATVplayFragment.class));
        }

        findViewById(mTabs[0]).performClick();//代码模拟手动点击第一个按钮，使当前显示的是第一个fragment
//        mFragmentHelper.show(mTabs[1], false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switchToPage(id);
        umentmark(v.getId());
    }

    private void umentmark(int id) {
        HashMap<String, String> map = new HashMap<String, String>();
        switch (id) {
            case R.id.btnMainCommon:
                map.put(getContext().getString(R.string.main_bottom), getContext().getString(R.string.main_nav_text_common));
                break;
            case R.id.btnMainGame:
                map.put(getContext().getString(R.string.main_bottom), getContext().getString(R.string.main_nav_text_game));
                break;
            case R.id.btnMainFind:
                map.put(getContext().getString(R.string.main_bottom), getContext().getString(R.string.main_nav_text_find));
                break;
            case R.id.btnMainManager:
                map.put(getContext().getString(R.string.main_bottom), getContext().getString(R.string.main_nav_text_found));
                break;
        }
//        MobclickAgent.onEvent(getContext(), UmengMarkConstants.MAIN_BOTTOM, map);//remark:首页底部点击
    }

    private void switchToPage(@IdRes int viewId) {
//        for (int j = 0; j < mTabs.length; j++) {
//            if (mTabs[j] == viewId) {
//                position = j;
//            } else {
//                position = 0;
//            }
//        }
        selectTabById(viewId);
        mFragmentHelper.show(viewId, false);
    }


    public void selectTabById(@IdRes int tabId) {
        if (tabId == 0) {
            tabId = mTabs[0];
//            position = 0;
        }

        findViewById(tabId).setSelected(true);//选中当前对应id的View
        if (mLastId != -1 && mLastId != tabId) {
            findViewById(mLastId).setSelected(false);//mLastId对应的View的状态为不选中
        }
        mLastId = tabId;//设置mLastId为当前选中的View的id
    }

//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//        SavedState savedState = new SavedState(superState);
//        savedState.currentPosition = position;
//        return savedState;
//    }

//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        SavedState savedState = (SavedState) state;
//        super.onRestoreInstanceState(savedState.getSuperState());
//        position = savedState.currentPosition;
//        requestLayout();
//    }


    /**
     * 该类用于保存当前显示的Fragment的position
     */
    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
