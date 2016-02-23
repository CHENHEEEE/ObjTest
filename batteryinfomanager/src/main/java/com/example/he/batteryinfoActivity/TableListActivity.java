package com.example.he.batteryinfoActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.he.NetAsyncTask.getTableList;

public class TableListActivity extends Activity {

    private TextView textViewBack,textViewTop;
    private String expid;
    private ListView tablelistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tablelist);

        textViewBack = (TextView) findViewById(R.id.subtopBack);
        textViewTop = (TextView) findViewById(R.id.subtopTextView);
        tablelistview = (ListView) findViewById(R.id.tablelistView);

        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableListActivity.this.finish();
            }
        });
        final String expname = getIntent().getExtras().getString("name");
        textViewTop.setText(expname);

        expid = getIntent().getExtras().getString("expid");
        new getTableList(TableListActivity.this,expid,tablelistview).execute();

        tablelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idtextview = (TextView)tablelistview.getChildAt(position).findViewById(R.id.Expid);
                String Tid = idtextview.getText().toString();
                TextView tabletextview = (TextView)tablelistview.getChildAt(position).findViewById(R.id.Expname);
                String tablename = tabletextview.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("tablename",tablename);
                intent.putExtra("tid",Tid);
                intent.setClass(TableListActivity.this, TableActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.to_right, R.animator.to_left);
            }
        });

    }
}
