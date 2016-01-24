package jp.co.se.android.recipe.chapter02;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class Ch0225 extends Activity implements OnNavigationListener {

    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ナビゲーションモードを Drop-Down Navigation に設定
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getActionBar().setDisplayShowTitleEnabled(false);

        // Drop-Down Navigation のリストに表示するもの
        List<String> list = new ArrayList<String>();
        list.add("Navi Menu 1");
        list.add("Navi Menu 2");
        list.add("Navi Menu 3");
        mAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                android.R.id.text1, list);
        getActionBar().setListNavigationCallbacks(mAdapter, this);
    }

    @Override
    public boolean onNavigationItemSelected(int position, long itemId) {
        String item = mAdapter.getItem(position);
        Toast.makeText(this, "選択した項目: " + item, Toast.LENGTH_SHORT).show();
        return false;
    }
}
