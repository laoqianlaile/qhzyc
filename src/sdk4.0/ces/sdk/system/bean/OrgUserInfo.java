package ces.sdk.system.bean;

public class OrgUserInfo {
	
	/** 组织用户关联ID */
	private String id;
	/** 组织ID */
	private String orgId;
	/** 用户ID */
	private String userId;
	/** 用户排序 */
	private Integer userShowOrder;
	/** 关系类型 */
	private String userType;
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
	 * @return 返回  String orgId
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * @param 设置  String orgId
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return 返回  String userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param 设置  String userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return 返回  Integer userShowOrder
	 */
	public Integer getUserShowOrder() {
		return userShowOrder;
	}
	/**
	 * @param 设置  Integer userShowOrder
	 */
	public void setUserShowOrder(Integer userShowOrder) {
		this.userShowOrder = userShowOrder;
	}
	/**
	 * @return 返回  String userType
	 */
	public String getUserType() {
		return userType;
	}
	/**
	 * @param 设置  String userType
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	
}
