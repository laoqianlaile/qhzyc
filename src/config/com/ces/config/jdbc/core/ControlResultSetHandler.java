/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-3-9 下午3:36:08
 * <p>描述: TODO(用一句话描述该文件做什么)</p>
 */
package com.ces.config.jdbc.core;

import java.sql.ResultSet;

/**
 * @author qiucs
 *
 */
public interface ControlResultSetHandler {
	
	/**
	 * qiucs 2015-5-11 下午5:13:30
	 * <p>描述: 数据封装处理 </p>
	 * @return boolean
	 *        false--终止数据封装
	 *        true --继续数据封装
	 */
	boolean processRowData(ResultSet rs);

}
