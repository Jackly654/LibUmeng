package com.gov.cn;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;

import share.gwy.gov.libumeng.UmengShare;

public class WeiboShareDialog extends Dialog implements
		android.view.View.OnClickListener {
	private View btCancel, btSend;
	private String content, imageUri;
	private ImageView ivImage;
	private EditText tvContent;

	private SnsPostListener postListener;
	private static final String AT = "@中国国务院";
	private UMImage umImage;

	public WeiboShareDialog(UmengShare umengShare, Context context,
							String content, String imageUri, SnsPostListener postListener) {
		super(context);
		this.content = content;
		this.imageUri = imageUri;
		this.postListener = postListener;
		this.umImage = new UMImage(getContext(), imageUri);
	}

	public WeiboShareDialog(UmengShare umengShare, Context context,
							String content, UMImage umImage, SnsPostListener postListener) {
		this(umengShare, context, content, "", postListener);
		this.umImage = umImage;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setContentView(R.layout.layout_share_dailog);
		btCancel = findViewById(R.id.btCancel);
		btSend = findViewById(R.id.btSend);
		tvContent = (EditText) findViewById(R.id.tvContent);
		ivImage = (ImageView) findViewById(R.id.ivImage);
		btCancel.setOnClickListener(this);
		btSend.setOnClickListener(this);
//		int duration = getContext().getResources().getInteger(
//				android.R.integer.config_longAnimTime);
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		//
		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(false).cacheOnDisc(true)
		.resetViewBeforeLoading(true)
		.showImageForEmptyUri(R.drawable.chinadaily_launcher_back)
		.showImageOnFail(R.drawable.chinadaily_launcher_back)
		.bitmapConfig(Config.RGB_565)
		.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();
		ImageLoader.getInstance().displayImage(imageUri, ivImage, imageOptions);
		tvContent.setText(Html.fromHtml(content + "\r" + AT));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btCancel:
			cancel();
			break;
		case R.id.btSend:
			UmengShare.shareWeibo_new(getContext(), content, null,
					umImage, postListener);
			this.dismiss();
			break;
		}
	}


}