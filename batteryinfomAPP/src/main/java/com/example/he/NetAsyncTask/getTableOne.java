package com.example.he.NetAsyncTask;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he.ListviewAdapter.vhAdapter_cut;
import com.example.he.ListviewAdapter.vhAdapterwithListener;
import com.example.he.database.MySQLiteOpenHelper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by HE on 2015/12/29.
 * 这是一个联网获得一级表（电池信息）的线程
 */
public class getTableOne extends AsyncTask<Object, Object, String>{
    // WSDL文档中的命名空间
    private static final String targetNameSpace = "http://tempuri.org/";
    // WSDL文档中的URL
    private static final String WSDL = "http://192.168.1.111:1666/Datebase.asmx";
    // 需要调用的方法名
    private static final String methodname = "getTableOne";
    private List<Map<String,String>> tlistItems = new ArrayList<Map<String,String>>();
    private List<Map<String,String>> listItems = new ArrayList<Map<String,String>>();
    private ListView mtitlelistview,mListView;
    private Context mcontext;
    private String mExpid;
    private ProgressDialog pd;
    private MySQLiteOpenHelper helper;
    private SQLiteDatabase database;
    private int length;
    private vhAdapter_cut msimpleAdapter;

    public getTableOne(Context context,String Expid,ListView titleListview, ListView listView,vhAdapter_cut msimpleAdapter){
        mcontext = context;
        mExpid = Expid;
        mtitlelistview = titleListview;
        mListView = listView;
        this.msimpleAdapter = msimpleAdapter;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(mcontext);
        pd.setMessage("载入中…");
        pd.setIndeterminate(false);// 在最大值最小值中移动
        pd.setCancelable(true);// 可以取消
        pd.show();
    }

    @Override
    protected void onPostExecute(String result) {

        //耗时计算
        Date dt1= new Date();
        Long t1= dt1.getTime();

        pd.dismiss();
        if (result.equals("success")) {
            //列表适配器
//            SimpleAdapter tsimpleAdapter = new SimpleAdapter(mcontext, tlistItems, R.layout.tabletitle_item,
//                    new String[] {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14"},
//                    new int[]{R.id.t0,R.id.t1,R.id.t2,R.id.t3,R.id.t4,R.id.t5,R.id.t6,R.id.t7,R.id.t8,R.id.t9,R.id.t10,R.id.t11,R.id.t12,R.id.t13,R.id.t14});
//            mtitlelistview.setAdapter(tsimpleAdapter);
//            vhAdapter msimpleAdapter = new vhAdapter(mcontext, listItems, R.layout.table_item,
//                    new String[] {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14"},
//                    new int[]{R.id.t0,R.id.t1,R.id.t2,R.id.t3,R.id.t4,R.id.t5,R.id.t6,R.id.t7,R.id.t8,R.id.t9,R.id.t10,R.id.t11,R.id.t12,R.id.t13,R.id.t14});
//            mListView.setAdapter(msimpleAdapter);
            Mhandler mhandler = new Mhandler();
            vhAdapterwithListener tsimpleAdapter = new vhAdapterwithListener(mcontext,tlistItems,length,mhandler);
            mtitlelistview.setAdapter(tsimpleAdapter);

            msimpleAdapter.setadapter(mcontext,listItems,length,mListView);
            mListView.setAdapter(msimpleAdapter);

            Date dt2= new Date();
            long t2 = dt2.getTime();
            Log.d("time-adapter",String.valueOf(t2-t1));

//            //SQLite查询
//            Cursor cursor = database.rawQuery("select distinct DoD from one",null);
//            cursor.moveToFirst();
//            String [] dod = new String[15];
//            int i=0;
//            while (!cursor.isAfterLast()){
//                dod[i] = cursor.getString(0);
//                Log.d("HeD_database",dod[i]);
//                i++;
//                cursor.moveToNext();
//            }
        }else {
            Toast.makeText(mcontext, "载入失败", Toast.LENGTH_SHORT).show();
        }
        super.onPostExecute(result);
    }

    @Override
    protected String doInBackground(Object... params) {
        // 根据命名空间和方法得到SoapObject对象
        SoapObject soapObject = new SoapObject(targetNameSpace,
                methodname);
        soapObject.addProperty("expid",mExpid);
        // 通过SOAP1.1协议得到envelop对象
        SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // 将soapObject对象设置为envelop对象，传出消息
        envelop.dotNet = true;
        envelop.setOutputSoapObject(soapObject);
        // 或者envelop.bodyOut = soapObject;
        HttpTransportSE httpSE = new HttpTransportSE(WSDL);

        //耗时计算
        Date dt1= new Date();
        Long t1= dt1.getTime();

        // 开始调用远程方法
        try {
            httpSE.call(targetNameSpace + methodname, envelop);
            // 得到远程方法返回的SOAP对象
            SoapObject resultObj = (SoapObject) envelop.getResponse();
            // 得到服务器传回的数据
            int num = Integer.parseInt(resultObj.getProperty(0).toString());
            int count = Integer.parseInt(resultObj.getProperty(1).toString());
            length = count;
            int cursor = 1;
            Map<String,String> tlistItem = new HashMap<String, String>();

            //数据库操作，新建库,新建表
            helper = MySQLiteOpenHelper.getInstance(mcontext);
            database = helper.getWritableDatabase();//调用方法，创建或打开链接
            //database.execSQL("create table one(mid INTEGER PRIMARY KEY autoincrement);");
            database.execSQL("DROP TABLE IF EXISTS one");
            database.execSQL("CREATE TABLE one (b_id INTEGER PRIMARY KEY AUTOINCREMENT);");
            ContentValues cv = new ContentValues();

            //从resultObj中获取列名
            for (int i=0; i < count; i++) {
                cursor++;
                tlistItem.put(String.valueOf(i), resultObj.getProperty(cursor).toString());

                //数据库操作，添加列
                String sql = "alter table one add column "+ resultObj.getProperty(cursor).toString()+" text;";
                database.execSQL(sql);
            }
            tlistItems.add(tlistItem);

            //从resultObj中获取字段值
            num = num/count;
            for (int j = 0; j < num; j++) {
                Map<String, String> listItem = new HashMap<String, String>();
                for (int i= 0;i<count;i++){
                    cursor++;
                    listItem.put(String.valueOf(i), resultObj.getProperty(cursor).toString());
                }
                listItems.add(listItem);
            }

            //将表格数据存到数据库中，通过事务优化存储速度
            database.beginTransaction();
            for (int j = 0; j < num; j++) {
                Map<String, String> listItem = new HashMap<String, String>();
                listItem = listItems.get(j);
                cv.clear();
                for (int i= 0;i<count;i++){
                    String key = tlistItem.get(String.valueOf(i));
                    cv.put(key,listItem.get(String.valueOf(i)));
                }
                database.insert("one", null, cv);
            }
            database.setTransactionSuccessful();
            database.endTransaction();

        } catch (IOException e) {
            e.printStackTrace();
            return "IOException";
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return "XmlPullParserException";
        }

        Date dt2= new Date();
        long t2 = dt2.getTime();
        Log.d("time-getresultObj",String.valueOf(t2-t1));

        return "success";
    }

    class  Mhandler extends Handler{
        public Mhandler(){}

        public Mhandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //接收消息
            if (msg.what == 666 && msg.obj != null)
            {
                String colvalue = msg.obj.toString();
                String colid = String.valueOf(msg.arg1);
                HashMap<String,String> listItem;
                List<Map<String,String>> list = new ArrayList<>();
                List<Integer>newposition = new ArrayList<>();
                int positoin = 0;
                for(Iterator i = listItems.iterator();i.hasNext();positoin++){
                    listItem = (HashMap)i.next();
                    String value = listItem.get(colid).toString();
                    if(colvalue.equals(value)||mListView.isItemChecked(positoin)){
                        if(mListView.isItemChecked(positoin)){
                            newposition.add(list.size());
                        }
                        list.add(listItem);
                    }
                }
                mListView.clearChoices();
                for(positoin=0;positoin<newposition.size();positoin++){
                    mListView.setItemChecked(newposition.get(positoin),true);
                }
                listItems.clear();
                for(Iterator i = list.iterator();i.hasNext();){
                    listItem = (HashMap)i.next();
                    listItems.add(listItem);
                }
                msimpleAdapter.notifyDataSetChanged();
            }
        }
    }
}
