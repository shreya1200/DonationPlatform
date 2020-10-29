package com.example.donation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class profile extends AppCompatActivity {
    Button proceedToDonatebtn;
    FirebaseAuth fauth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        proceedToDonatebtn=findViewById(R.id.proceedToDonate);
        proceedToDonatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CURRENT USER : "+fauth.getCurrentUser());
                Intent intent = new Intent(profile.this, UploadDonation.class);
                startActivity(intent);
            }
        });
    }
}