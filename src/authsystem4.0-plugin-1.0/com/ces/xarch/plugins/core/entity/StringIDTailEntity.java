package com.ces.xarch.plugins.core.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * 有尾巴的实体超类.
 * <p>描述:主要是创建人、创建日期、修改人、修改日期等字段</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author 管俊 GuanJun <a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>
 * @date 2015-4-24 16:03:40
 * @version 1.0.2015.0424
 */
@MappedSuperclass
public class StringIDTailEntity extends StringIDEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 排序号*/
	private Long orderNo;

	/** 要排序的id*/
	private String itemId;
	/** 排序的前一个项目的id*/
	private String preItemId;
	/** 排序的后一个项目的id*/
	private String nextItemId;
	/**排序前id*/
	private String sortBeforeIDs;
	/**排序后id*/
	private String sortAfterIDs;
	
	
	@JsonIgnore
	@Transient
	public String getSortBeforeIDs() {
		return sortBeforeIDs;
	}

	public void setSortBeforeIDs(String sortBeforeIDs) {
		this.sortBeforeIDs = sortBeforeIDs;
	}

	@JsonIgnore
	@Transient
	public String getSortAfterIDs() {
		return sortAfterIDs;
	}

	public void setSortAfterIDs(String sortAfterIDs) {
		this.sortAfterIDs = sortAfterIDs;
	}

	@JsonIgnore
	@Transient
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@JsonIgnore
	@Transient
	public String getPreItemId() {
		return preItemId;
	}

	public void setPreItemId(String preItemId) {
		this.preItemId = preItemId;
	}
	@JsonIgnore
	@Transient
	public String getNextItemId() {
		return nextItemId;
	}

	public void setNextItemId(String nextItemId) {
		this.nextItemId = nextItemId;
	}

	public Long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

}
