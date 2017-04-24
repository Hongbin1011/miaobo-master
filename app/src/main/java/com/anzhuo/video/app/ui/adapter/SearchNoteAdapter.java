package com.anzhuo.video.app.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/22/022.
 * 搜索记录
 */

public class SearchNoteAdapter extends RecyclerView.Adapter {
    ArrayList mSearchNoteList = new ArrayList();
    private static final int ITEM_TITLE = 0;
    private static final int ITEM_CENTER = 1;
    private static final int ITEM_FOOT = 2;

    public SearchNoteAdapter(ArrayList searchNoteList) {
        this.mSearchNoteList = searchNoteList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            //最后一个item设置为footerView
            return ITEM_FOOT;
        } else if (position == 0) {
            return ITEM_TITLE;
        } else {
            return ITEM_CENTER;
        }
    }

}

