package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class Ch0110 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0110_main);

        // ToggleButtonの切り替えを検出
        final ToggleButton tb = (ToggleButton) findViewById(R.id.ToggleButton);
        tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                Toast.makeText(Ch0110.this,
                        getString(R.string.ch0110_toggle_shift, isChecked),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Switchの切り替えを検出
        final Switch sw = (Switch) findViewById(R.id.Switch);
        sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                Toast.makeText(Ch0110.this,
                        getString(R.string.ch0110_switch_shift, isChecked),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
