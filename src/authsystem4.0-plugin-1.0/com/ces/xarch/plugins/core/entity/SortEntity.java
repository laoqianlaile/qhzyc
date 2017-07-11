package com.ces.xarch.plugins.core.entity;

/**
 * 
 * 排序辅组entity.
 * <p>描述:排序辅助字段类</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author 方俊新(fang.junxin@cesgroup.com.cn)
 * @date 2015-4-29 17:12:24
 * @version 1.0.2015.0429
 */
public class SortEntity {

	/**排序当前id*/
	private String curItemId;
	/** 排序的前一个项目的id*/
	private String preItemId;
	/** 排序的后一个项目的id*/
	private String nextItemId;
	/**排序前id*/
	private String sortBeforeIDs;
	/**排序后id*/
	private String sortAfterIDs;
	
	public String getSortBeforeIDs() {
		return sortBeforeIDs;
	}

	public void setSortBeforeIDs(String sortBeforeIDs) {
		this.sortBeforeIDs = sortBeforeIDs;
	}

	public String getSortAfterIDs() {
		return sortAfterIDs;
	}

	public void setSortAfterIDs(String sortAfterIDs) {
		this.sortAfterIDs = sortAfterIDs;
	}
	
	public String getCurItemId() {
		return curItemId;
	}

	public void setCurItemId(String curItemId) {
		this.curItemId = curItemId;
	}

	public String getPreItemId() {
		return preItemId;
	}

	public void setPreItemId(String preItemId) {
		this.preItemId = preItemId;
	}
	public String getNextItemId() {
		return nextItemId;
	}

	public void setNextItemId(String nextItemId) {
		this.nextItemId = nextItemId;
	}

}
