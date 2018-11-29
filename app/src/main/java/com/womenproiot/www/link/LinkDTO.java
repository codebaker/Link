package com.womenproiot.www.link;

public class LinkDTO {
    static final String X_NCP_APIGW_API_KEY_ID = "hzltr7zh5e";
    static final String X_NCP_APIGW_API_KEY = "sE5yynPMqq26Vak5BRntJcX8Z7FP3xy16qzV0qUl";

    class Place {
        public String name;
        public String roadAddress;
        public String jibunAddress;
        public String phoneNumber;
        public double latitude;
        public double longitude;
        public double distance;
        public String sessionId;

        public Place(String name, String roadAddress, String jibunAddress, String phoneNumber,
                     double latitude, double longitude, double distance, String sessionId) {
            this.name = name;
            this.roadAddress = roadAddress;
            this.jibunAddress = jibunAddress;
            this.phoneNumber = phoneNumber;
            this.latitude = latitude;
            this.longitude = longitude;
            this.distance = distance;
            this.sessionId = sessionId;
        }
    }
}
