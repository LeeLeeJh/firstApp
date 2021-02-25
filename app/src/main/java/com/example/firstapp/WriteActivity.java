package com.example.firstapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.firstapp.model.PostModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//import static com.example.firstapp.MyprofileActivity.myProfile;
//import static com.example.firstapp.Register3Activity.na;
//import static com.example.firstapp.Story_Activity.bitmap;
//import static com.example.firstapp.Story_Activity.mtime;
import static com.example.firstapp.Register2Activity.downloadUrl;
import static com.example.firstapp.Register2Activity.inputname;
import static com.example.firstapp.Register2Activity.photostring;
import static com.example.firstapp.Register2Activity.select_nation;
import static com.example.firstapp.Register3Activity.bitmap;
import static com.example.firstapp.Story_Activity.Heartclick;
import static com.example.firstapp.Story_Activity.info;
import static com.example.firstapp.Story_Activity.mAdapter;
import static com.example.firstapp.Story_Activity.mArrayList;
import static com.example.firstapp.Story_Activity.position;
import static com.example.firstapp.Story_Activity.rcode;
import static com.example.firstapp.Story_Activity.textView_time;

public class WriteActivity extends AppCompatActivity {

    final int REQ_CODE_SELECT_IMAGE=100;
  //  private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_ALBUM = 1;
    static String insert;
    static Bitmap img;
    static TextView writebutton;
    MyprofileActivity myprofileActivity;
    Register2Activity register2Activity;
    Story_Activity story_activity;
    static EditText edittext_writing;

    ImageView pick_image, translation, pick_camera;
    int pluspicture;
    Uri photoUri, modifyUri;
    ImageView imageView;
    byte[] byteArray;
    static String temp;
    static String time;
    private static final String TAG= "Write Activity";
    String modifytext , modifyimage, modifytime;
    static int modify;
    SharedPreferences SPP;
    String trans;

    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private FirebaseUser firebaseUser;

    File itempfile;
    String uid;
    long i = 0;
    String myname, profileString, mynation, modifykey, key, addpothostring;

    private static final int REQUEST_IMAGE_CAPTURE = 672;

    private String imageFilePath;
    private Uri pictureUri;

    private static final int PICK_FROM_CAMERA = 2;
    private File tempFile;
    private Boolean isPermission = true;
    Uri file;
    String Filecheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();

        SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);





        img = null; // img 초기화
        temp = null;
        pick_camera = (ImageView) findViewById(R.id.write_camera);
        writebutton = (TextView) findViewById(R.id.textView_insert); // 등록 버튼
        pick_image = (ImageView) findViewById(R.id.pick_image); // 갤러리 호출 버튼
        imageView = (ImageView) findViewById(R.id.imageView_plus); // 사진 첨부 이미지뷰
        edittext_writing = (EditText) findViewById(R.id.editText_writing); // 내용 들어가는 텍스트뷰
        translation = (ImageView) findViewById(R.id.writeActivity_translation);

        Intent intent = getIntent();

        modifytext = intent.getStringExtra("insert"); // 수정할 내용 가져오기
        Log.d("수정", "modifytext" + modifytext + "");
        edittext_writing.setText(modifytext); // 수정할 내용 텍스트뷰에 넣기
        modifykey = intent.getStringExtra("modifykey");
        Log.d(TAG, "modifykey111" + modifykey + "");
        modifyimage = intent.getStringExtra("modifyimage");
        Log.d(TAG, "modifyimage" + modifyimage + "");
        modifytime = intent.getStringExtra("modifytime");

        if(modifyimage == null) {


        } else {

            modifyUri = Uri.parse(modifyimage);
            Log.d(TAG, "modifyUri" + modifyUri + "");

            Glide.with(this).load(modifyUri).apply(new RequestOptions().centerCrop().override(1000,1000)).into(imageView);

        }

        pluspicture = 0;
/*

        SharedPreferences SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
        SharedPreferences.Editor storyeditor = SPP.edit();
        String strJson = SPP.getString("MyInsert", ""); // 글 목록 불러오기        Toast.makeText(Story_Activity.this, strJson + "", Toast.LENGTH_LONG).show();
*/

   /* if(modify == 1) { // 수정시 이미지 불러오기
    modifykey = intent.getStringExtra("modifykey");
    picstr = intent.getStringExtra("Picture");
    Log.d(TAG, "Picture" + picstr + "");
    byte[] PictureByte = Base64.decode(picstr, Base64.DEFAULT);
    Bitmap Picturebitmap = BitmapFactory.decodeByteArray(PictureByte, 0, PictureByte.length);
    imageView.setImageBitmap(Picturebitmap); // 수정할 사진 이미지뷰에 넣기
    Log.d(TAG, "Picture" + picstr + "");
    }*/



        myname = SPP.getString("Name","");
        profileString = SPP.getString("profileString","");
        mynation = SPP.getString("Nation", "");

        key = mDatabase.child("posts").push().getKey();



        pick_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tedPermission();
                if(isPermission)  takePhoto();
            }
        });


        translation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WriteActivity.this, translationActivity.class);
                startActivity(intent);

            }
        });



        writebutton.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {

                                               Intent intent = new Intent(WriteActivity.this, Story_Activity.class);
                                               insert = edittext_writing.getText().toString(); // 사용자 입력 내용



                                               String format = new String("a hh : mm"); // 현재 시간 표시
                                               SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
                                               time = sdf.format(new Date());


                                               if (photoUri == null) { // 사진 첨부 안 했을 때



                                                   if(modifykey == null) {
                                                       Log.d("사진", "사진첨부안함/수정안함");
                                                     //  String uid, String name, String profileString, String contentWriting, String image, String time, String postNumber, String nation, String comment){
                                                       PostModel postModel = new PostModel(uid, myname, profileString, insert, addpothostring, time, null, mynation);
                                                     //  postModel.Likes.put("default","default");
                                                       Log.d(TAG, "modifykey122" + modifykey + "");
                                                       mDatabase.child("posts").child(key).setValue(postModel);
                                                       mDatabase.child("users").child(uid).child("mposts").child(key).setValue(postModel);

                                                   } else {
                                                       Log.d("사진", "사진첨부안함/수정함");
                                                       PostModel postModel = new PostModel(uid, myname, profileString, insert, addpothostring, modifytime, null, mynation);
                                                     //  postModel.Likes.put("default","default");
                                                       Log.d(TAG, "modifykey133" + modifykey + "");
                                                       mDatabase.child("posts").child(modifykey).setValue(postModel);
                                                       mDatabase.child("users").child(uid).child("mposts").child(modifykey).setValue(postModel);

                                                   }


                                               } else { // 사진 첨부 했을 때

                                                   String path = getPath(photoUri);
                                                   StorageReference storageRef = storage.getReferenceFromUrl("gs://firstapp-2f818.appspot.com");
                                                   itempfile = new File(path);
                                                   file = Uri.fromFile(itempfile);
                                                   //  imageView.setImageURI(data.getData());
                                                   //  StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
                                                   final StorageReference riversRef = storageRef.child("writeimages").child(key + "jpg");
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


                                                   final StorageReference ref = storageRef.child("writeimages").child(key + "jpg");
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

                                                               addpothostring = download.toString();


                                                               Log.d(TAG, "addpothostring" + addpothostring + "");


                                                               if (modifykey == null) {
                                                                   Log.d("사진", "사진첨부함/수정안함");
                                                                   PostModel postModel = new PostModel(uid, myname, profileString, insert, addpothostring, time, null, mynation);
                                                               //    postModel.Likes.put("default","default");
                                                                   Log.d(TAG, "imagemodifynullkey" + key + "");

                                                                   Log.d(TAG, "modifykey222" + modifykey + "");
                                                                   mDatabase.child("posts").child(key).setValue(postModel);
                                                                   mDatabase.child("users").child(uid).child("mposts").child(key).setValue(postModel);

                                                               } else {
                                                                   Log.d("사진", "사진첨부함/수정함");
                                                                   PostModel postModel = new PostModel(uid, myname, profileString, insert, addpothostring, modifytime, null, mynation);
                                                                //   postModel.Likes.put("default","default");
                                                                   Log.d(TAG, "modifykey233" + modifykey + "");
                                                                   mDatabase.child("posts").child(modifykey).setValue(postModel);
                                                                   mDatabase.child("users").child(uid).child("mposts").child(modifykey).setValue(postModel);

                                                               }

                                                           } else {
                                                               // Handle failures
                                                               // ...
                                                               Log.v("알림", "실패다 실패" + task.getException());
                                                           }

                                                       }
                                                   });


                                               } // end of 사진첨부 if문




                                              /* private void writePost(String uid, String inputname, String inputage, String photostring){
                                                   // private void writeNewUser(String uid, String name, String age, String gender, Uri photoUri, Uri nation, String storyInsert){
                                                   downloadUrl = photostring;
                                                   final UserInfo userInfo = new UserInfo(uid,inputname,inputage,null,photostring,null);
                                                   mDatabase.child("users").child(uid).setValue(userInfo);*/

                                              // 1. PostModel 만들기

                                               if(Filecheck != null) { // 카메라 사용

                                                   StorageReference storageRef = storage.getReferenceFromUrl("gs://firstapp-2f818.appspot.com");
                                                   //  imageView.setImageURI(data.getData());
                                                   //  StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
                                                   final StorageReference riversRef = storageRef.child("writeimages").child(key + "jpg");
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


                                                   final StorageReference ref = storageRef.child("writeimages").child(key + "jpg");
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

                                                               addpothostring = download.toString();


                                                               Log.d(TAG, "addpothostring" + addpothostring + "");


                                                               if (modifykey == null) {
                                                                   Log.d("사진", "사진첨부함/수정안함");
                                                                   PostModel postModel = new PostModel(uid, myname, profileString, insert, addpothostring, time, null, mynation);
                                                                   //    postModel.Likes.put("default","default");
                                                                   Log.d(TAG, "imagemodifynullkey" + key + "");

                                                                   Log.d(TAG, "modifykey222" + modifykey + "");
                                                                   mDatabase.child("posts").child(key).setValue(postModel);
                                                                   mDatabase.child("users").child(uid).child("mposts").child(key).setValue(postModel);

                                                               } else {
                                                                   Log.d("사진", "사진첨부함/수정함");
                                                                   PostModel postModel = new PostModel(uid, myname, profileString, insert, addpothostring, modifytime, null, mynation);
                                                                   //   postModel.Likes.put("default","default");
                                                                   Log.d(TAG, "modifykey233" + modifykey + "");
                                                                   mDatabase.child("posts").child(modifykey).setValue(postModel);
                                                                   mDatabase.child("users").child(uid).child("mposts").child(modifykey).setValue(postModel);

                                                               }

                                                           } else {
                                                               // Handle failures
                                                               // ...
                                                               Log.v("알림", "실패다 실패" + task.getException());
                                                           }

                                                       }
                                                   });
                                               }


                                               if(pluspicture == 0) { // 사진 첨부 안 했을 경우
                                                 //  Toast.makeText(WriteActivity.this, "Picturewrite: " + intent.putExtra("Picture", temp) +"", Toast.LENGTH_SHORT).show();

                                                   intent.putExtra("result", insert);
                                                 //  intent.putExtra("Picture", temp);
                                                   intent.putExtra("time", time);
                                                   Log.d(TAG, "TimeW" + time + "");
                                                   setResult(RESULT_OK,intent);

                                              } else if(pluspicture == 1){ // 사진 첨부 했을 경우


                                                 //  Toast.makeText(WriteActivity.this, "Picturewrite: " + intent.putExtra("Picture", temp) +"", Toast.LENGTH_SHORT).show();
                                                   // 이미지 파일 크기 줄이기
                                                 /*  ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                   Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                                                   float scale = (float) (1024/(float)bitmap.getWidth());
                                                   int image_w = (int) (bitmap.getWidth() * scale);
                                                   int image_h = (int) (bitmap.getHeight() * scale);
                                                   Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
                                                   resize.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                                   byteArray = stream.toByteArray();
                                                   temp= Base64.encodeToString(byteArray, Base64.DEFAULT); // Bitmap to String*/


                                                   intent.putExtra("result", insert);
                                                   intent.putExtra("Picture", temp);
                                                   intent.putExtra("time", time);
                                                   modify = 0;
                                                   setResult(RESULT_OK, intent);
                                                   Log.d(TAG, "PictureN" + temp + "");
                                                //   startActivityForResult(intent,4050);
                                               }


                                               if(modifykey != null){
                                                   Log.d("수정할때", "modifykey" + modifykey + "");
                                                //   intent = new Intent(getApplicationContext(), MyprofileActivity.class);
                                                //   startActivity(intent);
                                                   finish();
                                               } else {
                                                   finish();
                                                   intent = new Intent(getApplicationContext(), Story_Activity.class);
                                                   startActivity(intent);
                                               }

                                             /*  finish();
                                               intent = new Intent(getApplicationContext(), Story_Activity.class);
                                               startActivity(intent);*/
                                           }
                                       });



        pick_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // 갤러리 호출 버튼 클릭
                modify = 0;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
               // intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 11);
                pluspicture = 1;

                /*ntent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 11);*/



                // Gallery 호출
                                                       /*intent.setType("image/*");
                                                       intent.setAction(Intent.ACTION_GET_CONTENT);
                                                       // 잘라내기 셋팅
                                                       intent.putExtra("crop", "true");
                                                       intent.putExtra("aspectX", 0);
                                                       intent.putExtra("aspectY", 0);
                                                       intent.putExtra("outputX", 200);
                                                       intent.putExtra("outputY", 150);
                                                       try {
                                                           intent.putExtra("return-data", true);
                                                           startActivityForResult(Intent.createChooser(intent,
                                                                   "Complete action using"), PICK_FROM_GALLERY);
                                                       } catch (ActivityNotFoundException e) {
                                                           // Do nothing for now
                                                       }
*/

            }
        });










    } // end of create






    public String getPath(Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(index);

    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            ((ImageView)findViewById(R.id.photo)).setImageBitmap(rotate(bitmap, exifDegree));
        }
    }
*/







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



            setImage();

        } else if (requestCode == PICK_FROM_CAMERA) {

            setImage();

        }


        if(resultCode == RESULT_OK){
            switch (requestCode){ // InnerStoryActivity 에서 수정하기 버튼 눌렀을 때 보낸 요청 코드 (4000)
                case 4000:
                  //  Toast.makeText(WriteActivity.this, "Result: " + mArrayList.get(position) + data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
                  //  Toast.makeText(WriteActivity.this, "Result: " + position , Toast.LENGTH_SHORT).show();
                  //  String insert = "";
                  //  Info info = mArrayList.get(position);
                  /*  Bundle extras = getIntent().getExtras();
                    insert = extras.getString("insert");
                    String insertstr = insert;
                    edittext_writing.setText(insertstr);*/ // 수정할내용 텍스트뷰에 입력





                    break;

                    case 11 :
                        if (resultCode == RESULT_OK) {
                            try {

                                photoUri = data.getData(); // 이미지 경로 원본
                                Glide.with(this).load(photoUri).apply(new RequestOptions().centerCrop().override(1000,1000)).into(imageView);
                                Filecheck = "삽입";
                              //  imageView.setImageURI(data.getData()); // 이미지 뷰에 삽입


                                // 선택한 이미지에서 uri 생성
                             //   addphotouri = data.getData();
                                // 파이어베이스 사용으로 url로 변경합니다.
                             /*   InputStream in = getContentResolver().openInputStream(data.getData());
                                img = BitmapFactory.decodeStream(in);
                                in.close();

                                imageView.setImageBitmap(img);

                                pluspicture = 1;
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                                float scale = (float) (1024/(float)bitmap.getWidth());
                                int image_w = (int) (bitmap.getWidth() * scale);
                                int image_h = (int) (bitmap.getHeight() * scale);
                                Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
                                resize.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                byteArray = stream.toByteArray();
                                temp= Base64.encodeToString(byteArray, Base64.DEFAULT);
*/


                              //  insertpicture.setImageURI(addphotouri);
                              //  intent.putExtra("PUri",addphotouri);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }





            }
        } else {
          //  Toast.makeText(WriteActivity.this, "Failed", Toast.LENGTH_SHORT).show();


        }
    }


    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {

            file = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }


    private File createImageFile() throws IOException {

        // 이미지 파일 이름 ( blackJin_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "blackJin_" + timeStamp + "_";

        // 이미지가 저장될 폴더 이름 ( blackJin )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/blackJin/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;
    }


    private void setImage() {


        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());
        Filecheck = "삽입";
        imageView.setImageBitmap(originalBm);


        /**
         *  tempFile 사용 후 null 처리를 해줘야 합니다.
         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
         */
        tempFile = null;

    }

    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                isPermission = true;

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
                isPermission = false;

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 11) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    pickimage.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        if(modifytext == null) {
            SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);
            trans = SPP.getString("copy", "");
            Log.d("copy2", "trans" + trans);
            edittext_writing.setText(trans);
            SharedPreferences.Editor editor = SPP.edit();
            editor.putString("copy", "");
            editor.commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        addpothostring = null;
        modifytext = null;
        modifyimage = null;
        Filecheck = null;
      //  modifykey = null;

    }







}

