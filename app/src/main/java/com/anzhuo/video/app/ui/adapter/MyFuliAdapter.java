package com.anzhuo.video.app.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.model.bean.FuliBean;
import com.anzhuo.video.app.utils.DisplayUtil;
import com.anzhuo.video.app.utils.ViewUtil;
import com.anzhuo.video.app.widget.divider.GridSpacingItemDecoration;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/22/022.
 */

public class MyFuliAdapter extends RecyclerView.Adapter {
    LayoutInflater mLayoutInflater;
    Activity context;
    List<FuliBean> mList = new ArrayList();

    public MyFuliAdapter(Activity context, List arrayList) {
        mList = arrayList;
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_found, parent, false);
        return new ItemViewHolders(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mList.size() == 0) {
            return;
        }

        FuliBean fuliBean = mList.get(position);
        //具体的布局 adapter
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(context, fuliBean);
        GridLayoutManager layout = new GridLayoutManager(context, 4) {
            @Override
            public boolean canScrollVertically() { //禁止-recyclerview-滑动事件
                return false;
            }
        };
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        ((ItemViewHolders) holder).mFound_recyclerview.setLayoutManager(layout);
        ((ItemViewHolders) holder).mFound_recyclerview.setAdapter(adapter);
        //  initData(holder, position);//初始化数据
        ((ItemViewHolders) holder).mFound_title.setText(fuliBean.getTitle());
        //分割线
        int line2 = DisplayUtil.px2dp(context, 55);
        int size = mList.size();
        Logger.i("=======[aaa]===========" + size);
        ((ItemViewHolders) holder).mFound_recyclerview.addItemDecoration(new GridSpacingItemDecoration(40, line2, true));
    }

    @Override
    public int getItemCount() {
        Logger.i("cq=============[大小为多少]===========" + mList.size());
        return mList.size();
    }

    private class ItemViewHolders extends RecyclerView.ViewHolder {

        private final TextView mFound_title;
        private final RecyclerView mFound_recyclerview;

        public ItemViewHolders(View inflate) {
            super(inflate);
            mFound_title = ViewUtil.findView(inflate, R.id.found_title);
            mFound_recyclerview = ViewUtil.findView(inflate, R.id.found_recyclerview);
        }
    }
}
