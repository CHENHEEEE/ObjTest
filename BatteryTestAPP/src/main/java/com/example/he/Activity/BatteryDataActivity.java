package com.example.he.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.he.NetAsyncTask.getBatteryData;
import com.example.he.Scrollview.HVscroll;
import com.example.he.Scrollview.ScrollViewListener;
import com.example.he.batteryinfoActivity.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BatteryDataActivity extends Activity implements ScrollViewListener{

    private TextView textViewBack,textViewTop;
    private HVscroll mtitlehvscroll,mhvscroll;
    private ListView mtitlelistview,mlistview;
    private ImageView imgView;
    private String expid,batteryid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_table);

        inite();

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("载入中…");
        pd.setIndeterminate(false);// 在最大值最小值中移动
        pd.setCancelable(false);// 不可以取消
        pd.show();

        //联网获取表格数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new getBatteryData(BatteryDataActivity.this,expid,batteryid,mtitlelistview,mlistview).execute();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }
        }).start();

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

    private void inite(){
        textViewBack = (TextView) findViewById(R.id.subtopBack);
        textViewTop = (TextView) findViewById(R.id.subtopTextView);
        imgView = (ImageView) findViewById(R.id.imageView);
        mtitlehvscroll = (HVscroll) findViewById(R.id.titlehvscroll);
        mhvscroll = (HVscroll) findViewById(R.id.hvscroll);
        mtitlelistview = (ListView) findViewById(R.id.tlistview);
        mlistview = (ListView) findViewById(R.id.mlistView);

        expid = getIntent().getExtras().getString("expid");
        batteryid = getIntent().getExtras().getString("batteryid");

        //设置标题（电池ID）
        textViewTop.setText(batteryid);

        //标题栏和内容设置监听，同步滚动
        mhvscroll.setScrollViewListener(this);
        mtitlehvscroll.setScrollViewListener(this);

        //左上角返回键监听
        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BatteryDataActivity.this.finish();
            }
        });

        //右上角图标监听
        imgView.setVisibility(View.VISIBLE);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("expid",expid);
                intent.putExtra("batteryid",batteryid);
                intent.setClass(BatteryDataActivity.this,LineChartActivity.class);
                startActivity(intent);
            }
        });
    }

}
