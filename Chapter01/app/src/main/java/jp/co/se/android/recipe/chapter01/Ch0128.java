package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;

public class Ch0128 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0128_main);

        // 設定したい日時を2010年6月30日とする
        int year = 2010;
        int month = 6 - 1;// 0から始まるので-1する
        int day = 30;

        // 日付をセット
        DatePicker dp = (DatePicker) findViewById(R.id.DatePicker);
        dp.updateDate(year, month, day);
    }
}
