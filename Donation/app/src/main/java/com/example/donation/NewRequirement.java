package com.example.donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedHashMap;

public class NewRequirement extends AppCompatActivity {
    EditText title,locality,desc;Spinner city,category;Button submit;
    FirebaseAuth fauth;FirebaseFirestore fstore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_requirement);
        title = findViewById(R.id.title);
        locality = findViewById(R.id.loc);
        desc = findViewById(R.id.desc);
        city = findViewById(R.id.city);
        category = findViewById(R.id.categories);
        submit = findViewById(R.id.submit);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        System.out.println("Into the activity NewRequirement");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkedHashMap<String,Object> map = new LinkedHashMap<>();
                map.put("requestBy",fauth.getCurrentUser().getUid());
                map.put("title",title.getText().toString());
                map.put("category",category.getSelectedItem().toString());
                map.put("locality",locality.getText().toString());
                map.put("city",city.getSelectedItem().toString());
                map.put("description",desc.getText().toString());
                DocumentReference docref = fstore.collection("requests").document(fauth.getCurrentUser().getUid()+"_"+System.currentTimeMillis());
                docref.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Requirement posted");
                        Toast.makeText(NewRequirement.this,"Requirement posted",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Requirement posting failed \n--------------------------------\n"+e.getMessage());
                        Toast.makeText(NewRequirement.this,"Requirement posting failed",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}