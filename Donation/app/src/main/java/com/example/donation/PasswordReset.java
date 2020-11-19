package com.example.donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordReset extends AppCompatActivity {
    Button proceed; TextView success,failed;
    FirebaseAuth fauth;
    ProgressBar progress; EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        proceed = findViewById(R.id.proceed);
        success = findViewById(R.id.success);
        failed = findViewById(R.id.failed);
        progress = findViewById(R.id.loading);
        email = findViewById(R.id.email);
        fauth = FirebaseAuth.getInstance();
        progress.setVisibility(View.INVISIBLE);
        success.setVisibility(View.INVISIBLE);
        failed.setVisibility(View.INVISIBLE);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                String mail = email.getText().toString();
                fauth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progress.setVisibility(View.INVISIBLE);
                        if(task.isSuccessful()){
                            success.setVisibility(View.VISIBLE);
                        }else{
                            failed.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }
}