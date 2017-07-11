package ces.sdk.system.dao;

import java.util.List;
import java.util.Map;

import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.bean.UserInfo;

public interface OrgInfoDao {

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
	 * 删除组织
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
	 * 改变组织， 根据组织id来更新父节点id
	 * @param orgId
	 * @param parentId
	 */
	public void updateParentIdById(String orgId, String parentId);
	
	/**************************************************
	 *  @author 王见
	 *  根据用户ID和组织类型ID获取该用户对应的组织信息
	 *  @param Integer UserID
	 *  @param String orgTypeId
	 *  @throws SystemFacadeException
	 */
	public List<OrgInfo> findOrgByTypeIdAndUserID(String userID, String orgTypeId);
	
	
}
