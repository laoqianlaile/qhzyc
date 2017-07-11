package ces.sdk.system.bean;

import java.util.Map;

/**
 * 角色.<br>
 * @author Administrator
 *
 */
public class RoleInfo {

	private String id;
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
	

}
