package com.example.vipsquoraapp.HomeScreen.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.vipsquoraapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatsActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    List<String> usersID = new ArrayList<>();
    RecyclerView recyclerView;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        recyclerView = findViewById(R.id.recyclerViewChatsActivity);
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(Objects.requireNonNull(auth.getUid())).child("messages");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        usersID.add(String.valueOf(dataSnapshot.getKey()));
                    }
                        recyclerView.setLayoutManager(new LinearLayoutManager(ChatsActivity.this));
                    recyclerView.setAdapter(new ChatUsersAdapter(usersID));
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(ChatsActivity.this)
                            .setTitle("No Chats")
                            .setMessage("You have no chats available")
                            .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    finish();
                                }
                            });
                    alert.create();
                    alert.setCancelable(false);
                    alert.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}