package jp.co.se.android.recipe.chapter11;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

public class Ch1115 extends Activity {
    private AirPlaneModeReciever mAirPlaneModeReciever;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1115_main);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // �@�����[�h�����o����u���[�h�L���X�g���V�[�o������
        if (mAirPlaneModeReciever != null) {
            unregisterReceiver(mAirPlaneModeReciever);
            mAirPlaneModeReciever = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // �@�����[�h�����o����u���[�h�L���X�g���V�[�o��o�^
        if (mAirPlaneModeReciever == null) {
            mAirPlaneModeReciever = new AirPlaneModeReciever();
            registerReceiver(mAirPlaneModeReciever, new IntentFilter(
                    Intent.ACTION_AIRPLANE_MODE_CHANGED));
        }
    }

    /**
     * �@�����[�h�����o����u���[�h�L���X�g���V�[�o.
     * 
     * @author Re:Kayo-System / m.iwasaki
     */
    private class AirPlaneModeReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // �@�����[�h�̏�Ԃ��擾���ĉ�ʂɕ\��
            boolean state = intent.getBooleanExtra("state", false);
            TextView tvDescription = (TextView) findViewById(R.id.description);
            if (state) {
                tvDescription
                        .setText(getString(R.string.ch1115_label_airplanemode_enable));
            } else {
                tvDescription
                        .setText(getString(R.string.ch1115_label_airplanemode_disable));
            }
        }
    }

}
