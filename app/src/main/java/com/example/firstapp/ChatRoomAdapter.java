package com.example.firstapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.firstapp.model.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {
    ArrayList<ChatRoomInfo> chatroomlist = new ArrayList<>();
    private Context mContext;
    static List<ChatModel> chatmodels = new ArrayList<>();
    String MyUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    static String lastMessageKey, lastMsg;

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder { // 리사이클러뷰에 반복될 아이템 레이아웃 연결

        protected ImageView listprofile;
        protected TextView listusername;
        TextView time, finalmsg;
        ImageView nation;


        public ChatRoomViewHolder(View view) {
            super(view);


            listprofile = (ImageView) view.findViewById(R.id.chatroom_profile); // 채팅방 리스트 프로필 사진
            listusername = (TextView) view.findViewById(R.id.chatroom_username); // 채팅방 리스트 유저 이름
            time = (TextView) view.findViewById(R.id.time_textView);
            nation = (ImageView) view.findViewById(R.id.chatroom_nation);
            finalmsg = (TextView) view.findViewById(R.id.finalText);

        }


    }


    public ChatRoomAdapter(Context context, ArrayList<ChatRoomInfo> chatroominfo) {
        this.chatroomlist = chatroominfo;
        mContext = context;
    }

    @Override
    public ChatRoomAdapter.ChatRoomViewHolder onCreateViewHolder(ViewGroup viewgroup, int viewType) {
        View view = LayoutInflater.from(viewgroup.getContext())
                .inflate(R.layout.chat_room_item, viewgroup, false);

        ChatRoomAdapter.ChatRoomViewHolder viewHolder = new ChatRoomAdapter.ChatRoomViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomAdapter.ChatRoomViewHolder viewholder, final int position) {

        final ChatRoomInfo info = chatroomlist.get(position);


        /*FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+MyUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatmodels.clear();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    chatmodels.add(item.getValue(ChatModel.class));
                    Log.d("알리미", " chatmodels" + chatmodels + "");
                }


                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Map<String,ChatModel.Comment> commentMap = new TreeMap<>(Collections.<String>reverseOrder());
        commentMap.putAll(chatmodels.get(position).comments);
        Log.d("알리미", " chatmodels.get(position).comments" + chatmodels.get(position).comments + "");
        lastMessageKey = (String) commentMap.keySet().toArray()[0];
        Log.d("알리미", " ?!!!!" + chatmodels.get(position).comments.get(lastMessageKey).message + "");*/


            viewholder.finalmsg.setText(ChatRoomActivity.fmsg.get(position));


        if (chatroomlist.get(position).nation.equals("미국")) {

            viewholder.nation.setImageResource(R.drawable.usaflag);

            // 미국 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 미국 국기 사진 출력
        } else if (chatroomlist.get(position).nation.equals("중국")) {


            viewholder.nation.setImageResource(R.drawable.chineseflag);


            //중국 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 중국 사진 출력
        } else if (chatroomlist.get(position).nation.equals("한국")) {

            viewholder.nation.setImageResource(R.drawable.koreaflag);


            //한국 국기 사진 가져와서
            //   mnation.setImageDrawable(img_nation);// 한국 사진 출력
        } else if (chatroomlist.get(position).nation.equals("일본")) {

            viewholder.nation.setImageResource(R.drawable.japanflag);
        }



        Glide.with(viewholder.itemView.getContext())
                .load(chatroomlist.get(position).profileString)
                .apply(new RequestOptions().circleCrop().centerCrop())
                .into(((ChatRoomViewHolder)viewholder).listprofile);

      //  viewholder.listprofile.setImageURI(chatroomlist.get(position).profilestring);// 프로필 이미지
        viewholder.listusername.setText(chatroomlist.get(position).userName); // 유저 이름


        if(chatroomlist.get(position).time != null) {
            long now = System.currentTimeMillis();
            long t = Long.parseLong(chatroomlist.get(position).time);
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

            viewholder.time.setText(time_ago);
        }




        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MessageActivity.class);
                intent.putExtra("destinationUid", chatroomlist.get(position).uid);
                view.getContext().startActivity(intent);
            }

        });




    }


    @Override
    public int getItemCount() { // 아이템 개수
        return chatroomlist.size();
        //  (null != list ? list.size() : 0);
    }





}
