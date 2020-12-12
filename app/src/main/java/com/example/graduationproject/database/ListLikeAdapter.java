package com.example.graduationproject.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.graduationproject.R;

public class ListLikeAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    private final String[] data = new String[]{"test","test","test","test","test","test","test"};
    public ListLikeAdapter (Context context){
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v =  (view==null) ? layoutInflater.inflate(R.layout.item_list_like,null) : view;
        TextView tv = v.findViewById(R.id.tv);
        String val = data[i]+i;
        tv.setText(val);
        return v;
    }
}
