package com.example.he.NetAsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by HE on 2016/2/1.
 * 这是一个联网获得设备状态的线程
 */
public class updateEqmSta extends AsyncTask<Object, Object, String>{
    // WSDL文档中的命名空间
    private static final String targetNameSpace = "http://tempuri.org/";
    // WSDL文档中的URL
    private static final String WSDL = "http://192.168.1.111:1666/Datebase.asmx";

    // 需要调用的方法名
    private static final String methodname = "updateEqmSta";
    private Context mcontext;
    private String mid,mstatus;
    ProgressDialog pd;

    public updateEqmSta(Context context, String id,String status){
        mcontext = context;
        mid = id;
        mstatus = status;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(mcontext);
        pd.setMessage("更新中…");
        pd.setIndeterminate(false);// 在最大值最小值中移动
        pd.setCancelable(true);// 可以取消
        pd.show();
    }

    @Override
    protected void onPostExecute(String result) {
        pd.dismiss();
        if (result.equals("success")) {
            //更新成功
            Toast.makeText(mcontext, "设备状态更新成功", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mcontext, "设备状态更新失败", Toast.LENGTH_SHORT).show();
        }
        super.onPostExecute(result);
    }



    @Override
    protected String doInBackground(Object... params) {
        // 根据命名空间和方法得到SoapObject对象
        SoapObject soapObject = new SoapObject(targetNameSpace,
                methodname);
        soapObject.addProperty("id",mid);
        soapObject.addProperty("status",mstatus);
        // 通过SOAP1.1协议得到envelop对象
        SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // 将soapObject对象设置为envelop对象，传出消息

        envelop.dotNet = true;
        envelop.setOutputSoapObject(soapObject);
        // 或者envelop.bodyOut = soapObject;
        HttpTransportSE httpSE = new HttpTransportSE(WSDL);
        // 开始调用远程方法
        try {
            httpSE.call(targetNameSpace + methodname, envelop);
            // 得到远程方法返回的SOAP对象
            String result = envelop.getResponse().toString();
            // 得到服务器传回的数据
            if (result.equals("true")){
                return "success";
            }
            else return "fail";
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException";
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return "XmlPullParserException";
        }
    }
}
