package com.anzhuo.video.app.utils.FrescoUtils;

import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * Created by Administrator on 2016/10/27 0027.
 */

public class NewFrescoUtil {

    /**
     * 初始化操作，建议在子线程中进行
     * 添加的依赖：
     * compile 'com.facebook.fresco:fresco:0.10.0+'
     * compile 'com.facebook.fresco:animated-webp:0.10.0'
     * compile 'com.facebook.fresco:animated-gif:0.10.0'
     * @param context
     * @param cacheSizeInM 磁盘缓存的大小，以M为单位
     */
    public static void init(final Context context, int cacheSizeInM) {
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setMaxCacheSize(cacheSizeInM * 1024 * 1024)
                .build();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setDownsampleEnabled(true)//Downsampling，它处理图片的速度比常规的裁剪更快，
                // 并且同时支持PNG，JPG以及WEP格式的图片，非常强大,与ResizeOptions配合使用
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build();
        Fresco.initialize(context, config);

    }
}
