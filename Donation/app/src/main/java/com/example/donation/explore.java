package com.example.donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class explore extends AppCompatActivity{

    ImageView exploreimg;
    String category;
    private RecyclerView recyclerView;
    ImageAdapter mAdapter;
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    private List<Upload> mUploads;
    RecyclerView.LayoutManager layoutManager;

    private ProgressBar progressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        recyclerView=findViewById(R.id.exploreItems);
//        layoutManager=new GridLayoutManager(explore.this,2);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
//        recyclerView.setLayoutManager(layoutManager);
        mUploads= new ArrayList<>();



        progressCircle=findViewById(R.id.progress_circle);

        exploreimg=(ImageView)findViewById(R.id.exploreimg);
        Intent intent=getIntent();
        category = intent.getStringExtra("Category");
        if (category != null) {
            switch(category)
            {
                case "Books":
                    exploreimg.setImageResource(R.drawable.bookscat);
                    getAllData("Books");
                    break;
                case "Clothes":
                    exploreimg.setImageResource(R.drawable.clothescat);
                    getAllData("Clothes");
                    break;
                case "Furniture":
                    exploreimg.setImageResource(R.drawable.furniturecat);
                    getAllData("Furniture");
                    break;
                case "Electronics":
                    exploreimg.setImageResource(R.drawable.electronicscat);
                    getAllData("Electronics");
                    break;
                case "Grains and Pulses":
                    exploreimg.setImageResource(R.drawable.grainscat);
                    getAllData("Grains and Pulses");
                    break;
                case "Stationary":
                    exploreimg.setImageResource(R.drawable.stationarycat);
                    getAllData("Stationary");
                    break;
                case "Utensils":
                    exploreimg.setImageResource(R.drawable.utensilscat1);
                    getAllData("Utensils");
                    break;
                case "Financial Aid":
                    exploreimg.setImageResource(R.drawable.finanacialaidcat);
                    getAllData("Financial Aid");
                    break;
            }
        }
    }
     public void getAllData(final String cat)
     {
         Task<QuerySnapshot> donationUploads = fstore.collection("donations").whereEqualTo("categories",cat).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
             @Override
             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                 if(task.isSuccessful()){
                     for(QueryDocumentSnapshot document : task.getResult()){
                         Map<String, Object> data = document.getData();
//                         Upload upload = (Upload) data.get(String.valueOf(Upload.class));
                         Upload upload = new Upload(data);
                         mUploads.add(upload);

                     }
                     mAdapter = new ImageAdapter(explore.this,mUploads);
                     recyclerView.setAdapter(mAdapter);
                     progressCircle.setVisibility(View.INVISIBLE);
                 }
             }
         });


     }
}