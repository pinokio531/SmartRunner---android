package com.haneoum.smartrunner.setting_listview;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.haneoum.smartrunner.R;
import com.haneoum.smartrunner.setting_listview.location.location_settings;

public class Warning extends AppCompatActivity {
    //임승섭
    MediaPlayer mp;
    ListView lV;
    String[] warninglist = {"warning horn 1", "warning horn 2", "warning horn 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warning);

        Bundle bundle = getIntent().getExtras();
        String text = bundle.getString("경고음 설정");

        TextView Tx = (TextView) findViewById(R.id.Text);
        Tx.setText(text);

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar_volume);
        final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int nMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int nCurrentVolume = audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);

        seekBar.setMax(nMax);
        seekBar.setProgress(nCurrentVolume);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), location_settings.class);
                startActivity(intent);
            }
        });

        lV = (ListView) findViewById(R.id.waringlist);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, warninglist);

        lV.setAdapter(arrayAdapter);

        lV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getItemAtPosition(i);
                if (i == 0) {
                    if (mp == null) {
                        mp = MediaPlayer.create(Warning.this, R.raw.siren);
                        mp.start();
                    } else {
                        PlayStop();
                        mp = MediaPlayer.create(Warning.this, R.raw.siren);
                        mp.start();
                    }
                } else if (i == 1) {
                    if (mp == null) {
                        mp = MediaPlayer.create(Warning.this, R.raw.abc);
                        mp.start();
                    } else {
                        PlayStop();
                        mp = MediaPlayer.create(Warning.this, R.raw.abc);
                        mp.start();
                    }
                } else if (i == 2) {
                    if (mp == null) {
                        mp = MediaPlayer.create(Warning.this, R.raw.def);
                        mp.start();
                    } else {
                        PlayStop();
                        mp = MediaPlayer.create(Warning.this, R.raw.def);
                        mp.start();
                    }
                }
            }

        });
    }
    @Override
    public void onBackPressed() {
        if (mp.isPlaying() == true) {
            PlayStop();
            super.onBackPressed();
            Intent intent;
            intent = new Intent(getParent(), Setting.class);
            startActivity(intent);
        }
        else {
            super.onBackPressed();
            Intent intent;
            intent = new Intent(getParent(), Setting.class);
            startActivity(intent);
        }

    }

    public void PlayStop() {
        mp.stop();
        mp.release();
    }
}
