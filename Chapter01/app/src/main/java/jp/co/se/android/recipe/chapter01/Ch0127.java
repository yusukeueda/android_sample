package jp.co.se.android.recipe.chapter01;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Toast;

public class Ch0127 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0127_main);

        // インスタンスを生成
        final DatePicker dp = (DatePicker) findViewById(R.id.DatePicker);

        // 現在の日付を取得
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // デイトピッカーの初期化
        dp.init(year, month, day, new OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year,
                    int monthOfYear, int dayOfMonth) {
                // 選択した日付を検出
                String date = year + "/" + monthOfYear + "/" + dayOfMonth;
                Toast.makeText(Ch0127.this, date, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
