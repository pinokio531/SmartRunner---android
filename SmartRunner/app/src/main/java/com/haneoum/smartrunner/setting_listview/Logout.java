package com.haneoum.smartrunner.setting_listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.haneoum.smartrunner.R;

public class Logout extends AppCompatActivity {
    //임승섭
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout);

        Bundle bundle = getIntent().getExtras();
        String text = bundle.getString("로그아웃");

        TextView Txt = (TextView) findViewById(R.id.Te);
        Txt.setText(text);
    }

}
