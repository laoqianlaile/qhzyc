/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-4-10 下午1:54:52
 * <p>描述: TODO(用一句话描述该文件做什么)</p>
 */
package com.ces.config.jdbc.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ces.config.jdbc.core.RowMapHandler;

/**
 * @author qiucs
 *
 */
public class RowMapHandlerImpl implements RowMapHandler {
	
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

	/* qiucs 2015-4-10 下午2:08:35
	 * (non-Javadoc)
	 * @see com.ces.config.jdbc.core.RowMapHandler#processRowData(java.util.Map)
	 */
	@Override
	public void processRowData(Map<String, Object> rowMap) {
		data.add(rowMap);
	}
	
	public List<Map<String, Object>> getData() {
		return data;
	}

	public Map<String, Object> getSingleData() {
		return data.isEmpty() ? null : data.get(0);
	}
}
