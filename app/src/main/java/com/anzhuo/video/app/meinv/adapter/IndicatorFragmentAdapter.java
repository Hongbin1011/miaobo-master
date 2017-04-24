package com.anzhuo.video.app.meinv.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.meinv.entity.NavigationInfo;
import com.anzhuo.video.app.meinv.fragment.ChildFragment;
import com.anzhuo.video.app.search.utils.XUtilLog;
import com.shizhefei.view.indicator.IndicatorViewPager;

import java.util.List;

/**
 * Created by Administrator on 2016/8/3.
 */
public class IndicatorFragmentAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

    private List<NavigationInfo> navigationInfos;
    private Context context;

    public IndicatorFragmentAdapter(FragmentManager fragmentManager, Context context, List<NavigationInfo> navigationInfos) {
        super(fragmentManager);
        this.context = context;
        this.navigationInfos = navigationInfos;
    }

    @Override
    public int getCount() {
        XUtilLog.log_i("wbb","navigationInfos.size:"+navigationInfos.size());
        return navigationInfos.size();
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.tab_top, container, false);
//            convertView = ((FragmentActivity) context).getLayoutInflater().inflate(R.layout.tab_top, container, false);
        }
        TextView textView = (TextView) convertView;
        textView.setText(navigationInfos.get(position).getTags());
//        textView.setText(versions[position]);
//        int witdh = getTextWidth(textView);
//        int padding = DisplayUtil.dip2px(context,4);
        //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
        //1.3f是根据上面字体大小变化的倍数1.3f设置
//        XUtilLog.log_i("wbb","=======witdh=======:"+witdh);
//        textView.setWidth(witdh);

        return convertView;
    }

    @Override
    public Fragment getFragmentForPage(int position) {
        ChildFragment fragment = new ChildFragment();
        Bundle bundle = new Bundle();
//        bundle.putInt(ChildFragment.INTENT_INT_INDEX, position);
        bundle.putString("INTENT_CATEGORY",navigationInfos.get(position).getTags());
        fragment.setArguments(bundle);
        return fragment;
    }

    private int getTextWidth(TextView textView) {
        if (textView == null) {
            return 0;
        }
        Rect bounds = new Rect();
        String text = textView.getText().toString();
        Paint paint = textView.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.left + bounds.width();
        return width;
    }
}
