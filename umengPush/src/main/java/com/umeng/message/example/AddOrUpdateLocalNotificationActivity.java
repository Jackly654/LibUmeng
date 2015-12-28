package com.umeng.message.example;

import java.util.Calendar;

import com.umeng.message.PushAgent;
import com.umeng.message.local.UmengCalendar;
import com.umeng.message.local.UmengLocalNotification;
import com.umeng.message.local.UmengLocalNotificationHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddOrUpdateLocalNotificationActivity extends ActionBarActivity {
	
	private final int CREATE_LOCAL_NOTIFICATION = 1;
	private final int UPDATE_LOCAL_NOTIFICATION = 2;
	
	private UmengLocalNotification localNotification;
	
	private TextView txvYear;
	private TextView txvMonth;
	private TextView txvDay;
	private EditText edtYear;
	private EditText edtMonth;
	private EditText edtDay;
	private EditText edtHour;
	private EditText edtMinute;
	private EditText edtSecond;
	private EditText edtRepeatingNum;
	private EditText edtRepeatingInterval;
	private Spinner spRepeatingUnit;
	private Spinner spSpecialDay;
	private EditText edtTitle;
	private EditText edtContent;
	private EditText edtTicker;
	private EditText edtFlags;
	private EditText edtDefaults;
	private EditText edtSmallIconDrawable;
	private EditText edtLargeIconDrawable;
	private EditText edtSoundDrawable;
	private Spinner spPlayVibrate;
	private Spinner spPlayLights;
	private Spinner spPlaySound;
	private Spinner spScreenOn;
	private EditText edtLayoutId;
	private EditText edtLayoutTitleId;
	private EditText edtLayoutContentId;
	private EditText edtLayoutIconId;
	private EditText edtLayoutIconDrawableId;
	
	private Handler handler;
	private Context mContext;
	private boolean update;
	private int specialDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_localnotification);
        initData();
        initView();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if(update)
			menu.add(0, 1, 3, R.string.update_local_notification).setIcon(android.R.drawable.ic_menu_edit);
		else
			menu.add(0, 2, 3, R.string.create_local_notification).setIcon(android.R.drawable.ic_menu_add);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 int item_id = item.getItemId();

	     switch (item_id) {
	     	case Constants.CREATE_LOCAL_NOTIFICATION:
	     		AlertDialog.Builder builder = new AlertDialog.Builder(AddOrUpdateLocalNotificationActivity.this);
	        	builder.setMessage("确认添加本地通知吗？");
	        	builder.setTitle("提示");
	        	builder.setPositiveButton("确认", new OnClickListener() {
	        		@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						handler.post(runnable);
						dialog.dismiss();
					}
	        	});
	        	builder.setNegativeButton("取消", new OnClickListener() {
	        		@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
	        	});
	        	builder.create().show();
	     		break;
	     	case Constants.UPDATE_LOCAL_NOTIFICATION:
	     		AlertDialog.Builder builder1 = new AlertDialog.Builder(AddOrUpdateLocalNotificationActivity.this);
	        	builder1.setMessage("确认修改本地通知吗？");
	        	builder1.setTitle("提示");
	        	builder1.setPositiveButton("确认", new OnClickListener() {
	        		@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						handler.post(runnable);
						dialog.dismiss();
					}
	        	});
	        	builder1.setNegativeButton("取消", new OnClickListener() {
	        		@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
	        	});
	        	builder1.create().show();
	     		break;
	        case android.R.id.home:
	            finish();
	            break;
	    }
		return super.onOptionsItemSelected(item);
	}
	
	private void initData() {
		mContext = this;
		String local_notification_id = getIntent().getStringExtra("local_notification_id");
		if(TextUtils.isEmpty(local_notification_id)) {
			localNotification = new UmengLocalNotification();
			specialDay = 0;
		} else {
			localNotification = PushAgent.getInstance(mContext).
				findLocalNotification(local_notification_id);
			update = true;
			specialDay = localNotification.getSpecialDay();
		}
		Log.d("AddOrUpdate", "local_notification_id="+local_notification_id);
		Log.d("AddOrUpdate", "localNotification="+localNotification);
	}
	
	private void initView() {
		ActionBar bar = getSupportActionBar();
		if(update)
			bar.setTitle(R.string.modify_local_notification);
		else
			bar.setTitle(R.string.new_local_notification);
        bar.setDisplayHomeAsUpEnabled(true);
		
        txvYear = (TextView) findViewById(R.id.yearTxv);
        txvMonth = (TextView) findViewById(R.id.monthTxv);
        txvDay = (TextView) findViewById(R.id.dayTxv);
		edtYear = (EditText) findViewById(R.id.edtYear);
		edtMonth = (EditText) findViewById(R.id.edtMonth);
		edtDay = (EditText) findViewById(R.id.edtDay);
		edtHour = (EditText) findViewById(R.id.edtHour);
		edtMinute = (EditText) findViewById(R.id.edtMinute);
		edtSecond = (EditText) findViewById(R.id.edtSecond);
		edtRepeatingNum = (EditText) findViewById(R.id.edtRepeatingNum);
		edtRepeatingInterval = (EditText) findViewById(R.id.edtRepeatingInterval);
		spRepeatingUnit = (Spinner) findViewById(R.id.spRepeatingUnit);
		spSpecialDay = (Spinner) findViewById(R.id.spSpecialDay);
		edtTitle = (EditText) findViewById(R.id.edtTitle);
		edtContent = (EditText) findViewById(R.id.edtContent);
		edtTicker = (EditText) findViewById(R.id.edtTicker);
		edtFlags = (EditText) findViewById(R.id.edtFlags);
		edtDefaults = (EditText) findViewById(R.id.edtDefaults);
		edtSmallIconDrawable = (EditText) findViewById(R.id.edtSmallIconDrawable);
		edtLargeIconDrawable = (EditText) findViewById(R.id.edtLargeIconDrawable);
		edtSoundDrawable = (EditText) findViewById(R.id.edtSoundDrawable);
		spPlayVibrate = (Spinner) findViewById(R.id.spPlayVibrate);
		spPlayLights = (Spinner) findViewById(R.id.spPlayLights);
		spPlaySound = (Spinner) findViewById(R.id.spPlaySound);
		spScreenOn = (Spinner) findViewById(R.id.spScreenOn);
		edtLayoutId = (EditText) findViewById(R.id.edtLayoutId);
		edtLayoutTitleId = (EditText) findViewById(R.id.edtLayoutTitleId);
		edtLayoutContentId = (EditText) findViewById(R.id.edtLayoutContentId);
		edtLayoutIconId = (EditText) findViewById(R.id.edtLayoutIconId);
		edtLayoutIconDrawableId = (EditText) findViewById(R.id.edtLayoutIconDrawableId);
		
		edtYear.setText(String.valueOf(localNotification.getYear()));
		edtMonth.setText(String.valueOf(localNotification.getMonth()));
		edtDay.setText(String.valueOf(localNotification.getDay()));
		
		if(specialDay == 2 || specialDay == 3 || specialDay ==4 || specialDay == 7 || specialDay == 8 || specialDay == 9 || 
				specialDay == 11 || specialDay == 12) {
			String date = UmengCalendar.solarTolunar(UmengLocalNotificationHelper.getDateTime(localNotification.getYear(), localNotification.getMonth(), 
					localNotification.getDay(), localNotification.getHour(), localNotification.getMinute(), localNotification.getSecond()));
			String[] s1 = date.split(" ");
			String[] s2 = s1[0].split("-");
			edtYear.setText(s2[0]);
			edtMonth.setText(s2[1]);
			edtDay.setText(s2[2]);
			txvYear.setText(R.string.lunarYear);
			txvMonth.setText(R.string.lunarMonth);
			txvDay.setText(R.string.lunarDay);
		}
		
		edtHour.setText(String.valueOf(localNotification.getHour()));
		edtMinute.setText(String.valueOf(localNotification.getMinute()));
		edtSecond.setText(String.valueOf(localNotification.getSecond()));
		edtRepeatingNum.setText(String.valueOf(localNotification.getRepeatingNum()));
		edtRepeatingInterval.setText(String.valueOf(localNotification.getRepeatingInterval()));
		spRepeatingUnit.setSelection(localNotification.getRepeatingUnit()-1);
		spSpecialDay.setSelection(localNotification.getSpecialDay());
		edtTitle.setText(localNotification.getTitle());
		edtContent.setText(localNotification.getContent());
		edtTicker.setText(localNotification.getTicker());
		edtFlags.setText(String.valueOf(localNotification.getNotificationBuilder().getFlags()));
		edtDefaults.setText(String.valueOf(localNotification.getNotificationBuilder().getDefaults()));
		edtSmallIconDrawable.setText(localNotification.getNotificationBuilder().getSmallIconDrawable());
		edtLargeIconDrawable.setText(localNotification.getNotificationBuilder().getLargeIconDrawable());
		edtSoundDrawable.setText(localNotification.getNotificationBuilder().getSoundDrawable());
		spPlayVibrate.setSelection(localNotification.getNotificationBuilder().isPlayVibrate() ? 0 : 1);
		spPlayLights.setSelection(localNotification.getNotificationBuilder().isPlayLights() ? 0 : 1);
		spPlaySound.setSelection(localNotification.getNotificationBuilder().isPlaySound() ? 0 : 1);
		spScreenOn.setSelection(localNotification.getNotificationBuilder().isScreenOn() ? 0 : 1);
		edtLayoutId.setText(String.valueOf(localNotification.getNotificationBuilder().getLayoutId()));
		edtLayoutTitleId.setText(String.valueOf(localNotification.getNotificationBuilder().getLayoutTitleId()));
		edtLayoutContentId.setText(String.valueOf(localNotification.getNotificationBuilder().getLayoutContentId()));
		edtLayoutIconId.setText(String.valueOf(localNotification.getNotificationBuilder().getLayoutIconId()));
		edtLayoutIconDrawableId.setText(String.valueOf(localNotification.getNotificationBuilder().getLayoutIconDrawableId()));
		
		spSpecialDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(position == 0) {
					edtMonth.setEnabled(true);
					edtDay.setEnabled(true);
				} else {
					edtMonth.setEnabled(false);
					edtDay.setEnabled(false);
				}
				if(position == 2 || position == 3 || position == 4 || position == 7 || position == 8 || position == 9 || 
						position == 11 || position == 12) {
					txvYear.setText(R.string.lunarYear);
					txvMonth.setText(R.string.lunarMonth);
					txvDay.setText(R.string.lunarDay);
				} else {
					txvYear.setText(R.string.year);
					txvMonth.setText(R.string.month);
					txvDay.setText(R.string.day);
				}
				if(position == specialDay)
					return;
				String sInfo=adapter.getItemAtPosition(position).toString();
				if(!TextUtils.isEmpty(sInfo))
					Toast.makeText(mContext, "设置了节日"+sInfo, Toast.LENGTH_LONG).show();
				resetDateTimeView(position);
				specialDay = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// TODO Auto-generated method stub
				
			}
		
		});
		
		handler = new Handler() {
        	@Override
        	public void handleMessage(Message msg) {
        		switch(msg.what) {
        			case CREATE_LOCAL_NOTIFICATION:
        			case UPDATE_LOCAL_NOTIFICATION:
        				Intent intent = new Intent(mContext, LocalNotificationActivity.class);
        				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        				mContext.startActivity(intent);
        				finish();
        				break;
        		}
        	}
        };
	}
	
	private boolean checkInput() {
		if(TextUtils.isEmpty(edtYear.getText().toString().trim()))
			return false;
		if(TextUtils.isEmpty(edtMonth.getText().toString().trim()))
			return false;
		if(TextUtils.isEmpty(edtDay.getText().toString().trim()))
			return false;
		if(TextUtils.isEmpty(edtHour.getText().toString().trim()))
			return false;
		if(TextUtils.isEmpty(edtMinute.getText().toString().trim()))
			return false;
		if(TextUtils.isEmpty(edtSecond.getText().toString().trim()))
			return false;
		if(TextUtils.isEmpty(edtRepeatingNum.getText().toString().trim()))
			return false;
		if(TextUtils.isEmpty(edtRepeatingInterval.getText().toString().trim()))
			return false;
		if(TextUtils.isEmpty(edtTitle.getText().toString().trim()))
			return false;
		if(TextUtils.isEmpty(edtContent.getText().toString().trim()))
			return false;
		return true;
	}
	
	private void resetDateTimeView(int position) {
		Log.d("AddOrUpdate", "pos="+String.valueOf(position));
		if(position == 0)
			return;
		try {
			long time = 0;
			long curTime = System.currentTimeMillis();
			int year = Integer.parseInt(edtYear.getText().toString());
			int month =  Integer.parseInt(edtMonth.getText().toString());;
			int day =  Integer.parseInt(edtDay.getText().toString());
			int hour = Integer.parseInt(edtHour.getText().toString());
			int minute = Integer.parseInt(edtMinute.getText().toString());
			int second = Integer.parseInt(edtSecond.getText().toString());
			String dateTimeStr = "";
			switch(position) {
				case UmengLocalNotification.NEW_YEAR_DAY:
					while(true) {
						dateTimeStr = year + "-01-01 ";
						dateTimeStr += (hour >= 10 ? hour : ("0" + hour)) + ":";
						dateTimeStr += (minute >= 10 ? minute : ("0" + minute)) + ":";
						dateTimeStr += second >= 10 ? second : ("0" + second);
						time = UmengLocalNotificationHelper.getTimeFromDate(dateTimeStr);
						if(time > curTime)
							break;
						year++;
					}
					month = 1;
					day = 1;
					break;
				case UmengLocalNotification.CHINESE_NEW_YEAR_EVE:
					while(true) {
						day = UmengCalendar.iGetLMonthDays(year, 12);
						time = UmengLocalNotificationHelper.getTimeFromDate(UmengCalendar.lunarTosolar(UmengLocalNotificationHelper.getDateTime(year, 12, day,
								hour, minute, second)));
						if(time > curTime)
							break;
						year++;
					}
					month = 12;
					break;
				case UmengLocalNotification.CHINESE_NEW_YEAR:
					while(true) {
						time = UmengLocalNotificationHelper.getTimeFromDate(UmengCalendar.lunarTosolar(UmengLocalNotificationHelper.getDateTime(year, 1, 1,
								hour, minute, second)));
						if(time > curTime)
							break;
						year++;
					}
					month = 1;
					day = 1;
					break;
				case UmengLocalNotification.LANTERN:
					while(true) {
						time = UmengLocalNotificationHelper.getTimeFromDate(UmengCalendar.lunarTosolar(UmengLocalNotificationHelper.getDateTime(year, 1, 15,
								hour, minute, second)));
						if(time > curTime)
							break;
						year++;
					}
					month = 1;
					day = 15;
					break;
				case UmengLocalNotification.QINGMING_FESTIVAL:
					while(true) {
						time = UmengLocalNotificationHelper.getQingMingTime(year, hour, minute, second);
						if(time > curTime)
							break;
						year++;
					}
					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(time);
					month = c.get(Calendar.MONTH)+1;
					day = c.get(Calendar.DAY_OF_MONTH);
					break;
				case UmengLocalNotification.LABOR_DAY:
					while(true) {
						dateTimeStr = year + "-05-01 ";
						dateTimeStr += (hour >= 10 ? hour : ("0" + hour)) + ":";
						dateTimeStr += (minute >= 10 ? minute : ("0" + minute)) + ":";
						dateTimeStr += second >= 10 ? second : ("0" + second);
						time = UmengLocalNotificationHelper.getTimeFromDate(dateTimeStr);
						if(time > curTime)
							break;
						year++;
					}
					month = 5;
					day = 1;
					break;
				case UmengLocalNotification.DRAGON_BOAT_FESTIVAL:
					while(true) {
						time = UmengLocalNotificationHelper.getTimeFromDate(UmengCalendar.lunarTosolar(UmengLocalNotificationHelper.getDateTime(year, 5, 5, 
								hour, minute, second)));
						if(time > curTime)
							break;
						year++;
					}
					month = 5;
					day = 5;
					break;
				case UmengLocalNotification.QIXI_FESTIVAL:
					while(true) {
						time = UmengLocalNotificationHelper.getTimeFromDate(UmengCalendar.lunarTosolar(UmengLocalNotificationHelper.getDateTime(year, 7, 7,
								hour, minute, second)));
						if(time > curTime)
							break;
						year++;
					}
					month = 7;
					day = 7;
					break;
				case UmengLocalNotification.MID_AUTUMN_FESTIVAL:
					while(true) {
						time = UmengLocalNotificationHelper.getTimeFromDate(UmengCalendar.lunarTosolar(UmengLocalNotificationHelper.getDateTime(year, 8, 15, 
								hour, minute, second)));
						if(time > curTime)
							break;
						year++;
					}
					month = 8;
					day = 15;
					break;
				case UmengLocalNotification.NATIONAL_DAY:
					while(true) {
						dateTimeStr = year + "-10-01 ";
						dateTimeStr += (hour >= 10 ? hour : ("0" + hour)) + ":";
						dateTimeStr += (minute >= 10 ? minute : ("0" + minute)) + ":";
						dateTimeStr += second >= 10 ? second : ("0" + second);
						time = UmengLocalNotificationHelper.getTimeFromDate(dateTimeStr);
						if(time > curTime)
							break;
						year++;
					}
					month = 10;
					day = 1;
					break;
				case UmengLocalNotification.CHUNG_YEUNG_FESTIVAL:
					while(true) {
						time = UmengLocalNotificationHelper.getTimeFromDate(UmengCalendar.lunarTosolar(UmengLocalNotificationHelper.getDateTime(year, 9, 9, 
								hour, minute, second)));
						if(time > curTime)
							break;
						year++;
					}
					month = 9;
					day = 9;
					break;
				case UmengLocalNotification.LABA_FESTIVAL:
					while(true) {
						time = UmengLocalNotificationHelper.getTimeFromDate(UmengCalendar.lunarTosolar(UmengLocalNotificationHelper.getDateTime(year, 12, 8, 
								hour, minute, second)));
						if(time > curTime)
							break;
						year++;
					}
					month = 12;
					day = 8;
					break;
			}
			
			edtYear.setText(String.valueOf(year));
			edtMonth.setText(String.valueOf(month));
			edtDay.setText(String.valueOf(day));
			edtHour.setText(String.valueOf(hour));
			edtMinute.setText(String.valueOf(minute));
			edtSecond.setText(String.valueOf(second));
			spRepeatingUnit.setSelection(0);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(!checkInput()) {
				Toast.makeText(mContext, "输入数据无效", Toast.LENGTH_LONG).show();
				return;
			}
			try {
				localNotification.setYear(Integer.parseInt(edtYear.getText().toString().trim()));
				localNotification.setMonth(Integer.parseInt(edtMonth.getText().toString().trim()));
				localNotification.setDay(Integer.parseInt(edtDay.getText().toString().trim()));
				localNotification.setHour(Integer.parseInt(edtHour.getText().toString().trim()));
				localNotification.setMinute(Integer.parseInt(edtMinute.getText().toString().trim()));
				localNotification.setSecond(Integer.parseInt(edtSecond.getText().toString().trim()));
				localNotification.setRepeatingNum(Integer.parseInt(edtRepeatingNum.getText().toString().trim()));
				localNotification.setRepeatingInterval(Integer.parseInt(edtRepeatingInterval.getText().toString().trim()));
				localNotification.setRepeatingUnit(spRepeatingUnit.getSelectedItemPosition()+1);
				localNotification.setSpecialDay(spSpecialDay.getSelectedItemPosition());
				localNotification.setTitle(edtTitle.getText().toString());
				localNotification.setContent(edtContent.getText().toString());
				localNotification.setTicker(edtTicker.getText().toString());
				localNotification.getNotificationBuilder().setFlags(Integer.parseInt(edtFlags.getText().toString().trim()));
				localNotification.getNotificationBuilder().setDefaults(Integer.parseInt(edtDefaults.getText().toString().trim()));
				localNotification.getNotificationBuilder().setSmallIconDrawable(edtSmallIconDrawable.getText().toString().trim());
				localNotification.getNotificationBuilder().setLargeIconDrawable(edtLargeIconDrawable.getText().toString().trim());
				localNotification.getNotificationBuilder().setSoundDrawable(edtSoundDrawable.getText().toString().trim());
				localNotification.getNotificationBuilder().setPlayVibrate(spPlayVibrate.getSelectedItemPosition() == 0 ? true : false);
				localNotification.getNotificationBuilder().setPlayLights(spPlayLights.getSelectedItemPosition() == 0 ? true : false);
				localNotification.getNotificationBuilder().setPlaySound(spPlaySound.getSelectedItemPosition() == 0 ? true : false);
				localNotification.getNotificationBuilder().setScreenOn(spScreenOn.getSelectedItemPosition() == 0 ? true : false);
				localNotification.getNotificationBuilder().setLayoutId(Integer.parseInt(edtLayoutId.getText().toString().trim()));
				localNotification.getNotificationBuilder().setLayoutTitleId(Integer.parseInt(edtLayoutTitleId.getText().toString().trim()));
				localNotification.getNotificationBuilder().setLayoutContentId(Integer.parseInt(edtLayoutContentId.getText().toString().trim()));
				localNotification.getNotificationBuilder().setLayoutIconId(Integer.parseInt(edtLayoutIconId.getText().toString().trim()));
				localNotification.getNotificationBuilder().setLayoutIconDrawableId(Integer.parseInt(edtLayoutIconDrawableId.getText().toString().trim()));
				
				boolean success = true;
				if(update)
					success = PushAgent.getInstance(mContext).updateLocalNotification(localNotification);
				else
					success = PushAgent.getInstance(mContext).addLocalNotification(localNotification);
				
				if(!success) {
					if(update)
						Toast.makeText(mContext, "修改本地通知失败，请检查你的设置", Toast.LENGTH_LONG).show();
					else
						Toast.makeText(mContext, "新建本地通知失败，请检查你的设置", Toast.LENGTH_LONG).show();
					return;
				}
				
				Message msg = new Message();
				if(update)
					msg.what = UPDATE_LOCAL_NOTIFICATION;
				else
					msg.what = CREATE_LOCAL_NOTIFICATION;
				handler.sendMessage(msg);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	};
	
}
