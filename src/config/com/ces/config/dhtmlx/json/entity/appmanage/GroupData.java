/**
 * <p>Copyright:Copyright(c) 2013</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.ces.config.dhtmlx.json.entity.appmanage</p>
 * <p>文件名:PrintData.java</p>
 * <p>类更新历史信息</p>
 * @todo Administrator 创建于 2013-10-14 12:35:35
 */
package com.ces.config.dhtmlx.json.entity.appmanage;

import java.util.List;

/**
 * .
 * <p>描述:</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Administrator
 * @date 2013-10-14  12:35:35
 * @version 1.0.2013.1014
 */
public class GroupData {
	
	List<PrintCell> group;
	
	List<List<PrintCell>> data;

	/**
	 * <p>获取属性group.</p>
	 * @return List<PrintCell>
	 * @author Administrator
	 * @date 2013-10-14  12:39:47
	 */
	public List<PrintCell> getGroup() {
		return group;
	}

	/**
	 * 设置属性group.
	 * @param group 
	 * @author Administrator
	 * @date 2013-10-14  12:39:47
	 */
	public void setGroup(List<PrintCell> group) {
		this.group = group;
	}

	/**
	 * <p>获取属性data.</p>
	 * @return List<List<PrintCell>>
	 * @author Administrator
	 * @date 2013-10-14  12:39:47
	 */
	public List<List<PrintCell>> getData() {
		return data;
	}

	/**
	 * 设置属性data.
	 * @param data 
	 * @author Administrator
	 * @date 2013-10-14  12:39:47
	 */
	public void setData(List<List<PrintCell>> data) {
		this.data = data;
	}


}
