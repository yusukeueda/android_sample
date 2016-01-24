package jp.co.se.android.recipe.chapter11;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class Ch1101 extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1101_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onPause() {
        // 加速度センサーのリスナーを解除
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 加速度センサーのリスナーを登録
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 加速度センサーの値を取得
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            TextView tvDataX = (TextView) findViewById(R.id.DataX);
            TextView tvDataY = (TextView) findViewById(R.id.DataY);
            TextView tvDataZ = (TextView) findViewById(R.id.DataZ);
            tvDataX.setText(String.valueOf(event.values[0]));
            tvDataY.setText(String.valueOf(event.values[1]));
            tvDataZ.setText(String.valueOf(event.values[2]));
        }
    }
}
