package com.anzhuo.video.app.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;

/**
 * Created by Administrator on 2017/3/13/013.
 * 作用：列表视频画出屏幕外自动停止
 */

public class RecycleViewVideoUtil {
    /**
     * 作用：列表视频画出屏幕外自动停止
     *
     * @param recyclerView
     */

    public static   int firstItemPosition = 0;
    public  static int lastItemPosition = 0;
    public  static int currentItemPosition = 0;
    public  static LinearLayoutManager linearManager;

    public static void addOnChildAttachStateChangeListener(RecyclerView recyclerView) {
        recyclerView.addOnChildAttachStateChangeListener
                (new RecyclerView.OnChildAttachStateChangeListener() {
                     @Override
                     public void onChildViewAttachedToWindow(View view) {//进入
                         Logger.i("=======[这里是走没有 进入]===========");
                     }

                     @Override
                     public void onChildViewDetachedFromWindow(View view) {//离开
                         try {
                             if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                                 JCVideoPlayer videoPlayer = JCVideoPlayerManager.getCurrentJcvd();
                                 if (((ViewGroup) view).indexOfChild(videoPlayer) != -1 &&
                                         (videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING ||
                                                 videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PREPARING)) {
                                     JCVideoPlayer.releaseAllVideos();
                                 }
                             }
                         } catch (Exception e) {
                             e.printStackTrace();
                         }
                     }
                 }
                );

        linearManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (linearManager!=null){
            //查找最后一个可见的item的position
            lastItemPosition = linearManager.findLastVisibleItemPosition();
            //查找第一个可见的item的position
            firstItemPosition =linearManager.findFirstVisibleItemPosition();
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                linearManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearManager==null){
                    return;
                }
                //查找最后一个可见的item的position
                int lastItemPositioned = linearManager.findLastVisibleItemPosition();
                //查找第一个可见的item的position
                int firstItemPositioned =linearManager.findFirstVisibleItemPosition();
//                Logger.e("---------firstItemPosition--------     "+firstItemPosition);
//                Logger.e("---------lastItemPosition--------     "+lastItemPosition);
//                Logger.e("---------firstItemPositioned--------     "+firstItemPositioned);
//                Logger.e("---------lastItemPositioned--------     "+lastItemPositioned);
//                Logger.e("---------currentItemPosition--------     "+currentItemPosition);
                if (dy>0){
                    if (firstItemPositioned>firstItemPosition&&currentItemPosition<firstItemPositioned){
                        try {
                            if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                                JCVideoPlayer videoPlayer = JCVideoPlayerManager.getCurrentJcvd();
                                if ((videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING ||
                                        videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PREPARING)) {
                                    JCVideoPlayer.releaseAllVideos();
                                    Logger.e("------dy>0---停止播放--------     ");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        firstItemPosition = firstItemPositioned;
                    }else {
                        firstItemPosition = firstItemPositioned;
                    }

                }else {
                    if (lastItemPositioned<lastItemPosition&&currentItemPosition>lastItemPositioned){
                        try {
                            if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                                JCVideoPlayer videoPlayer = JCVideoPlayerManager.getCurrentJcvd();
                                if ((videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING ||
                                        videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PREPARING)) {
                                    JCVideoPlayer.releaseAllVideos();
                                    Logger.e("-------dy<0--停止播放--------    ");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        lastItemPosition = lastItemPositioned;
                    }else {
                        lastItemPosition = lastItemPositioned;
                    }


                }


            }
        });


    }


    public static int getCurrentItemPosition() {
        return currentItemPosition;
    }

    public static void setCurrentItemPosition(int currentItemPosition) {
        RecycleViewVideoUtil.currentItemPosition = currentItemPosition;
    }
}
