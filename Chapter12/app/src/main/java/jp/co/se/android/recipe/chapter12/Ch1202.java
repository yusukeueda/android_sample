package jp.co.se.android.recipe.chapter12;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Ch1202 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1202_main);

        findViewById(R.id.buttonStart).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startAlarm();
                    }
                });
    }

    private void startAlarm() {
        // Ch1202Receiver���Ăяo���C���e���g���쐬
        Intent i = new Intent(getApplicationContext(), Ch1202Receiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, i, 0);

        // ���ݎ������15�b��̎��Ԃ�ݒ�
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 15);

        // AlramManager�擾
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        // AlramManager��PendingIntent��o�^
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }
}
