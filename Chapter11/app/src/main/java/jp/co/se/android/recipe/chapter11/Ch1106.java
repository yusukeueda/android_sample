package jp.co.se.android.recipe.chapter11;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class Ch1106 extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1106_main);

        final TextView tvDescription = (TextView) findViewById(R.id.description);
        final Switch proximitySwitch = (Switch) findViewById(R.id.proximitySwitch);

        // スイッチのON・OFF
        proximitySwitch
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {
                        if (isChecked) {
                            // センサーを登録
                            registSensor();
                            tvDescription.setText(Ch1106.this
                                    .getString(R.string.ch1106_label_pause_of_the_call));
                            tvDescription.setVisibility(View.VISIBLE);
                        } else {
                            // センサーを解除
                            unregistSensor();
                            tvDescription.setVisibility(View.GONE);
                        }
                    }
                });

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    protected void onDestroy() {
        // センサーを解除
        unregistSensor();
        super.onDestroy();
    }

    /**
     * 近接センサーを登録.
     */
    private void registSensor() {
        if (mSensorManager == null) {
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            mSensorManager.registerListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    /**
     * 近接センサーを解除
     */
    private void unregistSensor() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
            mSensorManager = null;
        }
        mVibrator.cancel();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 加速度センサーの値を取得
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] == 0) {
                mVibrator.vibrate(new long[] { 100, 100 }, 0);
            } else {
                mVibrator.cancel();
            }
        }
    }
}
