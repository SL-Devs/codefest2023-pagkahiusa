package com.example.codefest_cdo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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
import com.example.codefest_cdo.data.AccountDetails;
import com.example.codefest_cdo.data.CommentDetails;
import com.example.codefest_cdo.data.EFHDetails;
import com.example.codefest_cdo.home.HomeFragment;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecycleComments extends FirebaseRecyclerAdapter<CommentDetails, RecycleComments.MyViewHolder> {
    FirebaseAuth mAuth;
    StorageReference storageReference;
    Context contextHere;
    ImageView ivProfile,ivPostImage;
    EditText etComment;
    Button btnSend;
    TextView tvYes,tvNo;
    DatabaseReference mDatabase;
    RecyclerView rvTimeline;
    RecycleComments adapter;
    int otherSender;

    DatabaseReference mBase;

    HomeFragment homeFragment;
    String postID,user_id,name,comment_id,problem_title,problem_details,message,points,otherUserPoints;

    public RecycleComments(@NonNull FirebaseRecyclerOptions<CommentDetails> options) {
        super(options);
    }

    @NonNull
    @Override
    public RecycleComments.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card, parent, false);
        return new MyViewHolder(view, view.getContext());
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull CommentDetails model) {

        holder.tvUserID.setText(model.getUser_id());
        holder.tvName.setText(model.getName());
        holder.tvTitle.setText(model.getProblem_title());
        holder.tvDescription.setText(model.getProblem_details());
        holder.tvMessage.setText(model.getMessage());



        postID = model.getPost_id();
        name = model.getUser_id();
        user_id = model.getUser_id();
        problem_title = model.getProblem_title();
        problem_details =model.getUser_id();
        comment_id = model.getComment_id();
        message = model.getMessage();
        points = model.getPoints();


    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvUserID,tvTitle,tvDescription,tvMessage;
        EditText etAmmount,etID;
        Button btnSend;
        ImageView ivHeart;
        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            Animation anim = AnimationUtils.loadAnimation(context,R.anim.side_anim);
            itemView.startAnimation(anim);

            tvUserID = itemView.findViewById(R.id.tvUserID);
            tvName = itemView.findViewById(R.id.tvName);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvMessage = itemView.findViewById(R.id.tvMessage);
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

                    Dialog builder = new Dialog(context);
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.setContentView(R.layout.send_pop);
                    builder.setCancelable(true);
                    builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    etID = builder.findViewById(R.id.etID);
                    etAmmount = builder.findViewById(R.id.etAmmount);
                    btnSend = builder.findViewById(R.id.btnSend);

                    btnSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(etID.getText().toString().isEmpty()){
                                etID.getFocusable();
                                etID.setError("Please input the user id.");
                            }else if(etAmmount.getText().toString().isEmpty()) {
                                etAmmount.getFocusable();
                                etAmmount.setError("Please add some amount first.");
                            }else{
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                                AccountDetails accountDetails = new AccountDetails();
                                mDatabase.child(etID.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (!task.isSuccessful()) {
                                            Log.e("firebase", "Error getting data", task.getException());
                                        }
                                        else {
                                            AccountDetails accountDetails = task.getResult().getValue(AccountDetails.class);
                                            otherUserPoints = accountDetails.getPoints();

                                        }
                                    }
                                });


                                otherSender = Integer.parseInt(otherUserPoints);

                                int inputAmmount = Integer.parseInt(etAmmount.getText().toString());
                                int currentBalance = Integer.parseInt(points);

                                if(currentBalance < inputAmmount){
                                    Toast.makeText(context, "Insufficient Balance", Toast.LENGTH_SHORT).show();

                                }else{
                                    int ammount = currentBalance - inputAmmount;

                                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                                    mDatabase.child(mAuth.getUid()).child("points").setValue(String.valueOf(ammount)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            otherSender = otherSender + inputAmmount;
                                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                                            mDatabase.child(etID.getText().toString()).child("points").setValue(String.valueOf(otherSender)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
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
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
//                    AccountDetails accountDetails = new AccountDetails();
//                    mDatabase.child(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DataSnapshot> task) {
//                            if (!task.isSuccessful()) {
//                                Log.e("firebase", "Error getting data", task.getException());
//                            }
//                            else {
//                                AccountDetails accountDetails = task.getResult().getValue(AccountDetails.class);
//                                points = accountDetails.getPoints();
//                            }
//                        }
//                    });
//
//                    Dialog builder = new Dialog(context);
//                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    builder.setContentView(R.layout.send_pop);
//                    builder.setCancelable(true);
//                    builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    etAmmount = builder.findViewById(R.id.etAmmount);
//                    btnSend = builder.findViewById(R.id.btnSend);
//
//                    btnSend.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if(etAmmount.getText().toString().isEmpty()){
//                                etAmmount.getFocusable();
//                                etAmmount.setError("Please add some ammount first.");
//                            }else{
//                                int inputAmmount = Integer.parseInt(etAmmount.getText().toString());
//                                int currentBalance = Integer.parseInt(points);
//
//                                if(currentBalance > inputAmmount){
//                                    Toast.makeText(context, "Insufficient Balance", Toast.LENGTH_SHORT).show();
//
//                                }else{
//                                    int ammount = currentBalance - inputAmmount;
//                                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
//                                    mDatabase.child(mAuth.getUid()).child("points").setValue(String.valueOf(ammount));
//
//                                }
//
//
////                                CommentDetails commentDetails = new CommentDetails(mAuth.getUid(),comment_id,tvTitle.getText().toString(),tvDetails.getText().toString(),name,postID,etMessage.getText().toString());
////                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Comments"+mAuth.getUid());
////                                mDatabase.child(comment_id).setValue(commentDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
////                                    @Override
////                                    public void onComplete(@NonNull Task<Void> task) {
////                                        Toast.makeText(context, "Message sent!", Toast.LENGTH_SHORT).show();
////                                        builder.dismiss();
////                                    }
////                                });
//                            }
//                        }
//                    });
//
//
//
////                    tvUserID = builder.findViewById(R.id.tvUserID);
////                    tvName = builder.findViewById(R.id.tvName);
////                    tvTitle = builder.findViewById(R.id.tvTitle);
////                    tvDescription = builder.findViewById(R.id.tvDescription);
////                    tvMessage = builder.findViewById(R.id.tvMessage);
////
////                    tvUserID.setText(user_id);
////                    tvName.setText(name);
////                    tvTitle.setText(problem_title);
////                    tvDescription.setText(problem_details);
////                    tvMessage.setText(message);
//
//
//
//                    builder.show();
//
//                }
//            });

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
