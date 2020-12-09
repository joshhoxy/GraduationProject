package com.example.graduationproject.ocr;

import android.os.Bundle;

import com.example.graduationproject.R;


public class SearchThread extends MainActivity {
    protected void oncCreate (Bundle SavedInstance){
        super.onCreate(SavedInstance);
        setContentView(R.layout.activity_ocr_main);

        SearchThread myThread = new SearchThread();
        myThread.start();
    }

    private void start() {

    }

    public class ExampleThread extends Thread{
        private static final String TAG = "search location thread";

        public ExampleThread(){

        }
        public void run(){
            String keyword = textView.getText().toString();

        }
    }
}