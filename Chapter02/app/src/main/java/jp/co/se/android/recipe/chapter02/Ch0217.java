package jp.co.se.android.recipe.chapter02;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class Ch0217 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this)
                .setCancelable(false);
        alertDlg.setTitle("ダイアログタイトル");
        alertDlg.setMessage("メッセージ");
        alertDlg.setCancelable(false);
        alertDlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDlg.create().show();
    }
}
