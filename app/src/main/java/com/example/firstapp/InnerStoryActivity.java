package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import static com.example.firstapp.MyprofileActivity.myProfile;
//import static com.example.firstapp.Register3Activity.na;
import static com.example.firstapp.Story_Activity.info;
import static com.example.firstapp.Story_Activity.intent;
import static com.example.firstapp.Story_Activity.mAdapter;
import static com.example.firstapp.Story_Activity.mArrayList;
import static com.example.firstapp.Story_Activity.position;
import static com.example.firstapp.Story_Activity.rcode;
//import static com.example.firstapp.Story_Activity.x;
import static com.example.firstapp.WriteActivity.insert;

public class InnerStoryActivity extends AppCompatActivity {

    TextView nametext;
    TextView inserttext;
    TextView timetext, commentext;
    ImageView sendbutton, nation2;
    String key, comment;
    private static final String TAG = "InnerStory";
    List<PostModel> commentsList;
    List<PostModel> InfoList;
    private RecyclerView recyclerView;
    InnerStoryActivity.RecyclerViewAdapter mAdapter;
    int position;
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    SharedPreferences SPP;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    String pushkey;
    String keystr;
    String myname,mynation,profilestring;
    int commentCount;
   // String commentCountstr = "" + commentCount;
    List<String> keyList = new ArrayList<>();
    String commentmodify;
    ImageView translationButton;
    String trans;
    ImageView picture;
    PostModel postModel;
    String destinationUid;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_story);






        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_innerstory);


        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(InnerStoryActivity.this));


        // 리사이클러뷰 아이템 선으로 구분하기
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        commentsList = new ArrayList<>();
        InfoList = new ArrayList<>();




        // StoryActivity 정보 받기
        String name = ""; // 이름
        String insert = ""; // 글
        String time = ""; // 시간
        String profile = "";
        key = ""; // 댓글 저장하기 위한 key
        //  int position;
        String nation = "";
        String image = ""; // 첨부사진

        Bundle extras = getIntent().getExtras();
        profile = extras.getString("profileURL");
        name = extras.getString("name");
        insert = extras.getString("insert");
        time = extras.getString("time");
        key = extras.getString("commentkey");
        nation = extras.getString("nation");
        image = extras.getString("picture");
        destinationUid = extras.getString("uid");

        //  position = extras.getInt("position");
        Log.d("알림", "key" + key);



       /* Intent intent = getIntent();
        commentmodify  = intent.getStringExtra("commentmodify"); // 로그인 아이디 받아오기
        Log.w("댓글수정", "commentmodify" + commentmodify + "");*/



        picture = (ImageView) findViewById(R.id.inner_picture);
        translationButton = (ImageView) findViewById(R.id.inner_trans);
        nametext = (TextView) findViewById(R.id.textView_name2);
        inserttext = (TextView) findViewById(R.id.textView_insert2);
        timetext = (TextView) findViewById(R.id.textView_time2);
        ImageView mypforile2 = (ImageView) findViewById(R.id.myprofile2);
        commentext = (TextView) findViewById(R.id.editText_comment);
        sendbutton = (ImageView) findViewById(R.id.imageView_send);
        nation2 = (ImageView) findViewById(R.id.myNation2);


        String namestr = name;
        final String insertstr = insert;
        String timestr = time;
        String profilestr = profile;
        keystr = key;
        String imagestr = image;

        Log.d("알림", "profilestr" + profilestr);
        Log.d("알림", "keystr" + keystr);

        nametext.setText(namestr);
        inserttext.setText(insertstr);
        timetext.setText(timestr);

        Glide.with(this).load(profilestr).apply(new RequestOptions().circleCrop().centerCrop()).into(mypforile2);
        Glide.with(this).load(imagestr).apply(new RequestOptions().centerCrop().override(800,800)).into(picture);

        if(nation.equals("미국")){

            nation2.setImageResource(R.drawable.usaflag);

            // 미국 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 미국 국기 사진 출력
        } else if(nation.equals("중국")){


            nation2.setImageResource(R.drawable.chineseflag);


            //중국 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 중국 사진 출력
        } else if(nation.equals("한국")){

            nation2.setImageResource(R.drawable.koreaflag);



            //한국 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 한국 사진 출력
        } else if(nation.equals("일본")){

            nation2.setImageResource(R.drawable.japanflag);



            //일본 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 일본 사진 출력

        }







        SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
        myname = SPP.getString("Name","");
        // myage = SPP.getString("Age","");
        profilestring = SPP.getString("profileString", "");
        mynation = SPP.getString("Nation", "");
        //followCount = SPP.getInt("folloCount",0);

// 쉐어드에서 내 정보 꺼내와서 넣어야함... - ㅁ-;; 응ㅇ아아아앙ㅇ아ㅏㅇㅇ아ㅏㄱ
        // 이름, 프로필, 거주국가, comment, 작성 시간 저장해야함
        // 방금전, 뭐시기, 뭐시기 등 그거 하자~~ 시간





        recyclerView.setAdapter(new RecyclerViewAdapter());





        if(commentmodify == null) {

            sendbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   /* String format = new String("MM.dd hh : mm"); // 현재 시간 표시
                    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
                    String time = sdf.format(new Date());*/
                    long nowtime = System.currentTimeMillis();
                    String time = String.valueOf(nowtime);


                    Log.d("댓글", "댓글수정안한다");
                    comment = commentext.getText().toString().trim(); // 이거 저장하면됨
                    commentCount = commentCount + 1;
                    pushkey = mDatabase.child("posts").push().getKey();
                    //   String uid, String name, String profileString, String contentWriting, String image, String time, String postNumber, String nation


                    String content = myname + "님이 회원님의 게시물에 댓글을 남겼습니다.";
                    ChatRoomInfo chatRoomInfo = new ChatRoomInfo(myname, profilestring, uid,time,mynation,content);
                    mDatabase.child("notification").child(destinationUid).push().setValue(chatRoomInfo);


                    PostModel postModel = new PostModel(null, myname, profilestring, comment, null, time, null, mynation);

                    // 아 내 댓글도 저장해야되니깐 uid 필요함... 저장해야함 삭제하면 같이 삭제해야댕... push키 이용
                    mDatabase.child("posts").child(key).child("comments").child(pushkey).setValue(postModel);
                 //   mDatabase.child("users").child(uid).child("mposts").child(key).child("comments").child(pushkey).setValue(postModel);
                    mDatabase.child("posts").child(key).child("commentCount").setValue(commentCount);
                //    mDatabase.child("users").child(uid).child("mposts").child(key).child("commentCount").setValue(commentCount);
                    mDatabase.child("users").child(uid).child("mcomments").child(pushkey).setValue(postModel);
                    //  mDatabase.child("posts").child(key).child("mcommentCount").setValue(postModel);
                    commentext.setText("");
                }
            });

        } else {



        }


       translationButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(InnerStoryActivity.this, translationActivity.class);
               startActivity(intent);
           }
       });





    } // end of create


    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        public RecyclerViewAdapter() {





           /* FirebaseDatabase.getInstance().getReference().child("posts").child(key).child("comments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    commentsList.clear();
                    //  keyList.clear(); // 댓글 푸시키 필요
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        commentsList.add(0, snapshot.getValue(PostModel.class));
                        //  String modifykey = snapshot.getKey();
                        Log.d("알림", "commentsList" + commentsList);
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/

            // 리사이클러뷰에 댓글 목록 보여주기
            FirebaseDatabase.getInstance().getReference().child("posts").child(key).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    commentsList.clear();
                    keyList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        postModel = snapshot.getValue(PostModel.class);
                        String key = snapshot.getKey();
                        commentsList.add(0, postModel);
                        keyList.add(0,key);
                        Log.d("알림", "commentsList" + commentsList);



                        Log.d("내가먼저", "리스트먼저");
                    }

                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            FirebaseDatabase.getInstance().getReference().child("posts").child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    commentCount = postModel.commentCount;
                    Log.d("알림", "commentCount" + commentCount + "");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




            /*FirebaseDatabase.getInstance().getReference().child("posts").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    InfoList.clear();
                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        PostModel postModel = item.getValue(PostModel.class);

                        if(postModel.uid.equals(uid)){

                            InfoList.add(0,postModel);


                        }
                    }
                    // 메세지 갱신
                    notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/






/*
         FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    destinationUserinfo = dataSnapshot.getValue(UserInfo.class);
                    getMessageList();
                    textView.setText(destinationUserinfo.name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        void  getMessageList(){
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    comments.clear(); // 클리어를 하지 않으면 데이터가 계속 쌓이게 된다.

                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        comments.add(item.getValue(ChatModel.Comment.class));
                    }
                    // 메세지 갱신
                    notifyDataSetChanged();

                    recyclerView.scrollToPosition(comments.size() - 1); // 리사이클러뷰 스크롤 마지막 메시지로 이동
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_item, viewGroup, false);


            return new InnerStoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            InnerStoryViewHolder InnerStoryViewHolder = ((RecyclerViewAdapter.InnerStoryViewHolder) viewHolder);


            InnerStoryViewHolder.mname.setText(commentsList.get(position).name); // 이름
            InnerStoryViewHolder.minsert.setText(commentsList.get(position).contentWriting); // 글 내용
           // InnerStoryViewHolder.mtime.setText(commentsList.get(position).time); // 날짜
            // MyStoryViewHolder.mbubble.setImageResource(R.drawable.talkbubble);// 말풍선(댓글) 이미지
            //  MyStoryViewHolder.mheart.setImageResource(R.drawable.heart); // 하트 이미지
            //  MyStoryViewHolder.mnation.setImageDrawable(nation); // 국기 이미지
            // viewholder.mpicture.setImageBitmap(list.get(position).Picture); // 사진 첨부


            if(commentsList.get(position).time != null) {
                long now = System.currentTimeMillis();
                long t = Long.parseLong(commentsList.get(position).time);
                long passed_time = now - t;
                passed_time = passed_time / 60000;
                String time_ago = "";
                if ((passed_time / 1440) > 0) {
                    time_ago = passed_time / 1440 + "일 전";
                } else if ((passed_time / 60) > 0) {
                    time_ago = passed_time / 60 + "시간 전";
                } else if ((passed_time > 0)) {
                    time_ago = passed_time + "분 전";
                } else {
                    time_ago = "방금";
                }

                InnerStoryViewHolder.mtime.setText(time_ago);
            }



            Glide.with(InnerStoryViewHolder.itemView.getContext())
                    .load(commentsList.get(position).profileString)
                    .apply(new RequestOptions().circleCrop().centerCrop())
                    .into(InnerStoryViewHolder.mprofile);

          /*  Glide.with(MyStoryViewHolder.itemView.getContext())
                    .load(mPostArrayList.get(position).image)
                    .apply(new RequestOptions().centerCrop().override(1000,1000))
                    .into(MyStoryViewHolder.mpicture);*/

            if (commentsList.get(position).nation.equals("미국")) {

                InnerStoryViewHolder.mnation.setImageResource(R.drawable.usaflag);

                // 미국 국기 사진 가져와서
                //   mnation.setImageDrawable(img_nation);// 미국 국기 사진 출력
            } else if (commentsList.get(position).nation.equals("중국")) {


                InnerStoryViewHolder.mnation.setImageResource(R.drawable.chineseflag);


                //중국 국기 사진 가져와서
                //   mnation.setImageDrawable(img_nation);// 중국 사진 출력
            } else if (commentsList.get(position).nation.equals("한국")) {

                InnerStoryViewHolder.mnation.setImageResource(R.drawable.koreaflag);


                //한국 국기 사진 가져와서
                //   mnation.setImageDrawable(img_nation);// 한국 사진 출력
            } else if (commentsList.get(position).nation.equals("일본")) {

                InnerStoryViewHolder.mnation.setImageResource(R.drawable.japanflag);


                //일본 국기 사진 가져와서
                //   mnation.setImageDrawable(img_nation);// 일본 사진 출력

            }





        }


        private class InnerStoryViewHolder extends RecyclerView.ViewHolder implements View

                .OnCreateContextMenuListener {
            protected TextView mname;
            protected TextView minsert;
            //   MyprofileActivity myprofileActivity;
            protected ImageView mprofile;
            protected ImageView mheart;
            protected TextView mlike;
            protected TextView mtime;
            protected ImageView mnation;
            protected TextView deletebutton;
            protected ImageView mpicture;
            protected ImageView mbubble;


            public InnerStoryViewHolder(View view) {
                super(view);

                this.mname = (TextView) view.findViewById(R.id.comment_name); // 이름
                this.minsert = (TextView) view.findViewById(R.id.comment_textView_comment); // 글 내용
                mprofile = (ImageView) view.findViewById(R.id.commentItem_imageview_profile); // 프로필 사진
                mheart = (ImageView) view.findViewById(R.id.imageView_heart); // 하트 이미지
                mlike = (TextView) view.findViewById(R.id.textView_heart); // 좋아요 수
                mtime = (TextView) view.findViewById(R.id.comment_textView_time); // 시간
                mnation = (ImageView) view.findViewById(R.id.commentItem_imageview_Nation); // 거주 국가 이미지

                mpicture = (ImageView) view.findViewById(R.id.imageView_picture); // 첨부 사진 이미지뷰



                if(commentsList.get(position).name.equals(myname)) {
                    Log.d("댓글수정댓글", "postModel.name" + commentsList.get(position).name + "");
                    Log.d("댓글수정댓글", "myname" + myname + "");
                    view.setOnCreateContextMenuListener(this);
                }
            }


            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가U
                position = getAdapterPosition();
                if(commentsList.get(position).name.equals(myname)) {
                    MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "수정");
                    MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
                    Edit.setOnMenuItemClickListener(onEditMenu);
                    Delete.setOnMenuItemClickListener(onEditMenu);
                }
            }

            private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {


                    switch (item.getItemId()) { // 수정하기 눌렀을 때
                        case 1001:

                           /* Intent intent = new Intent(InnerStoryActivity.this, InnerStoryActivity.class);
                            intent.putExtra("commentmodify", keyList.get(position)); // 원래 안씀*/
                           ;
                            commentext.setText(commentsList.get(position).contentWriting);
                            commentmodify  = keyList.get(position);
                            Log.d("댓글수정댓글", "commentmodify" + commentmodify + "");

                            if(commentmodify != null) {
                                sendbutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.d("댓글", "댓글수정한다");
                                        comment = commentext.getText().toString().trim(); // 이거 저장하면됨

                                        PostModel postModel = new PostModel(null, myname, profilestring, comment, null, null, null, mynation);

                                        mDatabase.child("posts").child(key).child("comments").child(commentmodify).setValue(postModel);

                                        mDatabase.child("users").child(uid).child("mcomments").child(commentmodify).setValue(postModel);

                                        commentext.setText("");
                                        commentmodify = null;
                                    }
                                });
                            }

                            break;

                        case 1002:



                            mDatabase.child("posts").child(key).child("comments").child(keyList.get(position)).removeValue();
                            mDatabase.child("users").child(uid).child("mposts").child(key).child("comments").child(keyList.get(position)).removeValue();

                            commentCount = commentCount - 1;
                            mDatabase.child("posts").child(key).child("commentCount").setValue(commentCount);

                            break;


                    }

                    return true;
                }

            };


        }

        @Override
        public int getItemCount() {

            return commentsList.size();

        }

















   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 4000:
                 //   Toast.makeText(InnerStoryActivity.this, "Result: " + mArrayList.get(position) + data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
                 //   Toast.makeText(InnerStoryActivity.this, "Result: " + position, Toast.LENGTH_SHORT).show();
                    info = mArrayList.get(position);
                  // String modifytext = insert.getStringExtra("result");
                   Intent intent = new Intent(InnerStoryActivity.this, Story_Activity.class);
                   intent.putExtra("result", insert);
                   inserttext.setText(insert);
                  //  mAdapter.notifyDataSetChanged();
                  //  mArrayList.set(position, info);
                  //  mAdapter.notifyDataSetChanged();
                    setResult(RESULT_OK,intent);
                  *//* Intent intent = new Intent(InnerStoryActivity.this, Story_Activity.class);
                   startActivity(intent);*//*


         *//*  Info info = new Info(na, insert, bitmap , null, Heartclick, 0);

                    mArrayList.add(0,info);
                    //   mAdapter.notifyItemInserted(0);
                    mAdapter.notifyDataSetChanged();*//*


                    break;



                case 3001:

                    //   mAdapter.UpdateData(position, Info);

                    //    mArryList.remove(mAdapter.getAdapterPosition());
                    mAdapter.notifyDataSetChanged();
                    *//*notifyItemChanged();
                    notifyDataSetChanged();*//*

            }
        } else {
         //   Toast.makeText(InnerStoryActivity.this, "Failed", Toast.LENGTH_SHORT).show();


        }
    }*/


    }







    @Override
    protected void onResume() {
        super.onResume();

        SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
        trans = SPP.getString("copy","");
        Log.d("copy2", "trans" + trans);
        commentext.setText(trans);
        SharedPreferences.Editor editor = SPP.edit();
        editor.putString("copy","");
        editor.commit();
    }









}