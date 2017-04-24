package com.anzhuo.video.app.meinv.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.meinv.adapter.ViewPagerAdapter;
import com.anzhuo.video.app.meinv.base.BaseActivity;
import com.anzhuo.video.app.meinv.entity.ItemListInfo;
import com.anzhuo.video.app.meinv.entity.PictureDetailInfo;
import com.anzhuo.video.app.meinv.load.ImageLoaderManager;
import com.anzhuo.video.app.meinv.manager.ActivityJump;
import com.anzhuo.video.app.meinv.manager.MyInterface;
import com.anzhuo.video.app.meinv.manager.NIManage;
import com.anzhuo.video.app.meinv.utils.DatabaseUtils;
import com.anzhuo.video.app.meinv.utils.FileUtils;
import com.anzhuo.video.app.meinv.view.MyViewPager;
import com.anzhuo.video.app.search.utils.ToastUtil;
import com.anzhuo.video.app.search.utils.XUtilLog;
import com.anzhuo.video.app.utils.HttpUtils;
import com.anzhuo.video.app.utils.ViewUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by wbb on 2016/8/2.
 * 图片浏览 详情界面
 */
public class PictureDetailsActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private Intent mIntent;
    private List<PictureDetailInfo> PList;
    private List<ItemListInfo> itemList;
    private ArrayList<View> imageInfos;
    private MyViewPager picturePager;
    private ViewPagerAdapter adapter;
    private TextView nextSetOf;
    private ImageView download_img;
    private ImageView imgDetailCollection;
    private RelativeLayout functionLayout;
    private ProgressDialog loadingDialog;

    private TimerTask mTask;
    private int currentPoi = 0;
    private int collectionPoi = 0;
    private boolean isScrolled = false;
    private PhotoView pvImg;
    private final int viewId = R.layout.view_image_layout;

    private String listId = "0";
    private String categoryId = "0";
    private String is_Collection = "0";
    private String PicTitle = "";

    @Override
    protected int getLayoutRes() {
        int id = 0;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            id = R.layout.picture_detail_layoutl;
            return id;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            id = R.layout.picture_detail_layoutp;
            return id;
        }
        return id;
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        mIntent = getIntent();
        PList = new ArrayList<>();
        functionLayout = findView(R.id.bottom_function_layout);
        loadingDialog = new ProgressDialog(mContext);
        loadingDialog.setMessage("下载中...");

        collectionPoi = mIntent.getIntExtra("POI", 0);
        listId = mIntent.getStringExtra("LIST_ID");
        categoryId = mIntent.getStringExtra("CATEGORY_ID");
        is_Collection = mIntent.getStringExtra("ISCOLLECTION");
        nextSetOf = findViewAttachOnclick(R.id.next_set_of);
        itemList = (List<ItemListInfo>) mIntent.getSerializableExtra("LISTPIC");

        findViewAttachOnclick(R.id.img_detail_back);//返回
        imgDetailCollection = findViewAttachOnclick(R.id.img_detail_collection);//收藏
        imgDetailCollection.setBackgroundResource(R.drawable.heart);
        download_img = findViewAttachOnclick(R.id.img_detail_download);//下载
        if (is_Collection.equals("0")) {
            getPhotosData(categoryId, listId);
        } else {
            ViewUtil.gone(imgDetailCollection);
            getCollectionList();
        }
        TimerTask();
    }

    private void TimerTask() {
        mTask = new TimerTask() {
            public void run() {
                //execute the task
                handler.sendEmptyMessage(11);
            }
        };
        Timer timer = new Timer();
        timer.schedule(mTask, 5000);
    }

    private void initInfos(LayoutInflater inflater, int size) {
        View item = null;
        for (int i = 0; i < size; i++) {
            item = inflater.inflate(viewId, null);
            pvImg = findView(item, R.id.imageid);
//            // 启用图片缩放功能
//            pvImg.enable();
            pvImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
            CircleProgressBar cb = findView(item, R.id.progress1);
            final int finalI = i;
            pvImg.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    XUtilLog.log_i("wbb", "=======[点击了]=======:");
                    if (is_Collection.equals("0")){
                        if (PList.size() == (finalI + 1)) {
                            mTask.cancel();
                            ActivityJump.getInstance().JumpBuyPurchase(mContext, PList.get(finalI));
                        } else {
                            mTask.cancel();
                            ViewUtil.visible(functionLayout);
                            TimerTask();
                        }
                    }else {
                        mTask.cancel();
                        ViewUtil.visible(functionLayout);
                        TimerTask();
                    }
                }

                @Override
                public void onOutsidePhotoTap() {
                }
            });
            ImageLoaderManager.displayImageByUrl(mContext, pvImg, cb,functionLayout, PList.get(i).getFileurl());
//            mAttacher = new PhotoViewAttacher(pvImg);
//            mAttacher.update();
            this.imageInfos.add(item);
        }
    }

    private void getCollectionList() {
        List<PictureDetailInfo> p_list = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            PictureDetailInfo info = new PictureDetailInfo();
            info.setFileurl(itemList.get(i).getPicture());
            info.setId(itemList.get(i).getId());
            p_list.add(info);
        }
        PList.clear();
        PList.addAll(p_list);
        picturePager = findView(R.id.picture_pager);
        imageInfos = new ArrayList<>();
        adapter = new ViewPagerAdapter(imageInfos);
        picturePager.setAdapter(adapter);
        initInfos(LayoutInflater.from(mContext), PList.size());
        picturePager.setCurrentItem(collectionPoi);
        picturePager.setOnPageChangeListener(PictureDetailsActivity.this);
        picturePager.setOffscreenPageLimit(5);
//        picturePager.startAuto(3,3);
        ViewUtil.visible(functionLayout);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        picturePager.setAutoEnable(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        picturePager.setAutoEnable(false);
    }

    /**
     * 获取 图片集数据
     *
     * @param categoryid
     * @param itemid
     */
    private void getPhotosData(String categoryid, String itemid) {
        NIManage.PhotoCollections(categoryid, itemid, new MyInterface() {
            @Override
            public void onError(Call call, Exception e) {
//                ViewUtil.gone(cpb);
            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                if (state == 200) {
                    try {
                        List<PictureDetailInfo> temp = JSONArray.parseArray(data, PictureDetailInfo.class);
                        if (temp == null || temp.size() == 0) {
                        } else {
                            PList.clear();
                            PList.addAll(temp);
                            PicTitle = temp.get(0).getTitle();
                            listId = temp.get(0).getItemid();
                            String categoryId = temp.get(0).getCategory();
                            HashMap<String, String> map = new HashMap<>();
                            map.put("Pics_Title", PicTitle + "");
                            map.put("categoryId", categoryId + "");
                            map.put("Pics_Id", listId + "");
                            //统计图集点击被点击量
                            MobclickAgent.onEvent(mContext, "atlas_id", map);
                            picturePager = findView(R.id.picture_pager);
                            //adapter
                            imageInfos = new ArrayList<>();
                            adapter = new ViewPagerAdapter(imageInfos);
                            picturePager.setAdapter(adapter);
                            picturePager.setCurrentItem(0);
                            picturePager.setOnPageChangeListener(PictureDetailsActivity.this);
                            picturePager.setOffscreenPageLimit(5);
                            ViewUtil.visible(functionLayout);
                            initInfos(LayoutInflater.from(mContext), temp.size());
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        ToastUtil.toastLong(mContext, "数据出现异常");
                        e.printStackTrace();
                    }
                } else {
                    ToastUtil.toastLong(mContext, "数据出现异常");
                }
            }
        });
    }

    private void downloadImg(String imgUrl, String name) {
        XUtilLog.log_i("wbb", "=====[imgUrl]=====:" + imgUrl);
        XUtilLog.log_i("wbb", "=====[name]=====:" + name);
        HttpUtils.downloadNewSelf(imgUrl, new FileCallBack(FileUtils.getDownloadDir(), name) {
            @Override
            public void onError(Call call, Exception e,int id) {
                XUtilLog.log_i("wbb", "=====[Exception]=====:" + e.getMessage());
                ToastUtil.toastLong(mContext,"下载失败");
                loadingDialog.dismiss();
            }

            @Override
            public void onResponse(File response,int id) {
                XUtilLog.log_i("wbb", "=====[response]=====:" + response.toString());
                loadingDialog.dismiss();
                ToastUtil.toastLong(mContext, "图片下载成功,路径为：" + response.toString() + "");
            }

//            @Override
//            public void inProgress(float progress, long total) {
//                XUtilLog.log_i("wbb", "=====[progress]=====:" + progress);
//                XUtilLog.log_i("wbb", "=====[total]=====:" + total);
//            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_detail_collection://收藏
                if (is_Collection.equals("0")) {
                    if (PList == null || PList.size() == 0) {
                        ToastUtil.toastLong(mContext, "图片数据异常");
                    } else {
                        String id = PList.get(currentPoi).getId();
                        List<ItemListInfo> cInfo = DatabaseUtils.getCollection(id);
                        if (cInfo == null || cInfo.size() == 0) {//没有收藏状态
                            ItemListInfo info = new ItemListInfo();
                            info.setId(PList.get(currentPoi).getId());
                            info.setCategory(PList.get(currentPoi).getCategory());
                            info.setPicture(PList.get(currentPoi).getFileurl());
                            info.setIsShould("0");
                            info.setIsChecked("0");
                            info.save();
                            imgDetailCollection.setBackgroundResource(R.drawable.heart_select);
                            ToastUtil.toastLong(mContext, "收藏成功");
                        } else {
                            ToastUtil.toastLong(mContext, "该图已收藏");
                        }
                    }
                }
                break;
            case R.id.img_detail_download://下载
                if (PList != null || PList.isEmpty() || PList.size() != 0) {
                    loadingDialog.show();
                    downloadImg(PList.get(currentPoi).getFileurl(), PList.get(currentPoi).getFilename());
                }
                break;
            case R.id.next_set_of://下一组
                XUtilLog.log_i("wbb", "已经是最后一页了");
                int id = Integer.parseInt(listId);
                int id2 = id - 1;
                listId = id2 + "";
                getPhotosData(categoryId, id2 + "");
                isScrolled = true;
                break;
            case R.id.img_detail_back://返回
                onBackPressed();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    DisappearAnimation();
                    break;
            }
        }
    };

    AlphaAnimation disappearAnimation;

    private void DisappearAnimation() {
        disappearAnimation = new AlphaAnimation(1, 0);
        disappearAnimation.setDuration(300);
        functionLayout.startAnimation(disappearAnimation);
        disappearAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ViewUtil.gone(functionLayout);
                pvImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPoi = position;
        if (mTask != null) {
            mTask.cancel();
        }
        ViewUtil.gone(functionLayout);
        List<ItemListInfo> cInfo = DatabaseUtils.getCollection(PList.get(position).getId());
        if (cInfo == null || cInfo.size() == 0) {
            imgDetailCollection.setBackgroundResource(R.drawable.heart);
        } else {
            imgDetailCollection.setBackgroundResource(R.drawable.heart_select);
        }
        if (PList != null || PList.size() != 0) {
            if (is_Collection.equals("0")){
                int poi = (PList.size() - 1);
                if (poi == position) {
                    ViewUtil.invisible(imgDetailCollection, download_img);
                    ViewUtil.visible(functionLayout, nextSetOf);
                    mTask.cancel();
                } else {
                    ViewUtil.visible(imgDetailCollection, download_img);
                    ViewUtil.gone(nextSetOf);
                }
            }
        }

//        if (rightEdge != null && !rightEdge.isFinished()) {//到了最后一张并且还继续拖动，出现蓝色限制边条了。
//            int id = Integer.parseInt(listId);
//            int id2 = id - 1;
//            listId = id2 + "";
//            getPhotosData(categoryId, id2 + "");
//        }
//        if (leftEdge != null && !leftEdge.isFinished()) {//到了最左边一张并且还继续拖动，出现蓝色限制边条了。
//            int id = Integer.parseInt(listId);
//            int id2 = id + 1;
//            listId = id2 + "";
//            int lid1 = Integer.parseInt(localId);
//            if (lid1 < id2) {
//                ToastUtil.toastLong(mContext,"已经到第一图集了");
//            } else {
//                getPhotosData(categoryId, id2 + "");
//            }
//          }

    }


    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                isScrolled = false;
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                isScrolled = true;
                break;
            case ViewPager.SCROLL_STATE_IDLE:
//                if (picturePager.getCurrentItem() == picturePager.getAdapter().getCount() - 1 && !isScrolled) {
//                    XUtilLog.log_i("wbb", "已经是最后一页了");
//                    int id = Integer.parseInt(listId);
//                    int id2 = id - 1;
//                    listId = id2 + "";
//                    getPhotosData(categoryId, id2 + "");
//                }
//                isScrolled = true;
                break;
        }
    }

}
