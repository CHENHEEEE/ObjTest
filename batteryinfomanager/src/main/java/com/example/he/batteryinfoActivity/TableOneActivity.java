package com.example.he.batteryinfoActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.he.NetAsyncTask.getTableOne;
import com.example.he.scrollview.HVscroll;
import com.example.he.scrollview.ScrollViewListener;

public class TableOneActivity extends Activity implements ScrollViewListener{

    private TextView textViewBack,textViewTop;
    private HVscroll mtitlehvscroll,mhvscroll;
    private ListView mtitlelistview,mlistview;

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
        final String expname = getIntent().getExtras().getString("expname");
        final String expid = getIntent().getExtras().getString("expid");

        //设置标题
        textViewTop.setText("电池信息");

        //标题栏和内容设置监听，同步滚动
        mhvscroll.setScrollViewListener(this);
        mtitlehvscroll.setScrollViewListener(this);

        //左上角返回键监听
        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableOneActivity.this.finish();
            }
        });

        //联网获取表格数据
        new getTableOne(TableOneActivity.this,expid,mtitlelistview,mlistview).execute();

        //一级表点击监听
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView bIDtextview = (TextView)mlistview.getChildAt(position).findViewById(R.id.t1);
                String batteryId = bIDtextview.getText().toString();
                Log.d("HeD-batteryID",batteryId);
                Intent intent = new Intent();
                intent.putExtra("expid",expid);
                intent.putExtra("expname",expname);
                intent.putExtra("id", batteryId);
                intent.setClass(TableOneActivity.this, TableTwoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.to_right, R.animator.to_left);
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
}
