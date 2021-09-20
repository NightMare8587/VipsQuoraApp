package com.example.vipsquoraapp.SplashAndIntro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vipsquoraapp.GoogleSignInDB;
import com.example.vipsquoraapp.HomeScreen.Home;
import com.example.vipsquoraapp.MainActivity;
import com.example.vipsquoraapp.R;
import com.example.vipsquoraapp.SplashAndIntro.Fragments.FragFour;
import com.example.vipsquoraapp.SplashAndIntro.Fragments.FragOne;
import com.example.vipsquoraapp.SplashAndIntro.Fragments.FragThree;
import com.example.vipsquoraapp.SplashAndIntro.Fragments.FragTwo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class IntroActivity extends AppCompatActivity {
    FragmentManager manager;
    int count = 1;
    Fragment active;
    SharedPreferences introPref;
    SharedPreferences.Editor editorIntro;
    Button next,back;
    GoogleSignInOptions gso;
    GoogleSignInClient client;
    GoogleSignInAccount account;
    SignInButton signInButton;
    FirebaseAuth loginAuth = FirebaseAuth.getInstance();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseUser currentUser;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        sharedPreferences = getSharedPreferences("AccountInfo",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        manager = getSupportFragmentManager();
        FragOne fragOne = new FragOne();
        FragThree fragThree = new FragThree();
        FragFour fragFour = new FragFour();
        FragTwo fragTwo = new FragTwo();
        next = findViewById(R.id.nextIntroButton);
        back = findViewById(R.id.backButtonIntro);
        introPref = getSharedPreferences("IntroShown",MODE_PRIVATE);
        editorIntro = introPref.edit();
        active = fragOne;
        manager.beginTransaction().add(R.id.myIntroAct, fragFour, "4").hide(fragFour).commit();
        manager.beginTransaction().add(R.id.myIntroAct, fragThree, "3").hide(fragThree).commit();
        manager.beginTransaction().add(R.id.myIntroAct, fragTwo, "2").hide(fragTwo).commit();
        manager.beginTransaction().add(R.id.myIntroAct, fragOne, "1").commit();

        signInButton = findViewById(R.id.signInButton);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        sharedPreferences = getSharedPreferences("AccountInfo",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().getRoot();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("751537323583-80p42mrn730pb1ja4q3ppsn1iaoe8fbj.apps.googleusercontent.com")
                .requestEmail()
                .build();


        client = GoogleSignIn.getClient(IntroActivity.this,gso);


        signInButton.setOnClickListener(b -> {
            Intent signIn = client.getSignInIntent();
            startActivityForResult(signIn,3);
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;

                if(count == 1){
                    back.setVisibility(View.INVISIBLE);
                }else
                    back.setVisibility(View.VISIBLE);

                if(count == 4){
                    signInButton.setVisibility(View.VISIBLE);
                    next.setVisibility(View.INVISIBLE);
                }else {
                    signInButton.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);
                }

                switch (count){
                    case 1:
                        manager.beginTransaction().setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.slide_out
                        ).hide(active).show(fragOne).commit();
                        active = fragOne;
                        break;
                    case 2:
                        manager.beginTransaction().hide(active).setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.slide_out
                        ).show(fragTwo).commit();
                        active = fragTwo;
                        break;
                    case 3:
                        manager.beginTransaction().hide(active).setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.slide_out
                        ).show(fragThree).commit();
                        active = fragThree;
                        break;
                    case 4:
                        manager.beginTransaction().hide(active).setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.slide_out
                        ).show(fragFour).commit();
                        active = fragFour;
                        break;
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count--;

                if(count == 1){
                    back.setVisibility(View.INVISIBLE);
                }else
                    back.setVisibility(View.VISIBLE);

                if(count == 4){
                    signInButton.setVisibility(View.VISIBLE);
                    next.setVisibility(View.INVISIBLE);
                }else {
                    signInButton.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);
                }

                switch (count){
                    case 1:
                        manager.beginTransaction().hide(active).setCustomAnimations(
                                R.anim.slide_out,  // enter
                                R.anim.slide_in
                        ).show(fragOne).commit();
                        active = fragOne;
                        break;
                    case 2:
                        manager.beginTransaction().hide(active).setCustomAnimations(
                                R.anim.slide_out,  // enter
                                R.anim.slide_in
                        ).show(fragTwo).commit();
                        active = fragTwo;
                        break;
                    case 3:
                        manager.beginTransaction().hide(active).setCustomAnimations(
                                R.anim.slide_out,  // enter
                                R.anim.slide_in
                        ).show(fragThree).commit();
                        active = fragThree;
                        break;
                    case 4:
                        manager.beginTransaction().hide(active).setCustomAnimations(
                                R.anim.slide_out,  // enter
                                R.anim.slide_in
                        ).show(fragFour).commit();
                        active = fragFour;
                        break;

                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Toast.makeText(this, "Sign In", Toast.LENGTH_SHORT).show();
            createFirebaseAuthID(account.getIdToken());
            getSignInInformation();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, e.getLocalizedMessage()+"", Toast.LENGTH_SHORT).show();
        }
    }

    private void createFirebaseAuthID(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        loginAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    loginAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = loginAuth.getCurrentUser();
                    assert user != null;
                    loginAuth.updateCurrentUser(user);

                    reference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.hasChild(Objects.requireNonNull(loginAuth.getUid()))){
//                                        reference.child("Users").child(Objects.requireNonNull(loginAuth.getUid())).setValue(user);
                                reference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(user.getUid());
                                GoogleSignInDB googleSignInDB = new GoogleSignInDB(account.getDisplayName(),account.getEmail(),"no");
                                reference.setValue(googleSignInDB);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    editorIntro.putString("intro","yes");
                    editorIntro.apply();
                    startActivity(new Intent(IntroActivity.this, Home.class));

                });
    }

    private void getSignInInformation() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            String photoUrl = String.valueOf(personPhoto);
            editor.putString("email",personEmail);
            editor.putString("name",personName);
            editor.putString("photoUrl",personPhoto.toString());
            editor.apply();
            Log.i("info",personName+ " " + personEmail + " " + personFamilyName);
        }
    }
}