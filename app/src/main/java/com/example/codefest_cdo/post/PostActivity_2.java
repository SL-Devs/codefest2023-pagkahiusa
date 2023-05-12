package com.example.codefest_cdo.post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codefest_cdo.HomeActivity;
import com.example.codefest_cdo.R;
import com.example.codefest_cdo.data.AccountDetails;
import com.example.codefest_cdo.data.EFHDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostActivity_2 extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int UPLOAD_REQUEST = 1999;
    String title;
    TextView tvTitle,tvExpound,tvFullname;
    Button btnUpload,btnCapture,btnSubmit;
    ImageView ivPostImage,ivBack;
    EditText etTitle,etExpound;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase,postReference;

    SimpleDateFormat date,time,date_id,time_id;
    String post_id,name,currentDate,currentTime,currentDateID,currentTimeID,mobile_number,points;
    byte[] postImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post2);

        mAuth = FirebaseAuth.getInstance();

        title = getIntent().getStringExtra("title");

        tvTitle = findViewById(R.id.tvTitle);
        tvExpound = findViewById(R.id.tvExpound);

        ivBack = findViewById(R.id.ivBack);

        tvFullname = findViewById(R.id.tvFullname);
        etTitle = findViewById(R.id.etTitle);
        etExpound = findViewById(R.id.etExpound);

        ivPostImage = findViewById(R.id.ivPostImage);
        btnCapture = findViewById(R.id.btnCapture);
        btnUpload = findViewById(R.id.btnUpload);
        btnSubmit = findViewById(R.id.btnSubmit);

        date = new SimpleDateFormat("MM/dd/yy");
        time = new SimpleDateFormat("hh:mm:ss a");

        date_id = new SimpleDateFormat("MMddyy");
        time_id = new SimpleDateFormat("hhmmss");

        currentDate = date.format(new Date());
        currentTime = time.format(new Date());
        currentDateID = date_id.format(new Date());
        currentTimeID = time_id.format(new Date());


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        postReference = FirebaseDatabase.getInstance().getReference();

        if(title.equalsIgnoreCase("Mental Health Issue")){
            tvTitle.setText("Mental Health Issue");
            tvExpound.setText("What kind of Health Issue are you experiencing?");
        }else if(title.equalsIgnoreCase("Health Problem")){
            tvTitle.setText("Health Problem");
            tvExpound.setText("What kind of Health Problem are you experiencing?");
        }else if(title.equalsIgnoreCase("Financial Problem")){
            tvTitle.setText("Financial Problem");
            tvExpound.setText("Why do you need some financial assistance?");
        }else if(title.equalsIgnoreCase("Others")){
            tvTitle.setText("Others");
            tvExpound.setText("Any type of concerns you would like to share?");
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT, null);
                galleryintent.setType("image/*");

//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//                Intent chooser = new Intent(Intent.ACTION_CHOOSER);
//                chooser.putExtra(Intent.EXTRA_INTENT, galleryintent);
//                chooser.putExtra(Intent.EXTRA_TITLE, "Select from:");
//
//                Intent[] intentArray = { cameraIntent };
//                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(galleryintent, UPLOAD_REQUEST);
            }
        });

        AccountDetails accountDetails = new AccountDetails();
        mDatabase.child(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    AccountDetails accountDetails = task.getResult().getValue(AccountDetails.class) ;
                    name = accountDetails.getFullname();
                    mobile_number = accountDetails.getMobile_number();
                    tvFullname.setText(name);
                    Toast.makeText(PostActivity_2.this, accountDetails.getFullname(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_id = "BO_Post" + currentDateID + currentTimeID;

                EFHDetails efhDetails = new EFHDetails(mAuth.getUid(),post_id,tvFullname.getText().toString() ,"Student",etTitle.getText().toString(),etExpound.getText().toString(),title,mobile_number,currentDate + " " + currentTime , "gs://be-one-dd8fd.appspot.com/UserProfiles/" + mAuth.getUid()+".jpg","gs://be-one-dd8fd.appspot.com/PostImages/" + post_id + ".jpg","0");
                postReference.child("EFH_Posts").child(post_id).setValue(efhDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        postReference.child("EFH_Posts_Specific").child(mAuth.getUid()).child(post_id).setValue(efhDetails);
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference storageReference = storageRef.child("PostImages/"+ post_id + ".jpg");
                        ivPostImage.setDrawingCacheEnabled(true);
                        ivPostImage.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) ivPostImage.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostActivity_2.this,"Something went wrong! Please try again later", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(PostActivity_2.this, HomeActivity.class);
                                startActivity(i);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

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
                                            points = accountDetails.getTotalPost();

                                        }
                                    }
                                });
                                int currentPost = Integer.parseInt(points);
                                currentPost++;

                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                                mDatabase.child(mAuth.getUid()).child("totalPost").setValue(String.valueOf(currentPost)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(PostActivity_2.this,"Success!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                Toast.makeText(PostActivity_2.this,"Success!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(PostActivity_2.this, HomeActivity.class);
                                startActivity(i);
                            }
                        });



                    }
                });
//                Toast.makeText(PostActivity_2.this, currentDate + currentTime, Toast.LENGTH_SHORT).show();


            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            ivPostImage.setImageBitmap(photo);
            btnSubmit.setVisibility(View.VISIBLE);
        }else if(requestCode == UPLOAD_REQUEST && resultCode == Activity.RESULT_OK){
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            ivPostImage.setImageURI(selectedImage);
            btnSubmit.setVisibility(View.VISIBLE);
        }
    }



}