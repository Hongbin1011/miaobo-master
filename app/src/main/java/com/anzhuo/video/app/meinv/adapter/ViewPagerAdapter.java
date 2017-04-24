package com.anzhuo.video.app.meinv.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private ArrayList<View> imageInfos;

    public ViewPagerAdapter(ArrayList<View> infos) {
        this.imageInfos = infos;
    }

    @Override
    public int getCount() {
        return imageInfos == null ? 0 : imageInfos.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup views, int position, Object object) {
        views.removeView(imageInfos.get(position%imageInfos.size()));
    }

    public Object instantiateItem(ViewGroup views, int position) {
//        views.addView(imageInfos.get(position%imageInfos.size()),0);
//        return imageInfos.get(position%imageInfos.size());
        View view = imageInfos.get(position % imageInfos.size());
        views.addView(view, 0);
        return  view;
    }
}