package com.anzhuo.video.app.ui.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.manager.fuli2.FragmentFactory;
import com.anzhuo.video.app.manager.fuli2.PageSwitcher;
import com.anzhuo.video.app.model.bean.FuliBean;
import com.anzhuo.video.app.utils.ToastUtil;
import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2017/2/22/022.
 */

class RecyclerViewAdapter extends RecyclerView.Adapter {
    FuliBean fuliBeans;
    Activity contexts;
    LayoutInflater mLayoutInflater;

    public RecyclerViewAdapter(Activity context, FuliBean fuliBean) {
        fuliBeans = fuliBean;
        contexts = context;
        mLayoutInflater = LayoutInflater.from(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_found_item_vertical, parent, false);
        return new ItemViewHolders(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final FuliBean.ListBean listBean = fuliBeans.getList().get(position);
        ImageView imageView = ((ItemViewHolders) holder).mImageView;
        Glide.with(contexts)
                .load(listBean.getLogo_url())
                .into(imageView);
        ((ItemViewHolders) holder).mTextView.setText(listBean.getName());

        ItemFuliClick((ItemViewHolders) holder, position, listBean);//点击事件

    }

    /**
     * 点击事件
     *
     * @param holder
     * @param position
     * @param listBean
     */
    private void ItemFuliClick(ItemViewHolders holder, final int position, final FuliBean.ListBean listBean) {
        holder.mLl_gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showCenterToast("点击的是" + listBean.getName() + "===" + position);
                StarToTypeinPage(listBean);
            }
        });

    }

    /**
     * 根据类型进行跳转
     */
    private void StarToTypeinPage(FuliBean.ListBean info) {
        if (TextUtils.equals(info.getUrl_open_type(), "2")) {
            //打开APP
            if ("8".equals(info.getId())) {//美女图片
//                Intent intent = new Intent(mContext, com.movie.beauty.meinv.MainActivity.class);
//                startActivity(intent);
            }
            if ("9".equals(info.getId())) {  //9 电影天堂
//                Intent intent = new Intent(mContext, VideoMainActivity.class);
//                startActivity(intent);
            }
            if ("10".equals(info.getId())) {// 10 = 动啦
//                Intent intent = new Intent(mContext, DongtuMainActivity.class);
//                startActivity(intent);
            }
        } else if (TextUtils.equals(info.getUrl_open_type(), "1")) {
            //用webview加载
//            ActivityMy.skipX5Detail(contexts, info.getName(), info.getAndroid_url());

            //用webview加载
            Bundle bundle = new Bundle();
            bundle.putString("url", info.getAndroid_url());
            PageSwitcher.switchToPage(contexts, FragmentFactory.FRAGMENT_TYPE_X5_WEBVIEW, bundle);

        } else if (TextUtils.equals(info.getUrl_open_type(), "3")) {
            //游戏
            Bundle bundle = new Bundle();
            bundle.putString("url", info.getAndroid_url());
            PageSwitcher.switchToGamePage(contexts, FragmentFactory.FRAGMENT_TYPE_GAME_DETAIL2, bundle);
        }
    }

    @Override
    public int getItemCount() {
        return fuliBeans.getList().size();
    }

    private class ItemViewHolders extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView;
        LinearLayout mLl_gen;

        public ItemViewHolders(View inflate) {
            super(inflate);
            mImageView = (ImageView) inflate.findViewById(R.id.vertical_img);
            mTextView = (TextView) inflate.findViewById(R.id.vertical_text);
//            mLl_gen = (LinearLayout) inflate.findViewById(R.id.ll_gen);
        }
    }
}
