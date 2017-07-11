package com.ces.xarch.core.security.entity;

import java.util.List;

import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.plugins.core.entity.ISortEntity;

public class SysSystem extends StringIDEntity implements ISortEntity{
	
	
	private static final long serialVersionUID = 4064081672647411469L;
	
	/** 系统编码 */
	private String code;
	/** 系统名称 */
	private String name;
	/** 备注 */
	private String comments;
	/** 排序号 */ 
	private Long showOrder;
	/** 是否父节点 */
	private boolean isParent = false;
	/** 是否可以选中 */
	private boolean nocheck = false;
	/** 树节点类型 */
	private String treeNodeType = "system";
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * @return the showOrder
	 */
	public Long getShowOrder() {
		return showOrder;
	}
	/**
	 * @param showOrder the showOrder to set
	 */
	public void setShowOrder(Long showOrder) {
		this.showOrder = showOrder;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @return the isParent
	 */
	public boolean getIsParent() {
		return isParent;
	}
	/**
	 * @param isParent the isParent to set
	 */
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	public boolean getNocheck() {
		return nocheck;
	}

	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}

	public String getTreeNodeType() {
		return treeNodeType;
	}

	public void setTreeNodeType(String treeNodeType) {
		this.treeNodeType = treeNodeType;
	}
	
	
}
