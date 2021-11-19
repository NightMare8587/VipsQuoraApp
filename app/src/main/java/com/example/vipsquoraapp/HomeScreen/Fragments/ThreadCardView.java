package com.example.vipsquoraapp.HomeScreen.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vipsquoraapp.R;
import com.example.vipsquoraapp.Threads.Chat.ChatInThread;
import com.example.vipsquoraapp.Threads.FollowThreads.FollowThread;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;
import java.util.Objects;

public class ThreadCardView extends RecyclerView.Adapter<ThreadCardView.Holder> {
    List<String> title;
    List<String> likesCount;
    List<String> totalComments;
    List<String> createdBy;
    List<String> authID;
    List<String> threadID;

    public ThreadCardView(List<String> title, List<String> likesCount, List<String> totalComments, List<String> createdBy, List<String> authID, List<String> threadID) {
        this.title = title;
        this.likesCount = likesCount;
        this.totalComments = totalComments;
        this.createdBy = createdBy;
        this.authID = authID;
        this.threadID = threadID;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.thread_card_view,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(title.get(position));
        holder.createdByText.setText(createdBy.get(position));

        holder.like.setOnClickListener(click -> {
            FirebaseMessaging.getInstance().subscribeToTopic(Objects.requireNonNull(threadID.get(position)));
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FollowThread followThread = new FollowThread(title.get(position),threadID.get(position),createdBy.get(position),authID.get(position));
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(Objects.requireNonNull(auth.getUid()));
            databaseReference.child("Following Threads").child(threadID.get(position)).setValue(followThread);
            Toast.makeText(click.getContext(), "Thread Followed", Toast.LENGTH_SHORT).show();
        });

        holder.dislike.setOnClickListener(click -> {
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

        holder.cardView.setOnClickListener(click -> {
            Intent intent = new Intent(click.getContext(), ChatInThread.class);
            intent.putExtra("title",title.get(position));
            intent.putExtra("threadID",threadID.get(position));
            intent.putExtra("authID",authID.get(position));
            intent.putExtra("createdBy",createdBy.get(position));
            click.getContext().startActivity(intent);
        });

        holder.comment.setOnClickListener(click -> {
            Intent intent = new Intent(click.getContext(), ChatInThread.class);
            intent.putExtra("title",title.get(position));
            intent.putExtra("threadID",threadID.get(position));
            intent.putExtra("authID",authID.get(position));
            intent.putExtra("createdBy",createdBy.get(position));
            click.getContext().startActivity(intent);
        });

        holder.report.setOnClickListener(click -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(click.getContext());
            alert.setTitle("Report");
            EditText editText = new EditText(click.getContext());
            alert.setMessage("Enter Below");
            editText.setHint("Tell us what happened");
            editText.setMaxLines(200);
            LinearLayout layout = new LinearLayout(click.getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(editText);
            alert.setView(layout);
            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(!editText.getText().toString().equals("")) {
                        dialogInterface.dismiss();
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        SharedPreferences sharedPreferences = click.getContext().getSharedPreferences("AccountInfo", Context.MODE_PRIVATE);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().getRoot().child("Complaints");
                        ReportThredClass reportThredClass = new ReportThredClass(createdBy.get(position), title.get(position), threadID.get(position), auth.getUid(), editText.getText().toString(),authID.get(position),sharedPreferences.getString("email",""));
                        reference.child(Objects.requireNonNull(auth.getUid())).setValue(reportThredClass);
                        Toast.makeText(click.getContext(), "Thread Reported Successfully", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(click.getContext(), "Enter Some Reason", Toast.LENGTH_SHORT).show();
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alert.create().show();
        });
    }

    @Override
    public int getItemCount() {
        return threadID.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView title,createdByText;
        Button like,dislike,comment,report;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleCardView);
            cardView = itemView.findViewById(R.id.threadCardView);
            createdByText = itemView.findViewById(R.id.createdByTestVieewCard);

            like = itemView.findViewById(R.id.likeButtonCard);
            dislike = itemView.findViewById(R.id.dislikeButtonCard);
            comment = itemView.findViewById(R.id.commentButtonCard);
            report = itemView.findViewById(R.id.reportButtonCard);
        }
    }
}
