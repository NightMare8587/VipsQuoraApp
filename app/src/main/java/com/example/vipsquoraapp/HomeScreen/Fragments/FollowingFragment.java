package com.example.vipsquoraapp.HomeScreen.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vipsquoraapp.R;
import com.example.vipsquoraapp.Threads.CreateThread;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FollowingFragment extends Fragment {
    FloatingActionButton floatingActionButton;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(click -> {
            startActivity(new Intent(click.getContext(), CreateThread.class));
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.following_fragment,container,false);
    }
}
