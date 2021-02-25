package com.example.firstapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

//import static com.example.firstapp.Register3Activity.na;

public class UpdateWriteActivity extends AppCompatActivity {


       /* final int REQ_CODE_SELECT_IMAGE=100;
        private static final int PICK_FROM_CAMERA = 1;
        private static final int PICK_FROM_GALLERY = 2;
        private ImageView pickimage;
        static String insert;

        static TextView writebutton;
        MyprofileActivity myprofileActivity;
        Register2Activity register2Activity;
        Story_Activity story_activity;
        static EditText edittext_writing;
        static Intent intent_02;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_write);

            writebutton = (TextView) findViewById(R.id.textView_insert);
            final ImageView pick_image = (ImageView) findViewById(R.id.pick_image);
            final ImageButton realbutton = (ImageButton) findViewById(R.id.realimageButton);
            pickimage = (ImageView) findViewById(R.id.contentimageView);

            writebutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    intent_02 = new Intent(com.example.firstapp.UpdateWriteActivity.this, Story_Activity.class);

                    edittext_writing = (EditText) findViewById(R.id.editText_writing);

                    insert = edittext_writing.getText().toString();

                    register2Activity.byteArray
                            = getIntent().getByteArrayExtra("image");
                    intent_02.getStringExtra("입력한 내용");
                    String name = intent_02.getStringExtra("입력한 이름");


                    //   Info info = new Info(name,insert, 76, null);






                    realbutton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, 11);

                            // Gallery 호출
                                                       *//*intent.setType("image/*");
                                                       intent.setAction(Intent.ACTION_GET_CONTENT);
                                                       // 잘라내기 셋팅
                                                       intent.putExtra("crop", "true");
                                                       intent.putExtra("aspectX", 0);
                                                       intent.putExtra("aspectY", 0);
                                                       intent.putExtra("outputX", 200);
                                                       intent.putExtra("outputY", 150);
                                                       try {
                                                           intent.putExtra("return-data", true);
                                                           startActivityForResult(Intent.createChooser(intent,
                                                                   "Complete action using"), PICK_FROM_GALLERY);
                                                       } catch (ActivityNotFoundException e) {
                                                           // Do nothing for now
                                                       }
*//*

                        }
                    });


                    Info info = new Info(na,insert, 1 , null,null);

                    intent_02.putExtra("result", insert);
                    setResult(RESULT_OK,intent_02);




                    //  story_activity.mArryList.add(0, info);
                    //  story_activity.mAdapter.notifyDataSetChanged();



                    finish();
                }
            });














        }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            // Check which request we're responding to
            if (requestCode == 11) {
                // Make sure the request was successful
                if (resultCode == RESULT_OK) {
                    try {
                        // 선택한 이미지에서 비트맵 생성
                        InputStream in = getContentResolver().openInputStream(data.getData());
                        Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();
                        // 이미지 표시
                        pickimage.setImageBitmap(img);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }




*/


    }
