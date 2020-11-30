package com.example.donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class ProductDetails extends AppCompatActivity {
    TextView prodName,prodCategory,prodDescription;
    TextView donorName,donorEmail,donorContact;
    ImageView prodImg;
    Upload upload;
    Button donatedBtn;
    FirebaseAuth fauth = FirebaseAuth.getInstance();
    FirebaseFirestore fstore= FirebaseFirestore.getInstance();
    String donated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        prodName=findViewById(R.id.prodName);
        prodCategory=findViewById(R.id.prodCategory);
        prodDescription=findViewById(R.id.prodDes);
        prodImg=findViewById(R.id.productImg);
        donatedBtn=findViewById(R.id.donatedBtn);

        donorName=findViewById(R.id.donorName);
        donorEmail=findViewById(R.id.donorEmail);
        donorContact=findViewById(R.id.donorContact);



        Intent intent=new Intent();
        upload = (Upload)getIntent().getSerializableExtra("upload");

        prodName.setText(upload.getName());
        prodCategory.setText(upload.getProdCategory());
        prodDescription.setText(upload.getProdDescription());
        Picasso.with(ProductDetails.this).load(upload.getImageUrl()).into(prodImg);
        donated=upload.getDonated();
        System.out.println("xxxxxxxxxxxxxxxxxxxxx"+donated);



        if(fauth.getCurrentUser()==null){
            donatedBtn.setVisibility(View.GONE);
        }

        if(donated.equals("1")){
            donatedBtn.setVisibility(View.GONE);
        }else {
            donatedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fstore.collection("donations").document(upload.getDonation_id()).update("donated", 1);
                    donatedBtn.setVisibility(View.GONE);
                    Toast.makeText(ProductDetails.this, "Successfully Donated!", Toast.LENGTH_LONG).show();
                }
            });
        }

        System.out.println("ttttttttttttttttt"+upload.getUid());

        fstore.collection("users").document(upload.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document=task.getResult();
                    donorName.setText(document.get("name").toString());
                    donorEmail.setText(document.get("email").toString());
                    donorContact.setText(document.get("phone").toString());
                }

            }
        });

    }
}