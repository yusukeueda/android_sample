package jp.co.se.android.recipe.chapter15;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class Ch1503 extends Activity {
    private static final String CONFIG_NAME = "appconfig";

    private EditText mEditConfigText;
    private CheckBox mCheckConfigCheck1;
    private CheckBox mCheckConfigCheck2;
    private Spinner mSpinnerConfigSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ch1503_main);

        mEditConfigText = (EditText) findViewById(R.id.editConfigText);
        mCheckConfigCheck1 = (CheckBox) findViewById(R.id.checkConfigCheck1);
        mCheckConfigCheck2 = (CheckBox) findViewById(R.id.checkConfigCheck2);
        mSpinnerConfigSelect = (Spinner) findViewById(R.id.spinnerConfigSelect);

        loadConfig();
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveConfig();
    }

    private void loadConfig() {
        // PrivateでCONFIG_NAMEのファイルを作成し、SharedPreferencesインスタンスを取得
        SharedPreferences pref = getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);

        // editConfigTextをキーにテキスト値を取得
        mEditConfigText.setText(pref.getString("editConfigText", ""));
        // checkConfigCheck1をキーにBoolean値を取得
        mCheckConfigCheck1.setChecked(pref.getBoolean("checkConfigCheck1",
                false));
        // checkConfigCheck2をキーにBoolean値を取得
        mCheckConfigCheck2.setChecked(pref.getBoolean("checkConfigCheck2",
                false));
        // spinnerConfigSelectをキーに整数値を取得
        mSpinnerConfigSelect
                .setSelection(pref.getInt("spinnerConfigSelect", 0));
    }

    private void saveConfig() {
        // PrivateでCONFIG_NAMEのファイルを作成し、SharedPreferencesインスタンスを取得
        SharedPreferences pref = getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();

        // editConfigTextをキーにテキスト値を設定
        editor.putString("editConfigText", mEditConfigText.getText().toString());
        // checkConfigCheck1をキーにBoolean値を設定
        editor.putBoolean("checkConfigCheck1", mCheckConfigCheck1.isChecked());
        // mCheckConfigCheck2をキーにBoolean値を設定
        editor.putBoolean("checkConfigCheck2", mCheckConfigCheck2.isChecked());
        // spinnerConfigSelectをキーに整数値を設定
        editor.putInt("spinnerConfigSelect",
                mSpinnerConfigSelect.getSelectedItemPosition());

        // 設定を反映
        editor.commit();
    }

}
