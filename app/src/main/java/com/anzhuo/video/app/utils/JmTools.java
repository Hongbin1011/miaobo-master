package com.anzhuo.video.app.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * creat on 2016/4/6.
 */
public class JmTools {

    public final static String DKAETYA = "@#-*￥cxmx^&+#@*";

    /**
     * 输入加密 T
     *
     * @param str
     * @return
     */
    public static String DKAETYA16(String str) {

        String a = str.substring(0, 16);

        return a;
    }

    /**
     * json 解密
     *
     * @param object
     * @return
     */
    public static String DecryptKey(JSONObject object) {

        String result = object.getString("result");

        String t = object.getString("T");

        String keyString = Security.md5(t + DKAETYA);

        String key = DKAETYA16(keyString);

        //解密
        String data = Security.decrypt(key, result);

        return data;
    }

    /**
     * 字符串 加密
     *
     * @param time
     * @param mString
     * @return String1  String2  String3
     */
    public static String encryptionEnhanced(String time, String mString) {

//        JSONObject jo=new JSONObject();
//        jo.put("item","125496496");
//        JSONArray ja=new JSONArray();
//        ja.add(jo);
//        String name =ja.toString();
        String key = Security.md5(time + DKAETYA);

        String key16 = DKAETYA16(key);

        String date = Security.encrypt(key16, mString);

        return date;
    }

}
