package jp.co.se.android.recipe.chapter03;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Ch0313 extends Activity implements OnItemClickListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0313_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mTextView = (TextView) findViewById(R.id.text);

        setupNavigationDrawer();

        // 一覧のデータを設定
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1);
        adapter.add("Menu1");
        adapter.add("Menu2");
        adapter.add("Menu3");
        mDrawerList.setAdapter(adapter);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    private void setupNavigationDrawer() {
        // NavigationDrawerの影として設定するDrawableを指定する
        mDrawerLayout.setDrawerShadow(R.drawable.ch0313_drawer_shadow,
                GravityCompat.START);
        mDrawerList.setOnItemClickListener(this);

        // ActionBarのアイコンの左にDrawerToggleを表示する
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // ActionBarのホームボタンを有効にします。
        getActionBar().setHomeButtonEnabled(true);

        // ドロワーを開閉した時のイベントを受け取れるようにする
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.ch0313_drawer_open,
                R.string.ch0313_drawer_close) {
            @Override
            public void onDrawerClosed(View view) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // DrawerToggle側でオプションメニューの選択を検知できるようにする
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // DrawerToggle側でオプションメニューを制御できるようにする
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // DrawerToggle側で上下の変更を制御できるようにする
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View parent,
            int position, long id) {
        selectItem(position);
    }

    private void selectItem(int position) {
        ListAdapter adapter = mDrawerList.getAdapter();
        String item = (String) adapter.getItem(position);
        mTextView.setText("選択したアイテム: " + item);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

}
