package com.example.donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class profile extends AppCompatActivity {
    Button proceedToDonatebtn,currDonationsbtn,prevDonationsBtn,donatedBtn,logoutBtn;
    TextView nametxt,emailtxt,contacttxt;
    private RecyclerView recyclerView;
    ImageView profileImg;
    FirebaseAuth fauth = FirebaseAuth.getInstance();
    Upload uploadCurrent;
    FirebaseFirestore fstore= FirebaseFirestore.getInstance();
    ImageAdapter mAdapter;
    private List<Upload> mUploads;
    private ProgressBar progressCircle;

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent=new Intent(profile.this,homepage.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


//        CardView cardView=findViewById(R.id.cardView);
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println();
//            }
//        });

        logoutBtn=findViewById(R.id.logoutBtn);
        donatedBtn=findViewById(R.id.donatedBtn);
        recyclerView=findViewById(R.id.DonationsLists);
//        layoutManager=new GridLayoutManager(explore.this,2);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
//        recyclerView.setLayoutManager(layoutManager);
        mUploads= new ArrayList<>();

        progressCircle=findViewById(R.id.progressBar2);


        currDonationsbtn=findViewById(R.id.currDonationsBtn);
        prevDonationsBtn=findViewById(R.id.prevDonationsBtn);

        nametxt=findViewById(R.id.nametxt);
        emailtxt=findViewById(R.id.emailtxt);
        contacttxt=findViewById(R.id.contacttxt);
        profileImg=findViewById(R.id.productImg);

        fstore.collection("users").document(fauth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    System.out.println(fauth.getCurrentUser().getUid());
                    DocumentSnapshot document = task.getResult();
                    Picasso.with(profile.this).load(document.get("profileImgUrl").toString()).into(profileImg);
                    nametxt.setText(document.get("name").toString());
                    emailtxt.setText(document.get("email").toString());
                    contacttxt.setText(document.get("phone").toString());

                }
            }
        });

        proceedToDonatebtn=findViewById(R.id.proceedToDonate);
        proceedToDonatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CURRENT USER : "+fauth.getCurrentUser());
                Intent intent = new Intent(profile.this, UploadDonation.class);
                startActivity(intent);
            }
        });
        getAllData(0);
        currDonationsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllData(0);

            }
        });

        prevDonationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllData(1);
            }
        });
    }

    private void getAllData(final int i) {
        mUploads.clear();
        Task<QuerySnapshot> donationUploads = fstore.collection("donations").whereEqualTo("uid",fauth.getCurrentUser().getUid()).whereEqualTo("donated",i).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
//                    fstore.collection("donations").whereEqualTo("donated",i).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    Map<String, Object> data = document.getData();
//                         Upload upload = (Upload) data.get(String.valueOf(Upload.class));
                                    Upload upload = new Upload(data);
                                    mUploads.add(upload);
                                }
                                mAdapter = new ImageAdapter(profile.this,mUploads);
                                recyclerView.setAdapter(mAdapter);
                                progressCircle.setVisibility(View.INVISIBLE);

//                            }
//                        }
//                    }) ;
                }
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fauth.signOut();
                Intent intent=new Intent(profile.this,homepage.class);
                startActivity(intent);
            }
        });
//            Task<QuerySnapshot> donationUploads = fstore.collection("donations").whereEqualTo("uid",fauth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if(task.isSuccessful()){
//                        for(QueryDocumentSnapshot document : task.getResult()){
//                            Map<String, Object> data = document.getData();
////                         Upload upload = (Upload) data.get(String.valueOf(Upload.class));
//                            Upload upload = new Upload(data);
//                            mUploads.add(upload);
//                        }
//                        mAdapter = new ImageAdapter(profile.this,mUploads);
//                        recyclerView.setAdapter(mAdapter);
//                        progressCircle.setVisibility(View.INVISIBLE);
//                    }
//                }
//            });
    }
}