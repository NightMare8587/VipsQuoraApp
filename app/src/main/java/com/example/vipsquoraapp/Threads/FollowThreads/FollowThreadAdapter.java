package com.example.vipsquoraapp.Threads.FollowThreads;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vipsquoraapp.R;
import com.example.vipsquoraapp.Threads.ChatInThread;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;
import java.util.Objects;

public class FollowThreadAdapter extends RecyclerView.Adapter<FollowThreadAdapter.Holder> {
    List<String> createdBy;
    List<String> authID;
    List<String> threadID;
    List<String> title;

    public FollowThreadAdapter(List<String> createdBy, List<String> authID, List<String> threadID, List<String> title) {
        this.createdBy = createdBy;
        this.authID = authID;
        this.threadID = threadID;
        this.title = title;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.follow_card_view,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(title.get(position));
        holder.createdBy.setText(createdBy.get(position));

        holder.cardView.setOnClickListener(click -> {
            Intent intent = new Intent(click.getContext(), ChatInThread.class);
            intent.putExtra("title",title.get(position));
            intent.putExtra("threadID",threadID.get(position));
            intent.putExtra("authID",authID.get(position));
            intent.putExtra("createdBy",createdBy.get(position));
            click.getContext().startActivity(intent);
        });

        holder.remove.setOnClickListener(click -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(Objects.requireNonNull(auth.getUid()));
            databaseReference.child("Following Threads").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        if(snapshot.hasChild(threadID.get(position))){
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(Objects.requireNonNull(threadID.get(position)));
                            databaseReference.child("Following Threads").child(threadID.get(position)).removeValue();
                            Toast.makeText(click.getContext(), "Thread Unfollowed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        holder.report.setOnClickListener(click ->{

        });
    }

    @Override
    public int getItemCount() {
        return threadID.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        CardView cardView;
        Button remove,report;
        TextView title,createdBy;
        public Holder(@NonNull View itemView) {
            super(itemView);
            remove = itemView.findViewById(R.id.removeCardView);
            report = itemView.findViewById(R.id.reportFollowButtonCard);
            cardView = itemView.findViewById(R.id.threadFollowCardView);

            title = itemView.findViewById(R.id.titleFollowCardView);
            createdBy = itemView.findViewById(R.id.createdByTestFollowVieewCard);
        }
    }
}
