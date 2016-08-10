package com.example.he.AsyncTask;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.example.he.Database.MySQLiteOpenHelper;
import com.example.he.ListviewAdapter.vhAdapter_BatteryInfo;
import com.example.he.ListviewAdapter.vhAdapter_BatteryInfo_title;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HE on 2016/5/31.
 * 这是一个从数据库查询电池信息的线程
 */
public class getBatteryInfo extends AsyncTask<Object, Object, String>{
    private Map<String,String>tlistItem = new HashMap<>();
    private ListView titlelistview, listView;
    private Context context;
    private String Expid;

    private MySQLiteOpenHelper helper;
    private SQLiteDatabase database;
    private vhAdapter_BatteryInfo Adapter;
    private Cursor cursor;

    public getBatteryInfo(Context context, String Expid, ListView titlelistview, ListView listView,
                          vhAdapter_BatteryInfo Adapter){
        this.context = context;
        this.Expid = Expid;
        this.titlelistview = titlelistview;
        this.listView = listView;
        this.Adapter = Adapter;
    }


    @Override
    protected void onPreExecute() {
        //数据库初始化
        helper = MySQLiteOpenHelper.getInstance(context);
        database = helper.getWritableDatabase();
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("success")) {
            //列表适配器
            vhAdapter_BatteryInfo_title tsimpleAdapter = new vhAdapter_BatteryInfo_title(context,tlistItem,8);
            titlelistview.setAdapter(tsimpleAdapter);

            //注意与标题的适配器不同
            Adapter.initadapter(context,8, listView,cursor);
            listView.setAdapter(Adapter);
        }else {
            Toast.makeText(context, "载入失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected String doInBackground(Object... params) {

        //列名
        tlistItem.put("0","NO");
        tlistItem.put("1","Battery_N0");
        tlistItem.put("2","C_rate");
        tlistItem.put("3","DoD");
        tlistItem.put("4","Section");
        tlistItem.put("5","Temperature");
        tlistItem.put("6","Battery_Type");
        tlistItem.put("7","Stas");

        try{
            cursor = database.rawQuery("select [NO],Battery_NO,C_rate,DoD,Section,Temperature,Battery_Type,Stas " +
                    "from BatteryInfo where [TestID] ='" + Expid+ "'",null);
        }catch (SQLException e){
            return "fail";
        }
        return "success";
    }
}
