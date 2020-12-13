package com.example.graduationproject.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.graduationproject.R;

public class List01Adapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    private final String[] data = new String[]{"name: EDIYA"+"   "+ "location: Gyeonggi-do, Seongnam-si"   +"   " +"rating: 3.8",
                                                "name: coffee only"+"   "+ "location: Gangnam-gu, Seoul"   +"   " +"rating: 4.5",
                                                "name: Parsis Baguette"+"   "+ "location: Seocho-gu, Seoul"   +"   " +"rating: 3.7",
                                                "name: Twosome Place"+"   "+ "location: Gyeonggi-do, Seongnam-si"   +"   " +"rating: 4.1",
                                                "name: COMPOSE COFFFEE"+"   "+ "location: Gyeonggi-do, Seongnam-si"   +"   " +"rating: 3.8",
                                                "name: An old chum"+"   "+ "location: Gyeonggi-do, Suwon-si"   +"   " +"rating: 4.2",
                                                };
    public List01Adapter(Context context){
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

        View v =  (view==null) ? layoutInflater.inflate(R.layout.item_list_01,null) : view;
        TextView tv = v.findViewById(R.id.tv);
        String val = data[i]+i;
        tv.setText(val);
        tv.setSelected(true);
        return v;
    }
}
