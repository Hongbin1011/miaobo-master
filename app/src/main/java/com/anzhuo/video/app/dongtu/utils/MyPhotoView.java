package com.anzhuo.video.app.dongtu.utils;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.anzhuo.video.app.dongtu.bean.dongtu.ImageEntity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * created on 2016/11/17 0017.
 * 可以播放大图video的 photoView
 */

public class MyPhotoView extends FrameLayout {
    private Context context;
    //UI
    private ProgressBar progressBar;

    //Data
    private ImageEntity imageEntity;
    private int zoomWidth;//缩放后的宽度
    private int zoomHeight;//缩放后的高度
    private PhotoViewOnClickListener listener;


    public MyPhotoView(@NonNull Context context, ImageEntity imageEntity) {
        this(context, null, imageEntity);
    }

    public MyPhotoView(@NonNull Context context, @Nullable AttributeSet attrs, ImageEntity imageEntity) {
        this(context, null, 0, imageEntity);
    }

    public MyPhotoView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, ImageEntity imageEntity) {
        super(context, attrs, defStyleAttr);
        this.imageEntity = imageEntity;
        this.context = context;
        initView();
    }

    /**
     * 通过计算宽高和屏幕的宽高对比确定使用bigView 还是  smallView
     */
    private void initView() {
        addProgress();
        int width = Integer.parseInt(imageEntity.getBigWidth().trim());
        int height = Integer.parseInt(imageEntity.getBigHeight().trim());
        zoomWidth = DisplayUtil.getScreenWidth(context.getApplicationContext());
        int pictureHeight = (zoomWidth * height);
        zoomHeight = pictureHeight / width;
        int ScreenHeight = DisplayUtil.getScreenHeight(context.getApplicationContext());//屏幕宽度
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        if (height > ScreenHeight) {//大图
//            addBigView(params);
        } else {//小图
            addSmallView(params);
        }
    }

    /**
     * 添加 progress
     */
    private void addProgress() {
        progressBar = new ProgressBar(context);
        LayoutParams param = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        param.gravity = Gravity.CENTER;
        addView(progressBar, param);
    }

    /**
     * 添加大图View
     */
   /* private void addBigView(LayoutParams params) {
        Log.i("xx", "大图");
        BigLongView bigLongView = new BigLongView(context, imageEntity, new LoadFrescoListener() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                Logger.i("大图 onSuccess ");
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFail() {
                Logger.i("大图 onFail ");

            }
        });
        addView(bigLongView);
        bigLongView.setLayoutParams(params);
        bigLongView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick();
                }
            }
        });
    }*/

    /**
     * 添加小图View
     */
    private void addSmallView(LayoutParams params) {
        final PhotoDraweeView smallView = new PhotoDraweeView(context);
        Uri imgUri = Uri.parse(imageEntity.getBigImageUrl());
        //请求图片
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(imgUri)
                .setResizeOptions(new ResizeOptions(zoomWidth, zoomHeight))
                .setRotationOptions(RotationOptions.autoRotate()) //如果图片是侧着,可以自动旋转
                .build();
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setImageRequest(request);
        controller.setAutoPlayAnimations(true);
        controller.setOldController(smallView.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || smallView == null) {
                    return;
                }
                if (imageEntity.getImgType().equals("gif"))
                    removeProgressBar();
                smallView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        smallView.setController(controller.build());
        //请求图片---------------------
        smallView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (listener != null) {
                    listener.onClick();
                }
            }
        });
        addView(smallView);
        smallView.setLayoutParams(params);
    }

    /**
     * 设置点击事件
     * p
     *
     * @param listener
     */
    public void setOnclickListener(PhotoViewOnClickListener listener) {
        this.listener = listener;
    }

    public interface PhotoViewOnClickListener {
        void onClick();
    }

    /**
     * 回收资源、释放内存
     */
    public void setNull() {
        listener = null;
        this.removeAllViews();
    }

    private void removeProgressBar() {
        this.removeView(progressBar);
    }


}
