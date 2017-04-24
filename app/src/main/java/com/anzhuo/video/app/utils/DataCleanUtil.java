package com.anzhuo.video.app.utils;

import android.content.Context;
import android.os.Environment;

import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * 描 述:  主要功能有清除内/外缓存，清除数据库，清除sharedPreference，清除files和清除自定义目录
 */
public class DataCleanUtil {
    /**
     * 清除本应用内部缓存
     *
     * @param context
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除本应用所有数据库
     *
     * @param context
     */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/databases"));
    }

    /**
     * 清除本应用SharedPreference
     *
     * @param context
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 按名字清除本应用数据库
     *
     * @param context
     * @param dbName
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容
     *
     * @param context
     */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
     *
     * @param context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     *
     * @param filePath
     */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * 清除本应用所有的数据
     *
     * @param context
     */
    public static void cleanApplicationData(Context context) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     *
     * @param directory
     */
    private static void deleteFilesByDirectory(File directory) {
        try {
            Logger.i("==清楚缓存=====" + directory.getName());
            if (directory != null && directory.exists() && directory.isDirectory()) {
                File[] fils = directory.listFiles();

                if (fils != null) {
                    Logger.i("==清楚缓存=====" + directory.getName());
                    if (fils.length == 0) {
                        directory.delete();
                    }
                    for (File item : fils) {
                        if (item == null) {
                            continue;
                        }
                        boolean mCache = item.getName().equals("ACache");//如果是Acache 不清楚缓存
                        if (!mCache) {
                            if (item.isFile()) {
                                item.delete();
                            } else if (item.isDirectory()) {
                                deleteFilesByDirectory(item);
                            }
                        }
                    }
                } else {
                    directory.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAcache() {

    }
}