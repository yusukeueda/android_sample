package jp.co.se.android.recipe.chapter11;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import jp.co.se.android.recipe.utils.CameraUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;

public class Ch1109 extends Activity {
    private static final String SAVE_PATH = "/AndroidRecipe";
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera = null;
    private Button mBtnFocus;
    private Button mBtnExposure;
    private Size mPreviewSize;
    private boolean mIsSave = false;
    private List<Size> mSupportedPreviewSizes;
    private boolean mIsSupportFocus = false;
    private int mMinExposure;
    private int mMaxExposure;
    private int mExposureValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // フルスクリーン表示に変更
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.ch1109_main);

        // サーフェスビューでカメラが利用できるように設定
        mSurfaceView = (SurfaceView) findViewById(R.id.Preview);

        // カメラプレビューの処理
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
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

                        // オートフォーカスが利用可能か確認
                        mIsSupportFocus = CameraUtil
                                .isSupportFocus(Ch1109.this);
                        if (mIsSupportFocus) {
                            mBtnFocus.setVisibility(View.VISIBLE);
                        }

                        // 露出の値を確認
                        mMinExposure = mCamera.getParameters()
                                .getMinExposureCompensation();
                        mMaxExposure = mCamera.getParameters()
                                .getMaxExposureCompensation();
                        mExposureValue = mCamera.getParameters()
                                .getExposureCompensation();
                        mBtnExposure.setText(Ch1109.this
                                .getString(R.string.ch1109_label_exposure)
                                + mExposureValue);

                        // カメラのプレビューサイズをセット
                        params.setPreviewSize(mPreviewSize.width,
                                mPreviewSize.height);

                        // カメラの撮影サイズをセット
                        Size pictureSize = CameraUtil
                                .getSupportPictureSize(mCamera);
                        if (pictureSize != null) {
                            params.setPictureSize(pictureSize.width,
                                    pictureSize.height);
                        }
                        mCamera.setParameters(params);
                    }

                    // プレビュー開始
                    mCamera.startPreview();
                }
            }
        });

        // カメラプレビューをタッチした時
        mSurfaceView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mCamera != null) {
                        // 前回撮影した画像を保存中でないか確認
                        if (!mIsSave) {
                            // 写真を撮影
                            mCamera.takePicture(null, null, mPictureCallBack);
                            mIsSave = true;
                        }
                    }
                }
                return true;
            }
        });

        // フォーカスの使用
        mBtnFocus = (Button) findViewById(R.id.focus);
        mBtnFocus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCameraAutoFocus();

            }
        });

        // 露出の使用　
        mBtnExposure = (Button) findViewById(R.id.exposure);
        mBtnExposure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setExposure();
            }
        });
    }

    /**
     * カメラの露出をセット.
     */
    private void setExposure() {
        if (mCamera != null) {
            mExposureValue++;
            if (mExposureValue > mMaxExposure) {
                mExposureValue = mMinExposure;
            }
            Parameters params = mCamera.getParameters();
            params.setExposureCompensation(mExposureValue);
            mCamera.setParameters(params);
            mBtnExposure.setText(getString(R.string.ch1109_label_exposure)
                    + mExposureValue);
        }
    }

    /**
     * カメラのオートフォーカスを開始.
     */
    private void setCameraAutoFocus() {
        if (mCamera != null && mIsSupportFocus) {
            mCamera.autoFocus(new AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    // 一旦オートフォーカスをリセット
                    mCamera.cancelAutoFocus();
                    // オートフォーカスを開始
                    mCamera.autoFocus(null);
                }
            });
        }
    }

    /**
     * 撮影したJPEG画像のデータ生成が完了した際に呼ばれるコールバック
     */
    @SuppressLint("SimpleDateFormat")
    private Camera.PictureCallback mPictureCallBack = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // データがない場合は処理しない
            if (data == null) {
                return;
            }

            // 保存先の設定
            String savePath = Environment.getExternalStorageDirectory()
                    .getPath() + SAVE_PATH;
            File file = new File(savePath);
            if (!file.exists()) {
                file.mkdir();
            }
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String imgPath = savePath + "/" + "IMG_"
                    + sdFormat.format(cal.getTime()) + ".jpg";

            // 生成した画像データを保存
            try {
                FileOutputStream fos = new FileOutputStream(imgPath, true);
                fos.write(data);
                fos.close();

                // コンテンツプロバイダを更新
                ContentValues values = new ContentValues();
                values.put(Images.Media.MIME_TYPE, "image/jpeg");
                values.put("_data", imgPath);
                getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            } catch (Exception e) {
            }

            // カメラプレビューを再開
            mIsSave = false;
            mCamera.startPreview();
        }
    };
}
