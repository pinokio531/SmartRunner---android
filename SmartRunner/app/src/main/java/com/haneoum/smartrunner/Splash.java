package com.haneoum.smartrunner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);



        if (ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            WaitingTime("Permission.class");
        }
        else{
            WaitingTime("Login.class");
        }

    }

    public void WaitingTime(final String className) {

        Thread SplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int wait = 0;
                    while (wait < 1000) {
                        sleep(100);
                        wait += 100;
                    }
                    whichClass(className);
                } catch (InterruptedException e) {

                }
            }

        };
        SplashThread.start();
    }

    public void whichClass(String clsName){
        Intent intent;
        switch (clsName) {
            case "Permission.class":
                intent = new Intent(this, Permission.class);
                startActivity(intent);
                finish();
                break;

            case "Login.class":
                intent = new Intent(this, Login.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
