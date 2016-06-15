package com.example.he.NetAsyncTask;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.example.he.Database.MySQLiteOpenHelper;
import com.example.he.ListviewAdapter.vhAdapter_Equipment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HE on 2016/3/10.
 * 这是一个从数据库查询实验列表,通道情况的线程
 */
public class getEquipment extends AsyncTask<Void, Void, String>{
    private Context context;
    private ListView listView;
    private String city;
    private MySQLiteOpenHelper helper;
    private SQLiteDatabase database;
    private List<Map<Integer,String>> listItems = new ArrayList();

    public getEquipment(Context context, ListView listView, String city) {
        this.context = context;
        this.listView = listView;
        this.city = city;
    }

    @Override
    protected void onPreExecute() {
        helper = MySQLiteOpenHelper.getInstance(context);
        database = helper.getReadableDatabase();
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("success")) {
            vhAdapter_Equipment simpleAdapter = new vhAdapter_Equipment(context, listItems);
            listView.setAdapter(simpleAdapter);
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        try{
            Cursor cursor = database.rawQuery("select distinct EquipmentID from Equipment " +
                    "where City = '" + city + "'",null);
            cursor.moveToFirst();
            ArrayList<String> eqmids= new ArrayList<>();
            while(!cursor.isAfterLast()){
                eqmids.add(cursor.getString(0));
                cursor.moveToNext();
            }
            for(int i= 0;i<eqmids.size();i++){
                Map<Integer,String> listItem = new HashMap<>();
                cursor = database.rawQuery("select distinct Equipment from Equipment " +
                        "where EquipmentID ='" + eqmids.get(i) + "'",null);
                cursor.moveToFirst();
                listItem.put(0,cursor.getString(0));
                listItem.put(1,eqmids.get(i));

                cursor = database.rawQuery("select Status from Equipment where EquipmentID ='"
                        + eqmids.get(i) + "'",null);
                cursor.moveToFirst();
                int count = 2;
                while(!cursor.isAfterLast()){
                    listItem.put(count++,cursor.getString(0));
                    cursor.moveToNext();
                }
                listItems.add(listItem);
            }
        }catch (SQLException e){
            return "fail";
        }
        return "success";
    }
}

