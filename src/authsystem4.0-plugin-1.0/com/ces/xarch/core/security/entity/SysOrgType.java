package com.ces.xarch.core.security.entity;



import java.util.ArrayList;
import java.util.List;

import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.plugins.core.entity.ISortEntity;

/**
 * 组织级别实体类
 * <p>描述:组织级别实体类</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
 * @date 2015-6-1 10:21:55
 * @version 1.0.2015.0601
 */
public class SysOrgType extends StringIDEntity implements ISortEntity{


	private static final long serialVersionUID = 4064081672647411469L;
	
	/** 父id */
	private String parentId;
	/** 名称 */
	private String name;
	/** 显示顺序 */
	private Long showOrder;
	/** 是否父节点 */
	private boolean isParent = false;
	
	
	//====================================================
	private List<SysOrgType> children  = new ArrayList<SysOrgType>();
	
	public List<SysOrgType> getChildren() {
		return children;
	}
	public void setChildren(List<SysOrgType> list) {
		this.children = list;
	}
	
	//====================================================
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
		
	
	
}
