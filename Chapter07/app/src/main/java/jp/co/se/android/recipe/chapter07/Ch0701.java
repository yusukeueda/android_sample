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
 * �����́@http://maoudamashii.jokersounds.com/music_rule.html�@���璸���܂���
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

        // SoundPool�̏�����
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        // �����f�[�^�̓ǂݍ��݊��������m���郊�X�i�[��ݒ�
        mSoundPool.setOnLoadCompleteListener(this);
        // �����f�[�^�̓ǂݍ��݊J�n
        mSoundID = mSoundPool.load(this, R.raw.se_quetion, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // SoundPool�̉��
        mSoundPool.release();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1) {
            // �����̍Đ�
            mSoundPool.play(mSoundID, 1.0F, 1.0F, 0, 0, 1.0F);
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        // �������ǂݍ��݊����ɂȂ������ߍĐ��{�^����L���ɂ���
        findViewById(R.id.button1).setEnabled(true);
    }
}
