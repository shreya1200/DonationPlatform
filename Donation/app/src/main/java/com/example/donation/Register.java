package com.example.donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {
    Button registerbutton;
    TextView textforlogin;
    FirebaseAuth fauth;
    EditText email, password, confirm, name, number;
    FirebaseFirestore fstore;
    boolean exists;
    LocationManager loc;
    double loc_lat, loc_long; Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this.context;
        exists = false;
        fauth = FirebaseAuth.getInstance();
        textforlogin = findViewById(R.id.textforlogin);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirmpassword);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        fstore = FirebaseFirestore.getInstance();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        loc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        textforlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        registerbutton = findViewById(R.id.registerbutton);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(confirm.getText().toString())) {
                    if(true) {
                        fauth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    @SuppressLint("MissingPermission") Location location = loc.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    if(location != null){
                                        loc_lat = location.getLatitude();
                                        loc_long = location.getLongitude();
                                    } else {
                                        System.out.println("Error in accessing location.");
                                        return;
                                    }
                                    Map<String,Object> user = new HashMap<>();
                                    user.put("name",name.getText().toString());
                                    user.put("email",email.getText().toString());
                                    user.put("number", number.getText().toString());
                                    user.put("loc_lat",loc_lat);
                                    user.put("loc_long",loc_long);
                                    DocumentReference docref = fstore.collection("users").document(fauth.getCurrentUser().getUid());
                                    docref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            System.out.println("User added successfully");
                                            startActivity(new Intent(Register.this,homepage.class));
                                        }
                                    });
                                } else {
                                    System.out.println("Login failed");
                                    System.out.println(task.getException());
                                }
                            }
                        });
                    } else {
                        Toast.makeText(Register.this,"User already registered",Toast.LENGTH_LONG);
                    }
                }
            }
        });
    }
}