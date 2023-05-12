package com.example.codefest_cdo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.codefest_cdo.data.CommentDetails;
import com.example.codefest_cdo.data.EFHDetails;

import com.example.codefest_cdo.home.CommentSection;
import com.example.codefest_cdo.home.HomeFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RecycleFirebase extends FirebaseRecyclerAdapter<EFHDetails, RecycleFirebase.MyViewHolder> {
    FirebaseAuth mAuth;
    StorageReference storageReference;
    Context contextHere;
    ImageView ivProfile,ivPostImage;
    TextView tvYes,tvNo;
    DatabaseReference mDatabase;
    HomeFragment homeFragment;
    String postID,userID;

    RecyclerView rvTimeline;
    RecycleComments adapter;

    DatabaseReference mBase;

    public RecycleFirebase(@NonNull FirebaseRecyclerOptions<EFHDetails> options) {
        super(options);
    }

    @NonNull
    @Override
    public RecycleFirebase.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_pop, parent, false);

        return new MyViewHolder(view, view.getContext());
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull EFHDetails model) {


        holder.tvName.setText(model.getFullname());
        holder.tvTitle.setText(model.getProblem_title());
        holder.tvDetails.setText(model.getProblem_details());
        holder.tvDate.setText(model.getDate());
        holder.tvVotes.setText(model.getVotes());
        holder.tvPostID.setText(model.getPost_id());
        holder.tvNumber.setText(model.getMobile_number());
        postID = model.getPost_id();
        userID = model.getUser_id();
        holder.tvUserID.setText(model.getUser_id());
        urlRetrieveProfile(model.getImageLink());
        urlRetrievePostImage(model.getPostImageLink());


    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserID,tvNumber,tvName,tvTitle, tvDetails, tvVotes,tvDate,tvImageLink,tvPostImageLink,tvPostID;
        ImageView ivHeart;
        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            Animation anim = AnimationUtils.loadAnimation(context,R.anim.side_anim);
            itemView.startAnimation(anim);



            ivHeart = itemView.findViewById(R.id.ivHeart);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvName = itemView.findViewById(R.id.tvName);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDetails = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvVotes = itemView.findViewById(R.id.tvVotes);
            tvImageLink = itemView.findViewById(R.id.tvImageLink);
            tvUserID = itemView.findViewById(R.id.tvUserID);
            tvPostID = itemView.findViewById(R.id.tvPostID);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvPostImageLink = itemView.findViewById(R.id.tvPostImageLink);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);

            contextHere =  context;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Dialog builder = new Dialog(context);
//                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    builder.setContentView(R.layout.view_comments);
//                    builder.setCancelable(true);
//                    builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    rvTimeline = builder.findViewById(R.id.rvTimeline);
//                    rvTimeline.setLayoutManager(new LinearLayoutManager(homeFragment.getActivity()));
//                    mBase = FirebaseDatabase.getInstance().getReference().child(postID);
//                    FirebaseRecyclerOptions<CommentDetails> options
//                            = new FirebaseRecyclerOptions.Builder<CommentDetails>()
//                            .setQuery(mBase, CommentDetails.class)
//                            .build();
//
//                    adapter = new RecycleComments(options);
//
//                    rvTimeline.setAdapter(adapter);
//                    adapter.startListening();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mAuth = FirebaseAuth.getInstance();

                    Dialog builder = new Dialog(context);
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.setContentView(R.layout.delete_pop);
                    builder.setCancelable(true);
                    builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    tvYes = builder.findViewById(R.id.tvYes);
                    tvNo = builder.findViewById(R.id.tvNo);
                    tvYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("EFH_Posts").child(postID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mDatabase.child("EFH_Posts_Specific").child(userID).child(postID).removeValue();
                                    Toast.makeText(context,"Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    builder.dismiss();
                                }
                            });

                            mDatabase.child("ETH_Posts").child(postID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mDatabase.child("ETH_Posts_Specific").child(userID).child(postID).removeValue();
                                    Toast.makeText(context,"Deleted Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });

                    tvNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            builder.dismiss();
                        }
                    });
                    builder.show();


                    return false;
                }
            });
        }
    }
    public void urlRetrieveProfile(String imageUrl){
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
        Glide.with(contextHere).load(storageReference).circleCrop().into(ivProfile);
    }
    public void urlRetrievePostImage(String postImageUrl){
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(postImageUrl);
        Glide.with(contextHere).load(storageReference).circleCrop().into(ivPostImage);


    }
}
