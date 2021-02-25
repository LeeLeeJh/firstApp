package com.example.firstapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;

import static com.example.firstapp.Register2Activity.bundle;
//import static com.example.firstapp.Register2Activity.photoURI;


public class Register3Activity extends BaseActivity {
    MainActivity mainActivity;
    Register2Activity register2Activity;
    TextView textView_na, textView_ag;
    String na, ag;
    Uri ur;
    ImageView imageview;
    String name, age;
    EditText text_na, text_ag;
    static byte[] byteArray;
    static Bitmap bitmap;
    private static final String TAG= "Register3 Life Cycle";
    static MediaPlayer m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);


        /*text_na = (EditText) findViewById(R.id.nameText);
        text_ag = (EditText) findViewById(R.id.ageText);
        textView_na = (TextView) findViewById(R.id.textView_name);
        textView_ag = (TextView) findViewById(R.id.textView_age);
*/
        Intent intent_02 = getIntent();

        Bundle extras = getIntent().getExtras();


       /* byteArray = getIntent().getByteArrayExtra("image");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);*/
     //   imageview.setImageBitmap(bitmap);    // 이미지 비트맵으로 전달


       /*ur = intent_02.getParcelableExtra("Uri");
       na = intent_02.getStringExtra("입력한 이름");
       ag = intent_02.getStringExtra("입력한 나이");*/ // 쉐어드 사용
    //   name = text_na.getText().toString();
    //   age = text_ag.getText().toString();
       // Toast.makeText(Register3Activity.this, "Result: " + intent_02.getParcelableExtra("Uri"), Toast.LENGTH_SHORT).show();

        Button completeButton = (Button) findViewById(R.id.completebutton);
        Button levelButton = (Button) findViewById(R.id.levelbutton);
        Button mothertounguebutton = (Button) findViewById(R.id.mothertounguebutton);
        Button learnButton = (Button) findViewById(R.id.learnbutton);

      //  ImageView imageView = (ImageView) findViewById(R.id.imageView22);
      //  imageView.setImageURI(ur);


        mothertounguebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),Register5Activity.class);
                startActivity(intent);

                /*switch (v.getId()){
                    case R.id.mothertounguebutton :
                        Register5Activity dialog = new Register5Activity(Register3Activity.this);
                        dialog.setTitle("motherlanguage");
                        dialog.show();
                        break;*/

               // }

            //    Intent intent_03 = new Intent(getApplicationContext(),Register5Activity.class);
            //    startActivity(intent_03);
            }
        });

        // 완료 버튼 클릭
        completeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
              //  Intent intent_02 = new Intent(getApplicationContext(),Story_Activity.class);
                Intent intent_02 = new Intent(Register3Activity.this,Story_Activity.class);
                /*Context c = v.getContext();
                m = MediaPlayer.create(c, R.raw.track1);
                m.start();*/
               /* text_na = (EditText) findViewById(R.id.nameText);
                text_ag = (EditText) findViewById(R.id.ageText);

                name = text_na.getText().toString();
                age = text_ag.getText().toString();

*/
               /* intent_02.putExtra("입력한 이름", na);
                intent_02.putExtra("입력한 나이", ag);
            //  intent_02.putExtra("image", byteArray);
                intent_02.putExtra("Uri", ur);
*/ //쉐어드 사용

                startActivity(intent_02);
                finish();
              //  actFinish(); // Register 2,3 모두 종료
            }
        });

        levelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),Register4Activity.class);
                startActivity(intent);
            }
        });

        learnButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),Register6Activity.class);
                startActivity(intent);
            }
        });
    }



    public void onBackPressed() {
        onRestart();
        Register3Activity.super.onBackPressed();
    }





    @Override
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

}
