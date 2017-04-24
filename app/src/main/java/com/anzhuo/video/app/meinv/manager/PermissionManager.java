package com.anzhuo.video.app.meinv.manager;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by hulei on 2016/5/31.
 * 权限请求类
 */
public class PermissionManager {
    //        public static final int PERMISSIONS_GRANTED = 0; // 权限授权
    //        public static final int PERMISSIONS_DENIED = 1; // 权限拒绝
    public static void checkPermission(Activity context, String needCheckPermission, int requestCode, PermissionInterFace permissionInterFace) {
        if (ContextCompat.checkSelfPermission(context,needCheckPermission)!= PackageManager.PERMISSION_GRANTED) {
           /** permissions have not been granted. Requesting permissions.*/
            ActivityCompat.requestPermissions(context,new String[]{needCheckPermission},requestCode);
        } else {
            /**permissions have been granted.**/
            permissionInterFace.havePermission();
        }
    }

    public interface PermissionInterFace {
        void havePermission();//已经拥有权限
    }
}
