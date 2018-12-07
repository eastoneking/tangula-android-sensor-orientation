package com.tangula.android.sensor.orientation.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.tangula.android.base.TglBasicActivity;
import com.tangula.android.sensor.orientation.OrientationInfo;
import com.tangula.android.sensor.orientation.OrientationSensorFacade;
import com.tangula.android.utils.ApplicationUtils;

import io.reactivex.functions.Consumer;

public class MainActivity extends TglBasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ApplicationUtils.APP = this.getApplication();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OrientationSensorFacade.watch(new Consumer<OrientationInfo>() {
            @Override
            public void accept(OrientationInfo orientationInfo) throws Exception {
                Log.v("console", ""+orientationInfo.getAzimuthAngle());
            }
        });

    }
}
