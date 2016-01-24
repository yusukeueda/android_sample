package jp.co.se.android.recipe.utils;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Size;

public class CameraUtil {
    /**
     * �[���̃f�B�X�v���C�ɍœK�ȃv���r���[�T�C�Y��Ԃ� (Android��Sample�Ɏ��߂��Ă��郁�\�b�h�𗘗p)
     * 
     * @param sizes
     *            : ���p�\�ȃv���r���[�T�C�Y�̃��X�g
     * @param w
     *            : �f�B�X�v���C�̕�
     * @param h
     *            : �f�B�X�v���C�̍���
     * @return
     */
    public static Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null)
            return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    /**
     * �J�����̎B�e�\�T�C�Y��ԋp.
     * 
     * @return
     */
    public static Size getSupportPictureSize(Camera camera) {
        Size pictureSize = null;
        List<Size> list = camera.getParameters().getSupportedPictureSizes();
        if (list != null && list.size() > 0) {
            // 0�Ԗڂ���ԍ��𑜓x
            pictureSize = list.get(0);
        }
        return pictureSize;
    }

    /**
     * �J�����t���b�V���̃T�|�[�g�^�C�v��ԋp.
     * 
     * @return
     */
    public static List<String> getSupportFlash(Camera camera) {
        return camera.getParameters().getSupportedFlashModes();
    }

    /**
     * �z���C�g�o�����X�̃T�|�[�g�^�C�v��ԋp.
     * 
     * @return
     */
    public static List<String> getSupportWhiteBalance(Camera camera) {
        return camera.getParameters().getSupportedWhiteBalance();
    }

    /**
     * �I�[�g�t�H�[�J�X���T�|�[�g����Ă��邩�m�F.
     * 
     * @return
     */
    public static boolean isSupportFocus(Context context) {
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_AUTOFOCUS);
    }
}
