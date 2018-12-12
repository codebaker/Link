package com.womenproiot.www.link;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LinkDAO extends SQLiteOpenHelper {
    private static LinkDAO instance;
    private static SQLiteDatabase mdb;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String sql;
    Cursor cursor;

    public static final String DB_NAME = "link.db";
    private static final SQLiteDatabase.CursorFactory FACTORY = null;
    public static final int VERSION = 7;

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
        String meetup = "INSERT INTO MEETUP VALUES ('m0172','수요일엔닭모임', '40대', '여성','2018/11/29 06:20:05',null)";
        String attendee[] = new String[5];
        attendee[0] = "INSERT INTO attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) values ('m0172','광화문역 5호선', 37.5712497, 126.9773945, '2018/11/29 06:20:09',null,'QuQTW2cBOAUY6uUl2HEK','서울특별시 종로구 세종대로 172' )";
        attendee[1] = "INSERT INTO attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) values ('m0172','일산역 경의중앙선', 37.6820087, 126.7700597, '2018/11/29 06:20:10',null,'P8JjZWcBZphKmN8QmeUv','경기도 고양시 일산서구 경의로 672' )";
        attendee[2] = "INSERT INTO attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) values ('m0172','강남역7번출구', 37.4971748, 127.0277347, '2018/11/29 06:20:11',null,'4bFjZWcBe9kwkY1_1iAq','서울특별시 서초구 강남대로 433' )";
        attendee[3] = "INSERT INTO attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) values ('m0172','신갈역 분당선', 37.2861338, 127.1113233, '2018/11/29 06:20:12',null,'4b9kZWcBLhmWpuc1D1v4','경기도 용인시 기흥구 신갈동 167' )";
        attendee[4] = "INSERT INTO attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) values ('m0172','의정부역 1호선', 37.7384318, 127.0460187, '2018/11/29 06:20:13',null,'VKljZWcBe9kwkY1_OiJ7','경기도 의정부시 평화로 525' )";

        db.execSQL(meetup);
        for (int i = 0; i < attendee.length; i++) {
            db.execSQL(attendee[i]);
        }

        meetup = "INSERT INTO MEETUP VALUES ('q0275','고교동창', '50대', '여성','2018/12/29 11:55:05',null)";
        String attendee2[] = new String[3];
        attendee2[2] = "INSERT INTO attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) values ('q0275','광화문역 5호선', 37.5712497, 126.9773945, '2018/11/29 06:20:09',null,'QuQTW2cBOAUY6uUl2HEK','서울특별시 종로구 세종대로 172' )";
        attendee2[1] = "INSERT INTO attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) values ('q0275','일산역 경의중앙선', 37.6820087, 126.7700597, '2018/11/29 06:20:10',null,'P8JjZWcBZphKmN8QmeUv','경기도 고양시 일산서구 경의로 672' )";
        attendee2[0] = "INSERT INTO attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) values ('q0275','강남역7번출구', 37.4971748, 127.0277347, '2018/11/29 06:20:11',null,'4bFjZWcBe9kwkY1_1iAq','서울특별시 서초구 강남대로 433' )";
        db.execSQL(meetup);
        for (int i = 0; i < attendee2.length; i++) {
            db.execSQL(attendee2[i]);
        }

        meetup = "INSERT INTO MEETUP VALUES ('k0200','중학교동창', '60대', '남성','2018/12/29 11:55:05',null)";
        String attendee3[] = new String[3];
        attendee3[0] = "INSERT INTO attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) values ('k0200','강남역7번출구', 37.4971748, 127.0277347, '2018/11/29 06:20:11',null,'4bFjZWcBe9kwkY1_1iAq','서울특별시 서초구 강남대로 433' )";
        attendee3[2] = "INSERT INTO attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) values ('k0200','신갈역 분당선', 37.2861338, 127.1113233, '2018/11/29 06:20:12',null,'4b9kZWcBLhmWpuc1D1v4','경기도 용인시 기흥구 신갈동 167' )";
        attendee3[1] = "INSERT INTO attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) values ('k0200','의정부역 1호선', 37.7384318, 127.0460187, '2018/11/29 06:20:13',null,'VKljZWcBe9kwkY1_OiJ7','경기도 의정부시 평화로 525' )";
        db.execSQL(meetup);
        for (int i = 0; i < attendee3.length; i++) {
            db.execSQL(attendee3[i]);
        }
    }


    /*
    * SearchPlace에서 ResultPlaces로
    * */
    public void insertAttendees(String seq, ArrayList<AttendeeDto> attendees) {
        String sql = "INSERT INTO " +
                    "attendee (fr_seq,name,latitude,longitude,reg_date,modi_date,fr_code,address) " +
                    "values (?,?,?,?,?,?,?,?)";

        for (AttendeeDto atd : attendees) {

            mdb.execSQL(sql,new Object[]{seq
                                        ,atd.name
                                        ,String.valueOf(atd.latitude)
                                        ,String.valueOf(atd.longitude)
                                        ,format.format(new Date())
                                        ,"null"
                                        ,atd.sessionId
                                        ,atd.roadAddress
            });
        }
    }


    public ArrayList<MeetupDto> selectMeetupList() {

        String sql = "SELECT seq,title FROM meetup";

        cursor = mdb.rawQuery(sql,null);

        ArrayList<MeetupDto> list = new ArrayList<>();
        String seq,title;

        while(cursor.moveToNext()){
            seq = cursor.getString(cursor.getColumnIndex("SEQ"));
            title = cursor.getString(cursor.getColumnIndex("TITLE"));
            list.add(new MeetupDto(seq,title));
        }

        return list;
    }


    /*
     * seq로 Attendee 테이블에서 참석자를 검색해서 AttendeeDto 리스트로 만든다.
     * */
    public ArrayList<AttendeeDto> selectAttendee(String seq) {

        String sql = "SELECT fr_seq,name,address FROM attendee WHERE fr_seq = ?";

        cursor = mdb.rawQuery(sql,new String[]{seq});

        ArrayList<AttendeeDto> list = new ArrayList<>();
        String fr_seq,name,address;

        while(cursor.moveToNext()){
            fr_seq = cursor.getString(cursor.getColumnIndex("FR_SEQ"));
            name = cursor.getString(cursor.getColumnIndex("NAME"));
            address = cursor.getString(cursor.getColumnIndex("ADDRESS"));
            list.add(new AttendeeDto(fr_seq,name,address,0,0,null));
        }

        return list;
    }
}
