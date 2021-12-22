package com.example.vipsquoraapp;

public class GoogleSignInDB {
    public String name,email, anonymous,photoUrl;

    public GoogleSignInDB(String name, String email,String anonymous,String photoUrl) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.anonymous = anonymous;
    }
}
