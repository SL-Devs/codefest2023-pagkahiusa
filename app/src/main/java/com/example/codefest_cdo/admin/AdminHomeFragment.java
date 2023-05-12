package com.example.codefest_cdo.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.codefest_cdo.R;
import com.example.codefest_cdo.RecycleFirebase;
import com.example.codefest_cdo.RecycleFirebase_User;
import com.example.codefest_cdo.data.EFHDetails;
import com.example.codefest_cdo.profile.ProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class AdminHomeFragment extends Fragment {

    FirebaseAuth mAuth;
    DatabaseReference mBase_EFH,mBase_ETH;
    StorageReference storageReference;
    RecyclerView rvTimeline;
    RecycleFirebase adapter;
    ImageView ivHome;

    Button btnEFH,btnETH;
    boolean isChange = false;

    RecycleFirebase recycleFirebase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        View v = inflater.inflate(R.layout.fragment_admin_home, container, false);

        mBase_EFH = FirebaseDatabase.getInstance().getReference().child("EFH_Posts");
        mBase_ETH = FirebaseDatabase.getInstance().getReference().child("ETH_Posts");

        btnEFH = v.findViewById(R.id.btnEFH);
        btnETH = v.findViewById(R.id.btnETH);

        rvTimeline = v.findViewById(R.id.rvTimeline);
        rvTimeline.setLayoutManager(new LinearLayoutManager(getActivity()));

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