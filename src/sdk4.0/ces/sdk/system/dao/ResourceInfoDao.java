package ces.sdk.system.dao;

import ces.sdk.system.bean.ResourceInfo;
import ces.sdk.system.exception.SystemFacadeException;

import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/6/29.
 */
public interface ResourceInfoDao {

	/**
	 * 根据ID查询唯一对象
	 * @param id
	 * @return
	 */
	ResourceInfo findByID(String id);

	/**
	 * 分页查询 总数量
	 * @param param
	 * @return
	 */
	int findTotal(Map<String, String> param);

	/**
	 * 分页查询
	 * @param param
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	List<ResourceInfo> find(Map<String, String> param, int currentPage, int pageSize);

	/**
	 * 删除组织级别 (支持多条删除, 多条的话参数id的值用逗号分隔, 例:{id=101,102,103})
	 * @param id
	 */
	void delete(String id);

	/**
	 * 保存实体对象
	 * @param resourceInfo
	 * @return
	 */
	ResourceInfo save(ResourceInfo resourceInfo);

	/**
	 * 根据父节点找子节点
	 * @param parentId
	 * @return
	 */
	List<ResourceInfo> findChildsByParentId(String parentId);

	/**
	 * 更新实体对象
	 * @param resourceInfo
	 * @return
	 */
	ResourceInfo update(ResourceInfo resourceInfo);

	/**
	 * 获取在当前父节点下的最大值
	 * @param parentId
	 * @return
	 */
	long findMaxOrderNo(String parentId);

	/**
	 * 根据角色ID获得与该角色相关的所有的资源信息
	 * (系统框架必须)
	 * @param roleId 角色ID
	 * @throws SystemFacadeException
	 */
	List<ResourceInfo> findResInfosByRoleId(String roleId)
			throws SystemFacadeException;

	/**
	 * 根据条件查询
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author WILL(黄翔宇)
	 * @param param 查询条件
	 * @return 实体集合
	 */
	List<ResourceInfo> findByCondition(Map<String, String> param);

	/**
	 * 根据系统获取所有的资源总数
	 * @param systemId
	 * @param param
	 * @return
	 */
	int findResourcesTotalBySystemId(String systemId, Map<String, String> param);

	/**
	 * 根据系统获取所有的资源
	 * @param systemId
	 * @return
	 */
	List<ResourceInfo> findResourcesPageBySystemId(String systemId, Map<String, String> param, int currentPage, int pageSize);

	/**
	 * 根据系统获取所有的资源
	 * @param systemId
	 * @return
	 */
	List<ResourceInfo> findResourcesBySystemId(String systemId, Map<String, String> param);

	/**
	 * 根据资源ID获取对应的系统ID
	 * @param resourceId
	 * @return
	 */
	String findSystemIdByResource(String resourceId);

	/**
	 * 移动资源
	 * @param newPid
	 * @param resIds
	 */
	void moveResource(String newPid, String[] resIds);

	/**
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月3日 下午3:57:34
	 * @param userId
	 * @param systemId
	 * @return
	 */
	List<ResourceInfo> findResourcesByUserIdAndSystemId(String userId, String systemId);

	List<ResourceInfo> findResourcesPageByRoleId(String roleId, Map<String, String> param, int currentPage, int pageSize);

	/**
	 * 根据用户id和系统code获取该用户的资源权限
	 * @param userId
	 * @param systemCode
	 * @return
	 */
	List<ResourceInfo> findResourceByUserIdAndSystemCode(String userId, String systemCode);

	List<ResourceInfo> findResourceByUserIdAndParentId(String userId, String parentId);

	/**
	 * 根据资源的key值返回资源
	 * @param resourceKey
	 * @return
	 */
	ResourceInfo findByKey(String resourceKey);
}
