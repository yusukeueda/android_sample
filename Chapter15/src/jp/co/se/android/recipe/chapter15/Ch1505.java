package jp.co.se.android.recipe.chapter15;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Ch1505 extends Activity {
    private TextView mTextTempState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ch1505_main);

        mTextTempState = (TextView) findViewById(R.id.textTempState);

        printTempfile();
    }

    private void printTempfile() {
        // 内部ストレージのキャッシュフォルダのパスを取得
        File internalCachedir = getCacheDir();
        // 外部ストレージのキャッシュフォルダのパスを取得
        File externalCachedir = getExternalCacheDir();

        StringBuilder buf = new StringBuilder();
        buf.append("internal Cache Dire:\n").append(internalCachedir.getPath())
                .append("\n\n");
        buf.append("external Cache Dire:\n").append(externalCachedir.getPath())
                .append("\n");

        mTextTempState.setText(buf.toString());
    }
}
