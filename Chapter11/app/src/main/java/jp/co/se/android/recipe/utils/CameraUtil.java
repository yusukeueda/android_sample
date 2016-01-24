package jp.co.se.android.recipe.utils;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Size;

public class CameraUtil {
    /**
     * 端末のディスプレイに最適なプレビューサイズを返す (AndroidのSampleに収められているメソッドを利用)
     * 
     * @param sizes
     *            : 利用可能なプレビューサイズのリスト
     * @param w
     *            : ディスプレイの幅
     * @param h
     *            : ディスプレイの高さ
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
     * カメラの撮影可能サイズを返却.
     * 
     * @return
     */
    public static Size getSupportPictureSize(Camera camera) {
        Size pictureSize = null;
        List<Size> list = camera.getParameters().getSupportedPictureSizes();
        if (list != null && list.size() > 0) {
            // 0番目が一番高解像度
            pictureSize = list.get(0);
        }
        return pictureSize;
    }

    /**
     * カメラフラッシュのサポートタイプを返却.
     * 
     * @return
     */
    public static List<String> getSupportFlash(Camera camera) {
        return camera.getParameters().getSupportedFlashModes();
    }

    /**
     * ホワイトバランスのサポートタイプを返却.
     * 
     * @return
     */
    public static List<String> getSupportWhiteBalance(Camera camera) {
        return camera.getParameters().getSupportedWhiteBalance();
    }

    /**
     * オートフォーカスがサポートされているか確認.
     * 
     * @return
     */
    public static boolean isSupportFocus(Context context) {
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_AUTOFOCUS);
    }
}
