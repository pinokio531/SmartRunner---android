package com.haneoum.smartrunner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    //김광훈

    Button BtnSignIn, BtnSignUp;
    EditText inputID, inputPW;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        BtnSignUp = (Button)findViewById(R.id.btn_signup);
        BtnSignIn = (Button)findViewById(R.id.btn_signin);
        inputID = (EditText)findViewById(R.id.inputID);
        inputPW = (EditText)findViewById(R.id.inputPW);
        tv = (TextView)findViewById(R.id.textView2);

        BtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Login.this, "",
                        "Validating user...", true);
                new Thread(new Runnable() {
                    public void run() {
                        login();
                    }
                }).start();
            }
        });
    }

    void login() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://211.177.164.3/Login.php");
            nameValuePairs = new ArrayList(2);
            nameValuePairs.add(new BasicNameValuePair("username", inputID.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("password", inputPW.getText().toString()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv.setText("Response from PHP : " + response);
                    dialog.dismiss();
                }
            });

            if (response.equalsIgnoreCase("User Found")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                    }
                });

                startActivity((new Intent(Login.this, MainActivity.class)));
                finish();
            } else {
                Toast.makeText(Login.this, "Login Fail", Toast.LENGTH_SHORT).show();
                onResume();
            }
        }
        catch(Exception e)
        {
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }

    public void CliSignUp(View view)
    {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
