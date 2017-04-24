package com.anzhuo.video.app.movie;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.anzhuo.fulishipin.app.R;

import java.util.ArrayList;

/**
 * Created by husong on 2017/2/21.
 */

public class SubActivity extends BaseActivity{
    private final static int LAYOUT_CONTAINER = android.R.id.content;
    private FragmentFactory mFragmentFactory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public FragmentFactory getFragmentFactory(){
        if (mFragmentFactory == null) {
            mFragmentFactory = new FragmentFactory();
        }
        return mFragmentFactory;
    }

    public Fragment getCurrentFragment(){
        return getSupportFragmentManager().findFragmentById(LAYOUT_CONTAINER);
    }

    @Override
    public void onBackPressed() {
        Fragment baseFragment = getCurrentFragment();
        if (baseFragment instanceof BaseFragment) {
            if (((BaseFragment) baseFragment).goBack()) {
                return;
            }
        }
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
        } else {
            try {
                super.onBackPressed();
            } catch (Exception e) {

            }
        }
    }
    public void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        setIntent(intent);

        int type = intent.getIntExtra(PageSwitcher.INTENT_EXTRA_FRAGMENT_TYPE, 0);//intent_extra_fragment_type
        Bundle args = intent.getBundleExtra(PageSwitcher.INTENT_EXTRA_FRAGMENT_ARGS);
        boolean useCache = false;//是否使用缓存
        boolean useAnim = false;//是否使用动画

        if (args != null) {
            useCache = args.getBoolean(PageSwitcher.BUNDLE_FRAGMENT_CACHE);
            useAnim = args.getBoolean(PageSwitcher.BUNDLE_FRAGMENT_ANIM);
        } else {
            args = new Bundle();
            final Bundle extras = intent.getExtras();
            if (extras != null) {
                args.putAll(intent.getExtras());
            }
        }

        if (!useAnim) {
            pushFragment(type, args, LAYOUT_CONTAINER, useCache, false);

        } else {//使用动画
            pushFragment(type, args, LAYOUT_CONTAINER, useCache, true);
        }
    }

    /**
     *
     * @param type
     * @param args
     * @param container
     * @param useCache
     * @param useAnim
     */
    protected void pushFragment(int type, Bundle args, int container, boolean useCache, boolean useAnim) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fT = fragmentManager.beginTransaction();

        if (useAnim) {
            fT.setCustomAnimations(R.anim.open_slide_in, R.anim.open_slide_out, R.anim.close_slide_in, R.anim.close_slide_out);
        }

        Fragment fragment = null;
        if (useCache) {
            fragment = fragmentManager.findFragmentByTag(String.valueOf(type));
            if (fragment == null) {
                fragment = (Fragment) getFragmentFactory().getFragment(type, true);
            }

        } else {
            removeFragment(type);
            fragment = getFragmentFactory().getFragment(type, false);
        }

        if (fragment == fragmentManager.findFragmentById(container)) {
            return;
        }

        if (fragment != null) {
            if (args != null) {
                ((BaseFragment) fragment).setArguments(args);

            }

            fT.replace(container, fragment, String.valueOf(type));
            fT.addToBackStack(String.valueOf(type));
        }

        fT.commitAllowingStateLoss();
    }

    /**
     * @param type
     */
    public void removeFragment(int type) {
        getFragmentFactory().removeFragment(type);
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment f = getCurrentFragment();
        f.onActivityResult(requestCode, resultCode, data);
    }


    public ArrayList<PreOwnedTouchListener> listeners = new ArrayList<PreOwnedTouchListener>();


    public void registerTouchListener(PreOwnedTouchListener listener) {
        listeners.add(listener);
    }

    public void unRegisterListener(PreOwnedTouchListener listener) {
        listeners.remove(listener);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (PreOwnedTouchListener listener : listeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            X5WebViewFragment fragment = (X5WebViewFragment) mFragmentFactory.getFragment(FragmentFactory.FRAGMENT_TYPE_X5_WEBVIEW, true);
            fragment.doBackDown();
        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            X5WebViewFragment fragment = (X5WebViewFragment) mFragmentFactory.getFragment(FragmentFactory.FRAGMENT_TYPE_X5_WEBVIEW, true);
            fragment.doVolumeDown();
        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            X5WebViewFragment fragment = (X5WebViewFragment) mFragmentFactory.getFragment(FragmentFactory.FRAGMENT_TYPE_X5_WEBVIEW, true);
            fragment.doVolumeUp();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
//            X5WebViewFragment fragment = (X5WebViewFragment) mFragmentFactory.getFragment(FragmentFactory.FRAGMENT_TYPE_X5_WEBVIEW, true);
//            fragment.doKeyEnter();
//            return true;
//        }
        return super.dispatchKeyEvent(event);
    }
}
