package jp.co.se.android.recipe.chapter04;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Ch0406Sub extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0406_sub);

        Intent intent = getIntent();
        if (intent != null) {
            // 渡された文字列を取得
            String sValue = intent.getStringExtra("key_name");

            EditText getString = (EditText) findViewById(R.id.inputString);
            getString.setText(sValue);
        }

        findViewById(R.id.backActivity).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        EditText result = (EditText) findViewById(R.id.result);
                        // 戻り値に文字列が設定されている場合のみ戻り値を設定
                        if (result.getText().length() > 0) {
                            // 戻り値のためのIntentを生成
                            Intent data = new Intent();
                            // 戻り値を設定
                            data.putExtra("key_name", result.getText()
                                    .toString());
                            // 成功として設定
                            setResult(RESULT_OK, data);
                        }
                        finish();
                    }
                });
    }
}
