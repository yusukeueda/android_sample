package jp.co.se.android.recipe.chapter18;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Ch1802 extends Activity {
    private static final String TAG = Ch1802.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v(TAG, "verboseログ");
        Log.d(TAG, "debugログ");
        Log.i(TAG, "infoログ");
        Log.w(TAG, "warningログ");
        Log.e(TAG, "errorログ");
    }

}
