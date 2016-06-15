package com.example.he.Activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.he.Database.MySQLiteOpenHelper;
import com.example.he.MyTools.MyValueFormatter;
import com.example.he.batteryinfoActivity.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class LineChartActivity extends Activity {
    private String tablename,batteryid;
    private MySQLiteOpenHelper helper;
    private SQLiteDatabase database;
    private Cursor cursor;
    private LineChart mlinechart1;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_line_chart);
        tablename = "TestID_" + getIntent().getStringExtra("expid");
        batteryid = getIntent().getStringExtra("batteryid");

        //折线图1
        mlinechart1 = (LineChart) findViewById(R.id.line_chart_1);
        setLineChart(mlinechart1);
        loadLineChartData(mlinechart1);

        button = (Button) findViewById(R.id.button_reset);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlinechart1.fitScreen();
            }
        });
    }

    /**
     * 设置折现图的样式
     * @param chart
     */
    private void setLineChart(LineChart chart) {

        chart.setDescription("Cycle_Times - Degradion_amended");
        chart.setDrawGridBackground(false);//设置网格背景
        chart.setScaleEnabled(true);//设置缩放
        chart.setDoubleTapToZoomEnabled(false);//设置双击不进行缩放
        chart.enableScroll();

        //设置X轴
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴的位置
//        xAxis.setTypeface(mTf);//设置字体
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);


        //获得左侧侧坐标轴
        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5, false);
        leftAxis.setValueFormatter(new MyValueFormatter());
        leftAxis.setDrawGridLines(false);
//        leftAxis.setAxisLineWidth(1.5f);


        //设置右侧坐标轴
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawAxisLine(false);//右侧坐标轴线
        rightAxis.setDrawLabels(false);//右侧坐标轴数组Lable
//        rightAxis.setTypeface(mTf);
//        rightAxis.setLabelCount(5);
//        rightAxis.setDrawGridLines(false);
    }

    /**
     * 为折线图设置数据
     * @param chart
     */
    private void loadLineChartData(LineChart chart){
        //所有线的List
        ArrayList<ILineDataSet> allLinesList = new ArrayList<>();
        ArrayList<Entry> entryList = new ArrayList<>();
        helper = MySQLiteOpenHelper.getInstance(LineChartActivity.this);
        database = helper.getReadableDatabase();

        String sql = "select Cycle_Times,Degradion_amended from " + tablename + " where Battery_NO = '"
                + batteryid + "' order by Cycle_Times";
        cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        try {
            while (!cursor.isAfterLast()){
                entryList.add(new Entry(Float.parseFloat(cursor.getString(1))
                        ,Integer.parseInt(cursor.getString(0))));
                cursor.moveToNext();
            }
        }catch (Exception e){
            Toast.makeText(this,"存在数据不完整",Toast.LENGTH_SHORT).show();
        }
        //LineDataSet可以看做是一条线
        LineDataSet dataSet1 = new LineDataSet(entryList,batteryid);
        dataSet1.setLineWidth(2.5f);
        dataSet1.setCircleRadius(2.5f);
        //dataSet1.setHighLightColor(Color.RED);//设置点击某个点时，横竖两条线的颜色
        dataSet1.setDrawValues(false);//是否在点上绘制Value
        dataSet1.setColor(Color.rgb(253,80,90));
        dataSet1.setCircleColor(Color.rgb(253,80,90));

        allLinesList.add(dataSet1);

        //LineData表示一个LineChart的所有数据(即一个LineChart中所有折线的数据)
        LineData mChartData = new LineData(getXAxisShowLable(),allLinesList);

        // set data
        chart.setData((LineData) mChartData);
        chart.animateX(1500);//设置动画
    }

    private ArrayList<String> getXAxisShowLable() {
        ArrayList<String> m = new ArrayList<String>();
        cursor.moveToLast();
        if (!cursor.isAfterLast()){
            long max = Integer.parseInt(cursor.getString(0));
            for (int i=0;i<=max;i++){
                m.add(String.valueOf(i));
            }
        }
        return m;
    }
}
