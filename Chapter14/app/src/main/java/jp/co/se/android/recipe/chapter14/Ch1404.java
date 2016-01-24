package jp.co.se.android.recipe.chapter14;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class Ch1404 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1404_main);

        findViewById(R.id.linkText).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // �e�L�X�g���擾
                EditText etText = (EditText) findViewById(R.id.intputText);
                String text = etText.getText().toString();

                // �e�L�X�g������ۂȂ�x��
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(Ch1404.this,
                            getString(R.string.ch1404_label_input_empty),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // �e�L�X�g�A�g
                Intent intent = new Intent();
                // �e�L�X�g�A�g�̂��߂�Aciton��ݒ�
                intent.setAction(Intent.ACTION_SEND);
                // �e�L�X�g�f�[�^�̎�ʂ�ݒ�
                intent.setType("text/plain");
                // �e�L�X�g�f�[�^��ݒ�
                intent.putExtra(Intent.EXTRA_TEXT, text);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(Ch1404.this,
                            getString(R.string.ch1401_label_notfound_app),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
