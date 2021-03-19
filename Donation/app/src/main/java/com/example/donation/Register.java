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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.LinkedHashMap;
import java.util.Objects;


public class Register extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST=1;
    Button registerbutton;
    TextView textforlogin;
    //for profile image upload
    ImageButton uploadphoto;
    ImageView profileimg1;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseAuth fauth;
    EditText email, password, confirm, name, number;
    FirebaseFirestore fstore;
    boolean exists;
    LocationManager loc;
    double loc_lat, loc_long; Context context;

    @Override
    protected void onActivityResult(int requestCode,int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            imageUri = data.getData();
                System.out.println("lllllllllllllllllllll" + imageUri.toString());
                Picasso.with(Register.this).load(imageUri).into(profileimg1);
            }
//            profileimg1.setImageURI(data.getData());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this.context;
        exists = false;

        fauth = FirebaseAuth.getInstance();
        textforlogin = findViewById(R.id.textforlogin);
        email = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);

        //for profile image
        profileimg1 = findViewById(R.id.profileimg);
        uploadphoto=findViewById(R.id.uploadphoto);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("profile_images/");
        loc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        uploadphoto.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent();
                   intent.setType("image/*");
                   intent.setAction(Intent.ACTION_GET_CONTENT);
                   startActivityForResult(intent,PICK_IMAGE_REQUEST);
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
//                System.out.println("name.getText().toString()"+name.getText().toString());
//                System.out.println("email.getText().toString()"+email);
//                System.out.println("number.getText().toString()"+number.getText().toString());
                if (name.getText().toString().equals("") || number.getText().toString().equals("") || email.getText().toString().equals("") || password.getText().toString().equals("") || confirm.getText().toString().equals("") || !(isValid(email.getText().toString())) || password.getText().toString().length()<6 || !(password.getText().toString().equals(confirm.getText().toString()))) {
                    if (name.getText().toString().equals("")) {
                        name.setError("Name Required");
                    }
                    if (email.getText().toString().equals("")) {
                        email.setError("Email Required");
                    }
                    if (number.getText().toString().equals("")) {
                        number.setError("Phone No. Required");
                    }
                    if (password.getText().toString().equals("")) {
                        password.setError("Password Required");
                    }
                    if (confirm.getText().toString().equals("")) {
                        confirm.setError("Confirm Password");
                    }
                    if(!(isValid(email.getText().toString()))){
                        email.setError("Enter Valid Email");
                    }
                    if(password.getText().toString().length()<6){
                        password.setError("Atleast 6 characters required");
                        password.setText("");
                        confirm.setText("");
                    }
                    if(!(password.getText().toString().equals(confirm.getText().toString()))){
                        confirm.setError("Must match with password");
                    }

                } else {

                    @SuppressLint("MissingPermission") Location location = loc.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    try {
                        loc_lat = location.getLatitude();
                        loc_long = location.getLongitude();
                    } catch (Exception e) {
                        loc_lat = 0;
                        loc_long = 0;
                        System.out.println("Error in accessing location: " + e.getMessage());
                    }
                    if (password.getText().toString().equals(confirm.getText().toString())) {
                        setContentView(R.layout.loading_screen);
                        fauth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                FirebaseUser user = authResult.getUser();
                                if (user != null) {
                                    System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjxxxxxxxxxxxxx"+imageUri);
                                    if(imageUri==null){
                                        imageUri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+getResources().getResourcePackageName(R.drawable.user)+'/'+getResources().getResourceTypeName(R.drawable.user)+'/'+getResources().getResourceEntryName(R.drawable.user));
                                        System.out.println("ttttttttttttxxxxxxxxxxxxx"+imageUri);
                                    }
                                    StorageReference stref = storageRef.child(Objects.requireNonNull(fauth.getCurrentUser()).getUid() + "_" + System.currentTimeMillis() + "." + getExtension(imageUri));
                                    stref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            if (taskSnapshot != null) {
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        profileimg1.setImageURI(uri);
                                                        System.out.println("----ImageURL: " + uri.toString());
                                                        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                                                        map.put("uid", Objects.requireNonNull(fauth.getCurrentUser()).getUid());
                                                        map.put("name", name.getText().toString());
                                                        map.put("email", email.getText().toString());
                                                        map.put("profileImgUrl", uri.toString());
                                                        map.put("phone", "+91" + number.getText().toString());
                                                        map.put("loc_lat", loc_lat);
                                                        map.put("loc_long", loc_long);
                                                        DocumentReference docref = fstore.collection("users").document(fauth.getCurrentUser().getUid());
                                                        docref.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                System.out.println("User created successfully!");
                                                                startActivity(new Intent(Register.this, LoginActivity.class));
                                                                //startActivity(new Intent(Register.this,homepage.class));
                                                            }
                                                        })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        setContentView(R.layout.activity_register);
                                                                        FirebaseUser user = fauth.getCurrentUser();
                                                                        user.delete();
                                                                        System.out.println("Failed to create user");
                                                                    }
                                                                });
                                                    }
                                                });
                                            }

                                            System.out.print("Image upload complete");
                                            Toast.makeText(Register.this, "Image upload complete", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    setContentView(R.layout.activity_register);
                                                    FirebaseUser user = fauth.getCurrentUser();
                                                    user.delete();
                                                    System.out.print("Image upload failed");
                                                    System.out.println(e.getMessage());
                                                    System.out.println(e.getStackTrace());
                                                    Toast.makeText(Register.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    private String getExtension(Uri imageUri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(imageUri));
    }

}

//    DocumentReference docref = fstore.collection("users").document("user_details").collection("all_users").document(fauth.getCurrentUser().getUid());

//if(imageUri==null){
//        imageUri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+getResources().getResourcePackageName(R.drawable.ic_userr)+'/'+getResources().getResourceTypeName(R.drawable.ic_userr)+'/'+getResources().getResourceEntryName(R.drawable.ic_userr));
//        System.out.println("lllllllllllllllllllll" + imageUri.toString());
//        Picasso.with(Register.this).load(imageUri).into(profileimg1);
//        }else {
