package com.example.kevinfarel.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class OrderListMenu extends AppCompatActivity {
    String EmailUser,EmailChecker;
    ListView list;
    String jsonString,OrderText;
    JSONArray arr,count;
    JSONObject jObj;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    Context ctx=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_menu2);
        Intent i = getIntent();
        EmailUser=i.getStringExtra("EmailUser");
        list =(ListView) findViewById(R.id.orderlist);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        list.setAdapter(adapter);
        onResume();
    }
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
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
                url = new URL("https://kevinfarel.000webhostapp.com/transactioload.php");
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
                    OrderText = jObj.getString("Order_text");
                    EmailChecker=jObj.getString("Email_User");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(EmailUser.equals(EmailChecker)) {
                    // this line adds the data of your EditText and puts in your array
                    arrayList.add(OrderText);
                    // next thing you have to do is check if your adapter has changed
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
    protected void MainMenuUser(View v)
    {
        Intent i = new Intent(this,MainMenuUser.class);
        i.putExtra("EmailUser",EmailUser);
        startActivity(i);
        OrderListMenu.this.finish();
    }
}
