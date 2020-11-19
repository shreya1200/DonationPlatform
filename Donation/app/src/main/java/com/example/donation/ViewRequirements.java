package com.example.donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ViewRequirements extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore fstore;FirestoreRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requirements);
        recyclerView = findViewById(R.id.recyclerView);
        fstore = FirebaseFirestore.getInstance();
        Query query = fstore.collection("requests");
        FirestoreRecyclerOptions<RequestData> options = new FirestoreRecyclerOptions.Builder<RequestData>()
                .setQuery(query,RequestData.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<RequestData, RequestViewHolder>(options) {
            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requirement_listitem,parent,false);
                return new RequestViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(RequestViewHolder requestViewHolder, int i, RequestData requestData) {
                requestViewHolder.title.setText(requestData.getTitle());
                requestViewHolder.category.setText(requestData.getCategory());
                requestViewHolder.description.setText(requestData.getDescription());
                requestViewHolder.postedBy.setText(requestData.getName());
                requestViewHolder.contact.setText(requestData.getNumber());
                requestViewHolder.city.setText(requestData.getCity());
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    private class RequestViewHolder extends RecyclerView.ViewHolder{
        final private TextView title,category,description,postedBy,contact,city;
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            category = itemView.findViewById(R.id.category);
            description = itemView.findViewById(R.id.desc);
            postedBy = itemView.findViewById(R.id.listedBy);
            contact = itemView.findViewById(R.id.contact);
            city = itemView.findViewById(R.id.city);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}