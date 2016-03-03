package com.example.he.batteryinfoActivity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LineChartActivity extends AppCompatActivity {

    private List<Map<String,String>> listItems = new ArrayList<Map<String,String>>();
    private int x,y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        listItems = (List<Map<String,String>>)getIntent().getSerializableExtra("list");
        x = getIntent().getIntExtra("x",0);
        y = getIntent().getIntExtra("y",0);

        if(x !=0 && y !=0){
            //折线图1
            LineChart mlinechart1 = (LineChart) findViewById(R.id.line_chart_1);
            setLineChart(mlinechart1);
            loadLineChartData(mlinechart1);
        }else {
            Toast.makeText(this,"数据不完整",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 为折线图设置数据
     * @param chart
     */
    private void loadLineChartData(LineChart chart){
        //所有线的List
        ArrayList<ILineDataSet> allLinesList = new ArrayList<ILineDataSet>();

        ArrayList<Entry> entryList = new ArrayList<Entry>();
        int count = listItems.size();
        for(int i=0;i<count;i++){
            //Entry(yValue,xIndex);一个Entry表示一个点，第一个参数为y值，第二个为X轴List的角标
            entryList.add(new Entry((float)(Float.valueOf(listItems.get(i).get(String.valueOf(y-1)))),i));
        }
        //LineDataSet可以看做是一条线
        LineDataSet dataSet1 = new LineDataSet(entryList,"dataLine1");
        dataSet1.setLineWidth(2.5f);
        dataSet1.setCircleRadius(4.5f);
        dataSet1.setHighLightColor(Color.RED);//设置点击某个点时，横竖两条线的颜色
        dataSet1.setDrawValues(false);//是否在点上绘制Value

        allLinesList.add(dataSet1);

        //LineData表示一个LineChart的所有数据(即一个LineChart中所有折线的数据)
        LineData mChartData = new LineData(getXAxisShowLable(),allLinesList);


        // set data
        chart.setData((LineData) mChartData);
        chart.animateX(1500);//设置动画
    }


    /**
     * 设置折现图的样式
     * @param chart
     */
    private void setLineChart(LineChart chart) {

        chart.setDescription("Glan");
        chart.setDrawGridBackground(false);//设置网格背景
        chart.setScaleEnabled(false);//设置缩放
        chart.setDoubleTapToZoomEnabled(true);//设置双击不进行缩放

        //设置X轴
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴的位置
//        xAxis.setTypeface(mTf);//设置字体
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        //获得左侧侧坐标轴
        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5,true);
//        leftAxis.setAxisLineWidth(1.5f);

        //设置右侧坐标轴
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawAxisLine(false);//右侧坐标轴线
        rightAxis.setDrawLabels(false);//右侧坐标轴数组Lable
//        rightAxis.setTypeface(mTf);
//        rightAxis.setLabelCount(5);
//        rightAxis.setDrawGridLines(false);
    }

    private ArrayList<String> getXAxisShowLable() {
        ArrayList<String> m = new ArrayList<String>();
        int count = listItems.size();
        for (int i=0;i<count;i++){
            m.add(listItems.get(i).get(String.valueOf(x-1)));
        }
        return m;
    }
}
