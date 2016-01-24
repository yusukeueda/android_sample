package jp.co.se.android.recipe.chapter06;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

public class Ch0607 extends Activity {
    private static final String TEXT_MESSAGE = "�s����ȓ_��m���Ė��������Ȃ����Ƃ������҂̈̑�ȂƂ���ł���B";
    private static final int TEXT_SIZE = 38;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    class MyView extends View {
        Paint mPaint;

        public MyView(Context context) {
            super(context);
            mPaint = new Paint();
            mPaint.setColor(Color.RED);
            mPaint.setTextSize(TEXT_SIZE);
        }

        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            // 1�s�\��
            canvas.drawText(TEXT_MESSAGE, 0, 100, mPaint);

            int maxWidth = canvas.getWidth();
            int lineBreakPoint = Integer.MAX_VALUE;
            int currentIndex = 0;
            int linePointY = 200;

            // ���s�\��
            while (lineBreakPoint != 0) {
                String mesureString = TEXT_MESSAGE.substring(currentIndex);
                lineBreakPoint = mPaint.breakText(mesureString, true, maxWidth,
                        null);
                if (lineBreakPoint != 0) {
                    String line = TEXT_MESSAGE.substring(currentIndex,
                            currentIndex + lineBreakPoint);
                    canvas.drawText(line, 0, linePointY, mPaint);
                    linePointY += TEXT_SIZE;
                    currentIndex += lineBreakPoint;
                }
            }
        }

    }
}
