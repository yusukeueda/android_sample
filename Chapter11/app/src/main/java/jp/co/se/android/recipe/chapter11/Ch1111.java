package jp.co.se.android.recipe.chapter11;

import java.io.IOException;
import java.util.List;

import jp.co.se.android.recipe.utils.CameraUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

public class Ch1111 extends Activity {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera = null;
    private Size mPreviewSize;
    private List<Size> mSupportedPreviewSizes;
    private FaceMarkerView mFaceMarkerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // フルスクリーン表示に変更
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.ch1107_main);

        // サーフェスビューでカメラが利用できるように設定
        mSurfaceView = (SurfaceView) findViewById(R.id.Preview);

        // カメラプレビューの処理
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // 顔認識終了
                mCamera.stopFaceDetection();

                // カメラの終了処理
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // カメラの初期化処理
                mCamera = Camera.open();
                if (mCamera != null) {
                    try {
                        mCamera.setPreviewDisplay(mSurfaceHolder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 利用可能なプレビューサイズを取得
                    mSupportedPreviewSizes = mCamera.getParameters()
                            .getSupportedPreviewSizes();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                    int width, int height) {
                if (mCamera != null) {
                    Parameters params = mCamera.getParameters();

                    if (mSupportedPreviewSizes != null) {
                        // 端末ディスプレイのサイズに最適なカメラのプレビューサイズを選択する
                        mPreviewSize = CameraUtil.getOptimalPreviewSize(
                                mSupportedPreviewSizes, height, width);

                        // カメラのプレビューサイズをセット
                        params.setPreviewSize(mPreviewSize.width,
                                mPreviewSize.height);
                        mCamera.setParameters(params);
                    }

                    // プレビュー開始
                    mCamera.startPreview();

                    // カメラが検出可能な顔の数を取得
                    int maxFaces = params.getMaxNumDetectedFaces();

                    if (maxFaces > 0) {
                        // 顔認識を検出するリスナーをセット
                        mCamera.setFaceDetectionListener(new FaceDetectionListener() {
                            @Override
                            public void onFaceDetection(Face[] faces,
                                    Camera camera) {
                                // 顔を検出したら顔認識マーカー表示ビューへ値を渡す
                                mFaceMarkerView.faces = faces;
                                mFaceMarkerView.invalidate();
                            }
                        });
                        Toast.makeText(
                                Ch1111.this,
                                getString(R.string.ch1111_camera_maxface,
                                        maxFaces), Toast.LENGTH_SHORT).show();
                    }

                    // 顔認識開始
                    mCamera.startFaceDetection();
                }
            }
        });

        // 顔認識マーカーを表示する透明のビューをレイアウトに追加
        mFaceMarkerView = new FaceMarkerView(this);
        addContentView(mFaceMarkerView, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

    }

    /**
     * 顔認識マーカーを表示するビュー
     */
    @SuppressLint("DrawAllocation")
    private class FaceMarkerView extends View {
        Face[] faces;

        public FaceMarkerView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // オーバーレイ表示するために背景を透明にする
            canvas.drawColor(Color.TRANSPARENT);

            // マーカーの色や枠を設定
            Paint paint = new Paint();
            paint.setColor(Color.CYAN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setTextSize(30);

            // 顔が検出されればマーカーを描画する
            if (faces != null) {
                for (int i = 0; i < faces.length; i++) {
                    // 変更前のCanvasの状態を保存
                    int saveState = canvas.save();

                    // カメラドライバから取得される値は-1000~1000になっているため、Matrixを使って座標に変換できるようにする
                    Matrix matrix = new Matrix();
                    matrix.postScale(getWidth() / 2000f, getHeight() / 2000f);
                    matrix.postTranslate(getWidth() / 2f, getHeight() / 2f);
                    canvas.concat(matrix);

                    // 検出した顔を中心に矩形と認識精度を描画
                    float x = (faces[i].rect.right + faces[i].rect.left) / 2;
                    float y = (faces[i].rect.top + faces[i].rect.bottom) / 2;
                    String score = String.valueOf(faces[i].score);
                    canvas.drawText(score, x, y, paint);
                    canvas.drawRect(faces[i].rect, paint);

                    // Canvasを元の状態に戻す
                    canvas.restoreToCount(saveState);
                }
            }
        }
    }
}
