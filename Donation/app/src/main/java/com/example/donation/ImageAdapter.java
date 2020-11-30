package com.example.donation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

import java.io.Serializable;
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
        System.out.println("position="+position);
        System.out.println("mUploadsLength="+mUploads.size());
        Upload uploadCurrent = mUploads.get(position);
        System.out.println("uploadCurrent.getName()"+uploadCurrent.getName());
        holder.textViewName.setText(uploadCurrent.getName());
        System.out.println("hello");
          holder.textViewDonor.setText(uploadCurrent.getUser_name());
          System.out.println("abc");
          System.out.print("uploadCurrent.getImageUrl"+uploadCurrent.getImageUrl());
          System.out.println("abc");
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
        private Upload upload;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.itemName);
            textViewDonor = itemView.findViewById(R.id.donorName);
            imageView = itemView.findViewById(R.id.imageItem);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upload = mUploads.get(getLayoutPosition());
                    Intent intent=new Intent(v.getContext(),ProductDetails.class);
                    intent.putExtra("upload",upload);
                    context.startActivity(intent);


                }
            });
        }

    }
}
