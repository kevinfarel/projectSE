package com.example.kevinfarel.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterAsUser extends AppCompatActivity {
    EditText name, password, email, phone;
    String Name, Password, Email, Phone;
    Context ctx=this;

    class Background extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[2];
            String email = params[1];
            String phone = params[3];
            String data="";
            int tmp;

            try {
                URL url = new URL("https://kevinfarel.000webhostapp.com/register_user.php");
                String urlParams = "name="+name+"&email="+email+"&password="+password+"&phone="+phone;
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }
                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
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
            RegisterAsUser.this.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as_user);
        name = (EditText) findViewById(R.id.etName);
        password = (EditText) findViewById(R.id.etPassword);
        email = (EditText) findViewById(R.id.etEmail);
        phone= (EditText) findViewById(R.id.etPhone);
    }
    public void pindahLogin(View v)
    {
        Intent i = new Intent(this,Login.class);
        startActivity(i);
        finish();
    }
    public void RegisterButton(View v){
        Name = name.getText().toString();
        Password = password.getText().toString();
        Email = email.getText().toString();
        Phone=phone.getText().toString();
        Background b = new Background();
        b.execute(Name, Email, Password, Phone);
    }
}
