package com.example.donation;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    Context context;
    List<Upload> mUploads;
    FirebaseAuth fauth = FirebaseAuth.getInstance();
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    StorageReference storageRef;
    Uri imgURL;

    public ImageAdapter(Context context, List<Upload> uploads)
    {
        this.context = context;
        this.mUploads=uploads;
        this.storageRef= FirebaseStorage.getInstance().getReference("images/");
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.explorepgitems,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String nameDonor;
        System.out.println("position="+position);
        System.out.println("mUploadsLength="+mUploads.size());
        Upload uploadCurrent = mUploads.get(position);
//        if(uploadCurrent==null){
//            System.out.print("Upload current is null"+uploadCurrent);
//            return;
//        }
        System.out.println("uploadCurrent.getName()"+uploadCurrent.getName());
        holder.textViewName.setText(uploadCurrent.getName());
//        DocumentReference donorName = fstore.collection("users").document(fauth.getCurrentUser().getUid());
//        donorName.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        document.getData();
//                        nameDonor[0] = data.get("name").toString();
//                        System.out.println(document.getData());
//                    } else {
//                        System.out.println("not found");
//                    }
//                } else {
//                    System.out.println("Task unsuccessful");
//                }
//            }
//        });
//        System.out.println("nameDonor[0]"+nameDonor[0]);
        System.out.println("hello");
          holder.textViewDonor.setText(uploadCurrent.getUser_name());
          System.out.println("abc");
          System.out.print("uploadCurrent.getImageUrl"+uploadCurrent.getImageUrl());
         System.out.println("abc");
//        storageRef.child(uploadCurrent.getImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                imgURL=uri;
//                // Got the download URL for 'users/me/profile.png'
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });
          Picasso.with(context).load(uploadCurrent.getImageUrl()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageView);
        System.out.println("abc");
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewName;
        public ImageView imageView;
        public TextView textViewDonor;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.itemName);
            textViewDonor = itemView.findViewById(R.id.donorName);
            imageView = itemView.findViewById(R.id.imageItem);

        }
    }
}
