package ces.sdk.system.dao;

import ces.sdk.system.bean.SystemRoleInfo;

/**
 * Created by 黄翔宇 on 15/6/29.
 * 系统角色
 */
public interface SystemRoleInfoDao {


	enum SystemRoleIdType {
		SYSTEM, ROLE
	}

	/**
	 * 删除系统角色关系
	 * @param id
	 * @param systemRoleIdType
	 */
	void delete(String id, SystemRoleIdType systemRoleIdType);

	/**
	 * 保存系统角色关系
	 * @param systemRoleInfo
	 */
	SystemRoleInfo save(SystemRoleInfo systemRoleInfo);
}