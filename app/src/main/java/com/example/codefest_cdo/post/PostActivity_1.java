package com.example.codefest_cdo.post;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.codefest_cdo.R;

public class PostActivity_1 extends AppCompatActivity implements View.OnClickListener {

    Button btnMental,btnHealth,btnFinancial,btnOthers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post1);


        btnMental = findViewById(R.id.btnMental);
        btnHealth = findViewById(R.id.btnHealth);
        btnFinancial = findViewById(R.id.btnFinancial);
        btnOthers = findViewById(R.id.btnOthers);

        btnMental.setOnClickListener(this);
        btnHealth.setOnClickListener(this);
        btnFinancial.setOnClickListener(this);
        btnOthers.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnMental:
                Intent i = new Intent(PostActivity_1.this,PostActivity_2.class);
                i.putExtra("title","Mental Health Issue");
                startActivity(i);
                break;
            case R.id.btnHealth:
                Intent ii = new Intent(PostActivity_1.this,PostActivity_2.class);
                ii.putExtra("title","Health Problem");
                startActivity(ii);
                break;
            case R.id.btnFinancial:
                Intent iii = new Intent(PostActivity_1.this,PostActivity_2.class);
                iii.putExtra("title","Financial Problem");
                startActivity(iii);
                break;
            case R.id.btnOthers:
                Intent iiii = new Intent(PostActivity_1.this,PostActivity_2.class);
                iiii.putExtra("title","Others");
                startActivity(iiii);
                break;

        }
    }
}