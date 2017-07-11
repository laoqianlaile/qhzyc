package com.ces.xarch.plugins.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;

/**
 * 
 * <p>
 * Title: FileUtil
 * </p>
 * <p>
 * Description: 文件相关操作工具类
 * </p>
 * <p>
 * Company: www.cesgroup.com.cn
 * </p>
 * 
 * @author ding.kai
 * @date 2015年1月7日
 */
public class FileUtil extends com.ces.utils.FileUtil {

	public static final String FILE_SEPARATOR = "/";
	public static final String FILE_ENCODING = "UTF-8";


	/**
	 * 复制文件
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void copyFile(File srcFile, File destFile) throws IOException {
		if(srcFile.isDirectory()){
			FileUtils.copyDirectory(srcFile, destFile);
		}else{
			FileUtils.copyFile(srcFile, destFile);
		}
	}

	/**
	 * 复制文件
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void copyFile(String srcPath, String destPath) throws IOException {
		File srcFile = new File(srcPath);
		File destFile = new File(destPath);
		FileUtil.copyFile(srcFile, destFile);
	}
	
	/**
	 * 剪切文件
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void moveFile(File srcFile, File destFile) throws IOException {
		if (srcFile.isDirectory()) {
			FileUtils.moveDirectory(srcFile, destFile);
		} else {
			FileUtils.moveFile(srcFile, destFile);
		}
	}

	/**
	 * 获取文件名后缀
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileExt(String fileName) {
		int index = fileName.lastIndexOf(".") + 1;
		if (index <= 0)
			return "";
		return fileName.substring(index);
	}

	/**
	 * 获取不带后缀的文件名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getOnlyFileName(String fileName) {
		int index = fileName.lastIndexOf("\\");
		if (index == -1) {
			index = fileName.lastIndexOf("/");
		}
		String temp = fileName.substring(index + 1);
		index = temp.indexOf(".");
		temp = temp.substring(0, index);
		return temp;
	}

	/**
	 * 判断路径是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isPathExist(String path) {
		File file = new File(path);
		boolean isExist = file.exists();
		return isExist;
	}

	/**
	 * 通过路径删除文件
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public static void deleteFile(String filePath) throws IOException {
		File file = new File(filePath);
		deleteFile(file);
	}
	/**
	 * 通过文件对象删除文件
	 * @param file
	 * @throws IOException
	 */
	public static void deleteFile(File file) throws IOException {

		if (!file.exists()) {
			throw new FileExistsException(file.getName()+"文件不存在");
		}
		if(file.isDirectory()){
			FileUtils.deleteDirectory(file);
		}else{
			file.delete();
		}
	}

	/**
	 * 计算文件大小
	 * 
	 * @param file
	 * @return
	 */
	public static BigInteger sizeOf(File file) {
		return FileUtils.sizeOfAsBigInteger(file);
	}

	/**
	 * 计算文件大小
	 * 
	 * @param filePath
	 * @return
	 */
	public static BigInteger sizeOf(String filePath) {
		return sizeOf(new File(filePath));
	}

	public static String readAsString(File file, String encoding) throws IOException {
		return FileUtils.readFileToString(file, Charset.forName(encoding));
	}

	public static String readAsString(File file) throws IOException {
		if(file.length()==0)return "";
		String encoding = EncodingDetect.getJavaEncode(file);
		return readAsString(file, encoding);
	}
	public static String readAsString(String filePath) throws IOException {
		return readAsString(new File(filePath));
	}

	/**
	 * 将字符串写入文件
	 * @param file
	 * @param data
	 * @param encoding
	 * @throws IOException
	 */
	public static void writeStringToFile(File file, String data, String encoding) throws IOException {
		FileUtils.writeStringToFile(file, data, Charset.forName(encoding));
	}
	public static void writeStringToFile(File file, String data) throws IOException {
		FileUtils.writeStringToFile(file, data, Charset.forName(FILE_ENCODING));
	}

	/**
	 * 处理中文文件名
	 * <p>描述:处理中文文件名</p>
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @date 2015-5-6 13:47:14
	 * @version 1.0.2015.0506 
	 * @param fileName 文件名
	 * @param request 浏览器请求对象
	 * @param response 浏览器响应对象
	 * @return 处理好的文件名
	 * @throws UnsupportedEncodingException
	 *
	 */
	public static String convertFileName(String fileName,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		  /* 处理中文文件名 */
		  String agent = request.getHeader("User-Agent");
	      boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
	      if( isMSIE ){
	    	  fileName = java.net.URLEncoder.encode(fileName,"UTF8");
	      }else{
	    	  fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
	      }
	      
	      return fileName;
	}
}
