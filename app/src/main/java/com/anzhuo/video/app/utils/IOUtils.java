package com.anzhuo.video.app.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Administrator on 2016/4/1.
 */
public class IOUtils {

    /** 关闭流 */
    public static boolean close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
            }
        }
        return true;
    }
}
