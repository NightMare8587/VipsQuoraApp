package com.example.vipsquoraapp.ProfileInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vipsquoraapp.ProfileInfo.ChatUser.ChatWithProfileUser;
import com.example.vipsquoraapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ShowUserProfile extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    String authID;
    TextView userName,emailName;
    Button chatWithUser;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_profile);
        authID = getIntent().getStringExtra("authID");
        chatWithUser = findViewById(R.id.chatWithProfileUserButton);
        userName = findViewById(R.id.userNameShowProfile);
        imageView = findViewById(R.id.userImageShowProfile);
        emailName = findViewById(R.id.userEmailShowProfile);
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(authID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.hasChild("name")){
                        userName.setText(String.valueOf(snapshot.child("name").getValue()));
                    }
                    if(snapshot.hasChild("email")){
                        emailName.setText(String.valueOf(snapshot.child("email").getValue()));
                    }
                    if(snapshot.hasChild("photoUrl")){
                        Picasso.get().load(String.valueOf(snapshot.child("photoUrl").getValue())).resize(175,175).into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                imageDrawable.setCircular(true);
                                imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                imageView.setImageDrawable(imageDrawable);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chatWithUser.setOnClickListener(click -> {
            Intent intent = new Intent(ShowUserProfile.this, ChatWithProfileUser.class);
            intent.putExtra("authID",authID);
            startActivity(intent);
        });
    }
}