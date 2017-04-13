package com.example.kevinfarel.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends AppCompatActivity{
    int flag=0;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private EditText Email;
    private EditText Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCompat.requestPermissions(Login.this,new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION},1);
        Email=(EditText) findViewById(R.id.etEmail);
        Password=(EditText) findViewById(R.id.etPassword);
    }
    public void CheckLoginUser(View arg0) {

        // Get text from email and password field
        final String email = Email.getText().toString();
        final String password = Password.getText().toString();
        flag=1;
        // Initialize  AsyncLogin() class with email and password
        new AsyncLogin().execute(email, password);
    }
    public void CheckLoginPrinter(View arg0) {

        // Get text from email and password field
        final String email = Email.getText().toString();
        final String password = Password.getText().toString();
        flag=2;
        // Initialize  AsyncLogin() class with email and password
        new AsyncLogin().execute(email, password);
    }
    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Login.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tPlease Wait...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                if(flag==1) {
                    url = new URL("https://kevinfarel.000webhostapp.com/Login_User.php");
                }
                else if(flag==2) {
                    url = new URL("https://kevinfarel.000webhostapp.com/Login_Printer.php");
                }

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
                try {
                    // Setup HttpURLConnection class to send and receive data from php and mysql
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setRequestMethod("POST");

                    // setDoInput and setDoOutput method depict handling of both send and receive
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    // Append parameters to URL
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("email", params[0])
                            .appendQueryParameter("password", params[1]);
                    String query = builder.build().getEncodedQuery();

                    // Open connection for sending data
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();
                    conn.connect();

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    return "exception";
                }
                try {
                    int response_code = conn.getResponseCode();

                    // Check if successful connection made
                    if (response_code == HttpURLConnection.HTTP_OK) {

                        // Read data sent from server
                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder result = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        // Pass data to onPostExecute method
                        return (result.toString());

                    } else {
                        return ("unsuccessful");
                    }

                } catch (IOException el) {
                    el.printStackTrace();
                    return "exception";
                } finally {
                    conn.disconnect();
                }
        }
        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            if(result.equalsIgnoreCase("true"))
            {
            /* Here launching another activity when login successful. If you persist login state
            use sharedPreferences of Android. and logout button to clear sharedPreferences.
             */
            Intent intent=null;
                final String email = Email.getText().toString();
                SaveSharedPreferences.setUserName(Login.this,email);
                if(flag==1) {
                    intent = new Intent(Login.this, MainMenuUser.class);
                }
                else if(flag==2) {
                    intent = new Intent(Login.this, MainMenuUser.class);
                }
                Toast.makeText(Login.this,"Welcome to PrintItUp , "+Email.getText().toString() ,Toast.LENGTH_LONG).show();
                startActivity(intent);
                Login.this.finish();

            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                Toast.makeText(Login.this, "Invalid Email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(Login.this, "Oops! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }
}
        public void pindahRegister(View v) {
            Intent i = new Intent(this, Register.class);
            startActivity(i);
            finish();
        }
    }


