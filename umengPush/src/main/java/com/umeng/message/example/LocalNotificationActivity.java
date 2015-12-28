package com.umeng.message.example;

import java.util.List;

import com.umeng.message.PushAgent;
import com.umeng.message.local.UmengLocalNotification;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LocalNotificationActivity extends ActionBarActivity {
	
	private final int GET_LOCAL_NOTIFICATIONS = 1;
	private final int CLEAR_LOCAL_NOTIFICATIONS = 2;
	private final int DELETE_LOCAL_NOTIFICATION = 3;
	private final int REFRESH_LOCAL_NOTIFICATIONS = 4;
	private List<UmengLocalNotification> localNotifications;
	private Handler handler;
	private Context mContext;
	
	private ListView mListView1;
	private MyAdapter myAdapter;
	
	private ImageView ivDeleteText;
	private EditText etSearch;
	private Button btnSearch;
	
	private int mCurPos;
	private String searchText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_localnotification);
		ActionBar bar = getSupportActionBar();
		bar.setTitle(R.string.local_notification);
        bar.setDisplayHomeAsUpEnabled(true);
        
        mContext = this;

		mListView1 = (ListView) findViewById(R.id.lv);
		mListView1.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View view,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				menu.setHeaderTitle("Update/Delete");       
                menu.add(0, 1, 1, "Update Local Notification");
                menu.add(0, 5, 2, "Delete Local Notification");
			}       
		});
		
		ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
        etSearch = (EditText) findViewById(R.id.etSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        
        ivDeleteText.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				etSearch.setText("");
				handler.post(runnable5);
			}
		});
        
        etSearch.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					ivDeleteText.setVisibility(View.GONE);
				} else {
					ivDeleteText.setVisibility(View.VISIBLE);
				}
			}
		});
        
        btnSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text = etSearch.getText().toString().trim();
				if(TextUtils.isEmpty(text)) {
					Toast.makeText(mContext, "查找内容不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				searchText = text;
				handler.post(runnable4);
			}
		});
        
        handler = new Handler() {
        	@Override
        	public void handleMessage(Message msg) {
        		switch(msg.what) {
        			case GET_LOCAL_NOTIFICATIONS:
        				myAdapter = new MyAdapter(mContext, R.layout.localnotification_item);
        				myAdapter.setLocalNotifications(localNotifications);
        				mListView1.setAdapter(myAdapter);
        				break;
        			case CLEAR_LOCAL_NOTIFICATIONS:
        			case DELETE_LOCAL_NOTIFICATION:
        			case REFRESH_LOCAL_NOTIFICATIONS:
        				myAdapter.setLocalNotifications(localNotifications);
        				mListView1.setAdapter(myAdapter);
        				mListView1.invalidate();
        				break;
        		}
        	}
        };
        
        handler.post(runnable);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 3, 3, R.string.add_local_notification).setIcon(android.R.drawable.ic_menu_add);
        menu.add(0, 4, 4, R.string.clear_local_notification).setIcon(android.R.drawable.ic_menu_delete);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 int item_id = item.getItemId();

	     switch (item_id) {
	     	case Constants.ADD_LOCAL_NOTIFICATION:
	     		Intent intent = new Intent(mContext, AddOrUpdateLocalNotificationActivity.class);
	     		mContext.startActivity(intent);
	     		break;
	        case Constants.CLEAR_LOCAL_NOTIFICATION:
	        	AlertDialog.Builder builder = new AlertDialog.Builder(LocalNotificationActivity.this);
	        	builder.setMessage("确认清除本地通知吗？");
	        	builder.setTitle("提示");
	        	builder.setPositiveButton("确认", new OnClickListener() {
	        		@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						handler.post(runnable1);
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
	        case android.R.id.home:
	            finish();
	            break;
	    }
		return super.onOptionsItemSelected(item);
	}
	
	@Override       
    public boolean onContextItemSelected(MenuItem aItem) {       
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem.getMenuInfo(); 
		mCurPos = menuInfo.position;
		
         /* Switch on the ID of the item, to get what the user selected. */       
         switch (aItem.getItemId()) {       
         	case Constants.UPDATE_LOCAL_NOTIFICATION: 
         		Intent intent = new Intent(mContext, AddOrUpdateLocalNotificationActivity.class);
         		String local_notification_id = localNotifications.get(mCurPos).getId();
         		UmengLocalNotification localNotification = PushAgent.getInstance(mContext).findLocalNotification(local_notification_id);
         		if(localNotification == null) {
         			Toast.makeText(mContext, "此本地通知过期了，已删除", Toast.LENGTH_LONG).show();
         			handler.post(runnable3);
         		} else {
         			intent.putExtra("local_notification_id", local_notification_id);
         			mContext.startActivity(intent);
         		}
         		break;
            case Constants.DELETE_LOCAL_NOTIFICATION:
            	AlertDialog.Builder builder = new AlertDialog.Builder(LocalNotificationActivity.this);
  	        	builder.setMessage("确认删除本地通知吗？");
  	        	builder.setTitle("提示");
  	        	builder.setPositiveButton("确认", new OnClickListener() {
  	        		@Override
  					public void onClick(DialogInterface dialog, int which) {
  						// TODO Auto-generated method stub
  						handler.post(runnable2);
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
         }       
         return false;       
    }  
	
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			localNotifications = PushAgent.getInstance(mContext).findAllLocalNotifications();
			Message msg = Message.obtain();
			msg.what = GET_LOCAL_NOTIFICATIONS;
			handler.sendMessage(msg);
		}
		
	};
	
	Runnable runnable1 = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			PushAgent.getInstance(mContext).clearLocalNotifications();
			localNotifications = PushAgent.getInstance(mContext).findAllLocalNotifications();
			Message msg = Message.obtain();
			msg.what = CLEAR_LOCAL_NOTIFICATIONS;
			handler.sendMessage(msg);
		}
		
	};
	
	Runnable runnable2 = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String local_notification_id = localNotifications.get(mCurPos).getId();
			PushAgent.getInstance(mContext).deleteLocalNotification(local_notification_id);
			localNotifications.remove(mCurPos);
			Message msg = Message.obtain();
			msg.what = DELETE_LOCAL_NOTIFICATION;
			handler.sendMessage(msg);
		}
		
	};
	
	Runnable runnable3 = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			localNotifications.remove(mCurPos);
			Message msg = Message.obtain();
			msg.what = REFRESH_LOCAL_NOTIFICATIONS;
			handler.sendMessage(msg);
		}
		
	};
	
	Runnable runnable4 = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			localNotifications.clear();
			UmengLocalNotification localNotification = PushAgent.getInstance(mContext).findLocalNotification(searchText);
			if(localNotification != null) {
				localNotifications.add(localNotification);
			} else {
				List<UmengLocalNotification> list = PushAgent.getInstance(mContext).findLocalNotifications(searchText);
				if(list != null && !list.isEmpty()) {
					for(UmengLocalNotification ln : list)
						localNotifications.add(ln);
				}
			}
			Message msg = Message.obtain();
			msg.what = REFRESH_LOCAL_NOTIFICATIONS;
			handler.sendMessage(msg);
			searchText = "";
		}
		
	};
	
	Runnable runnable5 = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			localNotifications.clear();
			List<UmengLocalNotification> list = PushAgent.getInstance(mContext).findAllLocalNotifications();
			if(list != null && !list.isEmpty()) {
				for(UmengLocalNotification ln : list)
					localNotifications.add(ln);
			}
			Message msg = Message.obtain();
			msg.what = REFRESH_LOCAL_NOTIFICATIONS;
			handler.sendMessage(msg);
			searchText = "";
		}
		
	};
	
	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			//当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
			return false;
		}
	}
	
	class MyAdapter extends BaseAdapter {
		private List<UmengLocalNotification> localNotifications;
		private int id_row_layout;
		private LayoutInflater mInflater;

		public MyAdapter(Context context, int id_row_layout) {
			super();
			this.id_row_layout = id_row_layout;
			mInflater = LayoutInflater.from(context);

		}
		
		public void setLocalNotifications(List<UmengLocalNotification> localNotifications) {
			this.localNotifications = localNotifications;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return localNotifications != null ? localNotifications.size() : 0;
		}

		@Override
		public Object getItem(int idx) {
			// TODO Auto-generated method stub
			return localNotifications != null ? localNotifications.get(idx) : null;
		}

		@Override
		public long getItemId(int idx) {
			// TODO Auto-generated method stub
			return localNotifications != null ? localNotifications.get(idx).getId().hashCode() : 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parentView) {
			ViewHolder holder = null;
			if (convertView == null) {
				synchronized (LocalNotificationActivity.this) {
					convertView = mInflater.inflate(id_row_layout, null);
					holder = new ViewHolder();

					holder.txt1 = (TextView) convertView
							.findViewById(R.id.Title);
					holder.txt2 = (TextView) convertView
							.findViewById(R.id.Content);
					holder.txt3 = (TextView) convertView
							.findViewById(R.id.Datetime);

					convertView.setTag(holder);
				}
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			int[] colors = { Color.WHITE, Color.rgb(219, 238, 244) };//RGB颜色		   
			convertView.setBackgroundColor(colors[position % 2]);// 每隔item之间颜色不同
			
			if(localNotifications != null) {
				holder.txt1.setText(localNotifications.get(position).getTitle());
				holder.txt2.setText(localNotifications.get(position).getContent());
				holder.txt3.setText(localNotifications.get(position).getDateTime());
			}

			return convertView;
		}

		class ViewHolder {
			TextView txt1;
			TextView txt2;
			TextView txt3;
		}
	}

}
