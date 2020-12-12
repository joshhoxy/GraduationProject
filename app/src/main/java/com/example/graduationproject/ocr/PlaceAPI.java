package com.example.graduationproject.ocr;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PlaceAPI {

    Context mContext;

    StringBuilder mResponseBuilder = new StringBuilder();
    ArrayList<StoreData> mList = new ArrayList<>();

    PlaceAPI(Context con, double lat, double lon, double radius, String type)
    {
        mContext = con;
        try{
            String uStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lon
                    + "&radius=" + radius + "&types=" + type + "&key=AIzaSyAOXMGdV55T36994k1erX0-hwpvnvRm9Jc";

            Log.d("check", uStr);

            URL url = new URL(uStr);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));

            String line;
            while((line = in.readLine()) != null)
            {
                mResponseBuilder.append(line);
            }
            in.close();

        }catch (MalformedURLException me){
            me.printStackTrace();
        }
        catch (UnsupportedEncodingException ue){
            ue.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void parsing()
    {
        try{
            JSONArray jsonArray;
            JSONObject jsonObject;

            jsonObject = new JSONObject(mResponseBuilder.toString());
            jsonArray = jsonObject.getJSONArray("results");

            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject result = jsonArray.getJSONObject(i);

                //이름 얻기
                String store_name = result.getString("name");
                Log.d("check", "name : " + store_name);

                //price_level
                String price_level = "0";
                if(result.has("price_level"))
                    price_level = result.getString("price_level");

                //rating
                String rating = "0";
                if(result.has("rating"))
                    rating = result.getString("rating");

                //business_status
                String business_status = "null";
                if(result.has("business_status"))
                    business_status = result.getString("business_status");

                //opening_hours
                JSONObject opening_hours;
                String open_now = null;
                if(result.has("opening_hours"))
                {
                    opening_hours = result.getJSONObject("opening_hours");
                    open_now = opening_hours.getString("open_now");
                }

                mList.add(new StoreData(store_name, Float.valueOf(price_level), Float.valueOf(rating), business_status, open_now));
                //Log.d("check", String.valueOf(mList.size()));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public ArrayList<StoreData> getList()
    {
        return mList;
    }
}
