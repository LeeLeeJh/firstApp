package com.example.firstapp;

public class ChatRoomInfo {

    String userName;
    String profileString;
    String uid;
    String time;
    String nation;
    String content;




    public ChatRoomInfo(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String Uid) {
        uid = Uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        userName = username;
    }

    public String getProfileString() {
        return profileString;
    }

    public void setProfileString(String profileString) {
        this.profileString = profileString;
    }

    public ChatRoomInfo(String username, String profilestring, String Uid, String time, String nation, String content){
        profileString = profilestring;
        userName = username;
        this.uid = Uid;
        this.time = time;
        this.nation = nation;
        this.content = content;
    }





}
