/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-1-30 下午3:34:09
 * <p>描述: 日期工具类</p>
 */
package com.ces.config.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	/**
	 * qiucs 2015-1-30 下午3:38:14
	 * <p>描述: 获取当天日期 </p>
	 * @return String
	 */
	public static String today() {
		Date current = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(current);
	}
	
	/**
	 * qiucs 2015-2-11 上午9:24:00
	 * <p>描述: 获取当前时间 </p>
	 * @return String
	 */
	public static String currentTime() {
		Date current = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(current);
	}
	
	/**
	 * qiujinwei 2015-3-25
	 * <p>描述: 获取当前时间 </p>
	 * @return String
	 */
	public static String mouth() {
		Date current = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        return df.format(current);
	}
	
	/**
	 * qiujinwei 2015-3-25
	 * <p>描述: 获取当前时间 </p>
	 * @return String
	 */
	public static String now(String srcDateFormat) {
		Date current = new Date();
        DateFormat df = new SimpleDateFormat(srcDateFormat);
        return df.format(current);
	}
}
