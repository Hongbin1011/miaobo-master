package com.anzhuo.video.app.utils.permission;

import android.Manifest;
import android.app.Activity;
import android.view.View;

import com.tbruyelle.rxpermissions.RxPermissions;

import me.drakeet.materialdialog.MaterialDialog;
import rx.functions.Action0;
import rx.functions.Action1;


/**
 * Created by Administrator on 2016/9/23.
 * 权限工具
 */

public class PermissionUtil {
    /**
     * 获取权限
     *
     * @param permissionCallBack
     * @param permissions
     */
    public static void getPermission(Activity activity, final PermissionCallBack permissionCallBack, String... permissions) {
        new RxPermissions(activity)
                .request(permissions)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            permissionCallBack.PermissionSuccess();
                        } else {
                            permissionCallBack.PermissionFail();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                    }
                });
    }

    /**
     * 获取访问内存卡的权限
     *
     * @param permissionCallBack
     */
    public static void getStoragePermission(final Activity activity, final PermissionCallBack permissionCallBack) {
        getPermission(activity, new PermissionCallBack() {
                    @Override
                    public void PermissionSuccess() {
                        permissionCallBack.PermissionSuccess();
                    }

                    @Override
                    public void PermissionFail() {
                        showDialog(activity);
                        permissionCallBack.PermissionFail();
                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
// Manifest.permission.READ_PHONE_STATE
        );
    }

    private static void showDialog(final Activity context) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
        mMaterialDialog.setTitle("提示");
        mMaterialDialog.setMessage("由于部分功能需要相关权限，请授权！");
        mMaterialDialog.setPositiveButton("去授权", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStoragePermission(context, null);
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



}
