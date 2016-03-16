package com.example.he.ListviewAdapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.he.MyTools.Flags;
import com.example.he.PopupWindow.ListPopup;
import com.example.he.batteryinfoActivity.R;
import com.example.he.database.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by HE on 2016/3/1.
 * 重写baseAdapter，对标题栏每个Item加入监听
 */
public class vhAdapterwithListener extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Map<String,String>> mlistItems = new ArrayList<>();
    private Context mcontext;
    private Activity mactivity;
    private int length;
    private Handler handler;

    public vhAdapterwithListener(Context context, List<Map<String, String>> mlistItems){
        this.mInflater = LayoutInflater.from(context);
        this.mlistItems = mlistItems;
        this.mcontext = context;
        mactivity = (Activity)context;
    }

    public vhAdapterwithListener(Context context, List<Map<String, String>> mlistItems,int length,Handler handler){
        this.mInflater = LayoutInflater.from(context);
        this.mlistItems = mlistItems;
        this.mcontext = context;
        mactivity = (Activity)context;
        this.length = length;
        this.handler = handler;
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
        MyListviewListener myListviewListener = new MyListviewListener();
        List<TextView> textViews = new ArrayList<>();
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.tabletitle_item,null,true);
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

//            //设置文本内容和监听，注意，只能用于getview使用一次的
//            for (int i = 0;i<length;i++){
//                textViews.get(i).setText(mlistItems.get(position).get(String.valueOf(i)));
//                textViews.get(i).setOnClickListener(myListviewListener);
//            }

            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();
        }

//        //设置文本内容和监听,注意，这里些是错误
//        for (int i = 0;i<length;i++){
//            textViews.get(i).setText(mlistItems.get(position).get(String.valueOf(i)));
//            textViews.get(i).setOnClickListener(myListviewListener);
//        }
        //一般只能用以下方法
        holder.t0.setText(mlistItems.get(position).get("0"));
        holder.t1.setText(mlistItems.get(position).get("1"));
        holder.t2.setText(mlistItems.get(position).get("2") + "ˇ");
        holder.t3.setText(mlistItems.get(position).get("3") + "ˇ");
        holder.t4.setText(mlistItems.get(position).get("4") + "ˇ");
        holder.t5.setText(mlistItems.get(position).get("5") + "ˇ");
        holder.t6.setText(mlistItems.get(position).get("6") + "ˇ");
        holder.t7.setText(mlistItems.get(position).get("7") + "ˇ");
        holder.t8.setText(mlistItems.get(position).get("8") + "ˇ");
        holder.t9.setText(mlistItems.get(position).get("9") + "ˇ");
        holder.t10.setText(mlistItems.get(position).get("10") + "ˇ");
        holder.t11.setText(mlistItems.get(position).get("11") + "ˇ");
        holder.t12.setText(mlistItems.get(position).get("12") + "ˇ");
        holder.t13.setText(mlistItems.get(position).get("13") + "ˇ");
        holder.t14.setText(mlistItems.get(position).get("14") + "ˇ");

        //设置item监听
        holder.t2.setOnClickListener(myListviewListener);holder.t10.setOnClickListener(myListviewListener);
        holder.t3.setOnClickListener(myListviewListener);holder.t11.setOnClickListener(myListviewListener);
        holder.t4.setOnClickListener(myListviewListener);holder.t12.setOnClickListener(myListviewListener);
        holder.t5.setOnClickListener(myListviewListener);holder.t13.setOnClickListener(myListviewListener);
        holder.t6.setOnClickListener(myListviewListener);holder.t14.setOnClickListener(myListviewListener);
        holder.t7.setOnClickListener(myListviewListener);
        holder.t8.setOnClickListener(myListviewListener);
        holder.t9.setOnClickListener(myListviewListener);
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

    private class MyListviewListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d("HeD", "onclick");

            TextView tv = (TextView)v;
            String tag = tv.getTag().toString();
            Log.d("HeD",tag);
            String columnName = tv.getText().toString().substring(0, tv.getText().toString().length() - 1);
            startPOPwindow(columnName,tag);
        }

        private void startPOPwindow(final String columnName, final String colid){

            final ListPopup mListPopup;
            ListPopup.Builder builder=new ListPopup.Builder(mactivity);

            //SQLite查询
            MySQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance();
            final SQLiteDatabase database = helper.getWritableDatabase();//调用方法，创建或打开链接

            Cursor cursor = database.rawQuery("select distinct " + columnName + " from one order by " + columnName,null);
            cursor.moveToFirst();

            int i=0;
            final List<String> s = new ArrayList<>();
            while (!cursor.isAfterLast()){
                s.add(cursor.getString(0));
                builder.addItem(i,cursor.getString(0));
                Log.d("HeD_database",cursor.getString(0));
                i++;
                cursor.moveToNext();
            }
//            builder.addItem(TAG_CREATE,"Create-01");
            mListPopup=builder.build();

            mListPopup.setOnListPopupItemClickListener(new ListPopup.OnListPopupItemClickListener() {
                @Override
                public void onItemClick(int what) {
                    //标志为true，表示开始了筛选
                    Flags flags = Flags.getInstance();
                    flags.setFlag_true("filter");

                    String value = s.get(what);
                    String[] args = {value};
                    database.delete("one", columnName+"!=?", args);
                    //将筛选点击的值和列ID发给getTableone
                    Message msg = Message.obtain();
                    msg.what = 666;
                    msg.obj = value;
                    msg.arg1 = Integer.parseInt(colid);
                    handler.sendMessage(msg);


                    mListPopup.dismiss();
                }
            });

            mListPopup.showPopupWindow();
        }
    }
}


