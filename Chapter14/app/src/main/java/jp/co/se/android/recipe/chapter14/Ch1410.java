package jp.co.se.android.recipe.chapter14;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class Ch1410 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1410_main);

        findViewById(R.id.linkLine).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 検索するワードを取得
                EditText etMessage = (EditText) findViewById(R.id.intputMessage);
                String message = etMessage.getText().toString();

                // 検索ワードが空っぽなら警告
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(Ch1410.this,
                            getString(R.string.ch1410_label_input_empty),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // LINE連携
                Intent intent = new Intent(Intent.ACTION_VIEW);
                // LINEへ送るメッセージを設定
                intent.setData(Uri.parse("line://msg/text/" + message));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(Ch1410.this,
                            getString(R.string.ch1401_label_notfound_app),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}