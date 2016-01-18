package com.example.studentlist_v2;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class ListAcitvity extends Activity implements android.view.View.OnClickListener{

	private ViewPager mViewpager;
	private PagerAdapter mAdapter;
	private List<View> mViews=new ArrayList<View>();
	//TAB
	private LinearLayout mTabUpdate,mTabCld;
	private ImageButton mUpdateImg,mCldImg;  
	
	// WSDL文档中的命名空间
	private static final String targetNameSpace = "http://tempuri.org/";
	// WSDL文档中的URL
	private static final String WSDL = "http://370381b0.nat123.net:18506/WebService1.asmx";

	// 需要调用的方法名
	private static final String getStudentList  = "getStudentList ";
	private List<Map<String,String>> listItems;
	private ListView mListView;
	private ProgressDialog pd;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity);
		
		pd = new ProgressDialog(ListAcitvity.this);  
        pd.setMessage("载入中…");  
        pd.setIndeterminate(false);// 在最大值最小值中移动  
        pd.setCancelable(true);// 可以取消  
        pd.show();
        
		initView();
		
		initEvents();
		

		
	}

	private void initEvents() {
		mTabUpdate.setOnClickListener(this);
		mTabCld.setOnClickListener(this);
		
		mViewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				
				int currentItem=mViewpager.getCurrentItem();
				switch (currentItem) {
				case 0:
					resetImg();
					mUpdateImg.setImageResource(R.drawable.update_pressed);
					break;
				case 1:
					resetImg();
					mCldImg.setImageResource(R.drawable.calendar_pressed);
					break;
				default:
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}

	@SuppressLint("InflateParams")
	private void initView() {
		mViewpager = (ViewPager)findViewById(R.id.id_viewpager);
		//Tabs
		mTabUpdate=(LinearLayout)findViewById(R.id.id_tab_update);
		mTabCld=(LinearLayout)findViewById(R.id.id_tab_cld);
		//ImageButtons
		mUpdateImg=(ImageButton)findViewById(R.id.id_tab_update_img);
		mCldImg=(ImageButton)findViewById(R.id.id_tab_cld_img);
		
		LayoutInflater mInflater = LayoutInflater.from(this);
		View tab01=mInflater.inflate(R.layout.tab01, null);
		View tab02=mInflater.inflate(R.layout.tab02, null);		
		mViews.add(tab01);
		mViews.add(tab02);
		
		mAdapter = new PagerAdapter() {
			
			
			
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView(mViews.get(position));
				super.destroyItem(container, position, object);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View view=mViews.get(position);
				container.addView(view);
				return view;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0==arg1;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mViews.size();
			}
		};
		
		mViewpager.setAdapter(mAdapter);
		
		listItems = new ArrayList<Map<String,String>>();
		mListView = (ListView) tab02.findViewById(R.id.listView1);
		new NetAsyncTask().execute();

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String mSname = listItems.get(position).get("name");
				Log.d("StudentName", mSname);
				
				//获取当前的登录用户
				String user = getIntent().getExtras().getString("Sname");
				Log.d("user", user);
				Intent intent = new Intent();
				intent.putExtra("Sname", mSname);
				intent.putExtra("user", user);
				intent.setClass(ListAcitvity.this, WeekActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		
		resetImg();
		switch (v.getId()) {
		case R.id.id_tab_update:
			mViewpager.setCurrentItem(0);
			mUpdateImg.setImageResource(R.drawable.update_pressed);
			break;

		case R.id.id_tab_cld:
			mViewpager.setCurrentItem(1);
			mCldImg.setImageResource(R.drawable.calendar_pressed);
			break;
		}

	}

	/**
	 * 将所有图片切换为暗色
	 */
	private void resetImg() {
		mUpdateImg.setImageResource(R.drawable.update);
		mCldImg.setImageResource(R.drawable.calendar);
	}
	
	
	/**
	 * 联网获取用户列表线程
	 * @author HE
	 *
	 */
	class NetAsyncTask extends AsyncTask<Object, Object, String> {

		@Override
		protected void onPostExecute(String result) {
			if (result.equals("success")) {
				pd.dismiss();
				//列表适配器
				SimpleAdapter simpleAdapter = new SimpleAdapter(ListAcitvity.this, listItems, R.layout.name_item, 
						new String[] {"name"}, new int[]{R.id.name});
				mListView.setAdapter(simpleAdapter);
			}else {
				Toast.makeText(ListAcitvity.this, "载入失败", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

		@Override
		protected String doInBackground(Object... params) {
			// 根据命名空间和方法得到SoapObject对象
			SoapObject soapObject = new SoapObject(targetNameSpace,
					getStudentList);
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
				httpSE.call(targetNameSpace + getStudentList, envelop);
				// 得到远程方法返回的SOAP对象
				SoapObject resultObj = (SoapObject) envelop.getResponse();
				// 得到服务器传回的数据
				int count = resultObj.getPropertyCount();
				for (int i = 0; i < count; i++) {
					Map<String,String> listItem = new HashMap<String, String>();
					listItem.put("name", resultObj.getProperty(i).toString());
					Log.d("slist", resultObj.getProperty(i).toString());
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
}
