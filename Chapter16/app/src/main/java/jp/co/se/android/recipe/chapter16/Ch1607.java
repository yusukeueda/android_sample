package jp.co.se.android.recipe.chapter16;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Ch1607 extends Activity {
    private static final String TAG = "Ch1607";
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
                            helper = new ContactDbOpenHelper(Ch1607.this);
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
            db.beginTransaction();

            // 現在保存されているデータをすべて削除
            db.delete(Contact.TBNAME, null, null);

            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO ").append(Contact.TBNAME).append("(")
                    .append(Contact.NAME).append(",").append(Contact.AGE)
                    .append(") values(?, ?)");

            // プリコンパイルステートメントの生成
            SQLiteStatement stat = db.compileStatement(sql.toString());
            Log.d(TAG, "プリコンパイルステートメントの生成: " + sql.toString());

            for (int i = 0; i < NAMES.length; i++) {
                stat.bindString(1, NAMES[i]);
                stat.bindLong(2, 30);
                // データベースにContactのデータを生成
                long id = stat.executeInsert();
                Log.d(TAG, String.format("データを生成[%d]", id));
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

}
