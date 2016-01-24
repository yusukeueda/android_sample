package jp.co.se.android.recipe.chapter07;

import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/***
 * 
 * �����́@http://maoudamashii.jokersounds.com/music_rule.html�@���璸���܂���
 * 
 * @author yokmama
 * 
 */
@SuppressLint("NewApi")
public class Ch0709 extends Activity implements OnClickListener,
        OnPreparedListener, OnCompletionListener {
    public static final String TAG = "Chapter07";
    private Button mButtonPlayPause;
    private MediaPlayer mMediaPlayer;
    private Equalizer mEqualizer;
    private ArrayList<View> mSeekBars = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0709_main);

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

        findViewById(R.id.checkEqulizer).setOnClickListener(this);

        // Eaulizer�𐶐�
        mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId());

        LinearLayout layoutBandlevels = (LinearLayout) findViewById(R.id.layoutBandlevels);
        LayoutInflater layoutInflater = getLayoutInflater();

        // �C�R���C�U�̃o���h���̐�����SeekBar�𐶐�
        short bands = mEqualizer.getNumberOfBands();
        for (int i = 0; i < bands; i++) {
            View layout = layoutInflater.inflate(R.layout.ch0709_seekbar,
                    layoutBandlevels, false);
            layoutBandlevels.addView(layout,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mSeekBars.add(layout);
            SeekBar seekbar = (SeekBar) layout.findViewById(R.id.seekBar);
            // ���X�i�[�̒��łǂ̃o���h��SeekBar�ł��邩���f���邽�߂Ƀ^�O�ɒl��ݒ肵�Ă���
            seekbar.setTag(i);
            // SeekBar�̃^�b�`��SeekBar�̒l���擾���AEqualizer�ɐݒ�
            seekbar.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        SeekBar seekbar = (SeekBar) v;
                        short maxEQLevel = mEqualizer.getBandLevelRange()[1];
                        // ���O�ɐݒ肵�Ă�����SeekBar�̔ԍ����擾
                        int index = (Integer) seekbar.getTag();
                        // �ύX���ꂽSeekBar�̒l���擾���ŏ��l�ŕ␳
                        short band = (short) (seekbar.getProgress() - maxEQLevel);
                        View layout = mSeekBars.get(index);
                        // �ύX���ꂽSeekBar�̒l�����ɕ\���ƃC�R���C�U�̃o���h�̒l���X�V
                        TextView textFreq = (TextView) layout
                                .findViewById(R.id.textFreq);
                        textFreq.setText(String.format("%6d", band));
                        // �C�R���C�U�Ƀo���h�̒l��ݒ�
                        mEqualizer.setBandLevel((short) index, band);
                    }
                    return false;
                }
            });
        }

        // �C�R���C�U�̕\�����X�V
        updateEqlizerValue();
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
        } else if (v.getId() == R.id.checkEqulizer) {
            CheckBox checkbox = (CheckBox) v;
            // �C�R���C�U�̐ݒ��L���܂��͖����ɐݒ�
            mEqualizer.setEnabled(checkbox.isChecked());
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

    private void updateEqlizerValue() {
        // �C�R���C�U�̃o���h�����擾
        short bands = mEqualizer.getNumberOfBands();
        // �C�R���C�U�̃o���h�̍ŏ��l
        short minEQLevel = mEqualizer.getBandLevelRange()[0];
        // �C�R���C�U�̃o���h�̍ő�l
        short maxEQLevel = mEqualizer.getBandLevelRange()[1];
        // �C�R���C�U�̃o���h�̐�����SeekBar��TextView�̏����l��ݒ�
        for (int i = 0; i < bands; i++) {
            if (mSeekBars.size() > i) {
                View layout = mSeekBars.get(i);
                SeekBar seekbar = (SeekBar) layout.findViewById(R.id.seekBar);
                TextView textFreq = (TextView) layout
                        .findViewById(R.id.textFreq);
                TextView textFreqMax = (TextView) layout
                        .findViewById(R.id.textFreqMax);
                seekbar.setMax(maxEQLevel + Math.abs(minEQLevel));
                // ���݂̃C�R���C�U�̃o���h�̒l���擾
                short band = mEqualizer.getBandLevel((short) i);
                // �C�R���C�U�̎��g���т̒l���擾
                int freq = mEqualizer.getCenterFreq((short) i) / 1000;
                // SeekBar�Ƀo���h�̒l��ݒ�
                seekbar.setProgress(band + Math.abs(minEQLevel));
                textFreq.setText(String.format("%6d", band));
                textFreqMax.setText(String.format("%6dHz", freq));
            }
        }
    }
}
