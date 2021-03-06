package com.womenproiot.www.link;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.MarkerIcons;
import com.womenproiot.www.link.util.GpsInfo;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener {
// TODO: 2018-11-28 위치검색 -> 결과 5개 보여주고 선택하면 마커등록
// TODO: 2018-11-28 Circle 안에 들어가는 주소,길이름 받아오기  -> 서울시 데이터와 
// TODO: 2018-11-28 네이버에서 음식점 검색 추천.

    //상수정의
    static final int REQUEST_CODE = 2000;

    private NaverMap naverMap=null;
    private LatLng mLatLng = null;
    private CircleOverlay circle = null,circle0 = null;
    private ArrayList<Marker> markerList = new ArrayList<>();
    private ArrayList<AttendeeDto> attendeeList = new ArrayList<>();

    private ImageButton btnSearchCenterPoint,buttonAccept,buttonCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //MapFragment 붙이기
        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            NaverMapOptions mapOptions = new NaverMapOptions().locationButtonEnabled(false);
            mapFragment = MapFragment.newInstance(mapOptions);
            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.map_fragment, mapFragment)
                                    .commit();
        }
        mapFragment.getMapAsync(this);

        // 권한 요청을 해야 함. onCreate에
        if (!isPermission) callPermission();
        //현재위치 받아와서 이동
        mLatLng = new LatLng(new GpsInfo(MainActivity.this).getLocation());
        callPermission();


        //출발지점 검색버튼 리스너 등록
        ((ImageButton)findViewById(R.id.btnDepatureSearch)).setOnClickListener(this);

        buttonAccept= findViewById(R.id.buttonAccept);
        buttonAccept.setOnClickListener(this);

        buttonCancel= findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(this);

        //중심점 찾기버튼 리스너 등록
        btnSearchCenterPoint = findViewById(R.id.btnSearchCenterPoint);
        btnSearchCenterPoint.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //locationSource = null;
    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        //현재 위치로 카메라 이동
        naverMap.moveCamera(CameraUpdate.scrollTo(mLatLng).animate(CameraAnimation.Fly, 3000));
    }

    @Override
    public void onClick(View view) {
        LatLng centerlatLng=null;
        try {
            switch (view.getId()) {

                case R.id.btnDepatureSearch:

                    String[] query = new String[32];
                    query[0] = ((EditText)findViewById(R.id.editTextDepaturePlace)).getText().toString();
                    query[1] = mLatLng.longitude+","+mLatLng.latitude;

                    ArrayList<AttendeeDto> jsonResult = null;
                    jsonResult = new JSONHelpeAsyncTask().execute(query).get();

                    if(jsonResult.size()>0) {

                        // TODO: 미진씨 리스트뷰 작업 추가
                        ListView result_list = (ListView) findViewById (R.id.result_list);
                        ArrayList<String> strArray = new ArrayList<> ();
                        for (AttendeeDto place : jsonResult) {
                            String str = place.name + " \n " + place.roadAddress ;
                                    //+ " / " + place.longitude + " / " + place.latitude;
                            strArray.add (str);
                        }

                        result_list.setVisibility (View.VISIBLE);

                        ArrayAdapter adapter = new ArrayAdapter<> (this, android.R.layout.simple_list_item_1, strArray);
                        result_list.setAdapter (adapter);

                        final ArrayList<AttendeeDto> resultFinal = jsonResult;
                        result_list.setOnItemClickListener (new AdapterView.OnItemClickListener () {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                result_list.setVisibility (View.GONE);
                                LatLng latLng = new LatLng(resultFinal.get(position).latitude, resultFinal.get(position).longitude);
                                naverMap.moveCamera(CameraUpdate.scrollTo(latLng).animate(CameraAnimation.Fly, 3000));
                                setMarker (latLng);
                            }

                        });

                    } else {
                        Toast.makeText(this,"검색된 위치정보가 없습니다.",Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.btnSearchCenterPoint:

                    if(markerList.size()<2) {
                        Toast.makeText(this,"출발지점을 2곳이상 등록하세요.",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    buttonChange();
                    centerlatLng = searchMeetupSpot();
                    naverMap.moveCamera(CameraUpdate.scrollTo(centerlatLng).animate(CameraAnimation.Fly, 3000));
                    setCenterCircle(centerlatLng);
                    break;

                case R.id.buttonAccept:

                    LinkDAO.getInstance(this).insertAttendees(getIntent().getStringExtra("seq"),attendeeList);
                    Intent intent = new Intent(this, ResultPlacesActivity.class);
                    if(centerlatLng != null) {
                        intent.putExtra("centerLat",centerlatLng.latitude);
                        intent.putExtra("centerLng",centerlatLng.longitude);
                    }
                    intent.putExtra("seq",getIntent().getStringExtra("seq"));
                    startActivityForResult(intent,REQUEST_CODE);
                    break;

                case R.id.buttonCancel:

                    // TODO: 2018-11-29 db에 마커들을 모두 지우거나 그냥 두고 중간점 찾기버튼을 보이거나
                    break;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    private void buttonChange() {
        btnSearchCenterPoint.setVisibility(
                (btnSearchCenterPoint.getVisibility() != View.VISIBLE)?
                View.VISIBLE : View.GONE
        );
        buttonAccept.setVisibility(
                (buttonAccept.getVisibility() != View.VISIBLE)?
                        View.VISIBLE : View.GONE
        );
        buttonCancel.setVisibility(
                (buttonCancel.getVisibility() != View.VISIBLE)?
                        View.VISIBLE : View.GONE
        );
    }

    private LatLng searchMeetupSpot() {
        double minX=0.0, maxX=0.0, minY=0.0, maxY=0.0 ,x,y;

        double[][] markerArray = null;
        if (markerList!=null){
            markerArray = new double[markerList.size()][2];
            for(int i=0 ; i<markerList.size() ; i++){
                markerArray[i][0] = markerList.get(i).getPosition().latitude; // X,위도
                markerArray[i][1] = markerList.get(i).getPosition().longitude; // Y,경도
            }
        }
        if (markerArray.length > 0){
            for(int i=0; i< markerArray.length; i++){
                minX = (markerArray[i][0] < minX || minX == 0) ? markerArray[i][0] : minX;
                maxX = (markerArray[i][0] > maxX || maxX == 0) ? markerArray[i][0] : maxX;
                minY = (markerArray[i][1] < minY || minY == 0) ? markerArray[i][1] : minY;
                maxY = (markerArray[i][1] > maxY || maxY == 0) ? markerArray[i][1] : maxY;
            }
        }
        LatLngBounds bounds = new LatLngBounds(new LatLng(minX,minY),new LatLng(maxX,maxY));
        naverMap.setExtent(bounds);

//        Marker marker = new Marker(MarkerIcons.YELLOW);
//        marker.setPosition(bounds.getCenter());
//        marker.setMap(naverMap);
//        marker.setOnClickListener(this::onClick);
        Log.w("[kja]중심점","위도 : " + bounds.getCenter().latitude +" / 경도 : "+ bounds.getCenter().longitude);
        return bounds.getCenter();
        //return new LatLng(minX+((maxX-minX)/2),minY+((maxY-minY)/2));
    }

    private void setCenterCircle(LatLng latLng) {
        int radius = getResources().getDimensionPixelSize(R.dimen.pick_radius);

        if(circle!=null) circle.setMap(null);
        if(circle0!=null) circle0.setMap(null);

        circle = new CircleOverlay(latLng,radius);
        circle.setColor(getResources().getColor(R.color.colorCircle));
        circle.setMap(naverMap);
        circle0 = new CircleOverlay(latLng,50);
        circle0.setColor(getResources().getColor(R.color.colorCircle0));
        circle0.setMap(naverMap);
    }

    private void setMarker(LatLng latLng) {
        Marker marker = new Marker(MarkerIcons.RED);
        marker.setPosition(latLng);
        marker.setHeight(110);
        marker.setWidth(80);
        marker.setCaptionAlign(Align.Center);
        marker.setCaptionText("박");
        marker.setCaptionTextSize(18);
        marker.setMap(naverMap);
        markerList.add(marker);
        marker.setOnClickListener(this::onClick);
    }

    /*
     * 마커 클릭하면....
     * 마커 지우기
     * */
    private boolean onClick(Overlay overlay) {
        Marker marker = (Marker)overlay;
        marker.setMap(null);
        markerList.remove(marker);
        return true;
    }





    //권한관련
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isPermission = false;

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
}












    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        Intent intent  =null;
        switch (item.getItemId()) {
            case R.id.meetup_reg :
                intent = new Intent(this,MeetupRegActivity.class);
                break;
            case R.id.location_reg :
                intent = new Intent(this,LocationRegActivity.class);
                break;
            case R.id.attendees :
                intent = new Intent(this,AttendeesActivity.class);
                break;
            case R.id.search_places :
                intent = new Intent(this,SearchPlacesActivity.class);
                break;
            case R.id.result_places :
                intent = new Intent(this,ResultPlacesActivity.class);
                break;
            default:return false;
        }
        startActivityForResult(intent,REQUEST_CODE);
        return true;
    }*/
