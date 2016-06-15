package com.example.he.NetAsyncTask;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.he.Database.MySQLiteOpenHelper;
import com.example.he.MyTools.Flags;
import com.example.he.PullToRefresh.PullToRefreshLayout;
import com.example.he.PullToRefresh.PullableListView;
import com.example.he.batteryinfoActivity.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HE on 2016/3/10.
 * 这是一个下载数据的线程
 */
public class loadData extends AsyncTask<Object, Integer, String>{
    // WSDL文档中的命名空间
    private static final String targetNameSpace = "http://tempuri.org/";
    // WSDL文档中的URL
    private static final String WSDL = "http://460579d0.nat123.net:27312/Datebase.asmx";
    // 需要调用的方法名
    private static final String GETTESTLIST = "getTestList";
    private static final String GETBATTERYINFO = "getAllBatteryInfo";
    private static final String GETBATTERYDATABYID = "getAllBatteryDataByTestid";
    private static final String GETEQUIPMENT = "getEquipment";

    private Context context;
    private MySQLiteOpenHelper helper;
    private SQLiteDatabase database;
    private Flags flags = Flags.getInstance();
    private int testcount;
    private ProgressDialog mDialog;

    public loadData(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        //初始化数据库
        helper = MySQLiteOpenHelper.getInstance(context);
        database = helper.getWritableDatabase();

        //初始化进度条
        mDialog = new ProgressDialog(context);
        mDialog.setMax(100);
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setCancelable(false);
        //mDialog.setTitle("下载数据");
        mDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        mDialog.dismiss();
        if (result.equals("success")) {
            Toast.makeText(context,"下载成功 ",Toast.LENGTH_SHORT).show();
            SharedPreferences preferences = context.getSharedPreferences("Version",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("dataVersion",preferences.getInt("latestVersion",0));
            editor.apply();
        }
        else Toast.makeText(context,"下载失败",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(Object... params) {
        //下载TestList
        SoapObject soapObject = new SoapObject(targetNameSpace,
                GETTESTLIST);
        SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelop.dotNet = true;
        envelop.setOutputSoapObject(soapObject);
        HttpTransportSE httpSE = new HttpTransportSE(WSDL);

        try {
            httpSE.call(targetNameSpace + GETTESTLIST, envelop);
            // 得到远程方法返回的SOAP对象
            SoapObject resultObj = (SoapObject) envelop.getResponse();

            //数据库操作，新建库,新建表
            //database.execSQL("create table one(mid INTEGER PRIMARY KEY autoincrement);");
            database.execSQL("DROP TABLE IF EXISTS TestList");
            database.execSQL("CREATE TABLE TestList (id INTEGER PRIMARY KEY," +
                    "BatteryType text,BatteryBrand text,City text,TestType text," +
                    "TestName text,Sname text);");

            int num = resultObj.getPropertyCount();

            //得到服务器传回的数据,并将表格数据存到数据库中
            num = num/7;
            testcount = num;
            ContentValues cv = new ContentValues();
            for (int i = 0; i < num; i++) {
                cv.clear();
                cv.put("id",resultObj.getPropertyAsString(i*7));
                cv.put("BatteryType",resultObj.getPropertyAsString(i*7+1));
                cv.put("BatteryBrand",resultObj.getPropertyAsString(i*7+2));
                cv.put("City",resultObj.getPropertyAsString(i*7+3));
                cv.put("TestType",resultObj.getPropertyAsString(i*7+4));
                cv.put("TestName",resultObj.getPropertyAsString(i*7+5));
                cv.put("Sname",resultObj.getPropertyAsString(i*7+6));

                database.insert("TestList",null,cv);

                //更新进度条
                publishProgress(10*(i+1)/num);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException";
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return "XmlPullParserException";
        }

        //下载BatteryInfo
        //soapObject = new SoapObject(targetNameSpace, GETBATTERYINFO);
        try {
            httpSE.call(targetNameSpace + GETBATTERYINFO, envelop);
            // 得到远程方法返回的SOAP对象
            SoapObject resultObj = (SoapObject) envelop.getResponse();
            //数据库操作，新建库,新建表
            //database.execSQL("create table one(mid INTEGER PRIMARY KEY autoincrement);");
            database.execSQL("DROP TABLE IF EXISTS BatteryInfo");
            database.execSQL("CREATE TABLE BatteryInfo (NO INTEGER PRIMARY KEY," +
                    "Battery_NO text,C_rate text,DoD REAL,Section text," +
                    "Temperature INTEGER,Battery_Type text,Stas text,TestID INTEGER);");

            int num = resultObj.getPropertyCount();

            //得到服务器传回的数据,并将表格数据存到数据库中
            num = num/9;
            ContentValues cv = new ContentValues();
            for (int i = 0; i < num; i++) {
                cv.clear();
                cv.put("NO",resultObj.getPropertyAsString(i*9));
                cv.put("Battery_NO",resultObj.getPropertyAsString(i*9+1));
                cv.put("C_rate",resultObj.getPropertyAsString(i*9+2));
                cv.put("DoD",resultObj.getPropertyAsString(i*9+3));
                cv.put("Section",resultObj.getPropertyAsString(i*9+4));
                cv.put("Temperature",resultObj.getPropertyAsString(i*9+5));
                cv.put("Battery_Type",resultObj.getPropertyAsString(i*9+6));
                cv.put("Stas",resultObj.getPropertyAsString(i*9+7));
                cv.put("TestID",resultObj.getPropertyAsString(i*9+8));
                database.insert("BatteryInfo",null,cv);

                //更新进度条
                publishProgress(10+20*(i+1)/num);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException";
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return "XmlPullParserException";
        }

        //下载各个实验数据表
        for(int j=1; j<=testcount;j++){
            soapObject = new SoapObject(targetNameSpace, GETBATTERYDATABYID);
            if(!soapObject.hasProperty("testid"))
                soapObject.addProperty("testid",String.valueOf(j));
            else soapObject.setProperty(0,String.valueOf(j));
            envelop.setOutputSoapObject(soapObject);
            try {
                httpSE.call(targetNameSpace + GETBATTERYDATABYID, envelop);
                // 得到远程方法返回的SOAP对象
                SoapObject resultObj = (SoapObject) envelop.getResponse();
                //数据库操作，新建库,新建表
                //database.execSQL("create table one(mid INTEGER PRIMARY KEY autoincrement);");
                database.execSQL("DROP TABLE IF EXISTS TestID_"+String.valueOf(j));
                database.execSQL("CREATE TABLE TestID_"+String.valueOf (j)+"(ID INTEGER PRIMARY KEY," +
                        "Test_NO text,Battery_NO text,Cycle_Times INTEGER,Capacity_amended FLOAT," +
                        "Degradion_amended DOUBLE PRECISION,[Degradation--mAh] FLOAT,Test_Date text,"+
                        "Accumulate_Capacity FLOAT,Degradation FLOAT,Capacity FLOAT);");

                int num = resultObj.getPropertyCount();

                //得到服务器传回的数据,并将表格数据存到数据库中
                num = num/11;
                ContentValues cv = new ContentValues();
                for (int i = 0; i < num; i++) {
                    cv.clear();
                    cv.put("ID",resultObj.getPropertyAsString(i*11));
                    cv.put("Test_NO",resultObj.getPropertyAsString(i*11+1));
                    cv.put("Battery_NO",resultObj.getPropertyAsString(i*11+2));
                    cv.put("Cycle_Times",resultObj.getPropertyAsString(i*11+3));
                    cv.put("Capacity_amended",resultObj.getPropertyAsString(i*11+4));
                    cv.put("Degradion_amended",resultObj.getPropertyAsString(i*11+5));
                    cv.put("[Degradation--mAh]",resultObj.getPropertyAsString(i*11+6));
                    cv.put("Test_Date",resultObj.getPropertyAsString(i*11+7));
                    cv.put("Accumulate_Capacity",resultObj.getPropertyAsString(i*11+8));
                    cv.put("Degradation",resultObj.getPropertyAsString(i*11+9));
                    cv.put("Capacity",resultObj.getPropertyAsString(i*11+10));
                    database.insert("TestID_"+String.valueOf(j),null,cv);

                    //更新进度条
                    publishProgress(30+60*(i+1)*j/(num*testcount));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "IOException";
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                return "XmlPullParserException";
            }
        }

        //下载Equipment
        //soapObject = new SoapObject(targetNameSpace, GETEQUIPMENT);
        try {
            httpSE.call(targetNameSpace + GETEQUIPMENT, envelop);
            // 得到远程方法返回的SOAP对象
            SoapObject resultObj = (SoapObject) envelop.getResponse();
            //数据库操作，新建库,新建表
            //database.execSQL("create table one(mid INTEGER PRIMARY KEY autoincrement);");
            database.execSQL("DROP TABLE IF EXISTS Equipment");
            database.execSQL("CREATE TABLE Equipment (Equipment TEXT," +
                    "Channel TEXT,Status TEXT,Battery_NO TEXT,Temperature TEXT," +
                    "C_rate TEXT,Section TEXT,City TEXT,EquipmentID INTEGER);");

            int num = resultObj.getPropertyCount();

            //得到服务器传回的数据,并将表格数据存到数据库中
            num = num/9;
            ContentValues cv = new ContentValues();
            for (int i = 0; i < num; i++) {
                cv.clear();
                cv.put("Equipment",resultObj.getPropertyAsString(i*9));
                cv.put("Channel",resultObj.getPropertyAsString(i*9+1));
                cv.put("Status",resultObj.getPropertyAsString(i*9+2));
                cv.put("Battery_NO",resultObj.getPropertyAsString(i*9+3));
                cv.put("Temperature",resultObj.getPropertyAsString(i*9+4));
                cv.put("C_rate",resultObj.getPropertyAsString(i*9+5));
                cv.put("Section",resultObj.getPropertyAsString(i*9+6));
                cv.put("City",resultObj.getPropertyAsString(i*9+7));
                cv.put("EquipmentID",resultObj.getPropertyAsString(i*9+8));
                database.insert("Equipment",null,cv);

                //更新进度条
                publishProgress(90+10*(i+1)/num);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException";
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return "XmlPullParserException";
        }

        return "success";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        mDialog.setProgress(values[0]);
    }
}

