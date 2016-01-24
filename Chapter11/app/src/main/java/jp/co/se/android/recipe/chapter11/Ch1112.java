package jp.co.se.android.recipe.chapter11;

import jp.co.se.android.recipe.utils.NfcUtil;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

public class Ch1112 extends Activity {
    protected NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1112_main);

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
            // 起動中のActivityが優先的にNFCを受け取れるよう設定
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
            // Activityが非表示になる際に優先的にNFCを受け取る設定を解除
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

            // IDmを取得
            byte[] idm = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

            // IDｍを文字列に変換して表示
            TextView tvRead = (TextView) findViewById(R.id.Read);
            tvRead.setText(NfcUtil.bytesToHex(idm));
        }
    }
}