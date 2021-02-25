package com.example.firstapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.firstapp.model.PostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.firstapp.Info.LikeCount;
import static com.example.firstapp.Register2Activity.img_nation;
//import static com.example.firstapp.Register2Activity.photoURI;
//import static com.example.firstapp.Register2Activity.photoURI;
import static com.example.firstapp.Register2Activity.select_nation;
import static com.example.firstapp.Story_Activity.Heartclick;
import static com.example.firstapp.Story_Activity.LikeUid;
import static com.example.firstapp.Story_Activity.info;
import static com.example.firstapp.Story_Activity.keyList;
import static com.example.firstapp.Story_Activity.mArrayList;
import static com.example.firstapp.Story_Activity.myname;
import static com.example.firstapp.Story_Activity.nation;
import static com.example.firstapp.Story_Activity.position;
import static com.example.firstapp.Story_Activity.postModel;
import static com.example.firstapp.Story_Activity.profilestring;
import static com.example.firstapp.Story_Activity.rcode;

//import static com.example.firstapp.WriteActivity.uri;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
 //  private MyRecyclerViewClickListener mListener;
    ArrayList<PostModel> list = new ArrayList<>();
    private Context mContext;
    static int keyPosition;
    int a;
    private RecyclerView recyclerView;
    private static final String TAG= "Dongle";
    static int mPosition;
    final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    SharedPreferences SPP;
    SharedPreferences.Editor Adaptereditor;
    Uri myphotouri;
    String key;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    static PostModel p;
    String time;


   static TextView mlike;
  //  static EditText editText_writing;

    //public class CustomViewHolder extends RecyclerView.ViewHolder  {

       public class CustomViewHolder extends RecyclerView.ViewHolder implements View

                .OnCreateContextMenuListener { // 리사이클러뷰에 반복될 아이템 레이아웃 연결

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





        public CustomViewHolder(View view) {
            super(view);



            this.mname = (TextView) view.findViewById(R.id.comment_name); // 이름
            this.minsert = (TextView) view.findViewById(R.id.textView_insert); // 글 내용
            mprofile = (ImageView) view.findViewById(R.id.myprofilep); // 프로필 사진
            mheart = (ImageView) view.findViewById(R.id.imageView_heart); // 하트 이미지
            likeCount = (TextView) view.findViewById(R.id.textView_heart); // 좋아요 수
            mtime = (TextView) view.findViewById(R.id.textView_time); // 시간
            mnation = (ImageView) view.findViewById(R.id.myNation); // 거주 국가 이미지
            mpicture = (ImageView) view.findViewById(R.id.imageView_picture); // 첨부 사진 이미지뷰
            mbubble = (ImageView) view.findViewById(R.id.imageView_comment); // 말풍선 이미지(댓글)
            commentCount = (TextView) view.findViewById(R.id.textView_comment); // 댓글 수





          //  view.setOnCreateContextMenuListener(this);
        }




        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가U

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

                        Intent intent = new Intent(mContext, WriteActivity.class);
                      //  intent.putExtra("insert", info.getInsert()); // 원래 안씀
                      //  intent.putExtra("insert", list.get(position).Insert);
                     //   intent.putExtra("Picture", list.get(position).Picstr);
                     //   Log.d(TAG, "Picture" + list.get(position).Picstr + "" + "insert" + list.get(position).Insert + "");
                        WriteActivity.modify = 1;
                        ((Activity) mContext).startActivityForResult(intent, 4000);









                        break;

                    case 1002:

                        list.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), list.size());
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

   /* public void UpdateData(Info info){
        mInfo.remove(info);
        mInfo.add(info);
        // notifyItemChanged(position);
     //   notifyItemChanged(info);
        notifyDataSetChanged();
    }*/

    public CustomAdapter(Context context, ArrayList<PostModel> info){
        this.list = info;
        mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewgroup, int viewType){
        View view = LayoutInflater.from(viewgroup.getContext())
                .inflate(R.layout.item, viewgroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder viewholder, final int position){

        final PostModel info = mArrayList.get(position);



        viewholder.mbubble.setImageResource(R.drawable.talkbubble);// 말풍선 이미지
      //  viewholder.mheart.setImageResource(R.drawable.heart); // 하트 이미지
      //  viewholder.mprofile.setImageBitmap(info.getProfile()); // 프로필 사진
        viewholder.mname.setText(mArrayList.get(position).name); // 이름
        viewholder.minsert.setText(mArrayList.get(position).contentWriting); // 글 내용
        viewholder.mtime.setText(mArrayList.get(position).time); // 날짜

       // viewholder.mnation.setImageDrawable(nation); // 국기 이미지
       // viewholder.mpicture.setImageBitmap(list.get(position).Picture); // 사진 첨부









         if(mArrayList.get(position).commentCount != 0) {
            viewholder.commentCount.setText(String.valueOf(mArrayList.get(position).commentCount));
        }

        if(mArrayList.get(position).likeCount != 0) { // 0이 아닐 때만 숫자 표시
            viewholder.likeCount.setText(String.valueOf(mArrayList.get(position).likeCount));
        }

      /*  if(postModel.Likes.containsKey(MyUID)){
            Log.d("알림", "좋아요 UID 이뜨아");
            viewholder.mheart.setImageResource(R.drawable.redheart);
        } else {
            Log.d("알림", "좋아요 UID 없뜨아");
            viewholder.mheart.setImageResource(R.drawable.heart);
        }*/

        Glide.with(viewholder.itemView.getContext())
                .load(mArrayList.get(position).profileString)
                .apply(new RequestOptions().circleCrop().centerCrop())
                .into(((CustomAdapter.CustomViewHolder)viewholder).mprofile);

        if(mArrayList.get(position).image != null) {
            Glide.with(viewholder.itemView.getContext())
                    .load(mArrayList.get(position).image)
                    .apply(new RequestOptions().centerCrop().override(1000,1000))
                    .into(((CustomAdapter.CustomViewHolder) viewholder).mpicture);
        }
 //.apply(new RequestOptions().centerCrop().override(1000,1000))

        if(mArrayList.get(position).nation.equals("미국")){

            viewholder.mnation.setImageResource(R.drawable.usaflag);

            // 미국 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 미국 국기 사진 출력
        } else if(mArrayList.get(position).nation.equals("중국")){


            viewholder.mnation.setImageResource(R.drawable.chineseflag);


            //중국 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 중국 사진 출력
        } else if(mArrayList.get(position).nation.equals("한국")){

            viewholder.mnation.setImageResource(R.drawable.koreaflag);



            //한국 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 한국 사진 출력
        } else if(mArrayList.get(position).nation.equals("일본")){

            viewholder.mnation.setImageResource(R.drawable.japanflag);



            //일본 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 일본 사진 출력

        }


        if(mArrayList.get(position).Likes.containsKey(MyUID)){
            Log.d("알림", "좋아요 UID 이뜨아");

            viewholder.mheart.setImageResource(R.drawable.redheart);

        } else {
            Log.d("알림", "좋아요 UID 없뜨아");

            viewholder.mheart.setImageResource(R.drawable.heart);

        }



        viewholder.mprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, UserProfileActivity.class);
                intent.putExtra("name", mArrayList.get(position).name);
                intent.putExtra("profileString", mArrayList.get(position).profileString);
                intent.putExtra("destinationUid", mArrayList.get(position).uid);
                intent.putExtra("nation",mArrayList.get(position).nation);
                mContext.startActivity(intent);

            }
        });

        viewholder.minsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, InnerStoryActivity.class);
                intent.putExtra("name", mArrayList.get(position).name);
                intent.putExtra("insert", mArrayList.get(position).contentWriting);
                intent.putExtra("time", mArrayList.get(position).time);
                intent.putExtra("profileURL", mArrayList.get(position).profileString);
                intent.putExtra("commentkey", keyList.get(position));
                intent.putExtra("nation", mArrayList.get(position).nation);
                intent.putExtra("picture", mArrayList.get(position).image);
                intent.putExtra("uid",mArrayList.get(position).uid);
                //  intent.putExtra("position", position);
                mContext.startActivity(intent);

            }
        });


        viewholder.mbubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, InnerStoryActivity.class);
                intent.putExtra("name", mArrayList.get(position).name);
                intent.putExtra("insert", mArrayList.get(position).contentWriting);
                intent.putExtra("time", mArrayList.get(position).time);
                intent.putExtra("profileURL", mArrayList.get(position).profileString);
                intent.putExtra("commentkey", keyList.get(position));
                intent.putExtra("nation", mArrayList.get(position).nation);
                intent.putExtra("picture", mArrayList.get(position).image);
                intent.putExtra("uid",mArrayList.get(position).uid);
                //  intent.putExtra("position", position);
                mContext.startActivity(intent);

            }
        });



       // viewholder.mprofile.setOnClickListener(this);


        /*FirebaseDatabase.getInstance().getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mArrayList.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){


                    mArrayList.add(snapshot.getValue(PostModel.class));

                }
               notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


       /* FirebaseDatabase.getInstance().getReference().child("posts").child(mArrayList.get(position).Likes).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo destinationuserInfo  = dataSnapshot.getValue(UserInfo.class);
                follower = destinationuserInfo.followerCount;

                Log.d("유저", "follower" + follower + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

       /* SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
        myname = SPP.getString("Name","");
        myprofileString = SPP.getString("profileString","");*/

        viewholder.mheart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("알림", "클릭이 되고있긴 하다");


                onHeartClicked(mDatabase.child("posts").child(keyList.get(position)));
               // onHeartClicked(mDatabase.child("users").child(MyUID).child("mposts").child(keyList.get(position)));

                notifyDataSetChanged();



                if(!mArrayList.get(position).Likes.containsKey(MyUID)){
                    Log.d("알림", "좋아요 UID 이뜨아");
                    String content = myname + "님이 회원님의 게시물을 좋아합니다.";
                    long nowtime = System.currentTimeMillis();
                    time = String.valueOf(nowtime);
                    ChatRoomInfo chatRoomInfo = new ChatRoomInfo(myname, profilestring, MyUID,time, Story_Activity.mynation,content);
                    mDatabase.child("notification").child(mArrayList.get(position).uid).push().setValue(chatRoomInfo);


                } else {


                }



  // Array로 바꿀까..?
            /*    if(postModel.Likes.containsKey(MyUID)) { // uid로 변경
                    Log.d("알림", "좋아요버튼 UID 있다");


                    mDatabase.child("posts").child(keyList.get(position)).child("Likes").child(MyUID).removeValue();


                } else {
                   // PostModel postModel = new PostModel();


                    mDatabase.child("posts").child(keyList.get(position)).child("Likes").setValue(postModel);
                    Log.d("알림", "좋아요버튼 UID 없다");

                }*/




            }
        });

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

                if(p.Likes.containsKey(MyUID)){
                    p.likeCount = p.likeCount -1;
                    p.Likes.remove(MyUID);
                } else {
                    p.likeCount = p.likeCount +1;
                    p.Likes.put(MyUID,true);
                }

                mutableData.setValue(p);
                return Transaction.success(mutableData);

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }


    /*public int getItemCount(){
        return (null != mInfo ? mInfo.size() : 0);
    }*/




    public void UpdateData(int position, PostModel info){ // 데이터 수정
       list.remove(position);
       list.add(position,info);
        //  list.set(Position, info);
       notifyItemChanged(position, info);
       notifyDataSetChanged();
      //  list.set(position, info);
      //  notifyItemChanged(position);
      //  notifyDataSetChanged();*//*
      //  mAdapter.notifyItemRangeChanged(position, list.size());
    }

    public void RemoveData(int position){ // 데이터 삭제
        list.remove(position);
        notifyItemRemoved(position);
       // notifyDataSetChanged();;
        notifyItemChanged(list.size());
    }

    @Override
    public int getItemCount(){ // 아이템 개수
        return list.size();
              //  (null != list ? list.size() : 0);
    }


     /*  if(rcode == 3){
                           list.set(getAdapterPosition(), info);
                           notifyItemChanged(getAdapterPosition());
                       }*/


                    /*    WriteActivity writeActivity;
                     //  edittext_writing.setText(insert);
                        edittext_writing.setText(list.get(getAdapterPosition()).getInsert());
                        Intent intent1001 = new Intent(mContext, WriteActivity.class);
                        intent1001.putExtra("입력한 내용", insert);
                       // intent1001.putExtra("입력한 내용",(Serializable) mInfo);
                      //  mContext.startActivityForResult(intent1001, 3001);
                        ((Activity) mContext).startActivityForResult(intent1001, 3001);*/

    // startActivityForResult(intent, 3000);

                      /*  View view = LayoutInflater.from(mContext).inflate(R.layout.activity_write, null, false);
                        edittext_writing.setText(WriteActivity.insert);
                        edittext_writing.setText(mInfo.get(getAdapterPosition()).getInsert());*/
    // 원래 있던 것들


    //   final EditText editText_writing = (EditText) view.findViewById(R.id.editText_writing);

    //   final Button textView_insert = (Button) itemView.findViewById(textView_insert);


    //  edittext_writing.setText(mInfo.get(getAdapterPosition()).getInsert());


                       /* writebutton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                insert = edittext_writing.getText().toString();
                                Info info = new Info(na, insert, bitmap , null, Heartclick, 0);
                                info.setInsert(insert);




                                CustomAdapter.intent_1001.putExtra("result", insert);
                                //     intent_02.putExtra("result", mtime);
                                setResult(RESULT_OK,intent_1001);

*/

                               /* mInfo.remove(getAdapterPosition());
                                mInfo.add(info);*/

                               /* list.set(getAdapterPosition(), info);
                                notifyItemChanged(getAdapterPosition());*/
    //원래 있던 것들

    // notifyDataSetChanged();




}


