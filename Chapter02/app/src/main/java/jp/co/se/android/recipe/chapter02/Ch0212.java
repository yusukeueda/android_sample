package jp.co.se.android.recipe.chapter02;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class Ch0212 extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // カスタムビューを設定
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(
                R.layout.ch0212_dialog_contents_item,
                (ViewGroup) findViewById(R.id.layout_root));

        // アラーとダイアログ を生成
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ダイアログタイトル");
        builder.setView(layout);
        builder.setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // OK ボタンクリック処理
                EditText text = (EditText) layout.findViewById(R.id.edit_text);
                String string = text.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Cancel ボタンクリック処理
            }
        });

        // 表示
        builder.create().show();
    }
}
