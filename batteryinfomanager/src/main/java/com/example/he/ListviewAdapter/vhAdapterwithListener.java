package com.example.he.ListviewAdapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he.base.ListPopup;
import com.example.he.batteryinfoActivity.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by HE on 2016/3/1.
 * 重写baseAdapter，提高listview载入速度,并对标题栏每个Item加入监听
 */
public class vhAdapterwithListener extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Map<String,String>> mlistItems = new ArrayList<Map<String,String>>();
    private Context mcontext;
    private Activity mactivity;

    public vhAdapterwithListener(Context context, List<Map<String, String>> mlistItems){
        this.mInflater = LayoutInflater.from(context);
        this.mlistItems = mlistItems;
        this.mcontext = context;
        mactivity = (Activity)context;
    }

    @Override
    public int getCount() {
        return mlistItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        MyListviewListener myListviewListener = new MyListviewListener();
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.tabletitle_item,null,true);
            holder.t0 = (TextView) convertView.findViewById(R.id.t0);
            holder.t1 = (TextView) convertView.findViewById(R.id.t1);
            holder.t2 = (TextView) convertView.findViewById(R.id.t2);
            holder.t3 = (TextView) convertView.findViewById(R.id.t3);
            holder.t4 = (TextView) convertView.findViewById(R.id.t4);
            holder.t5 = (TextView) convertView.findViewById(R.id.t5);
            holder.t6 = (TextView) convertView.findViewById(R.id.t6);
            holder.t7 = (TextView) convertView.findViewById(R.id.t7);
            holder.t8 = (TextView) convertView.findViewById(R.id.t8);
            holder.t9 = (TextView) convertView.findViewById(R.id.t9);
            holder.t10 = (TextView) convertView.findViewById(R.id.t10);
            holder.t11 = (TextView) convertView.findViewById(R.id.t11);
            holder.t12 = (TextView) convertView.findViewById(R.id.t12);
            holder.t13 = (TextView) convertView.findViewById(R.id.t13);
            holder.t14 = (TextView) convertView.findViewById(R.id.t14);

            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.t0.setText(mlistItems.get(position).get("0"));
        holder.t1.setText(mlistItems.get(position).get("1"));
        holder.t2.setText(mlistItems.get(position).get("2"));
        holder.t3.setText(mlistItems.get(position).get("3"));
        holder.t4.setText(mlistItems.get(position).get("4"));
        holder.t5.setText(mlistItems.get(position).get("5"));
        holder.t6.setText(mlistItems.get(position).get("6"));
        holder.t7.setText(mlistItems.get(position).get("7"));
        holder.t8.setText(mlistItems.get(position).get("8"));
        holder.t9.setText(mlistItems.get(position).get("9"));
        holder.t10.setText(mlistItems.get(position).get("10"));
        holder.t11.setText(mlistItems.get(position).get("11"));
        holder.t12.setText(mlistItems.get(position).get("12"));
        holder.t13.setText(mlistItems.get(position).get("13"));
        holder.t14.setText(mlistItems.get(position).get("14"));

        //设置item监听
        holder.t0.setOnClickListener(myListviewListener);

        return convertView;
    }

    static class ViewHolder{
        public TextView t0;
        public TextView t1;
        public TextView t2;
        public TextView t3;
        public TextView t4;
        public TextView t5;
        public TextView t6;
        public TextView t7;
        public TextView t8;
        public TextView t9;
        public TextView t10;
        public TextView t11;
        public TextView t12;
        public TextView t13;
        public TextView t14;
    }

    private class MyListviewListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d("HeD", "onclick");
            startPOPwindow(v);
        }

        private void startPOPwindow(View parent){

            final int TAG_CREATE=0x01;
            final int TAG_DELETE=0x02;
            final int TAG_MODIFY=0x03;
            ListPopup mListPopup;
            ListPopup.Builder builder=new ListPopup.Builder(mactivity);
            builder.addItem(TAG_CREATE,"Create-01");
            builder.addItem(TAG_MODIFY,"Modify-01");
            builder.addItem(TAG_CREATE,"Create-02");
            builder.addItem(TAG_DELETE,"Delete-01");
            builder.addItem(TAG_MODIFY,"Modify-02");
            builder.addItem(TAG_CREATE,"Create-03");
            builder.addItem(TAG_DELETE,"Delete-02");
            builder.addItem(TAG_MODIFY,"Modify-03");
            builder.addItem(TAG_DELETE,"Delete-03");
            builder.addItem(TAG_MODIFY,"Modify-04");
            builder.addItem(TAG_DELETE,"Delete-04");
            builder.addItem(TAG_CREATE,"Create-04");
            mListPopup=builder.build();

            mListPopup.setOnListPopupItemClickListener(new ListPopup.OnListPopupItemClickListener() {
                @Override
                public void onItemClick(int what) {
                    switch (what){
                        case TAG_CREATE:
                            Toast.makeText(mactivity, "click create",Toast.LENGTH_SHORT).show();
                            break;
                        case TAG_DELETE:
                            Toast.makeText(mactivity, "click delete", Toast.LENGTH_SHORT).show();
                            //ToastUtils.ToastMessage(mactivity,"click delete");
                            break;
                        case TAG_MODIFY:
                            Toast.makeText(mactivity, "click modify",Toast.LENGTH_SHORT).show();
                            // ToastUtils.ToastMessage(mactivity,"click modify");
                            break;
                        default:
                            break;
                    }
                }
            });

            mListPopup.showPopupWindow();


//            Log.d("HeD","popwindow");
//
//            LayoutInflater inflater = LayoutInflater.from(mcontext);
//            //引入POPwindow配置文件
//            View view = inflater.inflate(R.layout.popwindow,null);
//            //创建POPwindow对象
//            final PopupWindow popupWindow = new PopupWindow(view,
//                    ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
//
//            popupWindow.showAtLocation(this, Gravity.BOTTOM, 0, 0);
//            popupWindow.setFocusable(true);
//            popupWindow.setTouchable(true);
//
//            popupWindow.setOutsideTouchable(true);
//            popupWindow.showAsDropDown(parent,0,0);

//            LayoutInflater inflater;
//            //获取LayoutInflater实例
//            inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            //这里的main布局是在inflate中加入的哦，以前都是直接this.setContentView()的吧？呵呵
//            //该方法返回的是一个View的对象，是布局中的根
//            View layout;
//            layout = inflater.inflate(R.layout.popwindow, null);
//            //下面我们要考虑了，我怎样将我的layout加入到PopupWindow中呢？？？很简单
//            PopupWindow menuWindow = new PopupWindow(layout, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); //后两个参数是width和height
//            //menuWindow.showAsDropDown(layout); //设置弹出效果
//            //menuWindow.showAsDropDown(null, 0, layout.getHeight());
//            //设置如下四条信息，当点击其他区域使其隐藏，要在show之前配置
//            menuWindow.setFocusable(true);
//            menuWindow.setOutsideTouchable(true);menuWindow.update();
//            menuWindow.setBackgroundDrawable(new BitmapDrawable());
//
//            menuWindow.showAtLocation(this.findViewById(R.id.schoolmain), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 50); //设置layout在PopupWindow中显示的位置
//            //如何获取我们main中的控件呢？也很简单
//            LinearLayout btn = (LinearLayout) layout.findViewById(R.id.button);
        }
    }
}


