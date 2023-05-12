package com.example.codefest_cdo.post;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.codefest_cdo.R;

public class PostActivity_ETH_1 extends AppCompatActivity implements View.OnClickListener {

    Button btnInnovative,btnLife,btnThesis,btnOthers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_eth1);

        btnInnovative = findViewById(R.id.btnInnovative);
        btnLife = findViewById(R.id.btnLife);
        btnThesis = findViewById(R.id.btnThesis);
        btnOthers = findViewById(R.id.btnOthers);

        btnInnovative.setOnClickListener(this);
        btnLife.setOnClickListener(this);
        btnThesis.setOnClickListener(this);
        btnOthers.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnInnovative:
                Intent i = new Intent(PostActivity_ETH_1.this,PostActivity_ETH_2.class);
                i.putExtra("title","Innovative Ideas");
                startActivity(i);
                break;
            case R.id.btnLife:
                Intent ii = new Intent(PostActivity_ETH_1.this,PostActivity_ETH_2.class);
                ii.putExtra("title","Life Advice");
                startActivity(ii);
                break;
            case R.id.btnThesis:
                Intent iii = new Intent(PostActivity_ETH_1.this,PostActivity_ETH_2.class);
                iii.putExtra("title","Thesis Ideas");
                startActivity(iii);
                break;
            case R.id.btnOthers:
                Intent iiii = new Intent(PostActivity_ETH_1.this,PostActivity_ETH_2.class);
                iiii.putExtra("title","Others");
                startActivity(iiii);
                break;
        }
    }
}