package com.example.he.NetAsyncTask;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.he.Database.MySQLiteOpenHelper;
import com.example.he.ListviewAdapter.vhAdapter_BatteryData;
import com.example.he.batteryinfoActivity.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HE on 2016/1/25.
 * 这是一个查询数据库中电池数据的线程
 */
public class getBatteryData extends AsyncTask<Object, Object, String>{
    private List<Map<String,String>> tlistItems = new ArrayList<>();
    private ListView titlelistview, listView;
    private Context context;
    private String testname, batteryid;
    private Cursor cursor;
    private MySQLiteOpenHelper helper;
    private SQLiteDatabase database;

    public getBatteryData(Context context, String Expid, String batteryid, ListView titleListview, ListView listView){
        this.context = context;
        this.testname = "TestID_" + Expid;
        this.batteryid = batteryid;
        this.titlelistview = titleListview;
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
            //列表适配器
            SimpleAdapter tsimpleAdapter = new SimpleAdapter(context, tlistItems, R.layout.tabletitle_item_bdata,
                    new String[] {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14"},
                    new int[]{R.id.t0,R.id.t1,R.id.t2,R.id.t3,R.id.t4,R.id.t5,R.id.t6,R.id.t7,R.id.t8,R.id.t9,R.id.t10});
            titlelistview.setAdapter(tsimpleAdapter);

            vhAdapter_BatteryData adapter = new vhAdapter_BatteryData(context,cursor);
            listView.setAdapter(adapter);

            Log.d("HE-D-start",String.valueOf(System.currentTimeMillis()));

        }else {
            Toast.makeText(context, "载入失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected String doInBackground(Object... params) {
        Map<String,String> tlistItem = new HashMap<>();
        tlistItem.put("0","ID");
        tlistItem.put("1","Test_NO");
        tlistItem.put("2","Battery_N0");
        tlistItem.put("3","Cycle_Times");
        tlistItem.put("4","Capacity_amended");
        tlistItem.put("5","Degradion_amended");
        tlistItem.put("6","Degradation--mAh");
        tlistItem.put("7","Test_Date");
        tlistItem.put("8","Accumulate_Capacity");
        tlistItem.put("9","Degradation");
        tlistItem.put("10","Capacity");
        tlistItems.add(tlistItem);

        try {
            cursor = database.rawQuery("select * from "+ testname +" where [Battery_NO] ='" + batteryid
                    + "' order by Cycle_Times",null);
        } catch (SQLException e) {
            return "fail";
        }
        return "success";
    }
}

