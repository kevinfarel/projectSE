package com.example.kevinfarel.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenuUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_user);
    }
    public void PrintNowMenu(View v)
    {
        Intent i = new Intent(this,PrintNowMenu.class);
        startActivity(i);
        finish();
    }
}
