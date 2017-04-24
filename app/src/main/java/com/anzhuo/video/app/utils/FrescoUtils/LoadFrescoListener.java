package com.anzhuo.video.app.utils.FrescoUtils;

import android.graphics.Bitmap;

public interface LoadFrescoListener {
    void onSuccess(Bitmap bitmap);

    void onFail();
}