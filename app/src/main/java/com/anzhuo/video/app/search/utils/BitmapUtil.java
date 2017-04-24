package com.anzhuo.video.app.search.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

import java.io.ByteArrayOutputStream;

/**
 * Created with Android Studio.
 * Authour：wbb
 * Date：2016/6/23 16:03
 * Des：
 */
public class BitmapUtil {

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if(drawable instanceof GlideBitmapDrawable){
            return ((GlideBitmapDrawable) drawable).getBitmap();
        }else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth()-2,
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        if (bitmap == null){
            return null;
        }
        Drawable drawable =new BitmapDrawable(bitmap);
        return drawable;
    }

    public static byte[] Drawable2Bytes(BitmapDrawable drawable){

        Bitmap bitmap  = drawable2Bitmap(drawable);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();

    }

    public static byte[] Drawable2Bytes(GlideBitmapDrawable drawable){
        Bitmap bitmap  = drawable2Bitmap(drawable);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static byte[] Drawable2Bytes(NinePatchDrawable drawable){
        Bitmap bitmap  = drawable2Bitmap(drawable);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    public static byte[] Bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 获取圆角位图的方法
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 多张图片拼成一张
     * @param context
     * @param icons 需要拼图的图片数组
     * @param strokeColor 边框颜色
     * @param strokeWidth 边框宽度
     * @return 组装后的图片
     */
    public static Bitmap createShortCutIcon(Context context, BitmapDrawable[] icons, String strokeColor,
                                            int strokeWidth) {
        int margin = 5;
        // 背景框的宽高
        BitmapDrawable drawable = icons[0];
        if (drawable == null) {
            return null;
        }
        int borderWidth = drawable.getBitmap().getWidth(), borderHeight = drawable.getBitmap().getHeight();
        // 将icons里的所有元素的内容都绘制到这个Bitmap上。
        Bitmap bitmap = Bitmap.createBitmap(borderWidth, borderHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(strokeColor));
        //        RectF rect = new RectF(margin,margin,borderWidth-margin,borderHeight-margin);
        RectF rect = new RectF(0, 0, borderWidth, borderHeight);
        canvas.drawRoundRect(rect, margin * 4, margin * 4, paint);//参数二：x方向上的圆角半径，参数三：y方向上的圆角半径
        // 背景框内部，每个小图标的宽高
        int itemWidth, itemHeight;
        if (icons.length == 1) {
            itemWidth = borderWidth - margin * 4;
            itemHeight = borderHeight - margin * 4;
            drawable = icons[0];
            drawable = resizeBitmap(context, drawable, itemWidth, itemHeight);
            // 设置Drawable的尺寸，否则将不会draw任何内容到bitmap中。
            drawable.setBounds(margin * 2, margin * 2, borderWidth - margin * 2, borderHeight - margin * 2);
            drawable.draw(canvas);
            canvas.drawBitmap(drawable.getBitmap(), margin * 2 - strokeWidth, margin * 2 - strokeWidth, paint);
        } else {
            itemWidth = (borderWidth - margin * 4) / 2;
            itemHeight = (borderHeight - margin * 4) / 2;
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;

            for (int i = 0; i < icons.length; i++) {
                drawable = icons[i];
                if (icons.length == 2) {
                    left = margin * 2 + (i % 2) * (itemWidth + margin);
                    top = (borderHeight - itemHeight) / 2;
                    right = left + itemWidth;
                    bottom = top + itemHeight;
                } else {
                    left = margin * 2 + (i % 2) * (itemWidth + margin);
                    if (i > 1) {
                        top = margin * 2 + itemHeight + margin;
                    } else {
                        top = margin * 2;
                    }
                    right = left + itemWidth;
                    bottom = top + itemHeight;
                }
                drawable = resizeBitmap(context, drawable, itemWidth, itemHeight);
                drawable.setBounds(left, top, right, bottom);
                canvas.drawBitmap(drawable.getBitmap(), left - strokeWidth, top - strokeWidth, paint);
            }
        }
        return bitmap;
    }

    public static BitmapDrawable resizeBitmap(Context context, BitmapDrawable drawable, int newWidth, int newHeight) {
        Bitmap oldBitmap = drawable.getBitmap();
        int width = oldBitmap.getWidth();
        int height = oldBitmap.getHeight();

        // calculate the scale - in this case = 0.4f
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // createa matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(oldBitmap, 0, 0, width, height, matrix, true);
        // make a Drawable from Bitmap to allow to set the BitMap
        // to the ImageView, ImageButton or what ever
        return new BitmapDrawable(context.getResources(), resizedBitmap);
    }
}
