package com.example.he.NetAsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
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
public class getEqmSta extends AsyncTask<Object, Object, String>{
    // WSDL文档中的命名空间
    private static final String targetNameSpace = "http://tempuri.org/";
    // WSDL文档中的URL
    private static final String WSDL = "http://192.168.1.111:1666/Datebase.asmx";

    // 需要调用的方法名
    private static final String methodname = "getEqmSta";
    private Context mcontext;
    private String mid,content,time;
    TextView mtv_c,mtv_t;
    ProgressDialog pd;

    public getEqmSta(Context context,String id,TextView tv_c,TextView tv_t){
        mcontext = context;
        mid = id;
        mtv_c = tv_c;
        mtv_t = tv_t;
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
        pd.dismiss();
        if (result.equals("success")) {
            //更新UI的内容和时间
            mtv_c.setText(content);
            mtv_t.setText("更新于："+time);
        }else {
            Toast.makeText(mcontext, "设备状态载入失败", Toast.LENGTH_SHORT).show();
        }
        super.onPostExecute(result);
    }



    @Override
    protected String doInBackground(Object... params) {
        // 根据命名空间和方法得到SoapObject对象
        SoapObject soapObject = new SoapObject(targetNameSpace,
                methodname);
        soapObject.addProperty("id",mid);
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
            SoapObject resultObj = (SoapObject) envelop.getResponse();
            // 得到服务器传回的数据
            content = resultObj.getProperty(0).toString();
            time = resultObj.getProperty(1).toString();
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
