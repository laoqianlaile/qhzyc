package ces.sdk.system.dao;

import ces.sdk.system.bean.RoleResInfo;

/**
 * Created by 黄翔宇 on 15/6/29.
 */
public interface RoleResInfoDao {

	enum RoleResIdType {
		ROLE, RES
	}

	void delete(String id, RoleResIdType roleResIdType);

	RoleResInfo save(RoleResInfo roleResInfo);

}
