package com.example.firstapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowerActivity extends AppCompatActivity {

    static FollowAdapter mAdapter;
    private RecyclerView mRecyclerview;
    ArrayList<ChatRoomInfo> mArrayList;
    private LinearLayoutManager mLinearLayoutManager;
    static int position;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);


        mRecyclerview = (RecyclerView) findViewById(R.id.Follower_recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(FollowerActivity.this));
        mArrayList = new ArrayList<>();



        mAdapter = new FollowAdapter(FollowerActivity.this, mArrayList);



        // 리사이클러뷰 아이템 선으로 구분하기
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerview.getContext(),
        mLinearLayoutManager.getOrientation());
        mRecyclerview.addItemDecoration(dividerItemDecoration);


        final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("users").child(MyUID).child("follower").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mArrayList.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                     ChatRoomInfo chatRoomInfo = snapshot.getValue(ChatRoomInfo.class);

                    /*if(chatRoomInfo.uid.equals(MyUID)){
                        continue;
                    }*/



                     mArrayList.add(chatRoomInfo);
                    Log.d("팔로워알리미", "mArrayList" + mArrayList + "");

                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRecyclerview.setAdapter(mAdapter); // Adapter 연결

        mRecyclerview.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //   PostModel info = mArrayList.get(position);
                //   Toast.makeText(getApplicationContext(),  info.getInsert()+' '+info.getName()+' '+info.getTime(), Toast.LENGTH_LONG).show();




                Intent intent = new Intent(getBaseContext(), UserProfileActivity.class);
                intent.putExtra("name", mArrayList.get(position).userName);
                intent.putExtra("profileString", mArrayList.get(position).profileString);
                intent.putExtra("destinationUid", mArrayList.get(position).uid);
                intent.putExtra("nation", mArrayList.get(position).nation);
                startActivity(intent);


            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));


    } // end of create

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector; // 제스처 이벤트
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
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


}
