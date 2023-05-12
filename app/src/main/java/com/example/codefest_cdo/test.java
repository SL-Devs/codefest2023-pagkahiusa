package com.example.codefest_cdo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class test extends AppCompatActivity {

    WebView wvLocations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        wvLocations = findViewById(R.id.wvLocations);
        WebSettings webSettings = wvLocations.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wvLocations.loadUrl("https://plus.codes/6QW6FJGX+RC7");


    }
}