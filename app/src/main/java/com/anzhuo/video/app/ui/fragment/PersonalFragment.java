package com.anzhuo.video.app.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.constant.LoginConfig;
import com.anzhuo.video.app.manager.ActivityMy;
import com.anzhuo.video.app.model.NewInterface;
import com.anzhuo.video.app.model.bean.LoginData;
import com.anzhuo.video.app.model.home.HomeModel;
import com.anzhuo.video.app.ui.base.BaseFragment;
import com.anzhuo.video.app.utils.DataCleanManager;
import com.anzhuo.video.app.utils.EventUtil;
import com.anzhuo.video.app.utils.ToastUtil;
import com.anzhuo.video.app.utils.XUtilNet;
import com.anzhuo.video.app.utils.permission.PermissionCallBack;
import com.anzhuo.video.app.utils.permission.PermissionUtil;
import com.anzhuo.video.app.utils.tempPhotoPick.TempPKHandler;
import com.anzhuo.video.app.utils.tempPhotoPick.TempPKHelper;
import com.anzhuo.video.app.utils.tempPhotoPick.TempPKParams;
import com.anzhuo.video.app.widget.StateView;
import com.anzhuo.video.app.widget.ViewUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;
import com.rey.material.app.BottomSheetDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Created by Administrator on 2017/3/8/008.
 */

public class PersonalFragment extends BaseFragment implements PermissionCallBack,TempPKHandler {

    private LinearLayout mCollectLaout, mClearLayout, mContainer;
    private TextView mCacheSize;
    private TextView mPhone, mVipTime, mInsertMoney, mName;
    private ImageView mNameEdit;
    private Button mEXit;
    private SimpleDraweeView mDraweeView;
    private boolean isVip = false;

    private StateView mStateView;
    private BottomSheetDialog mDialog;
    private TempPKParams params;

    public static PersonalFragment newInstance() {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
//        return 0;
        return R.layout.fragment_personal_center_layout;
    }

    @Override
    public void bindViews() {

        mStateView = new StateView(getContext())
                .showLoading();

        EventBus.getDefault().register(this);
        mCollectLaout = ViewUtil.findView(mRootView, R.id.personal_collect_layout);
        mClearLayout = ViewUtil.findView(mRootView, R.id.personal_clear_cache_layout);
        mCacheSize = ViewUtil.findView(mRootView, R.id.cache_size);
        mName = ViewUtil.findView(mRootView, R.id.name_edit_content);
        mNameEdit = ViewUtil.findView(mRootView, R.id.name_edit_btn);
        mPhone = ViewUtil.findView(mRootView, R.id.personal_phone);
        mVipTime = ViewUtil.findView(mRootView, R.id.vip_time);
        mInsertMoney = ViewUtil.findView(mRootView, R.id.vip_time_btn);
        mEXit = ViewUtil.findView(mRootView, R.id.btn_exit);
        mDraweeView = ViewUtil.findView(mRootView, R.id.main_sdv);
        mContainer = ViewUtil.findView(mRootView, R.id.view_container);

        mCollectLaout.setOnClickListener(mClickListener);
        mClearLayout.setOnClickListener(mClickListener);
        mEXit.setOnClickListener(mClickListener);
        mInsertMoney.setOnClickListener(mClickListener);
        mNameEdit.setOnClickListener(mClickListener);
        mName.setOnClickListener(mClickListener);
        mDraweeView.setOnClickListener(mClickListener);
        CheckCacheSize();

        params = new TempPKParams();
        params.compress = true;

    }

    //开启新线程检查缓存
    private void CheckCacheSize() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                File file = getContext().getExternalCacheDir();
                File file = getContext().getCacheDir();
                try {
                    final String mSize = DataCleanManager.getCacheSize(file);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mCacheSize.setText(mSize);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 获取用户详情
     *
     * @param s
     * @param phone
     * @param uid
     * @param reg_site
     */
    private void getUserInfo(String s, String phone, String uid, String reg_site) {

        if (XUtilNet.isWifiConnected()) {

            HomeModel.getUserInfo(s, phone, uid, reg_site, new NewInterface() {
                @Override
                public void onError(Call call, Exception e) {
                    if (mStateView!=null)
                    mStateView.showNoNet();//加载失败
                }

                @Override
                public void onSucceed(int state, String data, JSONObject obj) {
                    if (state == 200) {
                        List<LoginData> datas = JSON.parseArray(data, LoginData.class);
                        if (datas != null && !datas.isEmpty()) {
                            LoginData loginBean = datas.get(0);

                            mName.setText(loginBean.getUsername());
                            mPhone.setText("电话号码：" + loginBean.getPhone());
                            if (loginBean.getVip_level().equals("0")) {
                                isVip = false;
                                mVipTime.setText("会员期限：您当前不是会员");
                                mInsertMoney.setText("加入会员");
                            } else {
                                isVip = true;
                                mVipTime.setText("会员期限：" + loginBean.getVip_expire());
                                mInsertMoney.setText("续费");
                            }

                            if (loginBean.getLogo() != null && !TextUtils.isEmpty(loginBean.getLogo())) {
                                //创建将要下载的图片的URI
                                Uri imageUri = Uri.parse(loginBean.getLogo());
                                //开始下载
                                mDraweeView.setImageURI(imageUri);
                                //创建DraweeController
                                DraweeController controller = Fresco.newDraweeControllerBuilder()
                                        //加载的图片URI地址
                                        .setUri(imageUri)
                                        //设置点击重试是否开启
                                        .setTapToRetryEnabled(true)
                                        //设置旧的Controller
                                        .setOldController(mDraweeView.getController())
                                        //构建
                                        .build();
                                //设置DraweeController
                                mDraweeView.setController(controller);
                            }

                        }else {
                            if (mStateView!=null)
                            mStateView.showNoData();
                        }


                    } else {
                        mName.setText(LoginConfig.getUserName());
                        mPhone.setText("电话号码：" + LoginConfig.getUserPhone());
                        mVipTime.setText("会员期限：" + LoginConfig.getUserExprice());
                        Uri imageUri = Uri.parse(LoginConfig.getUserLogo());
                        mDraweeView.setImageURI(imageUri);
                    }
                    if (mStateView!=null)
                    mStateView.showSuccess();

                }
            });


        } else {
            if (mStateView!=null)
            mStateView.showNoNet();//加载失败
        }

    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.personal_collect_layout:
                    Logger.i("-------------onClick--------------------");
                    ActivityMy.startCollcetActivity(getActivity(), null);
                    break;
                case R.id.personal_clear_cache_layout:
                    boolean isSuccess = DataCleanManager.cleanInsideCache(getContext());
                    if (isSuccess) {
                        CheckCacheSize();
                        ToastUtil.showToast("清除缓存成功！");
                    } else {
                        ToastUtil.showToast("清除缓存失败，请重试！");
                    }


                    break;
                case R.id.btn_exit://退出逻辑  登录状态FALSE 跳到首页
                    EventBus.getDefault().post(new EventUtil.EnventExit());
                    break;
                case R.id.name_edit_btn:
                    showEditNamePop(getContext(), getLayoutRes(), "修改昵称", "取消", "保存");
                    break;
                case R.id.name_edit_content:
                    showEditNamePop(getContext(), getLayoutRes(), "修改昵称", "取消", "保存");
                    break;
                case R.id.vip_time_btn:
                    if (isVip) {
                        sendContinueVipEvent();
                    } else {
                        sendJoinVipEvent();
                    }
                    break;
                case R.id.main_sdv://头像选择
                    initDialog();
                    break;

                //选择图片开始
                case R.id.pop_quit_layout:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }

                    break;
                case R.id.pop_take_pic_layout:
                    getPermisson();
                    break;
                case R.id.pop_choose_pic_layout:
                    startActivityForResult(TempPKHelper.makeGalleryIntent(params), TempPKHelper.TEMP_REQUEST_CODE_GALLERY);

                    break;
                //选择图片结束
            }
        }
    };

    @Override
    public void onLazyLoad() {

    }

    private void sendJoinVipEvent() {
        EventUtil.EnventJoinVip enventJoinVip = new EventUtil.EnventJoinVip();
        EventBus.getDefault().post(enventJoinVip);

    }

    private void sendContinueVipEvent() {

        EventUtil.EnventContinueVip enventContinueVip = new EventUtil.EnventContinueVip();
        EventBus.getDefault().post(enventContinueVip);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_collect_layout:
                Logger.i("-------------onClick--------------------");
//                ActivityMy.startCollcetActivity(getActivity(),null);
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showJoinVipPop(EventUtil.EnventLoginInfo event) {

        Logger.e("------event.toLoginInfo---获取推送-" + event.toLoginInfo);
//        getUserInfo("", LoginConfig.getUserPhone(),event.toLoginInfo,"10");

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        getUserInfo("", LoginConfig.getUserPhone(), LoginConfig.getUserId(), "10");
    }

    private PopupWindow mLoginPop;

    private void showEditNamePop(Context context, int rootlayout, String title, String leftBtn, String rightBtn) {
        //设置contentView
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_common_edit_layout, null);
        ((TextView) (contentView.findViewById(R.id.dialog_title))).setText(title);
        final EditText mName = (EditText) (contentView.findViewById(R.id.dialog_notice_content));
        ((Button) (contentView.findViewById(R.id.dialog_btn_left))).setText(leftBtn);
        ((Button) (contentView.findViewById(R.id.dialog_btn_right))).setText(rightBtn);
        mLoginPop = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mLoginPop.setContentView(contentView);
        mLoginPop.setBackgroundDrawable(new ColorDrawable(0x8f000000));
        mLoginPop.setOutsideTouchable(true);
        mName.setText(this.mName.getText().toString()+"");
        //显示PopupWindow
        View rootview = LayoutInflater.from(context).inflate(rootlayout, null);
        mLoginPop.showAtLocation(rootview, Gravity.CENTER, 0, 0);
        ((Button) (contentView.findViewById(R.id.dialog_btn_left))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginPop != null && mLoginPop.isShowing())
                    mLoginPop.dismiss();
            }
        });

        ((Button) (contentView.findViewById(R.id.dialog_btn_right))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//修改昵称
                String _name = mName.getText().toString();
                if (_name != null && !TextUtils.isEmpty(_name)) {
                    setUserName("", LoginConfig.getUserPhone(), LoginConfig.getUserId(), _name);
                } else {
                    ToastUtil.showToast("昵称不能为空！");
                }


            }
        });

    }


    /**
     * 修改昵称
     *
     * @param s
     * @param phone
     * @param uid
     * @param name
     */
    private void setUserName(String s, String phone, String uid, final String name) {

        HomeModel.setUserName(s, phone, uid, name, new NewInterface() {
            @Override
            public void onError(Call call, Exception e) {

                ToastUtil.showToast("操作失败，稍后请重试！");

            }

            @Override
            public void onSucceed(int state, String data, JSONObject obj) {
                if (state == 200) {
                    mName.setText(name);
                    LoginConfig.sf_saveUserName(name);
                    ToastUtil.showToast("操作成功");

                    if (mLoginPop != null && mLoginPop.isShowing()) {
                        mLoginPop.dismiss();
                        mLoginPop = null;
                    }
                }

            }
        });

    }

    //根据下权限选择处理
    private void getPermisson(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtil.getPermission(getActivity(),this, Manifest.permission.CAMERA);
        }else{
            startActivityForResult(TempPKHelper.makeCaptureIntent(params), TempPKHelper.TEMP_REQUEST_CODE_CAMERA);
        }

    }

    //图片处理成功返回路径uri
    @Override
    public void onSucceed(Uri uri) {
        Logger.e("----onSucceed-----"+uri.getPath());
        upLoadFile(new String[]{uri.getPath()});
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

    }
    //图片处理取消
    @Override
    public void onCancel() {
        Logger.e("----onCancel-----");
    }
    //图片处理失败
    @Override
    public void onFailed(String message) {
        Logger.e("----onFailed-----"+message);
    }

    @Override
    public TempPKParams getPkParams() {
        return params;
    }

    public Activity getContext(){
        return getActivity();
    }

    @Override
    public void PermissionSuccess() {
        getPermisson();

    }

    @Override
    public void PermissionFail() {
        showCameraDialog(getActivity());

    }


    /**
     * 上传图片
     *
     * @param filePath
     */
    private void upLoadFile(String[] filePath) {
        Map<String, RequestBody> map = new HashMap<>();
        for (int i = 0; i < filePath.length; i++) {
            Logger.e("filePath" + i + " :" + filePath[i]);
            File currentFile = new File(filePath[i]);
            if (currentFile.exists()) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), currentFile);
                String[] arraypath = filePath[i].split("/");
                String name = arraypath[arraypath.length - 1];
                Logger.e("file name=" + name);
                map.put("image\"; filename=\"" + name, fileBody);
            } else {
                ToastUtil.showToast(filePath + "文件不存在");
            }
        }
//        mPreMineI.addFile(map); //TODO 接口

    }


    /**
     * 选择照片
     */
    private void initDialog() {

        mDialog = new BottomSheetDialog(getActivity());
        View view = getLayoutInflater(getArguments()).inflate(R.layout.temp_photo_pk_layout, null);
        mDialog.contentView(view).cancelable(true).canceledOnTouchOutside(true).show();

        view.findViewById(R.id.pop_quit_layout).setOnClickListener(mClickListener);
        view.findViewById(R.id.pop_take_pic_layout).setOnClickListener(mClickListener);
        view.findViewById(R.id.pop_choose_pic_layout).setOnClickListener(mClickListener);


    }

    /**
     * 权限提示
     * @param context
     */
    private  void showCameraDialog(final Activity context) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
        mMaterialDialog.setTitle("提示");
        mMaterialDialog.setMessage("由于部分功能需要相关权限，请授权！");
        mMaterialDialog.setPositiveButton("去授权", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        mMaterialDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        mMaterialDialog.show();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TempPKHelper.onResult(this, requestCode, resultCode, data);//图库图片处理回调
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (params!=null){
            params=null;
        }

    }
}
