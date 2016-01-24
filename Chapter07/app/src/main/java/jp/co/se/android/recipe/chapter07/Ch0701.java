package jp.co.se.android.recipe.chapter07;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/***
 * 
 * 音源は　http://maoudamashii.jokersounds.com/music_rule.html　から頂きました
 * 
 * @author yokmama
 * 
 */
public class Ch0701 extends Activity implements OnClickListener,
        OnLoadCompleteListener {
    private SoundPool mSoundPool;
    private int mSoundID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0701_main);

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button1).setEnabled(false);

        // SoundPoolの初期化
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        // 音声データの読み込み完了を検知するリスナーを設定
        mSoundPool.setOnLoadCompleteListener(this);
        // 音声データの読み込み開始
        mSoundID = mSoundPool.load(this, R.raw.se_quetion, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // SoundPoolの解放
        mSoundPool.release();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1) {
            // 音声の再生
            mSoundPool.play(mSoundID, 1.0F, 1.0F, 0, 0, 1.0F);
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        // 音声が読み込み完了になったため再生ボタンを有効にする
        findViewById(R.id.button1).setEnabled(true);
    }
}
