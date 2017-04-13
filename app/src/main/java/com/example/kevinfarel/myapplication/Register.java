package com.example.kevinfarel.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
    public void pindahRegisterPrinter(View v)
    {
        Intent i = new Intent(this,RegisterAsPrinter.class);
        String latlong=("Not Chosen,Not Chosen");
        i.putExtra("latlong",latlong);
        startActivity(i);
        finish();
    }
    public void pindahRegisterUser(View v)
    {
        Intent i = new Intent(this,RegisterAsUser.class);
        startActivity(i);
        finish();
    }
}
