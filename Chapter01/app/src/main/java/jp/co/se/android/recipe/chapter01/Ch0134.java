package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Ch0134 extends Activity {
    private float mRotate = 0.0F;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0134_main);
        final ImageView mImageView = (ImageView) findViewById(R.id.ImageViewTurn);

        findViewById(R.id.btnScaleMatrix).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // タップする度に90度回転する
                        mRotate += 90F;
                        if (mRotate >= 360F) {
                            mRotate = 0;
                        }

                        // 画像を回転させる
                        Matrix mtrx = new Matrix();
                        mtrx.postRotate(mRotate, mImageView.getDrawable()
                                .getBounds().width() / 2, mImageView
                                .getDrawable().getBounds().height() / 2);
                        mImageView.setImageMatrix(mtrx);
                    }
                });
    }

}
