package com.gov.cn;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageUtils {
	private static ImageLoader imageLoader= ImageLoader.getInstance();
	public static ImageLoader getInstance(){
		return imageLoader;
	}

}
