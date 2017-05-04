package com.example.kevinfarel.myapplication;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;


public class RegisterAsPrinter extends AppCompatActivity{
    EditText name, password, email;
    String Name, Password, Email, Latitude, Longitude,Status;
    Context ctx=this;

    class Background extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[1];
            String password = params[2];
            String email = params[0];
            String latitude = params[3];
            String longitude = params[4];
            String status=params[5];
            String data = "";
            int tmp;
            try {
                URL url = new URL("https://kevinfarel.000webhostapp.com/register_printer.php");
                String urlParams = "email="+email+"&name="+name+"&password="+password+"&latitude="+latitude+"&longitude="+longitude+"&status"+status;

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
                s="Data saved successfully.";
            }
            Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
            Intent i = new Intent(ctx, Login.class);
            startActivity(i);
            RegisterAsPrinter.this.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as_printer);
        name = (EditText) findViewById(R.id.etName);
        password = (EditText) findViewById(R.id.etPassword);
        email = (EditText) findViewById(R.id.etEmail);
        TextView address = (TextView) findViewById(R.id.addr);
        Intent i = getIntent();
        String[] LatLong = (i.getStringExtra("latlong")).split(",");
        if(LatLong[0].equals("Not Chosen")) {
            address.setText("Address Not Chosen Yet..");
        }else{
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());
            double latitude = Double.parseDouble(LatLong[0]);
            double longitude = Double.parseDouble(LatLong[1]);
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            address.setText(addresses.get(0).getAddressLine(0));
        }
    }
    public void pindahLogin(View v)
    {
        Intent i = new Intent(this,Login.class);
        startActivity(i);
        finish();
    }
    public void PindahMapRegister(View v)
    {
        Intent i = new Intent(this,RegisterMapsActivity. class);
        startActivity(i);
        finish();
    }
    public void RegisterButton(View v)
    {
        Name = name.getText().toString();
        Password = password.getText().toString();
        Email = email.getText().toString();
        Intent i = getIntent();
        String[] LatLong = (i.getStringExtra("latlong")).split(",");
        Latitude=LatLong[0];
        Longitude=LatLong[1];
        Status="Offline";
        RegisterAsPrinter.Background c = new RegisterAsPrinter.Background();
        c.execute(Email, Name, Password, Latitude,Longitude,Status);
    }


}

