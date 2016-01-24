package jp.co.se.android.recipe.chapter07;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/***
 * 
 * �����́@http://maoudamashii.jokersounds.com/music_rule.html�@���璸���܂���
 * 
 * @author yokmama
 * 
 */
public class Ch0702 extends Activity implements OnClickListener,
        OnPreparedListener, OnCompletionListener {
    private Button mButtonPlayPause;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0702_main);

        mButtonPlayPause = (Button) findViewById(R.id.buttonPlayPause);
        mButtonPlayPause.setOnClickListener(this);
        mButtonPlayPause.setEnabled(false);

        // ���f�B�A�v���C���[�̏�����
        mMediaPlayer = new MediaPlayer();
        // ���f�B�A�̍Đ����������̒ʒm���󂯎�郊�X�i�[�̐ݒ�
        mMediaPlayer.setOnPreparedListener(this);
        // ���f�B�A�̍Đ������̒ʒm���󂯎�郊�X�i�[�̐ݒ�
        mMediaPlayer.setOnCompletionListener(this);

        // ���f�B�A�t�@�C�����w���p�X���쐬
        String fileName = "android.resource://" + getPackageName() + "/"
                + R.raw.bgm_healing02;
        try {
            // ���f�B�A�t�@�C����MediaPlayer�ɐݒ�
            mMediaPlayer.setDataSource(this, Uri.parse(fileName));
            // ���f�B�A�t�@�C����񓯊��œǂݍ���
            mMediaPlayer.prepareAsync();
            setButtonText(mMediaPlayer);

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
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        // ���f�B�A�v���C���[�����
        mMediaPlayer.release();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonPlayPause) {
            if (mMediaPlayer.isPlaying()) {
                // ���f�B�A�v���C���[���Đ����Ȃ��~
                mMediaPlayer.pause();
                setButtonText(mMediaPlayer);
            } else {
                // ���f�B�A�v���C���[���Đ����łȂ��Ȃ�Đ�
                mMediaPlayer.start();
                setButtonText(mMediaPlayer);
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // ���f�B�A�v���C���[���Đ��\�ɂȂ����̂ōĐ��{�^����L���ɂ���
        mButtonPlayPause.setEnabled(true);
        setButtonText(mp);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // ���f�B�A�v���C���[�̍Đ����I������̂Ń{�^���̏�Ԃ�ύX
        setButtonText(mp);
    }

    private void setButtonText(MediaPlayer mp) {
        if (mp.isPlaying()) {
            // �Đ����Ȃ��~��\��
            mButtonPlayPause.setText(getString(R.string.text_stop));
        } else {
            // ��~���Ȃ�Đ���\��
            mButtonPlayPause.setText(getString(R.string.text_play));
        }
    }
}
