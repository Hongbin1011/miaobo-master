package com.anzhuo.video.app.widget.NineGridImageview;

import android.content.Context;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Jaeger on 16/2/24.
 * <p/>
 * Email: chjie.jaeger@gamil.com
 * GitHub: https://github.com/laobie
 */
public abstract class NineGridImageViewAdapter<T> {

    private Context context;

    public NineGridImageViewAdapter(Context context) {
        this.context = context;
    }

    /**
     * 展示图片
     *
     * @param position
     * @param SimpleDraweeView
     * @param t
     */
    protected abstract void onDisplayImage(int position, GridImageView SimpleDraweeView, T t);

    /**
     *
     * @param t
     * @param position
     */
    protected abstract void onItemImageClick(T t, int position);

    protected SimpleDraweeView generateImageView() {
        GridImageView simpleDraweeView = new GridImageView(context);
//        simpleDraweeView.setScaleType(SimpleDraweeView.ScaleType.FIT_XY);
        return simpleDraweeView;
    }

}