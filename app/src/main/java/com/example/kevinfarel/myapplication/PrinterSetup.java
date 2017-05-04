package com.example.kevinfarel.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PrinterSetup extends AppCompatActivity
{
    EditText Ptype,Pprice;
    ListView list;
    String Email,Type,Price,EmailChecker;
    String jsonString;
    JSONArray arr,count;
    JSONObject jObj;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    Context ctx=this;
    class Background2 extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String type = params[1];
            String price = params[2];
            String data = "";
            int tmp;
            try {
                URL url = new URL("https://kevinfarel.000webhostapp.com/addpaper.php");
                String urlParams = "email="+email+"&type="+type+"&price="+price;
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }
                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("")){
                s="Paper Added.";
            }
            Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
        }
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
                url = new URL("https://kevinfarel.000webhostapp.com/loadpaper.php");
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
            adapter.clear();
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
                    Type = jObj.getString("Paper_Type");
                    Price = jObj.getString("Paper_Price");
                    EmailChecker=jObj.getString("Email_Printer");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(Email.equals(EmailChecker)) {
                    // this line adds the data of your EditText and puts in your array
                    arrayList.add(Type + "  " + Price);
                    // next thing you have to do is check if your adapter has changed
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_setup);
        Ptype=(EditText) findViewById(R.id.papertype);
        Pprice=(EditText) findViewById(R.id.paperprice);
        list =(ListView) findViewById(R.id.listpaper);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        list.setAdapter(adapter);
        Intent i = getIntent();
        Email = i.getStringExtra("Email");
    }
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        new AsyncCaller().execute();
    }

    public void AddPaper(View v)
    {
        Type=Ptype.getText().toString();
        Price=Pprice.getText().toString();
        Background2 b = new Background2();
        b.execute(Email,Type,Price);
        onResume();
    }
}
