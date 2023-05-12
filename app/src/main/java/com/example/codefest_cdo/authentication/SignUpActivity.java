package com.example.codefest_cdo.authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.codefest_cdo.HomeActivity;
import com.example.codefest_cdo.R;
import com.example.codefest_cdo.data.AccountDetails;
import com.example.codefest_cdo.post.PostActivity_2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    private static final int CAMERA_REQUEST = 1888;
    private static final int UPLOAD_REQUEST = 1999;
    DatabaseReference mDatabase;
    EditText etFullname,etAddress,etNumber,etEmail,etPassword,etSpecific;
    ImageView ivProfile;
    Button btnSignUp;
    TextView tvLogIn,tvUpload;
    RadioGroup rgType;
    RadioButton rbArtist,rbStudent,rbResearcher,rbInnovator,rbOthers;
    String type;
    boolean hasUpload = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        etFullname = findViewById(R.id.etFullname);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etNumber = findViewById(R.id.etNumber);
        etPassword = findViewById(R.id.etPassword);
        etSpecific = findViewById(R.id.etSpecific);
        ivProfile = findViewById(R.id.ivProfile);
        tvLogIn = findViewById(R.id.tvLogIn);
        tvUpload = findViewById(R.id.tvUpload);
        btnSignUp = findViewById(R.id.btnSignUp);


        rgType = findViewById(R.id.rgType);

        rbArtist = findViewById(R.id.rbArtist);
        rbStudent = findViewById(R.id.rbStudent);
        rbInnovator = findViewById(R.id.rbInnovator);
        rbResearcher = findViewById(R.id.rbResearcher);
        rbOthers = findViewById(R.id.rbOthers);


        ivProfile.setImageResource(R.drawable.iv_profile);

        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(rbOthers.isChecked()){
                    etSpecific.setVisibility(View.VISIBLE);
                    if(etSpecific.getText().toString().isEmpty()){

                    }else{
                        type = etSpecific.getText().toString();
                    }
                }else{
                    etSpecific.setVisibility(View.GONE);
                }

                if(rbArtist.isChecked()){
                    type = "Artist";
                }

                if(rbStudent.isChecked()){
                    type = "Student";
                }

                if(rbInnovator.isChecked()){
                    type = "Innovator";
                }

                if(rbResearcher.isChecked()){
                    type = "Researcher";
                }

                if(rbResearcher.isChecked()){
                    type = "Researcher";
                }

            }
        });



        btnSignUp.setOnClickListener(this);
        tvLogIn.setOnClickListener(this);
        tvUpload.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvUpload:
                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT, null);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, UPLOAD_REQUEST);

                break;
            case R.id.btnSignUp:

                if(etFullname.getText().toString().isEmpty() || etAddress.getText()
                        .toString().isEmpty() || etNumber.getText().toString().isEmpty() || etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Please fill up all fields.", Toast.LENGTH_SHORT).show();
                }else{
                    if(etPassword.getText().toString().length() < 6){
                        Toast.makeText(SignUpActivity.this, "Please provide atleast 6 digit password.", Toast.LENGTH_SHORT).show();
                    }else if(etNumber.getText().toString().length() == 12) {
                        etNumber.getFocusable();
                        etNumber.setError("Number should be atleast 11 digits");
                        Toast.makeText(SignUpActivity.this, "Number should be atleast 11 digits", Toast.LENGTH_SHORT).show();
                    }else{
                        if(!(rbArtist.isChecked()) && !(rbStudent.isChecked()) && !(rbResearcher.isChecked()) && !(rbInnovator.isChecked()) && !(rbOthers.isChecked())){
                            Toast.makeText(SignUpActivity.this, "Please choose what type a person are you.", Toast.LENGTH_SHORT).show();
                        }else{
                            if(rbOthers.isChecked()){
                                if(etSpecific.getText().toString().isEmpty()){
                                    Toast.makeText(SignUpActivity.this, "Please specify what type of person are you.", Toast.LENGTH_SHORT).show();

                                }else{
                                    mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString())
                                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        AccountDetails accountDetails = new AccountDetails(mAuth.getUid(),etFullname.getText().toString(),type,etAddress.getText().toString(),etNumber.getText().toString(),etEmail.getText().toString(),"500","gs://be-one-dd8fd.appspot.com/UserProfiles/" + mAuth.getUid() + ".jpg","0");
                                                        mDatabase.child("Users").child(mAuth.getUid()).setValue(accountDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                uploadProfile();
                                                                Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                                                                startActivity(i);
                                                                finish();
                                                                Toast.makeText(SignUpActivity.this, "Successfully Logged In!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                });


                                }
                            }else{
                                mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString())
                                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful()){
                                                    AccountDetails accountDetails = new AccountDetails(mAuth.getUid(),etFullname.getText().toString(),type,etAddress.getText().toString(),etNumber.getText().toString(),etEmail.getText().toString(),"500","gs://be-one-dd8fd.appspot.com/UserProfiles/" + mAuth.getUid() + ".jpg","0");
                                                    mDatabase.child("Users").child(mAuth.getUid()).setValue(accountDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            uploadProfile();
                                                            Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                                                            startActivity(i);
                                                            finish();
                                                            Toast.makeText(SignUpActivity.this, "Successfully Logged In!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }else{
                                                    Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                                                }


                                            }
                                        });
                            }
                        }
                    }

                }


                break;
            case R.id.tvLogIn:

                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPLOAD_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://be-one-dd8fd.appspot.com/UserProfiles/" + mAuth.getUid() + ".jpg");
//            Glide.with(getContext()).load(storageReference).circleCrop().into(ivProfile);
            ivProfile.setImageURI(null);
            ivProfile.setImageURI(selectedImage);
            hasUpload = true;

        }
    }

    public void uploadProfile(){
        if(hasUpload == false){
            Drawable d = getResources().getDrawable(R.drawable.iv_profile_bwhite);
//            storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://be-one-dd8fd.appspot.com/UserProfiles/" + mAuth.getUid() + ".jpg");
//            Glide.with(getContext()).load(storageReference).circleCrop().into(ivProfile);
            ivProfile.setImageDrawable(d);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference storageReference = storageRef.child("UserProfiles/"+ mAuth.getUid() + ".jpg");
            ivProfile.setDrawingCacheEnabled(true);
            ivProfile.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) ivProfile.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = storageReference.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this,"Something went wrong! Please try again later", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(i);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(SignUpActivity.this,"Success!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(i);
                }
            });
        }else{
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference storageReference = storageRef.child("UserProfiles/"+ mAuth.getUid() + ".jpg");
            ivProfile.setDrawingCacheEnabled(true);
            ivProfile.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) ivProfile.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = storageReference.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this,"Something went wrong! Please try again later", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(i);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(SignUpActivity.this,"Success!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(i);
                }
            });
        }

    }
}