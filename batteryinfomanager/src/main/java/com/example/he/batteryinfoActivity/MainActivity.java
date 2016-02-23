package com.example.he.batteryinfoActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.he.NetAsyncTask.getEqmSta;
import com.example.he.NetAsyncTask.getExp;
import com.example.he.NetAsyncTask.updateEqmSta;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements android.view.View.OnClickListener{

    private ViewPager mViewpager;
    private PagerAdapter mAdapter;
    private List<View> mViews=new ArrayList<View>();
    //TAB
    private LinearLayout mTabPrj, mTabEqm;
    private ImageButton mPrjImg, mEqmImg;
    private TextView topTextView,gzTextView,dgTextView,
            textViewGZ_c,textViewGZ_t,textViewDG_c,textViewDG_t;
    private ListView expListView;
    private ImageView imgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        topTextView = (TextView)findViewById(R.id.topTextView);
        imgView = (ImageView)findViewById(R.id.imageView);

        initView();

        initEvents();
        
    }

    private void initEvents() {
        mTabPrj.setOnClickListener(this);
        mTabEqm.setOnClickListener(this);
        imgView.setOnClickListener(this);

        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                int currentItem = mViewpager.getCurrentItem();
                switch (currentItem) {
                    case 0:
                        resetImg();
                        mPrjImg.setImageResource(R.drawable.prj_pressed);
                        topTextView.setText("实验项目");
                        imgView.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        resetImg();
                        mEqmImg.setImageResource(R.drawable.eqm_pressed);
                        topTextView.setText("设备");
                        imgView.setVisibility(View.VISIBLE);
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
        gzTextView = (TextView) tab01.findViewById(R.id.GZtextView);
        dgTextView = (TextView) tab01.findViewById(R.id.DGtextView);
        gzTextView.setOnClickListener(this);
        dgTextView.setOnClickListener(this);

        expListView = (ListView) tab01.findViewById(R.id.explistView);
        expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取点击item的电池实验id，名字
                TextView idtextview = (TextView)expListView.getChildAt(position).findViewById(R.id.Expid);
                TextView nametextview = (TextView)expListView.getChildAt(position).findViewById(R.id.Expname);
                String expid = idtextview.getText().toString();
                String name = nametextview.getText().toString();
                Log.d("expid", expid);
                Intent intent = new Intent();
                intent.putExtra("expid",expid);
                intent.putExtra("expname",name);
                intent.setClass(MainActivity.this, TableOneActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.to_right,R.animator.to_left);
            }
        });

        //tab02内容布局
        textViewGZ_c = (TextView) tab02.findViewById(R.id.textViewGZ_c);
        textViewGZ_t = (TextView) tab02.findViewById(R.id.textViewGZ_t);
        textViewDG_c = (TextView) tab02.findViewById(R.id.textViewDG_c);
        textViewDG_t = (TextView) tab02.findViewById(R.id.textViewDG_t);

        //状态点击监听
        textViewGZ_c.setOnClickListener(this);
        textViewDG_c.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.id_tab_prj:
                resetImg();
                mViewpager.setCurrentItem(0);
                mPrjImg.setImageResource(R.drawable.prj_pressed);
                topTextView.setText("实验项目");
                imgView.setVisibility(View.INVISIBLE);
                break;

            case R.id.id_tab_eqm:
                resetImg();
                mViewpager.setCurrentItem(1);
                mEqmImg.setImageResource(R.drawable.eqm_pressed);
                topTextView.setText("设备");
                imgView.setVisibility(View.VISIBLE);
                break;

            case R.id.imageView:
                new getEqmSta(MainActivity.this, "1", textViewGZ_c, textViewGZ_t).execute();
                new getEqmSta(MainActivity.this, "2", textViewDG_c, textViewDG_t).execute();
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

            //广州设备状态点击更新
            case R.id.textViewGZ_c:
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_update);
                dialog.setTitle("更新设备状态");
                Window dialogWindow = dialog.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                dialogWindow.setGravity(Gravity.CENTER);
                dialogWindow.setAttributes(lp);

                Button btnConfirm = (Button) dialog.findViewById(R.id.buttonConfirm);
                Button btnCancel = (Button) dialog.findViewById(R.id.buttonCancel);

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //设备状态更新
                        EditText editText = (EditText) dialog.findViewById(R.id.editTextSta);
                        final String status = editText.getText().toString();
                        new updateEqmSta(MainActivity.this, "1", status).execute();
                        dialog.dismiss();
                        new getEqmSta(MainActivity.this, "1", textViewGZ_c, textViewGZ_t).execute();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;

            //东莞设备状态点击更新
            case R.id.textViewDG_c:
                final Dialog dialog1 = new Dialog(MainActivity.this);
                dialog1.setContentView(R.layout.dialog_update);
                dialog1.setTitle("更新设备状态");
                Window dialogWindow1 = dialog1.getWindow();
                WindowManager.LayoutParams lp1 = dialogWindow1.getAttributes();
                dialogWindow1.setGravity(Gravity.CENTER);
                dialogWindow1.setAttributes(lp1);

                Button btnConfirm1 = (Button) dialog1.findViewById(R.id.buttonConfirm);
                Button btnCancel1 = (Button) dialog1.findViewById(R.id.buttonCancel);

                btnConfirm1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //设备状态更新
                        EditText editText1 = (EditText) dialog1.findViewById(R.id.editTextSta);
                        final String status1 = editText1.getText().toString();
                        new updateEqmSta(MainActivity.this, "2", status1).execute();
                        dialog1.dismiss();
                        new getEqmSta(MainActivity.this, "2", textViewDG_c, textViewDG_t).execute();
                    }
                });

                btnCancel1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
                break;
        }

    }

    private void resetImg() {
        mPrjImg.setImageResource(R.drawable.prj);
        mEqmImg.setImageResource(R.drawable.eqm);
    }


}
