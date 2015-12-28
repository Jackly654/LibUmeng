package com.gov.cn.entity;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Save implements Serializable,Comparable<Save>{
	private static final long serialVersionUID = 1L;
	public String articleId;
	public int contentMode;
	public String title;
	public String author;
	public String source;
	public String content;
	public String columnId;
	public int position;
	public String specialId;
	public String updateTime;
    public String publishTime;
	public String feature;
	public String contentB;
	public String titleB;
	public int thumbnails;
	public String path;
	public String shareUrl;
	public int isSpecial;
	public Map<String,Picture> pictures;
	public int recommendTemplate;
	public Article article;
	private List<Picture> pictureList;
	public String description;
	
	
	public Picture firstPicture(){
		if(pictures!=null&&pictures.values().size()>0){
			if(pictureList==null){
				pictureList=new ArrayList<Picture>(pictures.values());
			}
			return pictureList.get(0);
		}
		return null;
	}
	public List<Picture> pictures(){
		if(pictures!=null&&pictures.values().size()>0){
			if(pictureList==null){
				pictureList=new ArrayList<Picture>(pictures.values());
			}
			return pictureList;
		}
		return null;
	}
	
	public String firstPictureFile(){
		if(firstPicture()!=null&&firstPicture().file!=null){
			return firstPicture().file;
		}
		return "";
	}
	public String firstPictureFileHD(){
		return firstPicture().fileHD;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o ==null){
			return false;
		}
		if(!(o instanceof Save)){
			return false;
		}
		Save another=(Save) o;
		return TextUtils.equals(this.articleId,another.articleId);
	}
	@Override
	public int compareTo(Save another) {
		try {
			Long mTime=Long.parseLong(updateTime);
			Long aTime=Long.parseLong(((Save)another).updateTime);
			if(mTime>aTime){
				return -1;
			}else
			if(mTime<aTime){
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
