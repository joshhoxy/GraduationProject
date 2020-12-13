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
    private final String[] data = new String[]{"name: EDIYA"+"   "+ "location: Gyeonggi-do, Seongnam-si"   +"   " +"rating: 3.8",
                                                "name: Twosome Place"+"   "+ "location: Gyeonggi-do, Seongnam-si"   +"   " +"rating: 4.1",
                                                "name: COMPOSE COFFFEE"+"   "+ "location: Gyeonggi-do, Seongnam-si"   +"   " +"rating: 3.8",
                                                "name: An old chum"+"   "+ "location: Gyeonggi-do, Suwon-si"   +"   " +"rating: 4.2"};
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
        String val = i+1 +". " + data[i];
        tv.setText(val);
        return v;
    }
}
