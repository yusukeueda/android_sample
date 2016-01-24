package jp.co.se.android.recipe.chapter11;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.co.se.android.recipe.utils.CameraUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.hardware.Camera;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class Ch1110 extends Activity {
    private static final String SAVE_PATH = "/AndroidRecipe";
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera = null;
    private Switch mSwTorch;
    private Button mBtnFlash;
    private Size mPreviewSize;
    private List<Size> mSupportedPreviewSizes;
    private List<String> mFlashType = null;
    private boolean mIsSave = false;
    private boolean mIsTorch = false;
    private int mFlashIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // �t���X�N���[���\���ɕύX
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.ch1110_main);

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

                        // �t���b�V���̃T�|�[�g�^�C�v���m�F
                        List<String> flashLists = CameraUtil
                                .getSupportFlash(mCamera);
                        if (flashLists != null && flashLists.size() > 0) {
                            mFlashType = new ArrayList<String>();
                            for (int i = 0; i < flashLists.size(); i++) {
                                String type = flashLists.get(i);
                                if (type.equals(Parameters.FLASH_MODE_TORCH)) {
                                    // �g�[�`�����p�\
                                    mIsTorch = true;
                                    mSwTorch.setVisibility(View.VISIBLE);
                                } else {
                                    // ���̑��t���b�V�����[�h�����p�\
                                    mFlashType.add(type);
                                    mBtnFlash.setVisibility(View.VISIBLE);
                                }
                            }
                            // �J�����̃t���b�V�����[�h��OFF�ɃZ�b�g
                            if (mFlashType != null && mFlashType.size() > 0) {
                                mBtnFlash
                                        .setText(getString(R.string.ch1110_label_flash)
                                                + mFlashType.get(0));
                            }
                        }

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

        // �g�[�`���C�g�̎g�p
        mSwTorch = (Switch) findViewById(R.id.torchSwitch);
        mSwTorch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                // �g�[�`���Z�b�g
                setTorch(isChecked);
            }
        });

        // �t���b�V���̎g�p
        mBtnFlash = (Button) findViewById(R.id.flash);
        mBtnFlash.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCamera != null && mFlashType != null
                        && mFlashType.size() > 0) {
                    // �J�����t���b�V�����Z�b�g
                    setCameraFlash();
                }
            }
        });
    }

    /**
     * �g�[�`���C�g��ON�EOFF.
     * 
     * @param isUse
     */
    private void setTorch(boolean isUse) {
        if (mCamera != null && mIsTorch) {
            String torchMode = null;
            if (isUse) {
                // �g�[�`ON
                torchMode = Parameters.FLASH_MODE_TORCH;
            } else {
                // �g�[�`OFF
                torchMode = Parameters.FLASH_MODE_OFF;
            }
            Parameters params = mCamera.getParameters();
            params.setFlashMode(torchMode);
            mCamera.setParameters(params);
        }
    }

    /**
     * �J�����̃t���b�V����؂�ւ���.
     */
    private void setCameraFlash() {
        mFlashIndex++;
        if (mFlashIndex >= mFlashType.size()) {
            mFlashIndex = 0;
        }
        String flashMode = mFlashType.get(mFlashIndex);
        Parameters params = mCamera.getParameters();
        params.setFlashMode(flashMode);
        mCamera.setParameters(params);
        mBtnFlash.setText(getString(R.string.ch1110_label_flash) + flashMode);
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
