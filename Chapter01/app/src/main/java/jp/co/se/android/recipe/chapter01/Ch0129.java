package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;

public class Ch0129 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0129_main);

        final DatePicker dp = (DatePicker) findViewById(R.id.DatePicker);

        // カレンダーを非表示
        Switch swCalendar = (Switch) findViewById(R.id.HideCalendar);
        swCalendar.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                dp.setCalendarViewShown(!isChecked);
            }
        });

        // 年を非表示
        Switch swYear = (Switch) findViewById(R.id.HideYear);
        swYear.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                LinearLayout parentFrame = (LinearLayout) dp.getChildAt(0);
                LinearLayout dateFrame = (LinearLayout) parentFrame
                        .getChildAt(0);
                dateFrame.getChildAt(0).setVisibility(
                        !isChecked ? View.VISIBLE : View.GONE);
            }
        });
        // 月を非表示
        Switch swMonth = (Switch) findViewById(R.id.HideMonth);
        swMonth.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                LinearLayout parentFrame = (LinearLayout) dp.getChildAt(0);
                LinearLayout dateFrame = (LinearLayout) parentFrame
                        .getChildAt(0);
                dateFrame.getChildAt(1).setVisibility(
                        !isChecked ? View.VISIBLE : View.GONE);
            }
        });

        // 日を非表示
        Switch swDay = (Switch) findViewById(R.id.HideDay);
        swDay.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                LinearLayout parentFrame = (LinearLayout) dp.getChildAt(0);
                LinearLayout dateFrame = (LinearLayout) parentFrame
                        .getChildAt(0);
                dateFrame.getChildAt(2).setVisibility(
                        !isChecked ? View.VISIBLE : View.GONE);
            }
        });

    }

}
