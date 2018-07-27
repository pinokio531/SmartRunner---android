package com.haneoum.smartrunner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.thalmic.myo.scanner.ScanActivity;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private long pressedtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.Bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new Monitoring());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.action_scan == id) {
            onScanActionSelected();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void onScanActionSelected() {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

            return true;
        }

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        Fragment fragment = null;

        switch (item.getItemId()) {

            case R.id.navigation_home:
                fragment = new Monitoring();
                break;
            case R.id.navigation_dashboard:
                fragment = new Songs();
                break;
            case R.id.navigation_notifications:
                fragment = new Setting();
                break;
        }
        return loadFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() >= pressedtime + 2000) {
            pressedtime = System.currentTimeMillis();
            Toast.makeText(this, " 한번 더 누르면 어플이 종료됩니다", Toast.LENGTH_SHORT).show();
            return;
        } else {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            finish();
        }
    }
}
