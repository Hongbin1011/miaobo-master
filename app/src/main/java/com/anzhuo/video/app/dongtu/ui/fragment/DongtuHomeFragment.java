package com.anzhuo.video.app.dongtu.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.dongtu.bean.dongtu.DongtuTitlesBean;
import com.anzhuo.video.app.dongtu.manager.ActivityMy;
import com.anzhuo.video.app.dongtu.ui.base.DongtuBaseFragment;
import com.anzhuo.video.app.dongtu.utils.CustomViewPager;
import com.anzhuo.video.app.dongtu.utils.DatabaseUtils;
import com.anzhuo.video.app.dongtu.utils.EventUtil;
import com.anzhuo.video.app.dongtu.utils.FrescoUtils.FrescoUtil;
import com.anzhuo.video.app.dongtu.widget.ScaleTransitionPagerTitleView;
import com.anzhuo.video.app.utils.ViewUtil;
import com.anzhuo.video.app.utils.XUtilNet;
import com.orhanobut.logger.Logger;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.BezierPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xxq on 2016/9/21.
 * 首页
 */
public class DongtuHomeFragment extends DongtuBaseFragment {


    private ImageView mAddTitle;
    private ImageView dongtuBack;

    @Override
    public void SetTag() {
    }

    TextView tvNoNetTips;//没有网络提示
    CustomViewPager viewpger_news;
    MagicIndicator tab_layout;

    private MyFragmentPagerAdapter adapter;
    private ArrayList<DongtuBaseFragment> fragmentList = new ArrayList<>();
    private MyCommonNavigatorAdapter mMyCommonNavigatorAdapter;// title adapter
    private List<DongtuTitlesBean> mDataTitles = new ArrayList<>();
    int number = 0;


    @Override
    protected int getLayoutRes() {
        return R.layout.dongtu_fragment_home;
    }

    @Override
    public void bindViews() {
        tvNoNetTips = ViewUtil.findView(mRootView, R.id.tv_no_net_tips);
        tab_layout = ViewUtil.findView(mRootView, R.id.tab_layout);
        viewpger_news = ViewUtil.findView(mRootView, R.id.viewpger_news);
        mAddTitle = ViewUtil.findView(mRootView, R.id.addtabtitle);
        dongtuBack = ViewUtil.findView(mRootView,R.id.dongtu_back);
        mAddTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityMy.SkipAddTitle(mActivity);//跳转至添加界面
            }
        });
        dongtuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        EventBus.getDefault().register(this);
      //  ButterKnife.bind(this, mRootView);
        initView();// 初始化控件
    }

    @Override
    public void onLazyLoad() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        initNetView();//初始化网络条
        //给ViewPager设置适配器
        adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
        viewpger_news.setAdapter(adapter);
        viewpger_news.setOffscreenPageLimit(0);
        // 设置ViewPager最大缓存的页面个数(cpu消耗少)
//        viewpger_news.setOffscreenPageLimit(2);
        //tab title
        CommonNavigator commonNavigator = new CommonNavigator(mActivity);
        mMyCommonNavigatorAdapter = new MyCommonNavigatorAdapter(mDataTitles);//
        commonNavigator.setAdapter(mMyCommonNavigatorAdapter);
        tab_layout.setNavigator(commonNavigator);
        ViewPagerHelper.bind(tab_layout, viewpger_news);
        viewpger_news.addOnPageChangeListener(new mOpageChangListener());
        initData();//初始化数据
    }

    /**
     * 初始化数据
     */
    private void initData() {
        try {
            mDataTitles.clear();
            fragmentList.clear();
            //从数据库中查找数据
            mDataTitles.addAll(DatabaseUtils.getAll());
            number = mDataTitles.size();
            // 创建fragment
            for (int i = 0; i < number; i++) {
                DongtuTitlesBean titlesBean = mDataTitles.get(i);
                Logger.i("cq=============[创建fragment]===========" + titlesBean.getCategory());
                DongtuHomeListFragment homeFragments = DongtuHomeListFragment.newInstance(titlesBean.getCategory().toString());
                fragmentList.add(homeFragments);
            }

            Logger.i("cq=============[添加删除之后 fragement]===========" + fragmentList.size());
            if (adapter != null && mMyCommonNavigatorAdapter != null) {
                adapter.notifyDataSetChanged();
                mMyCommonNavigatorAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化网络条
     * 有网隐藏、无网显示
     */
    private void initNetView() {
        if (XUtilNet.isNetConnected()) {
            tvNoNetTips.setVisibility(View.GONE);
        } else {
            tvNoNetTips.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

    }


    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private long baseId = 0;
        ArrayList<DongtuBaseFragment> type;

        public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<DongtuBaseFragment> types) {
            super(fm);
            type = types;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            Logger.i("cq=============[baseId 3]===========" + (baseId + position));
            return baseId + position;
        }

        @Override
        public Fragment getItem(int position) {
            return type.get(position);
        }

        /**
         * 更新fragment的数量之后，在调用notifyDataSetChanged之前，changeId(1) 改变id，改变tag
         *
         * @param n
         */
        public void changeId(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            Logger.i("cq=============[baseId 1]===========" + baseId);
            baseId += getCount() + n; //当前的fragment 数量
            Logger.i("cq=============[baseId 2]===========" + baseId);
        }

        @Override
        public int getCount() {
            return type.size();
        }

    }

    /**
     * 接受网络状态变化
     *
     * @param ListenerNetEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenerNetEvent(EventUtil.ListenerNetEvent ListenerNetEvent) {
        if (ListenerNetEvent.isHasNet) {
            tvNoNetTips.setVisibility(View.GONE);
        } else {
            tvNoNetTips.setVisibility(View.VISIBLE);
        }
    }


    /**
     * tab 的adapter
     */
    class MyCommonNavigatorAdapter extends CommonNavigatorAdapter {

        List<DongtuTitlesBean> mTitle = null;

        public MyCommonNavigatorAdapter(List<DongtuTitlesBean> titles) {
            mTitle = titles;
        }

        @Override
        public int getCount() {
            return mTitle.size() == 0 ? 0 : mTitle.size();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, final int index) {
            Logger.i("cq=============[添加删除之后 fragement viewpager title]===========" + mTitle.get(index).getCategory().toString());

            SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
            simplePagerTitleView.setText(mTitle.get(index).getCategory().toString());
//            simplePagerTitleView.setTextSize();
            simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            simplePagerTitleView.setNormalColor(getResources().getColor(R.color.dongla_whites));
            simplePagerTitleView.setSelectedColor(Color.WHITE);
            simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewpger_news.setCurrentItem(index);
                }
            });
            return simplePagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            BezierPagerIndicator indicator = new BezierPagerIndicator(context);
//            indicator.setColors(Color.parseColor("#ff4a42"), Color.parseColor("#fcde64"), Color.parseColor("#73e8f4"), Color.parseColor("#76b0ff"), Color.parseColor("#c683fe"));
            return null;
        }

    }

    /**
     * 跳转至添加界面
     */
//    @OnClick(R.id.addtabtitle)
//    public void AddTabTitle() {
//        ActivityMy.SkipAddTitle(mActivity);//跳转至添加界面
//    }

    /**
     * 接收事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EnventChangesData(EventUtil.EnventChangesData event) {

        boolean isAdd = event.isAdd;//如果是true 说明是添加 否则删除
        Logger.i("cq=============[接收到数据改变的事件]===========" + isAdd);
        if (isAdd) {
            //如果是添加栏目 只需要拿到最后一条数据 添加进 fragmentlist 集合中
            mDataTitles.clear();
            mDataTitles.addAll(DatabaseUtils.getAll());
            DongtuTitlesBean titlesBean = mDataTitles.get(mDataTitles.size() - 1);
            Logger.i("cq=============[接收到数据改变的事件 最后一条]===========" + titlesBean.toString());
            DongtuHomeListFragment homeFragments = DongtuHomeListFragment.newInstance(titlesBean.getCategory().toString());
            fragmentList.add(homeFragments);
            adapter.notifyDataSetChanged();
            mMyCommonNavigatorAdapter.notifyDataSetChanged();
        } else {
            mDataTitles.clear();
            fragmentList.clear();
            Logger.i("cq=============[创建fragment前]===========" + fragmentList.size());
            //从数据库中查找数据
            mDataTitles.addAll(DatabaseUtils.getAll());
            number = mDataTitles.size();
            //图片
            for (int i = 0; i < number; i++) {
                DongtuTitlesBean titlesBean = mDataTitles.get(i);
                DongtuHomeListFragment homeFragments = DongtuHomeListFragment.newInstance(titlesBean.getCategory().toString());
                fragmentList.add(homeFragments);
            }
            adapter.changeId(1);
            adapter.notifyDataSetChanged();
            mMyCommonNavigatorAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 接收eventbus
     * 有可能这还没有走完 homefragemtn 就已经创建完成 了
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ChangeLocaData(EventUtil.EnventSendStateChangeLocaData event) {
        List<DongtuTitlesBean> all = new ArrayList<>();
        all.addAll(DatabaseUtils.getAll());
        boolean b = all.size() == mDataTitles.size();
        Logger.i("cq=============[接收到首页改变的事件 数据是否相等]===========" + b);
        if (!b) { //说明现在显示的和本地的数据库中不一致 重新刷新
            Logger.i("cq=============[接收到首页改变的事件]===========");
            initData();
        }
    }

    /**
     * 滑动监听
     */
    private class mOpageChangListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    FrescoUtil.resume();//恢复网络 加载
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    FrescoUtil.pause(); //暂停网络 加载
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                    FrescoUtil.resume();//恢复网络 加载
                    break;
            }
        }
    }
}

