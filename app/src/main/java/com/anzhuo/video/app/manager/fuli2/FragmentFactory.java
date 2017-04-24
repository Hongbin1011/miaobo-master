package com.anzhuo.video.app.manager.fuli2;

import android.util.SparseArray;

import com.anzhuo.video.app.manager.fuli2.x5.X5WebViewFragment;


/**
 * Created by husong on 2017/2/21.
 */

public class FragmentFactory {

    public static final int FRAGMENT_TYPE_RECOMMEND = 0x01;//推荐
    public static final int FRAGMENT_TYPE_SOFTWARE = 0x02;//软件
    public static final int FRAGMENT_TYPE_GAME = 0x03;//游戏
    public static final int FRAGMENT_TYPE_MANAGER = 0x04; // 我的管理

    //用户中心
    public static final int FRAGMENT_TYPE_DISPLAY_MOVIE = 0x05;
    public static final int FRAGMENT_TYPE_USER_REGISTER = 0x06;//注册界面
    public static final int FRAGMENT_TYPE_USER_SET_PASSWORD = 0x07;//设置密码界面
    public static final int FRAGMENT_TYPE_USER_DATA_UPDATE = 0x08;//用户资料修改界面
    //h5公用fragment
    public static final int FRAGMENT_TYPE_X5_WEBVIEW = 0x100;//x5webView

    public static final int FRAGMENT_TYPE_MEDIA_RECORD = 0x101;//录像
    public static final int FRAGMENT_TYPE_CAPTURE = 0x102;//二维码扫描
    public static final int FRAGMENT_TYPE_ALLAPP = 0x1A;//所有应用
    public static final int FRAGMENT_TYPE_ALLGAME = 0x1B;//所有游戏
    public static final int FRAGMENT_TYPE_GAME_DETAIL = 0x1C;//游戏详情
    public static final int FRAGMENT_TYPE_GAME_DETAIL2 = 0x1d;//游戏详情

    public static final int FRAGMENT_TYPE_SEARCH = 0x2A;//搜索界面
    public static final int FRAGMENT_TYPE_SEARCH2 = 0x2D;//搜索界面
    public static final int FRAGMENT_TYPE_FOUND_DETAIL = 0X2B;//
    public static final int FRAGMENT_TYPE_SEARCH_DETAIL = 0X2C;//

    private SparseArray<BaseFragment> mFragmentCache = new SparseArray<BaseFragment>();

    public BaseFragment getFragment(int type, boolean useCache) {

        BaseFragment fragment = null;

        if (useCache && (fragment = mFragmentCache.get(type)) != null) {
            return fragment;
        }
        switch (type) {
            case FragmentFactory.FRAGMENT_TYPE_DISPLAY_MOVIE:
                //fragment = USATVplayFragment.newInstance();
                break;
            case FragmentFactory.FRAGMENT_TYPE_SOFTWARE:
//                fragment = SoftwareFragment.newInstance();
                break;
            case FragmentFactory.FRAGMENT_TYPE_GAME:
//                fragment = FilmFragment.newInstance();
                break;
            case FragmentFactory.FRAGMENT_TYPE_MANAGER:
//                fragment = TVplayFragment.newInstance();
                break;
//            case FragmentFactory.FRAGMENT_TYPE_USER_LOGIN:
//                fragment = UserLoginFragment.newInstance();
//                break;
//            case FragmentFactory.FRAGMENT_TYPE_USER_REGISTER:
//                fragment = UserRegisterFragment.newInstance();
//                break;
//            case FragmentFactory.FRAGMENT_TYPE_USER_SET_PASSWORD:
//                fragment = SetPasswordFragment.newInstance();
//                break;
            case FragmentFactory.FRAGMENT_TYPE_X5_WEBVIEW:
                fragment = X5WebViewFragment.newInstance();
                break;
//            case FragmentFactory.FRAGMENT_TYPE_MEDIA_RECORD:
//                fragment = MediaRecordFragment.newInstance();
//                break;
//            case FragmentFactory.FRAGMENT_TYPE_CAPTURE:
//                fragment = CaptureFragment.newInstance();
//                break;
//            case FragmentFactory.FRAGMENT_TYPE_ALLAPP:
////                fragment = AllAppFragment.newInstance();
//                break;
//            case FragmentFactory.FRAGMENT_TYPE_ALLGAME:
////                fragment = AllGameFragment.newInstance();
////                fragment = NewAllGameFragment.newInstance();
//                break;
//            case FragmentFactory.FRAGMENT_TYPE_SEARCH:
//                fragment = SearchFragment.newInstance();
//                break;

            case FragmentFactory.FRAGMENT_TYPE_SEARCH2:
                fragment = SearchFragment2.newInstance();
                break;
//
//            case FragmentFactory.FRAGMENT_TYPE_FOUND_DETAIL:
//                fragment = FoundDetailFragment.newInstance();
//                break;
//            case FragmentFactory.FRAGMENT_TYPE_GAME_DETAIL:
//                fragment = GameDetailFragment.newInstance();
//                break;

            case FragmentFactory.FRAGMENT_TYPE_GAME_DETAIL2:
                fragment = GameDetailFragment2.newInstance();
                break;

//            case FragmentFactory.FRAGMENT_TYPE_USER_DATA_UPDATE:
//                fragment = UpdateUserDataFragment.newInstance();
//                break ;
//
//            case FragmentFactory.FRAGMENT_TYPE_SEARCH_DETAIL:
//                fragment = SearchDetailsFragment.newInstance();
//                break;

        }

        if (useCache) {
            mFragmentCache.put(type, fragment);
        }
        return fragment;
    }

    public void removeFragment(int type) {
        mFragmentCache.remove(type);
    }

    public BaseFragment getFragmentFromCache(int type) {
        return mFragmentCache.get(type);
    }

    public void clearCache() {
        mFragmentCache.clear();
    }


}
