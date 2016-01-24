package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

public class Ch0122 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0122_main);

        // レイアウトからRatingBarインスタンス生成
        final RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        // RatingBarインスタンスにリスナー追加
        ratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            // レーティングが変化した場合
            public void onRatingChanged(RatingBar ratingBar, float rating,
                    boolean fromUser) {
                // トーストメッセージ表示
                Toast.makeText(Ch0122.this,
                        "New Rating: " + rating + "  /fromUser:" + fromUser,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}
