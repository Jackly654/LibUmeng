package com.gov.cn.application;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.gov.cn.ui.ArticleDetailActivity;
import com.gov.cn.utils.ImageUtils;
import com.gov.cn.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.io.File;

/**
 * Created by Administrator on 2015/12/28.
 */
public class GovApplication extends Application {

    private PushAgent mPushAgent;

    @Override
    public void onCreate() {
        super.onCreate();

        //测试图片需要
        initUIL();

        mPushAgent = PushAgent.getInstance(this);
        //调试模式
        mPushAgent.setDebugMode(true);

        /**
         * 负责处理消息的点击事件
         * (该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK)
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            private boolean dealDetail(Context context, UMessage msg){
                //处理推送发来的测试数据
                if(msg!=null&&msg.extra!=null){
                    Log.d("push", "msg.extra:" + msg.extra);
                    final String ColumnId = msg.extra.get("ColumnId");
                    final String ArticlePath = msg.extra.get("ArticlePath");
                    if(!TextUtils.isEmpty(ArticlePath)){
                        Runnable task=new Runnable() {

                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), ArticleDetailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("ColumnId", ColumnId);
                                intent.putExtra("ArticlePath", ArticlePath);
                                startActivity(intent);
                            }
                        };
                        task.run();
                    }
                    //根据需求处理代码逻辑
                }else{
                    Intent intent = new Intent(getApplicationContext(), ArticleDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                return false;
            }


            @Override
            public void dealWithCustomAction(Context arg0, UMessage msg) {
                if(dealDetail(arg0, msg)){
                    return;
                }
                super.dealWithCustomAction(arg0, msg);
            }
            @Override
            public void launchApp(Context arg0, UMessage msg) {
                if(dealDetail(arg0, msg)){
                    return;
                }
                super.launchApp(arg0, msg);
            }
            @Override
            public void openUrl(Context arg0, UMessage msg) {
                if(dealDetail(arg0, msg)){
                    return;
                }
                super.openUrl(arg0, msg);
            }
            @Override
            public void openActivity(Context arg0, UMessage msg) {
                if(dealDetail(arg0, msg)){
                    return;
                }
                super.openActivity(arg0, msg);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        /**************************负责处理消息，包括通知消息和自定义消息************************/

        UmengMessageHandler messageHandler = new UmengMessageHandler(){
            /**
             * 参考集成文档的1.6.3
             * http://dev.umeng.com/push/android/integration#1_6_3
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler(getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        UTrack.getInstance(getApplicationContext())
                                .trackMsgClick(msg);
                        // Toast.makeText(context, msg.custom,
                        // Toast.LENGTH_LONG).show();
                        Log.d("content", msg.extra.toString());
                        //
                        //path为null，证明意图是打开应用，不是进详情
//						if(TextUtils.isEmpty(save.path)){
                    }
                });
            }

            /**
             * 通知
             * (参考集成文档的1.6.4
             * http://dev.umeng.com/push/android/integration#1_6_4)
             * */
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                switch (msg.builder_id) {
                    //TODO  自定义通知样式
                   /* case 1:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView);
                        builder.setContentTitle(msg.title)
                                .setContentText(msg.text)
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);
                        Notification mNotification = builder.build();
                        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        return mNotification;*/
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);
    }

    private void initUIL() {
        ImageUtils.getInstance().init(
                new ImageLoaderConfiguration.Builder(this)
                        .defaultDisplayImageOptions(
                                new DisplayImageOptions.Builder()
                                        .showImageOnLoading(R.drawable.icon)
                                        .showImageForEmptyUri(R.drawable.icon)
                                        .cacheInMemory(true)
                                        .cacheOnDisc(true)
                                        .considerExifParams(true)
                                        .resetViewBeforeLoading(true)
                                        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                                        .bitmapConfig(Bitmap.Config.RGB_565)
                                        .build()
                        )
                        .writeDebugLogs()
                        .discCache(new UnlimitedDiscCache(new File(Environment
                                .getExternalStorageDirectory() + "/com.gov.cn"+"/images")))
                        .build()
        );
    }
}
