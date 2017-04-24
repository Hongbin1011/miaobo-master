package com.anzhuo.video.app.manager.fuli2.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.manager.fuli2.activity.MoreFoundActivity;
import com.anzhuo.video.app.search.entity.BaseFoundInfo;
import com.anzhuo.video.app.search.entity.FoundInfo;
import com.anzhuo.video.app.utils.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by husong on 2017/2/20.
 */

public class FoundBaseAdapter extends RecyclerView.Adapter<FoundBaseAdapter.ViewHolder> {

    private ArrayList<BaseFoundInfo> mDatas;
    private Context mContext;
    private FoundItemVerticalAdapter.OnFoundItemClickListener mListener;
    private final int MAX_SHOW = 8;

    public FoundBaseAdapter(Context context, ArrayList<BaseFoundInfo> datas, FoundItemVerticalAdapter.OnFoundItemClickListener listener){
        this.mContext = context;
        this.mDatas = datas;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FoundBaseAdapter.ViewHolder holder = new FoundBaseAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_found, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setDatas(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle;
        private TextView tvMore;
        private RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.found_title);
            tvMore = (TextView) itemView.findViewById(R.id.found_more);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.found_recyclerview);
        }

        public void setDatas(int position){
            final BaseFoundInfo bfi = mDatas.get(position);
            tvTitle.setText(bfi.getTitle());
            if(TextUtils.equals(bfi.getShowtype(), "0")){
                GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
                recyclerView.setLayoutManager(layoutManager);

                if(bfi.getList().size()>MAX_SHOW) {
                    tvMore.setVisibility(View.VISIBLE);
                    tvMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(mContext, MoreFoundActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("bundleData", bfi);
                            intent.putExtra("foundbundle", bundle);
                            mContext.startActivity(intent);
                        }
                    });

                    List<FoundInfo> templist = new ArrayList<>();
                    for (int i=0; i<MAX_SHOW; i++){
                        FoundInfo fi = bfi.getList().get(i);
                        templist.add(fi);
                    }
                    FoundItemVerticalAdapter adapter = new FoundItemVerticalAdapter(mContext, templist, mListener);
                    recyclerView.setAdapter(adapter);
                }else{
                    tvMore.setVisibility(View.GONE);
                    FoundItemVerticalAdapter adapter = new FoundItemVerticalAdapter(mContext, bfi.getList(), mListener);
                    recyclerView.setAdapter(adapter);
                }
            }else {
                GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.HORIZONTAL, 2, mContext.getResources().getColor(R.color.mzw_line_color)));
                recyclerView.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.VERTICAL, 2, mContext.getResources().getColor(R.color.mzw_line_color)));
                if(bfi.getList().size()>MAX_SHOW) {
                    tvMore.setVisibility(View.VISIBLE);
                    tvMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(mContext, MoreFoundActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("bundleData", bfi);
                            intent.putExtra("foundbundle", bundle);
                            mContext.startActivity(intent);
                        }
                    });

                    List<FoundInfo> templist = new ArrayList<>();
                    for (int i=0; i<MAX_SHOW; i++){
                        FoundInfo fi = bfi.getList().get(i);
                        templist.add(fi);
                    }
                    FoundItemHorizontalAdapter adapter = new FoundItemHorizontalAdapter(mContext, templist, mListener);
                    recyclerView.setAdapter(adapter);
                }else{
                    tvMore.setVisibility(View.GONE);
                    FoundItemHorizontalAdapter adapter = new FoundItemHorizontalAdapter(mContext, bfi.getList(), mListener);
                    recyclerView.setAdapter(adapter);
                }
            }
        }
    }
}
