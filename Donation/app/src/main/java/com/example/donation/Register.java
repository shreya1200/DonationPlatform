package com.example.donation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST=1;
    Button registerbutton;
    TextView textforlogin;
    //for profile image upload
    Button uploadphoto;
    ImageView profileimg;
    Uri ImageUri;
    String imageUrl;
    Upload upload;
    FirebaseStorage storage;
    StorageReference storageRef;
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

        //for profile image
        profileimg = findViewById(R.id.profileimg);
        uploadphoto=findViewById(R.id.uploadphoto);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("profile_images/");

        uploadphoto.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   openFileChooser();
               }
        });

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
                uploadFile();
                System.out.println("oooooooooooooooooooooooooooooooooooooooooooo");
                if (password.getText().toString().equals(confirm.getText().toString())) {
                    if(true) {
                        fauth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                                    @SuppressLint("MissingPermission") Location location = loc.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                                    try{
                                        loc_lat = location.getLatitude();
                                        loc_long = location.getLongitude();
                                    } catch(Exception e) {
                                        FirebaseUser user = fauth.getCurrentUser();
                                        user.delete();
                                        System.out.println("Error in accessing location: " + e.getMessage());
                                        return;
                                    }
                                    System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                                    Map<String,Object> user = new HashMap<>();
                                    user.put("uid",fauth.getCurrentUser().getUid());
                                    user.put("name",name.getText().toString());
                                    user.put("email",email.getText().toString());
                                    user.put("number", number.getText().toString());
                                    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+upload.getImageUrl());
                                    user.put("profileimg",upload.getImageUrl());
                                    user.put("loc_lat",loc_lat);
                                    user.put("loc_long",loc_long);
                                    DocumentReference docref = fstore.collection("users").document(fauth.getCurrentUser().getUid());
                                    docref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            System.out.println("User added successfully");
                                            startActivity(new Intent(Register.this,profile.class));
                                        }
                                    });
                                } else {
                                    System.out.println("Login failed");
                                    System.out.println(task.getException().getMessage());
                                }
                            }
                        });
                    } else {
                        Toast.makeText(Register.this,"User already registered",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if(ImageUri!=null){
            StorageReference fileReferece = storageRef.child(System.currentTimeMillis()+"."+getFileExtension(ImageUri));
            fileReferece.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Register.this,"Upload Successful",Toast.LENGTH_LONG).show();
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();
                                    //createNewPost(imageUrl);
                                    upload = new Upload(imageUrl);
                                }
                            });
                        }
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Register.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!=null)
        {
            ImageUri=data.getData();
            Picasso.with(Register.this).load(ImageUri).placeholder(R.drawable.ic_name).into(profileimg);
//            productimg.setImageURI(ImageUri);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
}

//    DocumentReference docref = fstore.collection("users").document("user_details").collection("all_users").document(fauth.getCurrentUser().getUid());
