package jp.co.se.android.recipe.chapter14;

import java.util.ArrayList;
import java.util.TimeZone;

import jp.co.se.android.recipe.chapter14.Ch1411.CalendarBean;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Ch1412 extends Activity implements OnClickListener {

    private EditText mEditTitle;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1412_main);

        mEditTitle = (EditText) findViewById(R.id.editTitle);
        mSpinner = (Spinner) findViewById(R.id.spnCalendars);
        Button btnDone = (Button) findViewById(R.id.btnDone);

        btnDone.setOnClickListener(this);

        // �J�����_�[��ID�ꗗ���擾
        ArrayList<CalendarBean> calendarList = Ch1411.getCalendarList(this);
        ArrayAdapter<CalendarBean> calendarAdapter = new ArrayAdapter<CalendarBean>(
                this, android.R.layout.simple_spinner_dropdown_item,
                calendarList);
        mSpinner.setAdapter(calendarAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnDone) {
            CalendarBean selectedItem = (CalendarBean) mSpinner
                    .getSelectedItem();
            String title = mEditTitle.getText().toString();

            // �C�x���g��ǉ�
            insertEvent(selectedItem.id, title);

            Toast.makeText(this, "�o�^���܂����B", Toast.LENGTH_SHORT).show();

            // �o�^����
            Intent intent = new Intent(this, Ch1411.class);
            startActivity(intent);
        }
    }

    private long insertEvent(long calendarId, String title) {
        // �f�t�H���g�̃^�C���]�[���擾
        // Asia/Tokyo �Ȃ�
        String timezone = TimeZone.getDefault().getID();

        // �C�x���g�̒ǉ�
        long nowDate = System.currentTimeMillis();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, "Android���V�s�ɂ��C�x���g�ł��B");
        values.put(CalendarContract.Events.DTSTART, nowDate);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timezone);
        values.put(CalendarContract.Events.DTEND, nowDate);
        values.put(CalendarContract.Events.EVENT_END_TIMEZONE, timezone);
        values.put(CalendarContract.Events.ALL_DAY, 1);

        Uri insert = getContentResolver().insert(
                CalendarContract.Events.CONTENT_URI, values);
        long eventId = ContentUris.parseId(insert);
        return eventId;
    }
}
