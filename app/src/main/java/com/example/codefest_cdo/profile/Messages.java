package com.example.codefest_cdo.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.codefest_cdo.HomeActivity;
import com.example.codefest_cdo.R;
import com.example.codefest_cdo.RecycleComments;
import com.example.codefest_cdo.RecycleFirebase_User;
import com.example.codefest_cdo.data.CommentDetails;
import com.example.codefest_cdo.data.EFHDetails;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class Messages extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mBase_EFH,mBase_ETH;
    DatabaseReference mBase;
    StorageReference storageReference;
    RecyclerView rvTimeline;
    RecycleComments adapter;
    ImageView ivHome,ivProfile,ivBack;

    TextView tvName;
    Button btnEFH,btnETH;
    boolean isChange = false;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        mAuth = FirebaseAuth.getInstance();

        mBase = FirebaseDatabase.getInstance().getReference().child("Comments" + mAuth.getUid());


        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Messages.this, HomeActivity.class);
                startActivity(i);
            }
        });

        rvTimeline = findViewById(R.id.rvTimeline);
        rvTimeline.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        FirebaseRecyclerOptions<CommentDetails> options
                = new FirebaseRecyclerOptions.Builder<CommentDetails>()
                .setQuery(mBase, CommentDetails.class)
                .build();

        adapter = new RecycleComments(options);

        rvTimeline.setAdapter(adapter);
        adapter.startListening();



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Messages.this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}