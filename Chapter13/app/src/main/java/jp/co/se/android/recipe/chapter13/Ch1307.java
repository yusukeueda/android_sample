package jp.co.se.android.recipe.chapter13;

import jp.co.se.android.recipe.chapter13.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Ch1307 extends Activity {
    private BatteryChangedReceiver mBatteryChangedReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1307_main);
        // バッテリーの状態を通知するブロードキャストレシーバ
        IntentFilter intentFilter = new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED);
        mBatteryChangedReceiver = new BatteryChangedReceiver(this);
        registerReceiver(mBatteryChangedReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        if (mBatteryChangedReceiver != null) {
            unregisterReceiver(mBatteryChangedReceiver);
            mBatteryChangedReceiver = null;
        }
        super.onDestroy();
    }

    public static class BatteryChangedReceiver extends BroadcastReceiver {
        Activity mActivity;

        public BatteryChangedReceiver(Activity activity) {
            this.mActivity = activity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            // 残量
            TextView tvBatteryLevel = (TextView) mActivity
                    .findViewById(R.id.BatteryLevel);
            int level = intent.getIntExtra("level", 0);
            int scale = intent.getIntExtra("scale", 0);
            tvBatteryLevel.setText(level + " / " + scale + "(%)");

            // 充電状態
            TextView tvBatteryCharge = (TextView) mActivity
                    .findViewById(R.id.BatteryCharge);
            switch (intent.getIntExtra("status", 0)) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                // 充電中
                tvBatteryCharge.setText(context
                        .getString(R.string.label_detect_charging));
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                // 放電中
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                tvBatteryCharge.setText(context
                        .getString(R.string.label_detect_discharging));
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                // 満タン
                tvBatteryCharge.setText(context
                        .getString(R.string.label_detect_fullbattery));
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                // 不明
                Log.d("BatteryChange",
                        context.getString(R.string.label_detect_status_unknown));
                break;
            }

            // 品質
            TextView tvBatteryQuality = (TextView) mActivity
                    .findViewById(R.id.BatteryQuality);
            switch (intent.getIntExtra("health", 0)) {
            case BatteryManager.BATTERY_HEALTH_GOOD:
                // 良好
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_good));
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                // 熱暴走
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_overheart));
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                // 過電圧
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_overvoltage));
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                // 故障
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_dead));
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                // 特定失敗
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_unspecifiedfailure));
                break;
            case BatteryManager.BATTERY_HEALTH_COLD:
                // 低温
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_cold));
                break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                // 不明
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_unknown));
                break;
            }

            // 温度&電圧
            TextView tvBatteryTemperature = (TextView) mActivity
                    .findViewById(R.id.BatteryTemperature);
            int temperatue = intent.getIntExtra("temperature", 0) / 10;
            float voltage = intent.getIntExtra("voltage", 0) / 1000;
            tvBatteryTemperature
                    .setText(context.getString(
                            R.string.label_detect_temperature, temperatue)
                            + " / "
                            + context.getString(R.string.label_detect_voltage,
                                    voltage));

            // 充電方法
            TextView tvBatteryConnect = (TextView) mActivity
                    .findViewById(R.id.BatteryConnect);
            switch (intent.getIntExtra("plugged", 0)) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                // AC接続
                tvBatteryConnect.setText(context
                        .getString(R.string.label_detect_ac));
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                // USB接続
                tvBatteryConnect.setText(context
                        .getString(R.string.label_detect_usb));
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                // ワイヤレス接続
                tvBatteryConnect.setText(context
                        .getString(R.string.label_detect_wireless));
                break;
            default:
                tvBatteryConnect.setText(context
                        .getString(R.string.label_detect_disconnect));
                break;

            }
        }
    };
}
