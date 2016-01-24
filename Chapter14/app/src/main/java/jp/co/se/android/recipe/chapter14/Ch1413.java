package jp.co.se.android.recipe.chapter14;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Ch1413 extends Activity implements OnClickListener {

    private static final String TAG = Ch1413.class.getSimpleName();
    private EditText mEditHours;
    private EditText mEditMinutes;
    private EditText mEditText;
    private Switch mSwitchSkipUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1413_main);

        mEditHours = (EditText) findViewById(R.id.editHours);
        mEditMinutes = (EditText) findViewById(R.id.editMinutes);
        mEditText = (EditText) findViewById(R.id.editText);
        mSwitchSkipUi = (Switch) findViewById(R.id.switchSkipUi);
        Button btnSetAlarm = (Button) findViewById(R.id.btnSetAlarm);
        btnSetAlarm.setOnClickListener(this);

        // �f�t�H���g��ݒ�
        Time time = new Time();
        time.setToNow();
        time.minute += 2; // 2������w��
        mEditHours.setText(Integer.toString(time.hour));
        mEditMinutes.setText(Integer.toString(time.minute));
        mEditText.setText("Android���V�s�{�ɂ��A���[���ł��B");
    }

    @Override
    public void onClick(View v) {
        String message = mEditText.getText().toString();
        String minutesStr = mEditMinutes.getText().toString();
        String hoursStr = mEditHours.getText().toString();
        boolean isSkip = mSwitchSkipUi.isChecked();

        try {
            setAlarm(Integer.parseInt(hoursStr), Integer.parseInt(minutesStr),
                    message, isSkip);
        } catch (NumberFormatException e) {
            // ���l�ł͂Ȃ��l�����͂���Ă����ꍇ
            Toast.makeText(this, "���l����͂��Ă��������B", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // �A���[���̃Z�b�g�Ɏ��s�����ꍇ�B
            Toast.makeText(this, "�ݒ�Ɏ��s���܂����B", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "�ݒ莸�s", e);
        }
    }

    /**
     * �A���[����ݒ肷��
     */
    private void setAlarm(int hours, int minutes, String message, boolean isSkip)
            throws Exception {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        // ���Ԃ��w�肷��
        intent.putExtra(AlarmClock.EXTRA_HOUR, hours);
        // �����w�肷��
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
        // ���b�Z�[�W���w�肷��
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        // �ݒ��A�A���[����ʂ̕\�����X�L�b�v���邩�ǂ������w�肷��
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, isSkip);

        startActivity(intent);
    }
}
