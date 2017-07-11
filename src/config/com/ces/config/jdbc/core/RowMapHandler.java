/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-3-9 下午2:53:08
 * <p>描述: TODO(用一句话描述该文件做什么)</p>
 */
package com.ces.config.jdbc.core;

import java.util.Map;

/**
 * @author qiucs
 *
 */
public interface RowMapHandler {

	void processRowData(Map<String, Object> rowMap);
}
