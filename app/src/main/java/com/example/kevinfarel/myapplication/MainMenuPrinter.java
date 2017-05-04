package com.example.kevinfarel.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainMenuPrinter extends AppCompatActivity {
    String Email;
    private TextView switchStatus;
    private Switch mySwitch;

    class Background extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            String status = params[1];
            String email = params[0];
            String data = "";
            int tmp;
            try {
                URL url = new URL("https://kevinfarel.000webhostapp.com/Status_online.php");
                String urlParams = "email=" + email + "&status=" + status;

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
        protected void onPostExecute(String s){
            if (s.equals("")) {
                s = "Switched";
            }
            Toast.makeText(MainMenuPrinter.this, s, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_printer);
        Intent intent=getIntent();
        Email = intent.getStringExtra("email");
        Toast.makeText(this, Email, Toast.LENGTH_SHORT).show();
        switchStatus = (TextView) findViewById(R.id.statview);
        mySwitch = (Switch) findViewById(R.id.statswitch);
        //set the switch to OFF
        mySwitch.setChecked(false);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    Toast.makeText(MainMenuPrinter.this, "on", Toast.LENGTH_SHORT).show();
                    switchStatus.setText("Online");
                    Background b= new Background();
                    b.execute(Email,"Online");
                }else{
                    Toast.makeText(MainMenuPrinter.this, "off", Toast.LENGTH_SHORT).show();
                    switchStatus.setText("Offline");
                    Background b= new Background();
                    b.execute(Email,"Offline");
                }
            }
        });
        //check the current state before we display the screen
        if(mySwitch.isChecked()){
            switchStatus.setText("Online");
        }
        else {
            switchStatus.setText("Offline");
        }
    }
    public void PindahSetupPrinter(View v) {
        Intent a = new Intent(this, PrinterSetup.class);
        a.putExtra("Email",Email);
        startActivity(a);
        finish();
    }
}
