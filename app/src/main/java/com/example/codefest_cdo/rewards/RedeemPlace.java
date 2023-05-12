package com.example.codefest_cdo.rewards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.example.codefest_cdo.HomeActivity;
import com.example.codefest_cdo.R;
import com.example.codefest_cdo.profile.ProfileActivity;

public class RedeemPlace extends AppCompatActivity {

    WebView wvLocations;
    ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_place);

        ivBack = findViewById(R.id.ivBack);
        wvLocations = findViewById(R.id.wvLocations);


        WebSettings webSettings = wvLocations.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wvLocations.setInitialScale(500);
        wvLocations.loadUrl("https://plus.codes/6QW6FJGX+RC7");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RedeemPlace.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(RedeemPlace.this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}