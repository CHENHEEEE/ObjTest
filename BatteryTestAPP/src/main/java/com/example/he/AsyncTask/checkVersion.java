package com.example.he.AsyncTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.example.he.MyTools.Zoom;
import com.example.he.batteryinfoActivity.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by HE on 2016/5/30.
 * 这是一个查询数据版本的线程
 */
public class checkVersion extends AsyncTask<Object, Object, String>{
    // WSDL文档中的命名空间
    private static final String targetNameSpace = "http://tempuri.org/";
    // WSDL文档中的URL
    private static final String WSDL = "http://460579d0.nat123.net:27312/Datebase.asmx";
    // 需要调用的方法名
    private static final String methodname = "getVersion";
    private int latestVersion,dataVersion;
    private Context context;

    public checkVersion(Context context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(String result) {
        SharedPreferences preferences = context.getSharedPreferences("Version",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        dataVersion = preferences.getInt("dataVersion",0);

        if (result.equals("success")) {
            editor.putInt("latestVersion", latestVersion);
            if(dataVersion==0 || dataVersion<latestVersion){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                Drawable drawable = context.getResources().getDrawable(R.drawable.ic_alert);
                builder.setIcon(Zoom.zoomDrawable(drawable,100,100));
                builder.setTitle("更新");
                builder.setMessage("有数据更新,是否现在下载?");

                //监听button点击事件
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new loadData(context).execute();
                    }
                });

                builder.setNegativeButton("否",null);

                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        else editor.putInt("latestVersion",0);

        editor.apply();
    }

    @Override
    protected String doInBackground(Object... params) {
        // 根据命名空间和方法得到SoapObject对象
        SoapObject soapObject = new SoapObject(targetNameSpace,
                methodname);
        // 通过SOAP1.1协议得到envelop对象
        SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // 将soapObject对象设置为envelop对象，传出消息
        envelop.dotNet = true;
        envelop.setOutputSoapObject(soapObject);
        HttpTransportSE httpSE = new HttpTransportSE(WSDL);

        // 开始调用远程方法
        try {
            httpSE.call(targetNameSpace + methodname, envelop);
            // 得到远程方法返回的SOAP对象
            SoapPrimitive resultObj = (SoapPrimitive) envelop.getResponse();
            latestVersion = Integer.parseInt(resultObj.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException";
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return "XmlPullParserException";
        }
        return "success";
    }
}

