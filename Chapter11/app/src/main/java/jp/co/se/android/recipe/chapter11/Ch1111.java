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

        // �t���X�N���[���\���ɕύX
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.ch1107_main);

        // �T�[�t�F�X�r���[�ŃJ���������p�ł���悤�ɐݒ�
        mSurfaceView = (SurfaceView) findViewById(R.id.Preview);

        // �J�����v���r���[�̏���
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // ��F���I��
                mCamera.stopFaceDetection();

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

                        // �J�����̃v���r���[�T�C�Y���Z�b�g
                        params.setPreviewSize(mPreviewSize.width,
                                mPreviewSize.height);
                        mCamera.setParameters(params);
                    }

                    // �v���r���[�J�n
                    mCamera.startPreview();

                    // �J���������o�\�Ȋ�̐����擾
                    int maxFaces = params.getMaxNumDetectedFaces();

                    if (maxFaces > 0) {
                        // ��F�������o���郊�X�i�[���Z�b�g
                        mCamera.setFaceDetectionListener(new FaceDetectionListener() {
                            @Override
                            public void onFaceDetection(Face[] faces,
                                    Camera camera) {
                                // ������o�������F���}�[�J�[�\���r���[�֒l��n��
                                mFaceMarkerView.faces = faces;
                                mFaceMarkerView.invalidate();
                            }
                        });
                        Toast.makeText(
                                Ch1111.this,
                                getString(R.string.ch1111_camera_maxface,
                                        maxFaces), Toast.LENGTH_SHORT).show();
                    }

                    // ��F���J�n
                    mCamera.startFaceDetection();
                }
            }
        });

        // ��F���}�[�J�[��\�����铧���̃r���[�����C�A�E�g�ɒǉ�
        mFaceMarkerView = new FaceMarkerView(this);
        addContentView(mFaceMarkerView, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

    }

    /**
     * ��F���}�[�J�[��\������r���[
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
            // �I�[�o�[���C�\�����邽�߂ɔw�i�𓧖��ɂ���
            canvas.drawColor(Color.TRANSPARENT);

            // �}�[�J�[�̐F��g��ݒ�
            Paint paint = new Paint();
            paint.setColor(Color.CYAN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setTextSize(30);

            // �炪���o�����΃}�[�J�[��`�悷��
            if (faces != null) {
                for (int i = 0; i < faces.length; i++) {
                    // �ύX�O��Canvas�̏�Ԃ�ۑ�
                    int saveState = canvas.save();

                    // �J�����h���C�o����擾�����l��-1000~1000�ɂȂ��Ă��邽�߁AMatrix���g���č��W�ɕϊ��ł���悤�ɂ���
                    Matrix matrix = new Matrix();
                    matrix.postScale(getWidth() / 2000f, getHeight() / 2000f);
                    matrix.postTranslate(getWidth() / 2f, getHeight() / 2f);
                    canvas.concat(matrix);

                    // ���o������𒆐S�ɋ�`�ƔF�����x��`��
                    float x = (faces[i].rect.right + faces[i].rect.left) / 2;
                    float y = (faces[i].rect.top + faces[i].rect.bottom) / 2;
                    String score = String.valueOf(faces[i].score);
                    canvas.drawText(score, x, y, paint);
                    canvas.drawRect(faces[i].rect, paint);

                    // Canvas�����̏�Ԃɖ߂�
                    canvas.restoreToCount(saveState);
                }
            }
        }
    }
}
