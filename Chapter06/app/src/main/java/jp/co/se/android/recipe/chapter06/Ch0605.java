package jp.co.se.android.recipe.chapter06;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

public class Ch0605 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    class MyView extends View {
        Paint mPaintStd = new Paint();
        Paint mPaintFill = new Paint();
        Rect mRectStd = new Rect();
        Rect mRectFill = new Rect();

        public MyView(Context context) {
            super(context);
            // 塗りつぶしなしのPaintの定義
            mPaintStd = new Paint();
            mPaintStd.setColor(Color.RED);
            mPaintStd.setStyle(Style.STROKE);
            mPaintStd.setStrokeWidth(5);
            // 塗りつぶしありのPaintの定義
            mPaintFill = new Paint();
            mPaintFill.setColor(Color.RED);
            mPaintFill.setStyle(Style.FILL);

            // 塗りつぶしなしの矩形の領域の定義
            mRectStd.set(100, 100, 300, 300);
            // 塗りつぶしありの矩形の領域の定義
            mRectFill.set(100, 500, 300, 700);
        }

        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            // 塗りつぶしなしの矩形の描画
            canvas.drawRect(mRectStd, mPaintStd);
            // 塗りつぶしありの矩形の描画
            canvas.drawRect(mRectFill, mPaintFill);
        }
    }

}
