package com.example.vipsquoraapp.Threads;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.vipsquoraapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateThread extends AppCompatActivity {
    EditText editText;
    Button button;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_thread);
        editText = findViewById(R.id.editTextTextMultiLine);
        button = findViewById(R.id.createThreadButton);

        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Threads");


    }
}