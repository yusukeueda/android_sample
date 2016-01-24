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

        // デフォルトを設定
        Time time = new Time();
        time.setToNow();
        time.minute += 2; // 2分先を指定
        mEditHours.setText(Integer.toString(time.hour));
        mEditMinutes.setText(Integer.toString(time.minute));
        mEditText.setText("Androidレシピ本によるアラームです。");
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
            // 数値ではない値が入力されていた場合
            Toast.makeText(this, "数値を入力してください。", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // アラームのセットに失敗した場合。
            Toast.makeText(this, "設定に失敗しました。", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "設定失敗", e);
        }
    }

    /**
     * アラームを設定する
     */
    private void setAlarm(int hours, int minutes, String message, boolean isSkip)
            throws Exception {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        // 時間を指定する
        intent.putExtra(AlarmClock.EXTRA_HOUR, hours);
        // 分を指定する
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
        // メッセージを指定する
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        // 設定後、アラーム画面の表示をスキップするかどうかを指定する
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, isSkip);

        startActivity(intent);
    }
}
