package com.example.firstapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Register5Activity extends AppCompatActivity {

    static int motherlang = 1;
  //  private static final String TAG= "Register5 Life Cycle";

  //  public Register5Activity(Context context) {
     //   super(context);
   // }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register5);

        ImageView n_english = (ImageView) findViewById(R.id.imageView_english);
        ImageView n_chinese = (ImageView) findViewById(R.id.imageView_chinese);
        ImageView n_korean = (ImageView) findViewById(R.id.imageView_korean);
        ImageView n_japanese = (ImageView) findViewById(R.id.imageView_japanese);



      n_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register5Activity.this, MyprofileActivity.class);
                motherlang = 1;
                finish();
            }

    });

        n_chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register5Activity.this, MyprofileActivity.class);
                motherlang = 2;
                finish();

            }

        });

        n_korean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register5Activity.this, MyprofileActivity.class);
                motherlang = 3;
                finish();
            }

        });

        n_japanese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register5Activity.this, MyprofileActivity.class);
                motherlang = 4;
                finish();
            }

        });

}














   /* @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

*/

}
