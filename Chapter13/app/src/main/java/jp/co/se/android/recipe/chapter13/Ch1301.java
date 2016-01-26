package jp.co.se.android.recipe.chapter13;

import jp.co.se.android.recipe.chapter13.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class Ch1301 extends Activity {
    /** Notification�̎��ʗpID */
    int ID_NOTIFICATION_SAMPLE_ACTIVITY = 0x00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1301_main);

        Switch notificationSwitch = (Switch) findViewById(R.id.notificationSwitch);
        notificationSwitch
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {
                        // �X�C�b�`�̐ؑւɘA�����ăX�e�[�^�X�o�[��ON�EOFF
                        showNotification(isChecked);
                    }
                });

    }

    /**
     * �X�e�[�^�X�o�[��\��.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(boolean isShow) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (isShow) {
            // �X�e�[�^�X�o�[��ʒm
            // �u���E�U���N������PendingIntent�𐶐�
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.url_shoeisha)));
            PendingIntent contentIntent = PendingIntent.getActivity(this,
                    ID_NOTIFICATION_SAMPLE_ACTIVITY, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            // Notification�̐ݒ�
            Notification.Builder nb = new Notification.Builder(this);
            // �ʒm�C�x���g�̃^�C���X�^���v
            nb.setWhen(System.currentTimeMillis());
            // �R���e���c���Z�b�g
            nb.setContentIntent(contentIntent);
            // �A�C�R�����Z�b�g
            nb.setSmallIcon(android.R.drawable.stat_notify_chat);
            // �ʒm���ɕ\�����镶����
            nb.setTicker(getString(R.string.label_status_ticker));
            // �X�e�[�^�X�o�[�ɕ\������^�C�g��
            nb.setContentTitle(getString(R.string.label_launch_browser));
            // ���ƃo�C�u�ƃ��C�g
            nb.setDefaults(Notification.DEFAULT_SOUND
                    | Notification.DEFAULT_VIBRATE
                    | Notification.DEFAULT_LIGHTS);
            // �^�b�v����Ǝ����ŕ\��������
            nb.setAutoCancel(true);
            Notification notification = nb.build();

            // Notification��ʒm
            notificationManager.notify(ID_NOTIFICATION_SAMPLE_ACTIVITY,
                    notification);
        } else {
            // �X�e�[�^�X�o�[������
            // Notification���L�����Z��
            notificationManager.cancel(ID_NOTIFICATION_SAMPLE_ACTIVITY);
        }
    }
}
