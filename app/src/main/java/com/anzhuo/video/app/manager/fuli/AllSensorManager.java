package com.anzhuo.video.app.manager.fuli;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

/**
 * all sensor manager
 * Created by husong on 2016/10/10.
 */

public class AllSensorManager {
    private static final AllSensorManager mInstance= new AllSensorManager();
    SensorManager sensorManager ;
    private String gsonStr;
    public static final int REQUEST_MSG_TO_H5 = 93;

    public AllSensorManager(){

    }

    public static AllSensorManager getInstance(){
        return mInstance;
    }

    public void registerGsensorListener(Context context){
        if(sensorManager==null)
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    SensorEventListener lsn = new SensorEventListener() {

        public void onSensorChanged(SensorEvent e) {
            HashMap<String, Object> params= new HashMap<String, Object>();
            params.put("x", e.values[0]);
            params.put("y", e.values[1]);
            params.put("z", e.values[2]);
            gsonStr = JSON.toJSONString(params);
        }
        public void onAccuracyChanged(Sensor s, int accuracy) {
        }
    };

    public void unregisterGsensorListener(Context context){
        if(sensorManager==null)
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(lsn);
    }

    public String getGsonStr(){
        return this.gsonStr;
    }

}
