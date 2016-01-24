package jp.co.se.android.recipe.chapter07;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class Ch0703 extends Activity {
    private static final String TAG = "Ch0703";
    // �Đ�
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer;
    private String mFilePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0703_main);

        ToggleButton recSwitch = (ToggleButton) findViewById(R.id.toggleRecord);
        recSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (isChecked) {
                    // �^���̊J�n
                    startRecord();
                } else {
                    // �^���̒�~
                    if (mMediaRecorder != null) {
                        mMediaRecorder.stop();
                        mMediaRecorder.reset();
                    }
                }
            }
        });

        // �Đ��{�^���̃C�x���g�o�^
        findViewById(R.id.buttonPlay).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // �^�������������Đ�
                startPlay();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // MediaPlayer�̉������
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        // MediaRecorder�̉������
        if (mMediaRecorder != null) {
            if (mMediaRecorder != null) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        }
    }

    /**
     * MediaRecorder�����������^�����J�n
     */
    private void startRecord() {
        // �Đ����ł���΍Đ����~
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
        }

        // MediaRecorder�̏�����
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        // ���̓\�[�X���}�C�N�ɐݒ�
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // �ۑ��t�H�[�}�b�g��3gp�ɐݒ�
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // Audio�G���R�[�h���f�t�H���g�ɐݒ�
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        // �O���X�g���[�W�imicroSD�Ȃǁj�Ɂuhoge.3gp�v�Ƃ������O�ŕۑ�����
        String fileName = "hoge.3gp";
        mFilePath = Environment.getExternalStorageDirectory() + "/" + fileName;
        mMediaRecorder.setOutputFile(mFilePath);

        // �^������������������^���J�n
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString(), e);
        }
    }

    /**
     * MediaPlayer�����������������Đ�
     */
    private void startPlay() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        } else {
            mMediaPlayer.reset();
        }
        try {
            mMediaPlayer.setDataSource(mFilePath);
            // �����̍Đ����������������ۂɌĂяo����郊�X�i�[
            mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // �������Đ�
                    mp.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(Ch0703.this,
                            R.string.text_complete_recordplay,
                            Toast.LENGTH_SHORT).show();
                }
            });
            mMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString(), e);
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString(), e);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString(), e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString(), e);
        }
    }
}
