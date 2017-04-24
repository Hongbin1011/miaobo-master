package com.anzhuo.video.app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.anzhuo.video.app.config.VideoApplication;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

//import com.activeandroid.util.Log;

/**
 * Created by Administrator on 2016/4/22.
 */
public class BitmapUtils {
    private static String TAG = "test";
    static Bitmap result = null;

    public static Bitmap createVideoThumbnail(final String url, final int width, final int height) {

        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;

    }


    public static Bitmap createVideoThumbnail(String filePath) {
        Class<?> clazz = null;
        Object instance = null;
        try {
            clazz = Class.forName("android.media.MediaMetadataRetriever");
            instance = clazz.newInstance();

            Method method = clazz.getMethod("setDataSource", String.class);
            method.invoke(instance, filePath);

            if (Build.VERSION.SDK_INT <= 9) {
                return (Bitmap) clazz.getMethod("captureFrame").invoke(instance);
            } else {
                byte[] data = (byte[]) clazz.getMethod("getEmbeddedPicture").invoke(instance);
                if (data != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if (bitmap != null) return bitmap;
                }
                return (Bitmap) clazz.getMethod("getFrameAtTime").invoke(instance);
            }
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } catch (InstantiationException e) {
            Log.e(TAG, "createVideoThumbnail", e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, "createVideoThumbnail", e);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "createVideoThumbnail", e);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "createVideoThumbnail", e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "createVideoThumbnail", e);
        } finally {
            try {
                if (instance != null) {
                    clazz.getMethod("release").invoke(instance);
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /**
     * Compress by quality,  and generate image to the path specified
     *
     * @param oldPath
     * @throws IOException
     */
//    public static File compressAndGenImage(String oldPath, int maxSize, Context context) throws IOException {
//        Bitmap image = BitmapFactory.decodeFile(oldPath);
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        // scale
//        int options = 100;
//        // Store the bitmap into output stream(no compress)
//        image.compress(Bitmap.CompressFormat.JPEG, options, os);
//        // Compress by loop
//        while (os.toByteArray().length / 1024 > maxSize) {
//            // Clean up os
//            os.reset();
//            // interval 10
//            options -= 10;
//            image.compress(Bitmap.CompressFormat.JPEG, options, os);
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(os.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
//        String fileName = oldPath.substring(oldPath.lastIndexOf("/"));
//        File file = bitmapToFile(bitmap, fileName, context);
//        return file;
//    }

   /* public static File compressImage(String oldPath, Context context) throws IOException {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//开始读入图片，此时把options.inJustDecodeBounds 设回true了
        Bitmap bitmap = BitmapFactory.decodeFile(oldPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1920f;//这里设置高度为800f
        float ww = 1080f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap2 = BitmapFactory.decodeFile(oldPath, newOpts);
        Bitmap compressBitmap = compressImage(bitmap2);//压缩好比例大小后再进行质量压缩
        String fileName = oldPath.substring(oldPath.lastIndexOf("/"));
        File file = bitmapToFile(bitmap2, fileName, context);
        return file;
    }*/


    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 1024 && options > 0) {//循环判断如果压缩后图片是否大于1024kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


//    public static File bitmapToFile(Bitmap bm, String fileName, Context context) throws IOException {
//        String path = FileUtils.getAppPath(context) + "/pic";
//        File dirFile = new File(path);
//        if (!dirFile.exists()) {
//            dirFile.mkdirs();
//        }
//        File myCaptureFile = new File(path, fileName);
//        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
//        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//        bos.flush();
//        bos.close();
//        return myCaptureFile;
//    }

    /**
     * 通过file转bitmap并且获得宽高
     *
     * @param FilePath
     * @return
     */
    public static BitmapFactory.Options FileToBitmap(String FilePath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        Bitmap bitmap = BitmapFactory.decodeFile(FilePath, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        Logger.i("file  width=" + width + " height= " + height);
        return opts;
    }

    /**
     * 截取视频第一秒
     *
     * @param VideoPath
     * @return
     */
    public static Bitmap VideoToBitmap(String VideoPath) {
        Bitmap bitmap = null;
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(VideoPath);
            bitmap = retriever.getFrameAtTime();
            FileOutputStream outStream = null;
            String videoName = VideoPath.substring(VideoPath.lastIndexOf("."));
            File file = new File(VideoApplication.getContext().getExternalCacheDir().getAbsolutePath() + "/mtdz/");
            if (!file.exists()) {
                file.mkdirs();
            }
            outStream = new FileOutputStream(new File(file + videoName + ".jpg"));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outStream);
            outStream.close();
            retriever.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 压缩回调
     */
    public interface CompressImgBack<T> {
        void CompressSuccess(T t);

        void CompressFail();
    }

    /**
     * 压缩图片
     *
     * @param context
     * @param file
     * @param compressImgBack
     */
  /*  public static void CompressImg(Context context, File file, final CompressImgBack compressImgBack) {
        Luban.get(context)
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .asObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        compressImgBack.CompressFail();
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends File>>() {
                    @Override
                    public Observable<? extends File> call(Throwable throwable) {
                        compressImgBack.CompressFail();
                        return Observable.empty();
                    }
                })
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(final File file1) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        Logger.i("压缩后的路径= " + file1.getPath());
                        compressImgBack.CompressSuccess(file1);

                    }
                });
    }
*/
}
