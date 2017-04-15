package com.example.kevinfarel.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class PrintNowMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_now_menu);
    }
    public void PilihLokasiPrinter(View v)
    {
        Intent i = new Intent(this,UserChoosePrinterMap.class);
        startActivity(i);
        finish();
    }
}
