package com.anzhuo.video.app.utils.FrescoUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.anzhuo.video.app.utils.ToastUtil;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static android.R.attr.path;

/**
 * created on 2016/10/14 0014.
 * 保存图片到本地
 */

public class FrescoSavePicUtil {
    private static final String TAG = "FrescoSavePicUtil";
    public static final String IMAGE_PIC_CACHE_DIR = Environment.getExternalStorageDirectory().getPath() + "/Android/MTDZ";

    /**
     * 保存图片
     *
     * @param
     * @param picUrl
     */
    public static void savePicture(String picUrl, Context context, String ImgType) {

        File picDir = new File(IMAGE_PIC_CACHE_DIR, "Boohee");

        if (!picDir.exists()) {
            picDir.mkdirs();
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(Uri.parse(picUrl)), context);
        File cacheFile = getCachedImageOnDisk(cacheKey);
        if (cacheFile == null) {
            String fileName = System.currentTimeMillis() + "";//以当前时间明命名
            downLoadImage(Uri.parse(picUrl), fileName, context, ImgType);
            return;
        } else {
            String fileName = System.currentTimeMillis() + "";//以当前时间明命名
            copyTo(context, cacheFile, picDir, fileName, ImgType);
        }
    }

    public static File getCachedImageOnDisk(CacheKey cacheKey) {
        File localFile = null;
        if (cacheKey != null) {
            if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }


    /**
     * 复制文件
     *
     * @param src 源文件
     * @param
     * @return
     */
    public static boolean copyTo(Context context, File src, File dir, String filename, String ImgType) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        File dst = null;
        String Myfilename = "";
        try {
            fi = new FileInputStream(src);
            in = fi.getChannel();//得到对应的文件通道
            if (ImgType.equals("gif") || ImgType.contains("gif")) {
                dst = new File(dir, filename + ".gif");
                Myfilename = filename + ".gif";
                Logger.i("Myfilename=" + Myfilename);
            } else {
                dst = new File(dir, filename + ".jpg");
                Myfilename = filename + ".jpg";
                Logger.i("Myfilename=" + Myfilename);
            }
            fo = new FileOutputStream(dst);
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
            ToastUtil.showCenterToast("保存成功");
            Logger.i("保存成功");
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }
                if (in != null) {
                    in.close();
                }

                if (fo != null) {
                    fo.close();
                }

                if (out != null) {
                    out.close();
                }
                // 其次把文件插入到系统图库
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        dst.getAbsolutePath(), Myfilename, "");
                // 最后通知图库更新
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 下载
     *
     * @param uri
     * @param filename
     * @param context
     */
    public static void downLoadImage(Uri uri, final String filename, final Context context, final String ImgType) {
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(Bitmap bitmap) {
                if (bitmap == null) {
                    Log.e(TAG, "保存图片失败啦,无法下载图片");
                }
                String Myfilename = "";
                File appDir = new File(IMAGE_PIC_CACHE_DIR, "Boohee");
                if (!appDir.exists()) {
                    appDir.mkdirs();
                }
                String fileName;
                if (ImgType.equals("gif") || ImgType.contains("gif")) {
                    fileName = filename + ".gif";
                    Myfilename = filename + ".gif";
                } else {
                    fileName = filename + ".jpg";
                    Myfilename = filename + ".jpg";
                }

                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    Logger.i("保存成功");
                    // 其次把文件插入到系统图库
                    MediaStore.Images.Media.insertImage(context.getContentResolver(),
                            file.getAbsolutePath(), Myfilename, "");
                    // 最后通知图库更新
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
            }
        }, CallerThreadExecutor.getInstance());
    }
}
