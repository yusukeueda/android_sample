package jp.co.se.android.recipe.chapter05;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.Toast;

public class Ch0506 extends Activity {
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(getApplicationContext(), "�߂�{�^����������܂����B",
                    Toast.LENGTH_SHORT).show();
        }
        return super.dispatchKeyEvent(event);
    }
}