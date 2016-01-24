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
 * 音源は　http://maoudamashii.jokersounds.com/music_rule.html　から頂きました
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

        // １曲目の音声データを再生するMediaPlayerの初期化
        mMediaPlayer1 = new MediaPlayer();
        mMediaPlayer1.setOnPreparedListener(this);
        // ２曲目の音声データを再生するMediaPlayerの初期化
        mMediaPlayer2 = new MediaPlayer();
        mMediaPlayer2.setOnPreparedListener(this);

        try {
            // １曲目の音声データを設定
            mMediaPlayer1.setDataSource(
                    this,
                    Uri.parse("android.resource://" + getPackageName() + "/"
                            + R.raw.bgm_healing02));
            // １曲目の音声データを読み込む
            mMediaPlayer1.prepareAsync();
            // ２曲目の音声データを設定
            mMediaPlayer2.setDataSource(
                    this,
                    Uri.parse("android.resource://" + getPackageName() + "/"
                            + R.raw.bgm_healing03));
            // ２曲目の音声データを読み込む
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
        // メディアプレイヤーが再生中なら停止
        if (mMediaPlayer1.isPlaying()) {
            mMediaPlayer1.stop();
        }
        // メディアプレイヤーが再生中なら停止
        if (mMediaPlayer2.isPlaying()) {
            mMediaPlayer2.stop();
        }

        // メディアプレイヤーを解放
        mMediaPlayer1.release();
        mMediaPlayer2.release();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp == mMediaPlayer1) {
            // １曲目の再生準備が整ったので再生を開始
            mMediaPlayer1.start();
        } else if (mp == mMediaPlayer2) {
            // ２曲目の再生準備が整ったので次の曲に設定
            mMediaPlayer1.setNextMediaPlayer(mMediaPlayer2);
        }
    }
}
