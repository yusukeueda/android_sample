package jp.co.se.android.recipe.chapter11;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class Ch1103 extends Activity implements LocationListener {
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1103_main);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    protected void onPause() {
        // ���P�[�V�����}�l�[�W���̃��X�i�[������
        mLocationManager.removeUpdates(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ���P�[�V�����}�l�[�W���̃��X�i�[��o�^
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        String provider = mLocationManager.getBestProvider(criteria, true);
        mLocationManager.requestLocationUpdates(provider, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        // ���P�[�V�������ύX���ꂽ��
        TextView tvLatitude = (TextView) findViewById(R.id.Latitude);
        TextView tvLongtude = (TextView) findViewById(R.id.Longitude);
        tvLatitude.setText(String.valueOf(location.getLatitude()));
        tvLongtude.setText(String.valueOf(location.getLongitude()));
    }

    @Override
    public void onProviderDisabled(String provider) {
        // ���P�[�V�����v���o�C�_�[�������ɂȂ�����
    }

    @Override
    public void onProviderEnabled(String provider) {
        // ���P�[�V�����v���o�C�_�[���L���ɂȂ�����
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // ���P�[�V�����v���o�C�_�[���ύX���ꂽ��
    }

}
