package jp.co.se.android.recipe.chapter05;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.Toast;

public class Ch0508 extends Activity {
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP){
            Toast.makeText(getApplicationContext(), "音量をあげました。" , 
Toast.LENGTH_SHORT).show();
        }
        if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN){
            Toast.makeText(getApplicationContext(), "音量をさげました。" , 
Toast.LENGTH_SHORT).show();
        }
        if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_MUTE){
            Toast.makeText(getApplicationContext(), "ミュートにしました。" , 
Toast.LENGTH_SHORT).show();
        }
        return super.dispatchKeyEvent(event);
    }
}