package jp.co.se.android.recipe.chapter07;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class Ch0707Receiver extends BroadcastReceiver {
    public static final String TAG = "Chapter07";
    private static final int KEYCODE_MEDIA_PLAY = 126;
    private static final int KEYCODE_MEDIA_PAUSE = 127;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent keyEvent = (KeyEvent) intent
                    .getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                Intent service = new Intent(context, Ch0707Service.class);
                switch (keyEvent.getKeyCode()) {
                case KEYCODE_MEDIA_PAUSE:
                case KEYCODE_MEDIA_PLAY:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                case KeyEvent.KEYCODE_HEADSETHOOK:
                    // �Đ��܂��͈ꎞ��~�܂��̓t�b�N�{�^��
                    service.setAction("playpause");
                    context.startService(service);
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    // ���̋Ȃ�
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    // �O�̋Ȃ�
                    break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    // ��~
                    service.setAction("stop");
                    context.startService(service);
                    break;
                case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                    // ������
                    break;
                case KeyEvent.KEYCODE_MEDIA_REWIND:
                    // �����߂�
                    break;
                default:
                    Log.w(TAG, "�\�����Ȃ��R�[�h���Ă΂ꂽ: " + keyEvent.getKeyCode());
                }
            }
        }
    }
}
