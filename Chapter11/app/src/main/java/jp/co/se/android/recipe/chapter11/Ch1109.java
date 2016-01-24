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

        // �t���X�N���[���\���ɕύX
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.ch1109_main);

        // �T�[�t�F�X�r���[�ŃJ���������p�ł���悤�ɐݒ�
        mSurfaceView = (SurfaceView) findViewById(R.id.Preview);

        // �J�����v���r���[�̏���
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // �J�����̏I������
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // �J�����̏���������
                mCamera = Camera.open();
                if (mCamera != null) {
                    try {
                        mCamera.setPreviewDisplay(mSurfaceHolder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // ���p�\�ȃv���r���[�T�C�Y���擾
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
                        // �[���f�B�X�v���C�̃T�C�Y�ɍœK�ȃJ�����̃v���r���[�T�C�Y��I������
                        mPreviewSize = CameraUtil.getOptimalPreviewSize(
                                mSupportedPreviewSizes, height, width);

                        // �I�[�g�t�H�[�J�X�����p�\���m�F
                        mIsSupportFocus = CameraUtil
                                .isSupportFocus(Ch1109.this);
                        if (mIsSupportFocus) {
                            mBtnFocus.setVisibility(View.VISIBLE);
                        }

                        // �I�o�̒l���m�F
                        mMinExposure = mCamera.getParameters()
                                .getMinExposureCompensation();
                        mMaxExposure = mCamera.getParameters()
                                .getMaxExposureCompensation();
                        mExposureValue = mCamera.getParameters()
                                .getExposureCompensation();
                        mBtnExposure.setText(Ch1109.this
                                .getString(R.string.ch1109_label_exposure)
                                + mExposureValue);

                        // �J�����̃v���r���[�T�C�Y���Z�b�g
                        params.setPreviewSize(mPreviewSize.width,
                                mPreviewSize.height);

                        // �J�����̎B�e�T�C�Y���Z�b�g
                        Size pictureSize = CameraUtil
                                .getSupportPictureSize(mCamera);
                        if (pictureSize != null) {
                            params.setPictureSize(pictureSize.width,
                                    pictureSize.height);
                        }
                        mCamera.setParameters(params);
                    }

                    // �v���r���[�J�n
                    mCamera.startPreview();
                }
            }
        });

        // �J�����v���r���[���^�b�`������
        mSurfaceView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mCamera != null) {
                        // �O��B�e�����摜��ۑ����łȂ����m�F
                        if (!mIsSave) {
                            // �ʐ^���B�e
                            mCamera.takePicture(null, null, mPictureCallBack);
                            mIsSave = true;
                        }
                    }
                }
                return true;
            }
        });

        // �t�H�[�J�X�̎g�p
        mBtnFocus = (Button) findViewById(R.id.focus);
        mBtnFocus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCameraAutoFocus();

            }
        });

        // �I�o�̎g�p�@
        mBtnExposure = (Button) findViewById(R.id.exposure);
        mBtnExposure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setExposure();
            }
        });
    }

    /**
     * �J�����̘I�o���Z�b�g.
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
     * �J�����̃I�[�g�t�H�[�J�X���J�n.
     */
    private void setCameraAutoFocus() {
        if (mCamera != null && mIsSupportFocus) {
            mCamera.autoFocus(new AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    // ��U�I�[�g�t�H�[�J�X�����Z�b�g
                    mCamera.cancelAutoFocus();
                    // �I�[�g�t�H�[�J�X���J�n
                    mCamera.autoFocus(null);
                }
            });
        }
    }

    /**
     * �B�e����JPEG�摜�̃f�[�^���������������ۂɌĂ΂��R�[���o�b�N
     */
    @SuppressLint("SimpleDateFormat")
    private Camera.PictureCallback mPictureCallBack = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // �f�[�^���Ȃ��ꍇ�͏������Ȃ�
            if (data == null) {
                return;
            }

            // �ۑ���̐ݒ�
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

            // ���������摜�f�[�^��ۑ�
            try {
                FileOutputStream fos = new FileOutputStream(imgPath, true);
                fos.write(data);
                fos.close();

                // �R���e���c�v���o�C�_���X�V
                ContentValues values = new ContentValues();
                values.put(Images.Media.MIME_TYPE, "image/jpeg");
                values.put("_data", imgPath);
                getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            } catch (Exception e) {
            }

            // �J�����v���r���[���ĊJ
            mIsSave = false;
            mCamera.startPreview();
        }
    };
}