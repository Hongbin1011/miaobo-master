package com.anzhuo.video.app.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.nio.charset.Charset;
import java.text.DecimalFormat;

/**
 * creat on 2016/3/25.
 */
public class MyTextUtils {

    /**
     * 设置字符串编码格式（utf-8）
     */
    public static byte[] getBytesUtf8(String string) {
        return getBytesUnchecked(string, "UTF-8");
    }

    public static byte[] getBytesUnchecked(String string, String charsetName) {
        if (string == null) {
            return null;
        }
        try {
            return string.getBytes(charsetName);
        } catch (Exception e) {
            return null;
        }
    }

    private enum CheckType {
        EMPTY,
        NOT_EMPTY,
        BLANK,
        NOT_BLANK;
    }

    private static boolean isAllCheck(CheckType checkType, CharSequence str, CharSequence... strs) {
        if (!isCheck(checkType, str)) {
            return false;
        }

        if (strs != null && strs.length > 0) {
            for (CharSequence strItem : strs) {
                if (!isCheck(checkType, strItem)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean isCheck(CheckType checkType, CharSequence str) {
        switch (checkType) {
            case EMPTY:
                return isEmpty(str);
            case NOT_EMPTY:
                return isNotEmpty(str);
            case BLANK:
                return isBlank(str);
            case NOT_BLANK:
                return isNotBlank(str);
        }

        return false;
    }


    /**
     * 字符串是否都为空
     */
    public static boolean isAllEmpty(CharSequence str, CharSequence... strs) {
        return isAllCheck(CheckType.EMPTY, str, strs);
    }

    /**
     * 字符串是否都不为空
     */
    public static boolean isAllNotEmpty(CharSequence str, CharSequence... strs) {
        return isAllCheck(CheckType.NOT_EMPTY, str, strs);
    }

    /**
     * 字符串是否都为空白
     */
    public static boolean isAllBlank(CharSequence str, CharSequence... strs) {
        return isAllCheck(CheckType.BLANK, str, strs);
    }

    /**
     * 字符串是否都不为空白
     */
    public static boolean isAllNotBlank(CharSequence str, CharSequence... strs) {
        return isAllCheck(CheckType.NOT_BLANK, str, strs);
    }


    /**
     * 字符串是否为空
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() < 1;
    }

    /**
     * 字符串是否不为空
     */
    public static boolean isNotEmpty(CharSequence str) {
        return str != null && str.length() > 0;
    }


    /**
     * <p>
     * Checks if a String is whitespace, empty ("") or null.
     * </p>
     * <p>
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     */
    public static boolean isBlank(CharSequence str) {
        if (str == null) {
            return true;
        }

        final int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }


    /**
     * 获取字符串字节长度
     */
    public static int getByteLen(String content) {
        if (isEmpty(content)) {
            return 0;
        }
        return content.getBytes(Charset.forName("UTF-8")).length;
    }

    public static void setClipboard(Context context, String content) {
        if (content == null) {
            return;
        }
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
//        cmb.setText(content);
        ClipData data = ClipData.newPlainText("", content);
        cmb.setPrimaryClip(data);
    }

    public static boolean checkStringInStrings(String string, String[] strings) {
        if (string == null || strings == null || strings.length < 1) {
            return false;
        }

        for (String str : strings) {
            if (string.equals(str)) {
                return true;
            }
        }

        return false;
    }

    //==========================================================
    // 将字符串解析成数字
    //==========================================================

    /**
     * 将字符串解析成 byte 类型的数字。
     *
     * @return 如果无法解析，返回 0 。
     */
    public static byte parseByte(CharSequence string) {
        return parseByte(string, 10);
    }

    /**
     * 将字符串解析成 byte 类型的数字。
     *
     * @return 如果无法解析，返回 0 。
     */
    public static byte parseByte(CharSequence string, int radix) {
        return (byte) parseNumber(NumberType.BYTE, string, radix);
    }


    /**
     * 将字符串解析成 short 类型的数字。
     *
     * @return 如果无法解析，返回 0 。
     */
    public static short parseShort(CharSequence string) {
        return parseShort(string, 10);
    }

    /**
     * 将字符串解析成 short 类型的数字。
     *
     * @return 如果无法解析，返回 0 。
     */
    public static short parseShort(CharSequence string, int radix) {
        return (short) parseNumber(NumberType.SHORT, string, radix);
    }


    /**
     * 将字符串解析成 int 类型的数字。
     *
     * @return 如果无法解析，返回 0 。
     */
    public static int parseInt(CharSequence string) {
        return parseInt(string, 10);
    }

    /**
     * 将字符串解析成 int 类型的数字。
     *
     * @return 如果无法解析，返回 0 。
     */
    public static int parseInt(CharSequence string, int radix) {
        return (int) parseNumber(NumberType.INT, string, radix);
    }


    /**
     * 将字符串解析成 long 类型的数字。
     *
     * @return 如果无法解析，返回 0 。
     */
    public static long parseLong(CharSequence string) {
        return parseLong(string, 10);
    }

    /**
     * 将字符串解析成 long 类型的数字。
     *
     * @return 如果无法解析，返回 0 。
     */
    public static long parseLong(CharSequence string, int radix) {
        return (long) parseNumber(NumberType.LONG, string, radix);
    }


    /**
     * 将字符串解析成 float 类型的数字。
     *
     * @return 如果无法解析，返回 0 。
     */
    public static float parseFloat(CharSequence string) {
        return parseFloat(string, 10);
    }

    /**
     * 将字符串解析成 float 类型的数字。
     *
     * @return 如果无法解析，返回 0 。
     */
    public static float parseFloat(CharSequence string, int radix) {
        return (float) parseNumber(NumberType.FLOAT, string, radix);
    }


    /**
     * 将字符串解析成 double 类型的数字。
     *
     * @return 如果无法解析，返回 0 。
     */
    public static double parseDouble(CharSequence string) {
        return parseDouble(string, 10);
    }

    /**
     * 将字符串解析成 double 类型的数字。
     *
     * @return 如果无法解析，返回 0 。
     */
    public static double parseDouble(CharSequence string, int radix) {
        return (double) parseNumber(NumberType.DOUBLE, string, radix);
    }


    public enum NumberType {
        BYTE, SHORT, INT, LONG, FLOAT, DOUBLE;
    }

    private static Number parseNumber(NumberType numberType, CharSequence string, int radix) {
        try {
            switch (numberType) {
                case BYTE:
                    return Byte.parseByte(string.toString(), radix);
                case SHORT:
                    return Short.parseShort(string.toString(), radix);
                case INT:
                    return Integer.parseInt(string.toString(), radix);
                case LONG:
                    return Long.parseLong(string.toString(), radix);
                case FLOAT:
                    return Float.parseFloat(string.toString());
                case DOUBLE:
                    return Double.parseDouble(string.toString());
            }
        } catch (Throwable tr) {
            tr.printStackTrace();
        }

        return 0;
    }


    /**
     * 检查字符串能否解析成对应类型的数字。
     *
     * @return 如果能解析返回 true，否则返回 false 。
     */
    public static boolean canParseNumber(NumberType numberType, CharSequence string) {
        return canParseNumber(numberType, string, 10);
    }

    /**
     * 检查字符串能否解析成对应类型的数字。
     *
     * @return 如果能解析返回 true，否则返回 false 。
     */
    public static boolean canParseNumber(NumberType numberType, CharSequence string, int radix) {
        try {
            switch (numberType) {
                case BYTE:
                    Byte.parseByte(string.toString(), radix);
                case SHORT:
                    Short.parseShort(string.toString(), radix);
                case INT:
                    Integer.parseInt(string.toString(), radix);
                case LONG:
                    Long.parseLong(string.toString(), radix);
                case FLOAT:
                    Float.parseFloat(string.toString());
                case DOUBLE:
                    Double.parseDouble(string.toString());
            }
        } catch (Throwable tr) {
            return false;
        }

        return true;
    }

    /**
     * 隐藏手机号中间几位
     */
    public static String getHideMobileNo(String mobileNo) {
        if (mobileNo == null) {
            return mobileNo;
        }
        int len = mobileNo.length();
        if (len < 11) {
            return mobileNo;
        } else {
            return mobileNo.substring(0, 3) + "*****" + mobileNo.substring(8, len);
        }
    }

    /**
     * Returns a string containing the tokens joined by delimiters.
     *
     * @param tokens an array objects to be joined. Strings will be formed from
     *               the objects by calling object.toString().
     */
    public static String join(CharSequence delimiter, Iterable tokens, CharSequence tokenPrefix,
                              CharSequence tokenSuffix) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }

            if (tokenPrefix != null) {
                sb.append(tokenPrefix);
            }

            sb.append(token);

            if (tokenSuffix != null) {
                sb.append(tokenSuffix);
            }
        }
        return sb.toString();
    }


    public final static String UTF_8 = "utf-8";

    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true
     */
    public static boolean isEquals(String... agrs) {
        String last = null;
        for (int i = 0; i < agrs.length; i++) {
            String str = agrs[i];
            if (isEmpty(str)) {
                return false;
            }
            if (last != null && !str.equalsIgnoreCase(last)) {
                return false;
            }
            last = str;
        }
        return true;
    }

    /**
     * 返回一个高亮spannable
     *
     * @param content 文本内容
     * @param color   高亮颜色
     * @param start   起始位置
     * @param end     结束位置
     * @return 高亮spannable
     */
    public static CharSequence getHighLightText(String content, int color, int start, int end) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        start = start >= 0 ? start : 0;
        end = end <= content.length() ? end : content.length();
        SpannableString spannable = new SpannableString(content);
        CharacterStyle span = new ForegroundColorSpan(color);
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 获取链接样式的字符串，即字符串下面有下划线
     *
     * @param resId 文字资源
     * @return 返回链接样式的字符串
     */
//    public static Spanned getHtmlStyleString(int resId) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<a href=\"\"><u><b>").append(UIUtils.getString(resId)).append(" </b></u></a>");
//        return Html.fromHtml(sb.toString());
//    }

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

    /**
     * 设置点击
     *
     * @param view
     * @param id
     */
    public static void setTextDrawable(Context context, TextView view, int id) {
        // 设置 Drawables
        Drawable drawable = context.getResources().getDrawable(id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 主要是给tv设置图片和字体颜色
     *
     * @param textView1
     * @param DrawableID
     * @param ColorID
     */
    public static void SetDrawableAndText(Context context, TextView textView1, int DrawableID, int ColorID) {
        Logger.i("========[DrawableID]============="+DrawableID);
        MyTextUtils.setTextDrawable(context, textView1, DrawableID);
        textView1.setTextColor(context.getResources().getColor(ColorID));
    }
    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能
     * add by wangqianzhou
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }




}
