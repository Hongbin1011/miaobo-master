package com.anzhuo.video.app.movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by husong on 2017/2/21.
 */

public class GridViewAdapter extends BaseAdapter {
    private List<MoviePlayUrlInfo> mDatas = new ArrayList<MoviePlayUrlInfo>();
    private Context mContext;
    private int mPlayIndex;

    public GridViewAdapter(Context context, List<MoviePlayUrlInfo> data){
        this.mContext = context;
        this.mDatas = data;
    }

    public void setPlayIndex(int index){
        this.mPlayIndex = index;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_item, parent, false);
        TextView textView = (TextView) convertView.findViewById(R.id.title_text);
        textView.setText(mDatas.get(position).getTitle());
        if(mPlayIndex == position){
            textView.setTextColor(mContext.getResources().getColor(R.color.text_selected));
        }else {
            textView.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        return convertView;
    }
}
