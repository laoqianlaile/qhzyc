/***************
   中信组件库
 ***************/
package ces.sdk.util.file;

/**
 * <p>文件名:CESProperties.java </p>
 * <p>功能描述:实现多种方式装载的Properties类 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 上海中信信息发展有限公司(CES)</p>
 * @author 郑国强
 * @version 1.0.2003.0911
 */

import java.util.Properties;
import java.io.*;
import java.net.URL;

public class CESProperties extends Properties {

	/**
	 * 构造函数1
	 * @param defaults
	 */
	public CESProperties() {

		super();
	}

	/**
	 * 构造函数2
	 * @param defaults
	 */
	public CESProperties(Properties defaults) {
		super(defaults);
	}

	/**
	 *  通过路径装载属性文件（根目录是classes）
	 *  例如：  prop.load("/bb.properties");
	 *  就是在：D:\Program Files\Apache Group\Tomcat 4.1\webapps\cescom\
	 *            WEB-INF\classes\bb.properties
	 * @param name
	 */
	public void load(String name) {
		try {
			super.load(getClass().getResourceAsStream(name));
		} catch (IOException iex) {
			new IOException("不能读取属性文件！请确保属性文件在相应的路径中！");
		}
	}

	/**
	 *  通过URL装载属性文件
	 * @param url 如http:\\192.10.5.8\\D:\\eclipse\\eclipse\\workspace\\zjkb\\config\\system.properties
	 */
	public void load(URL url) {
		try {
			super.load(url.openStream());
		} catch (IOException iex) {
			new IOException("不能读取属性文件！请确保属性文件在相应的路径中！");
		}
	}

	/**
	 *  装载属性文件
	 * @param file
	 */
	public void load(File file) {
		try {
			super.load(new FileInputStream(file));
		} catch (IOException iex) {
			new IOException("不能读取属性文件！请确保属性文件在相应的路径中！");
		}

	}

}