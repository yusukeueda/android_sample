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
 * 音源は　http://maoudamashii.jokersounds.com/music_rule.html　から頂きました
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

        // メディアプレイヤーの初期化
        mMediaPlayer = new MediaPlayer();
        // メディアの再生準備完了の通知を受け取るリスナーの設定
        mMediaPlayer.setOnPreparedListener(this);
        // メディアの再生完了の通知を受け取るリスナーの設定
        mMediaPlayer.setOnCompletionListener(this);

        // メディアファイルを指すパスを作成
        String fileName = "android.resource://" + getPackageName() + "/"
                + R.raw.bgm_healing02;
        try {
            // メディアファイルをMediaPlayerに設定
            mMediaPlayer.setDataSource(this, Uri.parse(fileName));
            // メディアファイルを非同期で読み込む
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

        // Eaulizerを生成
        mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId());

        LinearLayout layoutBandlevels = (LinearLayout) findViewById(R.id.layoutBandlevels);
        LayoutInflater layoutInflater = getLayoutInflater();

        // イコライザのバンド数の数だけSeekBarを生成
        short bands = mEqualizer.getNumberOfBands();
        for (int i = 0; i < bands; i++) {
            View layout = layoutInflater.inflate(R.layout.ch0709_seekbar,
                    layoutBandlevels, false);
            layoutBandlevels.addView(layout,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mSeekBars.add(layout);
            SeekBar seekbar = (SeekBar) layout.findViewById(R.id.seekBar);
            // リスナーの中でどのバンドのSeekBarであるか判断するためにタグに値を設定しておく
            seekbar.setTag(i);
            // SeekBarのタッチでSeekBarの値を取得し、Equalizerに設定
            seekbar.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        SeekBar seekbar = (SeekBar) v;
                        short maxEQLevel = mEqualizer.getBandLevelRange()[1];
                        // 事前に設定しておいたSeekBarの番号を取得
                        int index = (Integer) seekbar.getTag();
                        // 変更されたSeekBarの値を取得し最小値で補正
                        short band = (short) (seekbar.getProgress() - maxEQLevel);
                        View layout = mSeekBars.get(index);
                        // 変更されたSeekBarの値を元に表示とイコライザのバンドの値を更新
                        TextView textFreq = (TextView) layout
                                .findViewById(R.id.textFreq);
                        textFreq.setText(String.format("%6d", band));
                        // イコライザにバンドの値を設定
                        mEqualizer.setBandLevel((short) index, band);
                    }
                    return false;
                }
            });
        }

        // イコライザの表示を更新
        updateEqlizerValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // メディアプレイヤーが再生中なら停止
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        // メディアプレイヤーを解放
        mMediaPlayer.release();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonPlayPause) {
            if (mMediaPlayer.isPlaying()) {
                // メディアプレイヤーが再生中なら停止
                mMediaPlayer.pause();
                setButtonText(mMediaPlayer);
            } else {
                // メディアプレイヤーが再生中でないなら再生
                mMediaPlayer.start();
                setButtonText(mMediaPlayer);
            }
        } else if (v.getId() == R.id.checkEqulizer) {
            CheckBox checkbox = (CheckBox) v;
            // イコライザの設定を有効または無効に設定
            mEqualizer.setEnabled(checkbox.isChecked());
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // メディアプレイヤーが再生可能になったので再生ボタンを有効にする
        mButtonPlayPause.setEnabled(true);
        setButtonText(mp);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // メディアプレイヤーの再生が終わったのでボタンの状態を変更
        setButtonText(mp);
    }

    private void setButtonText(MediaPlayer mp) {
        if (mp.isPlaying()) {
            // 再生中なら停止を表示
            mButtonPlayPause.setText(getString(R.string.text_stop));
        } else {
            // 停止中なら再生を表示
            mButtonPlayPause.setText(getString(R.string.text_play));
        }
    }

    private void updateEqlizerValue() {
        // イコライザのバンド数を取得
        short bands = mEqualizer.getNumberOfBands();
        // イコライザのバンドの最小値
        short minEQLevel = mEqualizer.getBandLevelRange()[0];
        // イコライザのバンドの最大値
        short maxEQLevel = mEqualizer.getBandLevelRange()[1];
        // イコライザのバンドの数だけSeekBarとTextViewの初期値を設定
        for (int i = 0; i < bands; i++) {
            if (mSeekBars.size() > i) {
                View layout = mSeekBars.get(i);
                SeekBar seekbar = (SeekBar) layout.findViewById(R.id.seekBar);
                TextView textFreq = (TextView) layout
                        .findViewById(R.id.textFreq);
                TextView textFreqMax = (TextView) layout
                        .findViewById(R.id.textFreqMax);
                seekbar.setMax(maxEQLevel + Math.abs(minEQLevel));
                // 現在のイコライザのバンドの値を取得
                short band = mEqualizer.getBandLevel((short) i);
                // イコライザの周波数帯の値を取得
                int freq = mEqualizer.getCenterFreq((short) i) / 1000;
                // SeekBarにバンドの値を設定
                seekbar.setProgress(band + Math.abs(minEQLevel));
                textFreq.setText(String.format("%6d", band));
                textFreqMax.setText(String.format("%6dHz", freq));
            }
        }
    }
}
