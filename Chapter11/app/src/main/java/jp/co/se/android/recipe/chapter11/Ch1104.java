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
        // センサーの解除
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // センサーの登録
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
        case Sensor.TYPE_ACCELEROMETER: // 加速度センサーの値を取得
            mAcMatrix = event.values.clone();
            break;
        case Sensor.TYPE_MAGNETIC_FIELD:// 磁気センサーの値を取得
            mMgMatrix = event.values.clone();
            break;
        }

        if (mMgMatrix != null && mAcMatrix != null) {
            float[] orientation = new float[3];
            float[] R = new float[16];
            float[] I = new float[16];

            // 加速度センサー、磁気センサーの値を元に回転行列を計算
            SensorManager.getRotationMatrix(R, I, mAcMatrix, mMgMatrix);

            // デバイスの向きに応じて回転行列を計算
            SensorManager.getOrientation(R, orientation);

            // ラジアンから角度に変換
            float angle = (float) Math.floor(Math.toDegrees(orientation[0]));

            // 角度の範囲を0 ～ 360度に調整
            if (angle >= 0) {
                orientation[0] = angle;
            } else if (angle < 0) {
                orientation[0] = 360 + angle;
            }

            // 得られた角度を画面に表示
            mTvAzimuth.setText(String.valueOf(orientation[0]));
        }
    }
}