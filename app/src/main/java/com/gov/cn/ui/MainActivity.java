package com.gov.cn.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.gov.cn.R;
import com.gov.cn.entity.Article;
import com.gov.cn.utils.ShareUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.socialize.sso.UMSsoHandler;

import com.gov.cn.utils.ShareUtils.ShareEntity;

import share.gwy.gov.libumeng.UmengShare;

public class MainActivity extends AppCompatActivity {
    private Button open_share,analytics;
    private UmengShare umengShare;
    private Article article;
    private ShareUtils shareUtil;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO 集成测试服务
        MobclickAgent.setDebugMode( true );
        //TODO 关闭自统计
        MobclickAgent.openActivityDurationTrack(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        initShare();
        initPush();

        Log.i("DeviceInfo",getDeviceInfo(MainActivity.this));

    }

    /**
     * 集成测试设备信息
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context) {
        try{
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if( TextUtils.isEmpty(device_id) ){
                device_id = mac;
            }

            if( TextUtils.isEmpty(device_id) ){
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void initPush() {
        //开启推送服务
        PushAgent mPushAgent = PushAgent.getInstance(context);
        mPushAgent.enable();
        //统计应用启动数据
        PushAgent.getInstance(this).onAppStart();
    }


    private void initShare() {
        //友盟分享
        umengShare = new UmengShare();

        open_share = (Button) findViewById(R.id.open_share);
        analytics = (Button) findViewById(R.id.analytics);

        context = MainActivity.this;
        shareUtil = new ShareUtils(context);
        open_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // umengShare.openShare("友盟社会化分享组件-朋友圈","来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-朋友圈。http://www.umeng.com/social","http://www.umeng.com/social","http://www.umeng.com/images/pic/social/integrated_3.png",null);
                ShareEntity shareEntity = new ShareEntity();
                // TODO 假数据
                article = new Article();
                article.title = "友盟社会化分享组件-朋友圈";
                article.summary = "来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-朋友圈。";
                article.shareUrl = "http://www.umeng.com/social";

                shareEntity.setArticle(article);


                shareUtil.show(article);
            }
        });

        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, WebActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//TODO 分享SSO
        UMSsoHandler ssoHandler = umengShare.getController().getConfig().getSsoHandler(
                requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
            Log.d("", "#### ssoHandler.authorizeCallBack");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }
}
