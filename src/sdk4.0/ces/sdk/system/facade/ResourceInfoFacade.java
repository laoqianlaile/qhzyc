package ces.sdk.system.facade;

import ces.sdk.system.bean.ResourceInfo;
import ces.sdk.system.bean.SystemInfo;
import ces.sdk.system.exception.SystemFacadeException;

import java.util.List;
import java.util.Map;

/**
 * 资源操作代理接口。<br>
 * 资源的增、删、改、查等操作
 * @author Administrator
 * 
 */
public interface ResourceInfoFacade {
	
	/**
	 * wj
	 * */
	public ResourceInfo findByID(String id);
	
	public int findTotal(Map<String, String> param);
	
	public List<ResourceInfo> find(Map<String, String> param, int currentPage, int pageSize);
	
	
	/**
	 * 删除组织级别 (支持多条删除, 多条的话参数id的值用逗号分隔, 例:{id=101,102,103})
	 * 
	 * wj cp from gj
	 */
	public void delete(String id);
	
	
	public ResourceInfo save(ResourceInfo resourceInfo);
	
	/**
	 * wj 查找当前节点的子节点
	 * */
	public List<ResourceInfo> findChildsByParentId(String parentId);
	
	/**
	 * 更新实体对象
	 * wj cp from gj
	 */
	public ResourceInfo update(ResourceInfo resourceInfo);
	
	
	/**
	 * 获取在当前父节点下的最大值
	 * 
	 * wj cp from gj
	 */
	public long findMaxOrderNo(String parentId);

	/**
	 * 根据角色ID获得与该角色相关的所有的资源信息 
	 * (系统框架必须)
	 * @param roleId 角色ID
	 * @return:如果该角色下没有权限，返回size为０的List;
	 * @throws SystemFacadeException
	 */
	public List<ResourceInfo> findResInfosByRoleId(String roleId)
			throws SystemFacadeException;

	/**
	 * 根据条件查询
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author WILL(黄翔宇)
	 * @param param 查询条件
	 * @return 实体集合
	 */
	public List<ResourceInfo> findByCondition(Map<String, String> param);

	/**
	 * 根据系统获取所有的资源
	 * @param systemId
	 * @param param
	 * @return
	 */
	public int findResourcesTotalBySystemId(String systemId, Map<String, String> param);

	/**
	 * 根据系统获取所有的资源
	 * @param systemId
	 * @return
	 */
	public List<ResourceInfo> findResourcesPageBySystemId(String systemId, Map<String, String> param, int currentPage, int pageSize);

	/**
	 * 根据系统获取所有的资源
	 * @param systemId
	 * @return
	 */
	public List<ResourceInfo> findResourcesBySystemId(String systemId, Map<String, String> param);

	/**
	 * 添加资源到系统
	 * @param resourceInfo
	 * @param systemId
	 * @return
	 */
	public ResourceInfo save(ResourceInfo resourceInfo, String systemId);

	/**
	 * 根据资源ID获取对应的系统ID
	 * @param resourceId
	 * @return
	 */
	public String findSystemIdByResource(String resourceId);

	/**
	 * 是否有子资源
	 * @return
	 */
	public boolean hasChild(String resourceId);

	/**
	 * 移动资源
	 * @param newPid
	 * @param resIds
	 */
	public void moveResource(String newPid, String[] resIds);

	/**
	 * 根据用户id查询拥有的资源
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月3日 下午3:53:19
	 * @param userId
	 * @return
	 */
	public List<ResourceInfo> findResourcesByUserIdAndSystemId(String userId, String systemId);
	

	/**
	 * 根据role id获取其资源
	 *
	 * @param roleId
	 * @param currentPage
	 * @param pageSize    
	 * @return
	 */
	public List<ResourceInfo> findResourcesPageByRoleId(String roleId, Map<String, String> param, int currentPage, int pageSize);

	/**
	 * 批量新增资源
	 * 配置平台使用（ID自带，不生成）
	 * @param resourceInfos
	 * @param systemId
	 */
	void save(List<ResourceInfo> resourceInfos, String systemId);

	/**
	 * 根据资源的key值返回资源
	 * @param resourceKey
	 * @return
	 */
	ResourceInfo findByKey(String resourceKey);

	/**
	 * 根据角色Id获取所有资源
	 * @param userId
	 * @return
	 */
	public List<ResourceInfo> getResrouceByUserId(String userId) throws SystemFacadeException;

	/**
	 * 根据角色id及系统code获取资源
	 * @param userId
	 * @param systemCode
	 * @return
	 */
	public List<ResourceInfo> findResourceByUserIdAndSystemCode(String userId, String systemCode);

	/**
	 * 根据用户id和某资源key获取该用户在该资源下的资源权限
	 * @param userId
	 * @param resKey
	 * @return
	 */
	public List<ResourceInfo> findResourceByUserIdAndResKey(String userId, String resKey);
}

