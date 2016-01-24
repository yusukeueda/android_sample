package jp.co.se.android.recipe.chapter02;

import jp.co.se.android.recipe.chapter02.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Ch0201 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0201_main);
        ListView listView = (ListView) findViewById(R.id.ListView);

        // アダプタの作成
        ListAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, DAYS);
        // アダプタの設定
        listView.setAdapter(adapter);
    }

    // ListViewに表示させる文字列
    private static final String[] DAYS = new String[] { "Sunday", "Monday",
            "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

}
