package com.example.he.batteryinfoActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he.ListviewAdapter.vhAdapter_cut;
import com.example.he.NetAsyncTask.getTableOne;
import com.example.he.NetAsyncTask.preLoad;
import com.example.he.MyTools.Flags;
import com.example.he.scrollview.HVscroll;
import com.example.he.scrollview.ScrollViewListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TableOneActivity extends Activity implements ScrollViewListener {

    private TextView textViewBack,textViewTop,textViewmulti;
    private HVscroll mtitlehvscroll,mhvscroll;
    private ListView mtitlelistview,mlistview;
    private ModeCallback mcallback;
    private vhAdapter_cut adpter = new vhAdapter_cut();
    private RelativeLayout top;
    private ImageView check;
    private String expname,expid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_table);
        textViewBack = (TextView) findViewById(R.id.subtopBack);
        textViewTop = (TextView) findViewById(R.id.subtopTextView);
        textViewmulti = (TextView) findViewById(R.id.subtopmulti);
        mtitlehvscroll = (HVscroll) findViewById(R.id.titlehvscroll);
        mhvscroll = (HVscroll) findViewById(R.id.hvscroll);
        mtitlelistview = (ListView) findViewById(R.id.tlistview);
        mlistview = (ListView) findViewById(R.id.mlistView);
        top = (RelativeLayout) findViewById(R.id.subtop);

        expname = getIntent().getExtras().getString("expname");
        expid = getIntent().getExtras().getString("expid");

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
        new getTableOne(TableOneActivity.this,expid,mtitlelistview,mlistview,adpter).execute();
        //后台载入
        new preLoad(TableOneActivity.this,expid).execute();

        //进入多选模式
        textViewmulti.setVisibility(View.VISIBLE);
        mcallback = new ModeCallback();
        mlistview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mlistview.setMultiChoiceModeListener(mcallback);
        textViewmulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistview.setItemChecked(0, true);
                mlistview.clearChoices();
                mcallback.updateSelectdCount();
            }
        });


        //单选模式，点击监听
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView bIDtextview = (TextView) mlistview.getChildAt(position).findViewById(R.id.t1);
                String batteryId = bIDtextview.getText().toString();
                Log.d("HeD-batteryID", batteryId);
                Intent intent = new Intent();
                intent.putExtra("expid", expid);
                intent.putExtra("expname", expname);
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

    @Override
    public void onBackPressed() {
        Flags flags = Flags.getInstance();
        if(flags.checkFlagState("filter")){
            new getTableOne(TableOneActivity.this,expid,mtitlelistview,mlistview,adpter).execute();
            flags.setFlag_false("filter");
        }else {
            TableOneActivity.this.finish();
        }
    }

    //多选模式回调
    private class ModeCallback implements AbsListView.MultiChoiceModeListener{
        private View mMultiSelectActionBarView;
        private TextView mSelectedCount;

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            updateSelectdCount();
            mode.invalidate();
            adpter.notifyDataSetChanged();

            //long[] checkedid = mlistview.getCheckItemIds();
            long[] checkedid = getListCheckedItemIds();
            if(checkedid == null || (checkedid!=null && checkedid.length !=0)){
                String s = "checkedID is :";
                for (int i = 0; i < checkedid.length; i++) {
                    s = s + "," + checkedid[i];
                }
                Log.d("HeD",String.valueOf(checkedid[0]));
                Log.d("HeD",s);
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.multi_select_menu, menu);
            if(mMultiSelectActionBarView == null){
                mMultiSelectActionBarView = LayoutInflater.from(TableOneActivity.this)
                        .inflate(R.layout.list_multi_select_actionbar, null);
                mSelectedCount = (TextView)mMultiSelectActionBarView
                        .findViewById(R.id.selected_conv_count);
                check = (ImageView)mMultiSelectActionBarView.findViewById(R.id.check);
                check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //long[] checkedid = mlistview.getCheckedItemIds();
                        long[] checkedid = getListCheckedItemIds();
                        List<String> id = new ArrayList<>();
                        for (int i = 0; i < checkedid.length; i++) {
                            LinearLayout layout = (LinearLayout)mlistview.getChildAt((int) checkedid[i]);
                            TextView tv = (TextView)layout.findViewById(R.id.t1);
                            id.add(tv.getText().toString());
                        }
//                        String s = "checkedID is :";
//                        for (int i = 0; i < id.size(); i++) {
//                            s = s + id.get(i);
//                        }
//                        Toast.makeText(TableOneActivity.this, s, Toast.LENGTH_SHORT).show();

                        Flags flags = Flags.getInstance();
                        if(flags.checkFlagState(expid)){
                            Intent intent = new Intent();
                            intent.putExtra("Battery_No",(Serializable)id);
                            intent.putExtra("expid",expid);
                            intent.setClass(TableOneActivity.this,MultiLineChartActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.animator.to_right,R.animator.to_left);


                            Toast.makeText(TableOneActivity.this, "ok", Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(TableOneActivity.this, "后台载入中，请稍等...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            mode.setCustomView(mMultiSelectActionBarView);
            top.setVisibility(View.GONE);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            if (mMultiSelectActionBarView == null) {
//                ViewGroup v = (ViewGroup)LayoutInflater.from(TableOneActivity.this)
//                        .inflate(R.layout.list_multi_select_actionbar, null);
//                mode.setCustomView(v);
//                mSelectedCount = (TextView)v.findViewById(R.id.selected_conv_count);
////                check = (ImageView)v.findViewById(R.id.check);
////                check.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        //long[] checkedid = mlistview.getCheckedItemIds();
////                        long[] checkedid = getListCheckedItemIds();
////                        String s = "checkedID is :";
////                        for (int i = 0; i < checkedid.length; i++) {
////                            s = s + "," + checkedid[i];
////                        }
////                        Toast.makeText(TableOneActivity.this, s, Toast.LENGTH_SHORT).show();
////                    }
////                });
//            }
//            top.setVisibility(View.GONE);
            //更新菜单状态
            MenuItem item = menu.findItem(R.id.action_slelect);
            if(mlistview.getCheckedItemCount() == mlistview.getCount()){
                item.setTitle("取消全选");
            }else {
                item.setTitle("全选");
            }
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getTitle().toString()){
                case "取消全选":
                    mlistview.clearChoices();
                    mlistview.setItemChecked(0,false);
                    updateSelectdCount();
                    break;
                case "全选":
                    for(int i=0;i<mlistview.getCount();i++){
                        mlistview.setItemChecked(i,true);
                    }
                    updateSelectdCount();
            }
            adpter.notifyDataSetChanged();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mlistview.clearChoices();
            top.setVisibility(View.VISIBLE);
            Flags flags = Flags.getInstance();
            if(flags.checkFlagState("filter")){
                new getTableOne(TableOneActivity.this,expid,mtitlelistview,mlistview,adpter).execute();
                flags.setFlag_false("filter");
            }
        }

        public void updateSelectdCount() {
            mSelectedCount.setText(Integer.toString(mlistview.getCheckedItemCount()));
        }

        protected long[] getListCheckedItemIds(){
            final long[] ids = new long[mlistview.getCount()];
            int checkedCount = 0;
            for(int i=0;i<mlistview.getCount();i++){
                if(mlistview.isItemChecked(i)){
                    ids[checkedCount++] = i;
                }
            }
            if (checkedCount == mlistview.getCount()) {
                return ids;
            } else {
                final long[] result = new long[checkedCount];
                System.arraycopy(ids, 0, result, 0, checkedCount);

                return result;
            }
        }
    }
}
