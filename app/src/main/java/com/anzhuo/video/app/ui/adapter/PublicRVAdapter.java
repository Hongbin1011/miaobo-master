package com.anzhuo.video.app.ui.adapter;

import android.content.Context;

import com.anzhuo.video.app.model.NotifyPositionInterface;
import com.anzhuo.video.app.model.ReceiveState;
import com.anzhuo.video.app.model.bean.JokeEntity;
import com.anzhuo.video.app.ui.adapter.pub.RecordPositionAdapter;
import com.anzhuo.video.app.ui.adapter.pub.VideoDelagate;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * creat on 2016/8/3 14:51
 * 公共的Adapter(视频.段子.图片)
 */
public class PublicRVAdapter extends MultiItemTypeAdapter<JokeEntity> {
    private List<JokeEntity> datas;

    public PublicRVAdapter(Context context, List<JokeEntity> datas, final ReceiveState state, String parentString, String childString, String findUserID) {
        super(context, datas);
        this.datas = datas;
        addItemViewDelegate(new VideoDelagate(context, parentString, childString, findUserID, new NotifyPositionInterface() {
            @Override
            public void notify(int position) {
                Logger.i("传到PublicRVAdapter=" + position);

            }

            @Override
            public void DeleteNotify(int position) {
//                deleteNotify(position);
//                UpdateDataUtils.updateData(JokeApplication.getContext());
            }
        }));
        addItemViewDelegate(new RecordPositionAdapter(context, parentString, childString, findUserID, new NotifyPositionInterface() {
            @Override
            public void notify(int position) {
                Logger.i("传到PublicRVAdapter=" + position);
            }

            @Override
            public void DeleteNotify(int position) {
            }
        }));

    }

}
