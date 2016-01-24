package jp.co.se.android.recipe.chapter07;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

@SuppressLint("NewApi")
public class Ch0708Service extends Service {
    public static final String TAG = "Chapter07";
    private MediaPlayer mMediaPlayer;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if ("play".equals(action)) {
                if (mMediaPlayer == null || !mMediaPlayer.isPlaying()) {
                    play();
                }
            } else if ("pause".equals(action)) {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    pause();
                }
            } else if ("stop".equals(action)) {
                if (mMediaPlayer != null) {
                    stop();
                }
            } else if ("next".equals(action)) {
                // 未実装
            } else if ("back".equals(action)) {
                // 未実装
            } else if ("playpause".equals(action)) {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    pause();
                } else {
                    play();
                }
            }
        }

        return START_STICKY;
    }

    private void play() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            String fileName = "android.resource://" + getPackageName() + "/"
                    + R.raw.bgm_healing02;
            try {
                mMediaPlayer.setDataSource(this, Uri.parse(fileName));
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mMediaPlayer.start();

        // Notificationの登録
        startForeground(1, generateNotification());
    }

    private void pause() {
        mMediaPlayer.pause();
    }

    private void stop() {
        mMediaPlayer.stop();
        mMediaPlayer = null;

        // Notificationの解除
        stopForeground(true);
    }

    private Notification generateNotification() {
        // 通知領域タップ時のPendingIntentを生成
        Intent actionIntent = new Intent(getApplicationContext(), Ch0708.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
                0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 独自レイアウトのRemoveViewを生成
        RemoteViews mNotificationView = new RemoteViews(getPackageName(),
                R.layout.ch0708_statusbar);

        // Notificationの生成
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext());
        builder.setSmallIcon(R.drawable.ic_stat_media);
        // 独自レイアウトをNotificaitonに設定
        builder.setContent(mNotificationView);
        // trueで常に通知領域に表示
        builder.setOngoing(true);
        // 通知領域に初期表示時のメッセージを設定
        builder.setTicker("Sample Titleを再生");
        builder.setContentIntent(pi);

        // ステータスバーのレイアウトに設定されているイメージアイコンにアイコンを設定
        mNotificationView.setImageViewResource(R.id.imageicon,
                R.drawable.ic_launcher);

        // ステータスバーのレイアウトに設定されているタイトル名にタイトルを設定
        mNotificationView.setTextViewText(R.id.textTitle, "Sample Title");
        // ステータスバーのレイアウトに設定されているアーティスト名にアーティストを設定
        mNotificationView.setTextViewText(R.id.textArtist, "Sample Artist");

        // [イメージアイコン]ボタンを押された際に呼ばれるIntentを設定
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Ch0708.class), Intent.FLAG_ACTIVITY_NEW_TASK);
        mNotificationView
                .setOnClickPendingIntent(R.id.imageicon, contentIntent);

        // [再生]・[一時停止]ボタンを押された際に呼ばれるIntentを設定
        mNotificationView.setOnClickPendingIntent(R.id.btnPlay,
                createPendingIntent("playpause"));

        // 　[次へ]ボタンを押された際に呼ばれるIntentを設定
        mNotificationView.setOnClickPendingIntent(R.id.btnNext,
                createPendingIntent("next"));

        return builder.build();
    }

    private PendingIntent createPendingIntent(String action) {
        // 　Actionに応じたServiceのIntentを保持するPendingIntentを作成
        Intent service = new Intent(this, Ch0708Service.class);
        service.setAction(action);

        return PendingIntent.getService(this, 0, service, 0);
    }

}
