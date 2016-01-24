package jp.co.se.android.recipe.chapter16;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Ch1602 extends Activity {
    private static final String TAG = "Ch1602";

    // @formatter:off
    private String[] NAMES = new String[] {
            "Anastassia", "Juan", "Enrique",
            "Frannie", "Paloma", "Francisco",
            "Lorenzio", "Maryvonne", "Siv",
            "Georgie", "Casimir", "Catharine",
            "Joker"};
    // @formatter:on

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1602_main);

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

            // データの作成
            createData(db);
            // データの更新
            updateData(db);
            // データの削除
            deleteData(db);
        } finally {
            if (db != null) {
                db.close();
            }
            if (helper != null) {
                helper.close();
            }
        }
    }

    private void createData(SQLiteDatabase db) {
        for (int i = 0; i < NAMES.length; i++) {
            // 生成するデータを格納するContentValuesを生成
            ContentValues values = new ContentValues();
            values.put(Contact.NAME, NAMES[i]);
            values.put(Contact.AGE, 20);
            // 戻り値は生成されたデータの_IDが返却される
            long id = db.insert(Contact.TBNAME, null, values);
            Log.d(TAG, "insert data:" + id);
        }
    }

    private void updateData(SQLiteDatabase db) {
        // 更新データを格納するContentValuesを生成
        ContentValues values = new ContentValues();
        // Contact.NAMEにaが含まれるデータの年齢を25に変更
        values.put(Contact.AGE, 25);
        // 戻り値は更新した数が返却される
        int n = db.update(Contact.TBNAME, values, Contact.NAME + " like ?",
                new String[] { "%a%" });
        Log.d(TAG, "insert data:" + n);
    }

    private void deleteData(SQLiteDatabase db) {
        // Contact.NAMEがJokerのデータを削除
        // 戻り値は削除した数が返却される
        int n = db.delete(Contact.TBNAME, Contact.NAME + " = ?",
                new String[] { "Joker" });
        Log.d(TAG, "delete data:" + n);
    }

}
