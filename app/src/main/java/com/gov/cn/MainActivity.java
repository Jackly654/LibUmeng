package com.gov.cn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.gov.cn.entity.Article;
import com.umeng.message.PushAgent;
import com.umeng.socialize.sso.UMSsoHandler;

import com.gov.cn.ShareUtils.ShareEntity;

import javax.crypto.Mac;

import share.gwy.gov.libumeng.UmengShare;

public class MainActivity extends AppCompatActivity {
    private Button open_share;
    private UmengShare umengShare;
    private Article article;
    private ShareUtils shareUtil;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    }

    private void initPush() {
        //开启推送服务
        PushAgent mPushAgent = PushAgent.getInstance(context);
        mPushAgent.enable();
        //统计应用启动数据
        PushAgent.getInstance(this).onAppStart();
    }


    private void initShare() {
        umengShare = new UmengShare();
        open_share = (Button) findViewById(R.id.open_share);
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
}
