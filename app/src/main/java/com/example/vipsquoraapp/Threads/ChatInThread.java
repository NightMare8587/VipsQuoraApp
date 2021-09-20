package com.example.vipsquoraapp.Threads;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.vipsquoraapp.R;

public class ChatInThread extends AppCompatActivity {
    String title,createdBy,threadId,authId;
    TextView createBy,name;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_in_thread);
        title = getIntent().getStringExtra("title");
        createdBy = getIntent().getStringExtra("createdBy");
        threadId = getIntent().getStringExtra("threadID");
        authId = getIntent().getStringExtra("authID");
        recyclerView = findViewById(R.id.chatInThreadRecyclerView);
        name = findViewById(R.id.titleChatInThread);
        createBy = findViewById(R.id.createdByChatInthread);

        name.setText(title);
        createBy.setText(createdBy);

    }
}