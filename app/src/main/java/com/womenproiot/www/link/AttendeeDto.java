package com.womenproiot.www.link;

class AttendeeDto {
    public String name;
    public String roadAddress;
    public double latitude;
    public double longitude;
    public String sessionId;

    public AttendeeDto(String name, String roadAddress,
                       double latitude, double longitude,String sessionId) {
        this.name = name;
        this.roadAddress = roadAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sessionId = sessionId;
    }}
