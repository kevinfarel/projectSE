package com.example.kevinfarel.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PrintNowMenu extends AppCompatActivity {
    Spinner spinner;
    String Email,EmailUser,EmailPrinter,jsonString;
    String Type,Price;
    JSONArray arr,count;
    JSONObject jObj;
    List<String> listtype = new ArrayList<String>();
    List<String> listprice = new ArrayList<String>();

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
                    Email=  jObj.getString("Email_Printer");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(Email.equals(EmailPrinter)) {
                    listtype.add(Type);
                    listprice.add(Price);
                }
            }
                SpinnerLoad(listtype);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_now_menu);
        TextView info = (TextView) findViewById(R.id.infoPrinter);
        Intent i = getIntent();
        info.setText("Printer's Name    : "+i.getStringExtra("name")+"\nAddress    :"+i.getStringExtra("address"));
        EmailPrinter=i.getStringExtra("EmailPrinter");
        EmailUser=i.getStringExtra("EmailUser");
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView price = (TextView) findViewById(R.id.PriceText);
                int x=spinner.getSelectedItemPosition();
                price.setText("Price each page: "+listprice.get(x));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if(EmailPrinter.equals("Not Chosen"))
        {
            Toast.makeText(this, "Please Choose Location to print first", Toast.LENGTH_SHORT).show();
        }else {
            new AsyncCaller().execute();
        }
    }
    protected void SpinnerLoad(List<String> list)
    {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        Toast.makeText(this, "Data Loaded", Toast.LENGTH_SHORT).show();
    }
    public void PilihLokasiPrinter(View v)
    {
        Intent i = new Intent(this,UserChoosePrinterMap.class);
        startActivity(i);
        finish();
    }
}
