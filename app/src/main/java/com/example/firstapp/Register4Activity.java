package com.example.firstapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class Register4Activity extends AppCompatActivity {

    byte[] byteArray;
    static int level = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register4);


        TextView Introduction = (TextView) findViewById(R.id.textView_introduction);
        TextView intermediate = (TextView) findViewById(R.id.textView_intermediate);
        TextView beginning = (TextView) findViewById(R.id.textView_beginning);
        TextView high = (TextView) findViewById(R.id.textView_high);
        TextView nativelevel = (TextView) findViewById(R.id.textView_native);





        Introduction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent intent = new Intent(Register4Activity.this, MyprofileActivity.class);
        /*
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                float scale = (float) (1024/(float)bitmap.getWidth());
                int image_w = (int) (bitmap.getWidth() * scale);
                int image_h = (int) (bitmap.getHeight() * scale);
                Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
                resize.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                byte[] byteArray = stream.toByteArray();*/

                //intent.putExtra("image", byteArray);

             level = 1;
                finish();
            }


        });

        beginning.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Register4Activity.this, MyprofileActivity.class);
                level = 2;
                finish();
            }
        });


        intermediate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Register4Activity.this, MyprofileActivity.class);
                level = 3;
                finish();
            }
        });

        high.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Register4Activity.this, MyprofileActivity.class);
                level = 4;
                finish();
            }
        });

        nativelevel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Register4Activity.this, MyprofileActivity.class);
                level = 5;
                finish();
            }
        });




    }
}