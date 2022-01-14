package com.example.vipsquoraapp.Account;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.example.vipsquoraapp.MainActivity;
import com.example.vipsquoraapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MyAccount extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    EditText name,email;
    SharedPreferences.Editor editor;
    Button logout;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;
    Switch aSwitch;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        sharedPreferences = getSharedPreferences("AccountInfo",MODE_PRIVATE);
        email = findViewById(R.id.emailAccount);
        logout = findViewById(R.id.logoutButtonApplication);
        name = findViewById(R.id.personNammeAccount);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(Objects.requireNonNull(auth.getUid()));
        imageView = findViewById(R.id.homeImageView);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(MyAccount.this,gso);
        aSwitch = findViewById(R.id.anonomousProfile);
        if(sharedPreferences.contains("anonymous")){
            if(sharedPreferences.getString("anonymous","").equals("yes"))
                aSwitch.setChecked(true);
        }
        logout.setOnClickListener(click -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyAccount.this);
            builder.setTitle("Logout")
                    .setMessage("Do you wanna logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(Objects.requireNonNull(auth.getUid()));
                            auth.signOut();
                            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                            SharedPreferences settings = getSharedPreferences("AccountInfo", MODE_PRIVATE);
                            settings.edit().clear().commit();
                            SharedPreferences intro = getSharedPreferences("IntroShown", MODE_PRIVATE);
                            intro.edit().clear().commit();


                            startActivity(new Intent(MyAccount.this, MainActivity.class));
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create();

            builder.show();
        });
        email.setText(sharedPreferences.getString("email",""));
        name.setText(sharedPreferences.getString("name",""));
        editor = sharedPreferences.edit();
        aSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                editor.putString("anonymous","yes");
                editor.apply();
                databaseReference.child("anonymous").setValue("yes");
            }else{
                editor.putString("anonymous","no");
                editor.apply();
                databaseReference.child("anonymous").setValue("no");
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