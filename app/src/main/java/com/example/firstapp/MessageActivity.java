package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.firstapp.model.ChatModel;
import com.example.firstapp.model.NotificationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.firstapp.Register2Activity.inputname;

public class MessageActivity extends AppCompatActivity {

    private String destinationUid;
    private Button button;
    private EditText editText;
    private TextView textView;
    private ImageView translationButton;

    SharedPreferences SPP;

    private String uid;
    private String chatRoomUid;
    private UserInfo destinationUserinfo;
    String myname, trans;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    int peopleCount = 0;



    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    private RecyclerView recyclerView;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // 로그인된 UID
        destinationUid = getIntent().getStringExtra("destinationUid"); // 채팅 상대방 UID
      //  key = getIntent().getStringExtra("roomkey");
        button = (Button) findViewById(R.id.messagebutton);
        editText = (EditText) findViewById(R.id.Chat_Insert_Text);
        recyclerView = (RecyclerView) findViewById(R.id.messageActivity_recyclerview);
        textView = (TextView) findViewById(R.id.textView_roomName);
        translationButton = (ImageView) findViewById(R.id.message_trans);


        SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
        myname = SPP.getString("Name","");




        translationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, translationActivity.class);
                startActivity(intent);
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(uid,true);
                chatModel.users.put(destinationUid,true); // 상대방 UID





                if(chatRoomUid == null) {      // key부분 push였엉
                    button.setEnabled(false); // 데이터 가져오는 동안 버그 발생 방지 버튼 사용 못하게 함
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) { // 데이터 입력이 완료 되었을 때
                            checkChatRoom(); // 데이터 체크
                        }
                    });

                   // checkChatRoom();

                } else {
                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = uid;
                    comment.message = editText.getText().toString();
                    comment.timestamp = ServerValue.TIMESTAMP;

                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            sendGcm();
                            editText.setText(""); // 메세지 보낸 후 입력창 초기화

                        }
                    });

                }
                }
        });
        checkChatRoom();
    } // end of create






    @Override
    protected void onResume() {
        super.onResume();

        SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
        trans = SPP.getString("copy","");
        Log.d("copy2", "trans" + trans);
        editText.setText(trans);
        SharedPreferences.Editor editor = SPP.edit();
        editor.putString("copy","");
        editor.commit();
    }

    void sendGcm(){
        Gson gson = new Gson();


        NotificationModel notificationModel = new NotificationModel();
        notificationModel.to = destinationUserinfo.pushToken;
        notificationModel.notification.title = myname;
        notificationModel.notification.text = editText.getText().toString();
        notificationModel.data.title = myname;
        notificationModel.data.text = editText.getText().toString();


        // 알림 설정
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"),gson.toJson(notificationModel));

        Request request = new Request.Builder()
        .header("Content-Type","application/json")
                .addHeader("Authorization","key=AAAAfNCUH6g:APA91bGpERFNyufd7Sy12CUQkhQJhXcyTA76y6Vi0AzDFa18Twqv6lMlaTcssYU_hWFGnW2FW8qnjqBlG2apFX4FOUBBarZbzcZZNR_wKPvSBWaLwG7VYmdVNvT5xwYWD-ERQhUHIeWN")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }



    void checkChatRoom(){
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    ChatModel chatModel = item.getValue(ChatModel.class);
                    if(chatModel.users.containsKey(destinationUid)){
                        chatRoomUid = item.getKey();
                        button.setEnabled(true); // 버튼 클릭할 수 있게
                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                        recyclerView.setAdapter(new RecyclerViewAdapter());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

 class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


        List<ChatModel.Comment> comments;
        public  RecyclerViewAdapter(){
            comments = new ArrayList<>();

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
          /* databaseReference = FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments");
           valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {*/
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    comments.clear(); // 클리어를 하지 않으면 데이터가 계속 쌓이게 된다.
                    /*Map<String,Object> readUsersMap = new HashMap<>();*/
                    for(DataSnapshot item : dataSnapshot.getChildren()){
                       /* String key = item.getKey();
                        ChatModel.Comment comment_origin = item.getValue(ChatModel.Comment.class);
                        ChatModel.Comment comment_motify = item.getValue(ChatModel.Comment.class);
                        comment_motify.readUsers.put(uid,true);

                        readUsersMap.put(key,comment_motify);
                        comments.add(comment_origin);*/
                        comments.add(item.getValue(ChatModel.Comment.class));
                    }

                    /*if(comments.size() == 0) {
                        return;
                    }*/
                      /*  if (comments.get(comments.size() - 1).readUsers.containsKey(uid)) {
                            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments")
                                    .updateChildren(readUsersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // 메세지 갱신
                                    notifyDataSetChanged();
                                    recyclerView.scrollToPosition(comments.size() - 1); // 리사이클러뷰 스크롤 마지막 메시지로 이동
                                }
                            });
                        } else {*/
                            notifyDataSetChanged();
                            recyclerView.scrollToPosition(comments.size() - 1);
                       // }
                    }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

     @NonNull
     @Override
     public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message,viewGroup, false);


         return new MessageViewHolder(view);
     }

     @Override
     public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
          MessageViewHolder messageViewHolder = ((MessageViewHolder)viewHolder);


          // 내가 보낸 메세지
         if(comments.get(position).uid.equals(uid)) { // comments 안에 있는 uid, 내 uid
             messageViewHolder.textView_message.setText(comments.get(position).message);
             messageViewHolder.textView_message.setBackgroundResource(R.drawable.rightbubble);
             messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE); // 내 정보를 안보이게 함(내가 보내는 메세지에서는 프로필 사진, 이름 필요 없음)
             messageViewHolder.textView_message.setTextSize(15);
             messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT); // 말풍선 오른쪽 정렬
          //   setReadCounter(position, messageViewHolder.textView_readCounter_left);

          // 상대방이 보낸 메세지
         } else {
             Glide.with(viewHolder.itemView.getContext())
                     .load(destinationUserinfo.downloadUrl)
                     .apply(new RequestOptions().circleCrop().centerCrop())
                     .into(messageViewHolder.imageView_profile);
             messageViewHolder.textView_name.setText(destinationUserinfo.name);
             messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
             messageViewHolder.textView_message.setBackgroundResource(R.drawable.leftbubble);
             messageViewHolder.textView_message.setText(comments.get(position).message);
             messageViewHolder.textView_message.setTextSize(15);
             messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT); // 말풍선 왼쪽 정렬
            // setReadCounter(position, messageViewHolder.getTextView_readCounter_right);
         }

         long unixTime = (long) comments.get(position).timestamp;
         Date date = new Date(unixTime);
         simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/seoul"));
         String time = simpleDateFormat.format(date);
         messageViewHolder.textView_time.setText(time);
     }

    /* void setReadCounter(final int position, final TextView textView) {

         if (peopleCount == 0) {
             FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     Map<String, Boolean> users = (Map<String, Boolean>) dataSnapshot.getValue();
                     peopleCount = users.size() - 1;

                     int count = peopleCount - comments.get(position).readUsers.size();
                     if (count > 0) {
                         textView.setVisibility(View.VISIBLE);
                         textView.setText(String.valueOf(count));
                     } else {
                         textView.setVisibility(View.INVISIBLE);
                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
         } else {
             int count = peopleCount - comments.get(position).readUsers.size();
             if (count > 0) {
                 textView.setVisibility(View.VISIBLE);
                 textView.setText(String.valueOf(count));
             } else {
                 textView.setVisibility(View.INVISIBLE);
             }
         }
     }*/
     @Override
     public int getItemCount() {
         return comments.size();
     }

    private class MessageViewHolder extends RecyclerView.ViewHolder{
            public TextView textView_message;
            public TextView textView_name;
            public ImageView imageView_profile;
            public LinearLayout linearLayout_destination;
            public LinearLayout linearLayout_main;
            public TextView textView_time;
            public TextView textView_readCounter_left;
            public TextView TextView_readCounter_right;

            public MessageViewHolder(View view){
                super(view);
                textView_message = (TextView) view.findViewById(R.id.messageItem_textview_message);
                textView_name = (TextView) view.findViewById(R.id.messageItem_textview_name);
                imageView_profile = (ImageView) view.findViewById(R.id.messageItem_imageview_profile);
                linearLayout_destination = (LinearLayout) view.findViewById(R.id.messageItem_linearlayout_destination);
                linearLayout_main = (LinearLayout) view.findViewById(R.id.messageItem_linearlayout_main);
                textView_time = (TextView) view.findViewById(R.id.messageItem_textview_time);
            //    textView_readCounter_left = (TextView) view.findViewById(R.id.messageItem_textview_readCounter_left);
              //  TextView_readCounter_right = (TextView) view.findViewById(R.id.messageItem_textview_readCounter_right);


            }
     }


    }

   /* public void onBackPressed(){
        databaseReference.removeEventListener(valueEventListener);
        finish();
    }
*/

}
