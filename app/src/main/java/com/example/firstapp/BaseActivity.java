package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

  ImageView story, chat, bell, profile;



        public static ArrayList<Activity> actList = new ArrayList<Activity>();

        public void actFinish() {
            for (int i = 0; i < actList.size(); i++)
                actList.get(i).finish();
        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        story = (ImageView) findViewById(R.id.Nav_story);
        chat = (ImageView) findViewById(R.id.Nav_chat);
        bell = (ImageView) findViewById(R.id.Nav_bell);
        profile = (ImageView) findViewById(R.id.Nav_profile);



        story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                story.setImageResource(R.drawable.worldwidec);
                Intent intent = new Intent(BaseActivity.this, Story_Activity.class);
                startActivity(intent);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat.setImageResource(R.drawable.chatc);
                Intent intent = new Intent(BaseActivity.this, ChatRoomActivity.class);
                startActivity(intent);
            }
        });

        bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bell.setImageResource(R.drawable.alarmc);
                Intent intent = new Intent(BaseActivity.this, notificationActivity.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.setImageResource(R.drawable.userc);
                Intent intent = new Intent(BaseActivity.this, MyprofileActivity.class);
                startActivity(intent);
            }
        });

        actList.add(this);
        actList.add(this);
    }


    @Override
    protected void onDestroy() {
        //  Log.d(TAG, "onDestroy");
        super.onDestroy();
        actList.remove(this);
    }



}



