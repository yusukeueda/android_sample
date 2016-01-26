package jp.co.se.android.recipe.chapter13;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class Ch1310 extends Activity {
    private String TAG = "Ch1310";
    private String PROJECT_NUMBER = "<PROJECT_NUMBER>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1310_main);

        findViewById(R.id.buttonRegister).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        registerInBackground(Ch1310.this);
                    }
                });

        findViewById(R.id.buttonUnRegister).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        unrgisterInBackground(Ch1310.this);
                    }
                });
    }

    private void unrgisterInBackground(final Context context) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Log.d(TAG, "GCM�����J�n");
                try {
                    // GoogleCloudMessaging�C���X�^���X�̎擾
                    GoogleCloudMessaging gcm = GoogleCloudMessaging
                            .getInstance(context);
                    if (gcm != null) {
                        // GCM��������
                        gcm.unregister();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(null, null, null);
    }

    private void registerInBackground(final Context context) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Log.d(TAG, "GCM�o�^�J�n");
                try {
                    // GoogleCloudMessaging�C���X�^���X�̎擾
                    GoogleCloudMessaging gcm = GoogleCloudMessaging
                            .getInstance(context);
                    if (gcm != null) {
                        // GCM�o�^����
                        String regid = gcm.register(PROJECT_NUMBER);
                        Log.d(TAG, "�f�o�C�X�o�^�����@�o�^ID=" + regid);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(null, null, null);
    }

    // @formatter:off
    // curl --header
    // "Authorization: key=<API KEY>" --header
    // Content-Type:"application/json" https://android.googleapis.com/gcm/send
    // -d
    // "{\"registration_ids\":[\"<RegistrationID>\"],\"data\":{\"message\":\"Hello\"}}"
    // @formatter:on
}
