package com.example.donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class certificate extends AppCompatActivity {
    TextView donor_name;
    FirebaseAuth fauth = FirebaseAuth.getInstance();
    FirebaseFirestore fstore= FirebaseFirestore.getInstance();
    Button BackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);
        donor_name=findViewById(R.id.donor_name);
        BackBtn=findViewById(R.id.BackBtn);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(certificate.this,profile.class);
                startActivity(intent);
            }
        });




        fstore.collection("users").document(fauth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    System.out.println(fauth.getCurrentUser().getUid());
                    DocumentSnapshot document = task.getResult();
                    donor_name.setText(document.get("name").toString());

                }
            }
        });



    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent=new Intent(certificate.this,profile.class);
        startActivity(intent);
    }


}