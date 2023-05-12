package com.example.codefest_cdo.post;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codefest_cdo.R;
import com.example.codefest_cdo.RecycleFirebase;
import com.example.codefest_cdo.data.AccountDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class PostFragment extends Fragment {

    FirebaseAuth mAuth;
    RecyclerView rvTimeline;
    DatabaseReference mBase;
    RecycleFirebase adapter;
    DatabaseReference mDatabase;
    TextView tvName;

    LinearLayout llEFH,llETH;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_timeline, container, false);

        mAuth = FirebaseAuth.getInstance();

        llEFH = v.findViewById(R.id.llEFH);
        llETH = v.findViewById(R.id.llETH);
        tvName = v.findViewById(R.id.tvName);

        Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.fade_anim);
        tvName.startAnimation(anim);

        Animation animm = AnimationUtils.loadAnimation(getContext(),R.anim.left_anim);
        llEFH.startAnimation(animm);

        Animation animmm = AnimationUtils.loadAnimation(getContext(),R.anim.right_anim);
        llETH.startAnimation(animmm);

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

        llEFH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),PostActivity_1.class);
                startActivity(i);
            }
        });


        llETH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),PostActivity_ETH_1.class);
                startActivity(i);
            }
        });


//        mBase = FirebaseDatabase.getInstance().getReference().child("EFH_Posts");
//
//        rvTimeline = v.findViewById(R.id.rvTimeline);
//        rvTimeline.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        FirebaseRecyclerOptions<EFHDetails> options
//                = new FirebaseRecyclerOptions.Builder<EFHDetails>()
//                .setQuery(mBase, EFHDetails.class)
//                .build();
//
//        adapter = new RecycleFirebase(options);
//
//        rvTimeline.setAdapter(adapter);
        return v;

    }


}
