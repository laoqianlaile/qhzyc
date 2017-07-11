package ces.sdk.system.facade;

import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.bean.OrgUserInfo;
import ces.sdk.system.bean.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * 组织操作代理接口。<br>
 * @author Administrator
 *
 */
public interface OrgInfoFacade {


	/**
	 * 分页查询
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @date 2015年6月1日 下午3:25:32
	 * @param param 分页查询条件 (key是查询关键字,value是关键字的值)
	 * @param currentPage 当前页
	 * @param pageSize 每页总量
	 */
	public List<OrgInfo> find(Map<String, String> param, int currentPage, int pageSize);

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
	 * 查询子组织级别
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> 查询子组织级别</p>
	 * @date 2015年6月1日 下午7:02:57
	 * @param parentId
	 * @return
	 */
	public List<OrgInfo> findChildsByParentId(String parentId);

	/**
	 * 根据ID查询唯一对象
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月4日 下午4:18:29
	 * @param id
	 * @return
	 */
	public OrgInfo findByID(String id);

	/**
	 * 保存实体对象
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月4日 下午4:18:39
	 * @param orgInfo
	 * @return
	 */
	public OrgInfo save(OrgInfo orgInfo);

	/**
	 * 更新实体对象
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月4日 下午4:18:54
	 * @param orgInfo
	 * @return
	 */
	public OrgInfo update(OrgInfo orgInfo);
	
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
	 * 删除组织级别 (支持多条删除, 多条的话参数id的值用逗号分隔, 例:{id=101,102,103})
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月5日 上午10:52:56
	 * @param id 
	 */
	public void delete(String id);
	
	/**
	 * 删除组织级别 (支持多条删除, 多条的话参数id的值用逗号分隔, 例:{id=101,102,103})
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月5日 上午10:52:56
	 * @param id 
	 */
	public void delete(OrgInfo orgInfo);
	
	/**
	 * 删除组织级别 
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月5日 上午10:52:56
	 * @param id 
	 */
	public void delete(List<OrgInfo> orgInfos);


	/**
	 * 根据条件查询
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月15日 下午3:49:23
	 * @param param 查询条件(封装为map)
	 * @return
	 */
	public List<OrgInfo> findByCondition(Map<String, String> param);

	/**
	 * 查询全部
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月18日 上午11:20:48
	 * @return
	 */
	public List<OrgInfo> findAll();

	/**
	 * 组织是否拥有该角色
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	boolean hasRole(String orgId, String roleId);
	
	
	/**
	 * 组织变更
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:
	 * @date 2015年4月28日下午5:31:15
	 * @param orgId 组织id
	 * @param parentId 变更到的父组织id
	 */
	public void changeOrg(String orgId, String parentId) ;

	/**
	 * 给组织关联角色
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月1日 下午3:56:31
	 * @param orgId
	 * @param systemId
	 * @param roleId
	 */
	public void grantRole(String orgId, String systemId, String roleId);
	
	/**
	 * 批量给组织授予系统角色 
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月2日 下午3:10:40
	 * @param orgId
	 * @param roleSystemIds 角色系统, key是角色id, value是系统id. 代表角色对应的系统
	 */
	public void grantRoleBatch(String orgId, Map<String,String> roleSystemIds);
	
	
	/**
	 * 给组织去除关联角色
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月1日 下午3:56:41
	 * @param orgId
	 * @param systemId
	 * @param roleId
	 */
	public void unGrantRole(String orgId, String systemId, String roleId);
	
	/**************************************************
	 *  @author 王见
	 *  根据用户ID和组织类型ID获取该用户对应的组织信息 , 不考虑用户是否是兼职 （那么可能得到一个用户在多个部门工作）
	 *  @param Integer UserID
	 *  @param String orgTypeId
	 *  @throws SystemFacadeException
	 */
	public List<OrgInfo> findOrgByTypeIdAndUserID(String userID , String orgTypeId);
	
	/**
	 * 根据用户该ID该用户对应的组织(兼职/专职)
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月2日 下午3:10:40
	 * @param Map<String,String> param
	 */
	public List<OrgUserInfo> getOrgUserByUserId(Map<String, String> param);
	
}
