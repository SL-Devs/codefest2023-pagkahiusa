package com.example.codefest_cdo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.codefest_cdo.authentication.LogInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {


    Handler handler;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    TextView tvName;
    ImageView ivLogo,ivName;

    /*
    * Dialog builder = new Dialog(context);
    * builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
    * builder.setContentView(layout);
    * builder.setCancelable(true);
    * b = builder.findViewById(R.id.b);
    * builder.sho0w();
    *
    *
    * date = new SampleDateFormat("MM/DD/YY");
    * time = new SampleDateFormat("hh:mm:ss a");
    *
    * String currentDate = date.format(new Date());
     * String currentTime = time.format(new Date());

     * */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        ivLogo = findViewById(R.id.ivLogo);
        ivName = findViewById(R.id.ivName);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_anim);
        ivLogo.startAnimation(anim);



        Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_anim);
        ivName.startAnimation(anim2);



        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(currentUser != null){
//                    reload();
                    Intent i = new Intent(SplashActivity.this,HomeActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent ii = new Intent(SplashActivity.this, LogInActivity.class);
                    startActivity(ii);
                    finish();
                }


            }
        },3000);

    }
}