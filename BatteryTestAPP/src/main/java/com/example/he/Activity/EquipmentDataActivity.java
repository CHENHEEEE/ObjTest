package com.example.he.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he.Database.MySQLiteOpenHelper;
import com.example.he.AsyncTask.getEquipmentData;
import com.example.he.Scrollview.HVscroll;
import com.example.he.Scrollview.ScrollViewListener;
import com.example.he.batteryinfoActivity.R;

public class EquipmentDataActivity extends Activity implements ScrollViewListener {

    private TextView textViewBack,textViewTop;
    private HVscroll mtitlehvscroll,mhvscroll;
    private ListView mtitlelistview,mlistview;
    private String eqmid,eqmname,batteryid,testid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_table);
        textViewBack = (TextView) findViewById(R.id.subtopBack);
        textViewTop = (TextView) findViewById(R.id.subtopTextView);
        mtitlehvscroll = (HVscroll) findViewById(R.id.titlehvscroll);
        mhvscroll = (HVscroll) findViewById(R.id.hvscroll);
        mtitlelistview = (ListView) findViewById(R.id.tlistview);
        mlistview = (ListView) findViewById(R.id.mlistView);

        eqmid = getIntent().getExtras().getString("eqmid");
        eqmname = getIntent().getExtras().getString("eqmname");

        //设置标题
        textViewTop.setText(eqmname);

        //标题栏和内容设置监听，同步滚动
        mhvscroll.setScrollViewListener(this);
        mtitlehvscroll.setScrollViewListener(this);

        //左上角返回键监听
        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EquipmentDataActivity.this.finish();
            }
        });

        //设备数据加载
        new getEquipmentData(EquipmentDataActivity.this,eqmid,mtitlelistview,mlistview).execute();

        //listview点击监听
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textview = (TextView) mlistview.getChildAt(position).findViewById(R.id.t2);
                String bid = textview.getText().toString();
                Intent intent = new Intent();
                getbattery(bid);
                if(testid!= null) {
                    intent.putExtra("batteryid", batteryid);
                    intent.putExtra("expid",testid);
                    intent.setClass(EquipmentDataActivity.this, BatteryDataActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.animator.to_right, R.animator.to_left);
                }else Toast.makeText(EquipmentDataActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onScrollChanged(HVscroll hVscroll, int x, int y, int oldx, int oldy) {
        if (hVscroll == mhvscroll) {
            mtitlehvscroll.scrollTo(x, y);
        }
        if (hVscroll == mtitlehvscroll) {
            y = mhvscroll.getScrollY();
            mhvscroll.scrollTo(x, y);
        }
    }

    private void getbattery(String id){
        MySQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(EquipmentDataActivity.this);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select Battery_NO,TestID from BatteryInfo where " +
                "upper(Battery_NO) = upper('" + id +"')",null);
        cursor.moveToFirst();
        if(cursor.getCount()!= 0) {
            batteryid = cursor.getString(0);
            testid = cursor.getString(1);
        }else testid =null;
    }
}
