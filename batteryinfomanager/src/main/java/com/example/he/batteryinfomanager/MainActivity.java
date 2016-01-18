package com.example.he.batteryinfomanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.he.NetAsyncTask.getExp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements android.view.View.OnClickListener{

    private ViewPager mViewpager;
    private PagerAdapter mAdapter;
    private List<View> mViews=new ArrayList<View>();
    //TAB
    private LinearLayout mTabPrj, mTabEqm;
    private ImageButton mPrjImg, mEqmImg;
    private TextView topTextView,gzTextView,dgTextView;
    private ListView expListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        topTextView = (TextView)findViewById(R.id.topTextView);

        initView();

        initEvents();
        
    }

    private void initEvents() {
        mTabPrj.setOnClickListener(this);
        mTabEqm.setOnClickListener(this);

        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                int currentItem=mViewpager.getCurrentItem();
                switch (currentItem) {
                    case 0:
                        resetImg();
                        mPrjImg.setImageResource(R.drawable.prj_pressed);
                        topTextView.setText("实验项目");
                        break;
                    case 1:
                        resetImg();
                        mEqmImg.setImageResource(R.drawable.eqm_pressed);
                        topTextView.setText("设备");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }


    private void initView() {
        mViewpager = (ViewPager)findViewById(R.id.id_viewpager);
        //Tabs
        mTabPrj =(LinearLayout)findViewById(R.id.id_tab_prj);
        mTabEqm =(LinearLayout)findViewById(R.id.id_tab_eqm);
        //ImageButtons
        mPrjImg =(ImageButton)findViewById(R.id.id_tab_prj_img);
        mEqmImg =(ImageButton)findViewById(R.id.id_tab_eqm_img);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View tab01=mInflater.inflate(R.layout.tab01, null);
        View tab02=mInflater.inflate(R.layout.tab02, null);
        mViews.add(tab01);
        mViews.add(tab02);

        mAdapter = new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mViews.get(position));
                super.destroyItem(container, position, object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view=mViews.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0==arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return mViews.size();
            }
        };

        mViewpager.setAdapter(mAdapter);

        //内容布局
        gzTextView = (TextView) tab01.findViewById(R.id.GZtextView);
        dgTextView = (TextView) tab01.findViewById(R.id.DGtextView);
        gzTextView.setOnClickListener(this);
        dgTextView.setOnClickListener(this);

        expListView = (ListView) tab01.findViewById(R.id.explistView);
        expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idtextview = (TextView)expListView.getChildAt(position).findViewById(R.id.Expid);
                TextView nametextview = (TextView)expListView.getChildAt(position).findViewById(R.id.Expname);
                String expid = idtextview.getText().toString();
                String name = nametextview.getText().toString();
                Log.d("expid",expid);
                Intent intent = new Intent();
                intent.putExtra("expid",expid);
                intent.putExtra("name",name);
                intent.setClass(MainActivity.this, TableListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.to_right,R.animator.to_left);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.id_tab_prj:
                resetImg();
                mViewpager.setCurrentItem(0);
                mPrjImg.setImageResource(R.drawable.prj_pressed);
                topTextView.setText("实验项目");
                break;

            case R.id.id_tab_eqm:
                resetImg();
                mViewpager.setCurrentItem(1);
                mEqmImg.setImageResource(R.drawable.eqm_pressed);
                topTextView.setText("设备");
                break;

            case R.id.GZtextView:
                gzTextView.setBackgroundResource(R.drawable.boder_tab);
                gzTextView.setTextColor(Color.parseColor("#FD505A"));
                dgTextView.setBackgroundResource(R.drawable.boder);
                dgTextView.setTextColor(Color.BLACK);
                new getExp(MainActivity.this,"GZ",expListView).execute();
                break;

            case R.id.DGtextView:
                dgTextView.setBackgroundResource(R.drawable.boder_tab);
                dgTextView.setTextColor(Color.parseColor("#FD505A"));
                gzTextView.setBackgroundResource(R.drawable.boder);
                gzTextView.setTextColor(Color.BLACK);
                new getExp(MainActivity.this,"DG",expListView).execute();
                break;
        }

    }

    private void resetImg() {
        mPrjImg.setImageResource(R.drawable.prj);
        mEqmImg.setImageResource(R.drawable.eqm);
    }


}
