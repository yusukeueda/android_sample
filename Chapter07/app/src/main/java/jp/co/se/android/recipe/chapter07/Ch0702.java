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
 * 音源は　http://maoudamashii.jokersounds.com/music_rule.html　から頂きました
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
}
