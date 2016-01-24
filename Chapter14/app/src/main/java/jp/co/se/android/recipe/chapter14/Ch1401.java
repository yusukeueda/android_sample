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

public class Ch1401 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1401_main);

        findViewById(R.id.linkSms).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 宛先と本文を取得
                EditText etAddress = (EditText) findViewById(R.id.intputAddress);
                EditText etBody = (EditText) findViewById(R.id.intputBody);
                String address = etAddress.getText().toString();
                String body = etBody.getText().toString();

                // 宛先か本文どちらかが空っぽなら警告
                if (TextUtils.isEmpty(address) || TextUtils.isEmpty(body)) {
                    Toast.makeText(Ch1401.this,
                            getString(R.string.ch1401_label_input_empty),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // SMS連携
                Uri uri = Uri.parse("smsto:" + address);
                // 引数で送信先を設定、この値はsetDataで設定しても同じ意味になる
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                // 本文を設定
                intent.putExtra("sms_body", body);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(Ch1401.this,
                            getString(R.string.ch1401_label_notfound_app),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
