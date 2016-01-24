package jp.co.se.android.recipe.chapter15;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

public class MyContentProvider extends ContentProvider {
    // コンテンツプロバイダへアクセスする際に作成されるUriのAuthority
    public static final String AUTHORITY = "jp.co.se.android.recipe.chapter15";
    // AuthorityをふくめたUriの文字列
    public static final String CONTENT_AUTHORITY = "content://" + AUTHORITY
            + "/";

    // コメントテーブルへアクセスするUri
    public static final Uri COMMENTS_CONTENT_URI = Uri.parse(CONTENT_AUTHORITY
            + MySQLiteOpenHelper.TABLE_COMMENTS);

    // コメントのUriの判別コード
    public static final int CODE_COMMENT = 0;
    // ID指定によるコメントのUriの判別コード
    public static final int CODE_COMMENT_ID = 1;

    // コンテンツプロバイダ内で共通で使用するSQLiteOpenHelperインスタンス
    private MySQLiteOpenHelper mDatabaseHelper = null;
    // Uriの判別で使用
    private UriMatcher mUriMatcher = null;

    @Override
    public boolean onCreate() {
        // Uri判別のためUriMatcherを生成
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // コメントのUri判別のためのパターンを登録
        mUriMatcher.addURI(AUTHORITY, MySQLiteOpenHelper.TABLE_COMMENTS,
                CODE_COMMENT);
        // ID指定によるコメントのUri判別のためのパターンを登録
        mUriMatcher.addURI(AUTHORITY, MySQLiteOpenHelper.TABLE_COMMENTS + "/#",
                CODE_COMMENT_ID);

        // SQLiteOpenHelperインスタンスを取得
        mDatabaseHelper = new MySQLiteOpenHelper(getContext());

        return true;
    }

    @Override
    public String getType(Uri uri) {
        // 本サンプルでは複数のUriに対応していないため未実装
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        // 検索queryを作成するためSQLiteQueryBuilderを生成
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // UriMatcherを用いてUriから検索するデータ種別を判定
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT:
            // コメントに対するquery設定
            queryBuilder.setTables(MySQLiteOpenHelper.TABLE_COMMENTS);
            break;
        case CODE_COMMENT_ID:
            // ID指定のコメントに対するquery設定
            queryBuilder.setTables(MySQLiteOpenHelper.TABLE_COMMENTS);
            queryBuilder.appendWhere(BaseColumns._ID + "="
                    + uri.getLastPathSegment());
            break;
        default:
            // 予期されていないデータ種別なので例外を発生
            throw new IllegalArgumentException("Unknown URI");
        }

        // queryを実行し検索結果を返却
        Cursor cursor = queryBuilder.query(
                mDatabaseHelper.getReadableDatabase(), projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // UriMatcherを用いてUriから削除するデータ種別を判定
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT: {
            // コメントに対するdeleteを実行し削除数を返却
            return delete(MySQLiteOpenHelper.TABLE_COMMENTS, selection,
                    selectionArgs);
        }
        case CODE_COMMENT_ID: {
            // UriからIDを取得
            long id = ContentUris.parseId(uri);
            // ID指定のコメントに対するdeleteを実行し削除数を返却
            return delete(MySQLiteOpenHelper.TABLE_COMMENTS, BaseColumns._ID
                    + " = ?", new String[] { Long.toString(id) });
        }
        }
        // 処理がないので０を返却
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // UriMatcherを用いてUriから挿入するデータ種別を判定
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT: {
            // コメントに対するinsertを実行し、結果として取得したIDからUriを生成し返却
            return ContentUris.withAppendedId(COMMENTS_CONTENT_URI,
                    insert(MySQLiteOpenHelper.TABLE_COMMENTS, values));
        }
        }
        // 処理がないのでnullを返却
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // UriMatcherを用いてUriから挿入するデータ種別を判定
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT: {
            // コメントに対するupdateを実行し　更新数を返却
            return update(MySQLiteOpenHelper.TABLE_COMMENTS, values, selection,
                    selectionArgs);
        }
        case CODE_COMMENT_ID: {
            long id = ContentUris.parseId(uri);
            // ID指定のコメントに対するupdateを実行し更新数を返却
            return update(MySQLiteOpenHelper.TABLE_COMMENTS, values,
                    BaseColumns._ID + " = ?",
                    new String[] { Long.toString(id) });
        }
        }
        // 処理がないので０を返却
        return 0;
    }

    public int delete(String table, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        return db.delete(table, selection, selectionArgs);
    }

    private long insert(String table, ContentValues values) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        return db.insert(table, null, values);
    }

    private int update(String table, ContentValues values, String selection,
            String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        return db.update(table, values, selection, selectionArgs);
    }

}
