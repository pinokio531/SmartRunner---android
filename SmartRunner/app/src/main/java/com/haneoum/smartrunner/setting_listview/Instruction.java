package com.haneoum.smartrunner.setting_listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.haneoum.smartrunner.R;

public class Instruction extends AppCompatActivity {
    //임승섭
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instruction);

        Bundle bundle = getIntent().getExtras();
        String text = bundle.getString("설명서");

        TextView T = (TextView) findViewById(R.id.Tex);
        T.setText(text);
    }

}
