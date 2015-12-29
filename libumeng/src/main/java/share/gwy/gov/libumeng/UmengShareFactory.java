package share.gwy.gov.libumeng;

import android.app.Activity;
import android.content.Context;

import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.UMImage;

/**
 * Created by Administrator on 2015/12/28.
 */
public interface UmengShareFactory {
    /**
     * 分享到微博
     *
     *
     * @param content
     *            要分享的内容
     * @param uri
     *            要分享的地址
     * @param umImage
     *            要分享的图片
     */
    public void shareWeibo(Context context, String content, String uri,
                           UMImage umImage, SocializeListeners.SnsPostListener postListener);

    /**
     * 分享到QQ
     *
     *
     * @param content
     *            要分享的内容
     * @param uri
     *            要分享的地址
     * @param umImage
     *            要分享的图片
     */
    public void shareQicq(Context context, String title,String content, String uri,
                               UMImage umImage, SocializeListeners.SnsPostListener postListener);

    /**
     * 分享到微信
     *
     *
     * @param content
     *            要分享的内容
     * @param uri
     *            要分享的地址
     * @param umImage
     *            要分享的图片
     */
    public void shareWeiXin(Context context, String title,String content, String uri,
                            UMImage umImage, SocializeListeners.SnsPostListener postListener);

    /**
     * 分享到微信朋友圈
     *
     *
     * @param content
     *            要分享的内容
     * @param uri
     *            要分享的地址
     * @param umImage
     *            要分享的图片
     */
    public void shareWeiXinFriend(Context context, String title,String content,String uri,
                                  UMImage umImage, SocializeListeners.SnsPostListener postListener);
}
