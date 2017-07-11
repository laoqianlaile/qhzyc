package ces.sdk.system.facade;

import ces.sdk.system.bean.RoleInfo;
import ces.sdk.system.bean.RoleUserInfo;
import ces.sdk.system.exception.SystemFacadeException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * 角色操作代理接口。<br>
 * 角色的增、删、改、查等操作
 *
 * @author User
 */
public interface RoleInfoFacade {

	/**
	 * 获得所有角色信息
	 *
	 * @return 元素内容为 RoleInfo
	 * @throws SystemFacadeException
	 */
	public List<RoleInfo> findAllRoleInfos();


	/**
	 * 根据系统ID获得该系统下所有角色。
	 *
	 * @param systemId 系统id
	 * @param param
	 * @throws SystemFacadeException
	 * @return:
	 */
	public List<RoleInfo> findRolesBySystemId(String systemId, Map<String, String> param);

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
	public List<RoleInfo> findRoleInfosByUserId(String userId, String sysCode) throws SystemFacadeException;

	/***************************************************************************
	 * 根据用户获取角色列表信息(包含默认角色等角色)
	 *
	 * @param userId 待查询的用户ID
	 * @return List<RoleInfo> 返回查询的角色列表信息
	 * @throws SystemFacadeException
	 */
	public List<RoleInfo> findRoleInfosByUserId(String userId) throws SystemFacadeException;

	/**
	 * @param paramMap
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @author vision(甘名辉 GanMingHui<a href="mailto:gan.minghui@cesgroup.com.cn">gan.minghui@cesgroup.com.cn</a>)
	 * @comments:<p> 查询</p>
	 * @date 2015年6月4日 下午4:50:10
	 */
	public List<RoleInfo> find(Map<String, String> paramMap, int currentPage, int pageSize);

	/**
	 * 添加角色到系统
	 *
	 * @param roleInfo
	 * @param systemId
	 */
	public RoleInfo save(RoleInfo roleInfo, String systemId);

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
	public long findMaxOrderNo(String systemId);

	/**
	 * 根据id获取
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 *
	 * @return
	 * @author Synge
	 * @comments:<p> </p>
	 * @date 2015年6月23日
	 */
	public RoleInfo findByID(String id);

	/**
	 * 更新
	 *
	 * @param roleInfo
	 * @return
	 */
	public RoleInfo update(RoleInfo roleInfo);

	/**
	 * 删除
	 *
	 * @param id
	 */
	public void delete(String id);

	/**
	 * 查询系统下用户数量
	 *
	 * @param systemId
	 * @param param
	 * @return
	 */
	public int findTotal(String systemId, Map<String, String> param);

	/**
	 * 分页获取角色
	 *
	 * @param systemId
	 * @param param
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<RoleInfo> findRolePageBySystemId(String systemId, Map<String, String> param, int currentPage, int pageSize);

	/**
	 * 根据系统获取角色
	 *
	 * @param sysCode
	 * @return
	 * @throws SystemFacadeException
	 */
	public List<RoleInfo> findRolesBySysCode(String sysCode);

	/**
	 * 根据角色获取其系统id
	 *
	 * @param roleId
	 * @return
	 */
	public String findSystemIdByRole(String roleId);

	/**
	 * 根据roleid 获取资源总数
	 *
	 * @param roleId
	 * @return
	 */
	public int findResourceTotal(String roleId, Map<String, String> param);

	/**
	 * 为角色授予资源
	 *
	 * @param roleId
	 * @param resIds
	 */
	public void grantResource(String roleId, String resIds);

	/**
	 * 根据角色获取用户总数
	 *
	 * @param param
	 * @return
	 */
	public int findUserInfosByRoleIdTotal(Map<String, String> param);

	/**
	 * 获取有角色的所有用户所有的用户(分页，查询条件)
	 *
	 * @param param
	 * @return
	 */
	public List<RoleUserInfo> findUserInfosByRoleIdPage(Map<String, String> param, int currentPage, int pageSize);

	/**
	 * 批量授权用户角色
	 *
	 * @param roleId
	 * @param userAndOrgIds
	 */
	public void authorizeUser(String roleId, List<Map<String, String>> userAndOrgIds, String isTempAccredit, String dateStart, String dateEnd);

	/**
	 * 授权单个用户角色
	 *
	 * @param roleId
	 * @param userId
	 */
	public void authorizeUser(String roleId, String userId,String orgId, String isTempAccredit, String dateStart, String dateEnd);

	/**
	 * 用户是否具有某个角色
	 *
	 * @param roleId
	 * @param userId
	 * @return
	 */
	public boolean hasRole(String roleId, String userId, String orgId);

	/**
	 * 批量移除用户授权
	 *
	 * @param roleId
	 * @param userAndOrgIds
	 */
	public void unAuthorizeUser(String roleId, List<Map<String,String>> userAndOrgIds);

	/**
	 * 移除单个用户授权
	 *
	 * @param roleId
	 * @param userId
	 * @param orgId
	 */
	public void unAuthorizeUser(String roleId, String userId, String orgId);

	/**
	 * 角色下是否拥有某资源
	 *
	 * @param roleId
	 * @param resourceId
	 * @return
	 */
	public boolean hasResource(String roleId, String resourceId);

	/**
	 * 移除角色下的资源
	 *
	 * @param roleId
	 * @param id
	 */
	public void removeResource(String roleId, String id);

	/**
	 * 条件查询角色
	 *
	 * @param param
	 * @return
	 */
	public List<RoleInfo> findByCondition(Map<String, String> param);

	/**
	 * 获取全部要勾选的系统id和角色id, map结构为:
	 * { key : systemId, value : Set<String> systemIds,
	 * 	 key : roleId, value : Set<String> roleIds
	 * }
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月1日 下午2:40:47
	 * @param orgId
	 * @return
	 */
	public Map<String, Set<String>> findCheckedSystemAndRoleByOrgId(String orgId);

	/**
	 * 
	 * 根据系统id和组织id查询角色
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月1日 下午2:40:47
	 * @param orgId
	 * @return
	 */
	public List<RoleInfo> findRolesBysystemIdAndOrgId(String systemId, String parentOrgId);

	/**
	 * 获取全部要勾选的系统id和角色id, map结构为:
	 * { key : systemId, value : Set<String> systemIds,
	 * 	 key : roleId, value : Set<String> roleIds
	 * }
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月1日 下午2:40:47
	 * @param userId
	 * @return
	 */
	public Map<String, Set<String>> findCheckedSystemAndRoleByUserId(String userId);
}
