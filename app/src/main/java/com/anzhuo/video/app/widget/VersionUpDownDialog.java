package com.anzhuo.video.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anzhuo.fulishipin.app.R;
import com.anzhuo.video.app.utils.DialogUtils;
import com.anzhuo.video.app.utils.SharedUtil;
import com.anzhuo.video.app.utils.Utils;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class VersionUpDownDialog {
    Dialog versionUpDownDialog;
    View view;
    URL downloadUrl;
    Context mContext;
    TextView version_up_down_now_size, version_up_down_total_size;
    //    VersionInfo mVersionInfo;
    SeekBar version_up_down_sb;
    //    Thread downThread;
    DownloadThread downloadThread;
    private Message message = null;
    private int size = 0;
    private int hasRead = 0;
    private int len = 0;
    private byte buffer[] = new byte[1024 * 4];
    private int index = 1;
    private MyHandler handler = null;
    File apkFile;

    public VersionUpDownDialog(Context mContext) {
        this.mContext = mContext;
//        this.mVersionInfo = versionInfo;

        String versionCode = SharedUtil.getString("versionCode");
        String download_url = SharedUtil.getString("download_url");

        view = LayoutInflater.from(mContext).inflate(R.layout.version_up_down_dialog_layout, null);
//        String appName = DefaultSharePrefManager.getString("appName", "IntimateWeather") + DefaultSharePrefManager.getString("versionNew", "");
        String appName = "updatae";
        apkFile = new File(Environment.getExternalStorageDirectory() + "/" + appName + ".apk");
        handler = new MyHandler();
//        FileUtil.deleteFile(apkFile);
        try {
            downloadUrl = new URL(SharedUtil.getString("download_url"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        downloadThread = new DownloadThread();
        onCreateDialog(mContext);
    }

    private void onCreateDialog(Context context) {
        versionUpDownDialog = new Dialog(context);
        versionUpDownDialog.setCanceledOnTouchOutside(false);
        version_up_down_sb = (SeekBar) view.findViewById(R.id.version_up_down_sb);
        version_up_down_sb.setEnabled(false);
        version_up_down_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == seekBar.getMax()) {
                    Toast.makeText(mContext, "下载完成", Toast.LENGTH_SHORT).show();
                    handler.removeMessages(message.what);
                    versionUpDownDialog.dismiss();
                    Logger.i("=======[apkFile下载路径]===========" + apkFile);
                    DialogUtils.showInstallAPKDialog(mContext, apkFile);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //当前下载大小
        version_up_down_now_size = (TextView) view.findViewById(R.id.version_up_down_now_size);
        //总大小
        version_up_down_total_size = (TextView) view.findViewById(R.id.version_up_down_total_size);
        versionUpDownDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        versionUpDownDialog.setContentView(view);
//        version_up_down_exit = (ImageView) view.findViewById(R.id.version_up_down_exit);
//        version_up_down_exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                downloadThread.interrupt();
//                versionUpDownDialog.dismiss();
//            }
//        });
    }

    public void show() {
        versionUpDownDialog.show();
        downloadThread.start();
    }

    class DownloadThread extends Thread {
        @Override
        public void run() {
//            super.run();
            try {
                HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
                connection.setConnectTimeout(10 * 1000); //超时时间
                connection.connect();  //连接
                if (connection.getResponseCode() == 200) { //返回的响应码200,是成功.
//                    File file = new File(apkFile);   //这里我是手写了。建议大家用自带的类
                    apkFile.createNewFile();
                    size = connection.getContentLength();
                    InputStream inputStream = connection.getInputStream();
                    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(); //缓存
                    byte[] buffer = new byte[1024 * 10];
                    while (true) {
                        int len = inputStream.read(buffer);
                        hasRead += len;
//                        Log.e("downLoadSize2","======hasRead:"+hasRead);
                        index = (int) (hasRead * 100) / size;
                        message = new Message();
                        message.what = 1;
                        message.arg1 = hasRead;
                        message.arg2 = size;
                        handler.sendMessage(message);
                        if (len == -1) {
                            break;  //读取完
                        }
                        arrayOutputStream.write(buffer, 0, len);  //写入
                    }
                    arrayOutputStream.close();
                    inputStream.close();

                    byte[] data = arrayOutputStream.toByteArray();
                    FileOutputStream fileOutputStream = new FileOutputStream(apkFile);
                    fileOutputStream.write(data); //记得关闭输入流
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                Message messageFlag = new Message();
                if (e.toString().equals("java.io.InterruptedIOException")) {
                    messageFlag.what = 2;
                    handler.sendMessage(messageFlag);
                    versionUpDownDialog.dismiss();
                } else {
                    Log.i("response", "更新失败=====" + e.getMessage() + e.getStackTrace());
                    messageFlag.what = 3;
                    handler.sendMessage(messageFlag);
                    versionUpDownDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }
    }

    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    version_up_down_sb.setMax(msg.arg2);
                    version_up_down_sb.setProgress(msg.arg1);
                    version_up_down_total_size.setText(Utils.formatFileSize(msg.arg2));
                    version_up_down_now_size.setText(Utils.formatFileSize(msg.arg1));
                    break;
                case 2:
                    Toast.makeText(mContext, "您已取消更新", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(mContext, "下载更新包失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

}
