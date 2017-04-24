package com.anzhuo.video.app.dongtu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.dongtu.bean.dongtu.ImageEntity;
import com.anzhuo.video.app.dongtu.ui.base.DongtuBaseActivity;
import com.anzhuo.video.app.dongtu.utils.MultiTouchViewPager;
import com.anzhuo.video.app.dongtu.utils.MyPhotoView;

import java.util.HashMap;
import java.util.Map;



/**
 * 查看照片
 */
public class DongtuPhotoActivity extends DongtuBaseActivity {

    //    private String mLit_url;
    private String mPictureWidth;
    private String mPictureHeight;

    @Override
    public void SetTag() {

    }

    private String ImgUriList = null;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        intent = getIntent();
        if (intent != null) {
            ImgUriList = intent.getStringExtra("photourl");
//            mLit_url = intent.getStringExtra("Lit_url");
            mPictureWidth = intent.getStringExtra("pictureWidth");
            mPictureHeight = intent.getStringExtra("pictureHeight");
        }
        MultiTouchViewPager viewPager = (MultiTouchViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new DraweePagerAdapter(ImgUriList));
    }


    public class DraweePagerAdapter extends PagerAdapter {

        private String ImgList;
        private Map<Integer, Integer> tagMap;

        public DraweePagerAdapter(String ImgList) {
//            this.context = context;
            this.ImgList = ImgList;
            this.tagMap = new HashMap<>();
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            MyPhotoView myPhotoView = (MyPhotoView) object;
            myPhotoView.setNull();
            container.removeView(myPhotoView);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            JokeImgInfo jokeImgInfo = ImgList.get(position);

            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setBigImageUrl(ImgList)
                    .setBigHeight(mPictureHeight)
                    .setBigWidth(mPictureWidth)
                    .setSmallImageUrl(ImgList)
                    .setImgType("gif");

            MyPhotoView myPhotoView = new MyPhotoView(DongtuPhotoActivity.this, imageEntity);
            myPhotoView.setOnclickListener(new MyPhotoView.PhotoViewOnClickListener() {
                @Override
                public void onClick() {
                    finish();
                }
            });
            container.addView(myPhotoView);
            return myPhotoView;
        }
    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (jokeEntity != null) {
//            jokeEntity = null;
//        }
//        if (ImgUriList != null) {
//            ImgUriList.clear();
//            jokeEntity = null;
//        }
//    }
}
