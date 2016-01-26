package jp.co.se.android.recipe.chapter13;

import jp.co.se.android.recipe.chapter13.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Ch1306 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1306_main);
        final TextView tvDescription = (TextView) findViewById(R.id.description);
        final Switch callSwitch = (Switch) findViewById(R.id.callSwitch);

        // �X�C�b�`��ON�EOFF
        callSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (isChecked) {
                    // �d�b�̏󋵂����m���郊�X�i�[���Z�b�g
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    tm.listen(mPhoneStateListener,
                            PhoneStateListener.LISTEN_CALL_STATE);

                    tvDescription
                            .setText(getString(R.string.label_detect_callphone));
                    tvDescription.setVisibility(View.VISIBLE);
                } else {
                    tvDescription.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * �d�b�̏�Ԃ��󂯎�郊�X�i�[.
     */
    PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String number) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                // ���M�����m
                Toast.makeText(Ch1306.this,
                        getString(R.string.label_detect_calling, number),
                        Toast.LENGTH_SHORT).show();
            } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                // �ʘb�����m
                Toast.makeText(Ch1306.this,
                        getString(R.string.label_detect_talking, number),
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
}
