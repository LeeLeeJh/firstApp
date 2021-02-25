package com.example.firstapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.List;


import static com.example.firstapp.Story_Activity.myname;
import static com.example.firstapp.UserProfileActivity.destinationUid;
import static com.example.firstapp.UserProfileActivity.keyList;
import static com.example.firstapp.UserProfileActivity.uArrayList;

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder> {


        ArrayList<PostModel> list;
        private Context mContext;

        int a;
        private RecyclerView recyclerView;
        private static final String TAG= "Dongle";
        static int mPosition;
        static TextView mlike;
        SharedPreferences SPP;
        SharedPreferences.Editor Adaptereditor;
        Uri myphotouri;
        private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();;
        private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        static PostModel p;



   /* List<ChatModel.Comment> comments;
    public  UserProfileAdapter() {

        comments = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userPost = dataSnapshot.getValue(PostModel.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }*/
        public class UserProfileViewHolder extends RecyclerView.ViewHolder { // 리사이클러뷰에 반복될 아이템 레이아웃 연결

            protected TextView mname;
            protected TextView minsert;
            //   MyprofileActivity myprofileActivity;
            protected ImageView mprofile;
            protected ImageView mheart;
            protected TextView likeCount;
            protected TextView mtime;
            protected ImageView mnation;
            protected TextView deletebutton;
            protected ImageView mpicture;
            protected ImageView mbubble;
            protected  TextView commentCount;

            public UserProfileViewHolder(View view) {
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



            }


        }

        public UserProfileAdapter(Context context, ArrayList<PostModel> info){
            this.list = info;
            mContext = context;
        }

        @Override
        public UserProfileViewHolder onCreateViewHolder(ViewGroup viewgroup, int viewType){
            View view = LayoutInflater.from(viewgroup.getContext())
                    .inflate(R.layout.item, viewgroup, false);

            UserProfileViewHolder viewHolder = new UserProfileViewHolder(view);



            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull UserProfileViewHolder viewholder, final int position){

            final PostModel info = list.get(position);



            viewholder.mbubble.setImageResource(R.drawable.talkbubble);// 말풍선 이미지
            viewholder.mheart.setImageResource(R.drawable.heart); // 하트 이미지
            //  viewholder.mprofile.setImageBitmap(info.getProfile()); // 프로필 사진
            viewholder.mname.setText(uArrayList.get(position).name); // 이름
            viewholder.minsert.setText(uArrayList.get(position).contentWriting); // 글 내용
            viewholder.mtime.setText(uArrayList.get(position).time); // 날짜
            // viewholder.mnation.setImageDrawable(nation); // 국기 이미지
            // viewholder.mpicture.setImageBitmap(list.get(position).Picture); // 사진 첨부


            if(uArrayList.get(position).commentCount != 0) { // 댓글 수 0이 아닐 때만 숫자 표시
                viewholder.commentCount.setText(String.valueOf(uArrayList.get(position).commentCount));
            }

            if(uArrayList.get(position).likeCount != 0) { // 좋아요 수 0이 아닐 때만 숫자 표시
                viewholder.likeCount.setText(String.valueOf(uArrayList.get(position).likeCount));
            }



            Glide.with(viewholder.itemView.getContext())
                    .load(uArrayList.get(position).profileString)
                    .apply(new RequestOptions().circleCrop().centerCrop())
                    .into(viewholder.mprofile);

            Glide.with(viewholder.itemView.getContext())
                    .load(uArrayList.get(position).image)
                    .apply(new RequestOptions().centerCrop().override(1000,1000))
                    .into(viewholder.mpicture);


            if(uArrayList.get(position).nation.equals("미국")){

                viewholder.mnation.setImageResource(R.drawable.usaflag);

                // 미국 국기 사진 가져와서
                //   mnation.setImageDrawable(img_nation);// 미국 국기 사진 출력
            } else if(uArrayList.get(position).nation.equals("중국")){


                viewholder.mnation.setImageResource(R.drawable.chineseflag);


                //중국 국기 사진 가져와서
                //   mnation.setImageDrawable(img_nation);// 중국 사진 출력
            } else if(uArrayList.get(position).nation.equals("한국")){

                viewholder.mnation.setImageResource(R.drawable.koreaflag);



                //한국 국기 사진 가져와서
                //   mnation.setImageDrawable(img_nation);// 한국 사진 출력
            } else if(uArrayList.get(position).nation.equals("일본")){

                viewholder.mnation.setImageResource(R.drawable.japanflag);



                //일본 국기 사진 가져와서
                //   mnation.setImageDrawable(img_nation);// 일본 사진 출력

            }






            if(uArrayList.get(position).Likes.containsKey(userId)){
                Log.d("알림", "좋아요 UID 이뜨아");

                viewholder.mheart.setImageResource(R.drawable.redheart);

            } else {
                Log.d("알림", "좋아요 UID 없뜨아");

                viewholder.mheart.setImageResource(R.drawable.heart);

            }


            viewholder.minsert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, InnerStoryActivity.class);
                    intent.putExtra("name", uArrayList.get(position).name);
                    intent.putExtra("insert", uArrayList.get(position).contentWriting);
                    intent.putExtra("time", uArrayList.get(position).time);
                    intent.putExtra("profileURL", uArrayList.get(position).profileString);
                    intent.putExtra("commentkey", keyList.get(position));
                    intent.putExtra("picture", uArrayList.get(position).image);
                    intent.putExtra("nation", uArrayList.get(position).nation);
                    intent.putExtra("uid",uArrayList.get(position).uid);
                    //  intent.putExtra("position", position);
                    mContext.startActivity(intent);

                }
            });


            viewholder.mbubble.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, InnerStoryActivity.class);
                    intent.putExtra("name", uArrayList.get(position).name);
                    intent.putExtra("insert", uArrayList.get(position).contentWriting);
                    intent.putExtra("time", uArrayList.get(position).time);
                    intent.putExtra("profileURL", uArrayList.get(position).profileString);
                    intent.putExtra("commentkey", keyList.get(position));
                    intent.putExtra("picture", uArrayList.get(position).image);
                    intent.putExtra("nation", uArrayList.get(position).nation);
                    intent.putExtra("uid",uArrayList.get(position).uid);
                    //  intent.putExtra("position", position);
                    mContext.startActivity(intent);
                }
            });


            viewholder.mheart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onHeartClicked(mDatabase.child("posts").child(keyList.get(position)));


                    notifyDataSetChanged();



                    /*if(!uArrayList.get(position).Likes.containsKey(userId)){
                        Log.d("알림", "좋아요 UID 이뜨아");
                        String content = myname + "님이 회원님의 게시물을 좋아합니다.";
                        long nowtime = System.currentTimeMillis();
                        time = String.valueOf(nowtime);
                        ChatRoomInfo chatRoomInfo = new ChatRoomInfo(myname, profilestring, MyUID,time, Story_Activity.mynation,content);
                        mDatabase.child("notification").child(mArrayList.get(position).uid).push().setValue(chatRoomInfo);


                    } else {


                    }
*/

                }
            });




        }









        @Override
        public int getItemCount(){ // 아이템 개수
            return list.size();

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
                    p.Likes.put(userId,true);
                }

                mutableData.setValue(p);
                return Transaction.success(mutableData);

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }









}
