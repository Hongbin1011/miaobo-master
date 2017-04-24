package com.anzhuo.video.app.utils;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.widget.CustomProgressBar;
import com.anzhuo.video.app.widget.ImgLoadInterface;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.orhanobut.logger.Logger;

/**
 * 作者：熊晓清 on 2016/5/13 15:54
 * 加载工具
 */
public class FrescoUtil {
    /**
     * 得到上下文
     */
    public static Context getContext() {
        return VideoApplication.getContext();
    }

    /**
     * 得到 --- draweeController
     *
     * @param uri
     * @return
     */
    public static DraweeController GetController(Uri uri) {
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                Logger.i("图片下载完成");
                if (anim != null) {
                    // 其他控制逻辑
                    anim.start();
                }
            }
        };

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setControllerListener(controllerListener)
                // 其他设置（如果有的话）
                .build();
        return controller;
    }

    /**
     * 需要控制动画
     *
     * @param uri
     * @param imgLoadInterface
     * @return
     */
  /*  public static DraweeController GetController(Uri uri, final ImgLoadInterface imgLoadInterface) {
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
//                Logger.i("图片下载完成");
                if (anim != null) {
                    // 其他控制逻辑
                    if (ACacheUtil.IsGifAutoPlay()) {
                        Logger.i("动画是否动" + ACacheUtil.IsGifAutoPlay());
                        anim.start();
                    }
                    imgLoadInterface.Complete(true);
                }
            }
        };

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setControllerListener(controllerListener)
                .setTapToRetryEnabled(true)
                .setAutoPlayAnimations(ACacheUtil.IsGifAutoPlay())
                // 其他设置（如果有的话）
                .build();
        return controller;
    }*/

    /**
     * 长图的时候截取
     *
     * @param uri
     * @param imgLoadInterface
     * @return
     */
    public static DraweeController GetLongController(Uri uri, int width, int height, final ImgLoadInterface imgLoadInterface) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                Logger.i("图片下载完成");
                if (anim != null) {
                    // 其他控制逻辑
                    anim.start();
                    imgLoadInterface.Complete(true);
                }
            }
        };

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setControllerListener(controllerListener)
                .setTapToRetryEnabled(true)
                // 其他设置（如果有的话）
                .build();
        return controller;
    }

    public static DraweeController GetController2(Uri uri) {
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                Logger.i("图片下载完成");
                if (anim != null) {
                    // 其他控制逻辑
//                    anim.start();
//                    imgLoadInterface.Complete(true);
                }
            }
        };

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setControllerListener(controllerListener)
                .setTapToRetryEnabled(true)
                // 其他设置（如果有的话）
                .build();
        return controller;
    }

   /* *//**
     * 主要是设置进度条和加载图片
     *
     * @return
     *//*
    public static GenericDraweeHierarchy GetHierarchy() {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getContext().getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setProgressBarImage(new CustomProgressBar())//设置加载进度条
                .setPlaceholderImage(R.drawable.loading)//加载中
                .setFailureImage(R.drawable.weiguanzhu)//加载失败
                .setRetryImage(R.drawable.weiguanzhu)//加载失败点击重新加载
                .build();

        return hierarchy;
    }
*/
   /* *//**
     * 主要列表设置宽高
     *
     * @return
     *//*
    public static GenericDraweeHierarchy GetListHierarchy() {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getContext().getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                .setProgressBarImage(new CustomProgressBar())//设置加载进度条
                .setPlaceholderImage(R.drawable.loading)//加载中
                .setFailureImage(R.drawable.weiguanzhu)//加载失败
                .setRetryImage(R.drawable.weiguanzhu)//加载失败点击重新加载
                .build();

        return hierarchy;
    }*/

    /**
     * 长图
     *
     * @return
     *//*
    public static GenericDraweeHierarchy isLongHierarchy() {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getContext().getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                .setProgressBarImage(new CustomProgressBar())//设置加载进度条
                .setPlaceholderImage(R.drawable.loading)//加载中
                .setFailureImage(R.drawable.weiguanzhu)//加载失败
                .setRetryImage(R.drawable.weiguanzhu)//加载失败点击重新加载
                .build();

        return hierarchy;
    }*/



    /**
     * PhotoActivity
     *
     * @return
     */
    public static GenericDraweeHierarchy GetHierarchy2() {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getContext().getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                .setProgressBarImage(new CustomProgressBar())//设置加载进度条
                .build();
        return hierarchy;
    }

    public static GenericDraweeHierarchy GetPhoto() {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getContext().getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setProgressBarImage(new CustomProgressBar())//设置加载进度条
                .build();

        return hierarchy;
    }


    /**
     * 暂停网络请求
     * 在listview快速滑动时使用
     */
    public static void pause() {
        Fresco.getImagePipeline().pause();
    }


    /**
     * 恢复网络请求
     * 当滑动停止时使用
     */
    public static void resume() {
        Fresco.getImagePipeline().resume();
    }

//    public


}
