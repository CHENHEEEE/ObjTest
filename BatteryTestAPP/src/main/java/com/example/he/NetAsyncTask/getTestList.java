package com.example.he.NetAsyncTask;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.example.he.Database.MySQLiteOpenHelper;
import com.example.he.PullToRefresh.PullToRefreshLayout;
import com.example.he.PullToRefresh.PullableListView;
import com.example.he.batteryinfoActivity.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HE on 2016/3/10.
 * 这是一个从数据库查询实验列表的线程
 */
public class getTestList extends AsyncTask<Void, Void, String>{
    private Context context;
    private PullToRefreshLayout layout;
    private PullableListView listView;
    private String sql;
    private MySQLiteOpenHelper helper;
    private SQLiteDatabase database;
    private List<Map<String,String>> listItems = new ArrayList();

    public getTestList(Context context, PullToRefreshLayout layout, PullableListView listView, String sql) {
        this.context = context;
        this.layout = layout;
        this.listView = listView;
        this.sql = sql;
        Log.d("HE-D-pull-construct", String.valueOf(System.currentTimeMillis()));
    }

    @Override
    protected void onPreExecute() {
        Log.d("HE-D-pull-onPrestart", String.valueOf(System.currentTimeMillis()));
        helper = MySQLiteOpenHelper.getInstance(context);
        database = helper.getReadableDatabase();
        Log.d("HE-D-pull-onPreend", String.valueOf(System.currentTimeMillis()));
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("success")){
            SimpleAdapter simpleAdapter = new SimpleAdapter(context, listItems, R.layout.name_item_new,
                    new String[] {"Tname","Sname","id"}, new int[]{R.id.Expname,R.id.Sname,R.id.Expid});
            listView.setAdapter(simpleAdapter);
            Log.d("HE-D-pull-finish", String.valueOf(System.currentTimeMillis()));
            layout.refreshFinish(PullToRefreshLayout.SUCCEED);
        }else layout.refreshFinish(PullToRefreshLayout.FAIL);
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.d("HE-D-pull-dobackstart", String.valueOf(System.currentTimeMillis()));
        try{
            Cursor cursor = database.rawQuery(sql,null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Map<String,String> listItem = new HashMap<String, String>();
                listItem.put("id", cursor.getString(0));
                listItem.put("Tname", cursor.getString(1));
                listItem.put("Sname", "负责人："+cursor.getString(2));
                listItem.put("Type", cursor.getString(3));
                listItems.add(listItem);
                cursor.moveToNext();
            }
        }catch (SQLException e){
            return "fail";
        }
        return "success";
    }
}

