package com.anzhuo.video.app.manager.fuli;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.manager.LogManager;
import com.anzhuo.video.app.manager.fuli2.BaseFragment;
import com.anzhuo.video.app.manager.fuli2.FragmentFactory;
import com.anzhuo.video.app.manager.fuli2.PageSwitcher;
import com.anzhuo.video.app.manager.fuli2.base.BaseActivity;


/**
 * Created by husong on 2016/10/28.
 */

public class GameActivity extends BaseActivity {
    private final static int LAYOUT_CONTAINER = android.R.id.content;
    private FragmentFactory mFragmentFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_sub_layout);
        handleIntent(getIntent());
    }


    public FragmentFactory getFragmentFactory() {
        if (mFragmentFactory == null) {
            mFragmentFactory = new FragmentFactory();
        }
        return mFragmentFactory;
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(LAYOUT_CONTAINER);
    }

//    @Override
//    protected int getLayoutRes() {
//        return R.layout.base_sub_layout;
//    }




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
                LogManager.e(e);
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
            LogManager.d("push fragment %s, cache %s, anim %s", type, useCache, false);
            pushFragment(type, args, LAYOUT_CONTAINER, useCache, false);

        } else {//使用动画
            LogManager.d("push fragment %s, cache %s, anim %s", type, useCache, true);
            pushFragment(type, args, LAYOUT_CONTAINER, useCache, true);
        }
    }

    /**
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


//    public ArrayList<PreOwnedTouchListener> listeners = new ArrayList<PreOwnedTouchListener>();
//
//
//    public void registerTouchListener(PreOwnedTouchListener listener) {
//        listeners.add(listener);
//    }
//
//    public void unRegisterListener(PreOwnedTouchListener listener) {
//        listeners.remove(listener);
//    }
//
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        for (PreOwnedTouchListener listener : listeners) {
//            listener.onTouchEvent(ev);
//        }
//        return super.dispatchTouchEvent(ev);
//    }

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
