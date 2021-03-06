package com.example.kevinfarel.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainMenuUser extends AppCompatActivity {
    String email,nama;
    String jsonString;
    String EmailChecker;
    JSONArray arr,count;
    JSONObject jObj;
    String Nama_User;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_user);
        Intent i = getIntent();
        email=i.getStringExtra("email");
        new AsyncCaller().execute();
    }
    private class AsyncCaller extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
        }

        @Override
        protected Void doInBackground(Void... params) {
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you don't want to pass argument and u can access the parent class' variable url over here
            URL url = null;
            try {
                url = new URL("https://kevinfarel.000webhostapp.com/loaduser.php");
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
                jsonString = IOUtils.toString(in, "UTF-8");
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
            try {
                count= new JSONArray(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int a=0;a<count.length();a++) {
                try {
                    arr = new JSONArray(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jObj = arr.getJSONObject(a);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Nama_User = jObj.getString("Nama_User");
                    EmailChecker=jObj.getString("Email_User");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(email.equals(EmailChecker)) {
                   nama=Nama_User;
                }
            }
        }
    }
    public void PrintNowMenu(View v)
    {
        Intent i = new Intent(this,PrintNowMenu.class);
        i.putExtra("address","Not Chosen Yet");
        i.putExtra("name","Not Chosen Yet");
        i.putExtra("EmailPrinter","Not Chosen");
        i.putExtra("EmailUser",email);
        i.putExtra("NamaUser",nama);
        startActivity(i);
        finish();
    }
    public void OrderListMenu(View v)
    {
        Intent i = new Intent(this,OrderListMenu.class);
        i.putExtra("EmailUser",email);
        startActivity(i);
        finish();
    }
}
