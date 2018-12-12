package com.womenproiot.www.link;

class AttendeeDto {
    public String frSeq;
    public String name;
    public String roadAddress;
    public double latitude;
    public double longitude;
    public String sessionId;

    public AttendeeDto(String frSeq, String name, String roadAddress,
                       double latitude, double longitude,String sessionId) {
        this.frSeq = frSeq;
        this.name = name;
        this.roadAddress = roadAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sessionId = sessionId;
    }
}
