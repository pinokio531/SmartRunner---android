package com.haneoum.smartrunner;


import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.XDirection;

public class Monitoring extends Fragment {

    MediaPlayer mediaPlayer;
    private TextView mLockStateView;
    private TextView mTextView;
    private TextView vector_x;
    private TextView vector_y;
    private TextView vector_z;

    private DeviceListener mListener = new AbstractDeviceListener() {

        @Override
        public void onConnect(Myo myo, long timestamp) {
            mTextView.setTextColor(Color.GREEN);
        }

        @Override
        public void onDisconnect(Myo myo, long timestamp) {

            mTextView.setTextColor(Color.RED);
        }

        @Override
        public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
            mTextView.setText(myo.getArm() == Arm.LEFT ? R.string.arm_left : R.string.arm_right);
        }

        @Override
        public void onArmUnsync(Myo myo, long timestamp) {
            mTextView.setText("싱크완료!!");
        }

        @Override
        public void onUnlock(Myo myo, long timestamp) {
            mLockStateView.setText(R.string.unlocked);
        }

        @Override
        public void onLock(Myo myo, long timestamp) {
            mLockStateView.setText(R.string.locked);
            mLockStateView.setTextColor(Color.RED);
        }

        @Override
        public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
            float roll = (float) Math.toDegrees(Quaternion.roll(rotation));
            float pitch = (float) Math.toDegrees(Quaternion.pitch(rotation));
            float yaw = (float) Math.toDegrees(Quaternion.yaw(rotation));
            // Adjust roll and pitch for the orientation of the Myo on the arm.
            if (myo.getXDirection() == XDirection.TOWARD_ELBOW) {
                roll *= -1;
                pitch *= -1;
            }

            double x = rotation.x();
            double y = rotation.y();
            double z = rotation.z();

            vector_x.setText(String.format("%.4f", x));
            vector_y.setText(String.format("%.4f", y));
            vector_z.setText(String.format("%.4f", z));

            mTextView.setRotation(roll);
            mTextView.setRotationX(pitch);
            mTextView.setRotationY(yaw);
        }

        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {

            switch (pose) {
                case UNKNOWN:
                    mTextView.setText(getString(R.string.hello_world));
                    break;
                case REST:
                case DOUBLE_TAP:
                    int restTextId = R.string.hello_world;
                    switch (myo.getArm()) {
                        case LEFT:
                            restTextId = R.string.arm_left;
                            break;
                        case RIGHT:
                            restTextId = R.string.arm_right;
                            break;
                    }
                    mTextView.setText(getString(restTextId));
                    break;
                case FIST:
                    mTextView.setText(getString(R.string.pose_fist));
                    break;
                case WAVE_IN:
                    mTextView.setText(getString(R.string.pose_wavein));
                    break;
                case WAVE_OUT:
                    mTextView.setText(getString(R.string.pose_waveout));
                    break;
                case FINGERS_SPREAD:
                    mTextView.setText(getString(R.string.pose_fingersspread));
                    break;
            }
            if (pose != Pose.UNKNOWN && pose != Pose.REST) {

                myo.unlock(Myo.UnlockType.HOLD);
                myo.notifyUserAction();
            } else {
                myo.unlock(Myo.UnlockType.TIMED);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_monitoring, container, false);

        mLockStateView = (TextView) layout.findViewById(R.id.lock_state);
        mTextView = (TextView) layout.findViewById(R.id.test_TextView);

        vector_x = (TextView) layout.findViewById(R.id.vectorX);
        vector_y = (TextView) layout.findViewById(R.id.vectorY);
        vector_z = (TextView) layout.findViewById(R.id.vectorZ);

        Hub hub = Hub.getInstance();
        if (!hub.init(getActivity(), getActivity().getPackageName())) {
            Toast.makeText(getActivity(), "허브 초기화되지 않음", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        hub.addListener(mListener);

        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Hub.getInstance().removeListener(mListener);
        if (isRemoving()) {
            Hub.getInstance().shutdown();
        }
    }
}