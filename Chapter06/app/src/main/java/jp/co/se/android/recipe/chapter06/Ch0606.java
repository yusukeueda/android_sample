package jp.co.se.android.recipe.chapter06;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class Ch0606 extends Activity {
    private static final int POLYGON_NUM = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    class MyView extends View {
        Paint mPaintPolygon;
        Paint mPaintPoint;
        Path mPath = new Path();
        ArrayList<PointF> mPoints = new ArrayList<PointF>();

        public MyView(Context context) {
            super(context);
            // ���p�`��Paint�̒�`
            mPaintPolygon = new Paint();
            mPaintPolygon.setColor(Color.BLACK);
            mPaintPolygon.setStyle(Paint.Style.FILL);
            mPaintPolygon.setStrokeWidth(3);
            // ���_��Paint�̒�`
            mPaintPoint = new Paint();
            mPaintPoint.setColor(Color.RED);
            mPaintPoint.setStrokeWidth(10);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // ��ʂ��^�b�`�����ۂɃ^�b�`�������W�����Ƃ�Path�𐶐�
                if (mPoints.size() >= POLYGON_NUM || mPoints.size() == 0) {
                    mPoints.clear();
                    mPoints.add(new PointF(event.getX(), event.getY()));
                    mPath = new Path();
                    mPath.moveTo(event.getX(), event.getY());
                } else {
                    mPoints.add(new PointF(event.getX(), event.getY()));
                    mPath.lineTo(event.getX(), event.getY());
                    if (mPoints.size() >= POLYGON_NUM) {
                        mPath.close();
                    }
                }
                invalidate();
                break;
            default:
                break;
            }

            return true;
        }

        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            // ��������Path�����ɑ��p�`��`��
            if (mPath != null && !mPath.isEmpty()) {
                canvas.drawPath(mPath, mPaintPolygon);
            }
            // ���p�`�̒��_�̕`��
            for (int i = 0; i < mPoints.size(); i++) {
                PointF p = mPoints.get(i);
                canvas.drawPoint(p.x, p.y, mPaintPoint);
            }
        }
    }

}
