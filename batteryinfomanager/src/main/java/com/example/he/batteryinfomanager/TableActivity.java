package com.example.he.batteryinfomanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.example.he.NetAsyncTask.getTable;
import com.example.he.scrollview.HVscroll;
import com.example.he.scrollview.ScrollViewListener;

public class TableActivity extends Activity implements ScrollViewListener{

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
        final String tablename = getIntent().getExtras().getString("tablename");
        final String tid = getIntent().getExtras().getString("tid");

        mhvscroll.setScrollViewListener(this);
        mtitlehvscroll.setScrollViewListener(this);

        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableActivity.this.finish();
            }
        });
        textViewTop.setText(tablename);

        new getTable(TableActivity.this,tid,tablename,mtitlelistview,mlistview).execute();

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
