package jp.co.se.android.recipe.chapter02;

import java.util.TimeZone;

import jp.co.se.android.recipe.chapter02.R;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.TimeFormatException;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class Ch0235DialogPreference extends DialogPreference {

    /**
     * Preferenceで利用する値。プリファレンスXMLにandroid:defaultValueが定義されていない場合は、この値が利用される。
     */
    private String mPreferenceValue = "";

    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

    public Ch0235DialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        // ダイアログのレイアウトとして利用するリソースを指定
        setDialogLayoutResource(R.layout.ch0235_main);

        // OKボタンのラベルを指定
        setPositiveButtonText(android.R.string.ok);
        // キャンセルボタンのラベルを指定
        setNegativeButtonText(android.R.string.cancel);
    }

    /**
     * 値の初期化を行う。android:defaultValueがない場合は呼ばれない。
     */
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue,
            Object defaultValue) {
        if (restorePersistedValue) {
            mPreferenceValue = getPersistedString(mPreferenceValue);
        } else {
            mPreferenceValue = (String) defaultValue;
            persistString(mPreferenceValue);
        }
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);

        // 値を取得
        setTimeToView(mPreferenceValue);
    }

    /** 値を画面へ反映 */
    private void setTimeToView(String preferenceValue) {
        // 指定された文字列をTimeへ変換
        Time time = new Time(TimeZone.getDefault().getID());
        try {
            time.parse(preferenceValue);
        } catch (TimeFormatException e) {
            // 値の変換に失敗した場合（既に不正な値が入っている場合など）、現在時刻にする
            time.setToNow();
        }

        // 値を画面へセット
        mDatePicker.updateDate(time.year, time.month, time.monthDay);
        mTimePicker.setCurrentHour(time.hour);
        mTimePicker.setCurrentMinute(time.minute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // 時間を文字列へ変更
            Time time = new Time(TimeZone.getDefault().getID());
            time.year = mDatePicker.getYear();
            time.month = mDatePicker.getMonth();
            time.monthDay = mDatePicker.getDayOfMonth();
            time.hour = mTimePicker.getCurrentHour();
            time.minute = mTimePicker.getCurrentMinute();
            String newValue = time.format2445();
            if (callChangeListener(newValue)) {
                mPreferenceValue = newValue;
                persistString(mPreferenceValue);
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }

        // 値を保存する
        final PreferenceSavedState myState = new PreferenceSavedState(
                superState);
        myState.value = mPreferenceValue;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null
                || !state.getClass().equals(PreferenceSavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }

        // 値を保持する
        PreferenceSavedState myState = (PreferenceSavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        // 元の値を利用して、画面を構成
        setTimeToView(myState.value);
    }

    /**
     * 値の保存を行うためのクラス
     */
    private static class PreferenceSavedState extends BaseSavedState {
        String value;

        public PreferenceSavedState(Parcelable superState) {
            super(superState);
        }

        public PreferenceSavedState(Parcel source) {
            super(source);
            value = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<PreferenceSavedState> CREATOR = new Parcelable.Creator<PreferenceSavedState>() {

            public PreferenceSavedState createFromParcel(Parcel in) {
                return new PreferenceSavedState(in);
            }

            public PreferenceSavedState[] newArray(int size) {
                return new PreferenceSavedState[size];
            }
        };
    }
}
