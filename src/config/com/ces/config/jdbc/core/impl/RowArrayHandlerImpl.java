/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-4-10 下午2:41:52
 * <p>描述: TODO(用一句话描述该文件做什么)</p>
 */
package com.ces.config.jdbc.core.impl;

import java.util.ArrayList;
import java.util.List;

import com.ces.config.jdbc.core.RowArrayHandler;

/**
 * @author qiucs
 *
 */
public class RowArrayHandlerImpl implements RowArrayHandler {
	
	private List<Object[]> data = new ArrayList<Object[]>();

	/* qiucs 2015-4-10 下午2:45:12
	 * (non-Javadoc)
	 * @see com.ces.config.jdbc.core.RowMapHandler#processRowData(java.util.Map)
	 */
	@Override
	public void processRowData(Object[] objArr) {
		data.add(objArr);
	}
	
	public List<Object[]> getData() {
		return data;
	}

	public Object[] getSingleData() {
		return data.isEmpty() ? null : data.get(0);
	}
}
