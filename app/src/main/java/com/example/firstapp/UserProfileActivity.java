package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.firstapp.model.ChatModel;
import com.example.firstapp.model.PostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserProfileActivity extends AppCompatActivity {

    Button messageBt, followBt;
    TextView textView_name;
    private RecyclerView recyclerView;
    String name, profileString;
    private UserInfo destinationUserinfo;
    ImageView imageView;
    PostModel userPost;
    UserProfileAdapter mAdapter;
    static ArrayList<PostModel> uArrayList;
    List<UserInfo> followerCount;
    List<String> followerUid;
    private LinearLayoutManager mLinearLayoutManager;
    FirebaseDatabase firebaseDatabase;
    String MyUid, key;
    SharedPreferences SPP;
    SharedPreferences.Editor editor;
    static String destinationUid;
    Boolean follow = false;
    private DatabaseReference mDatabase;
    String myname,myprofileString,mynation;
    int follower, following;
    int position;
    String time, nation;
    ImageView usernation;
    TextView userintroduction, genderAge;
    String genderText;
    static ArrayList<String> keyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        followBt = (Button) findViewById(R.id.button_follow);

        MyUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        name = getIntent().getStringExtra("name");
        profileString = getIntent().getStringExtra("profileString");
        destinationUid = getIntent().getStringExtra("destinationUid");  // 글 작성자 UID
        nation = getIntent().getStringExtra("nation");
        Log.d("알림", "destinationUid" + destinationUid + "");

        recyclerView = (RecyclerView) findViewById(R.id.User_Story);
       /* recyclerView.setLayoutManager(new LinearLayoutManager(Story_Activity.this));
        recyclerView.setAdapter(new CustomViewAdapter());*/

        SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
        editor =  SPP.edit();


        myname = SPP.getString("Name","");
        myprofileString = SPP.getString("profileString","");
        mynation = SPP.getString("Nation", "");


        mLinearLayoutManager = new LinearLayoutManager(UserProfileActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserProfileActivity.this));
        uArrayList = new ArrayList<>();
        followerCount = new ArrayList<>();
        followerUid = new ArrayList<>();

        userintroduction = (TextView) findViewById(R.id.User_introduction);
        genderAge = (TextView) findViewById(R.id.userProfile_genderage);
        // 유저 POST 정보 불러오기 > 리사이클러뷰 삽입
       /* FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).child("mposts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uArrayList.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                    uArrayList.add(0,snapshot.getValue(PostModel.class));

                    Log.d("알림1", "UsermArray" + uArrayList + "");
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        FirebaseDatabase.getInstance().getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uArrayList.clear();
                keyList.clear();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    PostModel postModel = item.getValue(PostModel.class);

                    if(postModel.uid.equals(destinationUid)){
                        uArrayList.add(0,item.getValue(PostModel.class));
                        String key = item.getKey();
                        keyList.add(0,key);
                        Log.d("알림1", "UsermArray" + uArrayList + "");

                    }

                }
                // 메세지 갱신
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                userintroduction.setText(userInfo.introduction);
                if(userInfo.gender.equals("여성")){
                    genderText = "W";
                } else {
                    genderText = "M";
                }
                genderAge.setText(genderText + ". " + userInfo.age);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).child("follower").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    followerUid.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    followerUid.add(key);

                    if(followerUid.contains(MyUid)) { // 내 UID가 저장되어 있을 경우 , 팔로우를 이미 하고 있는 경우
                        int color = Color.parseColor("#DAD9FF");
                        followBt.setBackgroundColor(color);
                        followBt.setText("팔로잉");
                        Log.d("유저", "팔로잉" +  followBt.getText() + "");

                    } else {
                        followBt.setText("팔로우");
                        Log.d("유저", "팔로우" +  followBt.getText() + "");

                    } // 속도가 느리다... 파이어베이스 ㅂㄷㅂㄷ UID가져오는 속도보다 setTExt속도가 빠름
                    Log.d("유저", "followerUid" + followerUid + "");
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        mAdapter = new UserProfileAdapter(UserProfileActivity.this, uArrayList);
        recyclerView.setAdapter(mAdapter); // CustomAdapter 연결


        // 리사이클러뷰 아이템 선으로 구분하기
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
        mLinearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

       /* RecyclerDecoration spaceDecoration = new RecyclerDecoration(150);
        recyclerView.addItemDecoration(spaceDecoration);*/



       /* String format = new String("yyyy-MM-dd"); // 현재 시간 표시
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
        time = sdf.format(new Date());*/




       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // setContentView(R.layout.activity_user_profile);

        messageBt = (Button) findViewById(R.id.button_message);
        imageView = (ImageView) findViewById(R.id.imageView_userprofile);
        textView_name = (TextView) findViewById(R.id.userprofile_name);
        usernation = (ImageView) findViewById(R.id.userStory_nation);




         /*FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).child("mposts").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                destinationUserinfo = dataSnapshot.getValue(UserInfo.class);
                Log.d("알림", "destinationUserinfo" + destinationUserinfo + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/


        if (nation.equals("미국")) {

            usernation.setImageResource(R.drawable.usaflag);

            // 미국 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 미국 국기 사진 출력
        } else if (nation.equals("중국")) {


            usernation.setImageResource(R.drawable.chineseflag);


            //중국 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 중국 사진 출력
        } else if (nation.equals("한국")) {

            usernation.setImageResource(R.drawable.koreaflag);


            //한국 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 한국 사진 출력
        } else if (nation.equals("일본")) {

            usernation.setImageResource(R.drawable.japanflag);
        }





        textView_name.setText(name);
        Glide.with(this)
                .load(profileString)
                .apply(new RequestOptions().circleCrop().centerCrop())
                .into(imageView);

        Log.d("알림2", "UsermArray" + uArrayList + "");

       messageBt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              /* Intent intent2 = new Intent(UserProfileActivity.this, ChatRoomActivity.class);
               intent2.putExtra("destinationUid",destinationUid);*/

               chatroom(name,profileString,destinationUid);
               chatroom2(myname,myprofileString,MyUid);
              // Chat에 추가!!!

               Intent intent = new Intent(UserProfileActivity.this, MessageActivity.class);
               intent.putExtra("destinationUid", destinationUid);
               intent.putExtra("myname",myname);
               intent.putExtra("myprofile",myprofileString);
               intent.putExtra("mynation",mynation);
               intent.putExtra("name",name);
               intent.putExtra("profile",profileString);
               intent.putExtra("nation",nation);
             //  intent.putExtra("roomkey",key);
               startActivity(intent);
           }
       });



       // key = mDatabase.child("posts").push().getKey();




        FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo destinationuserInfo  = dataSnapshot.getValue(UserInfo.class);
                follower = destinationuserInfo.followerCount;

                Log.d("유저", "follower" + follower + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(MyUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo userInfo  = dataSnapshot.getValue(UserInfo.class);
                following = userInfo.followingCount;

                Log.d("유저", "following" + following + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        followBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    if(followerUid.contains(MyUid)) { // uid로 변경



                        followBt.setText("팔로우");
                        int color = Color.parseColor("#FFFFFF");
                        followBt.setBackgroundColor(color);
                        Log.d("유저", "언팔로우했다");
                        Log.d("유저", "follower" + follower);
                        follower = follower - 1;
                        Log.d("유저", "follower" + follower);
                        following = following - 1;
                        mDatabase.child("notification").child(destinationUid).child("follower").child(MyUid).removeValue();
                        mDatabase.child("users").child(MyUid).child("following").child(destinationUid).removeValue();
                        mDatabase.child("users").child(MyUid).child("followingCount").setValue(following);

                        mDatabase.child("users").child(destinationUid).child("follower").child(MyUid).removeValue();
                        mDatabase.child("users").child(destinationUid).child("followerCount").setValue(follower);

                    } else {
                        followBt.setText("팔로잉");
                        int color = Color.parseColor("#DAD9FF");
                        followBt.setBackgroundColor(color);
                        Log.d("유저", "팔로우했다");
                        String content = myname + "님이 회원님을 팔로우합니다.";
                        long nowtime = System.currentTimeMillis();
                        time = String.valueOf(nowtime);




                        ChatRoomInfo chatRoomInfo = new ChatRoomInfo(myname, myprofileString, MyUid,time,mynation,content);
                        mDatabase.child("notification").child(destinationUid).push().setValue(chatRoomInfo);
                        follower = follower + 1;
                        following = following + 1;

                            ChatRoomInfo chatRoomInfo2 = new ChatRoomInfo(name, profileString, destinationUid,time,nation,null);
                            mDatabase.child("users").child(MyUid).child("following").child(destinationUid).setValue(chatRoomInfo2);
                            mDatabase.child("users").child(MyUid).child("followingCount").setValue(following);

                            ChatRoomInfo chatRoomInfo3 = new ChatRoomInfo(myname, myprofileString, MyUid,time,mynation,null);
                            mDatabase.child("users").child(destinationUid).child("follower").child(MyUid).setValue(chatRoomInfo3);
                            mDatabase.child("users").child(destinationUid).child("followerCount").setValue(follower);

                            editor.putInt("followerCount", follower);
                            editor.commit();


                    }




            }
        });




    }


    private void chatroom(String inputname,String photostring,String uid){
        final ChatRoomInfo chatRoomInfo = new ChatRoomInfo(name,profileString,destinationUid,null,mynation,null);
        mDatabase.child("chat").child(MyUid).child(destinationUid).setValue(chatRoomInfo);
        mDatabase.child("chat").child(destinationUid).child(destinationUid).setValue(chatRoomInfo);

    }

    private void chatroom2(String inputname,String photostring,String uid){
        final ChatRoomInfo chatRoomInfo = new ChatRoomInfo(myname,myprofileString,MyUid,null,mynation,null);
        mDatabase.child("chat").child(MyUid).child(MyUid).setValue(chatRoomInfo);
        mDatabase.child("chat").child(destinationUid).child(MyUid).setValue(chatRoomInfo);

    }

    @Override
    protected void onStart() {
        super.onStart();




    }



}
