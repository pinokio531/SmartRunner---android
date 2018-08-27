package com.haneoum.smartrunner;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;

public class BackgroundService extends Service {

    private static final String TAG = "BackgroundService";
    private Toast mToast;

    private DeviceListener mListener = new AbstractDeviceListener() {
        @Override
        public void onConnect(Myo myo, long timestamp) {
            showToast(getString(R.string.connected));
        }
        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            showToast(getString(R.string.disconnected));
        }

        @SuppressLint("StringFormatInvalid")
        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            showToast(getString(R.string.pose, pose.toString()));
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Hub hub = Hub.getInstance();
        if (!hub.init(this, getPackageName())) {
            showToast("Hub 최기화하지 못함");
            stopSelf();
            return;
        }

        hub.setLockingPolicy(Hub.LockingPolicy.NONE);
        hub.addListener(mListener);
        hub.attachToAdjacentMyo();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Hub.getInstance().removeListener(mListener);
        Hub.getInstance().shutdown();
    }
    private void showToast(String text) {
        Log.w(TAG, text);
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }
}
