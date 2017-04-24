package com.anzhuo.video.app.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created with Android Studio.
 * Authour：Eric_chan
 * Date：2016/6/23 11:21
 * Des：
 */
public class Utils {

    /**
     * SD卡三种状
     */
    public static enum MountStatuds {
        SD_CARD_AVAILABLE, SD_CARD_NOT_AVAILABLE, SD_CARD_SPACE_NOT_ENOUGH
    }

    /**
     * 预设SD卡空间 (单位M)
     */
    public static final long CACHE_SIZE = 100;
    public static final int MB = 1024 * 1024;
    public static final String SDCARD_PATH = ("Android" + File.separator + "data" + File.separator).intern();

    public static final Locale[] LANGUAGE_CATEGORY = {Locale.getDefault(), Locale.CHINESE, Locale.ENGLISH,
            Locale.KOREAN, Locale.TAIWAN,};

    /**
     * 默认为可用状
     */
    public static MountStatuds SDCardStatus = MountStatuds.SD_CARD_AVAILABLE;

    private static ThreadLocal<byte[]> threadSafeByteBuf = null;

    public static byte[] getThreadSafeByteBuffer() {
        if (threadSafeByteBuf == null) {
            threadSafeByteBuf = new ThreadLocal<byte[]>();
        }

        byte[] buf = threadSafeByteBuf.get();

        if (buf == null) {
            buf = new byte[1024 * 4]; // 4kb
            threadSafeByteBuf.set(buf);
        }

        return buf;
    }


    /**
     * 关闭IO流
     *
     * @param obj
     */
    public static void closeCloseable(Closeable obj) {
        try {
            // 修复小米MI2的JarFile没有实现Closeable导致崩溃问题
            if (obj != null && obj instanceof Closeable)
                obj.close();

        } catch (IOException e) {
//            LogManager.e(e);
        }
    }

    // 产生userAgent
    public static String gennerateUserAgent(Context context) {
        StringBuilder sb = new StringBuilder();
//Mozilla/5.0 (X11; Linux i686) Android/537.36 (KHTML, like Gecko) 9Game/27.0.1453.93 Safari/537.36
        sb.append("Mozilla/5.0 (Linux; U; Android");
        sb.append(Build.VERSION.RELEASE);
        sb.append("; ");
        sb.append(Locale.getDefault().toString());

        String model = Build.MODEL;
        if (!TextUtils.isEmpty(model)) {
            sb.append("; ");
            sb.append(model);
        }

        String buildId = Build.ID;
        if (!TextUtils.isEmpty(buildId)) {
            sb.append("; Build/");
            sb.append(buildId);
        }

        sb.append(") ");

        int versionCode = 0;
        String packageName = context.getPackageName();
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo packageInfo = manager.getPackageInfo(packageName, 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // Keep the versionCode 0 as default.
        }

        sb.append(packageName);
        sb.append("/");
        sb.append(versionCode);

        sb.append("; ");
        return sb.toString();
    }


    public static String getImageCacheDir(Context context) {
        File f = new File(getRootPath(context), "image".intern());
        if (!f.exists()) {
            f.mkdirs();
        }
        return f.getPath();
    }

    /**
     * root 路径
     */
    public static String getRootPath(Context context) {
        StringBuilder sb = new StringBuilder();

        SDCardStatus = getSDCardStatus();
        switch (SDCardStatus) {
            case SD_CARD_AVAILABLE:
            case SD_CARD_SPACE_NOT_ENOUGH:
                sb.append(Environment.getExternalStorageDirectory().getPath()).append(File.separator).append(SDCARD_PATH)
                        .append(context.getPackageName());
                break;
            case SD_CARD_NOT_AVAILABLE:
                sb.append(context.getCacheDir().getPath());
                break;
        }
        return sb.toString();
    }

    public static MountStatuds getSDCardStatus() {
        MountStatuds status;
        String sdState = Environment.getExternalStorageState();
        if (sdState.equals(Environment.MEDIA_MOUNTED)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long availCount = sf.getAvailableBlocks();
            long blockSize = sf.getBlockSize();
            long availSize = availCount * blockSize / MB;
            /** 100M内存空间大小 */
            if (availSize < CACHE_SIZE) {
                /** TODO 是否提示用户空间不够 */
                status = MountStatuds.SD_CARD_SPACE_NOT_ENOUGH;
            } else {
                status = MountStatuds.SD_CARD_AVAILABLE;
            }
        } else {
            status = MountStatuds.SD_CARD_NOT_AVAILABLE;
        }
        return status;
    }


    public static String[] getHalfAMonthDate(int arrayLength) {
        String[] arrayTitles = new String[arrayLength];
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        for (int i = 0; i < arrayLength; i++) {
            if (i == arrayLength / 2) {//7
                arrayTitles[i] = "今 天";
            }
            if (i == arrayLength / 2 - 1) {
                arrayTitles[i] = "昨 天";
            }
            if (i == arrayLength / 2 + 1) {
                arrayTitles[i] = "明 天";
            }
            if (i < arrayLength / 2 - 1) {
                calendar.add(Calendar.DAY_OF_MONTH, i - arrayLength / 2);
                int newMonth = calendar.get(Calendar.MONTH) + 1;
                int date = calendar.get(Calendar.DAY_OF_MONTH);
                String today;
                if (date < 10) {
                    today = "0" + String.valueOf(date);
                } else {
                    today = String.valueOf(date);
                }
                String mon;
                if (month != newMonth) {
                    mon = newMonth < 10 ? "0" + String.valueOf(newMonth) : String.valueOf(newMonth);
                    arrayTitles[i] = mon + "/" + today;
                } else {
                    mon = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
                    arrayTitles[i] = mon + "/" + today;
                }
            }
            if (i > arrayLength / 2 + 1) {
                calendar.add(Calendar.DAY_OF_MONTH, i - arrayLength / 2);
                int newMonth = calendar.get(Calendar.MONTH) + 1;
                int date = calendar.get(Calendar.DAY_OF_MONTH);
                String today;
                if (date < 10) {
                    today = "0" + String.valueOf(date);
                } else {
                    today = String.valueOf(date);
                }
                String mon;
                if (month != newMonth) {
                    mon = newMonth < 10 ? "0" + String.valueOf(newMonth) : String.valueOf(newMonth);
                    arrayTitles[i] = mon + "/" + today;
                } else {
                    mon = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
                    arrayTitles[i] = mon + "/" + today;
                }
            }
            calendar = Calendar.getInstance();
        }
        return arrayTitles;
    }

    public static String[] getHalfOfTheMonthDate(int arrayLength) {
        String[] arrayTitles = new String[arrayLength];
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        for (int i = 0; i < arrayLength; i++) {
            if (i == arrayLength - 1) {//7
                arrayTitles[i] = "今 天";
            }
            if (i == arrayLength - 2) {
                arrayTitles[i] = "昨 天";
            }
            if (i < arrayLength - 2) {
                calendar.add(Calendar.DAY_OF_MONTH, i - (arrayLength - 1));
                int newMonth = calendar.get(Calendar.MONTH) + 1;
                int date = calendar.get(Calendar.DAY_OF_MONTH);
                String today;
                if (date < 10) {
                    today = "0" + String.valueOf(date);
                } else {
                    today = String.valueOf(date);
                }
                String mon;
                if (month != newMonth) {
                    mon = newMonth < 10 ? "0" + String.valueOf(newMonth) : String.valueOf(newMonth);
                    arrayTitles[i] = mon + "/" + today;
                } else {
                    mon = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
                    arrayTitles[i] = mon + "/" + today;
                }
            }
            calendar = Calendar.getInstance();
        }
        return arrayTitles;
    }

    private static String[] week = new String[]{" 周 日 ", " 周 一 ", " 周 二 ", " 周 三 ", " 周 四 ", " 周 五 ", " 周 六 "};

    public static String[][] getDomesticFutureDate(int x, int y) {//国内
        String[][] arrayTitles = new String[x][y];
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        for (int i = 0; i < x; i++) {//x=2
            for (int j = 0; j < y; j++) {//j = 15
                calendar.add(Calendar.DAY_OF_MONTH, -1);//向前移一天
                calendar.add(Calendar.DAY_OF_MONTH, j);
                if (i == 0) {
                    if (j == 0) {
                        arrayTitles[0][j] = " 昨 天 ";
                    } else if (j == 1) {
                        arrayTitles[0][j] = " 今 天 ";
                    } else {
                        arrayTitles[0][j] = week[calendar.get(Calendar.DAY_OF_WEEK) - 1];
                    }
                } else {
                    int tempMonth = calendar.get(Calendar.MONTH) + 1;//currentMonth
                    int tempDay = calendar.get(Calendar.DAY_OF_MONTH);//currentDay
                    String currentDay = tempDay < 10 ? "0" + String.valueOf(tempDay) : String.valueOf(tempDay);
                    String currentMonth;
                    if (month != tempMonth) {
                        currentMonth = tempMonth < 10 ? "0" + String.valueOf(tempMonth) : String.valueOf(tempMonth);
                        arrayTitles[i][j] = currentMonth + "/" + currentDay;
                    } else {
                        currentMonth = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
                        arrayTitles[i][j] = currentMonth + "/" + currentDay;
                    }
                }
                calendar = Calendar.getInstance();
            }
        }
        return arrayTitles;
    }

    public static String[][] getAbroadFutureDate(int x, int y) {//国外
        String[][] arrayTitles = new String[x][y];
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        for (int i = 0; i < x; i++) {//x=2
            for (int j = 0; j < y; j++) {//j = 15
                // calendar.add(Calendar.DAY_OF_MONTH,-1);//向前移一天
                calendar.add(Calendar.DAY_OF_MONTH, j);
                if (i == 0) {
                    if (j == 0) {
                        arrayTitles[0][j] = " 今 天 ";
                    } else {
                        arrayTitles[0][j] = week[calendar.get(Calendar.DAY_OF_WEEK) - 1];
                    }
                } else {
                    int tempMonth = calendar.get(Calendar.MONTH) + 1;//currentMonth
                    int tempDay = calendar.get(Calendar.DAY_OF_MONTH);//currentDay
                    String currentDay = tempDay < 10 ? "0" + String.valueOf(tempDay) : String.valueOf(tempDay);
                    String currentMonth;
                    if (month != tempMonth) {
                        currentMonth = tempMonth < 10 ? "0" + String.valueOf(tempMonth) : String.valueOf(tempMonth);
                        arrayTitles[i][j] = currentMonth + "/" + currentDay;
                    } else {
                        currentMonth = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
                        arrayTitles[i][j] = currentMonth + "/" + currentDay;
                    }
                }
                calendar = Calendar.getInstance();
            }
        }
        return arrayTitles;
    }


    /**
     * 格式化文件大小，不保留末尾的0
     */
    public static String formatFileSize(long len) {
        return formatFileSize(len, false);
    }

    /**
     * 格式化文件大小，保留末尾的0，达到长度一致
     */
    public static String formatFileSize(long len, boolean keepZero) {
        String size;
        DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
        DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
        if (len < 1024) {
            size = String.valueOf(len + "B");
        } else if (len < 10 * 1024) {
            // [0, 10KB)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
        } else if (len < 100 * 1024) {
            // [10KB, 100KB)，保留一位小数
            size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
        } else if (len < 1024 * 1024) {
            // [100KB, 1MB)，个位四舍五入
            size = String.valueOf(len / 1024) + "KB";
        } else if (len < 10 * 1024 * 1024) {
            // [1MB, 10MB)，保留两位小数
            if (keepZero) {
                size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024 / 1024 / (float) 100)) + "MB";
            } else {
                size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100) + "MB";
            }
        } else if (len < 100 * 1024 * 1024) {
            // [10MB, 100MB)，保留一位小数
            if (keepZero) {
                size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024 / 1024 / (float) 10)) + "MB";
            } else {
                size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
            }
        } else if (len < 1024 * 1024 * 1024) {
            // [100MB, 1GB)，个位四舍五入
            size = String.valueOf(len / 1024 / 1024) + "MB";
        } else {
            // [1GB, ...)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100) + "GB";
        }
        return size;
    }

    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;

    public static String StringData() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mYear + "年" + mMonth + "月" + mDay + "日" + "/星期" + mWay;
    }

    public static String myWidgeProviderStringData() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mMonth + "/" + mDay + "星期" + mWay;
    }
}

