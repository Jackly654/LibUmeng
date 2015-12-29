package com.gov.cn.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Article implements Serializable{
	private static final long serialVersionUID = 1L;
	public String articleId;
	public int contentMode;
	public String title;
	public String titleB;
	public String contentB;
	public String author;
	public String source;
	public String content;
	public String columnId;
	public String specialId;
	public String updateTime;
	public int thumbnails;
	public String publishTime;
	public String feature;
	public String shareUrl;
	public String path;
	public String description;
	public int languageMode;
	public Map<String,Picture> pictures;
	private List<Picture> pictureList;
	public String summary;



	public Picture firstPicture(){
		List<Picture> pictureList=pictures();
		if(pictureList!=null&&pictureList.size()>0){
			return pictureList.get(0);
		}
		return null;
	}
	public List<Picture> pictures(){
		if(pictureList==null){
			pictureList=new ArrayList<Picture>();
			if(pictures!=null){
				pictureList.addAll(pictures.values());
			}
			Collections.sort(pictureList);
		}
		return pictureList;
	}
	public String firstPictureFile(){
		if(firstPicture()!=null&&firstPicture().file!=null){
			return firstPicture().file;
		}
		return "";
	}
	public String firstPictureFileHD(){
		try {
			return firstPicture().fileHD;
		} catch (Exception e) {
			return "";
		}
	}
}
