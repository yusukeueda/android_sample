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
                // ������
            } else if ("back".equals(action)) {
                // ������
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

        // Notification�̓o�^
        startForeground(1, generateNotification());
    }

    private void pause() {
        mMediaPlayer.pause();
    }

    private void stop() {
        mMediaPlayer.stop();
        mMediaPlayer = null;

        // Notification�̉���
        stopForeground(true);
    }

    private Notification generateNotification() {
        // �ʒm�̈�^�b�v����PendingIntent�𐶐�
        Intent actionIntent = new Intent(getApplicationContext(), Ch0708.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
                0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // �Ǝ����C�A�E�g��RemoveView�𐶐�
        RemoteViews mNotificationView = new RemoteViews(getPackageName(),
                R.layout.ch0708_statusbar);

        // Notification�̐���
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext());
        builder.setSmallIcon(R.drawable.ic_stat_media);
        // �Ǝ����C�A�E�g��Notificaiton�ɐݒ�
        builder.setContent(mNotificationView);
        // true�ŏ�ɒʒm�̈�ɕ\��
        builder.setOngoing(true);
        // �ʒm�̈�ɏ����\�����̃��b�Z�[�W��ݒ�
        builder.setTicker("Sample Title���Đ�");
        builder.setContentIntent(pi);

        // �X�e�[�^�X�o�[�̃��C�A�E�g�ɐݒ肳��Ă���C���[�W�A�C�R���ɃA�C�R����ݒ�
        mNotificationView.setImageViewResource(R.id.imageicon,
                R.drawable.ic_launcher);

        // �X�e�[�^�X�o�[�̃��C�A�E�g�ɐݒ肳��Ă���^�C�g�����Ƀ^�C�g����ݒ�
        mNotificationView.setTextViewText(R.id.textTitle, "Sample Title");
        // �X�e�[�^�X�o�[�̃��C�A�E�g�ɐݒ肳��Ă���A�[�e�B�X�g���ɃA�[�e�B�X�g��ݒ�
        mNotificationView.setTextViewText(R.id.textArtist, "Sample Artist");

        // [�C���[�W�A�C�R��]�{�^���������ꂽ�ۂɌĂ΂��Intent��ݒ�
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Ch0708.class), Intent.FLAG_ACTIVITY_NEW_TASK);
        mNotificationView
                .setOnClickPendingIntent(R.id.imageicon, contentIntent);

        // [�Đ�]�E[�ꎞ��~]�{�^���������ꂽ�ۂɌĂ΂��Intent��ݒ�
        mNotificationView.setOnClickPendingIntent(R.id.btnPlay,
                createPendingIntent("playpause"));

        // �@[����]�{�^���������ꂽ�ۂɌĂ΂��Intent��ݒ�
        mNotificationView.setOnClickPendingIntent(R.id.btnNext,
                createPendingIntent("next"));

        return builder.build();
    }

    private PendingIntent createPendingIntent(String action) {
        // �@Action�ɉ�����Service��Intent��ێ�����PendingIntent���쐬
        Intent service = new Intent(this, Ch0708Service.class);
        service.setAction(action);

        return PendingIntent.getService(this, 0, service, 0);
    }

}
