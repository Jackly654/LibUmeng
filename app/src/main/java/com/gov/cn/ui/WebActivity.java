package com.gov.cn.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.gov.cn.R;
import com.umeng.analytics.MobclickAgentJSInterface;

/**
 * Created by Administrator on 2015/12/30.
 */
public class WebActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umeng_analytics_webview);

        WebView webview = (WebView) findViewById(R.id.webview);
        new MobclickAgentJSInterface(this, webview, new WebChromeClient());
        webview.loadUrl("file:///android_asset/demo.html");
    }
}
