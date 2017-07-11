package ces.sdk.system.bean;

/**
 * Created by 黄翔宇 on 15/6/25.
 * 用户角色关联对象
 */
public class RoleUserInfo {

	private String id;
	private String roleId;
	private String userId;
	private String isTempAccredit;
	private String dateStart;
	private String dateEnd;
	private String systemId;
	private String orgId;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIsTempAccredit() {
		return isTempAccredit;
	}

	public void setIsTempAccredit(String isTempAccredit) {
		this.isTempAccredit = isTempAccredit;
	}

	public String getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	/**
	 * @return 返回  String systemId
	 */
	public String getSystemId() {
		return systemId;
	}

	/**
	 * @param 设置  String systemId
	 */
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	
}
