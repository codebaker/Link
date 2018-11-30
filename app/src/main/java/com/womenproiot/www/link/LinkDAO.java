package com.womenproiot.www.link;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

public class LinkDAO extends SQLiteOpenHelper {
    private static LinkDAO instance;
    private static SQLiteDatabase mdb;
    String sql;
    Cursor cursor;

    public static final String DB_NAME = "link.db";
    private static final SQLiteDatabase.CursorFactory FACTORY = null;
    public static final int VERSION = 1;

    //db를 한개만 열어서 쓰기 위해 생성자를 private로.
    //객체는 getInstance()로만 얻을 수 있음.
    private LinkDAO(Context context) {

        super(context, DB_NAME, FACTORY, VERSION);
    }

    public static LinkDAO getInstance(Context context) {
        if(instance == null) instance = new LinkDAO(context);
        mdb = instance.getWritableDatabase();
        return instance;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE MEETUP " +
                            "(SEQ TEXT NOT NULL UNIQUE PRIMARY KEY" +
                            ", TITLE TEXT" +
                            ", AGE TEXT" +
                            ", GENDER TEXT" +
                            ", REG_DATE TEXT" +
                            ", MODI_DATE TEXT)";

        db.execSQL(sql);

        sql = "CREATE TABLE ATTENDEE (FR_SEQ TEXT NOT NULL" +
                        ", NAME TEXT" +
                        ", LATITUDE INTEGER" +
                        ", LONGITUDE INTEGER" +
                        ", REG_DATE TEXT" +
                        ", MODI_DATE TEXT" +
                        ", FR_CODE INTEGER NOT NULL" +
                        ", ADDRESS TEXT NOT NULL)";
        db.execSQL(sql);

        //초기에 메뉴와 테이블 테이터 5개씩 자동 삽입
        autoInsert(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE MEETUP ;");
        db.execSQL("DROP TABLE ATTENDEE;");
        onCreate(db);
    }

    //테스트 하기위한 fake data
    private void autoInsert(@NonNull SQLiteDatabase db){
        String meetup = "INSERT INTO MEETUP VALUES ('m0172','수요일엔닭모임', '40대', '여성','2018/11/29 06:20:09',null)";
        String attendee[] = new String[5];
        attendee[0] = "INSERT INTO attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) values ('m0172','광화문역 5호선', 37.5712497, 126.9773945, '2018/11/29 06:20:09',null,'QuQTW2cBOAUY6uUl2HEK','서울특별시 종로구 세종대로 172' )";
        attendee[1] = "INSERT INTO attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) values ('m0172','광화문역 5호선', 37.5712497, 126.9773945, '2018/11/29 06:20:09',null,'QuQTW2cBOAUY6uUl2HEK','서울특별시 종로구 세종대로 172' )";
        attendee[2] = "INSERT INTO attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) values ('m0172','광화문역 5호선', 37.5712497, 126.9773945, '2018/11/29 06:20:09',null,'QuQTW2cBOAUY6uUl2HEK','서울특별시 종로구 세종대로 172' )";
        attendee[3] = "INSERT INTO attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) values ('m0172','광화문역 5호선', 37.5712497, 126.9773945, '2018/11/29 06:20:09',null,'QuQTW2cBOAUY6uUl2HEK','서울특별시 종로구 세종대로 172' )";
        attendee[4] = "INSERT INTO attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) values ('m0172','광화문역 5호선', 37.5712497, 126.9773945, '2018/11/29 06:20:09',null,'QuQTW2cBOAUY6uUl2HEK','서울특별시 종로구 세종대로 172' )";

        //0번 테이블은 Takeout
        db.execSQL(meetup);
        for (int i = 1; i < attendee.length; i++) {
            db.execSQL(attendee[i]);
        }
    }


}
