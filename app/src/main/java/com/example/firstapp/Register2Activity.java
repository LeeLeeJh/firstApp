package com.example.firstapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.UserManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.firstapp.model.PostModel;
import com.example.firstapp.model.UserModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.firstapp.MyprofileActivity.profileedit;
import static com.example.firstapp.WriteActivity.insert;

public class Register2Activity extends BaseActivity {
    private static final String TAG= "Register2";
    private Boolean isPermission = true;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private File tempFile;

    static Bitmap originalBm;


    ImageView imageView;
    final Context context = this;
    private Spinner genderspinner, nationspinner;
    ArrayList<String> genderarrayList, nationarryList;
    ArrayAdapter<String> genderarrayAdapter, nationarrayAdapter;
    EditText text_na, text_ag;
   static String inputname,inputage;
    static byte[] byteArray;
    public static String select_nation = "";
    String select_gender;
    static BitmapDrawable img_nation;
    static ImageView mnation;
    Uri photoUri, nation;
    static Bundle bundle;
    static String photostring, photostr, photo;
    SharedPreferences SPP;
    SharedPreferences.Editor editor;
    String nations;
   static String loginID, uid;
    String myage, mynation,imagetemp, mygender;
    int nationi,genderi;
    File itempfile;
    File profile;
   static String pimage;
    static String downloadUrl , updown;

    private DatabaseReference mDatabase;// ...
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private FirebaseStorage storage;
    StorageReference storageRef;
    Uri downloadUri;
    String alreadyInfo;
    int followCount, followingCount;
    static String myname, profilestring;
    TextView spinnertext_gender,spinnertext_naion;
    String introduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
        editor = SPP.edit();

        alreadyInfo = SPP.getString("alreadyInfo","");
        Log.w(TAG, "alreadyInfo" + alreadyInfo + "");

        if(alreadyInfo == "name"){
            actFinish();
        }

        tedPermission(); // 갤러리 권한 허가


        Button nextButton = (Button) findViewById(R.id.nextbutton);
        mnation = (ImageView)findViewById(R.id.myNation);
        text_na = (EditText) findViewById(R.id.nameText);
        text_ag = (EditText) findViewById(R.id.ageText);
        imageView = (ImageView)findViewById(R.id.myprofiler);
        spinnertext_gender = (TextView) findViewById(R.id.spinner_gendertext);
        spinnertext_naion = (TextView) findViewById(R.id.spinner_nationtext);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
       /* FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //firebaseUser = firebaseAuth.getCurrentUser();
        uid = firebaseUser.getUid();
        Log.w(TAG, "uid" + uid + "");*/ // start로 옮겼음


        /*UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
        .setDisplayName("")
                .
*/
         Intent intent = getIntent();
         loginID = intent.getStringExtra("loginID"); // 로그인 아이디 받아오기
         Log.w(TAG, "loginID" + loginID + "");



        // 성별 선택 스피너
        genderarrayList = new ArrayList<>();
        genderarrayList.add("여성");
        genderarrayList.add("남성");
        genderarrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                genderarrayList);





        // 국적 선택 스피너
        nationarryList = new ArrayList<>();
        nationarryList.add("미국");
        nationarryList.add("중국");
        nationarryList.add("한국");
        nationarryList.add("일본");
        nationarrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                nationarryList);

        nationspinner = (Spinner)findViewById(R.id.nationspinner);
        genderspinner = (Spinner)findViewById(R.id.genderspinner);




        genderspinner.setAdapter(genderarrayAdapter);
        genderspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                select_gender = String.valueOf(genderarrayList.get(i));
                spinnertext_gender.setText(select_gender);
                Log.i(TAG, "Spinner selected item = "+ select_gender);



                if(select_gender.equals("여성")){
                    // nations = "미국";
                     genderi = 1;
                    //img_nation = (BitmapDrawable)getResources().getDrawable(R.drawable.usaflag);


                    // 미국 국기 사진 가져와서
                    //   mnation.setImageDrawable(img_nation);// 미국 국기 사진 출력
                } else if(select_gender.equals("남성")) {
                    //  nations = "중국";
                     genderi = 2;
                    // img_nation = (BitmapDrawable) getResources().getDrawable(R.drawable.chineseflag);

                }

            }


            @Override

            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });





        nationspinner.setAdapter(nationarrayAdapter);
        nationspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //  String str_nation = nationspinner.getSelectedItem().toString();

                select_nation = String.valueOf(nationarryList.get(i));
                Log.v("알림", "select_nation" + select_nation);
                Log.i(TAG, "Spinner selected item = "+ select_nation);
                Intent nationintent = new Intent(Register2Activity.this,Story_Activity.class);
                /*nationintent.putExtra("it_nation", select_nation);*/ // 쉐어드 사용 //


                spinnertext_naion.setText(select_nation);

                if(select_nation.equals("미국")){
                    nations = "미국";
                    Log.v("알림", "nations" + nations);
                    nationi = 1;
                    img_nation = (BitmapDrawable)getResources().getDrawable(R.drawable.usaflag);


                    // 미국 국기 사진 가져와서
                    //   mnation.setImageDrawable(img_nation);// 미국 국기 사진 출력
                } else if(select_nation.equals("중국")){
                    nations = "중국";
                    nationi = 2;
                    img_nation = (BitmapDrawable)getResources().getDrawable(R.drawable.chineseflag);



                    //중국 국기 사진 가져와서
                    //   mnation.setImageDrawable(img_nation);// 중국 사진 출력
                } else if(select_nation.equals("한국")){
                    nations = "한국";
                    nationi = 3;
                    img_nation = (BitmapDrawable)getResources().getDrawable(R.drawable.koreaflag);



                    //한국 국기 사진 가져와서
                    //   mnation.setImageDrawable(img_nation);// 한국 사진 출력
                } else if(select_nation.equals("일본")){
                    nations = "일본";
                    nationi = 4;
                    img_nation = (BitmapDrawable)getResources().getDrawable(R.drawable.japanflag);


                    //일본 국기 사진 가져와서
                    //   mnation.setImageDrawable(img_nation);// 일본 사진 출력

                }




            }






            @Override

            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });






        if(profileedit == 1){
            SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
            myname = SPP.getString("Name","");
            myage = SPP.getString("Age","");
            profilestring = SPP.getString("profileString", "");
            mynation = SPP.getString("Nation", "");
            introduction = SPP.getString("introduction","");
            Log.d("수정", "introduction" + introduction);
            mygender = SPP.getString("gender","");
          //  followCount = SPP.getInt("folloCount",0);

            if(mynation.equals("미국")){
                nationi = 1;
            } else if(mynation.equals("중국")){
                nationi = 2;
            } else if(mynation.equals("한국")){
                nationi = 3;
            } else if(mynation.equals("일본")){
                nationi = 4;
            }

            if(mygender.equals("여성")){
                genderi = 1;
            } else if(mygender.equals("남성")){
                genderi = 2;
            }

            text_na.setText(myname);
            text_ag.setText(myage);

           /* byte [] encodeByte= Base64.decode(imagetemp,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            imageView.setImageBitmap(bitmap);
*/

            downloadUri = Uri.parse(profilestring);

            Glide.with(this).load(downloadUri).apply(new RequestOptions().centerCrop()).into(imageView);

            genderspinner.setSelection(genderi-1);
            nationspinner.setSelection(nationi-1);
        }







        nextButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){ // 스토리 페이지로 넘어가는 버튼

                CheckTypesTask task = new CheckTypesTask();
                task.execute(); // 프로그레스바 사용

                inputname = text_na.getText().toString();
                inputage = text_ag.getText().toString();



                if (profilestring == null) { // 프로필 사진 쉐어드에 저장 안되어 있을 때 ( 프로필 정보가 없을 때 )
                    Log.e("수정", "프로필 정보 없을 때");
                String path = getPath(photoUri);
                StorageReference storageRef = storage.getReferenceFromUrl("gs://firstapp-2f818.appspot.com");
                itempfile = new File(path);
                Uri file = Uri.fromFile(itempfile);
                //  imageView.setImageURI(data.getData());
                //  StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
                final StorageReference riversRef = storageRef.child("images").child(uid + "jpg");
                UploadTask uploadTask = riversRef.putFile(file);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        //  Toast.makeText(Register2Activity.this,"실패",Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onFailure: 실패!!!!");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

                        //   updown = riversRef.getDownloadUrl().toString();

                        //    Log.d(TAG, "updownloadUrl" + updown + "");
                    }
                });





                final StorageReference ref = storageRef.child("images").child(uid + "jpg");
                uploadTask = ref.putFile(file);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();


                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri download = task.getResult();
                            Log.v("알림", "사진 다운로드 성공 " + download);

                            downloadUrl = download.toString();


                            SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
                            editor = SPP.edit();
                            editor.putString("profileString",downloadUrl);
                            editor.putString("Nation", nations);
                            editor.commit();


                            writeNewUser(uid,inputname,inputage,select_gender,downloadUrl,select_nation); // User info Update
                            passPushTokenToServer();
                            // chatroom(inputname,downloadUrl,uid); // Chat user info Update
                            // Postinfo(uid,inputname,downloadUrl,select_nation); // Post user info Update
                            Log.v("알림", "데이터저장이후" + downloadUrl);
                            Log.v("알림", "select_nation" + select_nation);

                            Log.v("알림", "downtostring " + download.toString() );
                            Log.v("알림", "downloadUrl " + downloadUrl);


                        } else {
                            // Handle failures
                            // ...
                            Log.v("알림", "실패다 실패" + task.getException());
                        }
                    }
                });






                } else { // 프로필 수정시

if(photoUri == null) { // 프로필 사진 수정 안했을 때
    Log.e("수정", "프로필사진 수정안했을때");
    downloadUrl = profilestring;
    Log.e("수정", "downloadUrl" + downloadUrl + "");

    SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
    editor = SPP.edit();
    editor.putString("profileString", downloadUrl);
    editor.putString("Nation", nations);
    editor.putString("gender",select_gender);
    editor.commit();

    Log.e("수정", "inputname" + inputname + "");
    Log.e("수정", "inputage" + inputage + "");

    final UserInfo userInfo = new UserInfo(uid,inputname,inputage,select_gender,downloadUrl,select_nation, followCount, followingCount,introduction);

    mDatabase.child("users").child(uid).updateChildren(userInfo.toMap());

    // 메소드 안에서 생성자에 매개변수로 입력한 변수와 같아야함


} else { // 프로필사진 수정 했을 때
    Log.e("수정", "프로필사진 수정했을때");
    String path = getPath(photoUri);
    StorageReference storageRef = storage.getReferenceFromUrl("gs://firstapp-2f818.appspot.com");
    itempfile = new File(path);
    Uri file = Uri.fromFile(itempfile);
    //  imageView.setImageURI(data.getData());
    //  StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
    final StorageReference riversRef = storageRef.child("images").child(uid + "jpg");
    UploadTask uploadTask = riversRef.putFile(file);

    // Register observers to listen for when the download is done or if it fails
    uploadTask.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
            // Handle unsuccessful uploads
            //  Toast.makeText(Register2Activity.this,"실패",Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onFailure: 실패!!!!");
        }
    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

            //   updown = riversRef.getDownloadUrl().toString();

            //    Log.d(TAG, "updownloadUrl" + updown + "");
        }
    });


    final StorageReference ref = storageRef.child("images").child(uid + "jpg");
    uploadTask = ref.putFile(file);

    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
        @Override
        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return ref.getDownloadUrl();


        }
    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
        @Override
        public void onComplete(@NonNull Task<Uri> task) {
            if (task.isSuccessful()) {
                Uri download = task.getResult();
                Log.v("알림", "사진 다운로드 성공 " + download);

                downloadUrl = download.toString();


                SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
                editor = SPP.edit();
                editor.putString("profileString", downloadUrl);
                editor.putString("Nation", nations);
                editor.putString("gender",select_gender);
                editor.commit();

                Log.d("수정", "introduction" + introduction);
                final UserInfo userInfo = new UserInfo(uid,inputname,inputage,select_gender,downloadUrl,select_nation, followCount, followingCount,introduction);

                mDatabase.child("users").child(uid).updateChildren(userInfo.toMap());
            } else {
                // Handle failures
                // ...
                Log.v("알림", "실패다 실패" + task.getException());
            }
        }
    });



}


                        }







                /*Intent intent_01 = new Intent(getApplicationContext(),MyprofileActivity.class);
                startActivity(intent_01);*/

                Intent intent_02 = new Intent(Register2Activity.this,Story_Activity.class);



               // inputname = text_na.getText().toString();
               // inputage = text_ag.getText().toString();


                // 사진 값 넘겨주는 것
               /* ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                float scale = (float) (1024/(float)bitmap.getWidth());
                int image_w = (int) (bitmap.getWidth() * scale);
                int image_h = (int) (bitmap.getHeight() * scale);
                Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
                resize.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                byteArray = stream.toByteArray();
                String temp= Base64.encodeToString(byteArray, Base64.DEFAULT); // Bitmap to String*/
                // URI로 변경합니다.










                // 로그인 id,pwd






                SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
                SharedPreferences.Editor profileeditor = SPP.edit();
                profileeditor.putString("Name", inputname);
                profileeditor.putString("Age", inputage);
                profileeditor.putString("Nation", nations);
                profileeditor.putString("gender",select_gender);
               // profileeditor.putString("Image", temp);

                profileeditor.commit();




                nationi = 0; // nation 초기화







                Log.d(TAG, uid + "");
                Log.d(TAG, inputname + "");
                Log.d(TAG, inputage + "");
              //  Log.d(TAG, photostring + "");











            /*
                intent_02.putExtra("입력한 이름", name);
                intent_02.putExtra("입력한 나이", age);
                intent_02.putExtra("Uri", photoURI);*/


                intent_02.putExtra("image", byteArray);


                startActivity(intent_02);
            }
        });









       // 사진 등록 버튼
       Button imagebutton = (Button)findViewById(R.id.imagebutton);
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
              //  intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // 파이어베이스 이용하면서 바꿈...
                startActivityForResult(intent, 1);



            //    if(isPermission) goToAlbum();
            //    else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();

           /*     Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);*/
           }
        });


        /*Button imagebutton = (Button)findViewById(R.id.imagebutton);
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 갤러리 불러와서 사진 추가
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(Intent.createChooser(intent, "Select File"), 1);

                }
                *//*intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);*//*
            }
        });   // Uri용...*/









    } // end of onCreate



   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==20 && resultCode==RESULT_OK){
           Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }*/





    /*private void goToAlbum() { // 앨범 열기

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    *//**
     *  폴더 및 파일 만들기
     *//*
    private File createImageFile() throws IOException {

        // 이미지 파일 이름 ( blackJin_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "blackJin_" + timeStamp + "_";

        // 이미지가 저장될 폴더 이름 ( blackJin )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/blackJin/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d(TAG, "createImageFile : " + image.getAbsolutePath());

        return image;
    }

    *//**
     *  tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
     *//*
    private void setImage() {

        //ImageView imageView = findViewById(R.id.imageView);

        BitmapFactory.Options options = new BitmapFactory.Options();
        originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());

        imageView.setImageBitmap(originalBm);

        *//**
         *  tempFile 사용 후 null 처리를 해줘야 합니다.
         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
         *//*
        tempFile = null;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        }

        if (requestCode == PICK_FROM_ALBUM) {

            Uri photoUri = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

            Cursor cursor = null;

            try {

                *//*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 *//*
                String[] proj = { MediaStore.Images.Media.DATA };

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

                Log.d(TAG, "tempFile Uri : " + Uri.fromFile(tempFile));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        } else if (requestCode == PICK_FROM_CAMERA) {

            setImage();

        }
    }
*/ // 앨범 이미지 가져오기 & 셋팅

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                  //  Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    photoURI = data.getData();
                    photostring = photoURI.toString(); // Uri -> String 변환

                 //   stringuri = Uri.parse(photostring); String -> Uri 변환
                    // 이미지 표시

                    Log.d(TAG, photoURI + "");
                    Log.d(TAG, photostring + "");
                      //  Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),photoURI);
                        imageView.setImageURI(photoURI);
                      //  imageView.setImageBitmap(bitmap);



                    Log.d(TAG, stringuri + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }*/ // 원본


    // 갤러리 권한 요청
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }


    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                Register2Activity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 5; i++) {
                    //asyncDialog.setProgress(i * 30);
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {


                    imageView.setImageURI(data.getData()); // 이미지 뷰에 삽입
                    photoUri = data.getData(); // 이미지 경로 원본

                  //  photostring = getPath(data.getData());




       /*             StorageReference reference= storageRef.child(System.currentTimeMillis()+ "."+getPath(data.getData()));
                    Uri file = Uri.fromFile(new File(getPath(data.getData())));
                    UploadTask mUploadTask= reference.putFile(file);
                    Task<Uri> urlTask = mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return storageRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                mDatabase.child("images").child("imageUrl").setValue(downloadUri.toString());
                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                           // progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });*/



                   /* photo = getPath(data.getData());
                    Log.d(TAG, "getPath(photoUri)" + photo + "");





                    photostring = photoUri.toString(); // myphotouri String - > Uri로 변환 / 프로필 사진 uri); // myphotouri String - > Uri로 변환 / 프로필 사진 uri
                    photostr = photostring;
*/



                  /*  // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    imageView.setImageBitmap(img);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public String getPath(Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(index);

    }




    private void chatroom(String inputname,String photostring,String uid){
     //   profilestring = downloadUrl;
        Log.d("널1", "chatroom " + profilestring);
        Log.d("널2", "chatroom " + downloadUrl);
        Log.d("널3", "chatroom " + photostring);
       final ChatRoomInfo chatRoomInfo = new ChatRoomInfo(inputname,downloadUrl,uid,null,null,null);
       mDatabase.child("chat").child(uid).setValue(chatRoomInfo);

    }


    private void Postinfo (String uid, String inputname, String profilestring, String nation){
      //  profilestring = downloadUrl;
      //  PostModel postModel = new PostModel(uid,inputname,downloadUrl,null,null,null,null,select_nation);
      //  mDatabase.child("users").child(uid).child("mposts").setValue(postModel);
    }



    // DB에 UserInfo 저장
    private void writeNewUser(String myuid, String mname, String mage, String gender, String profilestring, String nation){
      //  profilestring = downloadUrl;
      //  Log.d("널", "User" + photostring );


        final UserInfo userInfo = new UserInfo(uid,inputname,inputage,select_gender,downloadUrl,select_nation, followCount, followingCount,null);

        mDatabase.child("users").child(uid).setValue(userInfo);















       //  inputname = name;
       // inputage = age;
      //  photoUri = imageUri;

      //  UserInfo userInfo = new UserInfo(uid,inputname,inputage,null,photostring,null,null);

        /*UserInfo userInfo = new UserInfo();
        userInfo.uid = uid;
        userInfo.name = inputname;
        userInfo.age = inputage;
        userInfo.imageUri = photoUri;*/
      //  UserInfo userInfo = new UserInfo(uid,name,age,null,imageUri,null,null);


       // Map<String, Object> userMap = userInfo.toMap();

      //  Map<String, Object> childUpdates = new HashMap<>();
       // childUpdates.put(uid, userMap);

     //   mDatabase.updateChildren(userMap);

      //  Toast.makeText(Register2Activity.this, "??" +  mDatabase.child("users").child(uid).setValue(userInfo) + "", Toast.LENGTH_SHORT).show();

       /* final StorageReference profileImageRef =
        FirebaseStorage.getInstance().getReference().child("userImages").child(uid);
        profileImageRef.putFile(photoUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
               // @SuppressWarnings("VisibleForTests")
                photostr = profileImageRef.getDownloadUrl().toString();


                UserInfo userInfo = new UserInfo();
                userInfo.photostring = photostr;
              //  mDatabase.child("users").child(uid).setValue(userInfo);
            }
        });*/



       /* String filename = uid + "_" + System.currentTimeMillis();

         final StorageReference storageRef = firebaseStorage.getReferenceFromUrl("본인의 Firebase 저장소").child("WriteClassImage/" + filename);


        UploadTask uploadTask;


        uploadTask = storageRef.putFile(photoUri);


        final ProgressDialog progressDialog = new ProgressDialog(Register2Activity.this,R.style.MyAlertDialogStyle);

        progressDialog.setMessage("업로드중...");

        progressDialog.show();


        // Register observers to listen for when the download is done or if it fails

        uploadTask.addOnFailureListener(new OnFailureListener() {

            @Override

            public void onFailure(@NonNull Exception exception) {

                // Handle unsuccessful uploads

                Log.v("알림", "사진 업로드 실패");

                exception.printStackTrace();

            }

        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override

            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.


                URL downloadUrl = taskSnapshot.getStorage().getDownloadUrl();

                Log.v("알림", "사진 업로드 성공 " + downloadUrl);

            }

        });
*/
    /*    UploadTask uploadTask;
        Uri file = Uri.fromFile(new File(getPath(photoUri)));
        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
        uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });*/










        // userInfo.photostring = photostr;




    }





    void passPushTokenToServer(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();

        Map<String,Object> map = new HashMap<>();
        map.put("pushToken",token);

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(map);

    }









    @Override
    protected void onStart() {
        super.onStart();

        if(alreadyInfo == "name"){
            actFinish();
        }


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
     //   Toast.makeText(Register2Activity.this, "uid" + uid + "", Toast.LENGTH_SHORT).show();
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

        profileedit = 0;



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



    @Override
    protected void onSaveInstanceState(Bundle outState){
        Log.d(TAG,"onSaveInstanceState 함수 호출");
     /* //  inputname = text_na.getText().toString().trim();
       // inputage = text_ag.getText().toString().trim();

        if(inputname.length() == 0){
            outState.putString("name","입력없음");
            Log.d(TAG,"입력없음");
        } else {
            outState.putString("name",inputname);
            Log.d(TAG,"사용자가 입력했던 이름 저장");

        }

        if(inputage.length() == 0){
            outState.putString("age","입력없음");
            Log.d(TAG,"입력없음");
        } else {
            outState.putString("age",inputage);
            Log.d(TAG,"사용자가 입력했던 나이 저장");

        }
*/
        super.onSaveInstanceState(outState);

    }






    @Override
   protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG,"onRestoreInstanceState(Bundle) 함수가 실행");
    super.onRestoreInstanceState(savedInstanceState);

   /* String inputname = savedInstanceState.getString("name");
    String inputage = savedInstanceState.getString("age");
        Log.d(TAG,"사용자가 입력했던 값은"+ inputname);
        Log.d(TAG,"사용자가 입력했던 값은"+ inputage);

    text_na.setText(inputname);
    text_ag.setText(inputage);*/

     }






}