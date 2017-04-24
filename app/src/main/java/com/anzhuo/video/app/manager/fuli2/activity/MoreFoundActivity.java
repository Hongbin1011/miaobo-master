package com.anzhuo.video.app.manager.fuli2.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.manager.fuli2.adapter.FoundItemHorizontalAdapter;
import com.anzhuo.video.app.manager.fuli2.adapter.FoundItemVerticalAdapter;
import com.anzhuo.video.app.manager.fuli2.base.BaseActivity;
import com.anzhuo.video.app.search.entity.BaseFoundInfo;
import com.anzhuo.video.app.search.entity.FoundInfo;
import com.anzhuo.video.app.utils.RecycleViewDivider;


/**
 * Created by husong on 2017/2/20.
 */

public class MoreFoundActivity extends BaseActivity implements FoundItemVerticalAdapter.OnFoundItemClickListener{

    private TextView mTitleText;
    private ImageView mImgBack;
    private RecyclerView recyclerView;

    private BaseFoundInfo mFoundInfo;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_more_found;
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        mTitleText = findView(R.id.head_title);
        mImgBack = findViewAttachOnclick(R.id.main_back);
        recyclerView = (RecyclerView) findViewById(R.id.morefound_listview);

        mTitleText.setText("更多");
        mFoundInfo = getIntent().getBundleExtra("foundbundle").getParcelable("bundleData");

        if(TextUtils.equals(mFoundInfo.getShowtype(), "0")) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
            recyclerView.setLayoutManager(layoutManager);
            FoundItemVerticalAdapter adapter = new FoundItemVerticalAdapter(this, mFoundInfo.getList(), this);
            recyclerView.setAdapter(adapter);
        }else{
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.HORIZONTAL, 2, this.getResources().getColor(R.color.mzw_line_color)));
            recyclerView.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.VERTICAL, 2, this.getResources().getColor(R.color.mzw_line_color)));
            FoundItemHorizontalAdapter adapter = new FoundItemHorizontalAdapter(this, mFoundInfo.getList(), this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_back:
                finish();
                break;
        }
    }


    @Override
    public void OnFoundItemClick(FoundInfo info) {
        if (TextUtils.equals(info.getUrl_open_type(), "2")) {
            //打开APP
            if ("8".equals(info.getId())) {
//                Intent intent = new Intent(this, com.movie.beauty.meinv.MainActivity.class);
//                startActivity(intent);
            }
            if ("9".equals(info.getId())) {  //9 = 秒播
//                Intent intent = new Intent(this, VideoMainActivity.class);
//                startActivity(intent);
            }
            if ("10".equals(info.getId())) {// 10 = 动啦
//                Intent intent = new Intent(this, DongtuMainActivity.class);
//                startActivity(intent);
            }
        } else if (TextUtils.equals(info.getUrl_open_type(), "1")) {
            //用webview加载
//            Bundle bundle = new Bundle();
//            bundle.putString("url", info.getAndroid_url());
//            PageSwitcher.switchToPage(this, FragmentFactory.FRAGMENT_TYPE_X5_WEBVIEW, bundle);
        }else if(TextUtils.equals(info.getUrl_open_type(), "3")){
            //游戏
//            Bundle bundle = new Bundle();
//            bundle.putString("url", info.getAndroid_url());
//            PageSwitcher.switchToGamePage(this, FragmentFactory.FRAGMENT_TYPE_GAME_DETAIL2, bundle);
        }
    }
}