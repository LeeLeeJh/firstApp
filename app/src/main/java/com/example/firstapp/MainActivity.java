package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SharedMemory;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.w3c.dom.Text;

import static com.example.firstapp.RegisterActivity.a;
import static com.example.firstapp.Story_Activity.autologin;

public class MainActivity extends AppCompatActivity {

    EditText idText, pwdText;
    RegisterActivity registerActivity;
    String loginId, loginPwd;
    String id, pwd;
    CheckBox checkBox;
    Boolean savelogin;

    SharedPreferences SPP ;
    SharedPreferences.Editor editor;
    String save, alreadyInfo;

    private static String TAG = "LoginActivity";
    private DatabaseReference mDatabase;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener authStateListener; // 로그인 확인 리스너


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
      //  firebaseAuth.signOut(); // 로그아웃!!


        idText = (EditText) findViewById(R.id.idText);
        pwdText = (EditText) findViewById(R.id.passwordText);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        TextView register = (TextView) findViewById(R.id.register); // 회원가입 버튼
        checkBox = (CheckBox) findViewById(R.id.logincheckBox); // 자동로그인 체크박스
        autologin = 0;

      //  Boolean validation = login
        /*Intent Rintent = getIntent();

        String id = Rintent.getStringExtra("입력한 아이디");
        String ps = Rintent.getStringExtra("입력한 패스워드");

        idText.setText(String.valueOf(id));
        passwordText.setText(String.valueOf(ps));*/

         // 로딩페이지
        Intent spintent = new Intent(this, SplashActivity.class);
        startActivity(spintent);


       /* ValueEventListener infoListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                loginId = userInfo.getId();
                loginPwd = userInfo.getPwd();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabase.addValueEventListener(infoListener);*/ // Auth로 대체





        SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
        editor = SPP.edit();
       // editor.clear();
       // editor.commit();
      //  loginId = SPP.getString("Id",""); // 파이어베이스
      //  loginPwd = SPP.getString("Pwd",""); // 파이어베이스
      //  Toast.makeText(MainActivity.this, SPP.getString("Id","") + SPP.getString("Pwd",""), Toast.LENGTH_LONG).show();
        savelogin = SPP.getBoolean("SAVE_LOGIN", false);
        save = SPP.getString("Name","");
        alreadyInfo = SPP.getString("alreadyInfo","");
        Log.e(TAG, "onAuthStateChanged: "+alreadyInfo );

      //  Toast.makeText(MainActivity.this, "save" + save + "", Toast.LENGTH_LONG).show();
        // JSON에서 불러오기!!!! 



     //   id = idText.getText().toString(); // 로그인창 아이디 입력값
     //   pwd = pwdText.getText().toString(); // 로그인창 패스워드 입력값

        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                autologin = 1;
                savelogin = true;

            }
        });



        // 자동 로그인시 메인 보이게 해놓음 -> 나중에 Register2로 수정 //
        if(savelogin == true) {

                Intent intent = new Intent(getApplicationContext(),Story_Activity.class);
                startActivity(intent);
                finish();

        }



        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loginEvent();
               // id = idText.getText().toString(); // 로그인창 아이디 입력값
             //   pwd = pwdText.getText().toString(); // 로그인창 패스워드 입력값


               /* if(loginId.equals(id) && loginPwd.equals(pwd)){
                    Toast.makeText(MainActivity.this, "로그인 되었습니다.", Toast.LENGTH_LONG).show();


                    if(save == ""){
                        Intent intent = new Intent(MainActivity.this, Register2Activity.class);
                        startActivity(intent);
                        finish();

                    } else if(save != "false"){
                        Intent intent = new Intent(MainActivity.this, Story_Activity.class);
                        startActivity(intent);
                        finish();
                    }




                    if(autologin == 1){
                        SharedPreferences.Editor logineditor = SPP.edit();
                        logineditor.putBoolean("SAVE_LOGIN", true);
                        //   logineditor.putString("loginId", id);
                        //   logineditor.putString("loginPwd", pwd);
                        logineditor.commit();
                    }


                } else {
                    Toast.makeText(MainActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, id +  pwd + autologin, Toast.LENGTH_SHORT).show();
                }
*/




            }
        });


        // 로그인 인터페이스 리스너
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    // 로그인

                    /*// 이메일 입력 확인

                    if( idText.getText().toString().length() == 0 ) {

                        Toast.makeText(MainActivity.this, "이메일을 입력하세요!", Toast.LENGTH_SHORT).show();

                        idText.requestFocus();

                        return;

                    }



                    // 비밀번호 입력 확인

                    if( pwdText.getText().toString().length() == 0 ) {

                        Toast.makeText(MainActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();

                        pwdText.requestFocus();

                        return;

                    }*/

                  /*  String uid = firebaseUser.getUid();
                    DatabaseReference Ref;
                    Ref = FirebaseDatabase.getInstance().getReference("users");
                    Ref.orderByChild("name").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }); *//*{
                                                                                  @Override
                                                                                  public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                                                                                      Dinosaur dinosaur = dataSnapshot.getValue(Dinosaur.class);
                                                                                      System.out.println(dataSnapshot.getKey() + " was " + dinosaur.height + " meters tall.");
                                                                                  }

                                                                              }*/

                    Log.e(TAG, "onAuthStateChanged: "+alreadyInfo );
if(alreadyInfo.equals("name")){

    Log.e(TAG, "onAuthStateChanged: 실행중이야" );

    Intent intent = new Intent(getApplicationContext(),Story_Activity.class);
    startActivity(intent);
    finish();


} else {

    Log.e(TAG, "onAuthStateChanged: 실행 안되고있어" );
    id = idText.getText().toString(); // 로그인 아이디
    pwd = pwdText.getText().toString(); // 로그인 패스워드

    Intent intent = new Intent(MainActivity.this, Register2Activity.class);

    intent.putExtra("loginID", id);

    startActivity(intent);
    finish();


}



                } else { // 로그인 기록 있을 때(자동 로그인)




                }
            }
        };






        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, RegisterActivity.class));

                finish();

            }
        });
    } // end of create



    void loginEvent(){
        firebaseAuth.signInWithEmailAndPassword(idText.getText().toString(), pwdText.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                             // 로그인 실패한 부분
                            //Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, "이메일 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }





    /*private void save(){
        SharedPreferences.Editor logineditor = SPP.edit();
        logineditor.putBoolean("SAVE_LOGIN", checkBox.isChecked());
        logineditor.putString("loginId", idText.getText().toString().trim());
        logineditor.putString("loginPwd", pwdText.getText().toString().trim());
        logineditor.commit();
    }
*/


   /* private boolean loginValidation(String id, String pwd){
        if(SPP.getString("inputId",null).equals(id) && SPP.getString("inputPwd","").equals(pwd)){
            return true;
        } else if(SPP.getString("inputId",null).equals(null)){
            Toast.makeText(MainActivity.this, "Please Sign in first", Toast.LENGTH_LONG).show();
            return false;
        } else {
            // login fale;
            return false;
        }

    }
*/



   /* @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);



        // setResult를 통해 받아온 요청번호, 상태, 데이터

        Log.d("RESULT", requestCode + "");

        Log.d("RESULT", resultCode + "");

        Log.d("RESULT", data + "");



        if(requestCode == 1000 && resultCode == RESULT_OK) {

            Toast.makeText(MainActivity.this, "회원가입을 완료했습니다!", Toast.LENGTH_SHORT).show();

            idText.setText(data.getStringExtra("email"));

        }

    }*/

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }






/*
    @Override
    protected void onStart() {
        super.onStart();
        // 화면 기능을 실행할 준비를 함ㅁ
        Toast.makeText(getApplicationContext(), "onStart() 호출됨", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 화면의 기능을 중지시키는 과정
        Toast.makeText(getApplicationContext(), "onStop() 호출됨", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 화면을 메모리에서 없애버른 과정
        Toast.makeText(getApplicationContext(), "onDestroy() 호출됨", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 화면에서 보이지 않도록 하는 과정
        // 화면의 상태를 임시 저장
        Toast.makeText(getApplicationContext(), "onPause() 호출됨", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 화면에 보여주기 전에 준비를 끝냄
        // 임시로 저장된 화면의 상태를 불러온다
        Toast.makeText(getApplicationContext(), "onResume() 호출됨", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Toast.makeText(getApplicationContext(), "onRestart() 호출됨", Toast.LENGTH_LONG).show();
    }*/
}
