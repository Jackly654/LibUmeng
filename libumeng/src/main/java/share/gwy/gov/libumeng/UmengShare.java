package share.gwy.gov.libumeng;

import android.app.Activity;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * Created by Administrator on 2015/12/28.
 */
public class UmengShare implements UmengShareFactory{

    private final UMSocialService mController = UMServiceFactory
            .getUMSocialService(UmengCons.SHARE_URL);
    private Activity activity;

    private UmengShare() {
    }

    public UmengShare(Activity activity) {
        this.activity = activity;
        configPlatforms();
    }

    public UMSocialService getController() {
        return mController;
    }
    /**
     * 配置分享平台参数
     */
    private void configPlatforms() {

            // 添加新浪SSO授权
            mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        // 添加人人网SSO授权
//        RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(activity,
//                UmengCons.Renren_ID, UmengCons.Renren_APPKEY,
//                UmengCons.Renren_Secretkey);
//        mController.getConfig().setSsoHandler(renrenSsoHandler);

            // 添加腾讯微博SSO授权
            mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
            // 添加QQ支持, 并且设置QQ分享内容的target url
            UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity,
                    UmengCons.QZONE_APPID, UmengCons.QZONE_APPKEY);
            qqSsoHandler.addToSocialSDK();
            // 添加QZone平台
            QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, UmengCons.QZONE_APPID, UmengCons.QZONE_APPKEY);
            qZoneSsoHandler.addToSocialSDK();
            // 注意：在微信授权的时候，必须传递appSecret
            // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
            // 添加微信平台
            UMWXHandler wxHandler = new UMWXHandler(activity, UmengCons.WEIXIN_APPID, UmengCons.WEIXIN_SECRET);
            wxHandler.addToSocialSDK();
            // 支持微信朋友圈
            UMWXHandler wxCircleHandler = new UMWXHandler(activity, UmengCons.WEIXIN_APPID, UmengCons.WEIXIN_SECRET);
            wxCircleHandler.setToCircle(true);
            wxCircleHandler.addToSocialSDK();


            mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                    SHARE_MEDIA.QQ,  SHARE_MEDIA.SINA
            );
    }

    /**
     * 弹出底部分享框
     */
    public void openShare(String shareTitle, String shareContent, String shareTargetUrl,String umImage) {
        openShare(shareTitle, shareContent, shareTargetUrl, umImage, null);
    }

    /**
     * 弹出底部分享框
     */
    public void openShare(String shareTitle, String shareContent, String shareTargetUrl,String umImage, SocializeListeners.SnsPostListener listener) {

        /**--------------定义分享内容-----------------*/
        UMImage localImage = new UMImage(activity,umImage);
        /**--------------微信分享--------------*/
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(shareContent);
        weixinContent.setTitle(shareTitle);
        weixinContent.setTargetUrl(shareTargetUrl);
        weixinContent.setShareMedia(localImage);
        mController.setShareMedia(weixinContent);

        /**-------------朋友圈分享--------------*/
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(shareContent);
        circleMedia.setTitle(shareTitle+shareContent);
        circleMedia.setTargetUrl(shareTargetUrl);

        circleMedia.setShareMedia(localImage);

        mController.setShareMedia(circleMedia);



        /**-------------QQ空间分享--------------*/
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(shareContent);
        qzone.setTargetUrl(shareTargetUrl);
        qzone.setTitle(shareTitle);

        qzone.setShareMedia(localImage);
        // qzone.setShareMedia(uMusic);
        mController.setShareMedia(qzone);


        /**-------------QQ好友分享--------------*/
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(shareContent);
        qqShareContent.setTitle(shareTitle);
        qqShareContent.setShareMedia(localImage);
        qqShareContent.setTargetUrl(shareTargetUrl);
        mController.setShareMedia(qqShareContent);


        /**-------------新浪微博分享--------------*/
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(shareTitle + shareContent +shareTargetUrl);
//        sinaContent.setTitle(shareTitle);
        sinaContent.setShareMedia(localImage);
//        sinaContent.setTargetUrl(shareTargetUrl);
        mController.setShareMedia(sinaContent);


        if (listener != null) {
            mController.openShare(activity, listener);
        } else {
            mController.openShare(activity, false);
        }


    }


    @Override
    public void shareWeibo(Activity activity, String content, String uri, UMImage umImage, SocializeListeners.SnsPostListener postListener) {

    }

    @Override
    public void shareQicq(Activity activity, String content, String uri, UMImage umImage, SocializeListeners.SnsPostListener postListener) {

    }

    @Override
    public void shareWeiXin(Activity activity, String title,String content, String uri, UMImage umImage, SocializeListeners.SnsPostListener postListener) {
        /**
         * 初始化SDK，添加一些平台
         */
        UMWXHandler wxHandler = new UMWXHandler(activity, UmengCons.WEIXIN_APPID, UmengCons.WEIXIN_SECRET);
        wxHandler.addToSocialSDK();
        wxHandler.showCompressToast(false);

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent
                .setShareContent(content);
        if(title!=null) {
            weixinContent.setTitle(title);
        }else{
            weixinContent.setTitle("微信");
        }
        weixinContent.setTargetUrl(uri);
        weixinContent.setShareMedia(umImage);
        mController.setShareMedia(weixinContent);
        mController.directShare(activity, SHARE_MEDIA.WEIXIN,
                postListener);
    }

    @Override
    public void shareWeiXinFriend(Activity activity, String content, String uri, UMImage umImage, SocializeListeners.SnsPostListener postListener) {

    }
}
