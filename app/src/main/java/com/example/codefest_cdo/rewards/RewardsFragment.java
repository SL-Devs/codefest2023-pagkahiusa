package com.example.codefest_cdo.rewards;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.example.codefest_cdo.R;
import com.example.codefest_cdo.RecycleFirebase;
import com.example.codefest_cdo.RecycleFirebase_Rewards;
import com.example.codefest_cdo.data.EFHDetails;
import com.example.codefest_cdo.data.RewardsDetails;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RewardsFragment extends Fragment {

    Button btnLocation;
    FirebaseAuth mAuth;
    DatabaseReference mBase;
    RecyclerView rvTimeline;
    RecycleFirebase_Rewards adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();

        View v = inflater.inflate(R.layout.fragment_rewards, container, false);




        btnLocation = v.findViewById(R.id.btnLocation);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),RedeemPlace.class);
                startActivity(i);
            }
        });


        mBase = FirebaseDatabase.getInstance().getReference().child("Rewards");

        rvTimeline = v.findViewById(R.id.rvTimeline);
        rvTimeline.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseRecyclerOptions<RewardsDetails> options
                = new FirebaseRecyclerOptions.Builder<RewardsDetails>()
                .setQuery(mBase, RewardsDetails.class)
                .build();

        adapter = new RecycleFirebase_Rewards(options);

        rvTimeline.setAdapter(adapter);

        return v;
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
}