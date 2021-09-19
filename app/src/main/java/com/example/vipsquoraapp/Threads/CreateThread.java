package com.example.vipsquoraapp.Threads;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vipsquoraapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

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
        button.setOnClickListener(click -> {
            if(editText.getText().toString().equals("")){
                Toast.makeText(CreateThread.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                editText.requestFocus();
                editText.setError("Field can't be empty");
            }else{
                SharedPreferences sharedPreferences = getSharedPreferences("AccountInfo",MODE_PRIVATE);
                ThreadClass threadClass;
                if(sharedPreferences.contains("anonymous")) {
                    if (sharedPreferences.getString("anonymous", "").equals("yes")) {
                         threadClass = new ThreadClass(editText.getText().toString(), "0", "0", "anonymous", "null");
                    } else {
                         threadClass = new ThreadClass(editText.getText().toString(), "0", "0", sharedPreferences.getString("name", ""), auth.getUid() + "");
                    }
                }else
                     threadClass = new ThreadClass(editText.getText().toString(),"0","0",sharedPreferences.getString("name",""),auth.getUid()+"");

                String genratedID = RandomString.getAlphaNumericString(20);databaseReference.child(genratedID).setValue(threadClass);
                DatabaseReference followThreads = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(Objects.requireNonNull(auth.getUid())).child("Following Threads");
                followThreads.child(genratedID).child("follow").setValue("yes");
                Toast.makeText(CreateThread.this, "Thread Created Successfully", Toast.LENGTH_SHORT).show();
                FirebaseMessaging.getInstance().subscribeToTopic(Objects.requireNonNull(genratedID));
                finish();
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