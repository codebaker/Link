package com.womenproiot.www.link;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.widget.Toast.LENGTH_SHORT;


public class MeetupRegActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, View.OnKeyListener {

    private LinkDAO dbHelper;
    private SQLiteDatabase mdb;
    private RadioGroup radioGroup;

    RadioButton womanButton, manButton, mixButton;
    Spinner spinnerAge;
    Button buttonInput;
    String seletedGender;
    String seletedAge;
    EditText editTextNameInput;
    int checkRadio=0;
    int checkSpinner=0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_reg);



        RadioButton womanButton= findViewById(R.id.womanButton);
        RadioButton manButton= findViewById(R.id.manButton);
        RadioButton mixButton= findViewById(R.id.mixButton);
        ((Button) findViewById(R.id.buttonInput)).setOnClickListener(this);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);


        dbHelper = LinkDAO.getInstance(this);
        mdb = dbHelper.getWritableDatabase();


        editTextNameInput = (EditText) findViewById(R.id.editTextNameInput);
        editTextNameInput.setOnKeyListener(this);


        Spinner spinner1 = (Spinner) findViewById(R.id.spinnerAge);//연령대 spinner

        List<String> listAge = new ArrayList<String>();
        listAge.add("모임원 연령대 입력");
        listAge.add("10대");
        listAge.add("20대");
        listAge.add("30대");
        listAge.add("40대");
        listAge.add("50대");
        listAge.add("60대 이상");
        listAge.add("모든 연령대 포함");
        ArrayAdapter<String> dataAdapter1 =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAge);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter1);
        spinner1.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (position >= 1) {
            seletedAge = ((TextView) view).getText().toString();
            checkSpinner=1;

        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {//Enter key Action
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            InputMethodManager jmk = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            jmk.hideSoftInputFromWindow(v.getWindowToken(), 0);    //hide keyboard
            return true;
        }
        return false;

    }

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

            if (i == R.id.womanButton) {
                seletedGender = "여성";
                checkRadio=1;
            } else if (i == R.id.manButton) {
                seletedGender = "남성";
                checkRadio=1;
            } else if (i == R.id.mixButton) {
                seletedGender = "남녀 포함";
                checkRadio=1;
            }

        }};

    @Override
    public void onClick(View v) {

        String title = editTextNameInput.getText().toString();


        if (title.length() == 0)

        {
            editTextNameInput.requestFocus();
            editTextNameInput.setError("모임이름을 입력해주세요");
        }
        else if(checkRadio==0){
            Toast.makeText(getApplicationContext(), "모임원의 성별을 선택해주세요", Toast.LENGTH_SHORT).show();


        }
        else if(checkSpinner==0){
            Toast.makeText(getApplicationContext(), "모임원의 연령대를 선택해주세요", Toast.LENGTH_SHORT).show();


        }
        else if(title.length()>0&&checkSpinner==1&&checkRadio==1) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);


            Random r = new Random();
            String str = "" + ((char) (r.nextInt(26) + 97)); // for the first character
            while (str.length() < 5) //to add only till the length is less than 5.
            {
                int n = r.nextInt(10); // get new number
                if (!str.contains(n + ""))
                    str += n; // add only if it does not already contain the number.
            }


            // 현재시간 변수 정의 : 현재시간을 msec 으로 구한다.
            long now = System.currentTimeMillis();
            // 현재시간을 date 변수에 저장한다.
            Date date = new Date(now);
            // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            // nowDate 변수에 값을 저장한다.
            String formatDate = sdfNow.format(date);


            String query = "INSERT INTO MEETUP VALUES ('" + str + "','" + title + "','" + seletedAge + "','" + seletedGender + "','" + formatDate + "','" + null + "')";
            mdb.execSQL(query);
            Toast.makeText(getApplicationContext(), title + "모임등록 완료", LENGTH_SHORT).show();

            Intent intent  = new Intent(this,MainActivity.class);
            startActivity(intent);


        }
    }
}
