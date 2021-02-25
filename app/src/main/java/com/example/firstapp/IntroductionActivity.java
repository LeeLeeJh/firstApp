package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class IntroductionActivity extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String introductionText;
    TextView completeButton;
    EditText introduction;
    SharedPreferences SPP;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
        editor = SPP.edit();


        completeButton = (TextView) findViewById(R.id.introduction_button);
        introduction = (EditText) findViewById(R.id.introduction_text);

        Intent intent = getIntent();
        introductionText = intent.getStringExtra("insert");
        introduction.setText(introductionText);


        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introductionText = introduction.getText().toString();
                Log.d("자기소개", "introductionText1 " + introductionText);
                Log.d("자기소개", "완료");
                editor.putString("introduction",introductionText);
                editor.commit();
                introduction();
                finish();
            }
        });

    } // end of create

    void introduction(){

        Map<String,Object> map = new HashMap<>();
        map.put("introduction", introductionText);
        Log.d("자기소개", "introductionText" + introductionText);
        mDatabase.child("users").child(MyUID).updateChildren(map);

    }



}
