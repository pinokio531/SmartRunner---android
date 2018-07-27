package com.haneoum.smartrunner;

import android.app.Instrumentation;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MoveGesture extends Service{

    private static final String TAG = "MyoGlassService";
    private static final String PREF_MAC_ADDRESS = "PREF_MAC_ADDRESS";
    private Hub mHub;
    private SharedPreferences mPrefs;
    private boolean mActivityActive;
    private MyoListener mListener = new MyoListener();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new MBinder();

    public class MBinder extends Binder {
        public MoveGesture getService() {
            return MoveGesture.this;
        }
    }

    public void setActivityActive(boolean active) {
        mActivityActive = active;
    }

    public void attachToNewMyo() {

        mHub.detach(mPrefs.getString(PREF_MAC_ADDRESS, ""));
        mPrefs.edit().putString(PREF_MAC_ADDRESS, "").apply();
        mHub.attachToAdjacentMyo();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mHub = Hub.getInstance();
        if (!mHub.init(this, getPackageName())) {
            Log.e(TAG, "Could not initialize the Hub.");
            stopSelf();
            return;
        }
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mHub.addListener(mListener);

        if (mHub.getConnectedDevices().isEmpty()) {
            String myoAddress = mPrefs.getString(PREF_MAC_ADDRESS, "");

            if (TextUtils.isEmpty(myoAddress)) {
                mHub.attachToAdjacentMyo();
            } else {
                mHub.attachByMacAddress(myoAddress);
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        mHub.shutdown();
        mListener.shutdown();
    }

    private class MyoListener extends AbstractDeviceListener {
        private static final long LAUNCH_HOLD_DURATION = 1000;
        private Handler mHandler = new Handler();
        private Instrumentation mInstrumentation = new Instrumentation();
        private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
        private Runnable mLaunchRunnable = new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(MoveGesture.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        public void shutdown() {
            mExecutor.shutdown();
        }

        @Override
        public void onAttach(Myo myo, long timestamp) {
            mPrefs.edit().putString(PREF_MAC_ADDRESS, myo.getMacAddress()).apply();
        }

        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            Log.i(TAG, "pose: " + pose);
            if (!mActivityActive) {
                if (pose == Pose.FINGERS_SPREAD) {
                    mHandler.postDelayed(mLaunchRunnable, LAUNCH_HOLD_DURATION);
                    myo.notifyUserAction();
                    myo.unlock(Myo.UnlockType.HOLD);
                } else {
                    mHandler.removeCallbacks(mLaunchRunnable);
                    myo.notifyUserAction();
                    myo.unlock(Myo.UnlockType.TIMED);
                }
            } else {
                if (myo.getArm() == Arm.LEFT) {
                    if (pose == Pose.WAVE_IN) {
                        pose = Pose.WAVE_OUT;
                    } else if (pose == Pose.WAVE_OUT) {
                        pose = Pose.WAVE_IN;
                    }
                }

                switch (pose) {
                    case FIST:
                        sendEvents(myo, (List<MotionEvent>) Toast.makeText(MoveGesture.this, "FIST", Toast.LENGTH_SHORT));
                        break;
                    case FINGERS_SPREAD:
                        sendEvents(myo, (List<MotionEvent>) Toast.makeText(MoveGesture.this, "FINGER_SPREAD", Toast.LENGTH_SHORT));
                        break;
                    case WAVE_IN:
                        sendEvents(myo, (List<MotionEvent>) Toast.makeText(MoveGesture.this, "WAVE_IN", Toast.LENGTH_SHORT));
                        break;
                    case WAVE_OUT:
                        sendEvents(myo, (List<MotionEvent>) Toast.makeText(MoveGesture.this, "WAVE_OUT", Toast.LENGTH_SHORT));
                        break;
                }
            }
        }

        private void sendEvents(Myo myo, final List<MotionEvent> events) {
            if (mExecutor.isShutdown()) {
                Log.w(TAG, "EVENT 전송 실패");
                return;
            }

            myo.notifyUserAction();

            myo.unlock(Myo.UnlockType.TIMED);
            for (final MotionEvent event : events) {

                mExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mInstrumentation.sendPointerSync(event);
                        } catch (Exception e) {
                            Log.e(TAG, "Failed sending motion event." , e);
                        }
                    }
                });
            }
        }
    }
}

