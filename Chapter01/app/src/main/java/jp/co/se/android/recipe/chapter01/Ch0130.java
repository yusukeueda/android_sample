package jp.co.se.android.recipe.chapter01;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class Ch0130 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0130_main);

        // デイトピッカーダイアログを表示
        final Button btnDatePicker = (Button) findViewById(R.id.DatePickerDialog);
        btnDatePicker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    /**
     * デイトピッカーダイアログを表示.
     */
    private void showDatePickerDialog() {
        // 現在の日付を取得
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dlgDatePicker = new DatePickerDialog(this,
                new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                            int monthOfYear, int dayOfMonth) {
                        // 選択した日付を検出
                        String date = year + "/" + monthOfYear + "/"
                                + dayOfMonth;
                        Toast.makeText(Ch0130.this, date, Toast.LENGTH_SHORT)
                                .show();
                    }
                }, year, month, day);
        dlgDatePicker.show();
    }
}
