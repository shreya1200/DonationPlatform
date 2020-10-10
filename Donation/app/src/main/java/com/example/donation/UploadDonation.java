package com.example.donation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class UploadDonation extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST=1;

    ImageView productimg;
    Button uploadimg;
    EditText productName;
    EditText productDescription;
    Button donate;

    Uri ImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_donation);

        productimg=findViewById(R.id.productimg);
        uploadimg=findViewById(R.id.uploadimg);
        productName=findViewById(R.id.productName);
        productDescription=findViewById(R.id.productDescription);
        donate=findViewById(R.id.donate);

        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });


        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!=null)
        {
            ImageUri=data.getData();

            Picasso.with(this).load(ImageUri).into(productimg);
        }
    }
}