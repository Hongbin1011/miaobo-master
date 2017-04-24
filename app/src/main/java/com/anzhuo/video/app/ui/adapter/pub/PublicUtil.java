package com.anzhuo.video.app.ui.adapter.pub;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.anzhuo.video.app.model.bean.CollectListBean;
import com.anzhuo.video.app.model.bean.ContentListBean;
import com.anzhuo.video.app.model.bean.JokeEntity;
import com.anzhuo.video.app.model.bean.VideoInfo;
import com.anzhuo.video.app.utils.DisplayUtil;
import com.anzhuo.video.app.utils.FrescoUtils.FrescoUtil;
import com.anzhuo.video.app.widget.JCVideoPlayerStandardFresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.orhanobut.logger.Logger;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


/**
 * Created by Administrator on 2016/11/1 0001.
 */

public class PublicUtil {
    /**
     * 加载列表头像
     *
     * @param context
     * @param simpleDraweeView
     * @param imgUrl
     */
//    public static void LoadHeadImage(Context context, SimpleDraweeView simpleDraweeView, String imgUrl) {
//        int width = context.getResources().getDimensionPixelOffset(R.dimen.dp_head_icon);
//        FrescoUtil.loadUrl(imgUrl, simpleDraweeView, width, width, null);
//    }

    /**
     * 加载视频
     *
     * @param mContext
     * @param jcVideoPlayerStandard
     * @param jokeEntity
     */
    public static void LoadVideo(Context mContext, JCVideoPlayerStandardFresco jcVideoPlayerStandard, JokeEntity jokeEntity) {
        VideoInfo videoInfo = jokeEntity.getVideo().get(0);
        String VideoImageUrl = videoInfo.getImg_url();
        String VideoUrl = videoInfo.getVideo_url();
        //计算播放器的宽高
        int width = Integer.parseInt(videoInfo.getWidth().trim());
        int height = Integer.parseInt(videoInfo.getHeight().trim());
        int videoImgWidth = DisplayUtil.CountPictureWidth();
        int videoImgHeight = (videoImgWidth * height);
        videoImgHeight = videoImgHeight / width;
        int minHeight = DisplayUtil.dp2px(mContext, 200);
        LinearLayout.LayoutParams layoutParams;
        if (videoImgHeight > minHeight) {//如果视频的高度大于最小高度，就以大的为准
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, videoImgHeight);
        } else {
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, minHeight);
        }
        int marginSize = DisplayUtil.dp2px(mContext, 10);
        layoutParams.setMargins(marginSize, marginSize, marginSize, 0);
        jcVideoPlayerStandard.setLayoutParams(layoutParams);
        Logger.i(" videoImgWidth= " + videoImgWidth + " videoImgHeight= " + videoImgHeight + " width= " + width + " height= " + height);
        //加载视频
        jcVideoPlayerStandard.setUp(VideoUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");//
        //加载视频封面图片
        jcVideoPlayerStandard.thumbImageView.setHierarchy(FrescoUtil.GetHierarchy(false));  //进度条
        BaseControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
//                Logger.i("图片下载完成");
            }
        };
        FrescoUtil.loadVideoImgUrl(VideoImageUrl, jcVideoPlayerStandard.thumbImageView, videoImgWidth, videoImgHeight, controllerListener);

    }

    /**
     * 详情加载视频
     *
     * @param mContext
     * @param jcVideoPlayerStandard
     * @param widths
     * @param heights
     */
    public static void LoadVideos(Context mContext, JCVideoPlayerStandardFresco jcVideoPlayerStandard,
                                  String widths, String heights, String VideoUrl, String VideoImageUrl, boolean isDetail,
                                  ContentListBean contentListBean,String videoDesc) {
//        VideoUrl = "http://static.video.mtdz.xiaohua.com/video/0607cc58d456b97bb882b8d6af26774e.mp4";
        //计算播放器的宽高
//        int width = Integer.parseInt(widths);
//        int height = Integer.parseInt(heights);
//        int videoImgWidth = DisplayUtil.CountPictureWidth();
//
//        int videoImgHeight = (videoImgWidth * height);
//        videoImgHeight = videoImgHeight / width;


        int width = 0;
        int height = 0;
        if (!TextUtils.isEmpty(widths) || !widths.equals("0") && !TextUtils.isEmpty(heights) || !heights.equals("0")) {
            //高宽不为空 类型转换
            width = Integer.parseInt(widths);
            height = Integer.parseInt(heights);
        }
        int videoImgWidth = 0;
        int videoImgHeight = 0;
        if (width != 0 || height != 0) {
            //计算后的宽高
            videoImgWidth = DisplayUtil.CountPictureWidth();//获取屏幕的宽度 - 控件两边的间距
            videoImgHeight = (videoImgWidth * height);
            videoImgHeight = videoImgHeight / width;
        }

        int minHeight = DisplayUtil.dp2px(mContext, 200);
        if (isDetail) {
//            LinearLayout.LayoutParams layoutParams;
//            if (videoImgHeight > minHeight) {//如果视频的高度大于最小高度，就以大的为准
//                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, videoImgHeight);
//            } else {
//                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, minHeight);
//            }
            RelativeLayout.LayoutParams layoutParams;
            if (videoImgHeight > minHeight) {//如果视频的高度大于最小高度，就以大的为准
                layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, videoImgHeight);
            } else {
                layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, minHeight);
            }

            int marginSize = DisplayUtil.dp2px(mContext, 15);
            int marginSizeTop = DisplayUtil.dp2px(mContext, 0);
            layoutParams.setMargins(marginSize, marginSizeTop, marginSize, 0);
            jcVideoPlayerStandard.setLayoutParams(layoutParams);
        } else {
//            LinearLayout.LayoutParams layoutParams;
//            if (videoImgHeight > minHeight) {//如果视频的高度大于最小高度，就以大的为准
//                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, videoImgHeight);
//            } else {
//                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, minHeight);
//            }
            RelativeLayout.LayoutParams layoutParams;
            if (videoImgHeight > minHeight) {//如果视频的高度大于最小高度，就以大的为准
                layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, videoImgHeight);
            } else {
                layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, minHeight);
            }
            int marginSize = DisplayUtil.dp2px(mContext, 15);
            int marginSizeTop = DisplayUtil.dp2px(mContext, 13);
            layoutParams.setMargins(marginSize, marginSizeTop, marginSize, 0);
            jcVideoPlayerStandard.setLayoutParams(layoutParams);
        }

//        JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//        JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

//        int widths2 = Integer.parseInt(contentListBean.getV_width());
//        int heights2 = Integer.parseInt(contentListBean.getV_height());
//        if (widths2 >heights2 ) {
//            Logger.i("cq=============[宽高]===========");
//            JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//            JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//        } else {
//            Logger.i("cq=============[宽2高]===========");
//            JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//            JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//        }

        Logger.i(" videoImgWidth= " + videoImgWidth + " videoImgHeight= " + videoImgHeight + " width= " + width + " height= " + height);
        Logger.i(" VideoUrl= " + VideoUrl);
        //加载视频
        jcVideoPlayerStandard.setUp(VideoUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");//
        jcVideoPlayerStandard.thumbImageView.setHierarchy(FrescoUtil.GetHierarchy(false));  //进度条

        //        JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//        JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        BaseControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
//                Logger.i("图片下载完成");
            }
        };
        //加载视频封面图片
        FrescoUtil.loadVideoImgUrl(VideoImageUrl, jcVideoPlayerStandard.thumbImageView, videoImgWidth, videoImgHeight, controllerListener);

    }

    /**
     * 详情加载视频
     *
     * @param mContext
     * @param jcVideoPlayerStandard
     * @param widths
     * @param heights
     */
    public static void LoadVideos(Context mContext, JCVideoPlayerStandardFresco jcVideoPlayerStandard,
                                  String widths, String heights, String VideoUrl, String VideoImageUrl, boolean isDetail,
                                  CollectListBean contentListBean, String videoDesc) {
//        VideoUrl = "http://static.video.mtdz.xiaohua.com/video/0607cc58d456b97bb882b8d6af26774e.mp4";
        //计算播放器的宽高
//        int width = Integer.parseInt(widths);
//        int height = Integer.parseInt(heights);
//        int videoImgWidth = DisplayUtil.CountPictureWidth();
//
//        int videoImgHeight = (videoImgWidth * height);
//        videoImgHeight = videoImgHeight / width;


        int width = 0;
        int height = 0;
        if (!TextUtils.isEmpty(widths) || !widths.equals("0") && !TextUtils.isEmpty(heights) || !heights.equals("0")) {
            //高宽不为空 类型转换
            width = Integer.parseInt(widths);
            height = Integer.parseInt(heights);
        }
        int videoImgWidth = 0;
        int videoImgHeight = 0;
        if (width != 0 || height != 0) {
            //计算后的宽高
            videoImgWidth = DisplayUtil.CountPictureWidth();//获取屏幕的宽度 - 控件两边的间距
            videoImgHeight = (videoImgWidth * height);
            videoImgHeight = videoImgHeight / width;
        }

        int minHeight = DisplayUtil.dp2px(mContext, 200);
        if (isDetail) {
//            LinearLayout.LayoutParams layoutParams;
//            if (videoImgHeight > minHeight) {//如果视频的高度大于最小高度，就以大的为准
//                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, videoImgHeight);
//            } else {
//                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, minHeight);
//            }
            RelativeLayout.LayoutParams layoutParams;
            if (videoImgHeight > minHeight) {//如果视频的高度大于最小高度，就以大的为准
                layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, videoImgHeight);
            } else {
                layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, minHeight);
            }
            int marginSize = DisplayUtil.dp2px(mContext, 15);
            int marginSizeTop = DisplayUtil.dp2px(mContext, 0);
            layoutParams.setMargins(marginSize, marginSizeTop, marginSize, 0);
            jcVideoPlayerStandard.setLayoutParams(layoutParams);
        } else {
//            LinearLayout.LayoutParams layoutParams;
//            if (videoImgHeight > minHeight) {//如果视频的高度大于最小高度，就以大的为准
//                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, videoImgHeight);
//            } else {
//                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, minHeight);
//            }
            RelativeLayout.LayoutParams layoutParams;
            if (videoImgHeight > minHeight) {//如果视频的高度大于最小高度，就以大的为准
                layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, videoImgHeight);
            } else {
                layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, minHeight);
            }
            int marginSize = DisplayUtil.dp2px(mContext, 15);
            int marginSizeTop = DisplayUtil.dp2px(mContext, 13);
            layoutParams.setMargins(marginSize, marginSizeTop, marginSize, 0);
            jcVideoPlayerStandard.setLayoutParams(layoutParams);
        }

//        JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//        JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

//        int widths2 = Integer.parseInt(contentListBean.getV_width());
//        int heights2 = Integer.parseInt(contentListBean.getV_height());
//        if (widths2 >heights2 ) {
//            Logger.i("cq=============[宽高]===========");
//            JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//            JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//        } else {
//            Logger.i("cq=============[宽2高]===========");
//            JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//            JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//        }

        Logger.i(" videoImgWidth= " + videoImgWidth + " videoImgHeight= " + videoImgHeight + " width= " + width + " height= " + height);
        //加载视频
        jcVideoPlayerStandard.setUp(VideoUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");//
        jcVideoPlayerStandard.thumbImageView.setHierarchy(FrescoUtil.GetHierarchy(false));  //进度条

        //        JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//        JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        BaseControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
//                Logger.i("图片下载完成");
            }
        };
        //加载视频封面图片
        FrescoUtil.loadVideoImgUrl(VideoImageUrl, jcVideoPlayerStandard.thumbImageView, videoImgWidth, videoImgHeight, controllerListener);

    }
}
