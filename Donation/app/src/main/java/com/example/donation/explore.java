package com.example.donation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.Serializable;

public class explore extends AppCompatActivity{

    ImageView exploreimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        exploreimg=(ImageView)findViewById(R.id.exploreimg);
        Intent intent=getIntent();
        Serializable id=intent.getSerializableExtra("ID");
        switch((int)id)
        {
            case R.id.booksimg:
                exploreimg.setImageResource(R.drawable.bookscat);
                break;
            case R.id.clothesimg:
                exploreimg.setImageResource(R.drawable.clothescat);
                break;
            case R.id.furnitureimg:
                exploreimg.setImageResource(R.drawable.furniturecat);
                break;
            case R.id.electronicsimg:
                exploreimg.setImageResource(R.drawable.electronicscat);
                break;
            case R.id.grainsimg:
                exploreimg.setImageResource(R.drawable.grainscat);
                break;
            case R.id.stationaryimg:
                exploreimg.setImageResource(R.drawable.stationarycat);
                break;
            case R.id.utensilsimg:
                exploreimg.setImageResource(R.drawable.utensilscat1);
                break;
            case R.id.financeimg:
                exploreimg.setImageResource(R.drawable.finanacialaidcat);
                break;
        }


    }
}