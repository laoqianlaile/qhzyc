package com.ces.xarch.core.security.vo;

import java.util.ArrayList;
import java.util.List;

import com.ces.xarch.core.security.entity.SysRole;

/**
 * 系统角色VO类
 * <p>描述:</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
 * @date 2015年7月1日 上午10:57:43
 * @version 1.0.2015.0601
 */
public class SystemRoleVO {

	/** id */
	private String id;
	/** 系统名称 */
	private String name;
	/** 系统编码 */
	private String code;
	/** 树节点类型  */
	private String treeNodeType = "system";
	/** 是否父节点 */
	private boolean isParent = true;
	/** 是否选中 */
	private boolean checked = false;
	/** 系统子角色 */
	private List<SysRole> children = new ArrayList<SysRole>();
	/** 系统角色id */
	private String systemRoleId;
	
	
	public SystemRoleVO() {
		super();
	}
	public SystemRoleVO(String id, String name, String code) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
	}
	/**
	 * @return 返回  String id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param 设置  String id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
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
	 * @return 返回  String code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param 设置  String code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return 返回  boolean isParent
	 */
	public boolean getIsParent() {
		return isParent;
	}
	/**
	 * @param 设置  boolean isParent
	 */
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
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
	/**
	 * @return 返回  List<SysRole> children
	 */
	public List<SysRole> getChildren() {
		return children;
	}
	/**
	 * @param 设置  List<SysRole> children
	 */
	public void setChildren(List<SysRole> children) {
		this.children = children;
	}
	/**
	 * @return 返回  String treeNodeType
	 */
	public String getTreeNodeType() {
		return treeNodeType;
	}
	/**
	 * @param 设置  String treeNodeType
	 */
	public void setTreeNodeType(String treeNodeType) {
		this.treeNodeType = treeNodeType;
	}
	/**
	 * @return 返回  String systemRoleId
	 */
	public String getSystemRoleId() {
		return systemRoleId;
	}
	/**
	 * @param 设置  String systemRoleId
	 */
	public void setSystemRoleId(String systemRoleId) {
		this.systemRoleId = systemRoleId;
	}
	
	
	
}
