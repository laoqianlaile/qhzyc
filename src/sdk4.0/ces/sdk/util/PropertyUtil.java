package ces.sdk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * 属性文件解析类
 * */
public class PropertyUtil {
	private static PropertyUtil instance = null;
	private PropertyUtil(){}
	public static PropertyUtil getInstance() {
		instance = null==instance?new PropertyUtil():instance;
		return instance;
	}
	
	private Properties ces_initProperties ;  //ces_init.properties
	private Properties db_Properties ;  //数据源的配置文件
	private Properties sql_Properties ;    //当前的数据库配置文件
	private Properties commonSql_Properties ;    //通用的sql配置文件
	
	public Properties getCes_initProperties(){
		if(null==ces_initProperties){
			ces_initProperties = new Properties();
			String ces_initFilePath = CesGlobals.getInstance().getClassesPath()+"/ces_init.properties";
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(new File(ces_initFilePath));
				ces_initProperties.load(fis);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e.getMessage()+"/n/n ces_init.properties 配置文件没有找到!");
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				if(null!=fis) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return ces_initProperties;
	}
	
	/**
	 * 根据数据库类型获取当前sql配置文件
	 * */
	public Properties getSql_Properties(){
		if(null==sql_Properties){
			sql_Properties = new Properties();
			String sql_FilePath = "/sql_config/"+ConnectionUtil.getDataBaseType()+"/"+ConnectionUtil.getDataBaseType()+".properties";
			InputStream fis = getClass().getResourceAsStream(sql_FilePath);
			try {
				sql_Properties.load(fis);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage()+"/n/n sql配置文件没有找到或者读取错误!");
			}
		}
		return sql_Properties;
	}
	
	/**
	 * 根据数据库类型获取当前sql配置文件
	 * */
	public Properties getCommonSql_Properties(){
		if(null==commonSql_Properties){
			commonSql_Properties = new Properties();
			String sql_FilePath = "/sql_config/common/common_sql.properties";
			InputStream fis = getClass().getResourceAsStream(sql_FilePath);
			try {
				commonSql_Properties.load(fis);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage()+"/n/n sql配置文件没有找到或者读取错误!");
			}
		}
		return commonSql_Properties;
	}
	
	public Properties getDB_Properties(){
		if(null==db_Properties){
			db_Properties = new Properties();
			String ces_initFilePathString = CesGlobals.getInstance().getClassesPath()+"/db_config.properties";
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(new File(ces_initFilePathString));
				db_Properties.load(fis);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e.getMessage()+"/n/n db_config.properties 配置文件没有找到");
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				if(null!=fis) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return db_Properties;
	}
	
	public String getPropertyValueByKey(Properties properties,String key){
		String result = properties.getProperty(key);
//		if(null!=result)
//			try {
//				result = new String(result.getBytes("ISO8859-1"),"UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
		return result;
	}
	
	public static void main(String[] args) {
		//System.out.println(getInstance().getSql_Properties().getProperty("userInfoFacade_getMoreInfoByCondition"));
		System.out.println(MessageFormat.format(
							getInstance().getSql_Properties().getProperty("DBOpLogInfoFacade_queryOpLogInfo"),
							"管理","请求","2012-1-1","2015-1-1"));
	}
}
