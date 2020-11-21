package com.example.graduationproject.ocr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    SurfaceView cameraView;
    TextView textView, textViewAll;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_main);
        for (int i=0;i<10;i++){

        }
        cameraView = (SurfaceView)findViewById(R.id.surface_view);
        textView = (TextView)findViewById(R.id.text_view);
        textViewAll = (TextView)findViewById(R.id.text_view_all);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
        if(!textRecognizer.isOperational())
        {
            Log.w("MainActivity","Detector dependencies are not yet available");
        }
        else{

            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280,1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback(){

                @Override
                public void surfaceCreated(SurfaceHolder holder) {

                    try{
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(com.example.graduationproject.ocr.MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }

                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                ArrayList<String> result = new ArrayList<String>();
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    Log.d("Main","receiveDetections");
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    int count = 100;

                    if(items.size() != 0){
                        textView.post(new Runnable() {
                            @Override
                            public void run() {

                                StringBuilder stringBuilder = new StringBuilder();
                                for (int j = 0; j < items.size(); ++j) {
                                    TextBlock item = items.valueAt(j);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                result.add(stringBuilder.toString());

                                int[] index = new int[count];
                                if(result.size() == count){
                                    int max_idx = 0;
                                    int max_value = 0;
                                    index[0] = 0;
                                    Collections.sort(result);
                                    for ( int i=0; i< result.size()-1; i++) {
                                        if (result.get(i).equalsIgnoreCase(result.get(i + 1)))
                                            index[i+1] = index[i]+1;
                                    }
                                    Arrays.sort(index);
                                    int ind_max = index[index.length-1];
                                    String mod_result = result.get(ind_max).toString();

                                    /*for (int i=0;i<(count - 1);i++){
                                        if(index[i] < index[i + 1]){
                                            max_idx = i + 1;
                                            max_value = index[i + 1];
                                        }
                                    }
                                    String mod_result = result.get(max_idx).toString();*/

                                    textView.setText(mod_result + ", " + String.valueOf(ind_max + 1) + "%");
                                }
                                String list = result.toString();
                                textViewAll.setText(list);
                            }
                        });
                    }
                }
            });
        }
    }
}
