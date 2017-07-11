/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-1-20 下午1:03:08
 * <p>描述: JAVA应用编码工具类</p>
 */
package com.ces.config.utils;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.BusinessCode;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.service.code.BusinessCodeService;
import com.ces.xarch.core.web.listener.XarchListener;

/**
 * @author qiucs
 *
 */
public class CodeApplicationUtil {
	
	private static Log log = LogFactory.getLog(CodeApplicationUtil.class);

	/**
	 * qiucs 2015-1-21 下午1:42:03
	 * <p>描述: 获取JAVA应用实例对象（编码类型编码） </p>
	 * @return CodeApplication
	 */
	public static CodeApplication getApplicationInstance(String codeTypeCode) {
		BusinessCode entity = XarchListener.getBean(BusinessCodeService.class).getByCodeTypeCode(codeTypeCode);
		return getApplicationInstance(entity);
	}
	
	/**
	 * qiucs 2015-1-21 下午1:41:23
	 * <p>描述: 获取JAVA应用实例对象（编码类型对象） </p>
	 * @return CodeApplication
	 */
	public static CodeApplication getApplicationInstance(BusinessCode entity) {
		String className = entity.getClassName();
		if (StringUtil.isEmpty(className)) return null;
		try {
			return (CodeApplication)Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			log.error("实例化类错误", e);
		} catch (IllegalAccessException e) {
			log.error("非法访问类", e);
		} catch (ClassNotFoundException e) {
			log.error("没有找到类", e);
		}
		return null;
	}
	
	/**
	 * qiucs 2015-1-21 下午1:42:50
	 * <p>描述: 获取编码列表 </p>
	 * @return List<Code>
	 */
	public static List<Code> getCodeList(String codeTypeCode) {
		CodeApplication application = getApplicationInstance(codeTypeCode);
		return application.getCodeList(codeTypeCode);
	}
	
	/**
	 * qiucs 2015-1-21 下午1:43:16
	 * <p>描述: 根据编码名称获取编码 </p>
	 * @return String
	 */
	public static String getCodeValue(String codeTypeCode, String name) {
		CodeApplication application = getApplicationInstance(codeTypeCode);
		return application.getCodeValue(name);
	}
	
	/**
	 * qiucs 2015-1-21 下午1:43:42
	 * <p>描述: 根据编码获取名称 </p>
	 * @return String
	 */
	public static String getCodeName(String codeTypeCode, String value) {
		CodeApplication application = getApplicationInstance(codeTypeCode);
		return application.getCodeName(value);
	}
	
	/**
	 * qiucs 2015-1-21 下午1:44:04
	 * <p>描述: 获取编码树型结构数据 </p>
	 * @return Object
	 */
	public static Object getCodeTree(String codeTypeCode) {
		CodeApplication application = getApplicationInstance(codeTypeCode);
		return application.getCodeTree(codeTypeCode);
	}
}
