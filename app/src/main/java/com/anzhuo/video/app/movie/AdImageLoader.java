package com.anzhuo.video.app.movie;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;


/**
 * Created by husong on 2017/2/21.
 */

public class AdImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        ADVInfo ad = (ADVInfo) path;
        Picasso.with(context).load(ad.getAd_img()).into(imageView);
    }
}
