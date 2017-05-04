package com.example.kevinfarel.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenuUser extends AppCompatActivity {
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_user);
        Intent i = getIntent();
        email=i.getStringExtra("email");
    }
    public void PrintNowMenu(View v)
    {
        Intent i = new Intent(this,PrintNowMenu.class);
        i.putExtra("address","Not Chosen Yet");
        i.putExtra("name","Not Chosen Yet");
        i.putExtra("EmailPrinter","Not Chosen");
        i.putExtra("EmailUser",email);
        startActivity(i);
        finish();
    }
}
