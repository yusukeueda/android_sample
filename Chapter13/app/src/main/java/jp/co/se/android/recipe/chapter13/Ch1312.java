package jp.co.se.android.recipe.chapter13;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Ch1312 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1312_main);

        findViewById(R.id.buttonShow).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showToast("Hello");
            }
        });
    }

    private void showToast(String msg) {
        // LayoutInflaterを取得する
        LayoutInflater inflater = getLayoutInflater();

        // Toast用のレイアウトを取得する
        View layout = inflater.inflate(R.layout.ch1312_toast, null, false);

        // レイアウト内のImageViewを取得して任意のイメージを設定する
        ImageView image = (ImageView) layout.findViewById(R.id.imageIcon);
        image.setImageResource(R.drawable.ic_launcher);

        // レイアウト内のTextViewを取得して任意のテキストを設定する
        TextView text = (TextView) layout.findViewById(R.id.textMessage);
        text.setText(msg);
        // Toastを表示
        Toast toast = new Toast(this);
        toast.setView(layout);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.show();
    }

}
