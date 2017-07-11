package ces.sdk.system.dao;

import ces.sdk.system.bean.RoleInfo;
import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.exception.SystemFacadeException;

import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/6/29.
 */
public interface RoleInfoDao {

	/**
	 * 根据系统ID获得该系统下所有角色。
	 *
	 * @param systemId 系统id
	 * @param param
	 * @throws SystemFacadeException
	 * @return:
	 */
	List<RoleInfo> findRolesBySystemId(String systemId, Map<String, String> param);

	/***************************************************************************
	 * modify by ganminghui 2014-06-17
	 * <p/>
	 * 根据用户ID及系统标识获取角色列表
	 *
	 * @param userId  用户ID
	 * @param sysCode 系统编码
	 * @throws SystemFacadeException
	 * @return: List
	 */
	List<RoleInfo> findRoleInfosByUserId(String userId, String sysCode) throws SystemFacadeException;

	/***************************************************************************
	 * 根据用户获取角色列表信息(包含默认角色等角色)
	 *
	 * @param userId 待查询的用户ID
	 * @return List<RoleInfo> 返回查询的角色列表信息
	 * @throws SystemFacadeException
	 */
	List<RoleInfo> findRoleInfosByUserId(String userId) throws SystemFacadeException;

	/**
	 * @param paramMap
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @author vision(甘名辉 GanMingHui<a href="mailto:gan.minghui@cesgroup.com.cn">gan.minghui@cesgroup.com.cn</a>)
	 * @comments:<p> 修改数据权限</p>
	 * @date 2015年6月4日 下午4:50:10
	 */
	List<RoleInfo> find(Map<String, String> paramMap, int currentPage, int pageSize);

	/**
	 * 添加角色到系统
	 *
	 * @param roleInfo
	 */
	RoleInfo save(RoleInfo roleInfo);

	/**
	 * 获取最大值
	 * <p/>
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 *
	 * @return
	 * @author Synge
	 * @comments:<p> </p>
	 * @date 2015年6月23日
	 */
	long findMaxOrderNo(String systemId);

	/**
	 * 根据id获取
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 *
	 * @return
	 * @author Synge
	 * @comments:<p> </p>
	 * @date 2015年6月23日
	 */
	RoleInfo findByID(String id);

	/**
	 * 更新
	 *
	 * @param roleInfo
	 * @return
	 */
	RoleInfo update(RoleInfo roleInfo);

	/**
	 * 删除
	 *
	 * @param id
	 */
	void delete(String id);

	/**
	 * 查询系统下用户数量
	 *
	 * @param systemId
	 * @param param
	 * @return
	 */
	int findTotal(String systemId, Map<String, String> param);

	/**
	 * 分页获取角色
	 *
	 * @param systemId
	 * @param param
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	List<RoleInfo> findRolePageBySystemId(String systemId, Map<String, String> param, int currentPage, int pageSize);

	/**
	 * 根据角色获取其系统id
	 *
	 * @param roleId
	 * @return
	 */
	String findSystemIdByRole(String roleId);

	/**
	 * 根据roleid 获取资源总数
	 *
	 * @param roleId
	 * @return
	 */
	int findResourceTotal(String roleId, Map<String, String> param);

	/**
	 * 根据角色获取用户总数
	 *
	 * @param param
	 * @return
	 */
	int findUserInfosByRoleIdTotal(Map<String, String> param);

	/**
	 * 获取有角色的所有用户所有的用户(分页，查询条件)
	 *
	 * @param param
	 * @return
	 */
	List<UserInfo> findUserInfosByRoleIdPage(String roleId, Map<String, String> param, int currentPage, int pageSize);

	/**
	 * 用户是否具有某个角色
	 *
	 * @param roleId
	 * @param userId
	 * @return
	 */
	boolean hasRole(String roleId, String userId, String orgId);

	/**
	 * 角色下是否拥有某资源
	 *
	 * @param roleId
	 * @param resourceId
	 * @return
	 */
	boolean hasResource(String roleId, String resourceId);

	/**
	 * 移除角色下的资源
	 *
	 * @param roleId
	 * @param id
	 */
	void removeResource(String roleId, String id);

	/**
	 * 条件查询角色
	 *
	 * @param param
	 * @return
	 */
	List<RoleInfo> findByCondition(Map<String, String> param);

	/**
	 * 根据系统id和组织id查询角色
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月1日 下午4:40:25
	 * @param systemId
	 * @param parentOrgId
	 * @return
	 */
	List<RoleInfo> findRolesBysystemIdAndOrgId(String systemId, String parentOrgId);

}
