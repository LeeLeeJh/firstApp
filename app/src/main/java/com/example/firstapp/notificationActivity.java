package com.example.firstapp;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class notificationActivity extends AppCompatActivity {

    static notifyAdapter mAdapter;
    private RecyclerView mRecyclerview;
    ArrayList<ChatRoomInfo> mArrayList;
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    List<String> keyList = new ArrayList<>();
    ImageView Nav_Story,Nav_Chat,Nav_Noti,Nav_Profile;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView_notification);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(notificationActivity.this));
        mArrayList = new ArrayList<>();



        mAdapter = new notifyAdapter(notificationActivity.this, mArrayList);
        mRecyclerview.setAdapter(mAdapter); // CustomAdapter 연결


        // 리사이클러뷰 아이템 선으로 구분하기
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerview.getContext(),
        mLinearLayoutManager.getOrientation());
        mRecyclerview.addItemDecoration(dividerItemDecoration);



        Nav_Story = (ImageView)findViewById(R.id.Nav_story);
        Nav_Chat = (ImageView)findViewById(R.id.Nav_chat);
        Nav_Profile =(ImageView)findViewById(R.id.Nav_profile);



        final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("notification").child(MyUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mArrayList.clear();
                keyList.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                     String key = snapshot.getKey();

                     keyList.add(0,key);
                     mArrayList.add(0,snapshot.getValue(ChatRoomInfo.class));


                    Log.d("알리미", "mArrayList" + mArrayList + "");

                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Nav_Story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notificationActivity.this, Story_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        Nav_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notificationActivity.this, ChatRoomActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Nav_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notificationActivity.this, MyprofileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // remove item from adapter
                final int position = viewHolder.getAdapterPosition();

                // 데이터의 해당 포지션을 삭제한다
               // showToast("on remove " + mList.remove(position));
                // 아답타에게 알린다
                mAdapter.notifyItemRemoved(position);
                mDatabase.child("notification").child(MyUID).child(keyList.get(position)).removeValue();

            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                // move item in `fromPos` to `toPos` in adapter.
                return true;// true if moved, false otherwise
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerview);

    }


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


}
