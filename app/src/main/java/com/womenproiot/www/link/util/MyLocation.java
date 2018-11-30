package com.womenproiot.www.link.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocation {

    // GPSTracker class
    private GpsInfo gps;
    private String latitude,longitude;

    public MyLocation(Context context){

        gps = new GpsInfo(context);

        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {

            this.latitude = (String.valueOf(gps.getLongitude()));
            this.longitude = (String.valueOf(gps.getLongitude()));

        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }
    }

    public String getLatitude(){
        return this.latitude;
    };

    public String getLongitude(){
        return this.longitude;
    };

    public GpsInfo getGps(){ return this.gps; };

    public String getAddress(Context mContext, double lat, double lng) {
        String nowAddress ="현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress  = currentLocationAddress;

                }
            }

        } catch (IOException e) {
//            Toast.makeText(gps, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        return nowAddress;
    }



}


/*사용할 때



    //Manifest 권한 설정
    <!-- Google Map -->
    <!--<meta-data android:name="com.google.android.geo.API_KEY" android:value="" />-->
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />


    //Build.gradle(app)
    implementation 'com.google.android.gms:play-services-maps:16.0.0'




    //MainActivity.java에.....

    //Member 변수
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isPermission = false;



    // 권한 요청을 해야 함. onCreate에
    if (!isPermission) callPermission();
    MyLocation myLocation = new MyLocation(Activity.this);
    callPermission();


    // 전화번호 권한 요청. Activity에 메서드 추가
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }

*/