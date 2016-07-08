package com.example.he.ListviewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.he.batteryinfoActivity.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by HE on 2016/3/1.
 * 重写baseAdapter，提高listview载入速度
 */
public class vhAdapter_Equipment extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Map<Integer,String>> mlistItems = new ArrayList<>();

    public vhAdapter_Equipment(Context context, List<Map<Integer,String>> mlistItems){
        this.mInflater = LayoutInflater.from(context);
        this.mlistItems = mlistItems;
    }

    @Override
    public int getCount() {
        return mlistItems.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.equipment_item,null,true);
            holder.textView_eqm = (TextView) convertView.findViewById(R.id.textView_eqm);
            holder.textView_eqmid = (TextView) convertView.findViewById(R.id.textView_eqmid);
            holder.imageView_light_0 = (ImageView) convertView.findViewById(R.id.imageView_light_0);
            holder.imageView_light_1 = (ImageView) convertView.findViewById(R.id.imageView_light_1);
            holder.imageView_light_2 = (ImageView) convertView.findViewById(R.id.imageView_light_2);
            holder.imageView_light_3 = (ImageView) convertView.findViewById(R.id.imageView_light_3);
            holder.imageView_light_4 = (ImageView) convertView.findViewById(R.id.imageView_light_4);
            holder.imageView_light_5 = (ImageView) convertView.findViewById(R.id.imageView_light_5);
            holder.imageView_light_6 = (ImageView) convertView.findViewById(R.id.imageView_light_6);
            holder.imageView_light_7 = (ImageView) convertView.findViewById(R.id.imageView_light_7);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        try {
            holder.textView_eqm.setText(mlistItems.get(position).get(0));
            holder.textView_eqmid.setText(mlistItems.get(position).get(1));
            holder.imageView_light_0.setBackgroundResource(
                    (mlistItems.get(position).get(2).equals("正常")?R.drawable.green:
                            mlistItems.get(position).get(2).equals("空闲")?R.drawable.gray:R.drawable.red));
            holder.imageView_light_1.setBackgroundResource(
                    (mlistItems.get(position).get(3).equals("正常")?R.drawable.green:
                            mlistItems.get(position).get(3).equals("空闲")?R.drawable.gray:R.drawable.red));
            holder.imageView_light_2.setBackgroundResource(
                    (mlistItems.get(position).get(4).equals("正常")?R.drawable.green:
                            mlistItems.get(position).get(4).equals("空闲")?R.drawable.gray:R.drawable.red));
            holder.imageView_light_3.setBackgroundResource(
                    (mlistItems.get(position).get(5).equals("正常")?R.drawable.green:
                            mlistItems.get(position).get(5).equals("空闲")?R.drawable.gray:R.drawable.red));
            holder.imageView_light_4.setBackgroundResource(
                    (mlistItems.get(position).get(6).equals("正常")?R.drawable.green:
                            mlistItems.get(position).get(6).equals("空闲")?R.drawable.gray:R.drawable.red));
            holder.imageView_light_5.setBackgroundResource(
                    (mlistItems.get(position).get(7).equals("正常")?R.drawable.green:
                            mlistItems.get(position).get(7).equals("空闲")?R.drawable.gray:R.drawable.red));
            holder.imageView_light_6.setBackgroundResource(
                    (mlistItems.get(position).get(8).equals("正常")?R.drawable.green:
                            mlistItems.get(position).get(8).equals("空闲")?R.drawable.gray:R.drawable.red));
            holder.imageView_light_7.setBackgroundResource(
                    (mlistItems.get(position).get(9).equals("正常")?R.drawable.green:
                            mlistItems.get(position).get(9).equals("空闲")?R.drawable.gray:R.drawable.red));
        }catch (Exception e){
        }


        return convertView;
    }

    static class ViewHolder{
        public TextView textView_eqm;
        public TextView textView_eqmid;
        public ImageView imageView_light_0;
        public ImageView imageView_light_1;
        public ImageView imageView_light_2;
        public ImageView imageView_light_3;
        public ImageView imageView_light_4;
        public ImageView imageView_light_5;
        public ImageView imageView_light_6;
        public ImageView imageView_light_7;
    }
}


