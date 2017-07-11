package com.ces.config.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>描述:图片格式工具类</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @date 2013-12-18  21:08:53
 * @version 1.0.2013.1218
 */

public class ImageUtil {

	/** 图片格式种类.*/
    private final static List<String> formats = new ArrayList<String>();
    
    static {
    	formats.add("bmp");
    	formats.add("gif");
    	formats.add("jpg");
    	formats.add("png");
    	formats.add("tiff");
    	formats.add("jpeg");
    }
    
    /**
     * 判断是否是图片格式
     * @param fileName
     * @return
     * @date 2013-12-18  21:07:11
     */
    public static boolean isImageFormat(String fileName) {
    	//文件名为空，不是图片格式
    	if (StringUtil.isEmpty(fileName)) return false;
    	//文件名不包含"."，不是图片格式
    	int lastIndex = fileName.lastIndexOf(".");
    	if (lastIndex < 0) return false;
    	//截取文件名后缀，如果是图片格式，返回true
    	String format = fileName.substring(lastIndex + 1);
    	if (formats.contains(format.toLowerCase())) {
    		return true;
    	}
    	return false;
    }
}
