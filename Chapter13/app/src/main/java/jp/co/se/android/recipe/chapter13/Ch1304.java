package jp.co.se.android.recipe.chapter13;

import jp.co.se.android.recipe.chapter13.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class Ch1304 extends Activity {
    private static final String KEY_BOOT_COMPLETED = "key.boot.completed";
    private static final String INTENT_BOOT_FROM_RECIEVER = "intent.boot.from.reciever";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1304_main);
        final TextView tvDescription = (TextView) findViewById(R.id.description);
        final Switch poweronSwitch = (Switch) findViewById(R.id.poweronSwitch);

        boolean fromReciever = getIntent().getBooleanExtra(
                INTENT_BOOT_FROM_RECIEVER, false);
        if (fromReciever) {
            // ブロードキャストレシーバによって起動した場合
            tvDescription
                    .setText(getString(R.string.label_detect_boot_completed));
        }

        // 画面の状態を更新
        if (!isBootCompleted(this)) {
            tvDescription.setVisibility(View.GONE);
            poweronSwitch.setChecked(false);
        } else {
            tvDescription.setVisibility(View.VISIBLE);
            poweronSwitch.setChecked(true);
        }

        // スイッチのON・OFF
        poweronSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (isChecked) {
                    setBootCompleted(Ch1304.this, true);
                    tvDescription
                            .setText(getString(R.string.label_detect_reboot));
                    tvDescription.setVisibility(View.VISIBLE);
                } else {
                    setBootCompleted(Ch1304.this, false);
                    tvDescription.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * SharedPreferencesにBootCompletedフラグをセット.
     * 
     * @param isBoot
     */
    public static void setBootCompleted(Context context, boolean isBoot) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(KEY_BOOT_COMPLETED, isBoot).commit();
    }

    /**
     * SharedPreferencesからBootCompletedフラグの値を取得.
     */
    public static boolean isBootCompleted(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getBoolean(KEY_BOOT_COMPLETED, false);
    }

    /**
     * デバイスの起動を検知するブロードキャストレシーバ.
     */
    public static class BootCompletedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)
                    && isBootCompleted(context)) {
                Intent launch = new Intent(context, Ch1304.class);
                launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launch.putExtra(INTENT_BOOT_FROM_RECIEVER, true);
                context.startActivity(launch);
            }
        }
    }
}
