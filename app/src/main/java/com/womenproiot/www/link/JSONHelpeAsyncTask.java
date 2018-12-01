package com.womenproiot.www.link;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class JSONHelpeAsyncTask extends AsyncTask<String, Void, ArrayList<AttendeeDto>> {

    final static String NAVER_MAP_SEARCH_URL = "https://naveropenapi.apigw.ntruss.com/map-place/v1/search?query=";

    /*
     *장소 검색 API는 어떤 시설이나 지리적 위치의 장소 명칭을 질의어로 입력받아 최대 5개 장소의 장소 정보를 검색합니다.
     * */
    private ArrayList<AttendeeDto> getLocation(String... query) {

        ArrayList<AttendeeDto> resultList=new ArrayList<>();
        HttpURLConnection conn=null;
        InputStream in=null;

        try {
            //String encodeQuery = URLEncoder.encode(query,"UTF-8");
            String apiUrl = NAVER_MAP_SEARCH_URL+query[0]+"&coordinate="+query[1];
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", Constants.X_NCP_APIGW_API_KEY_ID);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", Constants.X_NCP_APIGW_API_KEY);
            int responseCode = conn.getResponseCode();
            if(responseCode == 200){
                in = new BufferedInputStream(conn.getInputStream());
            } else {
                in = new BufferedInputStream(conn.getErrorStream());
            }
            resultList = parseJson(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                if(in!=null) in.close();
                if(conn!=null) conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    private ArrayList<AttendeeDto> parseJson(InputStream in) {
        ArrayList<AttendeeDto> result = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(getStringFromInputStream(in));
            JSONArray placeArray = json.getJSONArray("places");
            String name,rAddr,jAddr,phone,id;
            double lat,lon, dist;
            for (int i=0 ; i<placeArray.length() ; i++) {
                name = placeArray.getJSONObject(i).getString("name");
                rAddr = placeArray.getJSONObject(i).getString("road_address");
                lon = placeArray.getJSONObject(i).getDouble("x");
                lat = placeArray.getJSONObject(i).getDouble("y");
                id = placeArray.getJSONObject(i).getString("sessionId");

                result.add(new AttendeeDto(name,rAddr,lat,lon,id));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getStringFromInputStream(InputStream in) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line = null;
        try{
            br= new BufferedReader((new InputStreamReader(in)));
            while((line=br.readLine()) != null) {
                sb.append(line);
            }
            Log.e("JSON",sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(br!=null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }


    @Override
    protected ArrayList<AttendeeDto> doInBackground(String... strings) {
        ArrayList<AttendeeDto> result = getLocation(strings);
        return result;
    }
}
