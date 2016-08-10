package com.example.he.ListviewAdapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.he.batteryinfoActivity.R;
import com.example.he.Activity.BatteryDataActivity.MyHandler;

/**
 * Created by HE on 2016/6/2.
 * 重写baseAdapter，提高listview载入速度
 */
public class vhAdapter_BatteryData extends BaseAdapter {
    private LayoutInflater mInflater;
    private Cursor cursor;
    MyHandler myHandler;

    public vhAdapter_BatteryData(Context context, Cursor cursor,MyHandler handler){
        this.mInflater = LayoutInflater.from(context);
        this.cursor = cursor;
        this.myHandler = handler;
        //exec = new ThreadPoolExecutor(100, 200, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
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
//                    super.onPostExecute(result);
//                    v.t0.setText(cursor.getString(0));
//                    v.t1.setText(cursor.getString(1));
//                    v.t2.setText(cursor.getString(2));
//                    v.t3.setText(cursor.getString(3));
//                    v.t4.setText(cursor.getString(4));
//                    v.t5.setText(cursor.getString(5));
//                    v.t6.setText(cursor.getString(6));
//                    v.t7.setText(cursor.getString(7));
//                    v.t8.setText(cursor.getString(8));
//                    v.t9.setText(cursor.getString(9));
//                    v.t10.setText(cursor.getString(10));
//                }
//            }.executeOnExecutor(exec,holder);

            holder.t0.setText(cursor.getString(0));
            holder.t1.setText(cursor.getString(1));
            holder.t2.setText(cursor.getString(2));
            holder.t3.setText(cursor.getString(3));
            holder.t4.setText(cursor.getString(4));
            holder.t5.setText(cursor.getString(5));
            holder.t6.setText(cursor.getString(6));
            holder.t7.setText(cursor.getString(7));
            holder.t8.setText(cursor.getString(8));
            holder.t9.setText(cursor.getString(9));
            holder.t10.setText(cursor.getString(10));
        }else if(position == cursor.getCount()){
            holder.t0.setText("*");
            holder.t1.setText("");
            holder.t2.setText("");
            holder.t3.setText("");
            holder.t4.setText("");
            holder.t5.setText("");
            holder.t6.setText("");
            holder.t7.setText("");
            holder.t8.setText("");
            holder.t9.setText("");
            holder.t10.setText("");
            myHandler.sendEmptyMessage(0);
            Log.i("HE-D-getData-gVfinish", String.valueOf(System.currentTimeMillis()));
        }

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


