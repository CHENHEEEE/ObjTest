package com.example.he.batteryinfoActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.he.NetAsyncTask.getTableTwo;
import com.example.he.scrollview.HVscroll;
import com.example.he.scrollview.ScrollViewListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableTwoActivity extends Activity implements ScrollViewListener{

    private TextView textViewBack,textViewTop;
    private HVscroll mtitlehvscroll,mhvscroll;
    private ListView mtitlelistview,mlistview;
    private List<Map<String,String>> listItems = new ArrayList<Map<String,String>>();
    private int x,y;//x,y 表示XY轴数据源在list中的下标
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_table);
        textViewBack = (TextView) findViewById(R.id.subtopBack);
        textViewTop = (TextView) findViewById(R.id.subtopTextView);
        imgView = (ImageView) findViewById(R.id.imageView);
        mtitlehvscroll = (HVscroll) findViewById(R.id.titlehvscroll);
        mhvscroll = (HVscroll) findViewById(R.id.hvscroll);
        mtitlelistview = (ListView) findViewById(R.id.tlistview);
        mlistview = (ListView) findViewById(R.id.mlistView);
        final String expname = getIntent().getExtras().getString("expname");
        final String expid = getIntent().getExtras().getString("expid");
        final String id = getIntent().getExtras().getString("id");

        //设置标题（电池ID）
        textViewTop.setText(id);

        //标题栏和内容设置监听，同步滚动
        mhvscroll.setScrollViewListener(this);
        mtitlehvscroll.setScrollViewListener(this);

        //左上角返回键监听
        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableTwoActivity.this.finish();
            }
        });

        //右上角图标监听
        imgView.setVisibility(View.VISIBLE);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("list",(Serializable)listItems);
                intent.putExtra("x",x);
                intent.putExtra("y",y);
                intent.setClass(TableTwoActivity.this,LineChartActivity.class);
                startActivity(intent);
            }
        });

        Mhandler mhandler = new Mhandler();

        //联网获取表格数据
        new getTableTwo(TableTwoActivity.this,expid,id,mtitlelistview,mlistview,mhandler).execute();

//        //二级表点击监听
//        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                TextView bIDtextview = (TextView)mlistview.getChildAt(position).findViewById(R.id.t1);
////                String batteryId = bIDtextview.getText().toString();
////                Log.d("HeD-batteryID",batteryId);
//                Intent intent = new Intent();
//                intent.putExtra("list",(Serializable)listItems);
//                intent.putExtra("x",x);
//                intent.putExtra("y",y);
//                intent.setClass(TableTwoActivity.this,LineChartActivity.class);
//                startActivity(intent);
//            }
//        });
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

    class  Mhandler extends Handler{
        public Mhandler(){}

        public Mhandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //接收消息
            if (msg.what == 999 && msg.obj != null)
            {
                listItems = (List<Map<String,String>>) msg.obj;
                x = msg.arg1;
                y = msg.arg2;
            }
        }
    }

}
