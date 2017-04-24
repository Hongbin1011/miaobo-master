package com.anzhuo.video.app.ui.activity;

import android.os.Bundle;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.ui.base.BaseActivity;
import com.anzhuo.video.app.ui.fragment.CollectFragment;

/**
 * Created by Administrator on 2017/3/8/008.
 */

public class CollectActivity extends BaseActivity {
    @Override
    public void SetTag() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_layout);
        CollectFragment mCollectFragment = CollectFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,mCollectFragment).commit();

    }
}
