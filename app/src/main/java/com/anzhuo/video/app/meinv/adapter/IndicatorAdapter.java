package com.anzhuo.video.app.meinv.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.meinv.entity.NavigationInfo;
import com.anzhuo.video.app.meinv.fragment.ChildFragment;
import com.anzhuo.video.app.search.utils.XUtilLog;
import com.anzhuo.video.app.utils.DisplayUtil;
import com.shizhefei.view.indicator.IndicatorViewPager;

import java.util.List;


/**
 * Created by Administrator on 2016/8/3.
 */
public class IndicatorAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter{
    private String[] versions = {"Cupcake", "Donut", "Éclair", "Froyo", "Gingerbread", "Honeycomb", "Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lolipop", "Marshmallow"};
    private String[] names = {"纸杯蛋糕", "甜甜圈", "闪电泡芙", "冻酸奶", "姜饼", "蜂巢", "冰激凌三明治", "果冻豆", "奇巧巧克力棒", "棒棒糖", "棉花糖"};
    private List<NavigationInfo> navigationInfos;
    private Context context;
    public IndicatorAdapter(Context context,List<NavigationInfo> navigationInfos) {
        this.context = context;
        this.navigationInfos = navigationInfos;
    }

    @Override
    public int getCount() {
        XUtilLog.log_i("leibown","navigationInfos.size():"+navigationInfos.size());
//        return versions.length;
        return navigationInfos.size();
//        return names.length;
    }


    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView =((FragmentActivity) context).getLayoutInflater().inflate(R.layout.tab_top, container, false);
        }
        TextView textView = (TextView) convertView;
        textView.setText(navigationInfos.get(position).getTags());
//        textView.setText(versions[position]);
        int witdh = getTextWidth(textView);
        int padding = DisplayUtil.dip2px(context, 20);

        //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
        //1.3f是根据上面字体大小变化的倍数1.3f设置
        textView.setWidth((int) (witdh * 1.3f) + padding);

        return convertView;
    }

    @Override
    public View getViewForPage(int position, View convertView, ViewGroup container) {
//        if (convertView == null) {
//            convertView = new TextView(container.getContext());
//        }
//        TextView textView = (TextView) convertView;
//        textView.setText(names[position]);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(Color.GRAY);
        convertView = new ChildFragment().getView();
        return convertView;
    }


    @Override
    public int getItemPosition(Object object) {
        //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
        // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
        return PagerAdapter.POSITION_UNCHANGED;
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

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        XUtilLog.log_i("leibown","notifyDataSetChanged");
    }
}
