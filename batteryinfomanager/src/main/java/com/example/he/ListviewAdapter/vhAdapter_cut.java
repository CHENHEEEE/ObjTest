package com.example.he.ListviewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.he.batteryinfoActivity.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by HE on 2016/3/1.
 * 重写baseAdapter，提高listview载入速度
 */
public class vhAdapter_cut extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Map<String,String>> mlistItems = new ArrayList<Map<String,String>>();
    private int length;

    public vhAdapter_cut(Context context, List<Map<String, String>> mlistItems, int length){
        this.mInflater = LayoutInflater.from(context);
        this.mlistItems = mlistItems;
        this.length = length;
    }

    @Override
    public int getCount() {
        return mlistItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        List<TextView> textViews = new ArrayList<>();
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.table_item,null,true);
            holder.t0 = (TextView) convertView.findViewById(R.id.t0);
            holder.t1 = (TextView) convertView.findViewById(R.id.t1);
            holder.t2 = (TextView) convertView.findViewById(R.id.t2);
            holder.t3 = (TextView) convertView.findViewById(R.id.t3);
            holder.t4 = (TextView) convertView.findViewById(R.id.t4);
            holder.t5 = (TextView) convertView.findViewById(R.id.t5);
            holder.t6 = (TextView) convertView.findViewById(R.id.t6);
            holder.t7 = (TextView) convertView.findViewById(R.id.t7);
            holder.t8 = (TextView) convertView.findViewById(R.id.t8);
            holder.t9 = (TextView) convertView.findViewById(R.id.t9);
            holder.t10 = (TextView) convertView.findViewById(R.id.t10);
            holder.t11 = (TextView) convertView.findViewById(R.id.t11);
            holder.t12 = (TextView) convertView.findViewById(R.id.t12);
            holder.t13 = (TextView) convertView.findViewById(R.id.t13);
            holder.t14 = (TextView) convertView.findViewById(R.id.t14);

            textViews.add(holder.t0);textViews.add(holder.t1);textViews.add(holder.t2);
            textViews.add(holder.t3);textViews.add(holder.t4);textViews.add(holder.t5);
            textViews.add(holder.t6);textViews.add(holder.t7);textViews.add(holder.t8);
            textViews.add(holder.t9);textViews.add(holder.t10);textViews.add(holder.t11);
            textViews.add(holder.t12);textViews.add(holder.t13);textViews.add(holder.t14);

            for (int i = length;i<textViews.size();i++){
                textViews.get(i).setVisibility(View.GONE);
            }
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.t0.setText(mlistItems.get(position).get("0"));
        holder.t1.setText(mlistItems.get(position).get("1"));
        holder.t2.setText(mlistItems.get(position).get("2"));
        holder.t3.setText(mlistItems.get(position).get("3"));
        holder.t4.setText(mlistItems.get(position).get("4"));
        holder.t5.setText(mlistItems.get(position).get("5"));
        holder.t6.setText(mlistItems.get(position).get("6"));
        holder.t7.setText(mlistItems.get(position).get("7"));
        holder.t8.setText(mlistItems.get(position).get("8"));
        holder.t9.setText(mlistItems.get(position).get("9"));
        holder.t10.setText(mlistItems.get(position).get("10"));
        holder.t11.setText(mlistItems.get(position).get("11"));
        holder.t12.setText(mlistItems.get(position).get("12"));
        holder.t13.setText(mlistItems.get(position).get("13"));
        holder.t14.setText(mlistItems.get(position).get("14"));
        return convertView;
    }

    static class ViewHolder{
        public TextView t0;
        public TextView t1;
        public TextView t2;
        public TextView t3;
        public TextView t4;
        public TextView t5;
        public TextView t6;
        public TextView t7;
        public TextView t8;
        public TextView t9;
        public TextView t10;
        public TextView t11;
        public TextView t12;
        public TextView t13;
        public TextView t14;
    }
}


