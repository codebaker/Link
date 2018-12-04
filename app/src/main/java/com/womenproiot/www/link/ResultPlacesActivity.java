package com.womenproiot.www.link;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;

import java.util.ArrayList;

public class ResultPlacesActivity extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_places);

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

        LinkDAO.getInstance(this).selectMeetUp(getIntent().getStringExtra("seq"));
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

    }
}
