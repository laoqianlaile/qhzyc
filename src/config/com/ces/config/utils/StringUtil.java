package com.ces.config.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class StringUtil {
    
    private static final String ZERO_10 = "0000000000";
    /** 空字符串.*/
    public static final String EMPTY = "";
    /** 零字符串.*/
    public static final String ZERO  = "0";

    /**
     * <p>标题: isEmpty</p>
     * <p>描述: 判断是否为空字符串</p>
     * @param  obj
     * @return boolean    返回类型   
     * @throws
     */
    public static boolean isEmpty(Object obj) {
        if (EMPTY.equals(null2empty(obj).trim())) return true;
        return false;
    }
    
    /**
     * <p>标题: isNotEmpty</p>
     * <p>描述: 判断是否为非空字符串</p>
     * @param  obj
     * @return boolean    返回类型   
     * @throws
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
    
    /**
     * <p>标题: null2empty</p>
     * <p>描述: 处理null对象，返回空口串""</p>
     * @param  obj
     * @return String    返回类型   
     * @throws
     */
    public static String null2empty(Object obj) {
        if (null == obj || "null".equals(String.valueOf(obj))) {
            return EMPTY;
        }
        return String.valueOf(obj);
    }
    
    /**
     * qiucs 2013-8-15 
     * <p>标题: null2zero</p>
     * <p>描述: 数字（如果null或不是数字时，则返回0）</p>
     * @param  obj
     * @return String    返回类型   
     * @throws
     */
    public static String null2zero(Object obj) {
        String str = null2empty(obj).trim();
        if (str.matches("^(-?[0-9]+(\\.[0-9]+)?)$")) {
            return str;
        }
        return ZERO;        
    }
    
    /**
     * qiucs 2013-8-12 
     * <p>标题: fillZero</p>
     * <p>描述: </p>
     * @param  len
     *         指定长度
     * @param  obj
     *         补零对象
     * @return String    返回类型   
     *         返回补零后字符串
     * @throws
     */
    public static String fillZero(int len, Object obj) {
        int lenOfObj = null2empty(obj).length();
        if (lenOfObj >= len) {
            return null2empty(obj);
        }
        int fillLen = (len - lenOfObj);
        String zeroStr = EMPTY;
        for (int i = 0; i < (fillLen/10); i++) {
            zeroStr += ZERO_10;
        }
        zeroStr += ZERO_10.substring(0, fillLen%10);
        return zeroStr.concat(null2empty(obj));
    }
    
    /**
     * 验证给定字符串是否为真.
     * <p>描述:true、1或on均表示为真</p>
     * @param value 要验证的字符串
     * @return 当<code>value</code>的值为true、1或on返回真，否则返回假
     */
    public static boolean isBooleanTrue(String value) {
        if (value != null && !EMPTY.equals(value.trim())) {
            return "true".equalsIgnoreCase(value.trim()) || "1".equals(value.trim()) || "on".equalsIgnoreCase(value.trim());
        }
        
        return false;
    }
    
    /**
     * qiucs 2015-8-6 下午2:49:06
     * <p>描述: HTML标签处理 </p>
     * @return String
     */
    public static String processHtmlTag(Object val) {
    	String str = null2empty(val);
    	if (EMPTY.equals(str)) return EMPTY;
    	if (str.contains("<")) str = str.replace("<", "&lt;");
    	if (str.contains(">")) str = str.replace(">", "&gt;");
    	return str;
    }
    
    /**
     * qiucs 2015-3-4 下午8:58:51
     * <p>描述: 连接 </p>
     * @return String
     */
    public static String join(Collection<String> collection, String seperator) {
    	if (null == collection) return null;
    	
    	StringBuilder sb = new StringBuilder();
    	
    	Iterator<String> it = collection.iterator();
    	
    	while (it.hasNext()) {
			String str = (String) it.next();
			sb.append(seperator).append(str);
		}
    	if (sb.length() > 0) sb.delete(0, seperator.length());
    	return sb.toString();
    }
    
    /**
     * qiucs 2015-3-4 下午8:59:10
     * <p>描述: 按逗号连接 </p>
     * @return String
     */
    public static String join(Collection<String> collection) {
    	return join(collection, ",");
    }

    /**
     * qiucs 2015-3-5 上午9:37:35
     * <p>描述: 转换为驼峰命名格式 </p>
     * @return String
     */
    public static String toCamelCase(String name) {
    	return toCamelCase(name, "_");
    }

    /**
     * qiucs 2015-3-5 上午9:37:35
     * <p>描述: 转换为驼峰命名格式 </p>
     * @return String
     */
    public static String toCamelCase(String name, String seperator) {
    	if (isEmpty(name)) return EMPTY;
    	name = name.toLowerCase();
    	String[] items = name.split(seperator);
    	int i = 0, len = items.length;
    	StringBuilder sb = new StringBuilder();
    	StringBuilder it = null;
    	sb.append(items[0]);
    	
    	for (i = 1; i < len; i++) {
    		it = new StringBuilder(items[i]);
    		it.setCharAt(0, Character.toUpperCase(it.charAt(0)));
    		sb.append(it);
    	}
    	
    	return sb.toString();
    }
    
    /**
     * qiucs 2015-7-8 上午11:27:47
     * <p>描述: 字符与数字混合随机字符串 </p>
     * @return String
     */
    public static String mixRandom(int len) {
    	StringBuilder sb = new StringBuilder(len);
    	Random rd = new Random();
    	if (len > 0) sb.append(chArr[rd.nextInt(26)]);
    	for (int i = 1; i < len; i++) {
    		sb.append(chArr[rd.nextInt(36)]);
    	}
    	
    	return sb.toString();
    }

    /**
     * qiucs 2015-7-8 上午11:27:47
     * <p>描述: 字符随机字符串 </p>
     * @return String
     */
    public static String charRandom(int len) {
    	StringBuilder sb = new StringBuilder(len);
    	Random rd = new Random();
    	for (int i = 0; i < len; i++) {
    		sb.append(chArr[rd.nextInt(26)]);
    	}
    	
    	return sb.toString();
    }

    
    /**
     * qiucs 2015-7-8 上午11:27:47
     * <p>描述: 数字混合随机字符串 </p>
     * @return String
     */
    public static String numRandom(int len) {
    	StringBuilder sb = new StringBuilder(len);
    	Random rd = new Random();
    	for (int i = 0; i < len; i++) {
    		sb.append(chArr[26 + rd.nextInt(10)]);
    	}
    	
    	return sb.toString();
    }
    
    private static final char[] chArr = {'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 
    	'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 
    	'Z', 'X', 'C', 'V', 'B', 'N', 'M', 
    	'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
}
