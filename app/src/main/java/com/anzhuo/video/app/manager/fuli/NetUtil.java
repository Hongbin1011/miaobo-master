package com.anzhuo.video.app.manager.fuli;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created with Android Studio.
 * Authour：Eric_chan
 * Date：2016/9/28 18:33
 * Des：
 */
public class NetUtil {
    /** 网络类型,全局广播接收者控制,判断网络类型用它 */
    public static NetType netType;

    public static enum NetType {
        Mobile, Mobile_Proxy, Wifi, NoNetwork, Unknown
    }

    public static boolean isGPRSEnable(Context context) {
        return isGPRSEnable(context, true);
    }

    /** 处理广播接收者判断网络延迟情况 */
    public static boolean isGPRSEnable(Context context, boolean immediately) {
        if (immediately) {
            return isNetworkActive(context, ConnectivityManager.TYPE_MOBILE);
        } else {
            return netType == NetType.Mobile || netType == NetType.Mobile_Proxy;
        }
    }

    public static boolean isWifiEnable(Context context) {
        return isWifiEnable(context, true);
    }

    public static boolean isWifiEnable(Context context, boolean immediately) {
        if (immediately) {
            return isNetworkActive(context, ConnectivityManager.TYPE_WIFI);
        } else {
            return netType == NetType.Wifi;
        }
    }

    public static boolean isNetworkEnable(Context context) {
        return isNetworkEnable(context, true);
    }

    public static boolean isNetworkEnable(Context context, boolean immediately) {
        if (immediately) {
            return networkEnable(context);
        } else {
            return !(netType == NetType.NoNetwork);
        }
    }

    private static boolean isNetworkActive(Context context, int type) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            return nInfo.getType() == type;
        } catch (Exception e) {
            // e.printStackTrace();

        }
        return false;
    }

    public static boolean networkEnable(Context context) {

        try {
            ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo workInfo = conn.getActiveNetworkInfo();
            if (workInfo != null) {
                return workInfo.isConnected();
            }
        } catch (Throwable e) {
            //			e.printStackTrace();
        }
        return false;
    }

    public static NetworkInfo getNetWorkInfo(Context context) {
        try {
            ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo workInfo = conn.getActiveNetworkInfo();
            return workInfo;
        } catch (Throwable e) {
        }
        return null;
    }

    /**
     * 得到网络类型
     *
     * @return
     */
    public static NetType getNetType(Context context) {
        NetType netType;
        if (networkEnable(context)) {
            if (isNetworkActive(context, ConnectivityManager.TYPE_WIFI)) {
                netType = NetType.Wifi;
            } else if (isNetworkActive(context, ConnectivityManager.TYPE_MOBILE)) {
                String host = android.net.Proxy.getDefaultHost();
                int port = android.net.Proxy.getDefaultPort();
                if (host != null && host.length() > 0 && port > 0) {
                    netType = NetType.Mobile_Proxy;
                } else {
                    netType = NetType.Mobile;
                }
            } else {
                netType = NetType.Unknown;
            }
        } else {
            netType = NetType.NoNetwork;
        }
        return netType;
    }

    public static String getNetInfo(Context context) {
        NetType type = getNetType(context);
        String netType = null;
        switch (type) {
            case Wifi:
                netType = "WIFI";
                break;
            case Mobile_Proxy:
                netType = "MOBILE_PROXY";
                break;
            case Unknown:
                netType = "UNKNOWN";
                break;
            default:
                break;
        }
        if (type == NetType.Mobile) {
            NetworkInfo info = getNetWorkInfo(context);
            switch (info.getSubtype()) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    netType = "2G";
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    netType = "3G";
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    netType = "4G";
                default:
                    netType = "UNKNOWN";
            }

        }

        return netType;
    }

}

