package com.gov.cn.entity;

import java.io.Serializable;

public class Picture implements Comparable<Picture>, Serializable {
	private static final long serialVersionUID = 1L;
	public String pictureId;
	public String articlePath;
	public String file;
	public String fileHD;
	public String description;
	public float width;
	public float height;
	public int position;
	public String articleId;
	private int currentCategoryPosition;
	private int currentPositionInCategory;
	private int currentCategoryPageSize;
	private int currentPositionInTotalCategory;
	public Save save;

	/**
	 * 获得当前图片在画廊中的位置
	 * 
	 * @return
	 */
	public int getCurrentPositionInTotalCategory() {
		return currentPositionInTotalCategory;
	}

	/**
	 * 设置当前图片在画廊中的位置
	 * 
	 * @param currentPositionInTotalCategory
	 */
	public void setCurrentPositionInTotalCategory(
			int currentPositionInTotalCategory) {
		this.currentPositionInTotalCategory = currentPositionInTotalCategory;
	}

	/**
	 * 设置当前图片组的大小
	 * 
	 * @param currentCategoryPageSize
	 */
	public void setCurrentCategoryPageSize(int currentCategoryPageSize) {
		this.currentCategoryPageSize = currentCategoryPageSize;
	}

	/**
	 * 设置当前图片组在所有图组中的位置
	 * 
	 * @param currentCategoryPosition
	 */
	public void setCurrentCategoryPosition(int currentCategoryPosition) {
		this.currentCategoryPosition = currentCategoryPosition;
	}

	/**
	 * 设置当前图片在当前图组中的位置
	 * 
	 * @param currentPositionInCategory
	 */
	public void setCurrentPositionInCategory(int currentPositionInCategory) {
		this.currentPositionInCategory = currentPositionInCategory;
	}

	/**
	 * 获得需要展示胡图片分组信息
	 * 
	 * @return
	 */
	public String getPageInfo() {
		return currentPositionInCategory + "/" + currentCategoryPageSize;
	}
	/**
	 * 获得当前图集大小
	 * @return
	 */
	public int getCurrentCategoryPageSize() {
		return currentCategoryPageSize;
	}
	/**
	 * 获得在当前图集中的位置 
	 * @return
	 */
	public int getCurrentPositionInCategory() {
		return currentPositionInCategory+1;
	}

	/**
	 * 获得当前图组在所有图组中的位置
	 * 
	 * @return
	 */
	public int getCurrentCategoryPosition() {
		return currentCategoryPosition;
	}

	@Override
	public int compareTo(Picture another) {
		if (this.position < another.position) {
			return -1;
		} else if (this.position > another.position) {
			return 1;
		}
		return 0;
	}
}
