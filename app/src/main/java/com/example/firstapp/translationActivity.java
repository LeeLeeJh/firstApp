package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class translationActivity extends AppCompatActivity {


    private EditText translationText;
    private TextView translationButton;
    private TextView copyButton;
    private TextView resultText;
    TextView spinner_before_text;
    TextView spinner_after_text;
    ImageView spinnertrans;
    private String result;

    private Spinner translationspinner, resultspinner;
    ArrayList<String> translationarrayList, resultarryList;
    ArrayAdapter<String> translationarrayAdapter, resultarrayAdapter;
    String spinner_before, spinner_after;
    String before, after;

    SharedPreferences SPP;


    class BackgroundTask extends AsyncTask<Integer, Integer, Integer>{
        protected void onPreExecute(){

        }

        @Override
        protected Integer doInBackground(Integer... arg0){
            StringBuilder output = new StringBuilder();
            String clientId = "Ggle59SeipfNLmr7bc3A"; // 파파고 클라이언트 ID값
            String clientSecret ="6oZ0xeGTQm"; // 파파고 클라이언트 시크릿값
            try{
               // 번역문을 UTF-8로 인코딩
               String text = URLEncoder.encode(translationText.getText().toString(), "UTF-8");
                Log.e("번역", "가즈아" + translationText.getText().toString());
               String apiURL = "https://openapi.naver.com/v1/papago/n2mt";

               // 파파고와 api연결 수행
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                // 번역할 문장을 파라미터로 전송
               // String postParams = "source=ko&target=en&text=" + text;
                String postParams = "source=" + before +"&target=" + after + "&text=" + text;
                Log.e("번역", "비포" + before +"" + "앱터" +  after + "");
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();

                // 번역 결과를 받아온다.
                int responseCode = con.getResponseCode();
                BufferedReader br;
                Log.e("번역", "responseCode" + responseCode);
                if(responseCode == 200){ // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else { // 에러 발생
                    Log.e("번역", "에러니..?");
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));

                }
                String inputLine;
                while ((inputLine = br.readLine()) != null){
                    output.append(inputLine);
                }
                br.close();

            } catch (Exception ex){
                Log.e("SampleHTTP", "Exception in processing response.", ex);
                ex.printStackTrace();
            }
            result = output.toString();
            return null;
        }

           protected void onPostExecute(Integer a){
            JsonParser parser = new JsonParser();
               JsonElement element = parser.parse(result);
               if(element.getAsJsonObject().get("errorMessage") != null){
                   Log.e("번역 오류", "번역 오류가 발생했습니다." + "[오류 코드: " + element.getAsJsonObject().get("errorCode").getAsString() + "]");
               } else if(element.getAsJsonObject().get("message") != null){
                   // 번역 결과 출력
                   resultText.setText(element.getAsJsonObject().get("message").getAsJsonObject().get("result").getAsJsonObject().get("translatedText").getAsString());
                   Log.e("번역되는거니", "왜이러니" + element.getAsJsonObject().get("message").getAsJsonObject().get("result").getAsJsonObject().get("translatedText").getAsString() +"");
               }
           }

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);

        spinnertrans = (ImageView) findViewById(R.id.trans_spinner);
        translationText = (EditText) findViewById(R.id.translationText);
        translationButton = (TextView) findViewById(R.id.translationButton);
        spinner_before_text = (TextView) findViewById(R.id.translation_spinner_before_text);
        spinner_after_text = (TextView) findViewById(R.id.translation_spinner_after_text);
        resultText = (TextView) findViewById(R.id.translationResult);
        resultText.setVisibility(View.INVISIBLE);
        copyButton = (TextView) findViewById(R.id.copyButton);


        spinnertrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_before_text.setText(spinner_after);
                spinner_after_text.setText(spinner_before);
                spinner_before = spinner_before_text.getText().toString();

                if(spinner_before.equals("한국어")){

                    before = "ko";

                } else if(spinner_before.equals("영어")) {

                    before = "en";

                } else if(spinner_before.equals("중국어(간체)")) {

                    before  = "zh-CN";

                } else if(spinner_before.equals("중국어(번체)")) {

                    before = "zh-TW";

                } else if(spinner_before.equals("일본어")){

                    before = "ja";


                }

                spinner_after = spinner_after_text.getText().toString();

                if(spinner_after.equals("한국어")){

                    after = "ko";
                    Log.e("ㅂ", "after" +  after);
                } else if(spinner_after.equals("영어")) {

                    after = "en";
                    Log.e("ㅂ", "after" +  after);
                } else if(spinner_after.equals("중국어(간체)")) {

                    after  = "zh-CN";
                    Log.e("ㅂ", "after" +  after);
                } else if(spinner_after.equals("중국어(번체)")) {

                    after = "zh-TW";
                    Log.e("ㅁ", " after" +  after);
                } else if(spinner_after.equals("일본어")){

                    after = "ja";
                    Log.e("ㅅ", " after" +  after);
                }

            }
        });

        SPP = getSharedPreferences("SPP", Activity.MODE_PRIVATE);

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(translationActivity.this, "복사되었습니다.", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = SPP.edit();
                editor.putString("copy",resultText.getText().toString());
                Log.d("copy1", "copy" + resultText.getText().toString());
                editor.commit();

            }
        });


        // 성별 선택 스피너
        translationarrayList = new ArrayList<>();
        translationarrayList.add("한국어");
        translationarrayList.add("영어");
        translationarrayList.add("중국어(간체)");
        translationarrayList.add("중국어(번체)");
        translationarrayList.add("일본어");
        translationarrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                translationarrayList);





        // 국적 선택 스피너
        resultarryList = new ArrayList<>();
        resultarryList.add("영어");
        resultarryList.add("한국어");
        resultarryList.add("중국어(간체)");
        resultarryList.add("중국어(번체)");
        resultarryList.add("일본어");
        resultarrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                resultarryList);



        translationspinner = (Spinner)findViewById(R.id.translationspinner);
        resultspinner = (Spinner)findViewById(R.id.resultspinner);


        translationspinner.setAdapter(translationarrayAdapter);
        translationspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


               spinner_before = String.valueOf(translationarrayList.get(i));
              //  Log.i(TAG, "Spinner selected item = "+ select_gender);
                Log.e("스피너", "spinner_before" + spinner_before);


                if(spinner_before.equals("한국어")){

                   before = "ko";
                    Log.e("스피너1", "spinner_before" + spinner_before);
                    Log.e("스피너1-2", "before" + before);
                } else if(spinner_before.equals("영어")) {

                    before = "en";
                    Log.e("스피너2", "before" + before);
                } else if(spinner_before.equals("중국어(간체)")) {

                    before  = "zh-CN";
                    Log.e("스피너3", "before" + before);
                } else if(spinner_before.equals("중국어(번체)")) {

                    before = "zh-TW";
                    Log.e("스피너4", "before" + before);
                } else if(spinner_before.equals("일본어")){

                    before = "ja";
                    Log.e("스피너5", "before" + before);

                }

                 spinner_before_text.setText(spinner_before);

            }


            @Override

            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });





        resultspinner.setAdapter(resultarrayAdapter);
        resultspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {



                spinner_after = String.valueOf(resultarryList.get(i));



                if(spinner_after.equals("한국어")){

                    after = "ko";
                    Log.e("ㅂ", "after" +  after);
                } else if(spinner_after.equals("영어")) {

                    after = "en";
                    Log.e("ㅂ", "after" +  after);
                } else if(spinner_after.equals("중국어(간체)")) {

                    after  = "zh-CN";
                    Log.e("ㅂ", "after" +  after);
                } else if(spinner_after.equals("중국어(번체)")) {

                    after = "zh-TW";
                    Log.e("ㅁ", " after" +  after);
                } else if(spinner_after.equals("일본어")){

                    after = "ja";
                    Log.e("ㅅ", " after" +  after);
                }

                spinner_after_text.setText(spinner_after);
            }


            @Override

            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


        translationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundTask().execute();
                resultText.setVisibility(View.VISIBLE);
            }
        });


    } // end of create
}
