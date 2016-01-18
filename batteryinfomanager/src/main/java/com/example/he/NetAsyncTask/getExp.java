package com.example.he.NetAsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.he.batteryinfomanager.R;

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
 * Created by HE on 2015/12/25.
 */
public class getExp extends AsyncTask<Object, Object, String> {
    // WSDL文档中的命名空间
    private static final String targetNameSpace = "http://tempuri.org/";
    // WSDL文档中的URL
    private static final String WSDL = "http://192.168.1.111:1666/Datebase.asmx";

    // 需要调用的方法名
    private static final String methodname = "getExp";
    private List<Map<String,String>> listItems = new ArrayList<Map<String,String>>();
    private ListView mListView;
    private Context mcontext;
    private String mcity;
    ProgressDialog pd;

    public getExp(Context context,String city,ListView listView){
        mcontext = context;
        mcity = city;
        mListView = listView;
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
            //列表适配器
            SimpleAdapter simpleAdapter = new SimpleAdapter(mcontext, listItems, R.layout.name_item_new,
                    new String[] {"expname","sname","id"}, new int[]{R.id.Expname,R.id.Sname,R.id.Expid});
            mListView.setAdapter(simpleAdapter);
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
        soapObject.addProperty("city",mcity);
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
            int count = resultObj.getPropertyCount();
            count = count/4;
            for (int i = 0; i < count; i++) {
                int a =i*4;
                Map<String,String> listItem = new HashMap<String, String>();
                listItem.put("id", resultObj.getProperty(a).toString());
                listItem.put("expname", resultObj.getProperty(a+1).toString());
                listItem.put("sname", "负责人："+resultObj.getProperty(a+2).toString());
                listItem.put("city", resultObj.getProperty(a+3).toString());
                listItems.add(listItem);
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
}
