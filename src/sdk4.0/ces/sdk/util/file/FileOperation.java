/***************
   中信组件库
 ***************/
package ces.sdk.util.file;

import java.io.*;
import java.net.*;
import java.util.*;

import ces.sdk.util.CommonSystemOut;

/**
 * <b>文 件 名:</b>FileOperation.java
 * <br><b>功能描述:</b>
 * 此类中封装一些常用的文件操作：读取、更新、删除、创建目录等
 * 所有方法都是静态方法，不需要生成此类的实例，
 * 为避免生成此类的实例，构造方法被申明为private类型的。
 * 2004/03/12 测试问题修改和完善；
 * <br>
 * <b>版权所有:</b>上海中信信息发展有限公司(CES)2003
 * @version 1.0.2003.0911
 * @author 钟新华
 */
public class FileOperation {

/**
	 *判断文件是否存在
	 *@param strFilename 文件名（含路径）
	 *@return true or false true---文件存在;false---文件不存在；
	 */
	public static boolean isExists(String strFilename) {
		boolean bln = false;
		try {
			File file = new File(strFilename);
			if (file.exists()) {
				bln = true;
			}
			else {
bln = false;
				CommonSystemOut.SystemOutFun("文件\"" + strFilename + "\"不存在!");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return bln;
	}

/**
	 * 把文本内容写入文件
	 *@param strContent 文件内容
	 *@param strFilename 文件名（含路径）
	 *@param overwrite 覆盖标志：true---覆盖;false---不覆盖;
	 *@return true---删除成功;false---删除失败；
	 */
	public static boolean writeFile(
		String strContent,
		String strFilename,
		boolean overwrite) {
		boolean write_flag = false;
		try {
			File file = new File(strFilename);
			if (file.exists() && !overwrite) {
				CommonSystemOut.SystemOutFun("文件\"" + strFilename + "\"不存在!");
			}
			else {
				File file1 = file.getParentFile();
				if (!file1.exists()) {
file1.mkdirs();
				}
				FileOutputStream fileoutputstream =
					new FileOutputStream(file);
				fileoutputstream.write(strContent.getBytes());
				fileoutputstream.close();
				write_flag = true;
			}
			file = null;
		}
		catch (IOException ioexception) {
			write_flag = false;
			CommonSystemOut.SystemOutFun(ioexception.getMessage());
		}
		return write_flag;
	}

/**
	 *删除指定的文件
	 *@param strFilename 文件名（含路径）
	 *@return boolean true---删除成功;false---删除失败；
	 */
	public static boolean deleteFile(String strFilename) {
		String msg = "";
		try {
			File File_todel = new File(strFilename);
			if (File_todel.exists()) {
				File_todel.delete();
			}
			return true;
		}
		catch (Exception e) {
			msg = "删除文件时出错：" + e.toString() + "<br>";
			CommonSystemOut.SystemOutFun(msg);
			return false;
		}
	}

    /**
	 * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
	 */
	private FileOperation() {
	}

    /**
	 * 修改文件的最后访问时间。
	 * 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * @param file 需要修改最后访问时间的文件。
	 * @since  0.1
	 */
	public static void touch(File file) {
		long currentTime = System.currentTimeMillis();
		if (!file.exists()) {
			System.err.println("file not found:" + file.getName());
			System.err.println("Create a new file:" + file.getName());
			try {
				if (file.createNewFile()) {
					CommonSystemOut.SystemOutFun("Succeeded!");
				}
				else {
					System.err.println("Create file failed!");
				}
			}
			catch (IOException e) {
				System.err.println("Create file failed!");
				e.printStackTrace();
			}
		}
		boolean result = file.setLastModified(currentTime);
		if (!result) {
			System.err.println("touch failed: " + file.getName());
		}
	}

/**
	 * 修改文件的最后访问时间。
	 * 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * @param fileName 需要修改最后访问时间的文件的文件名。
	 * @since  0.1
	 */
	public static void touch(String fileName) {
		File file = new File(fileName);
		touch(file);
	}

///**
//	 * 修改文件的最后访问时间。
//	 * 如果文件不存在则创建该文件。
//	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
//	 * @param files 需要修改最后访问时间的文件数组。
//	 * @since  0.1
//	 */
//	public static void touch(File[] files) {
//		for (int i = 0; i < files.length; i++) {
//			touch(files[i]);
//		}
//	}
//
///**
//	 * 修改文件的最后访问时间。
//	 * 如果文件不存在则创建该文件。
//	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
//	 * @param fileNames 需要修改最后访问时间的文件名数组。
//	 * @since  0.1
//	 */
//	public static void touch(String[] fileNames) {
//		File[] files = new File[fileNames.length];
//		for (int i = 0; i < fileNames.length; i++) {
//			files[i] = new File(fileNames[i]);
//		}
//		touch(files);
//	}

/**
	 * 判断指定的文件是否存在。
	 * @param fileName 要判断的文件的文件名
	 * @return 存在时返回true，否则返回false。
	 * @since  0.1
	 */
	public static boolean isFileExist(String fileName) {
		return new File(fileName).isFile();
	}

/**
	 * 创建指定的目录,当需要创建的目录存在时会给出提示,
	 * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。
	 * <b>注意：可能会在返回false的时候创建部分父目录。</b>
	 * @param file 要创建的目录
	 * @return 完全创建成功时返回true，否则返回false。
	 * @since  0.1
	 */
	public static boolean makeDirectory(File file) {
		if (file != null && file.isDirectory()) {
			//CommonSystemOut.SystemOutFun("该目录已经存在。");
		}
		else {
			File parent = file.getParentFile();
			if (parent != null) {
				return file.mkdirs();
			}
		}
		return false;
	}

/**
	 * 创建指定的目录。当需要创建的目录存在时会给出提示,
	 * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。
	 * <b>注意：可能会在返回false的时候创建部分父目录。</b>
	 * @param fileName 要创建的目录的目录名
	 * @return 完全创建成功时返回true，否则返回false。
	 * @since  0.1
	 */
	public static boolean makeDirectory(String fileName) {
		File file = new File(fileName);
		return makeDirectory(file);
	}

/**
	 * 清空指定目录中的文件.
	 * 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
	 * 另外这个方法不会迭代删除，即不会删除子目录及其内容，如果子目录为空将会被删除。
	 * @param directory File对象
	 * @return File对象下的所有文件都被成功删除时返回true，否则返回false.
	 * @since  0.2
	 */
	public static boolean emptyDirectory(File directory) {
		boolean result = false;
		File[] entries = directory.listFiles();
		for (int i = 0; i < entries.length; i++) {
			if (!entries[i].delete()) {
				result = false;
			}
		}
		return true;
	}

/**
	 * 清空指定目录中的文件。
	 * 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
	 * 另外这个方法不会迭代删除，即不会删除子目录及其内容，如果子目录为空将会被删除。
	 * @param directoryName 要清空的目录的目录名
	 * @return 目录下的所有文件都被成功删除时返回true，否则返回false。
	 * @since  0.1
	 */
	public static boolean emptyDirectory(String directoryName) {
		File dir = new File(directoryName);
		return emptyDirectory(dir);
	}

/**
	 * 删除指定目录及其中的所有内容。
	 * @param dirName 要删除的目录的目录名
	 * @return 删除成功时返回true，否则返回false。
	 * @since  0.1
	 */
	public static boolean deleteDirectory(String dirName) {
		return deleteDirectory(new File(dirName));

	}

    /**
	 * 删除指定文件。
	 * @param dir 要删除的文件
	 * @return 删除成功时返回true，否则返回false。
	 * @since  0.1
	 */
	public static boolean deleteDirectory(File dir) {
		if ( (dir == null) || !dir.isDirectory()) {
			new IOException("您输入的文件不是一个有效的目录。");
			return false;
		}
//			try{
		File[] entries = dir.listFiles();
		int sz = entries.length;
		for (int i = 0; i < sz; i++) {
			if (entries[i].isDirectory()) {
				if (!deleteDirectory(entries[i])) {
					return false;
				}
			}
			else {
				if (!entries[i].delete()) {
					return false;
				}
			}
		}
		if (!dir.delete()) {
			return false;
		}
		return true;

//		}
//		catch (Exception ex) {
//			ex.printStackTrace();
//		}

	}

/**
	 * 列出目录中的所有内容，包括其子目录中的内容。
	 * @param fileName 要列出的目录的目录名
	 * @return 目录内容的文件数组。
	 * @since  0.1
	 */
	public static File[] listAll(String fileName) {
		return listAll(new File(fileName));
	}

/**
	 * 列出目录中的所有内容，包括其子目录中的内容。
	 * @param file 要列出的目录
	 * @return 目录内容的文件数组。
	 * @since  0.1
	 */
	public static File[] listAll(File file) {
		ArrayList list = new ArrayList();
		File[] files;
		if (!file.exists() || file.isFile()) {
			return null;
		}
		list(list, file, new AllFileFilter());
		list.remove(file);
		files = new File[list.size()];
		list.toArray(files);
		return files;
	}

/**
	 * 列出目录中的所有内容，包括其子目录中的内容。
	 * @param file 要列出的目录
	 * @param filter 过滤器
	 * @return 目录内容的文件数组。
	 * @since  0.1
	 */
	public static File[] listAll(
		File file,
		javax.swing.filechooser.FileFilter filter) {
		ArrayList list = new ArrayList();
		File[] files;
		if (!file.exists() || file.isFile()) {
			return null;
		}
		list(list, file, filter);
		files = new File[list.size()];
		list.toArray(files);
		return files;
	}

/**
	 * 将目录中的内容添加到列表。
	 * @param list 文件列表
	 * @param filter 过滤器
	 * @param file 目录
	 */
	private static void list(
		ArrayList list,
		File file,
		javax.swing.filechooser.FileFilter filter) {
		if (filter.accept(file)) {
			list.add(file);
			if (file.isFile()) {
				return;
			}
		}
		if (file.isDirectory()) {
			File files[] = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				list(list, files[i], filter);
			}
		}

	}

/**
	 * 返回文件的URL地址。
	 * @param file 文件
	 * @return 文件对应的的URL地址
	 * @throws MalformedURLException
	 * @since  0.4
	 * @deprecated 在实现的时候没有注意到File类本身带一个toURL方法将文件路径转换为URL。
	 *             请使用File.toURL方法。
	 */
	public static URL getURL(File file) throws MalformedURLException {
		String fileURL = "file:/" + file.getAbsolutePath();
		URL url = new URL(fileURL);
		return url;
	}

/**
	 * 将DOS/Windows格式的路径转换为UNIX/Linux格式的路径。
	 * 其实就是将路径中的"\"全部换为"/"，因为在某些情况下我们转换为这种方式比较方便，
	 * 某中程度上说"/"比"\"更适合作为路径分隔符，而且DOS/Windows也将它当作路径分隔符。
	 * @param filePath 转换前的路径
	 * @return 转换后的路径
	 * @since  0.4
	 */
	public static String toUNIXpath(String filePath) {
		return filePath.replace('\\', '/');
	}

/**
	 * 从文件名得到UNIX风格的文件绝对路径。
	 * @param fileName 文件名
	 * @return 对应的UNIX风格的文件路径
	 * @since  0.4
	 * @see #toUNIXpath(String filePath) toUNIXpath
	 */
	public static String getUNIXfilePath(String fileName) {
		File file = new File(fileName);
		return toUNIXpath(file.getAbsolutePath());
	}

/**
	 * 得到路径分隔符在文件路径中首次出现的位置。
	 * 对于DOS或者UNIX风格的分隔符都可以。
	 * @param fileName 文件路径
	 * @return 路径分隔符在路径中首次出现的位置，没有出现时返回-1。
	 * @since  0.5
	 */
	public static int getPathIndex(String fileName) {
		int point = fileName.indexOf('/');
		if (point == -1) {
			point = fileName.indexOf('\\');
		}
		return point;
	}

/**
	 * 得到路径分隔符在文件路径中指定位置后首次出现的位置。
	 * 对于DOS或者UNIX风格的分隔符都可以。
	 * @param fileName 文件路径
	 * @param fromIndex 开始查找的位置
	 * @return 路径分隔符在路径中指定位置后首次出现的位置，没有出现时返回-1。
	 * @since  0.5
	 */
	public static int getPathIndex(String fileName, int fromIndex) {
		int point = fileName.indexOf('/', fromIndex);
		if (point == -1) {
			point = fileName.indexOf('\\', fromIndex);
		}
		return point;
	}

/**
	 * 得到路径分隔符在文件路径中最后出现的位置。
	 * 对于DOS或者UNIX风格的分隔符都可以。
	 * @param fileName 文件路径
	 * @return 路径分隔符在路径中最后出现的位置，没有出现时返回-1。
	 * @since  0.5
	 */
	public static int getPathLastIndex(String fileName) {
		int point = fileName.lastIndexOf('/');
		if (point == -1) {
			point = fileName.lastIndexOf('\\');
		}
		return point;
	}

/**
	 * 得到路径分隔符在文件路径中指定位置前最后出现的位置。
	 * 对于DOS或者UNIX风格的分隔符都可以。
	 * @param fileName 文件路径
	 * @param fromIndex 开始查找的位置
	 * @return 路径分隔符在路径中指定位置前最后出现的位置，没有出现时返回-1。
	 * @since  0.5
	 */
	public static int getPathLastIndex(String fileName, int fromIndex) {
		int point = fileName.lastIndexOf('/', fromIndex);
		if (point == -1) {
			point = fileName.lastIndexOf('\\', fromIndex);
		}
		return point;
	}

/**
	 * 拷贝文件。
	 * @param fromFileName 源文件名
	 * @param toFileName 目标文件名
	 * @return 成功生成文件时返回true，否则返回false
	 * @since  0.6
	 */
	public static boolean copy(String fromFileName, String toFileName) {
		return copy(fromFileName, toFileName, false);
	}

/**
	 * 拷贝文件。
	 * @param fromFileName 源文件名
	 * @param toFileName 目标文件名
	 * @param override 目标文件存在时是否覆盖
	 * @return 成功生成文件时返回true，否则返回false
	 * @since  0.6
	 */
	public static boolean copy(
		String fromFileName,
		String toFileName,
		boolean override) {
		File fromFile = new File(fromFileName);
		File toFile = new File(toFileName);

		if (!fromFile.exists()
			|| !fromFile.isFile()
			|| !fromFile.canRead()) {
			return false;
		}

		if (toFile.isDirectory()) {
			toFile = new File(toFile, fromFile.getName());

		}
		if (toFile.exists()) {
			if (!toFile.canWrite() || override == false) {
				return false;
			}
		}
else {
			String parent = toFile.getParent();
			if (parent == null) {
				parent = System.getProperty("user.dir");
			}
			File dir = new File(parent);
			if (!dir.exists() || dir.isFile() || !dir.canWrite()) {
				return false;
			}
		}

		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(fromFile);
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[4096];
			int bytes_read;
			while ( (bytes_read = from.read(buffer)) != -1) {
				to.write(buffer, 0, bytes_read);
			}
			return true;
		}
		catch (IOException e) {
			return false;
		}
		finally {
			if (from != null) {
				try {
					from.close();
				}
				catch (IOException e) {
					;
				}
			}
			if (to != null) {
				try {
					to.close();
				}
				catch (IOException e) {
					;
				}
			}
		}
	}

/**
	 *创建路径（可以多重）
	 *@param targetPathname 要创建的路径名称
	 *@return String targetPathname 创建后的路径名称,结束含有"/";为空（""）Error
	 * @deprecated 兼容以前版本
	 */
	public static String mkDirs(String targetPathname) {
		try {
			//创建路径：张标 add at 2002-3-27
			File filepath = new File(targetPathname);
			if (!filepath.exists()) {
				filepath.mkdirs();
			}
			int n_len = targetPathname.length();
			String str_slash = targetPathname.substring(n_len - 1, n_len);
			if (!str_slash.equals("/")) {
				targetPathname += "/";
			}
			filepath = null;
			return targetPathname;
		}
		catch (Exception e) {
			CommonSystemOut.SystemOutFun("ErrMsg at addpath:" + e.toString() + "<br>");
			return "";
		}
	}

///**
//	 *   This class moves an input file to output file
//	 *
//	 *   @param String input file to move from
//	 *   @param String output file
//	 *
//	 */
//	public static void move(String input, String output) throws Exception {
//		File inputFile = new File(input);
//		File outputFile = new File(output);
//		try {
//			inputFile.renameTo(outputFile);
//		}
//		catch (Exception ex) {
//			throw new Exception(
//				"Can not mv" + input + " to " + output + ex.getMessage());
//		}
//	}

	///**
	 //*  This class copies an input file to output file
//*
	   //*  @param String input file to copy from
		//*  @param String output file
		 //*/
		 //public static boolean copy(String input, String output) throws Exception{
		 //int BUFSIZE = 65536;
		 //FileInputStream fis = new FileInputStream(input);
		 //FileOutputStream fos = new FileOutputStream(output);
		 //
//try{
		 //	int s;
		 //	byte[] buf = new byte[BUFSIZE];
		 //	while ((s = fis.read(buf)) > -1 ){
		 //			fos.write(buf, 0, s);
		 //	}
		 //}catch (Exception ex) {
		 //				throw new Exception("makehome"+ex.getMessage());
		 //}finally{
		 //   fis.close();
		 //   fos.close();
		 //}
		 //return true;
		 //}

//		 public static void makehome(String home) throws Exception {
//			 File homedir = new File(home);
//			 if (!homedir.exists()) {
//				 try {
//					 homedir.mkdirs();
//				 }
//				 catch (Exception ex) {
//					 throw new Exception(
//						 "Can not mkdir :"
//						 + home
//						 + " Maybe include special charactor!");
//				 }
//			 }
//		 }

		 /**
		  *  目录复制，包括子文件夹
		  *  @param sourcedir   源目录
		  *  @param destdir     目标目录
		  *  @throws Exception
		  */
		 public static void copyDir(String sourcedir, String destdir) throws
			 Exception {
			 File dest = new File(destdir);
			 File source = new File(sourcedir);

			 String[] files = source.list();
			 try {
				 makeDirectory(destdir);
			 }
			 catch (Exception ex) {
				 throw new Exception("CopyDir:" + ex.getMessage());
			 }
			 if(files!=null && files.length>0){
				 for (int i = 0; i < files.length; i++) {
					 String sourcefile = source + File.separator + files[i];
					 //			CommonSystemOut.SystemOutFun("sourcefile:"+sourcefile);
					 String destfile = dest + File.separator + files[i];
					 //			CommonSystemOut.SystemOutFun("destfile:"+destfile);
					 File temp = new File(sourcefile);
					 if (temp.isFile()) {
						 try {
							 copy(sourcefile, destfile);
						 }
						 catch (Exception ex) {
							 throw new Exception("CopyDir:" + ex.getMessage());
						 }
					 }
					 else if (temp.isDirectory()) {
						 try {
							 copyDir(sourcefile, destfile);
						 }
						 catch (Exception ex) {
							 throw new Exception("CopyDir:" + ex.getMessage());
						 }
					 }
				 }
			 }
		 }

	/**
	 *  This class del a directory recursively,that means delete all files and directorys.
	 *  @param directory      the directory that will be deleted.
	 *  @throws Exception
	 */
	public static void recursiveRemoveDir(File directory) throws Exception {
		if (!directory.exists())
			throw new IOException(directory.toString() + " do not exist!");

		String[] filelist = directory.list();
		File tmpFile = null;
		for (int i = 0; i < filelist.length; i++) {
			tmpFile = new File(directory.getAbsolutePath(), filelist[i]);
			if (tmpFile.isDirectory()) {
				recursiveRemoveDir(tmpFile);
			}
			else if (tmpFile.isFile()) {
				try {
					tmpFile.delete();
				}
				catch (Exception ex) {
					throw new Exception(
									   tmpFile.toString()
									   + " can not be deleted "
									   + ex.getMessage());
				}
			}
		}
		try {
			directory.delete();
		}
		catch (Exception ex) {
			throw new Exception(
				directory.toString()
				+ " can not be deleted "
				+ ex.getMessage());
		}
		finally {
			filelist = null;
		}
	}
} //end Class