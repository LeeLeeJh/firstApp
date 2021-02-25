package com.example.firstapp;

import android.net.Uri;


public class comment_info {


    private String Name;
    private String Insert;
    public Uri Profile;

    public  String getName(){
        return Name;
    }

    public void setName(String name){
        Name = name;
    }

    public String getInsert(){
        return Insert;
    }

    public void setInsert(String insert){
        this.Insert = insert;
    }


    public Uri getProfile(){
        return Profile;
    }

    public void setProfile(Uri profile){
        Profile = profile;
    }

    public comment_info (String name, String insert, Uri profile){
        Name = name;
        Insert = insert;
        Profile = profile;

    }

}
