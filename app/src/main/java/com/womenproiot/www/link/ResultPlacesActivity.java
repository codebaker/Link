package com.womenproiot.www.link;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.CircleOverlay;

import java.util.ArrayList;

public class ResultPlacesActivity extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener {

    private String seq;
    private LatLng centerLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_places);


        //중심점 가져오기
        centerLatLng = new LatLng(getIntent().getDoubleExtra("centerLat",0),
                getIntent().getDoubleExtra("centerLng",0));


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





        String[] query = new String[32];
        query[0] = ((EditText)findViewById(R.id.editTextDepaturePlace)).getText().toString();
        query[1] = centerLatLng.longitude+","+centerLatLng.latitude;





        setResultPlaceRecyclerView();
    }

    private void setResultPlaceRecyclerView() {
        // TODO: 2018-12-07 여기에서
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.result_place_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ResultPlaceRecyclerAdapter adapter = new ResultPlaceRecyclerAdapter(this,getIntent().getStringExtra("seq"));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        setCircle(naverMap);
    }

    private void setCircle(NaverMap naverMap){
        if(centerLatLng != null){
            int radius = getResources().getDimensionPixelSize(R.dimen.pick_radius);
            CircleOverlay circle  = new CircleOverlay(centerLatLng,radius);
            circle.setColor(getResources().getColor(R.color.colorCircle));
            circle.setMap(naverMap);
        }
    }
}
