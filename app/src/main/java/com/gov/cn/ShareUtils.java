package com.gov.cn;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;


import java.util.HashMap;

import share.gwy.gov.libumeng.UmengShare;

import static com.umeng.socialize.bean.SHARE_MEDIA.EMAIL;
import static com.umeng.socialize.bean.SHARE_MEDIA.FACEBOOK;
import static com.umeng.socialize.bean.SHARE_MEDIA.SINA;
import static com.umeng.socialize.bean.SHARE_MEDIA.SMS;
import static com.umeng.socialize.bean.SHARE_MEDIA.TWITTER;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN_CIRCLE;

public class ShareUtils implements OnClickListener, SnsPostListener{
	private View shareContainer, shareLayout, btCancel,
			btWeibo, btWeichat, btWeichatMoment,btQicq;
	private WindowManager wm;
	private WindowManager.LayoutParams wmParams;
	private Context context;
	private Activity activity;
	private UmengShare umengShare;
	private Animation animPullUp, animPullDown;
	private final int Hide_None = 0;
	private final int Hide_Showing = 1;
	private final int Show_None = 2;
	private final int Show_Hiding = 3;
	private int state = Hide_None;
	private ShareEntity shareEntity;
	private AnalysisUtils analysisUtils;

	public void setShareEntity(ShareEntity shareEntity) {
		this.shareEntity = shareEntity;
	}

	public ShareEntity getShareEntity() {
		if (shareEntity == null) {
			shareEntity = new ShareEntity();
		}
		return shareEntity;
	}

	public static class ShareEntity {
		private String title;
		private String content;
		private Article article;

		public Article getArticle() {
			return article;
		}

		public void setArticle(Article article) {
			this.article = article;
		}

		public String getTitle() {
			if (TextUtils.isEmpty(title)) {
				return "";
			}
			return title;
		}

		public String getContent() {
			if (TextUtils.isEmpty(content)) {
				return "";
			}
			return content;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}

	public ShareUtils(Context context) {
		this.context = context;
		findViews();
		registerEvents();
		init();
	}

	public ShareUtils(Activity activity) {
		this.activity = activity;
		findViews();
		registerEvents();
		init();
	}

	private void findViews() {
		shareContainer = View.inflate(context, R.layout.layout_share, null);
		shareLayout = shareContainer.findViewById(R.id.shareLayout);
		btWeibo = shareContainer.findViewById(R.id.btWeibo);
		btWeichat = shareContainer.findViewById(R.id.btWeichat);
		btWeichatMoment = shareContainer.findViewById(R.id.btWeichatMoment);
		btQicq = shareContainer.findViewById(R.id.btQicq);
		btCancel = shareContainer.findViewById(R.id.btCancel);
	}

	@SuppressLint("ClickableViewAccessibility")
	private void registerEvents() {
		shareContainer.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				cancel();
				return true;
			}
		});
		shareLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		btWeibo.setOnClickListener(this);
		btWeichat.setOnClickListener(this);
		btWeichatMoment.setOnClickListener(this);
		btQicq.setOnClickListener(this);
		btCancel.setOnClickListener(this);
	}

	private void init() {
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wmParams = new WindowManager.LayoutParams();
		wmParams.format = PixelFormat.RGBA_8888;
		analysisUtils = new AnalysisUtils(context);
		animPullUp = AnimationUtils.loadAnimation(context, R.anim.anim_pull_up);
		animPullDown = AnimationUtils.loadAnimation(context,
				R.anim.anim_pull_down);
		umengShare = new UmengShare(activity);
	}

	/**
	 * cancel
	 */
	private void cancel() {
		if (state != Show_None) {
			return;
		}
		animPullDown.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				state = Show_Hiding;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				state = Hide_None;
				try {
					wm.removeView(shareContainer);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		shareLayout.startAnimation(animPullDown);
	}

	/**
	 * @param article
	 */
	public void show(Article article) {
		this.shareEntity = new ShareEntity();
		shareEntity.setArticle(article);
		show(shareEntity);
	}

	/**
	 * @Hide show
	 */
	private void show(ShareEntity shareEntity) {
		this.shareEntity = shareEntity;
		if (state != Hide_None) {
			return;
		}
		try {
			wm.addView(shareContainer, wmParams);
			animPullUp.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					state = Hide_Showing;
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					state = Show_None;
				}
			});
			shareLayout.startAnimation(animPullUp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void remove() {
		try {
			wm.removeView(shareContainer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		state = Hide_None;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btWeibo:
			remove();
			if (shareEntity != null && shareEntity.article != null) {
				Article article = getShareEntity().getArticle();
				String shareContent = createShareContent(article);
				String imageUrl = createShareImage(article);
				try {
					analysisUtils.requestArticleAnalysis(article,
							ArticleAnalysisType.SHARE);
					analysisUtils.requestArticleAnalysis(SINA);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Dialog dialog = new WeiboShareDialog(UmengShare.getInstance(),
						context, shareContent, imageUrl, this);
				dialog.show();
			}
			break;
		case R.id.btWeichat:
			remove();
			if(!AppUtils.isAppInstalled(context,"com.tencent.mm")){
				Toast.makeText(context, "你的手机没有安装微信", Toast.LENGTH_LONG).show();
				return;
			}
			if (shareEntity != null && shareEntity.article != null) {
				Article article = getShareEntity().getArticle();
				UMImage umImage = new UMImage(context,
						createShareImage(article));
				analysisUtils.requestArticleAnalysis(article,
						ArticleAnalysisType.SHARE);
				analysisUtils.requestArticleAnalysis(WEIXIN);
				umengShare.shareWeiXin(activity, article.title, article.zhaiYao,
						article.shareUrl, umImage, this);
			}
			break;
		case R.id.btWeichatMoment:
			remove();
			if(!AppUtils.isAppInstalled(context,"com.tencent.mm")){
				Toast.makeText(context, "You haven't installed WeChat on your phone.", 1).show();
				return;
			}
			if (shareEntity != null && shareEntity.article != null) {
				Article article = getShareEntity().getArticle();
				UMImage umImage = new UMImage(context,
						createShareImage(article));
				analysisUtils.requestArticleAnalysis(article,
						ArticleAnalysisType.SHARE);
				analysisUtils.requestArticleAnalysis(WEIXIN_CIRCLE);
				UmengShare.getInstance().shareWeiXinFriend(context,
						article.title+"fengexian"+article.zhaiYao, article.shareUrl, umImage, this);
			}
			break;
		case R.id.btQicq:
			remove();
			if (shareEntity != null && shareEntity.article != null) {
				Article article = getShareEntity().getArticle();
				UMImage umImage = new UMImage(context,
						createShareImage(article));

				String content=SMS_SUB + article.title + SMS_APP_DOWNLOAD;
				analysisUtils.requestArticleAnalysis(article,
						ArticleAnalysisType.SHARE);
				analysisUtils.requestArticleAnalysis(EMAIL);
				UmengShare.getInstance().shareEmail(context, article.title,
						content, new String[] {}, null, umImage, this);
			}

			break;


		case R.id.btCancel:
			cancel();
			break;
		}
	}

	private String createShareImage(Article article) {
		String imageUrl = Urls.image(article.path, article.articleId,
				article.firstPictureFileHD());
		return imageUrl;
	}

	private String createShareContent(Article article) {
		StringBuilder builder = new StringBuilder();
		builder.append(article.title);
		builder.append("\r");
		if (article.shareUrl == null) {
			article.shareUrl = "";
		}
		builder.append(article.shareUrl);
		String shareContent = builder.toString();
		return shareContent;
	}

	@Override
	public void onComplete(SHARE_MEDIA share_media, int arg1, SocializeEntity arg2) {
		System.out.println("ShareUtils.onComplete()");
		System.out.println(share_media);
		System.out.println(arg1);
		System.out.println(arg2);
		if (arg1 == HttpStatus.SC_OK) {
			// TODO 分享成功
			// Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
			switch(share_media){
			case SINA:
				Toast.makeText(context, "Shared to Weibo!", Toast.LENGTH_SHORT).show();
				break;
			case WEIXIN:
				Toast.makeText(context, "Shared to WeChat!", Toast.LENGTH_SHORT).show();
				break;
			case WEIXIN_CIRCLE:
				Toast.makeText(context, "Shared to WeChat Moments!", Toast.LENGTH_SHORT).show();
				break;
			case EMAIL:
			case SMS:
				Toast.makeText(context, "Successfully sent!", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onStart() {
		System.out.println("ShareUtils.onStart()");
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		System.out.println(arg0);
		System.out.println(arg1);
//		Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		System.out.println(arg0);
		System.out.println(arg1);
		System.out.println(arg2);
//		Toast.makeText(context, "complete", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		System.out.println(arg0);
		System.out.println(arg1);
		arg2.printStackTrace();
//		Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
	}
}
