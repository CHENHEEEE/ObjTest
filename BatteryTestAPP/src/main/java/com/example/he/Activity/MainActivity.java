package com.example.he.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he.MyTools.ToggleButtonGroupTableLayout;
import com.example.he.NetAsyncTask.checkVersion;
import com.example.he.NetAsyncTask.getEquipment;
import com.example.he.NetAsyncTask.getTestList;
import com.example.he.NetAsyncTask.loadData;
import com.example.he.PullToRefresh.PullToRefreshLayout;
import com.example.he.PullToRefresh.PullableListView;
import com.example.he.batteryinfoActivity.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements android.view.View.OnClickListener,
        AdapterView.OnItemClickListener{

    private ViewPager mViewpager;
    private PagerAdapter mAdapter;
    private List<View> mViews=new ArrayList<View>();
    //TAB
    private LinearLayout mTabPrj, mTabEqm;
    private ImageButton mPrjImg, mEqmImg;
    private TextView topTextView, textViewGZ_t,textViewDG_t;
    private ImageView imgView_refresh,imageView_download;
    private ToggleButtonGroupTableLayout RG_brand,RG_city,RG_type;
    private PullToRefreshLayout pulllayout;
    private PullableListView listView;
    private RadioButton rb_qd,rb_ls,rb_ld,rb_zh;
    private ListView listView_GZ,listView_DG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        topTextView = (TextView)findViewById(R.id.topTextView);
        imgView_refresh = (ImageView)findViewById(R.id.imageView_refresh);
        imageView_download = (ImageView) findViewById(R.id.imageView_download);

        initView();

        initEvents();

        new checkVersion(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new checkVersion(MainActivity.this).execute();
    }

    private void initEvents() {
        mTabPrj.setOnClickListener(this);
        mTabEqm.setOnClickListener(this);
        imgView_refresh.setOnClickListener(this);
        imageView_download.setOnClickListener(this);

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                int currentItem = mViewpager.getCurrentItem();
                switch (currentItem) {
                    case 0:
                        resetImg();
                        mPrjImg.setBackgroundResource(R.drawable.ic_test_press);
                        topTextView.setText("实验项目");
                        imgView_refresh.setVisibility(View.INVISIBLE);
                        imageView_download.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        resetImg();
                        mEqmImg.setBackgroundResource(R.drawable.ic_eqm_press);
                        topTextView.setText("设备");
                        imgView_refresh.setVisibility(View.VISIBLE);
                        imageView_download.setVisibility(View.INVISIBLE);
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

        //tab01内容布局
        RG_brand = (ToggleButtonGroupTableLayout) tab01.findViewById(R.id.RG_brand);
        RG_city = (ToggleButtonGroupTableLayout) tab01.findViewById(R.id.RG_city);
        RG_type = (ToggleButtonGroupTableLayout) tab01.findViewById(R.id.RG_type);
        listView = (PullableListView) tab01.findViewById(R.id.refresh_listview);
        pulllayout = (PullToRefreshLayout) tab01.findViewById(R.id.refresh_layout);
        rb_qd = (RadioButton) tab01.findViewById(R.id.radio_qd);
        rb_ls = (RadioButton) tab01.findViewById(R.id.radio_ls);
        rb_ld = (RadioButton) tab01.findViewById(R.id.radio_ld);
        rb_zh = (RadioButton) tab01.findViewById(R.id.radio_zh);

        //radiobutton的图标指定大小
        Drawable drawable = getResources().getDrawable(R.drawable.qiaodeng);
        drawable.setBounds(0,0,120,60);
        rb_qd.setCompoundDrawables(null,drawable,null,null);
        drawable = getResources().getDrawable(R.drawable.lishen);
        drawable.setBounds(0,0,120,60);
        rb_ls.setCompoundDrawables(null,drawable,null,null);
        drawable = getResources().getDrawable(R.drawable.lidong);
        drawable.setBounds(0,0,120,60);
        rb_ld.setCompoundDrawables(null,drawable,null,null);
        drawable = getResources().getDrawable(R.drawable.zhonghang);
        drawable.setBounds(0,0,120,60);
        rb_zh.setCompoundDrawables(null,drawable,null,null);

        pulllayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                String sql = getSql();
                Log.d("HE-D-pull-start", String.valueOf(System.currentTimeMillis()));
                new getTestList(MainActivity.this,pulllayout,listView,sql).execute();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView= (TextView) view.findViewById(R.id.Expid);
                String expid =  textView.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("Expid",expid);
                intent.setClass(MainActivity.this,BatteryInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.to_right, R.animator.to_left);
            }
        });


        //tab02内容布局
        listView_GZ = (ListView) tab02.findViewById(R.id.listView_GZ);
        listView_DG = (ListView) tab02.findViewById(R.id.listView_DG);
        textViewGZ_t = (TextView) tab02.findViewById(R.id.textViewGZ_t);
        textViewDG_t = (TextView) tab02.findViewById(R.id.textViewDG_t);

        new getEquipment(MainActivity.this,listView_GZ,"广州").execute();
        new getEquipment(MainActivity.this,listView_DG,"东莞").execute();

        listView_DG.setOnItemClickListener(this);
        listView_GZ.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.id_tab_prj:
                resetImg();
                mViewpager.setCurrentItem(0);
                mPrjImg.setBackgroundResource(R.drawable.ic_test_press);
                topTextView.setText("实验项目");
                imgView_refresh.setVisibility(View.INVISIBLE);
                break;

            case R.id.id_tab_eqm:
                resetImg();
                mViewpager.setCurrentItem(1);
                mEqmImg.setBackgroundResource(R.drawable.ic_eqm_press);
                topTextView.setText("设备");
                imgView_refresh.setVisibility(View.VISIBLE);
                break;

            case R.id.imageView_refresh:
                new getEquipment(MainActivity.this,listView_GZ,"广州").execute();
                new getEquipment(MainActivity.this,listView_DG,"东莞").execute();
                Toast.makeText(MainActivity.this,"已刷新",Toast.LENGTH_SHORT).show();
                break;

            case R.id.imageView_download:
                new loadData(MainActivity.this).execute();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView_name = (TextView) view.findViewById(R.id.textView_eqm);
        //TextView textView_name = (TextView) listView_DG.getChildAt(position).findViewById(R.id.textView_eqm);
        TextView textView_id = (TextView) view.findViewById(R.id.textView_eqmid);
        String eqmid = textView_id.getText().toString(),
                eqmname = textView_name.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("eqmid",eqmid);
        intent.putExtra("eqmname",eqmname);
        intent.setClass(MainActivity.this,EquipmentDataActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.to_right, R.animator.to_left);
    }

    private void resetImg() {
        mPrjImg.setBackgroundResource(R.drawable.ic_test);
        mEqmImg.setBackgroundResource(R.drawable.ic_eqm);
    }

    /**
     * 根据radiobutton的选择,在database中查询
     */
    private String getSql(){
        //生成sql语句
        String brand,city,type;
        switch(RG_brand.getCheckedRadioButtonId()){
            case R.id.radio_qd:
                brand = "乔登";
                break;
            case R.id.radio_ls:
                brand = "力神";
                break;
            case R.id.radio_ld:
                brand = "锂动";
                break;
            case R.id.radio_zh:
                brand = "中航";
                break;
            case R.id.radio_other:
                brand = "其他";
                break;
            default:
                brand = null;
        }
        switch(RG_city.getCheckedRadioButtonId()){
            case R.id.radio_GZ:
                city = "广州";
                break;
            case R.id.radio_DG:
                city = "东莞";
                break;
            default:
                city = null;
        }
        switch (RG_type.getCheckedRadioButtonId()){
            case R.id.radio_laohua:
                type = "老化";
                break;
            case R.id.radio_pingce:
                type = "评测";
                break;
            case R.id.radio_texing:
                type = "特性";
                break;
            default:
                type = null;
        }
        Log.d("HE-D",brand+","+city+","+type);
        String sql = "select id,TestName,Sname,TestType from TestList where ";
        if (brand!=null) sql = sql + "BatteryBrand= '" + brand + "' AND ";
        else sql += "BatteryBrand= TestList.BatteryBrand AND ";
        if (city!=null) sql = sql + "City= '" + city + "' AND ";
        else sql += "City= TestList.City AND ";
        if (type!=null) sql = sql + "TestType= '" + type + "'";
        else sql += "TestType= TestList.TestType";

        return sql+= " order by id";
    }
}
