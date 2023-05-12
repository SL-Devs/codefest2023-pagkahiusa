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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.codefest_cdo.authentication.LogInActivity;
import com.example.codefest_cdo.data.CommentDetails;
import com.example.codefest_cdo.data.EFHDetails;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecycleFirebase_User extends FirebaseRecyclerAdapter<EFHDetails, RecycleFirebase_User.MyViewHolder> {
    FirebaseAuth mAuth;
    StorageReference storageReference;
    Context contextHere;
    ImageView ivProfile,ivPostImage;
    EditText etMessage;
    Button btnSend;
    TextView tvYes,tvNo;
    DatabaseReference mDatabase;
    RecyclerView rvTimeline;
    RecycleComments adapter;

    DatabaseReference mBase;

    HomeFragment homeFragment;
    String postID,profileImageLink,name,comment_id;

    public RecycleFirebase_User(@NonNull FirebaseRecyclerOptions<EFHDetails> options) {
        super(options);
    }

    @NonNull
    @Override
    public RecycleFirebase_User.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_pop, parent, false);

        return new MyViewHolder(view, view.getContext());
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull EFHDetails model) {

        holder.tvTitle.setText(model.getProblem_title());
        holder.tvDetails.setText(model.getProblem_details());
        holder.tvDate.setText(model.getDate());
        holder.tvVotes.setText(model.getVotes());
        holder.tvPostID.setText(model.getPost_id());
        holder.tvNumber.setText(model.getMobile_number());
        holder.tvName.setText(model.getFullname());
        postID = model.getPost_id();
        name = model.getFullname();
        urlRetrieveProfile(model.getImageLink());
        urlRetrievePostImage(model.getPostImageLink());


    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvNumber,tvTitle, tvDetails, tvVotes,tvDate,tvImageLink,tvPostImageLink,tvPostID,tvPoints;
        ImageView ivHeart;
        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            Animation anim = AnimationUtils.loadAnimation(context,R.anim.side_anim);
            itemView.startAnimation(anim);

            ivHeart = itemView.findViewById(R.id.ivHeart);
            tvName = itemView.findViewById(R.id.tvName);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDetails = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvVotes = itemView.findViewById(R.id.tvVotes);
            tvImageLink = itemView.findViewById(R.id.tvImageLink);
            tvPostID = itemView.findViewById(R.id.tvPostID);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvPostImageLink = itemView.findViewById(R.id.tvPostImageLink);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            tvPoints = itemView.findViewById(R.id.tvPoints);

            contextHere =  context;

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    tvName.setText("dadadad");
//                    Toast.makeText(context, "dadadada", Toast.LENGTH_SHORT).show();
//                }
//            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog builder = new Dialog(context);
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.setContentView(R.layout.send_comment);
                    builder.setCancelable(true);
                    builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    etMessage = builder.findViewById(R.id.etMessage);

                    btnSend = builder.findViewById(R.id.btnSend);
                    btnSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mAuth = FirebaseAuth.getInstance();
                            SimpleDateFormat date,time,date_id,time_id;
                            String currentDate,currentTime,currentDateID,currentTimeID;

                            date = new SimpleDateFormat("MM/dd/yy");
                            time = new SimpleDateFormat("hh:mm:ss a");

                            date_id = new SimpleDateFormat("MMddyy");
                            time_id = new SimpleDateFormat("hhmmss");

                            currentDate = date.format(new Date());
                            currentTime = time.format(new Date());
                            currentDateID = date_id.format(new Date());
                            currentTimeID = time_id.format(new Date());

                            comment_id = "BO_Comment" + currentDateID + currentTimeID;
                            if(etMessage.getText().toString().isEmpty()){
                                Toast.makeText(context, "Please put some message.", Toast.LENGTH_SHORT).show();
                            }else{
                                CommentDetails commentDetails = new CommentDetails(mAuth.getUid(),comment_id,tvTitle.getText().toString(),tvDetails.getText().toString(),name,postID,etMessage.getText().toString());
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Comments"+mAuth.getUid());
                                mDatabase.child(comment_id).setValue(commentDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Message sent!", Toast.LENGTH_SHORT).show();
                                        builder.dismiss();
                                    }
                                });

                            }
                        }
                    });
                    builder.show();

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