package com.example.graduationproject.parse;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.graduationproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

public class Parsing extends AppCompatActivity  {

    AssetManager am;
    InputStream is = null;

    public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int c = bundle.getChar("name");

        if(c == 'k')
        {
            String json = getJsonString_Ko();
            jsonParsing(json);
        }
        else if(c == 'e')
        {
            String json = getJsonString_En();
            jsonParsing(json);
        }
        else if(c=='c')
        {
            String json = getJsonString_Ch();
            jsonParsing(json);
        }
    }

    public String getJsonString_En()
    {
        String json = "";
        am = getResources().getAssets();

        try
        {
            is = am.open("서울시 자랑스러운 한국음식점 정보 (영어).json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);

            json = new String(buffer,"UTF-8");

            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
    public String getJsonString_Ko()
    {

        String json = "";
        am = getResources().getAssets();
        try
        {
            is = am.open("서울시 자랑스러운 한국음식점 정보 (한국어).json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);

            json = new String(buffer,"UTF-8");

            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
    public String getJsonString_Ch()
    {

        String json = "";
        am = getResources().getAssets();
        try
        {
            is = am.open("서울시 자랑스러운 한국음식점 정보 (중국어_간체).json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);

            json = new String(buffer,"UTF-8");

            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    public void jsonParsing(String json)
    {

        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray infoArray = jsonObject.getJSONArray("DATA");

            for(int i = 0; i < infoArray.length(); i++)
            {
                JSONObject infoObject = infoArray.getJSONObject(i);

                Information information = new Information();

                information.setCate1_name(infoObject.getString("cate1_name"));
                information.setCate2_name(infoObject.getString("cate2_name"));
                information.setCate3_name(infoObject.getString("cate3_name"));
                information.setH_kor_city(infoObject.getString("h_kor_city"));
                information.setH_kor_dong(infoObject.getString("h_kor_dong"));
                information.setH_kor_gu(infoObject.getString("h_kor_gu"));
                information.setName_kor(infoObject.getString("name_kor"));
                information.setNm_dp(infoObject.getString("nm_dp"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
