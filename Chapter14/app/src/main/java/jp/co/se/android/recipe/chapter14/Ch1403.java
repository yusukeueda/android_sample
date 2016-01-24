package jp.co.se.android.recipe.chapter14;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Ch1403 extends Activity {
    private static final int RESULT_PICK_FILENAME = 1;
    private Uri mAttachFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1402_main);

        findViewById(R.id.selectAttach).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_PICK,
                                Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_PICK_FILENAME);

                    }
                });

        findViewById(R.id.linkMail).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // ����Ɩ{�����擾
                EditText etAddress = (EditText) findViewById(R.id.intputAddress);
                EditText etSubject = (EditText) findViewById(R.id.intputSubject);
                EditText etBody = (EditText) findViewById(R.id.intputBody);
                String address = etAddress.getText().toString();
                String subject = etSubject.getText().toString();
                String body = etBody.getText().toString();

                // ���悩�薼���{���ǂ��炩������ۂȂ�x��
                if (TextUtils.isEmpty(address) || TextUtils.isEmpty(subject)
                        || TextUtils.isEmpty(body)) {
                    Toast.makeText(Ch1403.this,
                            getString(R.string.ch1403_label_input_empty),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // ���[���A�g
                Uri uri = Uri.parse("mailto:" + address);
                // �����ő��M���ݒ�A���̒l��setData�Őݒ肵�Ă������Ӗ��ɂȂ�
                Intent intent = new Intent(Intent.ACTION_SEND, uri);
                // �����̑��M�悪����ꍇ�́AIntent.EXTRA_EMAIL���g�p���Đݒ�ł���
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
                // �ݒ肷�郁�[���̖{����Text�̏ꍇ��text/plain��HTML�̏ꍇ��text/html��ݒ�
                intent.setType("text/plain");
                // ������ݒ�
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                // �{����ݒ�
                intent.putExtra(Intent.EXTRA_TEXT, body);
                // �Y�t�t�@�C����ݒ�
                if (mAttachFile != null) {
                    intent.putExtra(Intent.EXTRA_STREAM, mAttachFile);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(Ch1403.this,
                                getString(R.string.ch1403_label_input_empty),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_PICK_FILENAME && resultCode == RESULT_OK
                && null != data) {
            mAttachFile = data.getData();

            String[] filePathColumn = { Media.DATA };

            Cursor cursor = getContentResolver().query(mAttachFile,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            TextView textAttachFile = (TextView) findViewById(R.id.textAttachFile);
            textAttachFile.setText(picturePath);
        }
    }
}
