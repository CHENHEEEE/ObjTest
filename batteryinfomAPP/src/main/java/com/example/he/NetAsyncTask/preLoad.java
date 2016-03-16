package com.example.he.NetAsyncTask;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.he.MyTools.Flags;
import com.example.he.database.MySQLiteOpenHelper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HE on 2016/3/10.
 * 这是一个后台文件缓存的线程
 */
public class preLoad extends AsyncTask<Object, Object, String>{
    // WSDL文档中的命名空间
    private static final String targetNameSpace = "http://tempuri.org/";
    // WSDL文档中的URL
    private static final String WSDL = "http://192.168.1.111:1666/Datebase.asmx";
    // 需要调用的方法名
    private static final String methodname = "getTableTwo_full";
    private Context mcontext;
    private String mExpid,tablename;
    private MySQLiteOpenHelper helper;
    private SQLiteDatabase database;
    private Flags flags = Flags.getInstance();

    public preLoad(Context context, String Expid){
        mcontext = context;
        mExpid = Expid;
        tablename = "TableTwoOf" + Expid;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(mcontext,"后台载入中",Toast.LENGTH_SHORT).show();
        flags.setFlag_false(mExpid);
    }

    @Override
    protected void onPostExecute(String result) {

        if (result.equals("success")) {
//            //save the task list to preference
//            SharedPreferences prefs = mcontext.getSharedPreferences("", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = prefs.edit();
/*            try {
                editor.putString("", ObjectSerializer.serialize((Serializable)listItems));
            } catch (IOException e) {
                e.printStackTrace();
            }
            editor.apply();*/

            Toast.makeText(mcontext, "后台载入成功", Toast.LENGTH_SHORT).show();

            flags.setFlag_true(mExpid);

        }else {
            Toast.makeText(mcontext, "后台载入失败", Toast.LENGTH_SHORT).show();
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

            //数据库操作，新建库,新建表
            helper = MySQLiteOpenHelper.getInstance(mcontext);
            database = helper.getWritableDatabase();//调用方法，创建或打开链接
            //database.execSQL("create table one(mid INTEGER PRIMARY KEY autoincrement);");
            database.execSQL("DROP TABLE IF EXISTS " + tablename);
            database.execSQL("CREATE TABLE " + tablename + " (id INTEGER PRIMARY KEY);");
            ContentValues cv = new ContentValues();

            int cursor = 2;
            List<String> colname = new ArrayList<>();
            colname.add("id");
            for (int i=1; i < count; i++) {
                cursor++;
                //数据库操作，添加列
                String cname = resultObj.getProperty(cursor).toString();
                String sql;
                //特定设置cycle_times为整数类型，方便以后比较
                if ("Cycle_Times".equals(cname)){
                    sql = "alter table " + tablename + " add column "+ resultObj.getProperty(cursor).toString()+" INTEGER;";
                }else {
                    sql = "alter table " + tablename + " add column "+ resultObj.getProperty(cursor).toString()+" text;";
                }
                colname.add(resultObj.getProperty(cursor).toString());
                database.execSQL(sql);
            }

            //数据库操作，将表格数据存到数据库中，通过事务优化存储速度
            database.beginTransaction();
            num = num/count;
            for (int j = 0; j < num; j++) {
                cv.clear();
                for (int i= 0;i<count;i++){
                    cursor++;
                    cv.put(colname.get(i),resultObj.getProperty(cursor).toString());
                }
                database.insert(tablename, null, cv);
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
        Log.d("time-preLoad",String.valueOf(t2-t1));

        return "success";
    }
}

