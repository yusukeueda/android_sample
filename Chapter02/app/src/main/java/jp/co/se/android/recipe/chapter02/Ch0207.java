package jp.co.se.android.recipe.chapter02;

import jp.co.se.android.recipe.chapter02.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Ch0207 extends Activity implements OnScrollListener {

    private ArrayAdapter<String> mAdapter;
    private AsyncTask<String, Void, String> mTask;
    private ListView mListView;
    private View mFooter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0201_main);

        mListView = (ListView) findViewById(R.id.ListView);

        // シンプルなリストを表示するAdapterを生成
        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        // テストデータを追加
        for (int i = 1; i < 10; i++) {
            mAdapter.add("Item" + i);
        }

        // 読み込み中のフッターを生成
        mFooter = getLayoutInflater().inflate(
                R.layout.ch0207_list_progress_item, null);

        // ListViewにフッターを設定
        mListView.addFooterView(mFooter);

        // アダプターを設定
        mListView.setAdapter(mAdapter);

        // スクロールリスナーを設定
        mListView.setOnScrollListener(this);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        // 末尾の場合次のデータを読み込む
        if (totalItemCount == firstVisibleItem + visibleItemCount) {
            additionalReading();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
    }

    private void additionalReading() {
        // 既に読み込み中ならスキップ
        if (mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        }
        /*
         * 通常はネットワークやファイルからデータが読まれるため、非同期に読み込み処理を実装する
         * 本サンプルでは簡略化のため、非同期処理はそのままとし、データの読み込みは意図的に遅延が発生するように実装してある
         */
        mTask = new MyAsyncTask(this).execute("text");
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        public MyAsyncTask(Ch0207 androidAsyncTaskActivity) {
        }

        protected String doInBackground(String... params) {
            // 2秒止める
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return params[0];
        }

        @Override
        protected void onPostExecute(String text) {
            // データ追加
            for (int n = 0; n < 10; n++) {
                mAdapter.add(text + n);
            }
        }

    }
}
