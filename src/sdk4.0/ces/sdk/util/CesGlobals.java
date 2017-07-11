package ces.sdk.util;

import java.net.URL;

/**
 * 此类是工具类 主要是提供获取CLASSES以及CONFIG文件夹的位置
 * */
public class CesGlobals {
	private static CesGlobals instance = null;
	private CesGlobals(){};
	public static synchronized CesGlobals getInstance(){
		instance = null==instance?new CesGlobals():instance;
		return instance;
	}
	
	/**
	 * 获取classes目录
	 * */
	public String getClassesPath(){
		return PathLoader.getInstance().getPathFromClassPath()+"/classes" ;
	}
	
	/**
	 * 获取config目录
	 *  先获得classes目录下的ces_init.properties
	 *  然后解析ces_init.properties文件获取config文件夹的位置
	 * */
	public String getConfigPath(){
		PropertyUtil instance = PropertyUtil.getInstance();
		return instance.getPropertyValueByKey(instance.getCes_initProperties(), "cesHome");
	}
	
	/**
	 * 获取config目录
	 *  先获得classes目录下的ces_init.properties
	 *  然后解析ces_init.properties文件获取config文件夹的位置
	 * */
	public String getSdkUniqueCode(){
		PropertyUtil instance = PropertyUtil.getInstance();
		return instance.getPropertyValueByKey(instance.getCes_initProperties(), "sdkUniqueCode");
	}
	
	public static void main(String[] args) {
		//System.out.println(CesGlobals.getInstance().getClassesPath());
		System.out.println(CesGlobals.getInstance().getSdkUniqueCode());
	}
}

/**
 * 获取到项目WEB-INF文件夹位置
 */
class PathLoader {
	private static PathLoader instance =null;
	private PathLoader(){}
	protected static synchronized PathLoader getInstance(){
		instance = null==instance?new PathLoader():instance;
		return instance;
	}
	@SuppressWarnings("unchecked")
	// return "D:/workspace/auth_sdk_new/WEB-INF"
	protected String getPathFromClassPath() {
		String strUrl = "";
		try {
			Class clazz = PathLoader.class;
			String className = clazz.getName();
			String fileName = className
					.substring(className.lastIndexOf('.') + 1)
					+ ".class";
			int packLength = getPackageName(clazz).length() + 8; 
			URL url = clazz.getResource(fileName);
			strUrl = java.net.URLDecoder.decode(url.toString(), "UTF-8");
			if (strUrl.substring(0, 4).equals("jar:")) {
				int libPos = strUrl.indexOf("lib/");
				strUrl = strUrl.substring(10, libPos - 1);
			} else {
				int offset = packLength + 18;
				strUrl = strUrl.substring(6, strUrl.length() - offset); 
			}
		} catch (Exception e) {
			strUrl = null;
		}
		return strUrl;
	}

	@SuppressWarnings("unchecked")
	private static String getPackageName(Class clazz) {
		Package pack = clazz.getPackage();
		if (pack != null) {
			return pack.getName();
		} else {
			return null;
		}
	}
}