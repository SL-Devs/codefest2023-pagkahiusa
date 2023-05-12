package com.example.codefest_cdo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.codefest_cdo.data.AccountDetails;
import com.example.codefest_cdo.data.RewardsDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.logging.Handler;

public class RecycleFirebase_Rewards extends FirebaseRecyclerAdapter<RewardsDetails, RecycleFirebase_Rewards.MyViewHolderRewards> {

    String url;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    Context contextHere;
    DatabaseReference mDatabase;
    ImageView ivImage;
    String points,value,quantity;
    public RecycleFirebase_Rewards(@NonNull FirebaseRecyclerOptions<RewardsDetails> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolderRewards holder, int position, @NonNull RewardsDetails model) {
        holder.tvName.setText(model.getName());
        holder.tvDetails.setText(model.getDetails());
        holder.tvQuantity.setText(model.getQuantity());
        holder.tvValue.setText(model.getValue());
        holder.tvImageLink.setText(model.getImageLink());
        value = model.getValue();
        quantity = model.getQuantity();
        urlRetrieve(model.getImageLink());
    }

    @NonNull
    @Override
    public RecycleFirebase_Rewards.MyViewHolderRewards onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_card, parent, false);
        return new MyViewHolderRewards(view, view.getContext());
    }



    class MyViewHolderRewards extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails, tvValue,tvQuantity,tvImageLink,tvPoints;


        Handler handler;
        public MyViewHolderRewards(@NonNull View itemView, Context context) {
            super(itemView);

            Animation anim = AnimationUtils.loadAnimation(context,R.anim.short_fade);
            itemView.startAnimation(anim);

            tvName = itemView.findViewById(R.id.tvName);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            tvValue = itemView.findViewById(R.id.tvValue);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvImageLink = itemView.findViewById(R.id.tvImageLink);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvPoints = itemView.findViewById(R.id.tvPoints);


            contextHere =  context;

            mAuth = FirebaseAuth.getInstance();
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
                        tvPoints.setText(points);


                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPoints = Integer.parseInt(tvPoints.getText().toString());
                    int valueNumber = Integer.parseInt(value);
                    if(currentPoints < valueNumber){
                        Toast.makeText(context, "You can't buy it!", Toast.LENGTH_SHORT).show();
                    }else{
                        int total = currentPoints - valueNumber;

                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                        mDatabase.child(mAuth.getUid()).child("points").setValue(String.valueOf(total)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Your code is BO_" + mAuth.getUid(), Toast.LENGTH_LONG).show();
                                itemView.setVisibility(View.GONE);
                            }
                        });

                    }



                    int result = 100 - Integer.parseInt(tvValue.getText().toString());
                    if(result < 0 ){
                        Toast.makeText(context, "You can't buy it!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Code: BO_1234", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
    public void urlRetrieve(String imageUrl){
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
        Glide.with(contextHere).load(storageReference).into(ivImage);
    }

}
