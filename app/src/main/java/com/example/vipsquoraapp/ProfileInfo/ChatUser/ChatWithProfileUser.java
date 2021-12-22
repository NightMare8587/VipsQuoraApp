package com.example.vipsquoraapp.ProfileInfo.ChatUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.vipsquoraapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ChatWithProfileUser extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    String URL = "https://fcm.googleapis.com/fcm/send";
    String authId;
    RecyclerView recyclerView;
    boolean containsBad = false;
    Button sendME;
    EditText editText;
    List<String> time = new ArrayList<>();
    List<String> leftOrRight = new ArrayList<>();
    List<String> sendBy = new ArrayList<>();
    List<String> message = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    List<String> badWords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_profile_user);
        authId = getIntent().getStringExtra("authID");
        recyclerView = findViewById(R.id.messageRecyclerView);
        sendME = findViewById(R.id.sendMessageButton);
        badWords = new ArrayList<>();
        initialise();
        linearLayoutManager = new LinearLayoutManager(this);
        editText = findViewById(R.id.sendMessageEditText);
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(Objects.requireNonNull(auth.getUid()));
        databaseReference.child("messages").child(authId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    message.clear();
                    time.clear();
                    leftOrRight.clear();
                    sendBy.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        sendBy.add(String.valueOf(dataSnapshot.child("senderId").getValue()));
                        message.add(String.valueOf(dataSnapshot.child("message").getValue()));
                        time.add(String.valueOf(dataSnapshot.child("time").getValue()));
                        leftOrRight.add(String.valueOf(dataSnapshot.child("id").getValue()));
                    }

                    linearLayoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(linearLayoutManager);

//                    recyclerView.smoothScrollToPosition(message.size()-1);
                    recyclerView.setAdapter(new chatAdapter(message,time,leftOrRight,sendBy,auth.getUid() + ""));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateChat();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateChat();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                updateChat();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                containsBad = false;
                if(editText.getText().toString().length() == 0){
                    Toast.makeText(ChatWithProfileUser.this, "Enter Some Text", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    String  inputString = editText.getText().toString();
                    for(int i=0;i<badWords.size();i++){
                        String word = badWords.get(i);
                        if(inputString.toLowerCase().contains(word)){
                            containsBad = true;
                            Toast.makeText(ChatWithProfileUser.this, "We don't allow bad words in our app", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    checkIfBadOrNot();

                }
            }
        });
    }

    private void checkIfBadOrNot() {
        if(!containsBad) {
            RequestQueue requestQueue = Volley.newRequestQueue(ChatWithProfileUser.this);
            JSONObject main = new JSONObject();
            try {
                main.put("to", "/topics/" + authId + "");
                JSONObject notification = new JSONObject();
                notification.put("title", "New Message");
                notification.put("body", "" + editText.getText().toString().trim());
                main.put("notification", notification);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, main, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getLocalizedMessage() + "null", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> header = new HashMap<>();
                        header.put("content-type", "application/json");
                        header.put("authorization", "key=AAAArvsdfj8:APA91bEJ8DpObwwfGHTFd_IdYxwD-rFx3bkD-SDIahld6WG2WvTNEh5pVX8Xhmcz3XSYsnxtmJ3ZCg3UaS42CzCltIaAB2ZRZbs9hOja3tIqyPdd2Nt8zRemfQMJNqWq6fQv9OTiIHl5");
                        return header;
                    }
                };

                requestQueue.add(jsonObjectRequest);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage() + "null", Toast.LENGTH_SHORT).show();
            }
            SharedPreferences preferences = getSharedPreferences("AccountInfo", MODE_PRIVATE);
            String currentTime = System.currentTimeMillis() + "";
            chat chat = new chat(editText.getText().toString().trim(), auth.getUid() + "", currentTime + "", "0", preferences.getString("name", ""));
            databaseReference.child("messages").child(authId).child(System.currentTimeMillis() + "").setValue(chat);
            databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(authId);
            databaseReference.child("messages").child(Objects.requireNonNull(auth.getUid())).child(currentTime + "").setValue(chat);
            editText.setText("");
            databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(Objects.requireNonNull(auth.getUid()));

            updateChat();
        }
    }

    private void initialise() {
        badWords.add("fuck");
        badWords.add("lodu");
        badWords.add("chutiya");
        badWords.add("madarchod");
        badWords.add("bc");
        badWords.add("mc");
        badWords.add("bhenchod");
        badWords.add("suar");
        badWords.add("randi");
    }

    private void updateChat() {
        databaseReference.child("messages").child(authId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    message.clear();
                    time.clear();
                    leftOrRight.clear();
                    sendBy.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        sendBy.add(String.valueOf(dataSnapshot.child("senderId").getValue()));
                        message.add(String.valueOf(dataSnapshot.child("message").getValue()));
                        time.add(String.valueOf(dataSnapshot.child("time").getValue()));
                        leftOrRight.add(String.valueOf(dataSnapshot.child("id").getValue()));
                    }

                    linearLayoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(linearLayoutManager);

//                    recyclerView.smoothScrollToPosition(message.size()-1);
                    recyclerView.setAdapter(new chatAdapter(message,time,leftOrRight,sendBy,auth.getUid() + ""));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}