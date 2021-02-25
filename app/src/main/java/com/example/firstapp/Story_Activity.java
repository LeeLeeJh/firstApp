package com.example.firstapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.firstapp.model.PostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static com.example.firstapp.MyprofileActivity.myProfile;
//import static com.example.firstapp.Register2Activity.photoURI;
//import static com.example.firstapp.Register3Activity.ag;
//import static com.example.firstapp.Register3Activity.na;
//import static com.example.firstapp.Register3Activity.ur;
//import static com.example.firstapp.Register2Activity.photoURI;
import static com.example.firstapp.RegisterActivity.Id;
import static com.example.firstapp.RegisterActivity.Pwd;
import static com.example.firstapp.WriteActivity.img;
import static com.example.firstapp.WriteActivity.insert;

import static com.example.firstapp.WriteActivity.temp;
import static com.example.firstapp.WriteActivity.time;
//import static com.example.firstapp.WriteActivity.intent_02;

public class Story_Activity extends AppCompatActivity {
    //   private TextView mTextMessage;
    WriteActivity writeActivity;
    MyprofileActivity myprofileActivity;
    Register2Activity register2Activity;
  //  static String insert;
  //  static ArrayList<Info> mArryList;
    static CustomAdapter mAdapter;
    private RecyclerView recyclerView;
    static TextView textView_insert;
    private int count = 0;
    static int Heartclick = 0;
    static Intent intent;
    Bitmap bitmap;
    byte[] byteArray;
    static ImageView profileimage;
    static TextView textView_time;
    private Context mContext;
    static ArrayList<PostModel> mArrayList;
    private LinearLayoutManager mLinearLayoutManager;
   // static ArrayList mArryList = new ArrayList<Info>();
  //  static ArrayList mArrayList = new ArrayList<>();
    static PostModel info;
    static int position;
    static int Remove = 0;
    static int rcode = 1;
    SharedPreferences SPP;
    SharedPreferences.Editor storyeditor;
    static int autologin;

    static String myname, myage, profilestring,mynation;
    Uri photouri, addpicture;
    private static final String TAG= "Story Activity";
    JSONArray jsonArray;
    String[] getName;
    int i;
    String imagetemp, Picturest,  CurrentT, alreadyInfo;
    Gson gson;
    String data, Picture, Picturestr;
    Bitmap Picturebitmap;
    static BitmapDrawable nation;


    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    DataSnapshot dataSnapshot;
    static String uid;
    ImageView mheart;
    static List<String> LikeUid = new ArrayList<>();

    static List<String> keyList = new ArrayList<>();
    boolean doubleBackToExitPressedOnce = false;
    static PostModel postModel;

    ImageView mp;
    ImageView chat;
    ImageView bell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_);
       // BottomNavigationView navView = findViewById(R.id.nav_view);
        //  mTextMessage = findViewById(R.id.message);
     //   navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mheart = (ImageView) findViewById(R.id.imageView_heart);
        alreadyInfo = "name";

        SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
        mynation = SPP.getString("Nation","");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       // uid = firebaseUser.getUid();



      /*  FirebaseDatabase.getInstance().getReference().child("posts").child("like").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LikeUid.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                    // key = snapshot.getKey();
                    LikeUid.add(snapshot.getValue(PostModel.));
                    Log.d("알림", "LikeUid" + LikeUid);
                }

                if(LikeUid.contains(uid)) { // 내 UID가 저장되어 있을 경우 , 좋아요를 이미 누른 경우
                    Log.d("알림", "좋아요 UID 이뜨아");
                    mheart.setImageResource(R.drawable.redheart);

                } else {
                    Log.d("알림", "좋아요 UID 없뜨아");
                    mheart.setImageResource(R.drawable.heart);
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/




        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
       /* recyclerView.setLayoutManager(new LinearLayoutManager(Story_Activity.this));
        recyclerView.setAdapter(new CustomViewAdapter());*/




        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(Story_Activity.this));
        mArrayList = new ArrayList<>();



        mAdapter = new CustomAdapter(Story_Activity.this, mArrayList);
        recyclerView.setAdapter(mAdapter); // CustomAdapter 연결


        // 리사이클러뷰 아이템 선으로 구분하기
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
        mLinearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);



        FirebaseDatabase.getInstance().getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mArrayList.clear();
                keyList.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                    postModel = snapshot.getValue(PostModel.class);
                    String key = snapshot.getKey();
                    mArrayList.add(0,postModel);
                    Log.d(TAG, "mArrayList" + mArrayList + "");
                    keyList.add(0,key);

                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });










        SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
        storyeditor = SPP.edit(); // 쉐어드 프리퍼런스 에디터
        myname = SPP.getString("Name","");
        profilestring = SPP.getString("profileString","");











       /* recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //   PostModel info = mArrayList.get(position);
                //   Toast.makeText(getApplicationContext(),  info.getInsert()+' '+info.getName()+' '+info.getTime(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getBaseContext(), InnerStoryActivity.class);
                intent.putExtra("name", mArrayList.get(position).name);
                intent.putExtra("insert", mArrayList.get(position).contentWriting);
                intent.putExtra("time", mArrayList.get(position).time);
                intent.putExtra("profileURL", mArrayList.get(position).profileString);
                intent.putExtra("commentkey", keyList.get(position));
                //  intent.putExtra("position", position);
                startActivityForResult(intent, 4000);


            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));*/

       // 어댑터로 옮김/게시글 클릭이벤트






        /*FirebaseDatabase.getInstance().getReference().child("chat").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mArrayList.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                    mArrayList.add(snapshot.getValue(PostModel.class));

                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/



        /*
        imagetemp = SPP.getString("Image", "");

        byte [] encodeByte = Base64.decode(imagetemp,Base64.DEFAULT);
        bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length); // 프로필 사진 비트맵

        myname = SPP.getString("Name",""); // name 쉐어드에서 불러오기
        myage = SPP.getString("Age",""); // age 쉐어드에서 불러오기
      //  myphotouri = SPP.getString("Image", "");
        mynation = SPP.getString("Nation", ""); // 거주 국가 쉐어드에서 불러오기


        String strJson = SPP.getString("MyInsert", ""); // 글 목록 불러오기       Toast.makeText(Story_Activity.this, strJson + "", Toast.LENGTH_LONG).show();
       // Toast.makeText(Story_Activity.this, strJson + "", Toast.LENGTH_LONG).show();
        Log.d(TAG, "SPPJSON" + strJson + "");
        if(strJson != "false") {
            try {
                JSONArray response = new JSONArray(strJson);
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    insert = jsonObject.getString("insert");
                    data = jsonObject.getString("picture");
                    String timeR = jsonObject.getString("time");
                    Log.d(TAG, "JSONTime" + timeR + "");
                 //   if(timeR != null)
                    byte [] PictureByte= Base64.decode(data,Base64.DEFAULT);
                    Picturebitmap= BitmapFactory.decodeByteArray(PictureByte, 0, PictureByte.length);
                 //   info = new Info(myname, insert, bitmap, null, Heartclick, timeR, Picturebitmap, data);
                    info = new Info(myname, insert, null, null, Heartclick, timeR, null, data);
                    mArrayList.add(info);
                    Log.d(TAG, "SPPJSON" + SPP.getAll() + "");
                }

               *//* mAdapter = new CustomAdapter(Story_Activity.this, mArrayList);
                mRecyclerview.setAdapter(mAdapter);*//*


            } catch (JSONException e) {
                e.printStackTrace();
            }
            mAdapter.notifyDataSetChanged();
       }
*/















      /*  if(mynation.equals("미국")){

            nation = (BitmapDrawable)getResources().getDrawable(R.drawable.usaflag);
            // 미국 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 미국 국기 사진 출력
        } else if(mynation.equals("중국")){

            nation = (BitmapDrawable)getResources().getDrawable(R.drawable.chineseflag);
            //중국 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 중국 사진 출력
        } else if(mynation.equals("한국")){

            nation = (BitmapDrawable)getResources().getDrawable(R.drawable.koreaflag);
            //한국 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 한국 사진 출력
        } else if(mynation.equals("일본")){

            nation = (BitmapDrawable)getResources().getDrawable(R.drawable.japanflag);
            //일본 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 일본 사진 출력

        }*/  // 잠깐 뺄게요 파이어베이스 테스트중









     //   loadArrayList(Story_Activity.this);
          // registerForContextMenu();
//        navView.setOnCreateContextMenuListener(this); 컨텍스트 메뉴 띄우기

      //  onPostExecute(mArrayList);
//        Toast.makeText(Story_Activity.this, getName[i] + "", Toast.LENGTH_SHORT).show();

       /* byteArray = getIntent().getByteArrayExtra("image");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);*/
       // byteArray 위로 옮겼습니다.





      /*  SPP = PreferenceManager.getDefaultSharedPreferences(Story_Activity.this);
        jsonstr = SPP.getString("Item","");
        Toast.makeText(Story_Activity.this, jsonstr + "", Toast.LENGTH_LONG).show();
        Log.d(TAG, jsonstr + "");*/
     //   Log.d(TAG, "Myobject" + SPP.getString("MyObject","") + "");

     /* String jsonString;
        jsonString = jsonString.replace("nameValuePairs(","");
        jsonString = jsonString.replace(")", "");
*//*
        try{
            JSONArray re = new JSONArray(strJson);

            for(int i = 0; i < re.length(); i++){
                JSONObject resinfo = re.getJSONObject(i);

                String name = resinfo.getString("name");
                Log.d(TAG, "name" + name + "");
                String insert = resinfo.getString("insert");
                String profile = resinfo.getString("profile");

                byte[] decodedByteArray = Base64.decode(profile, Base64.NO_WRAP);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);


                info = new Info(name, insert, decodedBitmap, null, Heartclick, 0, addpicture);

                *//*HashMap<String, String> resinfoMap = new HashMap<String, String>();
                resinfoMap.put("name", name);
                resinfoMap.put("insert", insert);
                resinfoMap.put("profile", profile);*//*


                info.setName(name);
                info.setInsert(insert);
                info.setProfile(decodedBitmap);
                //  info.setPicture(addpicture);



                mArrayList.add(info);
            }

            mAdapter = new CustomAdapter(Story_Activity.this, mArrayList);
            mRecyclerview.setAdapter(mAdapter);



        } catch (JSONException e){
            Log.d(TAG, e.toString());
        }*/















// 위로 옮겼음
      /*  SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
        storyeditor = SPP.edit();


        myname = SPP.getString("Name","");
        myage = SPP.getString("Age","");
        myphotouri = SPP.getString("Image", "");
        mynation = SPP.getString("Nation", "");
     //   Toast.makeText(Story_Activity.this, SPP.getString("Name","") + SPP.getString("Age","") , Toast.LENGTH_LONG).show();
     //   Toast.makeText(Story_Activity.this, SPP.getAll() + "", Toast.LENGTH_LONG).show();

     // storyeditor.clear();


        photouri = Uri.parse(myphotouri);*/
     //   Log.d(TAG, "photouri"+ photouri + "");


        //  mArryList.add(new Info("Ken","Hello",R.drawable.korean,null,null));
         //   mArryList.add(new Info("Nana","Oh My God!",R.drawable.myprofile,null,null));

            /*TextView textView_na = (TextView) findViewById(R.id.textView_name);
            TextView textView_ag = (TextView) findViewById(R.id.textView_age);
            ImageView myProfile = (ImageView) findViewById(R.id.myProfile);
            ImageView mylevel = (ImageView) findViewById(R.id.mylevel);
            Intent intent_02 = getIntent();

            String na = intent_02.getStringExtra("입력한 이름");
            String ag = intent_02.getStringExtra("입력한 나이");

           register2Activity.byteArray = getIntent().getByteArrayExtra("image");*/
          //  Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
           // myProfile.setImageBitmap(bitmap);


         /*   final TextView textView_name = (TextView) findViewById(R.id.comment_name);
              ImageView mnation = (ImageView)findViewById(R.id.myNation);
          //  ImageView profileimage = (ImageView) findViewById(R.id.myprofile);
            textView_insert = (TextView) findViewById(R.id.textView_insert);
            textView_time = (TextView) findViewById(R.id.textView_time);
          //  ImageView myview = (ImageView) findViewById(R.id.imageView21);*/ // 원래 있었음 6/7 7:00


            /*Intent intent_02 = getIntent();
            Bundle extras = getIntent().getExtras();*/

          //  final String na = intent_02.getStringExtra("입력한 이름");
          //  String ag = intent_02.getStringExtra("입력한 나이");
          //  String in = intent_02.getStringExtra("입력한 내용");
          //  uu = intent_02.getParcelableExtra("Uri");
        // 원래 있던 것 6/5 9:52

          /*  register2Activity.byteArray
                    = getIntent().getByteArrayExtra("image");
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);*/

//            textView_name.setText(String.valueOf(na));

         //   myProfile.setImageBitmap(bitmap);


          //  myview.setImageURI(ur);

          //  Toast.makeText(Story_Activity.this, "Result: " + ur, Toast.LENGTH_SHORT).show();

            /*ImageView clickheart = (ImageView) findViewById(R.id.imageView_heart);
            clickheart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                   TextView textView_heart = (TextView) findViewById(R.id.textView_heart);

                   textView_heart.setText(String.valueOf(++Heartclick));
                }
            });*/












        //   String format = new String("yyyyMMddHHmmss");
      /*  String format = new String("HH : mm");
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
        textView_time.setText(sdf.format(new Date()));*/
         //   mAdapter.setOnnItemClickListener


        mp = (ImageView) findViewById(R.id.Nav_profile);
        mp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              //  mp.setImageResource(R.drawable.userc);
                Intent intent = new Intent(Story_Activity.this, MyprofileActivity.class);
                startActivity(intent);
                finish();

            };

        });



        chat = (ImageView) findViewById(R.id.Nav_chat);
        chat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              //  chat.setImageResource(R.drawable.chatc);
                Intent intent = new Intent(Story_Activity.this, ChatRoomActivity.class);
                startActivity(intent);
                finish();

            };

        });

        bell = (ImageView) findViewById(R.id.Nav_bell);
        bell.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              //  bell.setImageResource(R.drawable.alarmc);
                Intent intent = new Intent(Story_Activity.this, notificationActivity.class);
                startActivity(intent);
                finish();

            };

        });



        ImageView writebutton = (ImageView) findViewById(R.id.writeButton);
        final ImageView heart = (ImageView) findViewById(R.id.imageView_heart);

        writebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivityForResult(intent, 3000);
             //   Toast.makeText(Story_Activity.this, "Result: " + info, Toast.LENGTH_SHORT).show();

                View view = LayoutInflater.from(Story_Activity.this)
                        .inflate(R.layout.activity_write, null, false);
               // builder.setView(view);

                final TextView textView_time = (TextView) findViewById(R.id.textView_time);
                final TextView textView_insert = (TextView) findViewById(R.id.textView_insert);
                final EditText edittext_writing = (EditText) findViewById(R.id.editText_writing);
//

                finish();

               /* mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview_main_list);
                mRecyclerview.setLayoutManager(new LinearLayoutManager(Story_Activity.this));

                *//*mArryList.add(new Info("Ken","Hello",R.drawable.korean,null,null));
                mArryList.add(new Info("Nana","Oh My God!",R.drawable.myprofile,null,null));*//*


                mAdapter = new CustomAdapter(Story_Activity.this, mArryList);
                mRecyclerview.setAdapter(mAdapter);*/




               /* Info info = new Info(na, insert, 1 , null, null);

                mArryList.add(info);
                mAdapter.notifyItemInserted(0);*/












/*
                   writeActivity.writebutton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v){



                         String text_writing = edittext_writing.getText().toString();
                        byteArray = getIntent().getByteArrayExtra("image");


                        Info info = new Info(myprofileActivity.textView_na,insert, register2Activity.byteArray , null);

                        mArryList.add(0, info);
                        mAdapter.notifyDataSetChanged();


                    }
                });*/

            };

        });







      //  view.setOnCreateContextMenuListener(this);

    } // end of create

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector; // 제스처 이벤트
        private Story_Activity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Story_Activity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY()); // 차일드 뷰 좌표 값
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                        position = recyclerView.getChildAdapterPosition(child); // 터치시 차일드뷰의 포지션 값을 읽어 포지션에 대입
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
           // 글 추가 작성 코드 (3000)
                case 3000:
                  //  Toast.makeText(Story_Activity.this, "Result: " + data.getStringExtra("result"), Toast.LENGTH_SHORT).show();

                  //  TextView textView = (TextView) findViewById(R.id.textView444);
                 //   textView_insert = data.getStringExtra("result");
                 //   textView_insert.setText(insert_text);
                 //   textView.setText(data.getStringExtra("result"));
                /*    myProfile = (ImageView) findViewById(R.id.myprofile);
                    Register3Activity.byteArray = byteArray;
                    byteArray = getIntent().getByteArrayExtra("image");
                    Register3Activity.bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    Register3Activity.bitmap = bitmap;*/

//                    myProfile.setImageBitmap(bitmap);

                    Intent intent = getIntent();
                    // 테스트중
                    Picturest = temp;
                    Log.d(TAG, "Picturest" + Picturest + "");
                 //   Toast.makeText(Story_Activity.this, "Picture: " + intent.getStringExtra("Picture"), Toast.LENGTH_SHORT).show();
                   //  byte[] encodeByte= Base64.decode(Picture,Base64.DEFAULT);
                   //  Picturebitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                    // mynation.setImageDrawable(img_nation);




                  /*  String format = new String("a HH : mm");
                    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
                    textView_time.setText(sdf.format(new Date()));

*/
                   CurrentT = time;
                    Log.d(TAG, "SPP" + SPP.getAll() + "");
                    //info = new PostModel(myname, null,uid,insert,null,null,CurrentT,sele);
                        // 어댑터로 올멱ㅆ는데 과연





                    //   info = new Info(myname, insert, null , null, Heartclick, CurrentT, null, Picturest);
                   // Info info = new Info();
                   /* info.setName(myname); // 여기부터
                    info.setInsert(insert);
                    info.setProfile(bitmap);
                    info.setPicture(addpicture);*/ // 여기까지
                  /*  info.setTime(textView_time);*/


                 //    addpicture = intent_02.getParcelableExtra("PUri");
                  //  imageView_picture.setImageURI(ur);
                  //  Log.d(TAG, "photoURI"+ photoURI + "");

                 //  Info.LikeCount =  Heartclick = 0;


                 //  mAdapter.setData(position);


                 /*   // Json to shared
                    try {
                        jsonArray = new JSONArray(); // 배열이 필요할 때
                        for(i = 0; i < mArrayList.size(); i++ )
                        { // 배열
                            JSONObject jsObject = new JSONObject(); // 배열 내에 들어갈 json

                         //   jsObject.put("name", mArrayList.get(i).getName());
                           jsObject.put("insert", mArrayList.get(i).getInsert());
                           jsObject.put("picture", mArrayList.get(i).getPicstr());
                           jsObject.put("time", mArrayList.get(i).getTime());
                         //   jsObject.put("profile", mArrayList.get(i).getProfile());
                         //   jsObject.put("time", mArrayList.get(i).getTime());
                            jsonArray.put(jsObject);
                            Log.d(TAG, "MyInsert" + jsonArray + "");
                        }

                      //  jsonMain.put("ID", Id);
                     //   jsonMain.put("PWD", Pwd);
                     //   jsonMain.put("Item", jsonArray);


                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                   *//* Gson gson = new Gson();
                    String json = gson.toJson(jsonMain);*//*
                    String json =  jsonArray.toString();
                    storyeditor.putString("MyInsert", json);
                    storyeditor.commit();
                    Log.d(TAG, "SPP" + SPP.getAll() + "");

*/ // 파이어베이스 사용


                    mArrayList.add(0,info); // 최신글이 가장 상단으로 위치하게 글 등록

                   // mAdapter.notifyDataSetChanged(); // 데이터 변경 알리기




                    break;


               // 글 수정 코드 4000
                case 4000:
                 //   Toast.makeText(Story_Activity.this, "Result: " + data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
                    Picturest = temp;
                    Log.d(TAG, "Picturest" + Picturest + "");
                   // info = new Info(myname, insert, bitmap , null, Heartclick, CurrentT, img, Picturest);
                 //   info = new Info(myname, insert, bitmap , null, Heartclick, CurrentT, img, Picturest);
                   // 다시 살리기








                    // info = mArrayList.get(position);
                  //  info = new Info();
                    /*info.setName(myname);
                    info.setInsert(insert);
                    info.setProfile(bitmap);
                    info.setPicture(Picturebitmap);*/
                    mAdapter.UpdateData(position, info);
                    //  mArrayList.set(position, info);

                   /* try {
                        jsonArray = new JSONArray(); // 배열이 필요할 때
                        for(i = 0; i < mArrayList.size(); i++ )
                        { // 배열
                            JSONObject jsObject = new JSONObject(); // 배열 내에 들어갈 json

                            jsObject.put("insert", mArrayList.get(i).getInsert());
                            jsObject.put("picture", mArrayList.get(i).getPicstr());
                            jsObject.put("time", mArrayList.get(i).getTime());

                            jsonArray.put(jsObject);
                            Log.d(TAG, "MyInsert" + jsonArray + "");
                        }


                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                   *//* Gson gson = new Gson();
                    String json = gson.toJson(jsonMain);*//*
                    json =  jsonArray.toString();
                    storyeditor.putString("MyInsert", json);
                    storyeditor.commit();
                    Log.d(TAG, "SPP" + SPP.getAll() + "");
*/ // 파이어베이스 사용
                    break;

                case 4001:
                    mAdapter.RemoveData(position);

            }
        } else {
          //  Toast.makeText(Story_Activity.this, "Failed", Toast.LENGTH_SHORT).show();


        }
    }


   /* protected void onSaveData(){
        info.setName(myname);
        info.setInsert(insert);
        info.setProfile(bitmap);
     //   info.setPicture(addpicture);
  //    Info info = new Info(myname, insert, bitmap , null, Heartclick, 0, addpicture);
        gson = new GsonBuilder().create();
        String strinfo = gson.toJson(info, Info.class);

        SPP = getSharedPreferences("SPP", MODE_PRIVATE);
        storyeditor.putString("Info", strinfo);
        storyeditor.commit();

    }

    protected void onSearchData(){
        SPP = getSharedPreferences("SPP", MODE_PRIVATE);
        String strinfo = SPP.getString("Info","");

        Type listType = new TypeToken<ArrayList<Info>>(){}.getType();
        ArrayList<Info> datas = gson.fromJson(strinfo, listType);

        for(Info data : datas){
            Log.d("PRINT", data.getName());
            Log.d("PRINT", data.getInsert());
        }


        Info info = gson.fromJson(strinfo, Info.class);

        mArrayList.add(info);

    }*/













    /*private void setData(){
        SPP = PreferenceManager.getDefaultSharedPreferences(Story_Activity.this);
        String strJson = SPP.getString("Item","");
        Log.d(TAG, SPP.getString("Item" + "Item","") + "");
        Gson gson = new Gson();
        ArrayList<Info> mArrayList = new ArrayList<>();

        Info[] infos = gson.fromJson(strJson,Info[].class);

        for(Info info : infos){
            mArrayList.add(info);
        }

        mAdapter = new CustomAdapter(Story_Activity.this, mArrayList);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(Story_Activity.this));
        mRecyclerview.setAdapter(mAdapter);
    }


private void loadArray(){
    SPP = PreferenceManager.getDefaultSharedPreferences(Story_Activity.this);
    String strJson = SPP.getString("MyInsert", "");
    Toast.makeText(Story_Activity.this, strJson + "", Toast.LENGTH_LONG).show();

    if(strJson != "fail"){
        try{
            JSONArray response = new JSONArray(strJson);
            for (int i=0; i<response.length(); i++){
                JSONObject jsonObject = response.getJSONObject(i);
                insert = jsonObject.getString("insert");
                info = new Info(myname, insert, bitmap , null, Heartclick, 0, img);
                mArrayList.add(info);
            }

            mAdapter = new CustomAdapter(Story_Activity.this, mArrayList);
            mRecyclerview.setAdapter(mAdapter);


        } catch (JSONException e){
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }
}






  public void loadArrayList(String dataObject){
     //   if(jsonString == null) return false;
      SPP = PreferenceManager.getDefaultSharedPreferences(Story_Activity.this);
      dataObject = SPP.getString("MyObject","");
      dataObject = data;

      Gson gson = new Gson();
    //  String json = gson.fromJson(dataObject,wrapObject);

     *//* String jsonString;
        jsonString = jsonString.replace("nameValuePairs(","");
        jsonString = jsonString.replace(")", "");
*//*


        try{

            JSONObject wrapObject = new JSONObject(dataObject);

            JSONArray re = new JSONArray(wrapObject.getString("Item"));

            for(int i = 0; i < re.length(); i++){
                JSONObject resinfo = re.getJSONObject(i);

                String name = resinfo.getString("name");
                String insert = resinfo.getString("insert");
                String profile = resinfo.getString("profile");

                byte[] decodedByteArray = Base64.decode(profile, Base64.NO_WRAP);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);


                info = new Info(name, insert, decodedBitmap, null, Heartclick, 0, img);

                *//*HashMap<String, String> resinfoMap = new HashMap<String, String>();
                resinfoMap.put("name", name);
                resinfoMap.put("insert", insert);
                resinfoMap.put("profile", profile);*//*


               *//* info.setName(name);
                info.setInsert(insert);
                info.setProfile(decodedBitmap);*//*
              //  info.setPicture(addpicture);



                mArrayList.add(info);
                mAdapter.notifyDataSetChanged();
            }

            mAdapter = new CustomAdapter(Story_Activity.this, mArrayList);
            mRecyclerview.setAdapter(mAdapter);



        } catch (JSONException e){
            Log.d(TAG, e.toString());
        }
     // mAdapter.notifyDataSetChanged();
  }

*/











   /*public void onPostExecute(){

     //  SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
       SPP = PreferenceManager.getDefaultSharedPreferences(Story_Activity.this);

       String myObject = SPP.getString("Item","");
       try {
       JSONArray jsonArray = new JSONArray(myObject);

            for (int i = 0; i < jsonArray.length(); i++) {
                getName = new String[jsonArray.length()];
                String[] getInsert = new String[jsonArray.length()];
                String[] getProfile = new String[jsonArray.length()];
                  JSONObject jsonObject = jsonArray.getJSONObject(i);
                //  jsonArray.getJSONArray(i);
            *//*    getName[i] = jsonArray.optString(i);
                getInsert[i] = jsonArray.optString(i);
                getProfile[i] = jsonArray.optString(i);*//*
                    getName[i] = jsonObject.getString("name");
                    getInsert[i] = jsonObject.getString("insert");
                    getProfile[i] = jsonObject.getString("profile");

                Toast.makeText(Story_Activity.this, getName[i] + "", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "getName" + getName[i] + "");
                Log.d(TAG, getInsert[i] + "");
                Log.d(TAG,  getProfile[i] + "");

                byte[] decodedByteArray = Base64.decode(getProfile[i], Base64.NO_WRAP);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);


                info = new Info(getName[i], getInsert[i], decodedBitmap, null, Heartclick, 0, Picturebitmap);


                mAdapter = new CustomAdapter(Story_Activity.this, mArrayList);
                mRecyclerview.setAdapter(mAdapter);
                mArrayList.add(0, info);



            }

                } catch (JSONException e){
                    e.printStackTrace();
                }



    }*/












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
        super.onStart();

        rcode = 1;


    SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
    SharedPreferences.Editor editor = SPP.edit();
    editor.putString("alreadyInfo",alreadyInfo);
    editor.commit();


    }
    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
/*

        if(Remove == 1){
            try {
                jsonArray = new JSONArray(); // 배열이 필요할 때
                for(i = 0; i < mArrayList.size(); i++ )
                { // 배열
                    JSONObject jsObject = new JSONObject(); // 배열 내에 들어갈 json

                    //   jsObject.put("name", mArrayList.get(i).getName());
                    jsObject.put("insert", mArrayList.get(i).getInsert());
                    jsObject.put("picture", mArrayList.get(i).getPicstr());
                    jsObject.put("time", mArrayList.get(i).getTime());
                    //   jsObject.put("profile", mArrayList.get(i).getProfile());
                    //   jsObject.put("time", mArrayList.get(i).getTime());
                    jsonArray.put(jsObject);
                    Log.d(TAG, "MyInsert" + jsonArray + "");
                }

                //  jsonMain.put("ID", Id);
                //   jsonMain.put("PWD", Pwd);
                //   jsonMain.put("Item", jsonArray);


            } catch (JSONException e){
                e.printStackTrace();
            }

                   */
/* Gson gson = new Gson();
                    String json = gson.toJson(jsonMain);*//*

            String json =  jsonArray.toString();
            storyeditor.putString("MyInsert", json);
            storyeditor.commit();
            Remove = 0;
            Log.d(TAG, "SPP" + SPP.getAll() + "");
            }
*/


    }





}