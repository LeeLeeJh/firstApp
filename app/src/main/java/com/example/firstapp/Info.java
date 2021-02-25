package com.example.firstapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

public class Info {

    public String Name;
    public String Insert;
    public static int LikeCount;
    public TextView Like;
    public int comments;
   // public Bitmap Profile;
    public String Profile;
    public ImageView nation;
    public ImageView Heart;
    public int chatbubble;
    public String Time;
    public int Position;
   // public Bitmap Picture;
    public String Picture;
    public String Picstr;

    public Info(){

    }

    public  String getName(){
        return Name;
    }

    public void setName(String name){
        Name = name;
    }


    public  String getPicstr(){
        return Picstr;
    }

    public void setPicstr(String picstr){
        this.Picstr = picstr;
    }



    public String getInsert(){
        return Insert;
    }

    public void setInsert(String insert){
        this.Insert = insert;
    }


    /*public Bitmap getProfile(){
        return Profile;
    }

    public void setProfile(Bitmap profile){
        Profile = profile;
    }*/


    public String getTime(){
        return Time;
    }

    public void setTime(String time){
        this.Time = time;
    }


    public int getLikeCount(){
        return LikeCount;
    }

    public void setLikeCount(int likecount){
        this.LikeCount = likecount;
    }


    public ImageView getHeart(){
        return Heart;
    }

    public void setHeart(ImageView heart){
        this.Heart = heart;
    }

    public int getPosition(){
        return Position;
    }

    public void setPosition(int position){
        this.Position = position;
    }

   /* public Bitmap getPicture(){
        return Picture;
    }

    public void setPicture(Bitmap picture){
        Picture = picture;
    }*/

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public Info(String name, String insert, String profile, ImageView heart, int likecount, String time, String picture, String picstr){
        Name = name;
        Insert = insert;
        Profile = profile;
        Heart = heart;
        LikeCount = likecount;
        Time = time;
        Picture = picture;
        Picstr = picstr;
    }
   /* public Info(){
        this.Like=0;
        this.Time= 0;
    }*/
}
