package com.example.codefest_cdo.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.codefest_cdo.HomeActivity;
import com.example.codefest_cdo.R;
import com.example.codefest_cdo.RecycleFirebase;
import com.example.codefest_cdo.authentication.LogInActivity;
import com.example.codefest_cdo.data.AccountDetails;
import com.example.codefest_cdo.data.EFHDetails;
import com.example.codefest_cdo.post.PostActivity_2;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity {

    Button btnShowPoints,btnLogOut;
    FirebaseAuth mAuth;
    TextView tvFullname,tvType,tvAddress,tvNumber,tvEmail,tvYes,tvNo,tvPoints,tvPoints_pop;
    ImageView ivClose,ivImage;

    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference mDatabase;

    DatabaseReference mBase_EFH,mBase_ETH;
    Button btnEFH,btnETH,btnMessages,btnGive,btnSend;
    boolean isChange = false;

    DatabaseReference mBase;
    RecyclerView rvTimeline;
    RecycleFirebase adapter;
    ImageView ivHome,ivProfile,ivBack;
    String points;
    RecycleFirebase recycleFirebase;
    EditText etAmmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        ivProfile = findViewById(R.id.ivProfile);
        tvFullname = findViewById(R.id.tvName);
        tvNumber = findViewById(R.id.tvNumber);
        tvAddress = findViewById(R.id.tvAddress);
        tvType = findViewById(R.id.tvType);
        tvPoints = findViewById(R.id.tvPoints);
        ivBack = findViewById(R.id.ivBack);
        btnMessages = findViewById(R.id.btnMessages);
        tvEmail = findViewById(R.id.tvEmail);

        btnEFH = findViewById(R.id.btnEFH);
        btnETH = findViewById(R.id.btnETH);
        btnGive = findViewById(R.id.btnGive);

        rvTimeline = findViewById(R.id.rvTimeline);
        rvTimeline.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        FirebaseRecyclerOptions<EFHDetails> options
                = new FirebaseRecyclerOptions.Builder<EFHDetails>()
                .setQuery(mBase, EFHDetails.class)
                .build();

        adapter = new RecycleFirebase(options);

        rvTimeline.setAdapter(adapter);

        showProfile();

        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://be-one-dd8fd.appspot.com/UserProfiles/" + mAuth.getUid() + ".jpg");
        Glide.with(ProfileActivity.this).load(storageReference).circleCrop().into(ivProfile);

        mBase_EFH = FirebaseDatabase.getInstance().getReference().child("EFH_Posts_Specific").child(mAuth.getUid());
        mBase_ETH = FirebaseDatabase.getInstance().getReference().child("ETH_Posts_Specific").child(mAuth.getUid());

        changeTimeline();

        btnGive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                AccountDetails accountDetails = new AccountDetails();
                mDatabase.child(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            AccountDetails accountDetails = task.getResult().getValue(AccountDetails.class);
                            points = accountDetails.getPoints();
                        }
                    }
                });

                Dialog builder = new Dialog(ProfileActivity.this);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.setContentView(R.layout.send_pop);
                builder.setCancelable(true);
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                etAmmount = builder.findViewById(R.id.etAmmount);
                btnSend = builder.findViewById(R.id.btnSend);
                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(etAmmount.getText().toString().isEmpty()){
                            etAmmount.getFocusable();
                            etAmmount.setError("Please add some ammount first.");
                        }else{
                            int inputAmmount = Integer.parseInt(etAmmount.getText().toString());
                            int currentBalance = Integer.parseInt(points);

                            if(currentBalance < inputAmmount){
                                Toast.makeText(getApplicationContext(), "Insufficient Balance", Toast.LENGTH_SHORT).show();

                            }else{
                                int ammount = currentBalance - inputAmmount;
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                                mDatabase.child(mAuth.getUid()).child("points").setValue(String.valueOf(ammount)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }


//                                CommentDetails commentDetails = new CommentDetails(mAuth.getUid(),comment_id,tvTitle.getText().toString(),tvDetails.getText().toString(),name,postID,etMessage.getText().toString());
//                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Comments"+mAuth.getUid());
//                                mDatabase.child(comment_id).setValue(commentDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Toast.makeText(context, "Message sent!", Toast.LENGTH_SHORT).show();
//                                        builder.dismiss();
//                                    }
//                                });
                        }
                    }
                });



//                    tvUserID = builder.findViewById(R.id.tvUserID);
//                    tvName = builder.findViewById(R.id.tvName);
//                    tvTitle = builder.findViewById(R.id.tvTitle);
//                    tvDescription = builder.findViewById(R.id.tvDescription);
//                    tvMessage = builder.findViewById(R.id.tvMessage);
//
//                    tvUserID.setText(user_id);
//                    tvName.setText(name);
//                    tvTitle.setText(problem_title);
//                    tvDescription.setText(problem_details);
//                    tvMessage.setText(message);



                builder.show();
            }
        });

        btnMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this,Messages.class);
                startActivity(i);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnEFH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChange = false;
                changeTimeline();
            }
        });

        btnETH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChange = true;
                changeTimeline();
            }
        });




//        AccountDetails accountDetails = new AccountDetails();
//
//        mDatabase.child(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                }
//                else {
//                    AccountDetails accountDetails = task.getResult().getValue(AccountDetails.class) ;
//                    tvFullname.setText(accountDetails.getFullname());
//
////                    tvAddress.setText(accountDetails.getAddress());
//                    Toast.makeText(ProfileActivity.this, accountDetails.getFullname(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        storage = FirebaseStorage.getInstance();
//        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://be-one-dd8fd.appspot.com/RewardImages/R1/cap1.png");
//
//        Glide.with(ProfileActivity.this).load(storageReference).into(ivImage);



        btnShowPoints = findViewById(R.id.btnShowPoints);
        btnShowPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog builder = new Dialog(ProfileActivity.this);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.setContentView(R.layout.show_points_pop);
                builder.setCancelable(true);
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                tvPoints_pop = builder.findViewById(R.id.tvPoints);
                tvPoints_pop.setText(tvPoints.getText().toString() + " pt/s");
                ivClose = builder.findViewById(R.id.ivClose);
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.dismiss();
                    }
                });
                builder.show();
            }
        });
        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog builder = new Dialog(ProfileActivity.this);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.setContentView(R.layout.log_out_pop);
                builder.setCancelable(true);
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                tvYes = builder.findViewById(R.id.tvYes);
                tvNo = builder.findViewById(R.id.tvNo);
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAuth.signOut();
                        Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                        startActivity(i);
                    }
                });

                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.dismiss();
                    }
                });
                builder.show();

            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ProfileActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void showProfile(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        AccountDetails accountDetails = new AccountDetails();
        mDatabase.child(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    AccountDetails accountDetails = task.getResult().getValue(AccountDetails.class);
                    tvPoints.setText(accountDetails.getPoints());
                    tvFullname.setText(accountDetails.getFullname());
                    tvType.setText(accountDetails.getType());
                    tvAddress.setText(accountDetails.getAddress());
                    tvNumber.setText(accountDetails.getMobile_number());
                    tvEmail.setText(accountDetails.getEmail());

                }
            }
        });
    }

    public void changeTimeline(){
        if(isChange == false){
            FirebaseRecyclerOptions<EFHDetails> options
                    = new FirebaseRecyclerOptions.Builder<EFHDetails>()
                    .setQuery(mBase_EFH, EFHDetails.class)
                    .build();

            adapter = new RecycleFirebase(options);

            rvTimeline.setAdapter(adapter);
            adapter.startListening();
        }else{
            FirebaseRecyclerOptions<EFHDetails> options
                    = new FirebaseRecyclerOptions.Builder<EFHDetails>()
                    .setQuery(mBase_ETH, EFHDetails.class)
                    .build();

            adapter = new RecycleFirebase(options);

            rvTimeline.setAdapter(adapter);
            adapter.startListening();
        }
    }

}