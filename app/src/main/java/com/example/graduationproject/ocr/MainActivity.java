package com.example.graduationproject.ocr;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
    public static TextView textView, textViewAll;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    public static String mod_result;
    Button buttonToMap;

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

    public String getModResult(){
        return mod_result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_main);
        ImageButton InfoButton = findViewById(R.id.
                btn_more);

        InfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName cn = new ComponentName("com.example.graduationproject", "com.example.graduationproject.ocr.OcrResultActivity");

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setComponent(cn);

                startActivity(intent);
            }
        });

        for (int i=0;i<10;i++){

        }
        cameraView = (SurfaceView)findViewById(R.id.surface_view);
        textView = (TextView)findViewById(R.id.text_view);
        textViewAll = (TextView)findViewById(R.id.text_view_all);

        buttonToMap = (Button)findViewById(R.id.buttonMap);

        buttonToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName cn = new ComponentName("com.example.graduationproject", "com.example.graduationproject.parse.MapMainActivity");

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setComponent(cn);
                startActivity(intent);
            }
        });

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
                    int count = 5;

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

                                if(result.size() == count)
                                    checkResult(result);
                            }
                        });
                    }
                }
            });
        }
    }

    public void checkResult(ArrayList<String> result)
    {
        int[] index = new int[100];
        Collections.sort(result);
        for(int i = 0; i < result.size() - 1; i++)
        {
            if(result.get(i).equalsIgnoreCase(result.get(i+1)))
                index[i+1] = index[i]+1;
        }
        Arrays.sort(index);
        int max_count = index[index.length -1];

        String ocr_result = result.get(max_count).toString();
        //Log.d("check", ocr_result);

        Intent intent = new Intent(getApplicationContext(), OcrResultActivity.class);
        intent.putExtra("ocr_result", ocr_result);
        //setResult(RESULT_OK, intent);
        startActivity(intent);
    }

    public String result_OCR = this.getModResult();
}
