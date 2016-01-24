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

        // NFCを扱うためのインスタンスを取得
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // NFCが搭載されているかチェック
        if (mNfcAdapter != null) {
            // NFC機能が有効になっているかチェック
            if (!mNfcAdapter.isEnabled()) {
                // NFC機能が無効の場合はユーザーへ通知
                Toast.makeText(getApplicationContext(),
                        getString(R.string.error_nfc_disable),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // NFC非搭載の場合はユーザーへ通知
            Toast.makeText(getApplicationContext(),
                    getString(R.string.error_nfc_nosupport), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            // 起動中のアクティビティが優先的にNFCを受け取れるよう設定
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
            // アクテイビティが非表示になる際に優先的にNFCを受け取る設定を解除
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

            // タグを取得
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            if (tag != null) {
                // 入力したテキストを取得
                EditText etWrite = (EditText) findViewById(R.id.Write);
                String ndefMsg = etWrite.getText().toString();
                if (!TextUtils.isEmpty(ndefMsg)) {

                    // NdefRecordの作成
                    NdefRecord[] ndefRecords = new NdefRecord[] { NdefRecord
                            .createUri(ndefMsg), };
                    // NdefMessageの作成
                    NdefMessage msg = new NdefMessage(ndefRecords);

                    // NFCタグに書き込み
                    write(tag, msg);
                }
            }
        }
    }

    /**
     * NFCタグに書き込む.
     * 
     * @param tag
     * @param msg
     */
    private void write(Tag tag, NdefMessage msg) {
        try {
            List<String> techList = Arrays.asList(tag.getTechList());
            // 書き込みを行うタグにNDEFデータが格納されているか確認
            if (techList.contains(Ndef.class.getName())) {
                // NDEFが含まれている場合
                Ndef ndef = Ndef.get(tag);
                try {
                    // そのままNDEFデータ上にNDEFメッセージを書き込む
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
                // NDEFFormattableが含まれている場合
                NdefFormatable ndeffmt = NdefFormatable.get(tag);
                try {
                    // そのままNDEFにフォーマットしつつNDEFメッセージを書き込む
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
            // 書き込み成功のトーストを表示
            Toast.makeText(this, getString(R.string.write_success),
                    Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            // 書き込み失敗のトーストを表示
            Toast.makeText(this, getString(R.string.write_failure),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
