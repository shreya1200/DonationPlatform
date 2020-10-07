package com.example.donation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class categories extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ImageButton booksimg=findViewById(R.id.booksimg);
        ImageButton clothesimg=findViewById(R.id.clothesimg);
        ImageButton furnitureimg=findViewById(R.id.furnitureimg);
        ImageButton electronicsimg=findViewById(R.id.electronicsimg);
        ImageButton grainsimg=findViewById(R.id.grainsimg);
        ImageButton stationaryimg=findViewById(R.id.stationaryimg);
        ImageButton utensilsimg=findViewById(R.id.utensilsimg);
        ImageButton financeimg=findViewById(R.id.financeimg);
        booksimg.setOnClickListener(this);
        clothesimg.setOnClickListener(this);
        furnitureimg.setOnClickListener(this);
        electronicsimg.setOnClickListener(this);
        grainsimg.setOnClickListener(this);
        stationaryimg.setOnClickListener(this);
        utensilsimg.setOnClickListener(this);
        financeimg.setOnClickListener(this);

    }
    public void onClick(View v)
    {
        Intent intent=new Intent(categories.this,explore.class);
        intent.putExtra("ID",v.getId());
        startActivity(intent);
    }
}