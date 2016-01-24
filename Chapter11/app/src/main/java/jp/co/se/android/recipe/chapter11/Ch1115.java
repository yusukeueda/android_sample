package jp.co.se.android.recipe.chapter11;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

public class Ch1115 extends Activity {
    private AirPlaneModeReciever mAirPlaneModeReciever;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1115_main);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 機内モードを検出するブロードキャストレシーバを解除
        if (mAirPlaneModeReciever != null) {
            unregisterReceiver(mAirPlaneModeReciever);
            mAirPlaneModeReciever = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 機内モードを検出するブロードキャストレシーバを登録
        if (mAirPlaneModeReciever == null) {
            mAirPlaneModeReciever = new AirPlaneModeReciever();
            registerReceiver(mAirPlaneModeReciever, new IntentFilter(
                    Intent.ACTION_AIRPLANE_MODE_CHANGED));
        }
    }

    /**
     * 機内モードを検出するブロードキャストレシーバ.
     * 
     * @author Re:Kayo-System / m.iwasaki
     */
    private class AirPlaneModeReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 機内モードの状態を取得して画面に表示
            boolean state = intent.getBooleanExtra("state", false);
            TextView tvDescription = (TextView) findViewById(R.id.description);
            if (state) {
                tvDescription
                        .setText(getString(R.string.ch1115_label_airplanemode_enable));
            } else {
                tvDescription
                        .setText(getString(R.string.ch1115_label_airplanemode_disable));
            }
        }
    }

}
