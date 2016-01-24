package jp.co.se.android.recipe.chapter07;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

public class Ch0705 extends Activity {
    private MusicIntentReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0704_main);

        // �C���z���̐ڑ���Ԃ��Ď�����
        myReceiver = new MusicIntentReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // �C���z���̐ڑ���Ԃ���������
        unregisterReceiver(myReceiver);
    }

    private class MusicIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                case 0:
                    Toast.makeText(Ch0705.this, "�w�b�h�Z�b�g��������܂���",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(Ch0705.this, "�w�b�h�Z�b�g���������܂�܂���",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }
}
