package com.example.firstapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.firstapp.model.ChatModel;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.example.firstapp.Story_Activity.uid;

public class MyStoryActivity extends AppCompatActivity {

    private static final String TAG= "MyStory";
    private RecyclerView recyclerView;
    ArrayList<PostModel> mPostArrayList;
    ArrayList<PostModel> PostArrayList;
    ArrayList<String> keyList = new ArrayList<>();
    ArrayList<String> postkeyList = new ArrayList<>();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId = user.getUid();
    RecyclerViewAdapter mAdapter;
    private Context mContext;
    int mposition;
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    static PostModel p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_story);


      /*  FirebaseDatabase.getInstance().getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                keyList.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                   // postModel = snapshot.getValue(PostModel.class);
                    String key = snapshot.getKey();

                    postkeyList.add(0,key);

                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        recyclerView = (RecyclerView) findViewById(R.id.mystory_recyclerview);


       // recyclerView.setLayoutManager(new LinearLayoutManager(Story_Activity.this));
       // recyclerView.setAdapter(new CustomViewAdapter());*/




        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyStoryActivity.this));
        mPostArrayList = new ArrayList<>();
        PostArrayList = new ArrayList<>();


/*
        mAdapter = new CustomAdapter(MyStoryActivity.this, mPostArrayList);
        recyclerView.setAdapter(mAdapter); // CustomAdapter 연결*/


        // 리사이클러뷰 아이템 선으로 구분하기
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
        mLinearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);





       // recyclerView.setLayoutManager(new LinearLayoutManager(MyStoryActivity.this));
        recyclerView.setAdapter(new RecyclerViewAdapter());



    }







    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{



        public  RecyclerViewAdapter(){



           /* FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("mposts").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mPostArrayList.clear();
                    keyList.clear();
                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                        mPostArrayList.add(0,snapshot.getValue(PostModel.class));
                        String modifykey = snapshot.getKey();
                        keyList.add(0,modifykey);
                        Log.d(TAG, " mPostArrayList" +  mPostArrayList + "");
                        Log.d(TAG, " mkeyList" +  keyList + "");
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
*/

            FirebaseDatabase.getInstance().getReference().child("posts").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    keyList.clear();
                    mPostArrayList.clear();
                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        PostModel postModel = item.getValue(PostModel.class);
                        //  PostArrayList.add(0,postModel);
                        //  String modifykey = snapshot.getKey();
                        //  keyList.add(0,modifykey);

                        if(postModel.uid.equals(userId)){
                            String modifykey = item.getKey();
                            mPostArrayList.add(0,postModel);
                            keyList.add(0,modifykey);
                            Log.d(TAG, " mkeyList" +  keyList + "");

                        }

                    }
                    // 메세지 갱신
                    notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


           /* FirebaseDatabase.getInstance().getReference().child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    keyList.clear();
                    mPostArrayList.clear();
                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                        PostModel postModel = snapshot.getValue(PostModel.class);
                      //  PostArrayList.add(0,postModel);
                      //  String modifykey = snapshot.getKey();
                      //  keyList.add(0,modifykey);

                        if(postModel.uid.equals(userId)){
                            String modifykey = snapshot.getKey();
                            mPostArrayList.add(0,postModel);
                            keyList.add(0,modifykey);
                            Log.d(TAG, " mkeyList" +  keyList + "");

                        }


                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
*/




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
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mystory_item,viewGroup, false);


            return new MyStoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
            MyStoryActivity.RecyclerViewAdapter.MyStoryViewHolder MyStoryViewHolder = ((MyStoryActivity.RecyclerViewAdapter.MyStoryViewHolder)viewHolder);


            MyStoryViewHolder.mname.setText(mPostArrayList.get(position).name); // 이름
            MyStoryViewHolder.minsert.setText(mPostArrayList.get(position).contentWriting); // 글 내용
            MyStoryViewHolder.mtime.setText(mPostArrayList.get(position).time); // 날짜
           // MyStoryViewHolder.mbubble.setImageResource(R.drawable.talkbubble);// 말풍선(댓글) 이미지
          //  MyStoryViewHolder.mheart.setImageResource(R.drawable.heart); // 하트 이미지
          //  MyStoryViewHolder.mnation.setImageDrawable(nation); // 국기 이미지
            // viewholder.mpicture.setImageBitmap(list.get(position).Picture); // 사진 첨부


            if(mPostArrayList.get(position).commentCount != 0) { // 댓글 수 0이 아닐 때만 숫자 표시
                MyStoryViewHolder.commentCount.setText(String.valueOf(mPostArrayList.get(position).commentCount));
            }

            if(mPostArrayList.get(position).likeCount != 0) { // 좋아요 수 0이 아닐 때만 숫자 표시
                MyStoryViewHolder.likeCount.setText(String.valueOf(mPostArrayList.get(position).likeCount));
            }


            Glide.with(MyStoryViewHolder.itemView.getContext())
                    .load(mPostArrayList.get(position).profileString)
                    .apply(new RequestOptions().circleCrop().centerCrop())
                    .into(MyStoryViewHolder.mprofile);

            Glide.with(MyStoryViewHolder.itemView.getContext())
                    .load(mPostArrayList.get(position).image)
                    .apply(new RequestOptions().centerCrop().override(1000,1000))
                    .into(MyStoryViewHolder.mpicture);

            if(mPostArrayList.get(position).nation.equals("미국")){

                MyStoryViewHolder.mnation.setImageResource(R.drawable.usaflag);

                // 미국 국기 사진 가져와서
                //   mnation.setImageDrawable(img_nation);// 미국 국기 사진 출력
            } else if(mPostArrayList.get(position).nation.equals("중국")){


                MyStoryViewHolder.mnation.setImageResource(R.drawable.chineseflag);


                //중국 국기 사진 가져와서
                //   mnation.setImageDrawable(img_nation);// 중국 사진 출력
            } else if(mPostArrayList.get(position).nation.equals("한국")){

                MyStoryViewHolder.mnation.setImageResource(R.drawable.koreaflag);



                //한국 국기 사진 가져와서
                //   mnation.setImageDrawable(img_nation);// 한국 사진 출력
            } else if(mPostArrayList.get(position).nation.equals("일본")){

                MyStoryViewHolder.mnation.setImageResource(R.drawable.japanflag);

                //일본 국기 사진 가져와서
                //   mnation.setImageDrawable(img_nation);// 일본 사진 출력

            }


            if(mPostArrayList.get(position).Likes.containsKey(userId)){
                Log.d("알림", "좋아요 UID 이뜨아");

                MyStoryViewHolder.mheart.setImageResource(R.drawable.redheart);

            } else {
                Log.d("알림", "좋아요 UID 없뜨아");

                MyStoryViewHolder.mheart.setImageResource(R.drawable.heart);

            }


            MyStoryViewHolder.minsert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MyStoryActivity.this, InnerStoryActivity.class);
                    intent.putExtra("name", mPostArrayList.get(position).name);
                    intent.putExtra("insert", mPostArrayList.get(position).contentWriting);
                    intent.putExtra("time", mPostArrayList.get(position).time);
                    intent.putExtra("profileURL", mPostArrayList.get(position).profileString);
                    intent.putExtra("commentkey", keyList.get(position));
                    intent.putExtra("nation", mPostArrayList.get(position).nation);
                    intent.putExtra("picture", mPostArrayList.get(position).image);
                    intent.putExtra("uid",mPostArrayList.get(position).uid);
                    //  intent.putExtra("position", position);
                    startActivity(intent);

                }
            });


            MyStoryViewHolder.mbubble.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MyStoryActivity.this, InnerStoryActivity.class);
                    intent.putExtra("name", mPostArrayList.get(position).name);
                    intent.putExtra("insert", mPostArrayList.get(position).contentWriting);
                    intent.putExtra("time", mPostArrayList.get(position).time);
                    intent.putExtra("profileURL", mPostArrayList.get(position).profileString);
                    intent.putExtra("commentkey", keyList.get(position));
                    intent.putExtra("nation",  mPostArrayList.get(position).nation);
                    intent.putExtra("picture", mPostArrayList.get(position).image);
                    intent.putExtra("uid",mPostArrayList.get(position).uid);
                    //  intent.putExtra("position", position);
                    startActivity(intent);

                }
            });

            MyStoryViewHolder.mheart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onHeartClicked(mDatabase.child("posts").child(keyList.get(position)));

                }
            });


        }



        private class MyStoryViewHolder extends RecyclerView.ViewHolder implements View

                .OnCreateContextMenuListener{
            protected TextView mname;
            protected TextView minsert;
            //   MyprofileActivity myprofileActivity;
            protected ImageView mprofile;
            protected ImageView mheart;
            protected TextView likeCount;
            protected TextView mtime;
            protected  ImageView mnation;
            protected  TextView deletebutton;
            protected  ImageView mpicture;
            protected  ImageView mbubble;
            protected  TextView commentCount;

            public MyStoryViewHolder(View view){
                super(view);

                this.mname = (TextView) view.findViewById(R.id.comment_name); // 이름
                this.minsert = (TextView) view.findViewById(R.id.textView_insert); // 글 내용
                mprofile = (ImageView) view.findViewById(R.id.myprofilep); // 프로필 사진
                mheart = (ImageView) view.findViewById(R.id.imageView_heart); // 하트 이미지
                likeCount = (TextView) view.findViewById(R.id.textView_heart); // 좋아요 수
                mtime = (TextView) view.findViewById(R.id.textView_time); // 시간
                mnation = (ImageView) view.findViewById(R.id.myNation); // 거주 국가 이미지
                commentCount = (TextView) view.findViewById(R.id.textView_comment); // 댓글 수
                mpicture = (ImageView) view.findViewById(R.id.imageView_picture); // 첨부 사진 이미지뷰
                mbubble = (ImageView) view.findViewById(R.id.imageView_comment); // 말풍선 이미지(댓글)


                view.setOnCreateContextMenuListener(this);
            }





            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가U
                mposition = getAdapterPosition();
                MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "수정");
                MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
                Edit.setOnMenuItemClickListener(onEditMenu);
                Delete.setOnMenuItemClickListener(onEditMenu);

            }

            private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {


                    switch (item.getItemId()) {
                        case 1001:
                            mposition = getAdapterPosition();
                            Intent intent = new Intent(MyStoryActivity.this, WriteActivity.class);
                              intent.putExtra("modifykey", keyList.get(mposition)); // 원래 안씀
                            Log.d("마이스토리", "modifykey" + keyList.get(mposition));
                              intent.putExtra("insert", mPostArrayList.get(mposition).contentWriting);
                            Log.d("마이스토리", "insert" + mPostArrayList.get(mposition).contentWriting);
                              intent.putExtra("modifyimage", mPostArrayList.get(mposition).image);
                            Log.d("마이스토리", "image" + mPostArrayList.get(mposition).image);
                              intent.putExtra("modifytime", mPostArrayList.get(mposition).time);
                            Log.d("마이스토리", "time" + mPostArrayList.get(mposition).time);
                            //  Log.d(TAG, "Picture" + list.get(position).Picstr + "" + "insert" + list.get(position).Insert + "");
                          //  WriteActivity.modify = 1;
                           // startActivityForResult(intent, 4000);
                            startActivity(intent);









                            break;

                        case 1002:
                            PostModel postModel = new PostModel();
                            mPostArrayList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), mPostArrayList.size());

                            mDatabase.child("posts").child(keyList.get(mposition)).removeValue();
                            mDatabase.child("users").child(userId).child("mposts").child(keyList.get(mposition)).removeValue();
                            //  SharedPreferences SPP = ((Object) mContext).getPreferences();
                            //   SharedPreferences SPP = PreferenceManager.getDefaultSharedPreferences(mContext);
                            //   SharedPreferences.Editor editor = SPP.edit();
                            Story_Activity.Remove = 1;


                            break;


                    }

                    return true;
                }

            };



        }

        @Override
        public int getItemCount() {

            return mPostArrayList.size();

        }


    }





    private void onHeartClicked(DatabaseReference postRef){
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                p = mutableData.getValue(PostModel.class);
                if(p == null){
                    return Transaction.success(mutableData);
                }

                if(p.Likes.containsKey(userId)){
                    p.likeCount = p.likeCount -1;
                    p.Likes.remove(userId);
                } else {
                    p.likeCount = p.likeCount +1;
                    p.Likes.put((userId),true);
                }

                mutableData.setValue(p);
                return Transaction.success(mutableData);

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();


    }


}
