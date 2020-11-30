package com.example.donation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class UploadDonation extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST=1;

    ImageView productimg;
    Button uploadimg;
    EditText productName;
    Spinner categories;
    EditText productDescription;
    Button donate;
    FirebaseAuth fauth = FirebaseAuth.getInstance();
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    String user_name="";
    String imageUrl;
    Upload upload;

    Uri ImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_donation);
        getCurrentUserName();

        productimg=findViewById(R.id.productimg);
        uploadimg=findViewById(R.id.uploadimg);
        productName=findViewById(R.id.productName);
        categories=findViewById(R.id.categories);
        productDescription=findViewById(R.id.productDescription);
        donate=findViewById(R.id.donate);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("images/");


        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                uploadImage();
                uploadFile();
                System.out.println("CURRENT USER : "+fauth.getCurrentUser());
                System.out.println("CURRENT USER : "+ Objects.requireNonNull(fauth.getCurrentUser()).getUid());
                System.out.println("CURRENT USER : "+fauth.getCurrentUser().getEmail());




            }
        });

    }

    private void getCurrentUserName() {
        fstore.collection("users").document(fauth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    user_name = document.get("name").toString();
                }
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!=null)
        {
            ImageUri=data.getData();
            Picasso.with(UploadDonation.this).load(ImageUri).into(productimg);
//            productimg.setImageURI(ImageUri);
        }
    }

    //to get file extension for image
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if(ImageUri!=null){
            StorageReference fileReferece = storageRef.child(System.currentTimeMillis()+"."+getFileExtension(ImageUri));
            fileReferece.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UploadDonation.this,"Upload Successful",Toast.LENGTH_LONG).show();
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();
                                    //createNewPost(imageUrl);
                                    upload = new Upload(productName.getText().toString().trim(), imageUrl);
//
//                    upload = new Upload(productName.getText().toString().trim(), imageUrl);
//                    Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl().toString()
                                    if(fauth.getCurrentUser() != null) {
                                        String donation_id=fauth.getCurrentUser().getUid()+System.currentTimeMillis();
//                    uploadImage();
                                        Map<String, Object> donation = new HashMap<>();
                                        donation.put("uid", fauth.getCurrentUser().getUid());
//                        donation.put("email", fauth.getCurrentUser().getEmail());
                                        donation.put("name",user_name);
                                        donation.put("categories", categories.getSelectedItem().toString());
                                        donation.put("product_description", productDescription.getText().toString());
                                        donation.put("imageurl",upload.getImageUrl());
                                        donation.put("product_name",upload.getName());
                                        donation.put("donated",0);
                                        donation.put("donation_id",donation_id);
                                        DocumentReference donationtbl = fstore.collection("donations").document(donation_id);
                                        donationtbl.set(donation).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                System.out.println("Data added successfully");
                                                Intent intent=new Intent(UploadDonation.this,explore.class);
                                                intent.putExtra("Category",categories.getSelectedItem().toString());
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                }
                            });
                        }
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadDonation.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                }
            });
        }else{
            Toast.makeText(UploadDonation.this,"No file selected",Toast.LENGTH_SHORT).show();
        }
    }

//    // UploadImage method
//    private void uploadImage()
//    {
//        if (ImageUri != null) {
//
//            // Code for showing progressDialog while uploading
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
//
//            // Defining the child of storageReference
//            StorageReference ref = storageRef.child("images/"+ UUID.randomUUID().toString());
//            // adding listeners on upload
//            // or failure of image
//            ref.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//
//                                @Override
//                                public void onSuccess(
//                                        UploadTask.TaskSnapshot taskSnapshot)
//                                {
//
//                                    // Image uploaded successfully
//                                    // Dismiss dialog
//                                    progressDialog.dismiss();
//                                    Toast.makeText(UploadDonation.this,"Image Uploaded!!",Toast.LENGTH_SHORT).show();
//                                }
//                            })
//
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e)
//                        {
//
//                            // Error, Image not uploaded
//                            progressDialog.dismiss();
//                            Toast.makeText(UploadDonation.this,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnProgressListener(
//                            new OnProgressListener<UploadTask.TaskSnapshot>() {
//
//                                // Progress Listener for loading
//                                // percentage on the dialog box
//                                @Override
//                                public void onProgress(
//                                        UploadTask.TaskSnapshot taskSnapshot)
//                                {
//                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                                    progressDialog.setMessage("Uploaded "+ (int)progress + "%");
//                                }
//                            });
//        }
//    }
}



//    DocumentReference donationtbl = fstore.collection("users").document("donations").collection("all_donations").document(fauth.getCurrentUser().getUid());