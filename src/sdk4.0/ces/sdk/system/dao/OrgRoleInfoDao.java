package ces.sdk.system.dao;

import java.util.Set;

import ces.sdk.system.bean.OrgRoleInfo;

public interface OrgRoleInfoDao {

	/**
	 * 保存实体对象
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015-6-29 10:23:54
	 * @param userInfo
	 * @return
	 */
	public OrgRoleInfo save(OrgRoleInfo orgRoleInfo);
	
	/**
	 * 保存实体对象
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015-6-29 10:23:54
	 * @param userInfo
	 * @return
	 */
	public void save(String orgId, String systemId, String roleId);

	/**
	 * 删除组织用户(id可为多个)
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015-6-29 10:23:51
	 * @param id 
	 */
	public void delete(String id);
	
	/**
	 * 根据组织id或角色id或用户类型删除组织用户
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015-6-29 10:23:48
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	public int delete(String orgId, String roleId,String systemId);
	
	/**
	 * 根据组织id或角色id查询总数
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月29日 上午10:37:21
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	public int findTotal(String orgId,String roleId, String systemId);

	/**
	 * 根据组织id查系统id, 并去重复
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月1日 下午2:47:14
	 * @param orgId
	 * @return
	 */
	public Set<String> findSystemIdsByOrgId(String orgId);
	
	/**
	 * 根据组织id查角色id, 并去重复
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月1日 下午2:47:14
	 * @param orgId
	 * @return
	 */
	public Set<String> findRoleIdsByOrgId(String orgId) ;
}
