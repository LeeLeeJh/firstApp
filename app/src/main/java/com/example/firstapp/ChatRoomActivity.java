package com.example.firstapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.firstapp.model.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;



public class ChatRoomActivity extends BaseActivity {

    static ChatRoomAdapter mAdapter;
    private RecyclerView mRecyclerview;
    static ArrayList<ChatRoomInfo> mArrayList;
    private LinearLayoutManager mLinearLayoutManager;
  //  static List<ChatModel> chatmodels = new ArrayList<>();
    List<String> keyList;
    static List<String> fmsg  = new ArrayList<>();
    List<String> roomkeyList;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String destinationUid, chatRoomUid;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String finalmsg, finaltime;
    TextView finaltext;
    static int position;
    String key;
    String lastMessageKey;

    ImageView Nav_Story,Nav_Chat,Nav_Noti,Nav_Profile;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        Nav_Story = (ImageView)findViewById(R.id.Nav_story);
        Nav_Noti = (ImageView)findViewById(R.id.Nav_bell);
        Nav_Profile =(ImageView)findViewById(R.id.Nav_profile);


        /*FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+MyUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatmodels.clear();
                fmsg.clear();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    chatmodels.add(item.getValue(ChatModel.class));
                    Log.d("알리미", " chatmodels" + chatmodels + "");
                }

                Map<String,ChatModel.Comment> commentMap = new TreeMap<>(Collections.<String>reverseOrder());
                commentMap.putAll(chatmodels.get(position).comments);
                Log.d("알리미", " chatmodels.get(position).comments" + chatmodels.get(position).comments + "");
                lastMessageKey = (String) commentMap.keySet().toArray()[0];
                Log.d("알리미", " ?!!!!" + chatmodels.get(position).comments.get(lastMessageKey).message + "");
                fmsg.add(0,chatmodels.get(position).comments.get(lastMessageKey).message);
                Log.d("알리미", " ?!!!!22" + fmsg.get(position) + "");
              //  finaltext = (TextView)findViewById(R.id.finalText);
              //  finaltext.setText(fmsg.get(position));

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/

      //  finaltext.setText(fmsg.get(position));

        //finaltext.setText(chatmodels.get(position).comments.get(lastMessageKey).message);




      //  destinationUid = getIntent().getStringExtra("destinationUid"); // 채팅 상대방 UID
      //  databaseReference = FirebaseDatabase.getInstance().getReference().child("photostring");
     //   FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        storageReference = FirebaseStorage.getInstance().getReference();





        mRecyclerview = (RecyclerView) findViewById(R.id.Chatroom_list);
        mRecyclerview.setAdapter(new RecyclerViewAdapter());
      //  mRecyclerview.setLayoutManager(new LinearLayoutManager(this));







        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(ChatRoomActivity.this));
        mArrayList = new ArrayList<>();
        keyList = new ArrayList<>();
        roomkeyList = new ArrayList<>();
        key = mDatabase.child("posts").push().getKey();


     //   mAdapter = new ChatRoomAdapter(ChatRoomActivity.this, mArrayList);



        // 리사이클러뷰 아이템 선으로 구분하기
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerview.getContext(),
        mLinearLayoutManager.getOrientation());
        mRecyclerview.addItemDecoration(dividerItemDecoration);







     /*   FirebaseDatabase.getInstance().getReference().child("chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 keyList.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                    String key = snapshot.getKey();

                    keyList.add(0,key);
                    Log.d("알리미", "keyList" + keyList + "");

                }
                 mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


     // 얘가 원래 있던것
       /* FirebaseDatabase.getInstance().getReference().child("chat").child(MyUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mArrayList.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                    ChatRoomInfo chatRoomInfo = snapshot.getValue(ChatRoomInfo.class);

                    if(chatRoomInfo.uid.equals(MyUID)){
                        continue;
                    }

                    // mArrayList.add(snapshot.getValue(ChatRoomInfo.class));

                    mArrayList.add(chatRoomInfo);


                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
   // 여기까지

        /*FirebaseDatabase.getInstance().getReference().child("chatrooms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                keyList.clear();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    ChatModel chatModel = item.getValue(ChatModel.class);
                    if(chatModel.users.containsKey(destinationUid)){
                        String key = item.getKey();
                        keyList.add(0,key);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/





        Nav_Story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatRoomActivity.this, Story_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        Nav_Noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatRoomActivity.this, notificationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Nav_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatRoomActivity.this, MyprofileActivity.class);
                startActivity(intent);
                finish();
            }
        });







      //  mRecyclerview.setAdapter(mAdapter); // CustomAdapter 연결




        mRecyclerview.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this);
                builder.setTitle("채팅방 나가기");
                builder.setMessage("채팅방을 나가시겠습니까?");
                builder.setPositiveButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ChatRoomActivity.this,"취소 클릭됨",Toast.LENGTH_LONG).show();
                            }
                        });
                builder.setNegativeButton("나가기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            //    mAdapter.notifyItemRemoved(position);
                                mDatabase.child("chat").child(keyList.get(position)).removeValue();

                                Toast.makeText(ChatRoomActivity.this,"나가기 클릭됨",Toast.LENGTH_LONG).show();
                            }
                        });
                builder.show();
                return false;
            }
        });


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





    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<ChatModel> chatModels = new ArrayList<>();
        private ArrayList<String> destinationUsers = new ArrayList<>();


        public RecyclerViewAdapter() {


            /*FirebaseDatabase.getInstance().getReference().child("chat").child(MyUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mArrayList.clear();
                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                        ChatRoomInfo chatRoomInfo = snapshot.getValue(ChatRoomInfo.class);

                        if(chatRoomInfo.uid.equals(MyUID)){
                            continue;
                        }

                        mArrayList.add(chatRoomInfo);


                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/


            /*FirebaseDatabase.getInstance().getReference().child("chatrooms").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    roomkeyList.clear();
                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                        String key = snapshot.getKey();

                        roomkeyList.add(key);


                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/



            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+MyUID).equalTo(true).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    chatModels.clear();
                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        chatModels.add(item.getValue(ChatModel.class));
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_room_item, viewGroup, false);


            return new ChatRoomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
            final ChatRoomViewHolder ChatRoomViewHolder = ((RecyclerViewAdapter.ChatRoomViewHolder) viewHolder);

            destinationUid = null;


            for(String user: chatModels.get(position).users.keySet()){
                if(!user.equals(MyUID)){
                    destinationUid = user;
                    destinationUsers.add(destinationUid);
                }
            }

            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                    Glide.with(ChatRoomViewHolder.itemView.getContext())
                            .load(userInfo.downloadUrl)
                            .apply(new RequestOptions().circleCrop().centerCrop())
                            .into(ChatRoomViewHolder.listprofile);


                    if (userInfo.nation.equals("미국")) {

                        ChatRoomViewHolder.nation.setImageResource(R.drawable.usaflag);

                    } else if (userInfo.nation.equals("중국")) {

                        ChatRoomViewHolder.nation.setImageResource(R.drawable.chineseflag);

                    } else if (userInfo.nation.equals("한국")) {

                        ChatRoomViewHolder.nation.setImageResource(R.drawable.koreaflag);

                    } else if (userInfo.nation.equals("일본")) {

                        ChatRoomViewHolder.nation.setImageResource(R.drawable.japanflag);

                    }

                    ChatRoomViewHolder.listusername.setText(userInfo.name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            // 메시지를 내림차순으로 정렬 후 마지막 메시지의 키값을 가져옴
            if(chatModels != null) {
                Map<String, ChatModel.Comment> commentMap = new TreeMap<>(Collections.<String>reverseOrder());
                commentMap.putAll(chatModels.get(position).comments);
                Log.d("알리미", " chatmodels.get(position).comments" + chatModels.get(position).comments + "");
                    lastMessageKey = (String) commentMap.keySet().toArray()[0];
                    Log.d("알리미", " ?!!!!" + chatModels.get(position).comments.get(lastMessageKey).message + "");
                    ChatRoomViewHolder.finalmsg.setText(chatModels.get(position).comments.get(lastMessageKey).message);
                    String time = chatModels.get(position).comments.get(lastMessageKey).timestamp.toString();
                    long time2 = Long.parseLong(time);
                    Date date = new Date(time2);
                    SimpleDateFormat dataf = new SimpleDateFormat("MM-dd hh:mm");
                    String time3 = dataf.format(date);
                    ChatRoomViewHolder.time.setText(time3);
            }



            ChatRoomViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid", destinationUsers.get(position));
                    view.getContext().startActivity(intent);
                }

            });


        }


        class ChatRoomViewHolder extends RecyclerView.ViewHolder {


            private ImageView listprofile;
            private TextView listusername;
            private TextView time, finalmsg;
            private ImageView nation;

            public ChatRoomViewHolder(View view) {
                super(view);

                listprofile = (ImageView) view.findViewById(R.id.chatroom_profile); // 채팅방 리스트 프로필 사진
                listusername = (TextView) view.findViewById(R.id.chatroom_username); // 채팅방 리스트 유저 이름
                time = (TextView) view.findViewById(R.id.time_textView);
                nation = (ImageView) view.findViewById(R.id.chatroom_nation);
                finalmsg = (TextView) view.findViewById(R.id.finalText);
            }


        }

        @Override
        public int getItemCount() {

            return chatModels.size();

        }


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

    @Override
    protected void onStart() {
        super.onStart();




    }

}
