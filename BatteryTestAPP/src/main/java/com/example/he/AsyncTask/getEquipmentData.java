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
 * Created by HE on 2016/6/5.
 * 这是一个从数据库查询设备数据的线程
 */
public class getEquipmentData extends AsyncTask<Object, Object, String>{
    private Map<String,String>tlistItem = new HashMap<>();
    private ListView titlelistview, listView;
    private Context context;
    private String eqmid;

    private MySQLiteOpenHelper helper;
    private SQLiteDatabase database;
    private Cursor cursor;

    public getEquipmentData(Context context, String eqmid, ListView titlelistview, ListView listView){
        this.context = context;
        this.eqmid = eqmid;
        this.titlelistview = titlelistview;
        this.listView = listView;
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
            //此处借用BatteryInfo的适配器
            //列表适配器
            vhAdapter_BatteryInfo_title tsimpleAdapter = new vhAdapter_BatteryInfo_title(context,tlistItem,7);
            titlelistview.setAdapter(tsimpleAdapter);

            //注意与标题的适配器不同
            vhAdapter_BatteryInfo adapter = new vhAdapter_BatteryInfo();
            adapter.initadapter(context,7, listView,cursor);
            listView.setAdapter(adapter);
        }else {
            Toast.makeText(context, "载入失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected String doInBackground(Object... params) {

        //列名
        tlistItem.put("0","Channel");
        tlistItem.put("1","Status");
        tlistItem.put("2","Battery_NO");
        tlistItem.put("3","Temperature");
        tlistItem.put("4","C_rate");
        tlistItem.put("5","Section");
        tlistItem.put("6","City");

        try{
            cursor = database.rawQuery("select Channel,Status,Battery_NO,Temperature,C_rate," +
                    "Section,City from Equipment where [EquipmentID] ='" + eqmid + "'",null);
        }catch (SQLException e){
            return "fail";
        }
        return "success";
    }
}
