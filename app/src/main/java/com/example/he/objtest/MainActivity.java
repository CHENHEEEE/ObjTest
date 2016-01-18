package com.example.he.objtest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.he.BatteryInfo.BatteryInfo;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String targetNameSpace = "http://tempuri.org/";
    private static final String WSDL = "http://192.168.1.111:1666/Datebase.asmx";

    private static final String sendObj ="getBatteryInfo";

    private Button btnsendobj;
    private BatteryInfo [] InfoList = new BatteryInfo[500];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnsendobj = (Button) findViewById(R.id.btnsendObj);
        btnsendobj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NetAsyncTask().execute();
            }
        });
    }

    class NetAsyncTask extends AsyncTask<Void,Object,Void>{


        @Override
        protected Void doInBackground(Void... params) {
            BatteryInfo info = new BatteryInfo();
            SoapObject soapObject = new SoapObject(targetNameSpace,sendObj);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(soapObject);
            envelope.addMapping(targetNameSpace,"BatteryInfo",info.getClass());
            HttpTransportSE httpTransportSE = new HttpTransportSE(WSDL);
            try {
                httpTransportSE.call(targetNameSpace + sendObj,envelope);
                SoapObject resultObj = (SoapObject)envelope.getResponse();
                int count = resultObj.getPropertyCount();
                SoapObject object;
                String no;
                for (int i=0;i<5;i++){
                    object = (SoapObject)resultObj.getProperty(i);
                     no = object.getProperty("no").toString();
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
