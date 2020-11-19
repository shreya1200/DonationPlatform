package com.example.donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedHashMap;

public class NewRequirement extends AppCompatActivity {
    EditText title,locality,desc,name,number;Spinner city,category;Button submit;
    FirebaseFirestore fstore;
    TextView success,failed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_requirement);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        title = findViewById(R.id.title);
        locality = findViewById(R.id.loc);
        desc = findViewById(R.id.desc);
        city = findViewById(R.id.city);
        category = findViewById(R.id.categories);
        submit = findViewById(R.id.submit);
        success = findViewById(R.id.success);
        failed = findViewById(R.id.failed);
        fstore = FirebaseFirestore.getInstance();
        System.out.println("Into the activity NewRequirement");
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    success.setVisibility(View.INVISIBLE);
                    failed.setVisibility(View.INVISIBLE);
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equals("") || number.getText().toString().equals("") || title.getText().toString().equals("") || locality.getText().toString().equals("") || desc.getText().toString().equals("")){
                    if(name.getText().toString().equals("")){
                        name.setError("Name is required");
                    }
                    if(number.getText().toString().equals("")){
                        number.setError("Contact number is required");
                    }
                    if(title.getText().toString().equals("")){
                        title.setError("Title is required");
                    }
                    if(locality.getText().toString().equals("")){
                        locality.setError("Locality is required");
                    }
                    if(desc.getText().toString().equals("")){
                        desc.setError("Too short description");
                    }
                }else{
                    LinkedHashMap<String,Object> map = new LinkedHashMap<>();
                    String id = category.getSelectedItemPosition()+"_"+title.getText().toString()+"_"+System.currentTimeMillis();
                    map.put("id",id);
                    map.put("name",name.getText().toString());
                    map.put("number",number.getText().toString());
                    map.put("title",title.getText().toString());
                    map.put("category",category.getSelectedItem().toString());
                    map.put("locality",locality.getText().toString());
                    map.put("city",city.getSelectedItem().toString());
                    map.put("description",desc.getText().toString());
                    DocumentReference docref = fstore.collection("requests").document(id);
                    docref.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            System.out.println("Requirement posted");
                            success.setVisibility(View.VISIBLE);
                            name.getText().clear();
                            number.getText().clear();
                            title.getText().clear();
                            locality.getText().clear();
                            desc.getText().clear();
                            category.setSelection(0);
                            city.setSelection(0);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Requirement posting failed \n--------------------------------\n"+e.getMessage());
                            failed.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
    }
}