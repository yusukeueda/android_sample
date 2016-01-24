package jp.co.se.android.recipe.chapter11;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class Ch1104 extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    TextView mTvAzimuth;
    private float[] mAcMatrix = new float[3];
    private float[] mMgMatrix = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1104_main);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mTvAzimuth = (TextView) findViewById(R.id.Azimuth);
    }

    @Override
    protected void onPause() {
        // �Z���T�[�̉���
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // �Z���T�[�̓o�^
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
        case Sensor.TYPE_ACCELEROMETER: // �����x�Z���T�[�̒l���擾
            mAcMatrix = event.values.clone();
            break;
        case Sensor.TYPE_MAGNETIC_FIELD:// ���C�Z���T�[�̒l���擾
            mMgMatrix = event.values.clone();
            break;
        }

        if (mMgMatrix != null && mAcMatrix != null) {
            float[] orientation = new float[3];
            float[] R = new float[16];
            float[] I = new float[16];

            // �����x�Z���T�[�A���C�Z���T�[�̒l�����ɉ�]�s����v�Z
            SensorManager.getRotationMatrix(R, I, mAcMatrix, mMgMatrix);

            // �f�o�C�X�̌����ɉ����ĉ�]�s����v�Z
            SensorManager.getOrientation(R, orientation);

            // ���W�A������p�x�ɕϊ�
            float angle = (float) Math.floor(Math.toDegrees(orientation[0]));

            // �p�x�͈̔͂�0 �` 360�x�ɒ���
            if (angle >= 0) {
                orientation[0] = angle;
            } else if (angle < 0) {
                orientation[0] = 360 + angle;
            }

            // ����ꂽ�p�x����ʂɕ\��
            mTvAzimuth.setText(String.valueOf(orientation[0]));
        }
    }
}