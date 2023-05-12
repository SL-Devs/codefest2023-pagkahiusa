package com.example.codefest_cdo.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.codefest_cdo.R;
import com.example.codefest_cdo.RecycleFirebase;
import com.example.codefest_cdo.RecycleFirebase_User;
import com.example.codefest_cdo.authentication.LogInActivity;
import com.example.codefest_cdo.data.AccountDetails;
import com.example.codefest_cdo.data.EFHDetails;
import com.example.codefest_cdo.profile.ProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeFragment extends Fragment {


    FirebaseAuth mAuth;
    DatabaseReference mBase_EFH,mBase_ETH;
    DatabaseReference mDatabase;
    StorageReference storageReference;
    RecyclerView rvTimeline;
    RecycleFirebase_User adapter;
    ImageView ivHome,ivProfile;

    TextView tvName;
    Button btnEFH,btnETH;
    boolean isChange = false;
    View v;

    RecycleFirebase_User recycleFirebase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mAuth = FirebaseAuth.getInstance();


        v = inflater.inflate(R.layout.fragment_home, container, false);

        mBase_EFH = FirebaseDatabase.getInstance().getReference().child("EFH_Posts");
        mBase_ETH = FirebaseDatabase.getInstance().getReference().child("ETH_Posts");

        btnEFH = v.findViewById(R.id.btnEFH);
        btnETH = v.findViewById(R.id.btnETH);
        tvName = v.findViewById(R.id.tvName);

        rvTimeline = v.findViewById(R.id.rvTimeline);
        rvTimeline.setLayoutManager(new LinearLayoutManager(getActivity()));

//        rvTimeline = v.findViewById(R.id.rvTimeline);
//        rvTimeline.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        FirebaseRecyclerOptions<EFHDetails> options
//                = new FirebaseRecyclerOptions.Builder<EFHDetails>()
//                .setQuery(mBase_EFH, EFHDetails.class)
//                .build();
//
//        adapter = new RecycleFirebase(options);
//
//        rvTimeline.setAdapter(adapter);

        changeTimeline();

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
                        tvName.setText("Hi " + accountDetails.getFullname() + "!");
                    }
                }
            });



        ivProfile = v.findViewById(R.id.ivProfile);

        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://be-one-dd8fd.appspot.com/UserProfiles/" + mAuth.getUid() + ".jpg");
        Glide.with(getContext()).load(storageReference).circleCrop().into(ivProfile);

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                startActivity(i);
            }
        });
//        ivHome = v.findViewById(R.id.home);
//        ivHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAuth.signOut();
//                Intent i = new Intent(getActivity(), LogInActivity.class);
//                startActivity(i);
//            }
//        });







        return  v;

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

    public void changeTimeline(){
        if(isChange == false){
            FirebaseRecyclerOptions<EFHDetails> options
                    = new FirebaseRecyclerOptions.Builder<EFHDetails>()
                    .setQuery(mBase_EFH, EFHDetails.class)
                    .build();

            adapter = new RecycleFirebase_User(options);

            rvTimeline.setAdapter(adapter);
            adapter.startListening();
        }else{
            FirebaseRecyclerOptions<EFHDetails> options
                    = new FirebaseRecyclerOptions.Builder<EFHDetails>()
                    .setQuery(mBase_ETH, EFHDetails.class)
                    .build();

            adapter = new RecycleFirebase_User(options);

            rvTimeline.setAdapter(adapter);
            adapter.startListening();
        }
    }

}