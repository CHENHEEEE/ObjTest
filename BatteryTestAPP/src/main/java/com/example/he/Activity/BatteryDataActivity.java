package com.example.he.Activity;
/**
 *电池数据表Activity
 *
 *author 陈鹤
 *created 16/9/13 下午3:16
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.he.AsyncTask.getBatteryData;
import com.example.he.Scrollview.HVscroll;
import com.example.he.Scrollview.ScrollViewListener;
import com.example.he.batteryinfoActivity.R;

import java.lang.ref.WeakReference;

public class BatteryDataActivity extends Activity implements ScrollViewListener{

    private TextView textViewBack,textViewTop;
    private HVscroll mtitlehvscroll,mhvscroll;
    private ListView mtitlelistview,mlistview;
    private ImageView imgView;
    private String expid,batteryid;
    static ProgressDialog pd;
    static MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_table);

        //控件初始化
        inite();

        //载入表格数据
        Log.i("HE-D-getData-begin", String.valueOf(System.currentTimeMillis()));
        new getBatteryData(BatteryDataActivity.this,expid,batteryid,mtitlelistview,
                mlistview,myHandler).execute();
    }

    //监听表头和表内容两个ScrollView,同步滚动
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
        pd = new ProgressDialog(this);
        pd.setMessage("载入中…");
        pd.setIndeterminate(false);// 在最大值最小值中移动
        pd.setCancelable(false);// 不可以取消
        pd.show();

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

        //初始化Handler
        myHandler = new MyHandler(this);
    }

    public static class MyHandler extends Handler {
        private final WeakReference<BatteryDataActivity> mActivity;
        public MyHandler(BatteryDataActivity activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            BatteryDataActivity activity = mActivity.get();
            if (null != activity && msg.what == 0) {
                pd.dismiss();
            }
        }
    }
}
