/***************
 中信组件库
 ***************/
package ces.sdk.util.file;


import java.io.*;
import java.net.*;

import java.util.Properties;

import ces.sdk.util.CommonSystemOut;
import ces.sdk.util.StringUtil;

/**
 * <b>文件名:</b>CesGlobals.java<br>
 * <b>功能描述:</b> 1、提供了一些通用的静态常量或方法：主要包括： 1）换行符； 2）文件分隔符 3）路径分隔符 4）java类路径
 * 5）当前操作系统名称 6）当前操作系统版本 7）当前操作系统版本
 * 2、常见时间刻度(秒（Second）、分(Minute)、小时(Hour)、天(Day)、周(Week))与微秒的换算公式
 * 3、获得本地化、时区及日期时间初始化; 4、 配置文件（XML和Properties二种格式）的读写操作： <br>
 * <b>版权所有:</b>上海中信信息发展有限公司(CES)2003
 * 
 * @author 钟新华 郑国强 顾永朋
 * @version 1.0.2003.0910
 */

public class CesGlobals {
	/** 换行符 */
	public static String LINE_SEPARATOR = System.getProperty("line.separator");

	/** 文件分隔符。输入为"\" */
	public static String FILE_SEPARATOR = System.getProperty("file.separator");

	/** 路径分隔符。输入为";" */
	public static String PATH_SEPARATOR = System.getProperty("path.separator");

	/** java类路径 */
	public static String JAVA_CLASS_PATH = System
			.getProperty("java.class.path");

	/** 当前操作系统名称 */
	public static String OS_NAME = System.getProperty("os.name");

	/** 当前操作系统版本 */
	public static String OS_VERSION = System.getProperty("os.version");

	/** 系统当前用户 */
	public static String USER_NAME = System.getProperty("user.name");

	/** SECOND常量 */
	public static final long SECOND = 1000;

	/** MINUTE常量 */
	public static final long MINUTE = 60 * SECOND;

	/** HOUR常量 */
	public static final long HOUR = 60 * MINUTE;

	/** DAY常量 */
	public static final long DAY = 24 * HOUR;

	/** WEEK常量 */
	public static final long WEEK = 7 * DAY;

	/** configFile私有变量 */
	private String configFile = "ces_config.xml";

	/** propConfigFile私有变量 */
	private static String propConfigFile = "system.properties";

	public CesGlobals(){
		configFile = "ces_config.xml";
	}
	
	/**
	 * 得到xml配置文件
	 * 
	 * @return xml配置文件名
	 */
	public String getConfigFile() {
		return configFile;
	}

	/**
	 * 设置xml配置文件
	 * 
	 * @param strFileName
	 *            配置文件名
	 * @return void
	 */
	public void setConfigFile(String strFileName) {
		this.configFile = strFileName;
		loadConfigFile();
	}

	/**
	 * 得到properties配置文件
	 * 
	 * @return properties配置文件名
	 */
	public static String getPropConfigFile() {
		return propConfigFile;
	}

	/**
	 * 设置properties配置文件
	 * 
	 * @param propConfigFile
	 *            properties配置文件名
	 */
	public void setPropConfigFile(String propConfigFile) {
		this.propConfigFile = propConfigFile;
		loadPropConfigFile();
	}

	/**
	 * 中信产品配置文件存放目录.所有配置文件应该放在这个目录。这个文件有一个cesHome属性名称，是存放应用程序目录
	 * 可以通过CesGlobals.getCesHome（）得到.
	 */
	private static String cesHome = null;

	private  XMLProperties properties = null; // XML属性文件读写操作

	private  CESProperties props = null; // XML属性文件读写操作

	/**
	 * 得到中信产品配置文件路径
	 * 
	 * @return cesHome 得到配置文件路径
	 * @record bill modified on Mar 3, 2004
	 * @recommend 这个方法实际是寻找config目录，所以应该叫getCesConfig比较合适，ceshome应该留给应用的根目录
	 */
	public static String getCesHome() {
		if (cesHome == null) {
			// 通过classLoader到Classes目录下寻找ces_init.properties配置文件
			cesHome = new InitPropLoader().getCesHome();
			if (cesHome != null && new File(cesHome).isDirectory()) {
				CommonSystemOut.SystemOutFun("从Classes目录下ces_init.properties配置文件得到的cesHome="
								+ cesHome);
				return cesHome.replace(File.separator.toCharArray()[0], '/');
			}
			// 通过找系统环境变量
			// cesHome = System.getProperty("cesHome");
			// if (cesHome != null && new File(cesHome).isDirectory()) {
			// CommonSystemOut.SystemOutFun("从系统环境变量得到的cesHome=" + cesHome);
			// return cesHome;
			// }
			// 通过配置文件web.xml中配置的cesHome
			// if (cesHome == null || cesHome.equals("")) {
			// ServletConfig config;
			// cesHome = getWebParam(config);
			// CommonSystemOut.SystemOutFun("cesHome2:" + cesHome);
			// }

			// 通过解析字符串自动寻找config文件夹
			cesHome = new PathLoader().getPathFromClassPath();
			if (cesHome != null && new File(cesHome).isDirectory()) {
				cesHome += "/config"; // 配置文件统一放在config目录下
				String logHome = cesHome + "/logs";
				//CommonSystemOut.SystemOutFun("自动查找得到的cesHome=" + cesHome);
				//CommonSystemOut.SystemOutFun("默认的日志文件存放目录：logHome=" + cesHome);
				System.setProperty("cesHome", cesHome);
				System.setProperty("logHome", logHome);
				return cesHome;
			}

			// 都没有找到抛出异常
			if (cesHome == null || !new File(cesHome).isDirectory()) {
				String info = "没有找到配置文件的目录!\n";
				info += "请设置cesHome（配置文件路径config）的位置,例如：\n";
				info += "在classes目录下使用ces_init.properties文件定义cesHome={$configPath}，\n注意路径分隔符用\\连接，如cesHome=E:\\tomcat4.1\\webapps\\test\\web-inf\\config。";
				throw new NullPointerException(info);
			}
		}
		return cesHome.replace(File.separator.toCharArray()[0], '/');
		// return cesHome;
	}

	/**
	 * 得到中信产品配置文件路径
	 * 
	 * @return cesHome 得到配置文件路径
	 * @record bill modified on Mar 3, 2004
	 * @recommend 这个方法实际是寻找config目录，所以应该叫getCesConfig比较合适，ceshome应该留给应用的根目录
	 */
	public static String getAppRoot() {
		String appRoot = null;
		String cesHome = CesGlobals.getCesHome();
		if (cesHome != null && cesHome.toUpperCase().indexOf("WEB-INF") != -1) {
			appRoot = cesHome.substring(0, cesHome.toUpperCase().indexOf(
					"WEB-INF"));
		}
		return appRoot;
	}

	/**
	 * 判断cesHome所在的文件夹是否可读
	 * 
	 * @return cesHome所在的文件夹是否可读
	 */
	public static boolean isCesHomeReadable() {
		return (new File(getCesHome())).canRead();
	}

	/**
	 * 判断cesHome所在的文件夹是否可写
	 * 
	 * @return cesHome所在的文件夹是否可写
	 */
	public static boolean isCesHomeWritable() {
		return (new File(getCesHome())).canWrite();
	}

	/**
	 * 得到CES属性文件值
	 * 
	 * @param name
	 *            要得到的属性名称
	 * @return 得到设置的属性值
	 */
	public String getCesXmlProperty(String name) {
		String strTemp = null;
		if (properties == null)
			loadConfigFile();
		Object objTemp = properties.getPropertyValue(name);

		if (objTemp != null) {
			strTemp = objTemp.toString();
			if (strTemp.trim().indexOf("[") != -1)
				strTemp = strTemp.substring(0, strTemp.indexOf("["))
						+ strTemp.substring(strTemp.indexOf("[") + 1);
			if (strTemp.indexOf("]") != -1)
				strTemp = strTemp.substring(0, strTemp.indexOf("]"))
						+ strTemp.substring(strTemp.indexOf("]") + 1);
			strTemp = StringUtil.replaceAll(strTemp, ",", "#$");
		}
		//CommonSystemOut.SystemOutFun(name + "  " + strTemp);
		return strTemp;
	}

	/**
	 * 设置XML属性文件的属性值,如果属性不存在将自动创建
	 * 
	 * @param name
	 *            要设置的属性名称
	 * @param value
	 *            要设置的属性值.
	 */
	public void setCesXmlProperty(String name, String value) {
		if (properties == null)
			loadConfigFile();
		properties.setProperty(name, value);
	}

	/**
	 * 设置XML属性文件的属性值,如果属性不存在将自动创建
	 * 
	 * @param name
	 *            要设置的属性名称
	 * @param value
	 *            要设置的属性值，可以为List,ArrayList.
	 */
	public void setCesXmlProperty(String name, Object value) {
		if (properties == null)
			loadConfigFile();
		properties.setProperty(name, value);
	}

	/**
	 * 删除一个属性项,如果要删除的属性名称不存在，这个方法将不起作用
	 * 
	 * @param name
	 *            要删除的属性名.
	 */
	public void deleteCesXmlProperty(String name) {
		if (properties == null)
			loadConfigFile();
		properties.deleteProperty(name);
	}

	/**
	 * 得到Properties文件属性值
	 * 
	 * @param name
	 *            要得到的属性名称
	 * @return 得到设置的属性值
	 */
	public String getCesProperty(String name) {
		String value = "";
		if (props == null)
			loadPropConfigFile();
		if (props != null) {
			value = props.getProperty(name);
		}
		return value;
	}

	/**
	 * 设置Properties属性文件的属性值,如果属性不存在将自动创建
	 * 
	 * @param name
	 *            要设置的属性名称
	 * @param value
	 *            要设置的属性值.
	 */
	public void setCesProperty(String name, String value) {
		if (props == null)
			loadPropConfigFile();
		try {
			if (props != null) {
				props.put(name, value);
				String strFileName = getCesHome() + File.separator
						+ getPropConfigFile();
				// FileOutputStream ou=new FileOutputStream(strFileName);
				FileOutputStream objFos = new FileOutputStream(strFileName);
				props.store(objFos, "");
			}
		} catch (Exception ex) {
		}

	}

	/**
	 * 删除Properties文件的一个属性项,如果要删除的属性名称不存在，这个方法将不起作用
	 * 
	 * @param name
	 *            要删除的属性名.
	 */
	public void deleteCesProperty(String name) {
		if (props == null)
			loadPropConfigFile();
		try {
			if (props != null) {
				props.remove(name);
				String strFileName = getCesHome() + File.separator
						+ getPropConfigFile();
				FileOutputStream objFos = new FileOutputStream(strFileName);
				props.store(objFos, "");

			}

		} catch (Exception ex) {
		}

	}

	/**
	 * 装载要处理的配置文件,默认为装载ces_config.xml配置文件
	 * 
	 * @record modified by Bill on Mar 12,2004
	 * 
	 */
	// synchronized static
	private void loadConfigFile() {
		// If cesHome is still null, no outside process has set it and
		// we have to attempt to load the value from jive_init.properties,
		// which must be in the classpath.
		if (cesHome == null) {
			cesHome = getCesHome();
		}
		String configFileName = getConfigFile();
		configFileName = cesHome + FILE_SEPARATOR + configFileName;
		if (!FileOperation.isFileExist(configFileName)) {
			System.err.println("配置文件" + configFileName + "不存在，请核对！");
		}
		if (configFileName.toLowerCase().endsWith(".xml")) {
			// Create a manager with the full path to the xml config file.
			properties = new XMLProperties(configFileName, true);
		} else if (configFileName.toLowerCase().endsWith(".properties")) {
			props = new CESProperties();
			File file = new File(configFileName);
			props.load(file);
		} else {
			System.err.println("您没有读入配置文件，或默认的ces_config.xml文件不存在。");
		}
	}

	/**
	 * 装载要处理的property配置文件，默认为system.properties. added by steve_gu at 2004-03-11
	 * 在方法前添加synchronized static modified by zxh at 2005-11-23 synchronized
	 * static
	 */
	private void loadPropConfigFile() {
		// If cesHome is still null, no outside process has set it and
		// we have to attempt to load the value from jive_init.properties,
		// which must be in the classpath.
		if (cesHome == null) {
			cesHome = new InitPropLoader().getCesHome();
		}
		String configFileName = getPropConfigFile();
		configFileName = cesHome + FILE_SEPARATOR + configFileName;
		if (FileOperation.isFileExist(configFileName)
				&& configFileName.toLowerCase().endsWith(".properties")) {
			props = new CESProperties();
			File file = new File(configFileName);
			props.load(file);
		} else {
			System.err.println("cesHome目录下没有配置文件，或文件不是properties类型！");
		}
	}
}

/**
 * 自动寻找cesHome的类
 * 
 * @author Administrator
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
class PathLoader {
	/**
	 * 根据当前类取得绝对路径
	 * 
	 * @return : 绝对路径，如果出错，返回""（长度为零的字符串）
	 */
	protected String getPathFromClassPath() {
		String strUrl = "";
		try {
			Class clazz = InitPropLoader.class;
			String className = clazz.getName();
			// CommonSystemOut.SystemOutFun("className:"+className);
			String fileName = className
					.substring(className.lastIndexOf('.') + 1)
					+ ".class";
			int packLength = getPackageName(clazz).length() + 8; // 8 =
																	// "package
																	// ".length();
			URL url = clazz.getResource(fileName);
			strUrl = URLDecoder.decode(url.toString(), "GBK");
			// CommonSystemOut.SystemOutFun("strUrl:"+strUrl);
			// CommonSystemOut.SystemOutFun("packLength:"+packLength);
			if (strUrl.substring(0, 4).equals("jar:")) {
				int offset = (packLength - 8) + 22;
				int libPos = strUrl.indexOf("lib/");
				strUrl = strUrl.substring(10, libPos - 1);
				//CommonSystemOut.SystemOutFun("cesHome in lib:" + strUrl);
			} else {
				int offset = packLength + 22; // 18 =
												// "/classes//ClassUtils.class".length();
				strUrl = strUrl.substring(6, strUrl.length() - offset); // 6 =
																		// "file:/".length();
				//CommonSystemOut.SystemOutFun("cesHome in classes:" + strUrl);
			}
		} catch (Exception e) {
			strUrl = null;
		}
		return strUrl;
	}

	private static String getPackageName(Object obj) {
		return getPackageName(obj.getClass());
	}

	private static String getPackageName(Class clazz) {
		Package pack = clazz.getPackage();
		if (pack != null) {
			return pack.getName();
		} else {
			return null;
		}
	}
}

/**
 * 用于装载ces_init.properties的一个非常小的类 fails.
 */
class InitPropLoader {
	/**
	 * 获取应用程序路径。通过载入ces_init.properties文件，
	 * 事先web-inf/classes目录下必须有ces_init.properies
	 * 
	 * @return 应用程序路径
	 */
	public String getCesHome() {
		// 将读取ces_init.properties配置文件
		Properties initProps = null;
		InputStream in = null;
		try {
			in = getClass().getResourceAsStream("/ces_init.properties");
			if (in != null) {
				initProps = new Properties();
				initProps.load(in);
				in.close();
			}
		} catch (Exception e) {
			System.err.println("在InitPropLoader中读取ces_init.properties文件出错！");
			e.printStackTrace();
		}
		String cesHome = null;
		if (initProps != null) {
			cesHome = initProps.getProperty("cesHome");
			if (cesHome != null) {
				try {
					cesHome = new String(cesHome.getBytes("ISO-8859-1"),
							"gb2312");
				} catch (UnsupportedEncodingException ex) {
					System.err.println("在InitPropLoader中转换编码时出错！");
					ex.printStackTrace();
				}
				cesHome = cesHome.trim();
				// Remove trailing slashes.
				while (cesHome.endsWith("/") || cesHome.endsWith("\\")) {
					cesHome = cesHome.substring(0, cesHome.length() - 1);
				}
			}
		}
		return cesHome;
	}

	public static void main(String[] args) {
		CesGlobals ces = new CesGlobals();
		// ces.setConfigFile("test.xml");
		CommonSystemOut.SystemOutFun(ces.getCesXmlProperty("upload.ftp.host"));
		CommonSystemOut.SystemOutFun("1121212");

		ces.setCesXmlProperty("upload.ftp.host", "hello");
		CommonSystemOut.SystemOutFun("43434");
		CommonSystemOut.SystemOutFun(ces.getCesXmlProperty("upload.ftp.host"));

		ces.setConfigFile("platform.xml");
		CommonSystemOut.SystemOutFun(ces.getCesXmlProperty("platform.version"));

		CommonSystemOut.SystemOutFun(ces.getCesProperty("log4j.rootLogger"));
		CommonSystemOut.SystemOutFun(ces.getCesProperty("log4j.logger.ces.com.jdbc"));
	}
}
