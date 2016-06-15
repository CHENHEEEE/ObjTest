package com.example.he.ListviewAdapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.he.batteryinfoActivity.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by HE on 2016/3/1.
 * 重写baseAdapter，提高listview载入速度
 */
public class vhAdapter_BatteryInfo extends BaseAdapter {
    private LayoutInflater mInflater;
    private int length;
    private ListView listView;
    private Context context;
    private Cursor cursor;
    private Executor exec;

    public vhAdapter_BatteryInfo(){
    }

    public void initadapter(Context context, int length, ListView listView, Cursor cursor){
        this.mInflater = LayoutInflater.from(context);
        this.length = length;
        this.listView = listView;
        this.context = context;
        this.cursor = cursor;
        //exec = new ThreadPoolExecutor(50, 200, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public int getCount() {
        return cursor.getCount()+1;
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
        List<TextView> textViews = new ArrayList<>();
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.table_item_fortab,null,true);
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

        if(cursor.moveToPosition(position)){
//            new AsyncTask<ViewHolder, Void, Cursor>() {
//                private ViewHolder v;
//
//                @Override
//                protected Cursor doInBackground(ViewHolder... params) {
//                    v = params[0];
//                    return cursor;
//                }
//
//                @Override
//                protected void onPostExecute(Cursor result) {
//                    v.t0.setText(result.getString(0));
//                    v.t1.setText(result.getString(1));
//                    v.t2.setText(result.getString(2));
//                    v.t3.setText(result.getString(3));
//                    v.t4.setText(result.getString(4));
//                    v.t5.setText(result.getString(5));
//                    v.t6.setText(result.getString(6));
//                    v.t7.setText(result.getString(7));
//                }
//            }.executeOnExecutor(exec,holder);

            try{
                holder.t0.setText(cursor.getString(0));
                holder.t1.setText(cursor.getString(1));
                holder.t2.setText(cursor.getString(2));
                holder.t3.setText(cursor.getString(3));
                holder.t4.setText(cursor.getString(4));
                holder.t5.setText(cursor.getString(5));
                holder.t6.setText(cursor.getString(6));
                holder.t7.setText(cursor.getString(7));
            }catch (Exception e){
            }
        }else if(position == cursor.getCount()){
            holder.t0.setText("*");
            holder.t1.setText("");
            holder.t2.setText("");
            holder.t3.setText("");
            holder.t4.setText("");
            holder.t5.setText("");
            holder.t6.setText("");
            holder.t7.setText("");
        }



        //按照筛选状态，更新背景
        int backgroundId;
        if (listView.isItemChecked(position)) {
            backgroundId = R.drawable.item_checked;
        } else {
            backgroundId = R.drawable.item_selector;
        }
        Drawable background = context.getResources().getDrawable(backgroundId);
        convertView.setBackgroundDrawable(background);

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

