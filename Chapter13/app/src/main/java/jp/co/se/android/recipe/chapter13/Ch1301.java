package jp.co.se.android.recipe.chapter13;

import jp.co.se.android.recipe.chapter13.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class Ch1301 extends Activity {
    /** Notificationの識別用ID */
    int ID_NOTIFICATION_SAMPLE_ACTIVITY = 0x00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1301_main);

        Switch notificationSwitch = (Switch) findViewById(R.id.notificationSwitch);
        notificationSwitch
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {
                        // スイッチの切替に連動してステータスバーをON・OFF
                        showNotification(isChecked);
                    }
                });

    }

    /**
     * ステータスバーを表示.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(boolean isShow) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (isShow) {
            // ステータスバーを通知
            // ブラウザを起動するPendingIntentを生成
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.url_shoeisha)));
            PendingIntent contentIntent = PendingIntent.getActivity(this,
                    ID_NOTIFICATION_SAMPLE_ACTIVITY, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            // Notificationの設定
            Notification.Builder nb = new Notification.Builder(this);
            // 通知イベントのタイムスタンプ
            nb.setWhen(System.currentTimeMillis());
            // コンテンツをセット
            nb.setContentIntent(contentIntent);
            // アイコンをセット
            nb.setSmallIcon(android.R.drawable.stat_notify_chat);
            // 通知時に表示する文字列
            nb.setTicker(getString(R.string.label_status_ticker));
            // ステータスバーに表示するタイトル
            nb.setContentTitle(getString(R.string.label_launch_browser));
            // 音とバイブとライト
            nb.setDefaults(Notification.DEFAULT_SOUND
                    | Notification.DEFAULT_VIBRATE
                    | Notification.DEFAULT_LIGHTS);
            // タップすると自動で表示を消す
            nb.setAutoCancel(true);
            Notification notification = nb.build();

            // Notificationを通知
            notificationManager.notify(ID_NOTIFICATION_SAMPLE_ACTIVITY,
                    notification);
        } else {
            // ステータスバーを消去
            // Notificationをキャンセル
            notificationManager.cancel(ID_NOTIFICATION_SAMPLE_ACTIVITY);
        }
    }
}
