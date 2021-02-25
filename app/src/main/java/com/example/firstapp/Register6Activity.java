package com.example.firstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class Register6Activity extends AppCompatActivity {

    static int learn = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register6);



        TextView learn_e = (TextView) findViewById(R.id.textView_le);
        TextView learn_c = (TextView) findViewById(R.id.textView_lc);
        TextView learn_k = (TextView) findViewById(R.id.textView_lk);
        TextView learn_j= (TextView) findViewById(R.id.textView_lj);



        learn_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register6Activity.this, MyprofileActivity.class);
                learn = 1;
                finish();
            }

        });

        learn_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register6Activity.this, MyprofileActivity.class);
                learn = 2;
                finish();
            }

        });

        learn_k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register6Activity.this, MyprofileActivity.class);
                learn = 3;
                finish();
            }

        });

        learn_j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register6Activity.this, MyprofileActivity.class);
                learn = 4;
                finish();
            }

        });
    }
}
