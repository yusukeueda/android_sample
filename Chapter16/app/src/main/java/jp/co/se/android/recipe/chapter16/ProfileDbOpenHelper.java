package jp.co.se.android.recipe.chapter16;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProfileDbOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "ProfileDbOpenHelper";
    static final String DATABASE_NAME = "profile.db";
    static final int DATABASE_VERSION = 1;

    public ProfileDbOpenHelper(Context context) {
        // データベースファイル名とバージョンを指定しSQLiteOpenHelperクラスを初期化
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "ProfileDbOpenHelperのコンストラクタが呼ばれました");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d(TAG, "ProfileDbOpenHelper.onCreateが呼ばれました");
        // Profileテーブルを生成
        // @formatter:off
	database.execSQL("CREATE TABLE " 
        + Profile.TBNAME + "(" 
        + Profile._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
        + Profile.NAME + " TEXT NOT NULL, " 
        + Profile.PHOTOGRAPHMAGE + " blob"+ ");");
	// @formatter:on
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "ProfileDbOpenHelper.onUpgradeが呼ばれました");
        // Profileテーブルを再定義するため現在のテーブルを削除
        db.execSQL("DROP TABLE IF EXISTS " + Profile.TBNAME);
        onCreate(db);
    }

}