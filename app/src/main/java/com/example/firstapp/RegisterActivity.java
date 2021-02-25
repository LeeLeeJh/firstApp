package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firstapp.model.UserModel;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private static final String TAG= "Register";
    private  EditText id;
    private  EditText password;
    private  EditText checkpassword;
    private  Button registerButton;
    static String Id, Pwd;

    static int a;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();



        id = (EditText) findViewById(R.id.idText);
        password = (EditText) findViewById(R.id.passwordText);
        checkpassword = (EditText) findViewById(R.id.checkpasswordText);
        registerButton = (Button) findViewById(R.id.registerButton);

       /* SharedPreferences SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
        Id = SPP.getString("inputId",null);
        Pwd = SPP.getString("inputPwd",null);*/



/*
        // 비밀번호 일치 검사

        checkpasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String password = passwordText.getText().toString();
                String confirm = checkpasswordText.getText().toString();



                if( password.equals(confirm) ) {

                    passwordText.setBackgroundColor(Color.GREEN);

                    checkpasswordText.setBackgroundColor(Color.GREEN);

                } else {

                    passwordText.setBackgroundColor(Color.RED);

                    checkpasswordText.setBackgroundColor(Color.RED);

                }

            }



            @Override

            public void afterTextChanged(Editable s) {



            }

        });*/



        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {




                /*// 이메일 입력 확인

                if( id.getText().toString().length() == 0 ) {

                    Toast.makeText(RegisterActivity.this, "ID를 입력하세요!", Toast.LENGTH_SHORT).show();

                    id.requestFocus();

                    return;

                }



                // 비밀번호 입력 확인

                if( password.getText().toString().length() == 0 ) {

                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();

                    password.requestFocus();

                    return;

                }



                // 비밀번호 확인 입력 확인

                if( checkpassword.getText().toString().length() == 0 ) {

                    Toast.makeText(RegisterActivity.this, "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();

                    checkpassword.requestFocus();

                    return;

                }



                // 비밀번호 일치 확인

                if( !password.getText().toString().equals( checkpassword.getText().toString()) ) {

                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();

                    password.setText("");

                    checkpassword.setText("");

                    password.requestFocus();

                    return;

                }*/

                Id = id.getText().toString();
                Pwd = password.getText().toString();


              // if(password.getText().toString().equals(checkpassword.getText().toString())) {

                   /*UserInfo userInfo = new UserInfo(Id,Pwd,null,null,null,null,null,null);


                   mDatabase.child("users").child(Id).setValue(userInfo);*/

                 //  Toast.makeText(RegisterActivity.this, "Authentication.", Toast.LENGTH_SHORT).show();

                  // startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                  // finish(); // 데이터베이스 사용 X





                   firebaseAuth.createUserWithEmailAndPassword( id.getText().toString(), password.getText().toString())
                           .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if(task.isSuccessful()){

                                     /* String uid = task.getResult().getUser().getUid();
                                      UserModel userModel = new UserModel();
                                      userModel.userName = name;
                                      uid = task.getRe
                                      FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);*/
 // 이름 등 기타 정보 저장은 Register2에서
                                       Log.d(TAG, "createUserWithEmail:success");
                                       FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                                       updateUI(user);
                                       Toast.makeText(RegisterActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                       //  startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                   } else {
                                       Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                     //  Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                       updateUI(null);
                                   }

                               }
                           });   // 여기까지

                  /*                 SharedPreferences SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
                   SharedPreferences.Editor register = SPP.edit();

                   register.putString("Id", Id);
                   register.putString("Pwd",Pwd);
                   register.commit();*/ // 쉐어드 일단 뺌

                  /* JSONObject jsObject = new JSONObject();
                   try{
                       jsObject.put("Id", id.getText().toString());
                       jsObject.put("Pwd",password.getText().toString());


                   Gson gson = new Gson();

                   String json = gson.toJson(jsObject);

                   register.putString(Id, json);

                   register.commit();

                   Log.d(TAG, "SPP" + SPP.getAll() + "");


                   }catch (Exception e){
                       e.printStackTrace();*/
                 //  }










                //   Log.d(TAG, SPP.getAll() + "");
                //   Toast.makeText(RegisterActivity.this, SPP.getAll() + "", Toast.LENGTH_SHORT).show();

               }

                // ID, PWD 쉐어드 저장값 가져오기 위해서 로그인 액티비티 새로 열기
             //   Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            //    startActivity(intent);
              //  finish();

       //     }

        });









    } // end of create


    @Override
    public void onBackPressed() {
        Intent intent2 = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent2);
        finish();

        RegisterActivity.super.onBackPressed();
    }



















     /*
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Rintent = new Intent(RegisterActivity.this, MainActivity.class);
*/
/*
                EditText text_id = (EditText) findViewById(R.id.idText);
                EditText text_ps = (EditText) findViewById(R.id.passwordText);

                String id = text_id.getText().toString();
                String ps = text_ps.getText().toString();

                Rintent.putExtra("입력한 아이디", id);
                Rintent.putExtra("입력한 패스워드", ps);*/






               /* startActivity(Rintent);;

        }
    });
    };*/

    public void  updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"U Signed In successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,MainActivity.class));
        }else {

          //  Toast.makeText(this,"U Didnt signed in",Toast.LENGTH_LONG).show();
        }
    }





}
