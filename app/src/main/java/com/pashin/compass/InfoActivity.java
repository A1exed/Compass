package com.pashin.compass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pashin.compass.ui.login.LoginActivity;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }

    public void goCompass(View view) {
        Intent intent = new Intent(InfoActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void goDB(View view) {
        Intent intent = new Intent(InfoActivity.this, DBActivity.class);
        startActivity(intent);
    }

    public void goLogin(View view) {
        Intent intent = new Intent(InfoActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}