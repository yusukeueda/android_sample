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
                // ����E�薼�E�{�����擾
                EditText etAddress = (EditText) findViewById(R.id.intputAddress);
                EditText etSubject = (EditText) findViewById(R.id.intputSubject);
                EditText etBody = (EditText) findViewById(R.id.intputBody);
                String address = etAddress.getText().toString();
                String subject = etSubject.getText().toString();
                String body = etBody.getText().toString();

                // ����E�薼�E�{�������ꂩ������ۂȂ�x��
                if (TextUtils.isEmpty(address) || TextUtils.isEmpty(subject)
                        || TextUtils.isEmpty(body)) {
                    Toast.makeText(Ch1402.this,
                            getString(R.string.ch1401_label_notfound_app),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // ���[���A�g
                Uri uri = Uri.parse("mailto:" + address);
                // �����ő��M���ݒ�A���̒l��setData�Őݒ肵�Ă������Ӗ��ɂȂ�
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                // �ݒ肷�郁�[���̖{����Text�̏ꍇ��text/plain��HTML�̏ꍇ��text/html��ݒ�
                intent.setType("text/plain");
                // ������ݒ�
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                // �{����ݒ�
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
