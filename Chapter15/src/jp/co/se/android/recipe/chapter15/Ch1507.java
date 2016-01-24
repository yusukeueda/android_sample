package jp.co.se.android.recipe.chapter15;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Ch1507 extends Activity {

    private ListView mListAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ch1507_main);

        mListAddress = (ListView) findViewById(R.id.listAddress);

        loadAddress();
    }

    private void loadAddress() {
        // 連絡帳の情報をコンテンツプロバイダ経由で取得
        Cursor cursor = getContentResolver().query(Phone.CONTENT_URI, null,
                null, null, null);

        // 取得した情報を表示するためのSimpleCursorAdapterを生成
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.ch1507_listrow, cursor, new String[] {
                        Phone.DISPLAY_NAME, Phone.NUMBER }, new int[] {
                        R.id.textName, R.id.textPhone }, 0);

        // 表示用のアダプターとして生成したSimpleCursorAdapterをListViewに設定
        mListAddress.setAdapter(adapter);
    }
}
