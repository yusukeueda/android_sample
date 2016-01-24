package jp.co.se.android.recipe.chapter07;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;

/***
 * 
 * �����́@http://maoudamashii.jokersounds.com/music_rule.html�@���璸���܂���
 * 
 * @author yokmama
 * 
 */
@SuppressLint("NewApi")
public class Ch0704 extends Activity implements OnPreparedListener {
    private MediaPlayer mMediaPlayer1;
    private MediaPlayer mMediaPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0704_main);

        // �P�Ȗڂ̉����f�[�^���Đ�����MediaPlayer�̏�����
        mMediaPlayer1 = new MediaPlayer();
        mMediaPlayer1.setOnPreparedListener(this);
        // �Q�Ȗڂ̉����f�[�^���Đ�����MediaPlayer�̏�����
        mMediaPlayer2 = new MediaPlayer();
        mMediaPlayer2.setOnPreparedListener(this);

        try {
            // �P�Ȗڂ̉����f�[�^��ݒ�
            mMediaPlayer1.setDataSource(
                    this,
                    Uri.parse("android.resource://" + getPackageName() + "/"
                            + R.raw.bgm_healing02));
            // �P�Ȗڂ̉����f�[�^��ǂݍ���
            mMediaPlayer1.prepareAsync();
            // �Q�Ȗڂ̉����f�[�^��ݒ�
            mMediaPlayer2.setDataSource(
                    this,
                    Uri.parse("android.resource://" + getPackageName() + "/"
                            + R.raw.bgm_healing03));
            // �Q�Ȗڂ̉����f�[�^��ǂݍ���
            mMediaPlayer2.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ���f�B�A�v���C���[���Đ����Ȃ��~
        if (mMediaPlayer1.isPlaying()) {
            mMediaPlayer1.stop();
        }
        // ���f�B�A�v���C���[���Đ����Ȃ��~
        if (mMediaPlayer2.isPlaying()) {
            mMediaPlayer2.stop();
        }

        // ���f�B�A�v���C���[�����
        mMediaPlayer1.release();
        mMediaPlayer2.release();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp == mMediaPlayer1) {
            // �P�Ȗڂ̍Đ��������������̂ōĐ����J�n
            mMediaPlayer1.start();
        } else if (mp == mMediaPlayer2) {
            // �Q�Ȗڂ̍Đ��������������̂Ŏ��̋Ȃɐݒ�
            mMediaPlayer1.setNextMediaPlayer(mMediaPlayer2);
        }
    }
}
