package com.anzhuo.video.app.manager.fuli;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.anzhuo.video.app.manager.CameraInterface;
import com.anzhuo.video.app.manager.fuli2.x5.X5WebViewFragment;

/**
 * Created by husong on 2016/9/29.
 */
public class X5WebViewJSFunction implements WebViewJavaScriptFunction {

    private Context mContext;
    private Dialog progressDialog;
    private Handler mHandler;
    private Activity mActivity;
    public static final int REQUEST_FLASHLIGHT_PERMISSION = 111; //相机权限
    public static final int REQUEST_LOACTION_PERMISSION = 112; //定位权限

    private PermissionsChecker mPermissionsChecker;
    private final String[] PERMISSIONS = new String[]{
             Manifest.permission.CAMERA,
//            android.Manifest.permission.FLASHLIGHT,
            Manifest.permission.VIBRATE
    };

    public X5WebViewJSFunction(Context context, Handler handler, Activity activity) {
        this.mContext = context;
        this.mHandler=handler;
        this.mActivity= activity;
        mPermissionsChecker = new PermissionsChecker(activity);
    }

    @Override
    public void onJsFunctionCalled(String tag) {
        Log.e("husong", "--------->>call :  "+tag);
    }

    /**
     * Description 打开/关闭手电筒
     * UpdateUser husong.
     * UpdateDate 2016/9/29 16:41.
     * Version 1.0
     */
    @JavascriptInterface
    public void flashLight() {
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            MyPermissionsActivity.startActivityForResult(mActivity, REQUEST_FLASHLIGHT_PERMISSION, PERMISSIONS);
        } else {
            String request_h5 = CameraInterface.getInstance().flashLight(mContext);
            Message msg=new Message();
            msg.what= X5WebViewFragment.MSG_REQUEST_H5_LED;
            msg.obj= request_h5;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * Description 由js调起定位
     * UpdateUser husong.
     * UpdateDate 2016/9/30 16:36.
     * Version 1.0
     */
    @JavascriptInterface
    public void startaLaction() {
//        String permissions[] = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
//        if(mPermissionsChecker.lacksPermissions(permissions)){
//            MyPermissionsActivity.startActivityForResult(mActivity, REQUEST_LOACTION_PERMISSION, permissions);
//            return;
//        }
//        progressDialog = ProgressDialog.createProgressDialog(mContext, "正在定位...", null);
//        progressDialog.show();
//        AMapInterface aMapInterface = new AMapInterface(mContext, locationListener);
//        aMapInterface.startLocation();

        String result = DefaultSharePrefManager.getString(SharePrefConstant.KEY_LOCATION, null);
        if(!TextUtils.isEmpty(result)) {
            Message msg = new Message();
            msg.what = X5WebViewFragment.MSG_REQUEST_H5_LOCATION;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
//        AMapInterface aMapInterface = new AMapInterface(mContext, locationListener);
//        aMapInterface.startLocation();
    }

    public void destroy() {
        CameraInterface.getInstance().destroy();
    }


//
//    /**
//     * Description 将设备信息以json格式返给js
//     * UpdateUser husong.
//     * UpdateDate 2016/9/30 16:37.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public String getDeviceMessage(){
//        return DeviceInterface.getInstance().getDeviceMessage(mContext);
//    }
//
//    /**
//     * Description 让h5调起检查联系人的权限
//     *
//     * UpdateUser husong.
//     * UpdateDate 2016/10/23 11:31.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void checkContactsPermissons(){
//        String permissions[] = new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS};
//        if(mPermissionsChecker.lacksPermissions(permissions)){
//            MyPermissionsActivity.startActivityForResult(mActivity, -99, permissions);
//            return;
//        }
//    }
//
//    /**
//     * Description 获取所有手机联系人
//     * UpdateUser husong.
//     * UpdateDate 2016/10/9 16:40.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public String getAllContacts(){
//        List<ContactInfo> list = ContactsManager.getInstance().getPhoneContacts(mContext);
//        list.addAll(ContactsManager.getInstance().getSIMContacts(mContext));
//        String result = JSON.toJSONString(list);
//        Log.d("husong", "备份："+result);
//        return result;
//    }
//
//    /**
//     * Description 保存联系人
//     * UpdateUser husong.
//     * UpdateDate 2016/10/9 16:40.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public Boolean saveContact(String contact){
//        ContactInfo cinfo = JSON.parseObject(contact, ContactInfo.class);
//        ContactsManager.getInstance().saveContactInfo(cinfo, mContext);
//        return true;
//    }
//
//    /**
//     * Description 批量增加联系人
//     * UpdateUser husong.
//     * UpdateDate 2016/10/20 17:33.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public Boolean saveContacts(String contacts){
//        List<ContactInfo> cinfos = JSON.parseArray(contacts, ContactInfo.class);
//        for (ContactInfo ci : cinfos){
//            ContactsManager.getInstance().saveContactInfo(ci, mContext);
//        }
//        ToastUtil.toastShort(mContext, "All Contacts add!");
//        return true;
//    }
//
//    /**
//     * Description 删除联系人
//     * UpdateUser husong.
//     * UpdateDate 2016/10/9 16:41.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public Boolean deleteContact(String rawContactId){
//        try{
//            ContactsManager.getInstance().deleteContact(Long.parseLong(rawContactId), mContext);
//        }catch (Exception e){
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * Description 更新联系人
//     * UpdateUser husong.
//     * UpdateDate 2016/10/9 16:41.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void updateContact(String json){
//        ContactInfo cinfo = JSON.parseObject(json, ContactInfo.class);
//        ContentValues values = new ContentValues();
//        if(!TextUtils.isEmpty(cinfo.getContactName())) {
//            values.put(StructuredName.GIVEN_NAME, cinfo.getContactName());
//            values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
//            ContactsManager.getInstance().updateContact(mContext, cinfo.getContactid(), values, StructuredName.CONTENT_ITEM_TYPE);
//        }
//
//        if(!TextUtils.isEmpty(cinfo.getPhoneNumber())) {
//            values.clear();
//            values.put(Phone.NUMBER, cinfo.getPhoneNumber());
//            values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
//            values.put(Phone.TYPE, Phone.TYPE_WORK_MOBILE);
//            ContactsManager.getInstance().updateContact(mContext, cinfo.getContactid(), values, Phone.CONTENT_ITEM_TYPE);
//        }
//
//        if(cinfo.getContactPhoto()!=null){
//            values.clear();
//            values.put(Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
//            values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, BitmapUtil.Bitmap2Bytes(cinfo.getContactPhoto()));
//            ContactsManager.getInstance().updateContact(mContext, cinfo.getContactid(), values, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
//        }
//    }
//
//    /**
//     * Description 还原联系人
//     * UpdateUser husong.
//     * UpdateDate 2016/10/21 16:52.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void reductionContact(String contacts){
//        Log.d("husong", "还原："+contacts);
//        ContactsManager.getInstance().reductionContact(mContext, contacts);
//    }
//
//    /**
//     * Description 拍照
//     * UpdateUser husong.
//     * UpdateDate 2016/10/10 9:55.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void takePicture(){
//        File f=new File(FileUtils.ROOT_DIR+ File.separator);
//        CameraInterface.getInstance().showCameraAction(mActivity, null);
//    }
//
//    /**
//     * Description 开始录像
//     * UpdateUser husong.
//     * UpdateDate 2016/10/10 14:16.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void startRecorder(){
//        //PageSwitcher.switchToPage(mContext, FragmentFactory.FRAGMENT_TYPE_MEDIA_RECORD);
//        CameraInterface.getInstance().showSystemVideo(FileUtils.getFilePath(), mActivity);
//    }
//
//    /**
//     * Description 二维码扫描
//     * UpdateUser husong.
//     * UpdateDate 2016/10/10 16:46.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void scanQRCode(){
//        //TODO 此处需求h5提供扫描完成的回调
//        PageSwitcher.switchToPage(mContext, FragmentFactory.FRAGMENT_TYPE_CAPTURE);
//    }
//
//    /**
//     * Description 生成二维码
//     * UpdateUser husong.
//     * UpdateDate 2016/10/10 18:49.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void getQRCode(String content){
//        try{
//            Bitmap bm = BitmapUtils.cretaeBitmap(content, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logo));
////            Message msg = new Message();
////            msg.what=3;
////            msg.obj = bm;
////            mHandler.sendMessage(msg);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Description 返回
//     * UpdateUser husong.
//     * UpdateDate 2016/10/10 17:18.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void back(){
//        mActivity.finish();
//    }
//
//    /**
//     * Description 由h5调起注册gsensor监听，然后以100ms每次的频率回调h5函数，向其返回x,y,z值。
//     * UpdateUser husong.
//     * UpdateDate 2016/10/10 20:14.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void registerGsonser(){
//        AllSensorManager.getInstance().registerGsensorListener(mContext);
//        mHandler.sendEmptyMessage(AllSensorManager.REQUEST_MSG_TO_H5);
//    }
//
//    /**
//     * Description 若注册了gsensor监听，则一定要要求h5在不用时，调此函数unregister掉gsensor的监听
//     * UpdateUser husong.
//     * UpdateDate 2016/10/10 20:17.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void unregisterGsonsor(){
//        AllSensorManager.getInstance().unregisterGsensorListener(mContext);
//        if(mHandler.hasMessages(AllSensorManager.REQUEST_MSG_TO_H5))
//            mHandler.removeMessages(AllSensorManager.REQUEST_MSG_TO_H5);
//    }
//
//    /**
//     * Description 返回屏幕宽高，密度，状态栏高度
//     * UpdateUser husong.
//     * UpdateDate 2016/10/11 10:50.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public String getDeviceDisplay(){
//        return DeviceInterface.getInstance().getDeviceDisplay(mContext);
//    }
//
//    /**
//     * Description 调用录音
//     * UpdateUser husong.
//     * UpdateDate 2016/10/11 15:23.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public boolean startAudioRecord(){
//        AudioManager.getInstance().showAudioRecord(mContext);
//        return true;
//    }
//
//    /**
//     * Description 暂停录音
//     * UpdateUser husong.
//     * UpdateDate 2016/10/13 15:48.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public boolean pasuAudioRecord(){
//        AudioManager.getInstance().pauseRecord();
//        return true;
//    }
//
//    /**
//     * Description 停止录音
//     * UpdateUser husong.
//     * UpdateDate 2016/10/12 19:35.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public String stopRecord(){
//        AudioManager.getInstance().pauseRecord();
//        return AudioManager.getInstance().stopRecord();
//    }
//
//    /**
//     * Description 调用系统播放器播放本地音频
//     * UpdateUser husong.
//     * UpdateDate 2016/10/11 15:30.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void setplayAudio(String filepath){
//        File file =new File(filepath);
//        if(file.exists())
//            AudioManager.getInstance().playMusic(mContext, file);
//    }
//
//    /**
//     * Description 播放音频
//     * UpdateUser husong.
//     * UpdateDate 2016/10/13 15:49.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void playAudio(){
//        AudioManager.getInstance().play();
//    }
//
//    /**
//     * Description 暂停播放音频
//     * UpdateUser husong.
//     * UpdateDate 2016/10/13 15:49.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void pausePlayAudio(){
//        AudioManager.getInstance().pausePlayMusic();
//    }
//
//    /**
//     * Description 停止播放音频
//     * UpdateUser husong.
//     * UpdateDate 2016/10/13 15:49.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void stopPlayAudio(){
//        AudioManager.getInstance().stopPlayMusic();
//    }
//
//    /**
//     * Description 从系统媒体库选择图片(单选)
//     * UpdateUser husong.
//     * UpdateDate 2016/10/11 15:50.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void selectPicture(){
////        CameraInterface.getInstance().showSelectPictures(mContext);
//        //带配置
////        FunctionConfig config = new FunctionConfig.Builder()
////                .setMutiSelectMaxSize(8)
////                .setEnableEdit(false)
////                .setEnableCrop(false)
////                .setEnableCamera(false)
////                .setEnableRotate(false)
////                .setEnablePreview(false).build();
////        GalleryFinal.openGallerySingle(CameraInterface.REQUEST_SELECT_PICTURE,config, this);
//    }
//
//    /**
//     * Description 从系统媒体库选择图片(多选)
//     * UpdateUser husong.
//     * UpdateDate 2016/10/11 19:07.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void mutiSelectPicture(){
////        FunctionConfig config = new FunctionConfig.Builder()
////                .setMutiSelectMaxSize(8)
////                .setEnableEdit(false)
////                .setEnableCrop(false)
////                .setEnableCamera(false)
////                .setEnableRotate(false)
////                .setEnablePreview(false).build();
////        GalleryFinal.openGalleryMuti(23, config, this);
//    }
//
//    /**
//     * Description 获取指定目录下的所有文件(耗时操作，需要h5提供回调)
//     * UpdateUser husong.
//     * UpdateDate 2016/10/12 14:04.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void getAllRootFile(final String path){
//        progressDialog = ProgressDialog.createProgressDialog(mContext, "获取文件信息...", null);
//        progressDialog.show();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String result = IOManager.getInstance().getAllFileFromPath(path == null ? FileUtil.getRootDir(mContext) : path);
//                progressDialog.dismiss();
//                Message msg = new Message();
//                msg.what = X5WebViewFragment.MSG_REQUEST_H5_GET_FILE;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
//            }
//        }).start();
//
//    }
//
//    /**
//     * Description 打开指定的文件
//     * UpdateUser husong.
//     * UpdateDate 2016/10/12 15:05.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void openFile(String path){
//        File f = new File(path);
//        IOManager.getInstance().openFile(f, mContext);
//    }
//
//    /**
//     * Description 删除文件或文件夹
//     * UpdateUser husong.
//     * UpdateDate 2016/10/12 15:11.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public boolean deleteFile(String path){
//        return FileUtil.deleteFile(path);
//    }
//
//    /**
//     * Description 写入文件
//     * UpdateUser husong.
//     * UpdateDate 2016/10/12 15:48.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public boolean writeFile(String path, String content){
//        return FileUtil.writeFile(path, content);
//    }
//
//    /**
//     * Description 获取手机短信
//     * UpdateUser husong.
//     * UpdateDate 2016/10/12 17:48.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public String getSmsInfos(int type){
//        List<SmsInfo> list = MessageManager.getInstance().getSmsInfo(mActivity, type);
//        return JSON.toJSONString(list);
//    }
//
//    /**
//     * Description 发送短信
//     * UpdateUser husong.
//     * UpdateDate 2016/10/12 18:40.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void showSendSms(String toNumber, String body){
//        MessageManager.getInstance().sendSms(mContext, toNumber, body);
//    }
//
//
//    public void destroy() {
//        CameraInterface.getInstance().destroy();
//    }
//
//    /**
//     * 登录 QQ
//     */
//    @JavascriptInterface
//    public void LoginQQ(){
//        ThirdPartyServiceManager.getInstance().LoginQQ(listener);
//    }
//
//    /**
//     * 登录 微信
//     */
//    @JavascriptInterface
//    public void LoginWechat(){
//        ThirdPartyServiceManager.getInstance().LoginWechat(listener);
//    }
//
//    /**
//     * 登录 新浪微博
//     */
//    @JavascriptInterface
//    public void LoginSinaWeibo(){
//        ThirdPartyServiceManager.getInstance().LoginSinaWeibo(listener);
//    }
//
//    /**
//     * 得到用户信息
//     * 信息是后天接口返回的json格式数据
//     */
//    @JavascriptInterface
//    public String  getUserInformation(){
//        LoginUserInfo user = getLoginUser();
//        if(user==null){
//            PageSwitcher.switchToPage(mActivity, FragmentFactory.FRAGMENT_TYPE_USER_LOGIN);
//            return "error";
//        }
//        return  JSON.toJSONString(user);
//    }
//
//    /**
//     * 得到用户id
//     * 如果用户id = 0 表示没有登录
//     */
//    @JavascriptInterface
//    public String  getUserId(){
//        return  getString(KEY_USER_ID,"0");
//    }
//
//    /**
//     * 得到用户头像
//     * 用到头像时 最好用这个方法
//     */
//    @JavascriptInterface
//    public String  getUserHead(){
//        return  getLoginUser().getLogo();
//    }
//
//
//    /**
//     * 分享 平台（微信好友,QQ好友,微信朋友圈,新浪微博 四个平台弹窗）
//     *
//     * @param title       分享的标题
//     * @param context     分享的内容
//     * @param url         分享成功点击分享内容大开的url
//     * @param imgUrl      分享成功后 显示的图标logo
//     * @param isShareShot 是否发送截屏分享。 true = 发送截屏分享；false=发送文本网页分享。
//     */
//    @JavascriptInterface
//    public void SharePlatform(String title, String context, String url, String imgUrl, boolean isShareShot) {
//        ShareInfo info = new ShareInfo();
//        info.setTitle(title);
//        info.setContext(context);//详情
//        info.setUrl(url);
//        info.setImgUrl(imgUrl);//分享的图标
//        DialogUtils.showShareDialog(mContext, mActivity, isShareShot, info, shareListener);
//    }
//
//    /**
//     * 分享 平台（微信分享）
//     *
//     * @param title       分享的标题
//     * @param context     分享的内容
//     * @param url         分享成功点击分享内容大开的url
//     * @param imgUrl      分享成功后 显示的图标logo
//     * @param isShareShot 是否发送截屏分享。 true = 发送截屏分享；false=发送文本网页分享。
//     */
//    public void ShareWechate(String title, String context, String url, String imgUrl, boolean isShareShot){
//        ShareInfo info = new ShareInfo();
//        info.setTitle(title);
//        info.setContext(context);//详情
//        info.setUrl(url);
//        info.setImgUrl(imgUrl);//分享的图标
//        share(isShareShot, Share.wechate, info,shareListener);
//    }
//
//    /**
//     * 分享 平台（微信朋友圈分享）
//     *
//     * @param title       分享的标题
//     * @param context     分享的内容
//     * @param url         分享成功点击分享内容大开的url
//     * @param imgUrl      分享成功后 显示的图标logo
//     * @param isShareShot 是否发送截屏分享。 true = 发送截屏分享；false=发送文本网页分享。
//     */
//    public void ShareWechatMoment(String title, String context, String url, String imgUrl, boolean isShareShot){
//        ShareInfo info = new ShareInfo();
//        info.setTitle(title);
//        info.setContext(context);//详情
//        info.setUrl(url);
//        info.setImgUrl(imgUrl);//分享的图标
//        share(isShareShot,Share.WechatMoment,info,shareListener);
//    }
//
//    /**
//     * 分享 平台（新浪微博分享）
//     *
//     * @param title       分享的标题
//     * @param context     分享的内容
//     * @param url         分享成功点击分享内容大开的url
//     * @param imgUrl      分享成功后 显示的图标logo
//     * @param isShareShot 是否发送截屏分享。 true = 发送截屏分享；false=发送文本网页分享。
//     */
//    public void ShareSinaWeibos(String title, String context, String url, String imgUrl, boolean isShareShot){
//        ShareInfo info = new ShareInfo();
//        info.setTitle(title);
//        info.setContext(context);//详情
//        info.setUrl(url);
//        info.setImgUrl(imgUrl);//分享的图标
//        share(isShareShot,Share.SinaWeibos,info,shareListener);
//    }
//
//    /**
//     * 分享 平台（QQ好友分享）
//     *
//     * @param title       分享的标题
//     * @param context     分享的内容
//     * @param url         分享成功点击分享内容大开的url
//     * @param imgUrl      分享成功后 显示的图标logo
//     * @param isShareShot 是否发送截屏分享。 true = 发送截屏分享；false=发送文本网页分享。
//     */
//    public void ShareQQ(String title, String context, String url, String imgUrl, boolean isShareShot){
//        ShareInfo info = new ShareInfo();
//        info.setTitle(title);
//        info.setContext(context);//详情
//        info.setUrl(url);
//        info.setImgUrl(imgUrl);//分享的图标
//        share(isShareShot,Share.QQs,info,shareListener);
//    }
//
//    /**
//     * 分享
//     * @param isShareShot 是否截图
//     * @param shareChannel 是否发送截屏分享。 true = 发送截屏分享；false=发送文本网页分享。
//     * @param shareInfo 分享相关信息实体类
//     */
//    public void share(boolean isShareShot,String shareChannel,ShareInfo shareInfo,PlatformActionListener shareListener) {
//        ScreenShotUtil.content2Png(mContext, mActivity);
//        if (isShareShot){
//            shareInfo.setImgUrl(ScreenShotUtil.SharePicture_PATH + "1122Picture.jpg");
//            ShareUtils.Share(mContext, shareInfo, shareChannel, isShareShot,shareListener);
//        }else {
//            ShareUtils.Share(mContext, shareInfo, shareChannel, isShareShot,shareListener);
//        }
//    }
//
//    /**
//     * Description 设置壁纸
//     * UpdateUser husong.
//     * UpdateDate 2016/10/14 10:44.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void setWallPaper(String url, String fileName){
//        DeviceInterface.getInstance().SetWallPaper(mContext, url, fileName);
//    }
//
//    @JavascriptInterface
//    public void setLockWallPaper(){
//        DeviceInterface.getInstance().SetLockWallPaper(mContext, "/mnt/sdcard/PhotoNoter/s12.png");
//    }
//
//    /**
//     * Description 下载文件
//     * UpdateUser husong.
//     * UpdateDate 2016/10/14 10:44.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void downloadFile(String url, String fileName){
//        IOManager.getInstance().downloadFile(url, fileName, mHandler);
//    }
//
//    /**
//     * Description 发送邮件
//     * UpdateUser husong.
//     * UpdateDate 2016/10/14 11:04.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void sendEmail(String toEmail){
//        MessageManager.getInstance().sendEmail(mContext, toEmail);
//    }
//
//    /**
//     * Description 开启一个新的窗口
//     * UpdateUser husong.
//     * UpdateDate 2016/10/14 13:45.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void openNewWindow(String url){
//        Bundle bundle = new Bundle();
//        bundle.putString("url", url);
//        PageSwitcher.switchToPage(mContext, FragmentFactory.FRAGMENT_TYPE_X5_WEBVIEW, bundle);
//    }
//
//    /**
//     * Description 开启一个新的全屏窗口
//     * UpdateUser husong.
//     * UpdateDate 2016/11/1 19:26.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void openNewFullScreenWindow(String url){
//        Bundle bundle = new Bundle();
//        bundle.putString("url", url);
//        bundle.putString("isShowFoloat", "false");
//        PageSwitcher.switchToGamePage(mContext, FragmentFactory.FRAGMENT_TYPE_GAME_DETAIL, bundle);
//    }
//
//
//    /**
//     * Description 退出客户端程序
//     * UpdateUser husong.
//     * UpdateDate 2016/10/17 18:26.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void exitApp(){
//        RuntimeManager.getInstance().exitApp();
//    }
//
//    /**
//     * Description 重启客户端程序
//     * UpdateUser husong.
//     * UpdateDate 2016/10/17 20:11.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void reStartApp(){
//        RuntimeManager.getInstance().reStartApp(mContext);
//    }
//
//    /**
//     * Description 判断是否安装第三方应用
//     * UpdateUser husong.
//     * UpdateDate 2016/10/17 20:17.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public boolean isAppInstall(String packageName){
//        return PackageUtil.isAppInstall(mContext, packageName);
//    }
//
//    /**
//     * Description 打开第三方应用
//     * UpdateUser husong.
//     * UpdateDate 2016/10/17 20:20.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public boolean startApp(String packageName){
//        return PackageUtil.startApp(mContext, packageName);
//    }
//
//    /**
//     * Description 设置程序快捷方式上显示的提示数字
//     * UpdateUser husong.
//     * UpdateDate 2016/10/17 20:31.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void sendBadgeNumber(String number){
//        RuntimeManager.getInstance().sendBadgeNumber(mContext, number);
//    }
//
//    /**
//     * Description 压缩文件(夹)
//     * @param srcPath 要压缩的文件(夹)
//     * @param zipPath 压缩到指定的路径
//     * UpdateUser husong.
//     * UpdateDate 2016/10/18 14:15.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public boolean zipFile(String srcPath, String zipPath){
//        try {
//            File f= new File(srcPath);
//            File outZip = new File(zipPath);
//            List<File> files = new ArrayList<>();
//            files.add(f);
//
//            ZipUtil.zipFiles(files, outZip);
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * Description 解压缩文件
//     * UpdateUser husong.
//     * UpdateDate 2016/10/18 14:39.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public boolean unZipFile(String zipPath, String srcPath){
//        File zipFile = new File(zipPath);
//        ZipUtil.upZipFile(zipFile, srcPath, false);
//        return true;
//    }
//
//    /**
//     * Description 质量压缩图片
//     * @param srcPath 要压缩的图片
//     * UpdateUser husong.
//     * UpdateDate 2016/10/19 11:13.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void compressImage(final String srcPath){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Bitmap bm = BitmapUtils.readBitmap(srcPath);
//                    BitmapUtils.compressImage(bm, srcPath);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//    }
//
//    /**
//     * Description 大小压缩
//     * @param be 压缩比例
//     * UpdateUser husong.
//     * UpdateDate 2016/10/19 14:09.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void compressImageWH(final String srcPath,final int be){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    BitmapUtils.compressImage(srcPath, be);
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//    }
//
//    /**
//     * Description 旋转图片
//     * UpdateUser husong.
//     * UpdateDate 2016/10/19 15:28.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void rotateBitmap(final String path,final int degree){
//        TaskExecutor.executeTask(new Runnable() {
//            @Override
//            public void run() {
//                BitmapUtils.rotateBitmap(path, degree);
//            }
//        });
//    }
//
//    /**
//     * Description 裁剪图片
//     * UpdateUser husong.
//     * UpdateDate 2016/10/19 17:00.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void cropPicture(String filePath){
//        File f = new File(filePath);
//        Intent intent = new Intent();
//        intent.setAction("com.android.camera.action.CROP");
//        intent.setDataAndType(Uri.fromFile(f), "image/*");// mUri是已经选择的图片Uri
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);// 裁剪框比例
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 300);// 输出图片大小
//        intent.putExtra("outputY", 300);
//        intent.putExtra("return-data", true);
//        mActivity.startActivityForResult(intent, X5WebViewFragment.REQUEST_CROP_PICTURE);
//    }
//
//    @JavascriptInterface
//    public void formatPicture(final String path){
//        TaskExecutor.executeTask(new Runnable() {
//            @Override
//            public void run() {
//                BitmapUtils.formatPicture(path);
//            }
//        });
//    }
//
//    /**
//     * Description 语音识别
//     * UpdateUser husong.
//     * UpdateDate 2016/10/20 14:20.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void startSpeechRecognizer(){
//        mActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                AudioManager.getInstance().initSpeechRecognizer(mContext, X5WebViewJSFunction.this, X5WebViewJSFunction.this).show();
//            }
//        });
//    }
//
//    /**
//     * Description 调用拨号盘拨打电话
//     * UpdateUser husong.
//     * UpdateDate 2016/10/20 14:38.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void dialPhoneNumber(String phoneNumber){
//        DeviceInterface.getInstance().dialPhoneNumber(mContext, phoneNumber);
//    }
//
//    /**
//     * Description 上传文件
//     * UpdateUser husong.
//     * UpdateDate 2016/10/25 15:01.
//     * Version 1.0
//     */
//    @JavascriptInterface
//    public void uploadFile(String interfaceName,String path, String id){
//        String url = BaseAppServerUrl.getAppServerUrl()+ interfaceName;
//        IOManager.getInstance().uploadFile(mContext, path, url, id);
//    }
//
//    @JavascriptInterface
//    public void setTitleColor(String color){
//        int c = Color.parseColor(color);
//        VideoApplication.getInstance().getMessagePump().
//                broadcastMessage(com.movie.beauty.message.Message.Type.REQUEST_H5_SET_TITLE_COLOR, c);
//    }
//
//
//    /**
//     * 第三方平台登录成功与否
//     */
//    PlatformActionListener listener = new PlatformActionListener() {
//        @Override
//        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//            Message msg=new Message();
//            msg.what= X5WebViewFragment.MSG_REQUEST_H5_LOGIN;
//            msg.obj= hashMap.toString();
//            msg.arg1 = 0;
//            mHandler.sendMessage(msg);
//        }
//
//        @Override
//        public void onError(Platform platform, int i, Throwable throwable) {
//            Message msg=new Message();
//            msg.what= X5WebViewFragment.MSG_REQUEST_H5_LOGIN;
//            msg.obj= throwable.getMessage().toString();
//            msg.arg1 = 1;
//            mHandler.sendMessage(msg);
//        }
//
//        @Override
//        public void onCancel(Platform platform, int i) {
//            Message msg=new Message();
//            msg.what= X5WebViewFragment.MSG_REQUEST_H5_LOGIN;
//            msg.obj= "";
//            msg.arg1 = 2;
//            mHandler.sendMessage(msg);
//        }
//    };
//
//    /**
//     * 分享回调 成功与否
//     */
//    PlatformActionListener shareListener = new PlatformActionListener() {
//        @Override
//        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//            //回调的地方是子线程，进行UI操作要用handle处理
//            Message msg = Message.obtain();
//            msg.what = X5WebViewFragment.MSG_REQUEST_H5_SHARE;
//            msg.obj = platform.getName();
//            String PlateName = (String) msg.obj;
//            if (PlateName.equals("Wechat")) {
//                msg.arg1 = 0;
//                Toast.makeText(mContext, "微信分享成功", Toast.LENGTH_LONG).show();
//            } else if (PlateName.equals("WechatMoments")) {
//                msg.arg1 = 1;
//                Toast.makeText(mContext, "朋友圈分享成功", Toast.LENGTH_LONG).show();
//            } else if (PlateName.equals("SinaWeibo")) {
//                msg.arg1 = 2;
//                Toast.makeText(mContext, "新浪分享成功", Toast.LENGTH_LONG).show();
//            } else {
//                msg.arg1 = 3;
//                Toast.makeText(mContext, PlateName + "分享成功", Toast.LENGTH_LONG).show();
//            }
//            mHandler.sendMessage(msg);
//        }
//
//        @Override
//        public void onError(Platform platform, int i, Throwable throwable) {
//            //回调的地方是子线程，进行UI操作要用handle处理
//            throwable.printStackTrace();
//            Message msg = Message.obtain();
//            msg.what = X5WebViewFragment.MSG_REQUEST_H5_SHARE;
//            msg.obj = throwable.getMessage();
//            msg.arg1 = 4;
//            mHandler.sendMessage(msg);
//            Toast.makeText(mContext, "分享失败", Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onCancel(Platform platform, int i) {
//            //回调的地方是子线程，进行UI操作要用handle处理
//            Message msg = Message.obtain();
//            msg.what = X5WebViewFragment.MSG_REQUEST_H5_SHARE;
//            msg.obj = platform.getName();
//            msg.arg1 = 5;
//            mHandler.sendMessage(msg);
//            Toast.makeText(mContext, platform.getName() + "取消分享", Toast.LENGTH_LONG).show();
//        }
//    };
//
//    /**
//     * 定位监听
//     */
////    AMapLocationListener locationListener = new AMapLocationListener() {
////        @Override
////        public void onLocationChanged(AMapLocation loc) {
////            if(null != progressDialog && progressDialog.isShowing())
////                progressDialog.dismiss();
////
////            if (null != loc) {
////                String resultLoc = DefaultSharePrefManager.getString(SharePrefConstant.KEY_LOCATION, null);
////
////                //解析定位结果
////                String result = AMapUtils.getLocationStr(loc);
////                DefaultSharePrefManager.putString(SharePrefConstant.KEY_LOCATION, result);
////                if(TextUtils.isEmpty(resultLoc)){
////                    Message msg=new Message();
////                    msg.what= X5WebViewFragment.MSG_REQUEST_H5_LOCATION;
////                    msg.obj= result;
////                    mHandler.sendMessage(msg);
////                }
////            } else {
////                LogManager.d("husong = %s", "定位失败，loc is null");
////            }
////        }
////    };
//
////    @Override
////    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
////        for(PhotoInfo pi : resultList){
////            LogManager.d("husong = %s", pi.getPhotoPath());
////        }
////    }
////
////    /***
////     * 科大讯飞 语音识别的监听 Start
////     * ***/
////    @Override
////    public void onHanlderFailure(int requestCode, String errorMsg) {
////
////    }
//
//    @Override
//    public void onInit(int code) {
//        Log.d("husong", "SpeechRecognizer init() code = " + code);
//        if (code != ErrorCode.SUCCESS) {
//           Log.d("husong", "初始化失败，错误码："+code);
//        }
//    }
//
//    @Override
//    public void onResult(RecognizerResult recognizerResult, boolean b) {
//        if(!b){
//            String result = recognizerResult.getResultString();
//            VideoApplication.getInstance().getMessagePump().
//                    broadcastMessage(com.movie.beauty.message.Message.Type.REQUEST_H5_SPEECHRECOGNIZER, result);
//        }
//    }
//
//    @Override
//    public void onError(SpeechError speechError) {
//
//    }
//    /***
//     * 科大讯飞 语音识别的监听 End
//     * ***/
//
//    private LoginUserInfo getLoginUser(){
//        String uid = getString(SharePrefConstant.KEY_USER_ID, null);
//        if(TextUtils.isEmpty(uid)) return null;
//        List<LoginUserInfo> userInfos = DataSupport.findAll(LoginUserInfo.class);
//        if(userInfos!=null){
//            for (LoginUserInfo user : userInfos){
//                if(TextUtils.equals(uid, user.getUid()))
//                    return user;
//            }
//        }
//        return null;
//    }
}
