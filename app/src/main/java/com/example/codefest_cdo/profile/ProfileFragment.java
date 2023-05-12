package com.example.codefest_cdo.profile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codefest_cdo.R;
import com.example.codefest_cdo.authentication.LogInActivity;
import com.example.codefest_cdo.data.AccountDetails;
import com.example.codefest_cdo.post.PostActivity_2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;


public class ProfileFragment extends Fragment {

    Button btnShowPoints,btnLogOut;
    FirebaseAuth mAuth;
    TextView tvYes,tvNo;
    ImageView ivClose;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        btnShowPoints = v.findViewById(R.id.btnShowPoints);
        btnShowPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog builder = new Dialog(getContext());
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.setContentView(R.layout.show_points_pop);
                builder.setCancelable(true);
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        btnLogOut = v.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog builder = new Dialog(getContext());
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
                        Intent i = new Intent(getContext(), LogInActivity.class);
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
        return v;
    }

}