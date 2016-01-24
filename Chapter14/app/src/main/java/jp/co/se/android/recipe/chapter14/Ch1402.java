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

public class Ch1402 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1402_main);

        findViewById(R.id.linkMail).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 宛先・題名・本文を取得
                EditText etAddress = (EditText) findViewById(R.id.intputAddress);
                EditText etSubject = (EditText) findViewById(R.id.intputSubject);
                EditText etBody = (EditText) findViewById(R.id.intputBody);
                String address = etAddress.getText().toString();
                String subject = etSubject.getText().toString();
                String body = etBody.getText().toString();

                // 宛先・題名・本文いずれかが空っぽなら警告
                if (TextUtils.isEmpty(address) || TextUtils.isEmpty(subject)
                        || TextUtils.isEmpty(body)) {
                    Toast.makeText(Ch1402.this,
                            getString(R.string.ch1401_label_notfound_app),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // メール連携
                Uri uri = Uri.parse("mailto:" + address);
                // 引数で送信先を設定、この値はsetDataで設定しても同じ意味になる
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                // 設定するメールの本文がTextの場合はtext/plainをHTMLの場合はtext/htmlを設定
                intent.setType("text/plain");
                // 件名を設定
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                // 本文を設定
                intent.putExtra(Intent.EXTRA_TEXT, body);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(Ch1402.this,
                            getString(R.string.ch1402_label_input_empty),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
