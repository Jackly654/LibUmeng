package com.gov.cn.utils;

import android.content.Context;

import com.gov.cn.entity.Article;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.util.Calendar.APRIL;
import static java.util.Calendar.AUGUST;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.FEBRUARY;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.JULY;
import static java.util.Calendar.JUNE;
import static java.util.Calendar.MARCH;
import static java.util.Calendar.MAY;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.NOVEMBER;
import static java.util.Calendar.OCTOBER;
import static java.util.Calendar.SEPTEMBER;
import static java.util.Calendar.YEAR;

public class AnalysisUtils {
	private Context context;

	/**
	 * @param context
	 */
	public AnalysisUtils(Context context) {
		this.context = context;
	}

	private String getArticleEventId(Calendar calendar,
			ArticleAnalysisType analysisType) {
		StringBuilder eventId = new StringBuilder();
		eventId.append(String.valueOf(calendar.get(YEAR)) + '年');
		switch (calendar.get(MONTH)) {
		case JANUARY:
		case FEBRUARY:
		case MARCH:
			eventId.append("第一季度");
			break;
		case APRIL:
		case MAY:
		case JUNE:
			eventId.append("第二季度");
			break;
		case JULY:
		case AUGUST:
		case SEPTEMBER:
			eventId.append("第三季度");
			break;
		case OCTOBER:
		case NOVEMBER:
		case DECEMBER:
			eventId.append("第四季度");
			break;
		default:
			break;
		}
		eventId.append(analysisType.str);
		return eventId.toString();
	}

	private Map<String, String> getAtricleKV(String title, Calendar calendar) {
		Map<String, String> data = new HashMap<String, String>();
		data.put(D_MONTH.parseDMonth(calendar), title);
		return data;
	}

	/**
	 * 提交 文章统计
	 *
	 * @param article
	 * @param analysisType
	 */
	public void requestArticleAnalysis(Article article,
			ArticleAnalysisType analysisType) {
		try {
			long aTime = Long.parseLong(article.updateTime) * 1000;
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(aTime);
			MobclickAgent.onEvent(context,
					getArticleEventId(calendar, analysisType),
					getAtricleKV(article.title, calendar));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}catch(NullPointerException e){
			e.printStackTrace();
		}
	}

	public static final String EVENT_FUNCTIONS = "功能统计";
	public static final String EVENT_FUNCTIONS_SHARE = "分享服务";

	/**
	 * 功能统计 分享服务
	 *
	 * @param media
	 */
	public void requestArticleAnalysis(SHARE_MEDIA media) {
		Map<String, String> data = new HashMap<String, String>();
		data.put(EVENT_FUNCTIONS_SHARE, media == null ? "browser" : media
				.name().toLowerCase(Locale.getDefault()));
		MobclickAgent.onEvent(context, EVENT_FUNCTIONS, data);
	}




	public static final String EVENT_SETTINGS = "调整设置";
	public static final String EVENT_SETTINGS_FONTSIZE = "字体调整";
	public static final String EVENT_SETTINGS_CLEARCACHE = "清除缓存";
	public static final String EVENT_SETTINGS_AUTOUPDATE = "自动更新";
	public static final String EVENT_SETTINGS_OTHER = "其他设置";
	public static final String AD = "广告位统计";

	public void requestAdAnalysis() {
		MobclickAgent.onEvent(context, AD);
	}

    //TODO 多标签事件
	public void requestOtherSettings() {
		MobclickAgent.onEvent(context, EVENT_SETTINGS, EVENT_SETTINGS_OTHER);
	}

	/**
	 * 清除缓存
	 */
	public void requestClearCache() {
		MobclickAgent.onEvent(context, EVENT_SETTINGS,
				EVENT_SETTINGS_CLEARCACHE);
	}



	/**
	 * 修改 自动更新设置
	 *
	 * @param open
	 */
	public void requestAutoUpdateSwitcher(boolean open) {
		Map<String, String> data = new HashMap<String, String>();
		data.put(EVENT_SETTINGS_AUTOUPDATE, String.valueOf(open));
		MobclickAgent.onEvent(context, EVENT_SETTINGS, data);
	}

	/**
	 * 修改 字体大小设置
	 *
	 * @param fontSizeType
	 *//*
	public void requestFontSizeChange(FontSizeType fontSizeType) {
		Map<String, String> data = new HashMap<String, String>();
		data.put(EVENT_SETTINGS_FONTSIZE,
				fontSizeType.name().toUpperCase(Locale.getDefault()));
		MobclickAgent.onEvent(context, EVENT_SETTINGS, data);
	}*/

	public enum AppPromotionType {
		READ("阅读"), DOWNLOAD("下载"), OPEN("打开");
		String str;

		/**
		 * @param str
		 */
		private AppPromotionType(String str) {
			this.str = str;
		}

	}

	public enum ArticleAnalysisType {
		READ("阅读文章"), SAVE("保存文章"), SHARE("分享文章"), MEDIA("播放多媒体");
		String str;

		/**
		 * @param str
		 */
		private ArticleAnalysisType(String str) {
			this.str = str;
		}

	}

	enum D_MONTH {
		SX("上旬"), ZX("中旬"), XX("下旬");
		String xun;

		/**
		 * @param xun
		 */
		private D_MONTH(String xun) {
			this.xun = xun;
		}

		public static String parseDMonth(Calendar calendar) {
			int day = calendar.get(DAY_OF_MONTH);
			String month = String.valueOf(calendar.get(MONTH) + 1) + '月';
			if (day > 0 && day <= 10) {
				return month + SX.xun;
			} else if (day > 10 && day <= 20) {
				return month + ZX.xun;
			} else {
				return month + XX.xun;
			}
		}
	}

	public static final String COLUMN = "栏目统计";

	/**
	 * 栏目统计
	 *
	 * @param column
	 */
	public void requestColumnSwitch(String column) {
		MobclickAgent.onEvent(context, COLUMN, column);
	}
}
