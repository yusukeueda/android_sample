package jp.co.se.android.recipe.chapter02;

import jp.co.se.android.recipe.chapter02.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class Ch0226 extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentViewより前にWindowにActionBar表示を設定
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.ch0226_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ch0226_menu, menu);

        // メニューの要素を追加
        MenuItem actionItem = menu.add("プログラムから追加したアイテム");

        // SHOW_AS_ACTION_IF_ROOM: 余裕があれば表示、SHOW_AS_ACTION_WITH_TEXT: テキストも同時に表示
        actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        // アイコンを設定
        actionItem.setIcon(android.R.drawable.ic_menu_compass);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "選択した項目: " + item.getTitle(), Toast.LENGTH_SHORT)
                .show();
        return true;
    }
}
