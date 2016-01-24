package jp.co.se.android.recipe.chapter16;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Ch1603 extends Activity {
    private static final String TAG = "Ch1603";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1603_main);

        findViewById(R.id.buttonStart).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startTest();
                    }
                });
    }

    private void startTest() {
        ContactDbOpenHelper helper = null;
        SQLiteDatabase db = null;
        try {
            // ContactDbOpenHelperを生成
            helper = new ContactDbOpenHelper(this);
            // 書き込み可能なSQLiteDatabaseインスタンスを取得
            db = helper.getWritableDatabase();

            // データの検索
            searchData(db);
        } finally {
            if (db != null) {
                db.close();
            }
            if (helper != null) {
                helper.close();
            }
        }
    }

    private void searchData(SQLiteDatabase db) {
        Cursor cursor = null;
        try {
            // Commentsテーブルのすべてのデータを取得
            cursor = db.query(Contact.TBNAME, null, Contact.AGE + " > ?",
                    new String[] { Integer.toString(20) }, null, null,
                    Contact.NAME);
            // Cursorにデータが１件以上ある場合処理を行う
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // 名前を取得
                    String name = cursor.getString(cursor
                            .getColumnIndex(Contact.NAME));
                    // 年齢を取得
                    int age = cursor.getInt(cursor.getColumnIndex(Contact.AGE));
                    Log.d(TAG, name + ":" + age);

                    // 次のデータへCursorを移動
                }
                ;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
