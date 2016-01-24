package jp.co.se.android.recipe.chapter04;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Ch0406 extends Activity {

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0406_main);

        findViewById(R.id.launchActivity).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 呼び出す画面Intentを作成
                        final Intent intent = new Intent(Ch0406.this,
                                Ch0406Sub.class);
                        // IntentにKEY_NAMEのキーで文字列を設定
                        EditText inputString = (EditText) findViewById(R.id.inputString);
                        String value = inputString.getText().toString();
                        intent.putExtra("key_name", value);

                        // 戻り値を取得できる呼び出し方法でActivityを開始
                        startActivityForResult(intent, REQUEST_CODE);

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // startActivityForResult実行時の引数に設定したRequestCodeを比較
        if (requestCode == REQUEST_CODE) {
            // Activity終了時のフラグを判定
            if (resultCode == RESULT_OK) {
                // 戻り値として設定されたKEY_NAMEの値を取得
                String value = data.getStringExtra("key_name");
                EditText result = (EditText) findViewById(R.id.result);
                result.setText(value);
            }
        }
    }
}
