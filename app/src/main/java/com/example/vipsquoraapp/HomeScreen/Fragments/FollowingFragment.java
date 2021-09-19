package com.example.vipsquoraapp.HomeScreen.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vipsquoraapp.R;
import com.example.vipsquoraapp.Threads.CreateThread;
import com.example.vipsquoraapp.Threads.FollowThreads.FollowThreadAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FollowingFragment extends Fragment {
    FloatingActionButton floatingActionButton;
    DatabaseReference databaseReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    List<String> title = new ArrayList<>();
    List<String> threadID = new ArrayList<>();
    List<String> authId = new ArrayList<>();
    List<String> createdBy = new ArrayList<>();
    RecyclerView recyclerView;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        recyclerView = view.findViewById(R.id.followingFragRecyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(Objects.requireNonNull(auth.getUid()));
        databaseReference.child("Following Threads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    title.clear();
                    createdBy.clear();
                    authId.clear();
                    threadID.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        title.add(String.valueOf(dataSnapshot.child("title").getValue()));
                        createdBy.add(String.valueOf(dataSnapshot.child("createdBy").getValue()));
                        authId.add(String.valueOf(dataSnapshot.child("authID").getValue()));
                        threadID.add(String.valueOf(dataSnapshot.child("threadID").getValue()));
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    recyclerView.setAdapter(new FollowThreadAdapter(createdBy,authId,threadID,title));
                }else{
                    title.clear();
                    createdBy.clear();
                    authId.clear();
                    threadID.clear();
                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    recyclerView.setAdapter(new FollowThreadAdapter(createdBy,authId,threadID,title));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        floatingActionButton.setOnClickListener(click -> startActivity(new Intent(click.getContext(), CreateThread.class)));

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateChild();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateChild();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                updateChild();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateChild() {
        databaseReference.child("Following Threads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    title.clear();
                    createdBy.clear();
                    authId.clear();
                    threadID.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        title.add(String.valueOf(dataSnapshot.child("title").getValue()));
                        createdBy.add(String.valueOf(dataSnapshot.child("createdBy").getValue()));
                        authId.add(String.valueOf(dataSnapshot.child("authID").getValue()));
                        threadID.add(String.valueOf(dataSnapshot.child("threadID").getValue()));
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    recyclerView.setAdapter(new FollowThreadAdapter(createdBy,authId,threadID,title));
                }else{
                    title.clear();
                    createdBy.clear();
                    authId.clear();
                    threadID.clear();
                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    recyclerView.setAdapter(new FollowThreadAdapter(createdBy,authId,threadID,title));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.following_fragment,container,false);
    }
}
