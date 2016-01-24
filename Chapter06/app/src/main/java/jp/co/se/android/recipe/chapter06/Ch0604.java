package jp.co.se.android.recipe.chapter06;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.view.View;

public class Ch0604 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    class MyView extends View {
        Paint mPaintStd = new Paint();
        Paint mPaintFill = new Paint();

        public MyView(Context context) {
            super(context);
            // �h��Ԃ��Ȃ���Paint�̒�`
            mPaintStd = new Paint();
            mPaintStd.setColor(Color.RED);
            mPaintStd.setStyle(Style.STROKE);
            mPaintStd.setStrokeWidth(5);

            // �h��Ԃ������Paint�̒�`
            mPaintFill = new Paint();
            mPaintFill.setColor(Color.RED);
            mPaintFill.setStyle(Style.FILL);
        }

        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            // �h��Ԃ��Ȃ��̉~�̕`��
            canvas.drawCircle(200, 200, 100, mPaintStd);
            // �h��Ԃ�����̉~�̕`��
            canvas.drawCircle(200, 500, 100, mPaintFill);
        }
    }

}
