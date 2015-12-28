/**
 * Copyright (C) 2013 Umeng, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.umeng.message.example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.common.message.UmengMessageDeviceConfig;
import com.umeng.message.ALIAS_TYPE;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.local.UmengLocalNotification;
import com.umeng.message.local.UmengNotificationBuilder;
import com.umeng.message.tag.TagManager;

public class MainActivity extends Activity {
	protected static final String TAG = MainActivity.class.getSimpleName();
	
	private EditText edTag, edAlias, edExclusiveAlias, edAliasType;
	private TextView tvStatus, infoTextView;
	private ImageView btnEnable;
	private Button btnaAddTag, btnListTag, btnAddAlias, btnAddExclusiveAlias, btnLocalNotification; 
	private ProgressDialog dialog;
	private Spinner spAliasType;
	
	private PushAgent mPushAgent;
	
	private boolean edAliasTypeFocus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		setContentView(R.layout.activity_main);
		
		printKeyValue();
		
		mPushAgent = PushAgent.getInstance(this);
//		mPushAgent.setPushCheck(true);    //默认不检查集成配置文件
//		mPushAgent.setLocalNotificationIntervalLimit(false);  //默认本地通知间隔最少是10分钟
		
		//应用程序启动统计
		//参考集成文档的1.5.1.2
		//http://dev.umeng.com/push/android/integration#1_5_1
		mPushAgent.onAppStart();
		
		//开启推送并设置注册的回调处理
 	    mPushAgent.enable(mRegisterCallback);

		PushAgent.sendSoTimeout(this, 600);	//设置护保进程间隔时间
 	    
 	    //添加本地定时通知示例
// 	addLocalNotification();
		
		tvStatus = (TextView) findViewById(R.id.tvStatus);
		btnEnable = (ImageView) findViewById(R.id.btnEnable);
		btnaAddTag = (Button) findViewById(R.id.btnAddTags);
		btnAddAlias = (Button) findViewById(R.id.btnAddAlias);
		btnAddExclusiveAlias = (Button) findViewById(R.id.btnAddExclusiveAlias);
		btnListTag = (Button) findViewById(R.id.btnListTags);
		btnLocalNotification = (Button) findViewById(R.id.btnLocalNotification);
		infoTextView = (TextView)findViewById(R.id.info);
		edTag = (EditText) findViewById(R.id.edTag);
		edAlias = (EditText) findViewById(R.id.edAlias);
		edExclusiveAlias = (EditText) findViewById(R.id.edExclusiveAlias);
		edAliasType = (EditText) findViewById(R.id.edAliasType);
		spAliasType = (Spinner) findViewById(R.id.spAliasType);
		
		edAliasType.setOnFocusChangeListener(new View.OnFocusChangeListener() {

		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		    	if(hasFocus) {
		    		edAliasTypeFocus = true;
		    	} else {
		    		edAliasTypeFocus = false;
		    	}
		    }

		});
		
		edAliasType.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if(edAliasTypeFocus) {
					spAliasType.setSelection(0);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		String[] aliasType = new String[]{"Alias Type:",ALIAS_TYPE.SINA_WEIBO,ALIAS_TYPE.BAIDU,
			ALIAS_TYPE.KAIXIN,ALIAS_TYPE.QQ,ALIAS_TYPE.RENREN,ALIAS_TYPE.TENCENT_WEIBO,
			ALIAS_TYPE.WEIXIN};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, aliasType);
		spAliasType.setAdapter(adapter);
		spAliasType.setBackgroundColor(Color.LTGRAY);
		spAliasType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				TextView tv = (TextView)arg1;
				if(tv != null) {
					//int rate = (int)(5.0f*(float) screenWidth/320.0f);
					//int textSize = rate < 15 ? 15 : rate;
					float textSize = 18.0f;
					tv.setTextSize((float)textSize);
				}
				
				if(arg2 != 0) {
					String type = (String)spAliasType.getItemAtPosition(arg2);
					edAliasType.setText(type);
				} else if(!edAliasTypeFocus) {
					edAliasType.setText("");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		tvStatus.setOnClickListener(clickListener);
		btnEnable.setOnClickListener(clickListener);
		btnaAddTag.setOnClickListener(clickListener);
		btnListTag.setOnClickListener(clickListener);
		btnAddAlias.setOnClickListener(clickListener);
		btnAddExclusiveAlias.setOnClickListener(clickListener);
		btnLocalNotification.setOnClickListener(clickListener);

		updateStatus();
		
		//此处是完全自定义处理设置
		//参考集成文档1.6.5#3
		//http://dev.umeng.com/push/android/integration#1_6_5
//		mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void printKeyValue() {
		//获取自定义参数
		Bundle bun = getIntent().getExtras();
		if (bun != null)
		{
			Set<String> keySet = bun.keySet();
			for (String key : keySet) {
				String value = bun.getString(key);
				Log.i(TAG, key + ":" + value);
			}
		}
		
	}

	private void switchPush(){		
		String info = String.format("enabled:%s  isRegistered:%s",
				mPushAgent.isEnabled(), mPushAgent.isRegistered());
		Log.i(TAG, "switch Push:" + info);

		btnEnable.setClickable(false);
		if (mPushAgent.isEnabled() || UmengRegistrar.isRegistered(MainActivity.this)) {
			//开启推送并设置注册的回调处理
			mPushAgent.disable(mUnregisterCallback);
		} else {
			//关闭推送并设置注销的回调处理
			mPushAgent.enable(mRegisterCallback);
		}
	}
	
	private void updateStatus() {
		String pkgName = getApplicationContext().getPackageName();
		String info = String.format("enabled:%s  isRegistered:%s  DeviceToken:%s " + 
				"SdkVersion:%s AppVersionCode:%s AppVersionName:%s",
				mPushAgent.isEnabled(), mPushAgent.isRegistered(),
				mPushAgent.getRegistrationId(), MsgConstant.SDK_VERSION,
				UmengMessageDeviceConfig.getAppVersionCode(this), UmengMessageDeviceConfig.getAppVersionName(this));
		tvStatus.setText("应用包名："+pkgName+"\n"+info);
		
		btnEnable.setImageResource(mPushAgent.isEnabled()?R.drawable.open_button:R.drawable.close_button);
		btnEnable.setClickable(true);
		copyToClipBoard();
		
		Log.i(TAG, "updateStatus:" + String.format("enabled:%s  isRegistered:%s",
				mPushAgent.isEnabled(), mPushAgent.isRegistered()));
		Log.i(TAG, "=============================");
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void copyToClipBoard()
	{
		if (Build.VERSION.SDK_INT<11) 
			return;
		String deviceToken = mPushAgent.getRegistrationId();
		if (!TextUtils.isEmpty(deviceToken))
		{
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(deviceToken);
			toast("DeviceToken已经复制到剪贴板了");		
		}
	}
	
	// sample code to add tags for the device / user
	private void addTag() {
		String tag = edTag.getText().toString();
		if (TextUtils.isEmpty(tag))
		{
			toast("请先输入Tag");
			return;
		}
		if (!mPushAgent.isRegistered())
		{
			toast("抱歉，还未注册");
			return;
		}

		showLoading();
		new AddTagTask(tag).execute();
		hideInputKeyboard();
	}

	// sample code to add tags for the device / user
	private void listTags() {
		if (!mPushAgent.isRegistered())
		{
			toast("抱歉，还未注册");
			return;
		}
		showLoading();
		new ListTagTask().execute();
	}
	
	private void localNotification() {
		Intent intent = new Intent(this, LocalNotificationActivity.class);
		startActivity(intent);
	}

	// sample code to add alias for the device / user
	private void addAlias() {
		String alias = edAlias.getText().toString();
		String aliasType = edAliasType.getText().toString();
		if (TextUtils.isEmpty(alias))
		{
			toast("请先输入Alias");
			return;
		}
		if (TextUtils.isEmpty(aliasType))
		{
			toast("请先输入Alias Type");
			return;
		}
		if (!mPushAgent.isRegistered())
		{
			toast("抱歉，还未注册");
			return;
		}
		showLoading();
		new AddAliasTask(alias,aliasType).execute();
		hideInputKeyboard();
	}
	
	private void addExclusiveAlias() {
		String exclusiveAlias = edExclusiveAlias.getText().toString();
		String aliasType = edAliasType.getText().toString();
		if (TextUtils.isEmpty(exclusiveAlias))
		{
			toast("请先输入Exclusive Alias");
			return;
		}
		if (TextUtils.isEmpty(aliasType))
		{
			toast("请先输入Alias Type");
			return;
		}
		if (!mPushAgent.isRegistered())
		{
			toast("抱歉，还未注册");
			return;
		}
		showLoading();
		new AddExclusiveAliasTask(exclusiveAlias,aliasType).execute();
		hideInputKeyboard();
	}

	public void showLoading(){
		if (dialog == null){
			dialog = new ProgressDialog(this);
			dialog.setMessage("Loading");
		}
		dialog.show();
	}
	
	public void updateInfo(String info){
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
		infoTextView.setText(info);
	}
	
	public OnClickListener clickListener  = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == btnaAddTag){
				addTag();
			}else if (v == btnAddAlias){
				addAlias();
			}else if (v == btnAddExclusiveAlias){
				addExclusiveAlias();
			}else if (v == btnListTag){
				listTags();
			}else if (v == btnEnable){
				switchPush();
			}else if (v == tvStatus) {
				updateStatus();
			}else if (v == btnLocalNotification) {
				localNotification();
			}
		}
	};
	
	public Handler handler = new Handler();
	
	//此处是注册的回调处理
	//参考集成文档的1.7.10
	//http://dev.umeng.com/push/android/integration#1_7_10
	public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {
		
		@Override
		public void onRegistered(String registrationId) {
			// TODO Auto-generated method stub
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					updateStatus();
				}
			});
		}
	};
	
	//此处是注销的回调处理
	//参考集成文档的1.7.10
	//http://dev.umeng.com/push/android/integration#1_7_10
	public IUmengUnregisterCallback mUnregisterCallback = new IUmengUnregisterCallback() {
		
		@Override
		public void onUnregistered(String registrationId) {
			// TODO Auto-generated method stub
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					updateStatus();
				}
			});
		}
	};
	
	private Toast mToast;
	public void toast(String str){
		if (mToast == null)
			mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		mToast.setText(str);
		mToast.show();
	}
	
	
	class AddTagTask extends AsyncTask<Void, Void, String>{

		String tagString;
		String[] tags;
		public AddTagTask(String tag) {
			// TODO Auto-generated constructor stub
			tagString = tag;
			tags = tagString.split(",");
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				TagManager.Result result = mPushAgent.getTagManager().add(tags);
				Log.d(TAG, result.toString());
				return result.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "Fail";
		}
		
		@Override
		protected void onPostExecute(String result) {
			edTag.setText("");
			updateInfo("Add Tag:\n" + result);
		}
	}
	
	class AddAliasTask extends AsyncTask<Void, Void, Boolean>{
		
		String alias;
		String aliasType;
		
		public AddAliasTask(String aliasString,String aliasTypeString) {
			// TODO Auto-generated constructor stub
			this.alias = aliasString;
			this.aliasType = aliasTypeString;
		}
		
		protected Boolean doInBackground(Void... params) {
			try {
				return mPushAgent.addAlias(alias, aliasType);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (Boolean.TRUE.equals(result)) 
				Log.i(TAG, "alias was set successfully.");
			
			edAlias.setText("");
			updateInfo("Add Alias:" + (result?"Success":"Fail"));
		}

	}
	
class AddExclusiveAliasTask extends AsyncTask<Void, Void, Boolean>{
		
		String exclusiveAlias;
		String aliasType;
		
		public AddExclusiveAliasTask(String aliasString,String aliasTypeString) {
			// TODO Auto-generated constructor stub
			this.exclusiveAlias = aliasString;
			this.aliasType = aliasTypeString;
		}
		
		protected Boolean doInBackground(Void... params) {
			try {
				return mPushAgent.addExclusiveAlias(exclusiveAlias, aliasType);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (Boolean.TRUE.equals(result)) 
				Log.i(TAG, "exclusive alias was set successfully.");
			
			edExclusiveAlias.setText("");
			updateInfo("Add Exclusive Alias:" + (result?"Success":"Fail"));
		}

	}
	
	class ListTagTask extends AsyncTask<Void , Void, List<String>>{
		@Override
		protected List<String> doInBackground(Void... params) {
			List<String> tags = new ArrayList<String>();
			try {
				tags = mPushAgent.getTagManager().list();
				Log.d(TAG, String.format("list tags: %s", TextUtils.join(",", tags)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return tags;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			if (result != null) {
				StringBuilder info = new StringBuilder();
				info.append("Tags:\n");
				for (int i=0; i<result.size(); i++){
					String tag = result.get(i);
					info.append(tag+"\n");
				}
				info.append("\n");
				updateInfo(info.toString());
			} else {
				updateInfo("");
			}
		}
	}
	
	public void hideInputKeyboard()
	{
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
		.hideSoftInputFromWindow(getCurrentFocus()
		.getWindowToken(),
		InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	//添加本地定时通知处理示例
	private void addLocalNotification() {
		//初始化通知
		UmengLocalNotification localNotification = new UmengLocalNotification();
		//设置通知开始时间
		//1.开始时间为当前时间往后1小时
		long time = System.currentTimeMillis() + 60 * 60 * 1000;
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date d=new Date(time);  
        String t=format.format(d);
        localNotification.setDateTime(t);
        //2.开始时间为特殊节日，只需要设置开始的年，如果设置了特殊节日，则1设置的时间无效
        //开始为当前年，节日为春节
        /*
        Calendar c = Calendar.getInstance();
        localNotification.setYear(c.get(Calendar.YEAR));
        localNotification.setHour(12);
        localNotification.setMinute(12);
        localNotification.setSecond(12);
        localNotification.setSpecialDay(UmengLocalNotification.CHINESE_NEW_YEAR);
        */
        //设置重复次数，默认是1
        localNotification.setRepeatingNum(100);
        //设置重复间隔，默认是1
        localNotification.setRepeatingInterval(2);        
        //设置重复单位，默认是天
        localNotification.setRepeatingUnit(UmengLocalNotification.REPEATING_UNIT_HOUR);
        
        //初始化通知样式
        UmengNotificationBuilder builder = new UmengNotificationBuilder();
        //设置小图标
        builder.setSmallIconDrawable("ic_launcher");
        //设置大图标
        builder.setLargeIconDrawable("ic_launcher");
        //设置自动清除
        builder.setFlags(Notification.FLAG_AUTO_CANCEL);
        
        localNotification.setNotificationBuilder(builder);
        
        mPushAgent.addLocalNotification(localNotification);
	}
}
