package com.anzhuo.video.app.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * creat on 2016/3/25.
 */
public final class ViewUtils {

    /**
     * 显示视图
     */
    public static void visible(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 隐藏视图
     */
    public static void invisible(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 消失视图
     */
    public static void gone(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 根据boolean值设定view显示隐藏
     */
    public static void setVisible(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 判断视图是否显示
     */
    public static boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }


    /**
     * 隐藏软键盘，无需输入框对象，调用系统方法隐藏软键盘
     */
    public static void hideSoftInput(Activity activity) {
        if (activity != null) {
            final InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm != null) {
                View focusView = activity.getCurrentFocus();
                if (focusView != null) {
                    IBinder windowToken = focusView.getWindowToken();
                    if (windowToken != null) {
                        imm.hideSoftInputFromWindow(windowToken, 0);
                    }
                }
            }
        }
    }

    /**
     * 强制显示软键盘
     */
    public static void openSoftInput(Activity activity, EditText editText, boolean isShow) {
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        }
        if (isShow == false) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 显示长短线
     *
     * @param shortLine    短线视图
     * @param longLine     长线视图
     * @param position     当前项位置
     * @param longPosition 需要显示长线的位置
     */
    public static void showShortOrLongLine(View shortLine, View longLine, int position,
                                           int longPosition) {
        if (shortLine != null && longLine != null) {
            if (position == longPosition) {
                shortLine.setVisibility(View.GONE);
                longLine.setVisibility(View.VISIBLE);
            } else {
                shortLine.setVisibility(View.VISIBLE);
                longLine.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 搜索内容匹配高亮显示
     */
    public static String highlightSearchString(final String searchString,
                                               final String matchedString, int mColor) {
        if (matchedString == null || searchString == null) {
            return matchedString;
        }

        //忽略大小写匹配
        final Matcher matcher = Pattern.compile(Pattern.quote(searchString.toLowerCase())).matcher(
                matchedString.toLowerCase());

        // 0,1,2,[3,4,5],6,7
        final StringBuilder stringBuilder = new StringBuilder();
        int subStartIndex = 0;
        int subEndIndex;
        while (matcher.find())//start:3, end:6
        {
            subEndIndex = matcher.start();//3

            stringBuilder
                    .append(matchedString.substring(subStartIndex, subEndIndex));//append:0,1,2

            subStartIndex = subEndIndex;//3
            subEndIndex = matcher.end();//6
            stringBuilder
                    .append("<font color=" + mColor + ">" + matchedString
                            .substring(subStartIndex, subEndIndex) + "</font>");//append:3,4,5

            subStartIndex = subEndIndex;//6
        }

        final int matchedStringLen = matchedString.length();//8
        if (subStartIndex < matchedStringLen) {
            subEndIndex = matchedStringLen;//8
            stringBuilder.append(matchedString.substring(subStartIndex, subEndIndex));//append:6,7
        }

        return stringBuilder.toString();
    }

    /**
     * 搜索内容匹配高亮显示
     */
    private String highlightSearchString(String mSearchString, String matchedString) {
        return ViewUtils.highlightSearchString(mSearchString, matchedString, 0xff97d069);
    }

    /**
     * 清除文本内容
     */
    public static void clearTextView(TextView view) {
        if (view != null) {
            view.setText(null);
        }
    }

    /**
     * 根据布局文件返回
     *
     * @param context
     * @param ResID
     * @return
     */
    public static View GetView(Context context, int ResID) {
        View view = LayoutInflater.from(context).inflate(ResID, null);
        return view;
    }


}
