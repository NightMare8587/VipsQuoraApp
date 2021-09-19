package com.example.vipsquoraapp.HomeScreen.Fragments;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllThreadsFragment extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    List<String> title = new ArrayList<>();
    List<String> likesCount = new ArrayList<>();
    List<String> totalComments = new ArrayList<>();
    List<String> createdBy = new ArrayList<>();
    List<String> authID = new ArrayList<>();
    List<String> threadID = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_threads_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.allThreadRecyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Threads");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        threadID.add(String.valueOf(dataSnapshot.getKey()));
                        title.add(String.valueOf(dataSnapshot.child("title").getValue()));
                        likesCount.add(String.valueOf(dataSnapshot.child("likes").getValue()));
                        createdBy.add(String.valueOf(dataSnapshot.child("createdBy").getValue()));
                        totalComments.add(String.valueOf(dataSnapshot.child("comments").getValue()));
                        authID.add(String.valueOf(dataSnapshot.child("authID").getValue()));
                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    recyclerView.setAdapter(new ThreadCardView(title,likesCount,totalComments,createdBy,authID,threadID));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
