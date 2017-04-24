package com.anzhuo.video.app.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.anzhuo.fulishipin.app.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 相机有关的实现
 * Created by husong on 2016/9/29.
 */
public class CameraInterface {

    private static final CameraInterface mInstance = new CameraInterface();
    private Camera camera;

    private final int LED_OPEN=0;
    private final int LED_CLOSE=1;
    private final int LED_NO_SUPPORT=2;
    private final String REQUEST_H5_STATUS="stateCode";
    /* 用来标识请求照相功能的activity */
    public static final int REQUEST_CAMERA = 100;
    public static final int REQUEST_SELECT_PICTURE = 101;

    public CameraInterface(){

    }

    public static CameraInterface getInstance(){
        return mInstance;
    }

    /**
     * Description 打开/关闭手电筒的实现
     * UpdateUser husong.
     * UpdateDate 2016/9/29 16:46.
     * Version 1.0
     */
    public String flashLight(Context mContext){
        Map<String, Object> param = new HashMap<String, Object>();
        if(camera==null)
            camera = Camera.open();
        Camera.Parameters mParameters = camera.getParameters();
        if(hasFlash(mContext)){
            if(TextUtils.equals(mParameters.getFlashMode(), Camera.Parameters.FLASH_MODE_TORCH) ||
                    TextUtils.equals(mParameters.getFlashMode(), Camera.Parameters.FLASH_MODE_RED_EYE) ||
                    TextUtils.equals(mParameters.getFlashMode(), Camera.Parameters.FLASH_MODE_ON)){
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(mParameters);
                camera.stopPreview();
                param.put(REQUEST_H5_STATUS, LED_CLOSE);
            }else{
                openFlashlight();
                param.put(REQUEST_H5_STATUS, LED_OPEN);
            }
        }else {
            param.put(REQUEST_H5_STATUS, LED_NO_SUPPORT);
        }

        return JSON.toJSONString(param);
    }

    // 打开闪光灯
    protected void openFlashlight() {
        try {
            int textureId = 0;
            camera.setPreviewTexture(new SurfaceTexture(textureId));
            camera.startPreview();
            Camera.Parameters mParameters = camera.getParameters();

            mParameters.setFlashMode(mParameters.FLASH_MODE_TORCH);
            camera.setParameters(mParameters);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public boolean hasFlash(Context context) {
        PackageManager pm = context.getPackageManager();
        FeatureInfo[] featureInfos = pm.getSystemAvailableFeatures();
        for (FeatureInfo f : featureInfos) {
            if (PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Description 调用系统照相机
     * UpdateUser husong.
     * UpdateDate 2016/10/9 18:28.
     * Version 1.0
     */
    public void showCameraAction(Activity activity, File file) {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            if (null != file) {
                //如果output到File中后，activtyResult的data中将没有值
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            }
            activity.startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(activity, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Description 调用系统录像机
     * UpdateUser husong.
     * UpdateDate 2016/10/11 14:01.
     * Version 1.0
     */
    public void showSystemVideo(String filepath, Activity activity){
        Intent intent = new Intent();
        intent.setAction("android.media.action.VIDEO_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");
        File file = new File(filepath);
        if(file.exists()){
                file.delete();
        }
        Uri uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, 0);
    }

    public void showSelectPictures(Context context){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        intent.putExtra("crop", true);
        ((Activity)context).startActivityForResult(intent, REQUEST_SELECT_PICTURE);
    }

    /**
     * Description 释放资源
     * UpdateUser husong.
     * UpdateDate 2016/9/29 16:46.
     * Version 1.0
     */
    public void destroy(){
        if(camera!=null){
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}
