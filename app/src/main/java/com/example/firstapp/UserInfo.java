package com.example.firstapp;

import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;






//파이어베이스 사용 가능한 유형
//String
//Long
//Double
//Boolean
//Map<String, Object>
//List<Object>


public class UserInfo {

    public String uid;
    public String name;
    public String age;
    public String gender;
    public String downloadUrl;
    public int followerCount;
    public int followingCount;
    public String nation; // select nation
    public String introduction;
    public String pushToken;


    public UserInfo(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }




    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("age", age);
        result.put("gender", gender);
        result.put("photostring", downloadUrl);
        result.put("nation", nation);
        result.put("introduction", introduction);

        return result;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String Uid) {
        Uid = uid;
    }

 //   public String getPwd() {
    //    return Pwd;
 //   }

  //  public void setPwd(String pwd) {
 //       Pwd = pwd;
 //   }


    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

/* public Uri getPhotoUri() {
        return photouri;
    }

    public void setPhotoUri(Uri photouri) {
        this.photouri = photouri;
    }*/


    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public UserInfo(String uid, String name, String age, String gender, String downloadUrl, String nation, int followerCount, int followingCount, String introduction){
        this.uid = uid;
       // this.Pwd = pwd;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.downloadUrl = downloadUrl;
        this.nation = nation;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.introduction = introduction;
        this.pushToken = pushToken;
    }



}
