package com.anzhuo.video.app.ui.adapter.pub;

import android.content.Context;
import android.support.annotation.NonNull;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.model.NotifyPositionInterface;
import com.anzhuo.video.app.model.bean.JokeEntity;
import com.anzhuo.video.app.widget.JCVideoPlayerStandardFresco;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import static com.anzhuo.video.app.ui.adapter.pub.PublicSet.initPublicView;


/**
 * creat on 2016/8/3 15:53
 * 视频
 */
public class VideoDelagate implements ItemViewDelegate<JokeEntity> {
    private Context mContext;
    private NotifyPositionInterface notifyPositionInterface;
    private String parentString;
    private String childString;
    private String findUserID;


    public VideoDelagate(Context context, String parentString, String childString, String findUserID, NotifyPositionInterface notifyPositionInterface) {
        this.mContext = context;
        this.notifyPositionInterface = notifyPositionInterface;
        this.parentString = parentString;
        this.childString = childString;
        this.findUserID = findUserID;
    }

    /**
     * 设置布局文件
     *
     * @return
     */
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_joke_video;
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
        return true;
    }


    /**
     * 绑定控件
     *
     * @param holder
     * @param jokeEntity
     * @param position
     */
    @Override
    public void convert(ViewHolder holder, JokeEntity jokeEntity, int position) {
        initPublicView(mContext, holder, jokeEntity, parentString, childString, findUserID, position, new NotifyPositionInterface() {
            @Override
            public void notify(int position) {
                Logger.i("position=" + position);
                notifyPositionInterface.notify(position);
            }

            @Override
            public void DeleteNotify(int position) {
                notifyPositionInterface.DeleteNotify(position);
            }
        });
        try {
            //以下是视频部分
            JCVideoPlayerStandardFresco jcVideoPlayerStandard = holder.getView(R.id.custom_videoplayer_standard);
            jcVideoPlayerStandard.setTag(jokeEntity.getJokeID());
            if (!jcVideoPlayerStandard.getTag().equals(jokeEntity.getJokeID())) {
                return;
            }
            PublicUtil.LoadVideo(mContext, jcVideoPlayerStandard, jokeEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
