/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-1-20 上午11:57:52
 * <p>描述: </p>
 */
package com.ces.config.application;

import java.util.List;

import com.ces.config.dhtmlx.entity.code.Code;

public abstract class CodeApplication {

	/**
	 * qiucs 2015-1-20 下午6:16:45
	 * <p>描述: 根据名称获取编码 </p>
	 * @return String
	 */
	public abstract String getCodeValue(String name);
	
	/**
	 * qiucs 2015-1-20 下午6:16:56
	 * <p>描述: 根据编码获取名称 </p>
	 * @return String
	 */
	public abstract String getCodeName(String value);
	
	/**
	 * qiucs 2015-1-20 下午6:17:01
	 * <p>描述: 获取编码列表 </p>
	 * @return List<Code>
	 */
	public abstract List<Code> getCodeList(String codeTypeCode);
	
	/**
	 * qiucs 2015-1-20 下午6:17:06
	 * <p>描述: 获取编码树型数据 </p>
	 * @return Object
	 */
	public abstract Object getCodeTree(String codeTypeCode);
	
	/**
	 * qiucs 2015-1-20 下午6:17:06
	 * <p>描述: 获取下拉列表配置 </p>
	 * @return Object
	 */
	public abstract Object getCodeGrid(String codeTypeCode);
}
