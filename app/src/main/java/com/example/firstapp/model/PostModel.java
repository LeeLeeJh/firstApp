package com.example.firstapp.model;

import com.google.firebase.database.Exclude;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class PostModel {

    public String name;
    public String profileString;
    public String uid;
    public String contentWriting;
    public String image;
    public String postNumber;
    public String time;
    public String nation;
 //   public int likeCount;
    public int commentCount = 0;
    public int likeCount = 0;


    public PostModel(){

    }

    public Map<String, Boolean> Likes = new HashMap<>();


    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("profileString", profileString);
        result.put("contentWriting", contentWriting);
      //  result.put("image", image);
        result.put("time", time);


        return result;
    }

   /* public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }*/

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileString() {
        return profileString;
    }

    public void setProfileString(String profileString) {
        this.profileString = profileString;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContentWriting() {
        return contentWriting;
    }

    public void setContentWriting(String contentWriting) {
        this.contentWriting = contentWriting;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPostNumber() {
        return postNumber;
    }

    public void setPostNumber(String postNumber) {
        this.postNumber = postNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }





    public PostModel(String uid, String name, String profileString, String contentWriting, String image, String time, String postNumber, String nation){
        this.uid = uid;
        this.name = name;
        this.profileString = profileString;
        this.contentWriting = contentWriting;
        this.image = image;
        this.time = time;
        this.postNumber = postNumber;
        this.nation = nation;
        this.commentCount = commentCount;


    }





}
