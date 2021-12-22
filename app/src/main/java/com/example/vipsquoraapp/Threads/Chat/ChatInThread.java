package com.example.vipsquoraapp.Threads.Chat;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vipsquoraapp.Account.MyAccount;
import com.example.vipsquoraapp.ProfileInfo.ShowUserProfile;
import com.example.vipsquoraapp.R;
import com.example.vipsquoraapp.Threads.FollowThreads.FollowThread;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatInThread extends AppCompatActivity {
    String title,createdBy,threadId,authId;
    TextView createBy,name;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    List<String> titleName = new ArrayList<>();
    List<String> createdByName = new ArrayList<>();
    List<String> threadIdName = new ArrayList<>();
    String URL = "https://fcm.googleapis.com/fcm/send";
    List<String> authIdName = new ArrayList<>();
    DatabaseReference addCommentToDB;
    Button addComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_in_thread);
        title = getIntent().getStringExtra("title");
        createdBy = getIntent().getStringExtra("createdBy");
        threadId = getIntent().getStringExtra("threadID");
        authId = getIntent().getStringExtra("authID");
        recyclerView = findViewById(R.id.chatInThreadRecyclerView);
        SharedPreferences sharedPreferences = getSharedPreferences("AccountInfo",MODE_PRIVATE);
        addComment = findViewById(R.id.replyToChatInThread);
        name = findViewById(R.id.titleChatInThread);
        createBy = findViewById(R.id.createdByChatInthread);
        addCommentToDB = FirebaseDatabase.getInstance().getReference().getRoot().child("Threads").child(threadId);
        addComment.setOnClickListener(click -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(ChatInThread.this);
            alert.setTitle("Comment On Thread");
            alert.setMessage("Enter you comment in below field");
            EditText commentEdit = new EditText(ChatInThread.this);
            commentEdit.setHint("Enter Comment Here");
            alert.setPositiveButton("Submit", (dialogInterface, i) -> {
                if(commentEdit.getText().toString().equals("")){
                    Toast.makeText(ChatInThread.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    commentEdit.requestFocus();
                    commentEdit.setError("Enter some text/comment");

                }else {
                    RequestQueue requestQueue = Volley.newRequestQueue(ChatInThread.this);
                    JSONObject main = new JSONObject();
                    try {
                        main.put("to", "/topics/" + threadId);
                        JSONObject notification = new JSONObject();
                        notification.put("title", "Table " + "New Comment");
                        notification.put("body", "Someone replied to thread you are following");
                        main.put("notification", notification);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, main, response -> {

                        }, error -> Toast.makeText(getApplicationContext(), error.getLocalizedMessage() + "null", Toast.LENGTH_SHORT).show()) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> header = new HashMap<>();
                                header.put("content-type", "application/json");
                                header.put("authorization", "key=AAAAsigSEMs:APA91bEUF9ZFwIu84Jctci56DQd0TQOepztGOIKIBhoqf7N3ueQrkClw0xBTlWZEWyvwprXZmZgW2MNywF1pNBFpq1jFBr0CmlrJ0wygbZIBOnoZ0jP1zZC6nPxqF2MAP6iF3wuBHD2R");
                                return header;
                            }
                        };

                        requestQueue.add(jsonObjectRequest);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage() + "null", Toast.LENGTH_SHORT).show();
                    }
                    ChatClass chatClass;
                    String generatedID = String.valueOf(System.currentTimeMillis());
                    if(sharedPreferences.contains("anonymous")){
                        if(sharedPreferences.getString("anonymous","").equals("yes")){
                            chatClass = new ChatClass(commentEdit.getText().toString(),generatedID,auth.getUid(),"anonymous");
                        }else
                            chatClass = new ChatClass(commentEdit.getText().toString(),generatedID,auth.getUid(),sharedPreferences.getString("name",""));
                    }else
                        chatClass = new ChatClass(commentEdit.getText().toString(),generatedID,auth.getUid(),sharedPreferences.getString("name",""));

                    addCommentToDB.child("comments").child(generatedID).setValue(chatClass);
                    dialogInterface.dismiss();
                    FirebaseMessaging.getInstance().subscribeToTopic(threadId);
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FollowThread followThread = new FollowThread(title,threadId,createdBy,authId);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(Objects.requireNonNull(auth.getUid()));
                    databaseReference.child("Following Threads").child(threadId).setValue(followThread);
                    Toast.makeText(click.getContext(), "Thread Followed", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("Cancel", (dialogInterface, i) -> {
                Toast.makeText(ChatInThread.this, "Cancelled", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            });
            LinearLayout linearLayout = new LinearLayout(ChatInThread.this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            commentEdit.setTextColor(Color.BLACK);
            commentEdit.setMaxLines(200);
            linearLayout.addView(commentEdit);
            alert.setView(linearLayout);

            alert.create().show();
        });

        createBy.setOnClickListener(click -> {
            if(createBy.getText().toString().equals("anonymous")){
                Toast.makeText(ChatInThread.this, "Can't open anonymous profile", Toast.LENGTH_SHORT).show();
            }else if(authId.equals(String.valueOf(auth.getUid()))){
                    startActivity(new Intent(ChatInThread.this, MyAccount.class));
            }else
            {
                Intent intent = new Intent(ChatInThread.this, ShowUserProfile.class);
                intent.putExtra("authID",authId);
                startActivity(intent);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Threads").child(threadId).child("comments");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    threadIdName.clear();
                    authIdName.clear();
                    titleName.clear();
                    createdByName.clear();

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        threadIdName.add(String.valueOf(dataSnapshot.getKey()));
                        authIdName.add(String.valueOf(dataSnapshot.child("authID").getValue()));
                        titleName.add(String.valueOf(dataSnapshot.child("title").getValue()));
                        createdByName.add(String.valueOf(dataSnapshot.child("createdBy").getValue()));
                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(ChatInThread.this));
                    recyclerView.setAdapter(new ComentsAdapter(titleName,createdByName,threadIdName,authIdName));
                }else{
                    threadIdName.clear();
                    authIdName.clear();
                    titleName.clear();
                    createdByName.clear();
                    recyclerView.setLayoutManager(new LinearLayoutManager(ChatInThread.this));
                    recyclerView.setAdapter(new ComentsAdapter(titleName,createdByName,threadIdName,authIdName));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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

        name.setText(title);
        createBy.setText(createdBy);

    }

    private void updateChild() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    threadIdName.clear();
                    authIdName.clear();
                    titleName.clear();
                    createdByName.clear();

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        threadIdName.add(String.valueOf(dataSnapshot.getKey()));
                        authIdName.add(String.valueOf(dataSnapshot.child("authID").getValue()));
                        titleName.add(String.valueOf(dataSnapshot.child("title").getValue()));
                        createdByName.add(String.valueOf(dataSnapshot.child("createdBy").getValue()));
                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(ChatInThread.this));
                    recyclerView.setAdapter(new ComentsAdapter(titleName,createdByName,threadIdName,authIdName));
                }else{
                    threadIdName.clear();
                    authIdName.clear();
                    titleName.clear();
                    createdByName.clear();
                    recyclerView.setLayoutManager(new LinearLayoutManager(ChatInThread.this));
                    recyclerView.setAdapter(new ComentsAdapter(titleName,createdByName,threadIdName,authIdName));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static class RandomString {

        // function to generate a random string of length n
        static String getAlphaNumericString(int n) {

            // chose a Character random from this String
            String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "0123456789"
                    + "abcdefghijklmnopqrstuvxyz";

            // create StringBuffer size of AlphaNumericString
            StringBuilder sb = new StringBuilder(n);

            for (int i = 0; i < n; i++) {

                // generate a random number between
                // 0 to AlphaNumericString variable length
                int index
                        = (int) (AlphaNumericString.length()
                        * Math.random());

                // add Character one by one in end of sb
                sb.append(AlphaNumericString
                        .charAt(index));
            }

            return sb.toString();
        }
    }
}