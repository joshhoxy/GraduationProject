package com.example.graduationproject.ocr;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.graduationproject.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class OcrResultActivity extends AppCompatActivity implements Runnable{

    TextView ocr_result_txtView;
    TextView place_now_txtView;
    TextView place_result_txtView;
    String result_text;

    String result;
    GpsTracker gpsTracker;
    PlaceAPI places;

    double latitude;
    double longitude;

    //ar code
    ArFragment arFragment;
    private ModelRenderable tigerRenderable;
    TextView card ;
    View arrayView[];
    ViewRenderable content;
    int selected = 1;
    String card_content;
    CharSequence charSequence;

    String store_name;
    String rating;
    String business_state;
    String open_now;
    String place_id;

    private FirebaseDatabase mDatabase;
    private DatabaseReference logRef;
    private FirebaseAuth mAuth;
    private String UID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_result);

        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance();
        logRef = mDatabase.getReference().child("User").child(UID).child("log");


        //ocr_result_txtView = (TextView) findViewById(R.id.ocr_result_text);
        //place_now_txtView = (TextView) findViewById(R.id.place_now_text);
        //place_result_txtView = (TextView) findViewById(R.id.place_result_text);

        //인텐트로 받아온 ocr 결과값을 꺼낸다
        Intent intent = getIntent();
        result = intent.getStringExtra("ocr_result");
        //Log.d("check", result);
        //ocr_result_txtView.setText(result);

        //gpsTracker 클래스 사용해서 현재 위치를 알아냄
        gpsTracker = new GpsTracker(OcrResultActivity.this);
        //LatLng currentLatLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        String now_location = latitude + ", " + longitude;
        //place_now_txtView.setText(now_location);

        Thread t = new Thread(this);
        t.start();

        //ar code
        card = (TextView)findViewById(R.id.make_card);
        arFragment = (ArFragment)getSupportFragmentManager().findFragmentById(R.id.sceneform_ux_fragment);

        setClickListener();

        setupModel();

        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener(){
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                //when user tap on plane, we will add model
                if(selected == 1)
                {
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    createModel(anchorNode,selected);
                }
            }
        });

    }

    private void setupModel() {

        ViewRenderable.builder()
                .setView(this,R.layout.name_card)
                .build()
                .thenAccept(renderable -> content = renderable);

        ModelRenderable.builder()
                .setSource(
                        this,
                        Uri.parse(
                                "https://storage.googleapis.com/ar-answers-in-search-models/static/Tiger/model.glb"))
                .build().thenAccept(renderable -> tigerRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this, "Unable to load Tiger renderable", Toast.LENGTH_LONG).show();
                            return null;
                        }
                );
    }

    private void createModel(AnchorNode anchorNode, int selected){
        if(selected == 1){
            TransformableNode ar_text = new TransformableNode(arFragment.getTransformationSystem());
            ar_text.setParent(anchorNode);
            ar_text.setRenderable(tigerRenderable);
            ar_text.select();


            addName(anchorNode, ar_text, card_content);
        }
    }

    private void addName(AnchorNode anchorNode, TransformableNode model, String name){
        TransformableNode nameView = new TransformableNode(arFragment.getTransformationSystem());
        nameView.setLocalPosition(new Vector3(0f, model.getLocalPosition().y+0.5f,0));
        nameView.setParent(anchorNode);
        nameView.setRenderable(content);
        nameView.select();

        TextView txt_name = (TextView)content.getView();
        txt_name.setText(name);

        txt_name.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                anchorNode.setParent(null);
            }
        });
    }

    private void setClickListener(){

    }


    public void onClick(View view){
        if(view.getId() == R.id.make_card)
            selected = 1;

    }

    @Override
    public void run() {

        //현재 위치 근방에 있는 가게들의 정보를 알아온다.
        places = new PlaceAPI(this, latitude, longitude, 500.00, "restaurant");
        places.parsing();

        ArrayList<StoreData> store_data = new ArrayList<>();
        store_data = places.getList();

        //여기부터

        int i = 0;
        double[] similar = new double[store_data.size()];
        //Log.d("check", "check " + result);
        for(StoreData data : store_data)
        {
            String store_name = data.getStore_name();

            similar[i] = similarity(store_name, result);
            i++;
        }

        double max = 0;
        int max_index = 0;
        for(int j = 0; j < i; j++)
        {
            if(max < similar[j])
            {
                max = similar[j];
                max_index = j;
            }
        }
        store_name = store_data.get(max_index).getStore_name();
        rating = store_data.get(max_index).getRating().toString();
        business_state = store_data.get(max_index).getBusiness_status();
        open_now = store_data.get(max_index).getOpen_now();

        place_id = store_data.get(max_index).getPlace_id();

//        if(open_now.equalsIgnoreCase("false")){
//            open_now = "No";
//        }
//        else if(open_now.equalsIgnoreCase("true")){
//            open_now = "Yes";
//        }
        card_content = "Name - " + store_name + "\nRating - " + rating + "\nBusiness state - " + business_state + "\nOpen now? " + open_now;

        // upload search history _ 일괄처리
        //logRef.push().setValue(card_content);


        logRef =logRef.child(place_id);
        logRef.child("name").setValue(store_name);
        logRef.child("rating").setValue(rating);
        logRef.child("name").setValue(store_name);

        Log.d("check", card_content);

        //place_result_txtView.setText(charSequence);

        //여기까지


        while(true)
        {
            try {
                Thread.sleep(50);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //밑에 함수 두개 추가했습니다~
    private double similarity(String s1, String s2){
        String longer = s1, shorter = s2;

        if(s1.length() < s2.length()){
            longer = s2;
            shorter = s1;
        }

        int longerLength = longer.length();
        if(longerLength == 0) return 1.0;

        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    private int editDistance(String s1, String s2){
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];

        for(int i = 0; i <= s1.length(); i++)
        {
            int lastValue = i;
            for(int j = 0; j <= s2.length(); j++)
            {
                if(i ==0){
                    costs[j] = j;
                } else {
                    if( j > 0){
                        int newValue = costs[j-1];

                        if(s1.charAt(i - 1) != s2.charAt(j - 1)){
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j])+1;
                        }

                        costs[j-1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
}