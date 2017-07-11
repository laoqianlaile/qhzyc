package ces.sdk.system.bean;

/**
 * Created by 黄翔宇 on 15/6/19.
 * 角色系统绑定接口
 */
public class SystemRoleInfo {

	/** id */
	private String id;

	/** 系统id */
	private String systemId;

	/** 角色id */
	private String roleId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
