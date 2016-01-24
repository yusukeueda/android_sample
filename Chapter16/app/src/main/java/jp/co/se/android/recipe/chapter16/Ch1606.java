package jp.co.se.android.recipe.chapter16;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Ch1606 extends Activity {
    private static final String TAG = "Ch1606";
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
        setContentView(R.layout.ch1606_main);

        findViewById(R.id.buttonStart).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ContactDbOpenHelper helper = null;
                        SQLiteDatabase db = null;
                        try {
                            // ContactDbOpenHelperを生成
                            helper = new ContactDbOpenHelper(Ch1606.this);
                            // 書き込み可能なSQLiteDatabaseインスタンスを取得
                            db = helper.getWritableDatabase();

                            // データの作成
                            createDataByTransaction(db);
                        } finally {
                            if (db != null) {
                                db.close();
                            }
                            if (helper != null) {
                                helper.close();
                            }
                        }
                    }
                });
    }

    private void createDataByTransaction(SQLiteDatabase db) {
        try {
            // トランザクションを開始
            db.beginTransaction();
            Log.d(TAG, "トランザクションを開始");

            // 現在保存されているデータをすべて削除
            db.delete(Contact.TBNAME, null, null);

            for (int i = 0; i < NAMES.length; i++) {
                // 生成するデータを格納するContentValuesを生成
                ContentValues values = new ContentValues();
                values.put(Contact.NAME, NAMES[i]);
                values.put(Contact.AGE, 20);
                // データベースにContactのデータを生成
                db.insert(Contact.TBNAME, null, values);
                Log.d(TAG, String.format("データを生成[%d]", i + 1));
            }
            // トランザクションを確定
            db.setTransactionSuccessful();
            Log.d(TAG, "トランザクションを確定");
        } finally {
            // トランザクションを終了
            db.endTransaction();
            Log.d(TAG, "トランザクションを終了");
        }
    }

}
