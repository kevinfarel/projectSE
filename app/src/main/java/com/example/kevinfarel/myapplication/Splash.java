package com.example.kevinfarel.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    public String test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent i = new Intent(Splash.this,Login.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        new AsyncCaller().execute();

    }
    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(Splash.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            URL url = null;
            try {
                url = new URL("https://kevinfarel.000webhostapp.com/Login_User.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
                test = IOUtils.toString(in,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            try {
                url = new URL("https://kevinfarel.000webhostapp.com/Login_Printer.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
                test = IOUtils.toString(in,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread
            // Toast.makeText(SplashScreen.this, test, Toast.LENGTH_SHORT).show();
            pdLoading.dismiss();
        }


    }
}
