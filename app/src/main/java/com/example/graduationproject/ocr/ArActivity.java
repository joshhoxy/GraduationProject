package com.example.graduationproject.ocr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.graduationproject.R;
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

public class ArActivity extends AppCompatActivity
{
    ArFragment arFragment;
    private ModelRenderable tigerRenderable;
    TextView card ;
    View arrayView[];
    ViewRenderable content;
    int selected = 1;
    String card_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aractivity);

        Intent intent = getIntent();
        card_content = intent.getStringExtra("content");

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
                        throwable -> {Toast.makeText(this, "Unable to load Tiger renderable", Toast.LENGTH_LONG).show();
                            return null;
                        }
                );

    }

    private void createModel(AnchorNode anchorNode, int selected){
        if(selected == 1){
            TransformableNode tiger = new TransformableNode(arFragment.getTransformationSystem());
            tiger.setParent(anchorNode);
            tiger.setRenderable(tigerRenderable);
            tiger.select();


            addName(anchorNode, tiger, "hello");
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
}
