package ces.sdk.system.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ces.sdk.system.bean.RoleUserInfo;

public interface RoleUserInfoDao {

	/**
	 * 保存实体对象
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月4日 下午4:18:39
	 * @param roleUserInfo
	 * @return
	 */
	public RoleUserInfo save(RoleUserInfo roleUserInfo);

	/**
	 * 删除角色用户(id可为多个)
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月5日 上午10:52:56
	 * @param id 
	 */
	public void delete(String id);
	
	/**
	 * 根据角色id或用户id删除组织用户
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月28日 下午4:31:02
	 * @param orgId
	 * @param userId
	 * @return
	 */
	public int delete(String roleId, String userId, String systemId,String orgId);

	/**
	 * 根据用户id查系统id, 并去重复
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月1日 下午2:47:14
	 * @param userId
	 * @return Set<String>
	 */
	public Set<String> findSystemIdsByUserId(String userId);
	
	/**
	 * 根据用户id查角色id, 并去重复
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月1日 下午2:47:14
	 * @param userId
	 * @return Set<String>
	 */
	public Set<String> findRoleIdsByUserId(String userId) ;

	/**
	 * 根据条件查找总数
	 * @param param
	 * @return
	 */
	int findByConditionTotal(Map<String, String> param);

	/**
	 * 根据条件查找
	 * @param param
	 * @return
	 */
	List<RoleUserInfo> findByConditionPage(Map<String, String> param, int currentPage, int pageSize);
}
