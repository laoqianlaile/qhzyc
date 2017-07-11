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
public interface ResultSetHandler {
	
	void processRowData(ResultSet rs);

}
