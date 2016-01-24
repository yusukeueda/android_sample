package jp.co.se.android.recipe.chapter11;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

public class Ch1113 extends Activity {
    protected NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1113_main);

        // NFC���������߂̃C���X�^���X���擾
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // NFC�����ڂ���Ă��邩�`�F�b�N
        if (mNfcAdapter != null) {
            // NFC�@�\���L���ɂȂ��Ă��邩�`�F�b�N
            if (!mNfcAdapter.isEnabled()) {
                // NFC�@�\�������̏ꍇ�̓��[�U�[�֒ʒm
                Toast.makeText(getApplicationContext(),
                        getString(R.string.error_nfc_disable),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // NFC�񓋍ڂ̏ꍇ�̓��[�U�[�֒ʒm
            Toast.makeText(getApplicationContext(),
                    getString(R.string.error_nfc_nosupport), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            // �N�����̃A�N�e�B�r�e�B���D��I��NFC���󂯎���悤�ݒ�
            Intent intent = new Intent(this, this.getClass())
                    .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    getApplicationContext(), 0, intent, 0);
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, null,
                    null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            // �A�N�e�C�r�e�B����\���ɂȂ�ۂɗD��I��NFC���󂯎��ݒ������
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // �^�O���擾
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            if (tag != null) {
                // ���͂����e�L�X�g���擾
                EditText etWrite = (EditText) findViewById(R.id.Write);
                String ndefMsg = etWrite.getText().toString();
                if (!TextUtils.isEmpty(ndefMsg)) {

                    // NdefRecord�̍쐬
                    NdefRecord[] ndefRecords = new NdefRecord[] { NdefRecord
                            .createUri(ndefMsg), };
                    // NdefMessage�̍쐬
                    NdefMessage msg = new NdefMessage(ndefRecords);

                    // NFC�^�O�ɏ�������
                    write(tag, msg);
                }
            }
        }
    }

    /**
     * NFC�^�O�ɏ�������.
     * 
     * @param tag
     * @param msg
     */
    private void write(Tag tag, NdefMessage msg) {
        try {
            List<String> techList = Arrays.asList(tag.getTechList());
            // �������݂��s���^�O��NDEF�f�[�^���i�[����Ă��邩�m�F
            if (techList.contains(Ndef.class.getName())) {
                // NDEF���܂܂�Ă���ꍇ
                Ndef ndef = Ndef.get(tag);
                try {
                    // ���̂܂�NDEF�f�[�^���NDEF���b�Z�[�W����������
                    ndef.connect();
                    ndef.writeNdefMessage(msg);
                } catch (IOException e) {
                    throw new RuntimeException(
                            getString(R.string.error_connect), e);
                } catch (FormatException e) {
                    throw new RuntimeException(
                            getString(R.string.error_format), e);
                } finally {
                    try {
                        ndef.close();
                    } catch (IOException e) {
                    }
                }
            } else if (techList.contains(NdefFormatable.class.getName())) {
                // NDEFFormattable���܂܂�Ă���ꍇ
                NdefFormatable ndeffmt = NdefFormatable.get(tag);
                try {
                    // ���̂܂�NDEF�Ƀt�H�[�}�b�g����NDEF���b�Z�[�W����������
                    ndeffmt.connect();
                    ndeffmt.format(msg);
                } catch (IOException e) {
                    throw new RuntimeException(
                            getString(R.string.error_connect), e);
                } catch (FormatException e) {
                    throw new RuntimeException(
                            getString(R.string.error_format), e);
                } finally {
                    try {
                        ndeffmt.close();
                    } catch (IOException e) {
                    }
                }
            }
            // �������ݐ����̃g�[�X�g��\��
            Toast.makeText(this, getString(R.string.write_success),
                    Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            // �������ݎ��s�̃g�[�X�g��\��
            Toast.makeText(this, getString(R.string.write_failure),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
