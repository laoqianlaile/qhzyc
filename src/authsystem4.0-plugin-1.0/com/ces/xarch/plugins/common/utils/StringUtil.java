package com.ces.xarch.plugins.common.utils;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
/**
 * 
 * 字符串处理工具类.
 * <p>描述:字符串处理工具类</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author 方俊新(fang.junxin@cesgroup.com.cn)
 * @date 2015-03-11 09:51:43
 * @version 1.0.2015.0311
 */
public class StringUtil{
	
	/**
	 * 获取uuid
	 * @return
	 */
	public static String getUUID(){
		 UUID uuid = UUID.randomUUID();
		 return uuid.toString();
	}
	/**
	 * 剪切文本。如果进行了剪切，则在文本后加上"..."
	 * 
	 * @param s
	 *            剪切对象。
	 * @param len
	 *            编码小于256的作为一个字符，大于256的作为两个字符。
	 * @return
	 */
	public static String textCut(String s, int len, String append) {
		if (s == null) {
			return null;
		}
		int slen = s.length();
		if (slen <= len) {
			return s;
		}
		// 最大计数（如果全是英文）
		int maxCount = len * 2;
		int count = 0;
		int i = 0;
		for (; count < maxCount && i < slen; i++) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
		}
		if (i < slen) {
			if (count > maxCount) {
				i--;
			}
			if (!StringUtils.isBlank(append)) {
				if (s.codePointAt(i - 1) < 256) {
					i -= 2;
				} else {
					i--;
				}
				return s.substring(0, i) + append;
			} else {
				return s.substring(0, i);
			}
		} else {
			return s;
		}
	}

//	public static String htmlCut(String s, int len, String append) {
//		String text = html2Text(s, len * 2);
//		return textCut(text, len, append);
//	}

//	public static String html2Text(String html, int len) {
//		try {
//			Lexer lexer = new Lexer(html);
//			Node node;
//			StringBuilder sb = new StringBuilder(html.length());
//			while ((node = lexer.nextNode()) != null) {
//				if (node instanceof TextNode) {
//					sb.append(node.toHtml());
//				}
//				if (sb.length() > len) {
//					break;
//				}
//			}
//			return sb.toString();
//		} catch (ParserException e) {
//			throw new RuntimeException(e);
//		}
//	}
	
	public static String nullToEmpty(String string){
		return null == string ? "" : string;
	}
	
	
	public static String toUtf8String(String s){  
	     StringBuffer sb = new StringBuffer();  
	       for (int i=0;i<s.length();i++){  
	          char c = s.charAt(i);  
	          if (c >= 0 && c <= 255){sb.append(c);}  
	        else{  
	        byte[] b;  
	         try { b = Character.toString(c).getBytes("utf-8");}  
	         catch (Exception ex) {  
	             System.out.println(ex);  
	                  b = new byte[0];  
	         }  
	            for (int j = 0; j < b.length; j++) {  
	             int k = b[j];  
	              if (k < 0) k += 256;  
	              sb.append("%" + Integer.toHexString(k).toUpperCase());  
	              }  
	     }  
	  }  
	  return sb.toString();  
	}
	/**
	 * 变更type
	 * @param type
	 * @return
	 */
	public static String changType(String type){
		type = type.replaceFirst(type.substring(0, 1),type.substring(0, 1).toUpperCase());
		for(int i=0;i<type.length();i++){
			if(type.charAt(i)=='-'){
				type = type.replace(type.substring(i+1, i+2),type.substring(i+1, i+2).toUpperCase());	
			}
		}
		type = type.replace("-", "");
		return type;
	}
}
