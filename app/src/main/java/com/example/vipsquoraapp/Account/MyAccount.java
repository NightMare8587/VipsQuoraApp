package com.example.vipsquoraapp.Account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.vipsquoraapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MyAccount extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    EditText name,email;
    SharedPreferences.Editor editor;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    Switch aSwitch;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        sharedPreferences = getSharedPreferences("AccountInfo",MODE_PRIVATE);
        email = findViewById(R.id.emailAccount);
        name = findViewById(R.id.personNammeAccount);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(Objects.requireNonNull(auth.getUid()));


        imageView = findViewById(R.id.homeImageView);
        aSwitch = findViewById(R.id.anonomousProfile);
        if(sharedPreferences.contains("anonymous")){
            if(sharedPreferences.getString("anonymous","").equals("yes"))
                aSwitch.setChecked(true);
        }

        email.setText(sharedPreferences.getString("email",""));
        name.setText(sharedPreferences.getString("name",""));
        editor = sharedPreferences.edit();
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    editor.putString("anonymous","yes");
                    editor.apply();
                    databaseReference.child("anonymous").setValue("yes");
                }else{
                    editor.putString("anonymous","no");
                    editor.apply();
                    databaseReference.child("anonymous").setValue("no");
                }
            }
        });

        Picasso.get().load(sharedPreferences.getString("photoUrl","")).resize(175,175).into(imageView, new Callback() {
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