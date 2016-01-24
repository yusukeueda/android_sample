package jp.co.se.android.recipe.chapter02;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;

public class Ch0202 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0202_main);
        GridView gridView = (GridView) findViewById(R.id.gridView);

        // アダプタの作成
        ListAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, WEEK);
        // アダプタの設定
        gridView.setAdapter(adapter);
    }

    // ListView に表示させる文字列
    private static final String[] WEEK = new String[] { "月", "火", "水", "木",
            "金", "土", "日" };
}
