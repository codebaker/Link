package com.womenproiot.www.link;

import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;

public class MeetupDto {

    public String seq;
    public String title;
    public String age;
    public String gender;
    public ArrayList<AttendeeDto> attendees;
    public LatLng centerPoint;

    public MeetupDto(String seq, String title, String age, String gender,
                     ArrayList<AttendeeDto> attendees, LatLng centerPoint) {
        this.seq = seq;
        this.title = title;
        this.age = age;
        this.gender = gender;
        this.attendees = attendees;
        this.centerPoint = centerPoint;
    }
    public MeetupDto(String seq, String title) {
        this.seq = seq;
        this.title = title;
    }
}
