package com.anzhuo.video.app.ui.adapter.pub;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.model.NotifyPositionInterface;
import com.anzhuo.video.app.model.bean.JokeEntity;
import com.anzhuo.video.app.utils.NoDoubleClickListener;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;



/**
 * created on 2016/11/23 0023.
 * 记录位置：上次看到这里，点击刷新
 */

public class RecordPositionAdapter implements ItemViewDelegate<JokeEntity> {
    private Context mContext;
    private NotifyPositionInterface notifyPositionInterface;
    private String parentString;
    private String childString;
    private String findUserID;


    public RecordPositionAdapter(Context context, String parentString, String childString, String findUserID, NotifyPositionInterface notifyPositionInterface) {
        this.mContext = context;
        this.notifyPositionInterface = notifyPositionInterface;
        this.parentString = parentString;
        this.childString = childString;
        this.findUserID = findUserID;
    }


    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_joke_record_position;
    }

    /**
     * 设置什么时候显示
     *
     * @param item
     * @param position
     * @return
     */
    @Override
    public boolean isForViewType(@NonNull JokeEntity item, int position) {
        return item.getType().toString().trim().equals("4");
    }

    /**
     * 绑定控件
     *
     * @param holder
     * @param jokeEntity
     * @param position
     */
    @Override
    public void convert(ViewHolder holder, final JokeEntity jokeEntity, int position) {
        TextView textView = holder.getView(R.id.tv_item_record_position);
        textView.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                Logger.i("点击记录位置刷新");
            }
        });
    }

}