package jp.co.se.android.recipe.chapter07;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.RemoteControlClient;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

@SuppressLint("NewApi")
public class Ch0707Service extends Service {
    public static final String TAG = "Chapter07";
    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;
    private RemoteControlClient mRemoteControlClient;
    private ComponentName mComponentName;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
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
            registerMediaButtonEventReceiver();
        }

        mMediaPlayer.start();

        // RemoteControlClient��o�^
        registerRemoteControlClient();

        // ���b�N�X�N���[���̏�Ԃ��Đ��ɐݒ�
        mRemoteControlClient
                .setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
        // ���b�N�X�N���[���ɕ\�����鉹�y����ݒ�
        mRemoteControlClient
                .editMetadata(true)
                .putString(MediaMetadataRetriever.METADATA_KEY_ARTIST,
                        "Sample Artist")
                .putString(MediaMetadataRetriever.METADATA_KEY_ALBUM,
                        "Sample Album")
                .putString(MediaMetadataRetriever.METADATA_KEY_TITLE,
                        "Sample Music").apply();
    }

    private void pause() {
        mMediaPlayer.pause();

        // ���b�N�X�N���[���̏�Ԃ��ꎞ��~�ɐݒ�
        mRemoteControlClient
                .setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);
    }

    private void stop() {
        mMediaPlayer.stop();
        mMediaPlayer = null;
        unregisterMediaButtonEventReceiver();

        // RemoteControlClient�̓o�^������
        unregisterRemoteControlClient();
    }

    private void registerMediaButtonEventReceiver() {
        if (mComponentName == null) {
            // MediaButtonEventReceiver��o�^����
            mComponentName = new ComponentName(this, Ch0707Receiver.class);
            // MediaButtonEventReceiver���V�X�e���ɓo�^����
            mAudioManager.registerMediaButtonEventReceiver(mComponentName);
        }
    }

    private void unregisterMediaButtonEventReceiver() {
        if (mComponentName != null) {
            // MediaButtonEventReceiver�̓o�^����������
            mAudioManager.unregisterMediaButtonEventReceiver(mComponentName);
            mComponentName = null;
        }

    }

    private void registerRemoteControlClient() {
        if (mRemoteControlClient == null) {
            // LockScreen�ŉ����ꂽ�{�^���̃C�x���g���󂯎�郌�V�[�o�[��PendingIntent�𐶐�
            Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            mediaButtonIntent.setComponent(mComponentName);
            PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(), 0, mediaButtonIntent, 0);

            // RemoteControlClient�𐶐����APendingIntent��ݒ�
            mRemoteControlClient = new RemoteControlClient(mediaPendingIntent);
            mRemoteControlClient
                    .setTransportControlFlags(RemoteControlClient.FLAG_KEY_MEDIA_PLAY
                            | RemoteControlClient.FLAG_KEY_MEDIA_PAUSE
                            | RemoteControlClient.FLAG_KEY_MEDIA_NEXT
                            | RemoteControlClient.FLAG_KEY_MEDIA_STOP);

            // RemoteControlClient��o�^
            mAudioManager.registerRemoteControlClient(mRemoteControlClient);

            // AudioFocus���擾����
            mAudioManager.requestAudioFocus(new OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    Log.d(TAG, "focusChanged:" + focusChange);
                }
            }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }

    private void unregisterRemoteControlClient() {
        if (mRemoteControlClient != null) {
            // RemoteControlClient�̓o�^������
            mAudioManager.unregisterRemoteControlClient(mRemoteControlClient);
            mRemoteControlClient = null;
        }

    }

}
