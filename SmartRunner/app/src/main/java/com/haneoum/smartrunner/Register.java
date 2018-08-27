package com.haneoum.smartrunner;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Register extends AppCompatActivity {

    //김광훈

    private EditText editTextID;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextAge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextID = (EditText) findViewById(R.id.userID);
        editTextPassword = (EditText) findViewById(R.id.userPassword);
        editTextName = (EditText) findViewById(R.id.userName);
        editTextAge = (EditText) findViewById(R.id.userAge);
    }

    public void insert(View view) {
        String userID = editTextID.getText().toString();
        String userPassword = editTextPassword.getText().toString();
        String userName = editTextName.getText().toString();
        int userAge = Integer.parseInt(editTextAge.getText().toString());

        insertoToDatabase(userID, userPassword, userName, userAge+"");
    }
    private void insertoToDatabase(String userID, String userPassword, String userName, String userAge) {
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String userID = (String) params[0];
                    String userPassword = (String) params[1];
                    String userName = (String) params[2];
                    String userAge = (String) params[3];

                    String link = "http://211.177.164.3/Register.php";
                    String data = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(userID, "UTF-8");
                    data += "&" + URLEncoder.encode("userPassword", "UTF-8") + "=" + URLEncoder.encode(userPassword, "UTF-8");
                    data += "&" + URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8");
                    data += "&" + URLEncoder.encode("userAge", "UTF-8") + "=" + URLEncoder.encode(userAge, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(userID, userPassword, userName, userAge);
    }
}
