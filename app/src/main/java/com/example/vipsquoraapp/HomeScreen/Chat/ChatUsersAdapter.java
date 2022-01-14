package com.example.vipsquoraapp.HomeScreen.Chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vipsquoraapp.ProfileInfo.ChatUser.ChatWithProfileUser;
import com.example.vipsquoraapp.ProfileInfo.ShowUserProfile;
import com.example.vipsquoraapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ChatUsersAdapter extends RecyclerView.Adapter<ChatUsersAdapter.Holder> {
    List<String> userId;

    public ChatUsersAdapter(List<String> userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_chats_recycler_layout,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatWithProfileUser.class);
                intent.putExtra("authID",userId.get(position));
                view.getContext().startActivity(intent);
            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(userId.get(position));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.textView.setText(snapshot.child("name").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userId.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView textView;
        CardView cardView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textNameUserChats);
            cardView = itemView.findViewById(R.id.userChatsCadrView);
        }
    }
}
