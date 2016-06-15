package com.example.he.ListviewAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.he.batteryinfoActivity.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HE on 2016/6/1.
 * 重写baseAdapter
 */
public class vhAdapter_BatteryInfo_title extends BaseAdapter {
    private LayoutInflater mInflater;
    private Map<String,String> mlistItem = new HashMap<>();
    private Context mcontext;
    private Activity mactivity;
    private int length;


    public vhAdapter_BatteryInfo_title(Context mcontext, Map<String, String> mlistItem, int length){
        this.mInflater = LayoutInflater.from(mcontext);
        this.mlistItem = mlistItem;
        this.mcontext = mcontext;
        mactivity = (Activity)mcontext;
        this.length = length;
    }


    @Override
    public int getCount() {
        return 1;
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
            convertView = mInflater.inflate(R.layout.tabletitle_item_eqm,null,true);
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

            textViews.add(holder.t0);textViews.add(holder.t1);textViews.add(holder.t2);
            textViews.add(holder.t3);textViews.add(holder.t4);textViews.add(holder.t5);
            textViews.add(holder.t6);textViews.add(holder.t7);textViews.add(holder.t8);
            textViews.add(holder.t9);textViews.add(holder.t10);

            for (int i = length;i<textViews.size();i++){
                textViews.get(i).setVisibility(View.GONE);
            }

            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();
        }

//        //设置文本内容和监听,注意，这里些是错误
//        for (int i = 0;i<length;i++){
//            textViews.get(i).setText(mlistItem.get(String.valueOf(i)));
//            textViews.get(i).setOnClickListener(myListviewListener);
//        }
        //一般只能用以下方法
        holder.t0.setText(mlistItem.get("0"));
        holder.t1.setText(mlistItem.get("1"));
        holder.t2.setText(mlistItem.get("2"));
        holder.t3.setText(mlistItem.get("3"));
        holder.t4.setText(mlistItem.get("4"));
        holder.t5.setText(mlistItem.get("5"));
        holder.t6.setText(mlistItem.get("6"));
        holder.t7.setText(mlistItem.get("7"));

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
    }
}
