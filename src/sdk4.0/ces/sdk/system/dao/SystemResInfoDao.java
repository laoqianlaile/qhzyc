package ces.sdk.system.dao;

import ces.sdk.system.bean.SystemResInfo;

/**
 * Created by 黄翔宇 on 15/6/29.
 * 系统资源
 */
public interface SystemResInfoDao {

	enum SystemResIdType {
		SYSTEM, RES
	}

	/**
	 * 删除系统资源关联数据
	 * @param id
	 * @param systemResIdType
	 */
	void delete(String id, SystemResIdType systemResIdType);

	/**
	 * 保存系统资源关联数据
	 * @param systemResInfo
	 * @return
	 */
	SystemResInfo save(SystemResInfo systemResInfo);

}
