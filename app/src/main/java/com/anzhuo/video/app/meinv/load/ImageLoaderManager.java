package com.anzhuo.video.app.meinv.load;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.search.utils.BitmapUtil;
import com.anzhuo.video.app.search.utils.ToastUtil;
import com.anzhuo.video.app.utils.ViewUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Random;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * Created with Android Studio.
 * Authour：Eric_chan
 * Date：2016/6/23 16:07
 * Des：
 */
public class ImageLoaderManager {

    private static GlideBuilder glideBuilder;
    private static int defaultCircle = 20;

    public static void init(Context context) {
        glideBuilder = new GlideBuilder(context);
    }

    /**
     * 设置在内置存储路径缓存目录文件，路径为Android/data/cache/dir
     *
     * @param dir
     * @param size
     * @param context
     */
    private static void setInternalCacheDiskCache(String dir, int size, Context context) {
        glideBuilder.setDiskCache(new InternalCacheDiskCacheFactory(context, dir, size));
    }

    /**
     * 设置在外置存储路径缓存目录文件
     *
     * @param dir
     * @param size
     * @param context
     */
    private static void setExternalCacheDiskCache(String dir, int size, Context context) {
        glideBuilder.setDiskCache(new ExternalCacheDiskCacheFactory(context, dir, size));
    }


    /**
     * 传入String加载图片
     *
     * @param context
     * @param imageView
     * @param url
     * @param defaultBitmmap
     */
    public static void displayImageByUrl(Context context, ImageView imageView, String url, Bitmap defaultBitmmap) {
        Drawable defaultDrawable = BitmapUtil.bitmap2Drawable(defaultBitmmap);
        displayImageByUrl(context, imageView, url, defaultDrawable);

    }

    /**
     * 传入String加载图片
     *
     * @param context
     * @param imageView
     * @param url
     * @param defaultDrawable
     */
    public static void displayImageByUrl(Context context, ImageView imageView, String url, Drawable defaultDrawable) {
        if (url == null) {
            return;
        }
        if (url.equals("")) {
            return;
        }
        Glide.with(context).load(url).error(defaultDrawable).placeholder(defaultDrawable).fallback(defaultDrawable).into(imageView);

    }


    private static int[] defultImage = {R.drawable.five, R.drawable.four, R.drawable.one, R.drawable.sex, R.drawable.sex_57, R.drawable.three, R.drawable.two};

    /**
     * 传入String加载图片
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void displayImageByUrl(Context context, final ImageView imageView, String url) {
        Random random = new Random();
        int defultPosition = random.nextInt(7);
        displayImageByUrl(context, imageView, url, defultImage[defultPosition]);
    }

    /**
     * 传入String加载图片
     *
     * @param context
     * @param imageView
     * @param url
     * @param drawableId
     */
    public static void displayImageByUrl(Context context, ImageView imageView, String url, int drawableId) {
        Glide.with(context)
                .load(url)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .thumbnail(1f)
                .crossFade()//使用一个淡入淡出动画,默认时间是300毫秒。可以设置时间
                .error(drawableId)
                .placeholder(drawableId)
                .into(imageView);
    }


    /**
     * 传入String加载图片(原始尺寸，原始大小)
     *
     * @param context
     * @param imageView
     * @param url
     * @param drawableId
     */
    public static void displayImageByUrlDetail(Context context, ImageView imageView, String url, int drawableId) {
        Glide.with(context)
                .load(url)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .thumbnail(1f)
                .crossFade()//使用一个淡入淡出动画,默认时间是300毫秒。可以设置时间
                .error(drawableId)
                .placeholder(drawableId)
                .into(imageView);
    }

    /**
     * 传入String加载图片
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void displayImageByUrl(final Context context, ImageView imageView,
                                         final CircleProgressBar pb, final RelativeLayout layout,
                                         String url) {
//                .dontAnimate()
//                .skipMemoryCache(true)//不进行内存缓存   忽略sk卡缓存每次都去加载
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//磁盘高速缓存策略
        Random random = new Random();
        int defultPosition = random.nextInt(7);
        Glide.with(context)
                .load(url)
                .thumbnail(1f)
                .crossFade()//使用一个淡入淡出动画,默认时间是300毫秒。可以设置时间
                .error(defultImage[defultPosition])
                .into(new GlideDrawableImageViewTarget(imageView) {
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        ViewUtil.gone(pb);
                        ViewUtil.visible(layout);
                        ToastUtil.toastLong(context, "该图片加载失败，请重新进入。");
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        ViewUtil.visible(pb);
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        ViewUtil.gone(pb);
                    }
                });
    }


    /**
     * 圆角图片加载
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void displayRoundImageByUrl(Context context, ImageView imageView, String url) {
        displayRoundImageByUrl(context, imageView, url, R.mipmap.icon_launcher);
    }

    /**
     * 圆角图片加载
     *
     * @param context
     * @param imageView
     * @param url
     * @param defaultIcon
     */
    public static void displayRoundImageByUrl(Context context, ImageView imageView, String url, int defaultIcon) {
        displayRoundImageByUrl(context, imageView, url, defaultIcon, defaultCircle);
    }

    /**
     * 圆角图片加载
     *
     * @param context
     * @param imageView
     * @param url
     * @param drawableId
     * @param radius
     */
    public static void displayRoundImageByUrl(Context context, ImageView imageView, String url, int drawableId, int radius) {
        if (radius == 0) {
            radius = defaultCircle;
        }
        if (imageView instanceof RoundedImageView) {
            Glide.with(context).load(url).bitmapTransform(new RoundedCornersTransformation(Glide.get(context).getBitmapPool(), radius, 0)).dontAnimate().error(drawableId).placeholder(drawableId).into(imageView);
        } else {
            if (!TextUtils.isEmpty(url) && url.endsWith("gif")) {
                Glide.with(context).load(url).asBitmap().error(drawableId).placeholder(drawableId).into(imageView);
            } else {
                Glide.with(context).load(url).bitmapTransform(new RoundedCornersTransformation(Glide.get(context).getBitmapPool(), radius, 0)).crossFade().error(drawableId).placeholder(drawableId).into(imageView);
            }
        }
    }


    public static void displayRoundImageByResource(Context context, ImageView imageView, int resourceId, int drawableId, int radius) {

        if (radius == 0) {
            radius = defaultCircle;
        }
        Glide.with(context).load(resourceId).bitmapTransform(new RoundedCornersTransformation(Glide.get(context).getBitmapPool(), radius, 0)).crossFade().error(drawableId).placeholder(drawableId).into(imageView);
    }

    public static void displayCircleImageByUrl(Context context, ImageView imageView, String url, int drawableId) {

        if (imageView instanceof RoundedImageView) {
            Glide.with(context).load(url).bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool())).dontAnimate().error(drawableId).placeholder(drawableId).into(imageView);
        } else {
            Glide.with(context).load(url).bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool())).crossFade().error(drawableId).placeholder(drawableId).into(imageView);
        }
    }

    /**
     * 传入Drawable加载图片
     *
     * @param context
     * @param imageView
     * @param drawable
     * @param defaultBitmap
     */
    public static void displayImageByBitmapDrawable(Context context, ImageView imageView, BitmapDrawable drawable, Bitmap defaultBitmap) {
        Drawable defaultDrawable = BitmapUtil.bitmap2Drawable(defaultBitmap);
        displayImageByBitmapDrawable(context, imageView, drawable, defaultDrawable);
    }

    /**
     * 传入Drawable加载图片
     *
     * @param context
     * @param imageView
     * @param drawable
     * @param defaultDrawble
     */
    public static void displayImageByBitmapDrawable(Context context, ImageView imageView, BitmapDrawable drawable, Drawable defaultDrawble) {
        byte[] bytes = BitmapUtil.Drawable2Bytes(drawable);
        Glide.with(context).load(bytes).error(defaultDrawble).fallback(defaultDrawble).into(imageView);
    }

    /**
     * BitmapDrawable
     *
     * @param context
     * @param imageView
     * @param drawable
     */
    public static void displayImageByBitmapDrawable(Context context, ImageView imageView, BitmapDrawable drawable) {
        displayImageByBitmapDrawable(context, imageView, drawable, R.mipmap.icon_launcher);
    }

    /**
     * 传入Drawable加载图片
     *
     * @param context
     * @param imageView
     * @param drawable
     * @param drawableId
     */
    public static void displayImageByBitmapDrawable(Context context, ImageView imageView, BitmapDrawable drawable, int drawableId) {
        byte[] bytes = BitmapUtil.Drawable2Bytes(drawable);
        Glide.with(context).load(bytes).error(drawableId).fallback(drawableId).into(imageView);
    }

    /**
     * 传入Drawable加载图片
     *
     * @param context
     * @param imageView
     * @param drawable
     */
    public static void displayImageByDrawable(Context context, ImageView imageView, Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }

    /**
     * 传入Drawable加载图片
     *
     * @param context
     * @param imageView
     * @param drawable
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void displayImageByDrawable(Context context, ImageView imageView, int drawable) {
        imageView.setBackgroundResource(drawable);
    }

    /**
     * 加载本地图片
     *
     * @param context
     * @param imageView
     * @param folderName 图片所在的文件夹名称raw/mipmap/drawable
     * @param imageName
     */
    public static void displayImageByResource(Context context, ImageView imageView, String folderName, String imageName) {
        imageView.setImageResource(context.getResources().getIdentifier(imageName, folderName, context.getPackageName()));
    }
}
