package com.anzhuo.video.app.manager.fuli;

/**
 * Created with Android Studio.
 * Authour：Eric_chan
 * Date：2016/10/9 10:26
 * Des：
 */
public interface SharePrefConstant {

    //preference_name
    String NAME_DEFAULT = "1122";


    //===========================================================

    //用户相关信息
//    String KEY_USER_INFORMATION ="key_user_information";  //用户信息
    String KEY_USER_ID ="key_user_id";  //用户id
//    String KEY_USER_HEAD ="key_user_head";  //用户头像


    String KEY_AUTOUPDATE_APP_VERSION ="key_autoupdate_app_version";  //是否自动更新


    String KEY_ITEM_GRIDWITH = "key_item_gridwith";
    String KEY_ITEM_GRIDHEIGHT = "key_item_gridhight";

    String KEY_APP_VERSION_CODE ="key_app_version_code";  //客户端版本号
    String KEY_PRE_UPDATE_TIME = "key_pre_update_time";//上一次跑版本更新接口的时间
    String KEY_GPU_INFO = "key_gpu_info";  //GPU信息
    String KEY_CPU_TYPE = "key_cpu_type";  //CPU类型
    String KEY_HAS_ROOT = "key_has_root";  //是否有Root权限
    String KEY_CHECK_ROOT = "key_check_root";  //是否检测有Root权限
    String KEY_CREATE_SHORTCUT = "key_create_shortcut"; //创建快捷方式


    //设置  key
    String KEY_SETTINGS_PATH_DOWNLOAD = "downloadpath";	//文件存放路劲
    String KEY_SETTINGS_AUTOINSTALL = "auto_install";//下载完后直接安装
    String KEY_SETTINGS_SILENT_INSTALL = "silent_install";//是否开启静默安装
    String KEY_SETTINGS_WIFI_AUTO_DOWNLOAD = "download_auto_wifi";//WIFI下自动下载
    String KEY_SETTINGS_GPRSNOTDOWNLOADIMAGE = "imageload";//仅在WIFI网络下显示图片
    String KEY_SETTINGS_DOWNLOAD_ON_WIFI = "wifidownload";//仅在wifi下下载
    String KEY_SETTINGS_UPDATECHECK = "updatecheck";//新版更新栏提示
    String KEY_SETTINGS_DOWNLOAD_PLAYAUDIO = "download_playaudio";//下载完提示音
    String KEY_SETTINGS_INSTALL_AUTODELETE = "install_autodelete";//安装完成后删除下载文件

    String KEY_JPUSH_SET_TAGALIAS_SUCCESS = "set_tagalias_success"; //设置jpush的tag或alias成功
    String KEY_LOCATION = "key_location"; //定位
}