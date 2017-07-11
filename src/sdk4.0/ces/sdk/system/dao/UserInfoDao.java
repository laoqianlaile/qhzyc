package ces.sdk.system.dao;

import java.util.List;
import java.util.Map;

import ces.sdk.system.bean.ResourceInfo;
import ces.sdk.system.bean.RoleInfo;
import ces.sdk.system.bean.UserInfo;

public interface UserInfoDao {

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
	 * @return
	 */
	public UserInfo save(UserInfo userInfo);

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
	 * 重置用户的密码
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月3日 下午7:22:49
	 * @param userId
	 */
	public void resetUserPasswd(String userId);

	public int updateUserPasswd(String loginName, String oldPassword,
			String newPassword);
	
	/**
	 * 获得该角色下的所有的用户信息，不对该角色是否存在进行判断．
	 * 
	 * @param roleId 角色ID;
	 * @param sfContext 系统访问上下文
	 * @return:如果该角色下没有用户，返回size为０的List;
	 * @throws SystemFacadeException
	 */
	public List<UserInfo> findUserInfosByRoleId(String roleId);

	/**
	 * 获取用户具有的角色
	 * @param userId
	 * @return 角色Id
	 */
	public List<String> getRoleByUserId(String userId);


}
