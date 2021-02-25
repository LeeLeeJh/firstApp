package com.example.firstapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class notifyAdapter extends RecyclerView.Adapter<com.example.firstapp.notifyAdapter.notifyViewHolder> {

    ArrayList<ChatRoomInfo> chatroomlist = new ArrayList<>();
    private Context mContext;


    public class notifyViewHolder extends RecyclerView.ViewHolder { // 리사이클러뷰에 반복될 아이템 레이아웃 연결

        protected ImageView listprofile;
        protected TextView listusername;
        TextView time;
        ImageView nation;

        public notifyViewHolder(View view) {
            super(view);


            listprofile = (ImageView) view.findViewById(R.id.notify_profile); // 채팅방 리스트 프로필 사진
            listusername = (TextView) view.findViewById(R.id.notify_notification); // 채팅방 리스트 유저 이름
            time = (TextView) view.findViewById(R.id.notify_time);
            nation = (ImageView) view.findViewById(R.id.notify_nation);

        }


    }


    public notifyAdapter(Context context, ArrayList<ChatRoomInfo> chatroominfo) {
        this.chatroomlist = chatroominfo;
        mContext = context;
    }

    @Override
    public notifyAdapter.notifyViewHolder onCreateViewHolder(ViewGroup viewgroup, int viewType) {
        View view = LayoutInflater.from(viewgroup.getContext())
                .inflate(R.layout.notifyitem, viewgroup, false);

        notifyAdapter.notifyViewHolder viewHolder = new notifyAdapter.notifyViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull notifyAdapter.notifyViewHolder viewholder, final int position) {

        final ChatRoomInfo info = chatroomlist.get(position);



        Glide.with(viewholder.itemView.getContext())
                .load(chatroomlist.get(position).profileString)
                .apply(new RequestOptions().circleCrop().centerCrop())
                .into(viewholder.listprofile);

        //  viewholder.listprofile.setImageURI(chatroomlist.get(position).profilestring);// 프로필 이미지
        viewholder.listusername.setText(chatroomlist.get(position).content); // 유저 이름


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
                Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
                intent.putExtra("name", chatroomlist.get(position).userName);
                intent.putExtra("profileString", chatroomlist.get(position).profileString);
                intent.putExtra("destinationUid", chatroomlist.get(position).uid);
                intent.putExtra("nation",chatroomlist.get(position).nation);
                view.getContext().startActivity(intent);
            }

        });



    }




    @Override
    public int getItemCount () { // 아이템 개수
        return chatroomlist.size();
        //  (null != list ? list.size() : 0);
    }

}