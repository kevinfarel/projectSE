package com.example.kevinfarel.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class UserChoosePrinterMap extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    String jsonString;
    ArrayList<String> stringArray = new ArrayList<String>();
    JSONArray arr,count;
    JSONObject jObj;
    String x,y,name,status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_choose_printer_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        new AsyncCaller().execute();
    }
    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(UserChoosePrinterMap.this);

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
                url = new URL("https://kevinfarel.000webhostapp.com/send-data.php");
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
                jsonString = IOUtils.toString(in,"UTF-8");
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
                    x = jObj.getString("Position_Latitude");
                    y = jObj.getString("Position_Longitude");
                    name = jObj.getString("Nama_Printer");
                    status= jObj.getString("Status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(status.equals("Online")) {
                    MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(Double.parseDouble(x), Double.parseDouble(y))).title(name);
                    mMap.addMarker(marker);
                }
            }
        }

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
    }
}
