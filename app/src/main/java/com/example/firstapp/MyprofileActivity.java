package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import static com.example.firstapp.Register2Activity.img_nation;
//import static com.example.firstapp.Register2Activity.photoURI;

//import static com.example.firstapp.Register2Activity.photoURI;

//import static com.example.firstapp.Register3Activity.ag;
//import static com.example.firstapp.Register3Activity.na;
//import static com.example.firstapp.Register3Activity.ur;

public class MyprofileActivity extends BaseActivity {

    private static final String TAG= "Myprofile Life Cycle";
    Register4Activity register4Activity;
    Register3Activity register3Activity;
    Register5Activity register5Activity;
    Register6Activity register6Activity;
    Register2Activity register2Activity;
    byte[] byteArray;
    ImageView myphoto;
    Bitmap bitmap;
    TextView mynamet, myaget, myStory, follower, following;
    String myname, myage, myprofile, mynation, mygender;
    Uri profileuri;
    SharedPreferences SPP;
    SharedPreferences.Editor myp;
    ImageView nation;
    String imagetemp, MyUid;
    static int profileedit;
    BitmapDrawable mnation, mgender;
    Button logoutButton, myeditButton;
    int followerCount, followingCount;
    ImageView gender;
    private FirebaseAuth firebaseAuth;
    TextView introduceButton, introduction;
    TextView genderAge;
    String genderText;

    ImageView Nav_Story,Nav_Chat,Nav_Noti,Nav_Profile;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
      //  BottomNavigationView navView = findViewById(R.id.nav_view);
       // navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MyUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        introduction = (TextView) findViewById(R.id.textView_introduction);
        introduceButton = (TextView) findViewById(R.id.introduceButton);
       // gender = (ImageView) findViewById(R.id.imageView_gender);
        myStory = (TextView) findViewById(R.id.MyProfileActivity_MyStory);
        follower = (TextView) findViewById(R.id.followercount);
        following = (TextView) findViewById(R.id.followingcount);

        genderAge = (TextView) findViewById(R.id.myProfile_genderage);

        mynamet = (TextView) findViewById(R.id.comment_name); // 이름 표시 텍스트뷰
      //  myaget = (TextView) findViewById(R.id.textView_age); // 나이 표시 텍스트뷰
        myphoto = (ImageView) findViewById(R.id.myphoto); // 프로필사진 표시 이미지뷰
        nation = (ImageView) findViewById(R.id.myNation); // 거주 국가 표시 이미지뷰
        logoutButton = (Button) findViewById(R.id.logoutbutton); // 로그 아웃 버튼
        myeditButton = (Button) findViewById(R.id.myeditbutton); // 정보 수정 버튼
        Intent intent_02 = getIntent();
        profileedit = 0;

        Nav_Story = (ImageView)findViewById(R.id.Nav_story);
        Nav_Chat = (ImageView)findViewById(R.id.Nav_chat);
        Nav_Noti =(ImageView)findViewById(R.id.Nav_bell);


        FirebaseDatabase.getInstance().getReference().child("users").child(MyUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo userInfo  = dataSnapshot.getValue(UserInfo.class);
                followerCount = userInfo.followerCount;
                followingCount = userInfo.followingCount;
                Log.d("유저", "followerCount" + followerCount + "");
                Log.d("유저", "followingCount" + followingCount + "");
                follower.setText(String.valueOf(followerCount));
                following.setText(String.valueOf(followingCount));
                introduction.setText(userInfo.introduction);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Nav_Story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyprofileActivity.this, Story_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        Nav_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyprofileActivity.this, ChatRoomActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Nav_Noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyprofileActivity.this, notificationActivity.class);
                startActivity(intent);
                finish();
            }
        });



        follower.setText(String.valueOf(followerCount));
        following.setText(String.valueOf(followingCount));


        introduceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyprofileActivity.this, IntroductionActivity.class);
                intent.putExtra("insert", introduction.getText().toString());
                startActivity(intent);
            }
        });


       /* String na = intent_02.getStringExtra("입력한 이름");
        String ag = intent_02.getStringExtra("입력한 나이");
        Uri ur = intent_02.getParcelableExtra("Uri");
        mynation.setImageDrawable(img_nation);
        Toast.makeText(MyprofileActivity.this, "Result: " + intent_02.getParcelableExtra("Uri"), Toast.LENGTH_SHORT).show();
        myProfile.setImageURI(ur);*/ // 쉐어드 사용

       /* final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();



      //  register2Activity.byteArray = byteArray;
        /* byteArray = getIntent().getByteArrayExtra("image");
         bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);*/


       //follower.setText(followerCount);




        /*Intent intent_01 = new Intent(MyprofileActivity.this,Story_Activity.class);

        intent_01.putExtra("입력한 이름", na);
        intent_01.putExtra("입력한 나이", ag);
      //  intent_01.putExtra("image", byteArray);
        intent_01.putExtra("Uri", ur);*/ //쉐어드 사용



        SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
        myname = SPP.getString("Name","");
        myage = SPP.getString("Age","");
        myprofile = SPP.getString("profileString", "");
        mynation = SPP.getString("Nation", "");
        mygender = SPP.getString("gender","");

        if(mygender.equals("여성")){
            genderText = "W";
        } else {
            genderText = "M";
        }

        genderAge.setText(genderText + ". " + myage);
      //  Toast.makeText(MyprofileActivity.this, SPP.getString("Name","") + SPP.getString("Age","") + SPP.getString("Image", ""), Toast.LENGTH_LONG).show();


    /*    // String to Bitmap
        try {
            byte [] encodeByte= Base64.decode(imagetemp,Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        } catch(Exception e) {
            e.getMessage();

        }

        myphoto.setImageBitmap(bitmap);*/



        profileuri = Uri.parse(myprofile);

        Glide.with(this).load(profileuri).apply(new RequestOptions().centerCrop()).into(myphoto);



        /*if(mygender.equals("여성")){

            mgender = (BitmapDrawable)getResources().getDrawable(R.drawable.femaleage);

            gender.setImageDrawable(mgender);

        } else if(mygender.equals("남성")){

            mgender = (BitmapDrawable)getResources().getDrawable(R.drawable.maleage);

            gender.setImageDrawable(mgender);
        }
*/


        if(mynation.equals("미국")){
            mnation = (BitmapDrawable)getResources().getDrawable(R.drawable.usaflag);
            // 미국 국기 사진 가져와서
            nation.setImageDrawable(mnation);// 미국 국기 사진 출력
        } else if(mynation.equals("중국")){

            mnation = (BitmapDrawable)getResources().getDrawable(R.drawable.chineseflag);
            //중국 국기 사진 가져와서
            nation.setImageDrawable(mnation);// 중국 사진 출력
        } else if(mynation.equals("한국")){

            mnation = (BitmapDrawable)getResources().getDrawable(R.drawable.koreaflag);
            //한국 국기 사진 가져와서
            nation.setImageDrawable(mnation);// 한국 사진 출력
        } else if(mynation.equals("일본")){

            mnation = (BitmapDrawable)getResources().getDrawable(R.drawable.japanflag);
            //일본 국기 사진 가져와서
            nation.setImageDrawable(mnation);// 일본 사진 출력

        }


      //  myphotouri = "image";
        /*byte[] byteArray = myphotouri.getBytes();
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        myphoto.setImageBitmap(bitmap);
        Log.d(TAG, myphoto + "");*/
     //   photouri = Uri.parse(myphotouri); // myphotouri String - > Uri로 변환 / 프로필 사진 uri






        mynamet.setText(String.valueOf(myname));
       // myaget.setText(String.valueOf(myage)); // 쉐어드 사용
       // myphoto.setImageURI(photouri);
       // nation.setImageDrawable(img_nation);

     //   Log.d(TAG, "photouri"+  photouri + "");

//        Picasso.with(this).load(myphotouri).into(myphoto);

      /*  if(photouri == null){
            Log.d(TAG, "myphoto is null");
        } else {
            Log.d(TAG, "myphoto has something");
        }*/


        following.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){ // 팔로잉 카운트 눌렀을 때

                Intent intent = new Intent(getApplicationContext(),FollowingActivity.class);
                startActivity(intent);
            }
        });

        follower.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){ // 팔로워 카운트 눌렀을 때

                Intent intent = new Intent(getApplicationContext(),FollowerActivity.class);
                startActivity(intent);
            }
        });

        myeditButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){ // 수정 버튼 눌렀을 때
                profileedit = 1;
                Intent intent = new Intent(getApplicationContext(),Register2Activity.class);
                startActivity(intent);
            }
        });



        myStory.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){ // My story 목록 보기

                Intent intent = new Intent(getApplicationContext(),MyStoryActivity.class);
                startActivity(intent);
            }
        });




        logoutButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){ // 로그아웃 버튼 눌렀을 때
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                myp = SPP.edit();
               // Boolean logout = SPP.getBoolean("SAVE_LOGIN",false);
              //  logout = false;
               // myp.putBoolean("SAVE_LOGIN", false);
                myp.clear();
                myp.commit();

                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                startActivity(intent);
                finish();
            }
        });







       /* if(register5Activity.motherlang == 1){
            textview_m.setText("EN");
        } else if(register5Activity.motherlang == 2){
            textview_m.setText("CH");
        } else if(register5Activity.motherlang == 3){
            textview_m.setText("KR");
        } else if(register5Activity.motherlang == 4){
            textview_m.setText("JP");
        }




        if(register6Activity.learn == 1){
            textView_l.setText("EN");
        } else if(register6Activity.learn == 2){
            textView_l.setText("CH");
        } else if(register6Activity.learn == 3){
            textView_l.setText("KR");
        } else if(register6Activity.learn== 4){
            textView_l.setText("JP");
        }




       if(register4Activity.level == 1){
           mylevel.setImageResource(R.drawable.introduction);
       } else if(register4Activity.level == 2){
           mylevel.setImageResource(R.drawable.beginning);
       } else if(register4Activity.level == 3){
           mylevel.setImageResource(R.drawable.intermediate);
       } else if(register4Activity.level == 4){
           mylevel.setImageResource(R.drawable.highlevel);
       } else {
           mylevel.setImageResource(R.drawable.nativelevel);
       }*/




    }


    /*private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_earth:
                    finish();
                    //  mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_chat:
                    finish();
                    //  mTextMessage.setText("Chat");
                    return true;
                case R.id.navigation_notifications:
                    finish();
                    //  mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_myprofile:
                    //  mTextMessage.setText("Profile");
                    Intent intent_02 = new Intent(getApplicationContext(),MyprofileActivity.class);

                   *//* intent_02.putExtra("입력한 이름", na);
                    intent_02.putExtra("입력한 나이", ag);
                    intent_02.putExtra("image", byteArray);
*//* // 쉐어드 사용
                    startActivity(intent_02);
                    return true;
            }
            return false;
        }
    };
*/

    /*public static String stringToJsonGSON(String json){
        Gson gson = new Gson();
        Info model = new gson.fromJson(json,Info.class);
        return model.getName();
    }
*/  // 좀 더 공부 필요...

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;    }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false; }    }, 2000);}

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
       // register3Activity.m.start();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
      //  register3Activity.m.pause();
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
      //  register3Activity.m.release();
    }

}
