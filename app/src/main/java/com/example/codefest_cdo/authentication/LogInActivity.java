package com.example.codefest_cdo.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codefest_cdo.HomeActivity;
import com.example.codefest_cdo.R;
import com.example.codefest_cdo.admin.AdminHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText etEmail,etPassword;
    Button btnLogIn;
    TextView tvSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();


        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        btnLogIn = findViewById(R.id.btnLogIn);

        btnLogIn.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogIn:

                if(etEmail.getText().toString().isEmpty()){
                    etEmail.getFocusable();
                    etEmail.setError("Please fill up your email.");
                }else if(etPassword.getText().toString().isEmpty()){
                    etPassword.getFocusable();
                    etPassword.setError("Please fill up your password.");
                }else{
                    if(etEmail.getText().toString().equalsIgnoreCase("admin") && etPassword.getText().toString().equalsIgnoreCase("admin")){
                        Intent i = new Intent(LogInActivity.this, AdminHome.class);
                        startActivity(i);
                        finish();
                    }else{
                        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent i = new Intent(LogInActivity.this, HomeActivity.class);
                                    startActivity(i);
                                    finish();
                                    Toast.makeText(LogInActivity.this, "Successfully Logged In!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(LogInActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }


                }

                break;
            case R.id.tvSignUp:
                Intent i = new Intent(LogInActivity.this,SignUpActivity.class);
                startActivity(i);
                break;
        }
    }
}