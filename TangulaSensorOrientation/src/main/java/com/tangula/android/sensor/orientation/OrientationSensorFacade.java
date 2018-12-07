package com.tangula.android.sensor.orientation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.tangula.android.utils.ApplicationUtils;
import com.tangula.android.utils.LogUt;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class OrientationSensorFacade {

    private static volatile SensorManager SM = null;

    private static final Object LOCK = new Object();

    private static Sensor accelerometer; // 加速度传感器
    private static Sensor  magnetic; // 地磁场传感器


    private static float[] accelerometerValues = new float[3];
    private static float[] magneticFieldValues = new float[3];

    private static final List<Consumer<OrientationInfo>> LISTENERS = new LinkedList<>();


    public static void calculateOrientation() {
        float[] values = new float[3];
        float[] R =  new float[9];
        SensorManager.getRotationMatrix(
                R, null, accelerometerValues,
                magneticFieldValues
        );
        SensorManager.getOrientation(R, values);

        OrientationInfo info = new OrientationInfo(
                (float)Math.toDegrees(values[0]),
                (float)Math.toDegrees(values[1]),
                (float)Math.toDegrees(values[2])
        );

        synchronized (LISTENERS){
            for(Consumer<OrientationInfo> cur:LISTENERS){
                try {
                    cur.accept(info);
                } catch (Exception e) {
                    LogUt.e(e);
                }
            }
        }


    }

    private static final SensorEventListener SENSOR_LISTENER =new  SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            int type = event.sensor.getType();
            if ( type == Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = event.values;
            }
            if (type == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticFieldValues = event.values;
            }
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private static SensorManager getSensorManager(){
        if(SM==null) {
            synchronized (LOCK) {
                if (SM == null) {
                    if(ApplicationUtils.APP!=null) {
                        SM = (SensorManager) ApplicationUtils.APP.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
                        // 初始化加速度传感器
                        accelerometer = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                        // 初始化地磁场传感器
                        magnetic = SM.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                    }else{
                        throw new RuntimeException("ApplicationUtils.APP is null.");
                    }
                }
            }
        }
        return SM;
    }



    public static void watch(Consumer<OrientationInfo> listener){
        if(listener!=null) {
            synchronized (LISTENERS) {
                if (LISTENERS.size() == 0) {
                    LISTENERS.add(listener);
                    SensorManager sm = getSensorManager();
                    sm.registerListener(SENSOR_LISTENER,
                            accelerometer, Sensor.TYPE_ACCELEROMETER);
                    sm.registerListener(
                            SENSOR_LISTENER, magnetic,
                            Sensor.TYPE_MAGNETIC_FIELD
                    );
                } else {
                    LISTENERS.add(listener);
                }
            }
        }
    }

    public static void unwatch(Consumer<OrientationInfo> listener){
        if(listener!=null) {
            synchronized (LISTENERS) {
                LISTENERS.remove(listener);
                if (LISTENERS.size() == 0) {
                    getSensorManager().unregisterListener(SENSOR_LISTENER);
                }
            }
        }
    }



}
