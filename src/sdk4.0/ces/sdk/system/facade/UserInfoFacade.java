package ces.sdk.system.facade;

import java.util.List;
import java.util.Map;

import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.exception.SystemFacadeException;

public interface UserInfoFacade {
	
	/**
	 * 分页查询
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @date 2015年6月1日 下午3:25:32
	 * @param param 分页查询条件 (key是查询关键字,value是关键字的值)
	 * @param currentPage 当前页
	 * @param pageSize 每页总量
	 */
	public List<UserInfo> find(Map<String, String> param, int currentPage, int pageSize);

	/**
	 * 分页查询 总数量
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @date 2015年6月1日 下午3:29:43
	 * @param param 分页查询条件 (key是查询关键字,value是关键字的值)
	 * @return
	 */
	public int findTotal(Map<String, String> param);

	/**
	 * 查询子用户
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> 查询子用户</p>
	 * @date 2015年6月1日 下午7:02:57
	 * @param parentId
	 * @return
	 */
	public List<UserInfo> findUsersByOrgId(String orgId);

	/**
	 * 根据ID查询查询唯一用户
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p>根据ID查询唯一对象 </p>
	 * @date 2015年6月4日 下午4:18:29
	 * @param id 用户id
	 * @return
	 */
	public UserInfo findByID(String id);

	/**
	 * 根据登录名查询唯一用户
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p>根据登录名查询唯一用户 </p>
	 * @date 2015年6月4日 下午4:18:29
	 * @param loginName 登录名
	 * @return
	 */
	public UserInfo findByLoginName(String loginName);

	
	/**
	 * 保存实体对象
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月4日 下午4:18:39
	 * @param userInfo
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	public UserInfo save(UserInfo userInfo,String orgId,String roleId);

	/**
	 * 更新实体对象
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月4日 下午4:18:54
	 * @param userInfo
	 * @return
	 */
	public UserInfo update(UserInfo userInfo);
	
	/**
	 * 获取在当前父节点下的最大值
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月4日 下午4:19:04
	 * @param parentId
	 * @return
	 */
	public long findMaxOrderNo(String parentId);

	/**
	 * 删除用户 (支持多条删除, 多条的话参数id的值用逗号分隔, 例:{id=101,102,103})
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月5日 上午10:52:56
	 * @param id 
	 */
	public void delete(String id);
	
	/**
	 * 删除用户 (支持多条删除, 多条的话参数id的值用逗号分隔, 例:{id=101,102,103})
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月5日 上午10:52:56
	 * @param id 
	 */
	public void delete(UserInfo userInfo);
	
	/**
	 * 删除用户 
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月5日 上午10:52:56
	 * @param id 
	 */
	public void delete(List<UserInfo> userInfos);


	/**
	 * 根据条件查询
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月15日 下午3:49:23
	 * @param param 查询条件(封装为map)
	 * @return
	 */
	public List<UserInfo> findByCondition(Map<String, String> param);

	/**
	 * 查询全部
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月18日 上午11:20:48
	 * @return
	 */
	public List<UserInfo> findAll();
	
	
	/**
	 * 获得 用户ID 组织ID 从而改变 OrgUser 表中的  OrgId 字段  
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月18日 上午11:20:48
	 * @return
	 */
	public void delete(String userId , String orgId,String userType);
	
	
	/**
	 * 为用户解除绑定的系统角色
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月2日 下午3:24:46
	 * @param userId
	 * @param orgId
	 * @param systemId
	 * @param roleId
	 */
	public void unGrantRole(String userId, String systemId, String roleId,String orgId);

	/**
	 * 为用户绑定系统角色
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月2日 下午2:55:18
	 * @param userId 用户id
	 * @param roleId 要绑定的角色id
	 * @param orgId 要绑定的组织id
	 * @param systemId 要绑定的系统id
	 */
	public void grantRole(String userId, String roleId, String systemId,String orgId);

	/**
	 * 批量为用户绑定系统角色
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月2日 下午3:21:56
	 * @param userId 用户
	 * @param orgId 组织ID
	 * @param roleSystemIds roleSystemIds 角色系统, key是角色id, value是系统id. 代表角色对应的系统
	 */
	public void grantRoleBatch(String userId, Map<String, String> roleSystemIds,String orgId);
	
	/**
	 * 用户兼职
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月2日 下午3:21:56
	 * @param userId 用户
	 * @param orgIDs 待兼职的组织ID(多个之间用逗号分割)
	 */
	public void parttime(String userId, String addParttimeOrgIds, String removeParttimeOrgIds);

	/**
	 * 重置用户密码
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月3日 下午7:38:56
	 * @param userId
	 */
	public void resetUserPasswd(String userId);

	/**
	 * 更新用户密码
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月3日 下午7:39:03
	 * @param loginName
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	public int updateUserPasswd(String loginName, String oldPassword, String newPassword);
	
	/**
	 * 获得该角色下的所有的用户信息，不对该角色是否存在进行判断．
	 * 
	 * @param roleId 角色ID;
	 * @param sfContext 系统访问上下文
	 * @return:如果该角色下没有用户，返回size为０的List;
	 * @throws SystemFacadeException
	 */
	public List<UserInfo> findUserInfosByRoleId(String roleId)
			throws SystemFacadeException;

	public void joinOrg(String userIds, String orgId);
	
}
