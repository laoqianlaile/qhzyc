package com.ces.xarch.core.security.entity;

import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.plugins.core.entity.ISortEntity;

public class SysRole extends StringIDEntity implements ISortEntity {

	/** 角色名称 */
	private String name;
	/** 备注 */
	private String comments;
	/** 角色代码 */
	private String roleKey;
	/** 排序号 */
	private Long showOrder;
	/** 绑定组织级别 */
	private String bindOrgType;
	
	/** 树节点类型 */
	private String treeNodeType = "role";
	/** 父节点id, 一般为系统 */
	private String parentId;
	/** 是否选中 */
	private boolean checked = false;
	
	/**
	 * @return 返回  String name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param 设置  String name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return 返回  String comments
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param 设置  String comments
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * @return 返回  String roleKey
	 */
	public String getRoleKey() {
		return roleKey;
	}
	/**
	 * @param 设置  String roleKey
	 */
	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}
	/**
	 * @return 返回  Long showOrder
	 */
	public Long getShowOrder() {
		return showOrder;
	}
	/**
	 * @param 设置  Long showOrder
	 */
	public void setShowOrder(Long showOrder) {
		this.showOrder = showOrder;
	}
	/**
	 * @return 返回  String bindOrgType
	 */
	public String getBindOrgType() {
		return bindOrgType;
	}
	/**
	 * @param 设置  String bindOrgType
	 */
	public void setBindOrgType(String bindOrgType) {
		this.bindOrgType = bindOrgType;
	}

	public String getTreeNodeType() {
		return treeNodeType;
	}

	public void setTreeNodeType(String treeNodeType) {
		this.treeNodeType = treeNodeType;
	}
	/**
	 * @return 返回  String parentId
	 */
	public String getParentId() {
		return parentId;
	}
	/**
	 * @param 设置  String parentId
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	/**
	 * @return 返回  boolean checked
	 */
	public boolean isChecked() {
		return checked;
	}
	/**
	 * @param 设置  boolean checked
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	
}
