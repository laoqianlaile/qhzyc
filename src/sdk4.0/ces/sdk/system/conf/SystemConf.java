package ces.sdk.system.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class SystemConf {
	protected Log log = LogFactory.getLog(SystemConf.class);
	
	public static final String DEFAULT_CONF_FILE_NAME = "system-sdk-conf.xml";

	public static final String SYSTEM_SDK_CONF = "system-sdk-conf";

	public static final String ENTITY_FACTORY_CLASS_NAME = "entity-factory-class-name";

	public static final String FACADE_FACTORY_CLASS_NAME = "facade-factory-class-name";

	public static final String SDK_DATASOURCE_CLASS_NAME = "sdk-datasource-class-name";

	private static SystemConf instance;

	private String confFileName;

	private String entityFactoryClassName;

	private String facadeFactoryClassName;

	private String sdkDataSourceClassName;

	private SystemConf() {
		if(isConfigFileExist()){
			init();
		}
	}
	public static synchronized SystemConf getInstance() {
		if (instance == null) {
			instance = new SystemConf();
		}
		return instance;
	}
	
	// 判断system-sdk-conf.xml 文件是否存在,不存在则用2013架构实例化Sysfactory等对象,存在,则解析该xml 
	private boolean isConfigFileExist(){
		confFileName = null==confFileName?
			      ces.sdk.util.CesGlobals.getInstance().getConfigPath()+"/"+DEFAULT_CONF_FILE_NAME:confFileName;
		File file = new File(confFileName);
		return file.exists();
	}
	
	@SuppressWarnings("unchecked")
	private void parseXML(){
		FileInputStream fis = null;
		SAXReader reader = new SAXReader();
		try {
			confFileName = null==confFileName?
					      ces.sdk.util.CesGlobals.getInstance().getConfigPath()+"/"+DEFAULT_CONF_FILE_NAME:confFileName;
	        fis = new FileInputStream(confFileName);
	        Document document = reader.read(fis);
	        Element root = document.getRootElement();
	        Iterator iter = root.elementIterator(SYSTEM_SDK_CONF);
	        while(iter.hasNext()){
	        	Element itemEle = (Element) iter.next();
	        	setEntityFactoryClassName(itemEle.elementTextTrim(ENTITY_FACTORY_CLASS_NAME));
	        	setFacadeFactoryClassName(itemEle.elementTextTrim(FACADE_FACTORY_CLASS_NAME));
	        	setSdkDataSourceClassName(itemEle.elementTextTrim(SDK_DATASOURCE_CLASS_NAME));
	        }
		} catch (Exception e) {
			throw new NullPointerException(e.getMessage()+"\r\n 解析system-sdk-conf.xml 出错");
		} finally {
			if(null!=fis)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	private void init() {
		parseXML();
	}
	public String getEntityFactoryClassName() {
		log.debug("entityFactoryClassName = " + entityFactoryClassName);
		return entityFactoryClassName;
	}

	public void setEntityFactoryClassName(String entityFactoryClassName) {
		this.entityFactoryClassName = entityFactoryClassName;
	}

	public String getFacadeFactoryClassName() {
		log.debug("facadeFactoryClassName = " + facadeFactoryClassName);
		return facadeFactoryClassName;
	}

	public void setFacadeFactoryClassName(String facadeFactoryClassName) {
		this.facadeFactoryClassName = facadeFactoryClassName;
	}

	public String getSdkDataSourceClassName() {
		log.debug("sdkDataSourceClassName = " + sdkDataSourceClassName);
		return sdkDataSourceClassName;
	}

	public void setSdkDataSourceClassName(String sdkDataSourceClassName) {
		this.sdkDataSourceClassName = sdkDataSourceClassName;
	}
	
	public String getAuthEngineClassName() {
		return "";
	}
	public void setAuthEngineClassName(String sdkDataSourceClassName) {
	}
	
}
