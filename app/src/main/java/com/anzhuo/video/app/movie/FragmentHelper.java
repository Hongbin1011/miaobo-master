package com.anzhuo.video.app.movie;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;

/**
 * Created by husong on 2017/2/21.
 */

public class FragmentHelper {
    private FragmentManager mFragmentManager;
    private HashMap<String, OperationInfo> mFragmentItems = new HashMap<String, OperationInfo>();
    private OperationInfo mShowingInfo;
    private int mFrameRes;
    private int [] animRes = new int[2];

    public static class OperationInfo {
        protected Context context;
        protected String tag;
        protected Class<?> cls;
        protected Bundle args;
        protected Fragment fragment;

        public OperationInfo(Context context, String tag, Class<?> cls) {
            this(context, tag, cls, null);
        }

        public OperationInfo(Context context, String tag, Class<?> cls, Bundle args) {
            this.context = context;
            this.tag = tag;
            this.cls = cls;
            this.args = args;
        }

        public OperationInfo(Context context, int viewId, Class<?> cls, Bundle args) {
            this.context = context;
            this.tag = String.valueOf(viewId);
            this.cls = cls;
            this.args = args;
        }

        public OperationInfo(Context context, int viewId, Class<?> cls){
            this(context, viewId, cls, null);
        }

        public String getTag() {
            return tag;
        }
    }

    public FragmentHelper(FragmentManager fragmentManager, int frameRes) {
        this.mFragmentManager = fragmentManager;
        this.mFrameRes = frameRes;
        mFragmentItems.clear();

        animRes[0] = android.R.anim.fade_in;
        animRes[1] = android.R.anim.fade_out;
    }

    /**
     * Add fragment info for map.
     */
    public void addFragmentItem(OperationInfo info) {
        mFragmentItems.put(info.getTag(), info);
    }

    public OperationInfo getFragmentItem(OperationInfo info){
        return mFragmentItems.get(info.getTag());
    }

    /**
     * Show fragment by tag.
     */
    public Fragment show(String tag, boolean isAnimate) {
        return show(mFragmentItems.get(tag), isAnimate);
    }

    public Fragment show(int viewId, boolean isAnimate) {
        return show(mFragmentItems.get(String.valueOf(viewId)), isAnimate);
    }
    /**
     * Show fragment by tag and set bundle.
     */
    public Fragment show(String tag, Bundle args, boolean isAnimate) {
        OperationInfo info = mFragmentItems.get(tag);
        info.args = args;
        return show(info, isAnimate);
    }

    /**
     * Judge the tag's fragment is showing.
     */
    public boolean isShowing(String tag) {
        return mShowingInfo.tag.equals(tag);
    }

    public void setCustomAnimations(int enterAnimRes, int exitAnimRes){
        animRes[0] = enterAnimRes;
        animRes[1] = exitAnimRes;
    }

    /**
     * Show fragment by info.
     */
    public Fragment show(OperationInfo info, boolean isAnimate) {
        final FragmentTransaction trans = mFragmentManager.beginTransaction().disallowAddToBackStack();

        if(isAnimate){
            trans.setCustomAnimations(animRes[0], animRes[1]);
        }

        if (mShowingInfo == info) {
            // Fragment is showing, don't try again
        } else {
            if (mShowingInfo != null && mShowingInfo.fragment != null) {
                trans.hide(mShowingInfo.fragment);
            }
            mShowingInfo = info;
            if (mShowingInfo != null) {
                if (mShowingInfo.fragment == null) {
                    mShowingInfo.fragment = Fragment.instantiate(mShowingInfo.context,mShowingInfo.cls.getName(), mShowingInfo.args);
                    if (info.args != null)
                        mShowingInfo.fragment.setArguments(info.args);
                    trans.add(mFrameRes, mShowingInfo.fragment, mShowingInfo.tag);
                } else {
                    trans.show(mShowingInfo.fragment);
                }
            }
        }

        trans.commitAllowingStateLoss();
        if(mShowingInfo == null)
            return null;
        else
            return mShowingInfo.fragment;

    }

    public void setFragmentManager(FragmentManager fragmentManager){
        mFragmentManager = fragmentManager;
    }
}
