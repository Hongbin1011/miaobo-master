package com.anzhuo.video.app.meinv.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.config.VideoApplication;
import com.anzhuo.video.app.constant.Constants;
import com.anzhuo.video.app.meinv.base.BaseActivity;
import com.anzhuo.video.app.meinv.entity.ItemListInfo;
import com.anzhuo.video.app.meinv.load.ImageLoaderManager;
import com.anzhuo.video.app.meinv.manager.ActivityJump;
import com.anzhuo.video.app.meinv.tool.HorizontalDividerItemDecoration;
import com.anzhuo.video.app.meinv.utils.DatabaseUtils;
import com.anzhuo.video.app.meinv.view.ViewTipModule;
import com.anzhuo.video.app.search.utils.ToastUtil;
import com.anzhuo.video.app.search.utils.XUtilLog;
import com.anzhuo.video.app.utils.ViewUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbb on 2016/8/2.
 * 我的收藏 详情界面
 */
public class MyCollectionActivity extends BaseActivity implements View.OnClickListener {

    private CommonAdapter<ItemListInfo> adapter;
    private List<ItemListInfo> datas;

    private RecyclerView myCollectionList;
    private Button deleteImg;

    private boolean isSelect = false;

    private ViewTipModule module;
    private RelativeLayout collectionMainLayout;
    private int deleteSelectNum = 0;

    @Override
    protected int getLayoutRes() {
        return R.layout.my_collection_layout;
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        setHeadTitle("我的收藏");
        setRightImg(R.drawable.editor_collection);
        collectionMainLayout = findView(R.id.collection_main_layout);
        deleteImg = findViewAttachOnclick(R.id.delete_imgs);
        datas = new ArrayList<>();
        myCollectionList = findView(R.id.my_collection_list);
        myCollectionList.setLayoutManager(new GridLayoutManager(mContext, 2));
        myCollectionList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.translucent_bg))
                .size(4)
                .build());

        module = new ViewTipModule(this, collectionMainLayout, myCollectionList);
        MVPictureAdapter();
        reload();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void MVPictureAdapter() {
        adapter = new CommonAdapter<ItemListInfo>(mContext, R.layout.picture_item_layout, datas) {
            @Override
            protected void convert(ViewHolder holder, ItemListInfo info, int position) {
                LinearLayout pictureLayout = holder.getView(R.id.picture_item_main_layout);
                ImageView pictureImg = holder.getView(R.id.picture_img);
                LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, (Constants.Phigh / 2) - 110);
                linearParams.setMargins(8, 8, 8, 8);
                pictureLayout.setLayoutParams(linearParams);
                ImageLoaderManager.displayImageByUrl(VideoApplication.getContext(), pictureImg, info.getPicture());
                holder.setText(R.id.picture_num, info.getNum());

                CheckBox cb = holder.getView(R.id.checkbox_img);
                XUtilLog.log_i("wbb", "======[info.isShould()]======:" + info.getIsShould());
                cb.setVisibility(info.getIsShould().equals("1") ? View.VISIBLE : View.GONE);
                cb.setChecked(info.getIsChecked().equals("1"));
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (datas.get(position).getIsShould().equals("1")) {
                    datas.get(position).setAutoChedck();
                    boolean isSelect = datas.get(position).getIsChecked().equals("1");
                    if (isSelect) {
                        deleteSelectNum = deleteSelectNum + 1;
                        handler.sendEmptyMessage(1);
                    } else {
                        deleteSelectNum = deleteSelectNum - 1;
                        handler.sendEmptyMessage(1);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    //跳转到图片详情界面
                    ActivityJump.getInstance().PictureDetails(mContext, "1", position, datas);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        myCollectionList.setAdapter(adapter);
    }

    private void reload() {
        datas.clear();
        requestData();
    }

    /**
     * 请求收藏数据
     */
    private void requestData() {
        List<ItemListInfo> listInfos = DatabaseUtils.getCollectionAll();
        if (listInfos == null || listInfos.size() == 0) {
            module.showNoDataState("暂时没有收藏数据");
        } else {
            module.showSuccessState();
            datas.clear();
            datas.addAll(listInfos);
            adapter.notifyDataSetChanged();
            ViewUtil.gone(deleteImg);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_back://返回
                onBackPressed();
                break;
            case R.id.head_right_img://编辑删除
                if (datas == null || datas.size() == 0) {
                    ToastUtil.toastLong(mContext, "没有收藏图集");
                } else {
                    if (isSelect) {//显示 CheckBox 了
                        for (int i = 0; i < datas.size(); i++) {
                            datas.get(i).setIsShould("0");
                            datas.get(i).setIsChecked("0");
                            isSelect = false;
                        }
                    } else {
                        for (int i = 0; i < datas.size(); i++) {
                            datas.get(i).setIsShould("1");
                            isSelect = true;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.delete_imgs://删除选中的图片集
                for (int i = 0; i < datas.size(); i++) {
                    datas.get(i).setIsShould("0");
                    if (datas.get(i).getIsChecked().equals("1")) {
                        datas.get(i).delete();
                    }
                }
                ViewUtil.gone(deleteImg);
                requestData();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    if (deleteSelectNum <= 0) {
                        ViewUtil.gone(deleteImg);
                    } else {
                        ViewUtil.visible(deleteImg);
                    }
                    break;

                default:
                    break;
            }
        }
    };
}
