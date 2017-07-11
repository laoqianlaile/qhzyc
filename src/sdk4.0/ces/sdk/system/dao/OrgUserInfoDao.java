package ces.sdk.system.dao;

import java.util.List;
import java.util.Map;

import ces.sdk.system.bean.OrgTypeInfo;
import ces.sdk.system.bean.OrgUserInfo;
import ces.sdk.system.common.CommonConst;

public interface OrgUserInfoDao {

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
	public OrgUserInfo save(OrgUserInfo orgUserInfo);

	/**
	 * 删除组织用户(id可为多个)
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月5日 上午10:52:56
	 * @param id 
	 */
	public void delete(String id);
	
	/**
	 * 根据组织id或用户id或用户类型删除组织用户
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月28日 下午4:31:42
	 * @param orgId
	 * @param userId
	 * @param userType
	 * @return
	 */
	public int delete(String orgId, String userId, String userType);
	
	/**
	 * 根据组织id或用户id或用户类型查询组织用户
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月7日 上午10:47:50
	 * @param orgId
	 * @param userId
	 * @return
	 */
	public List<OrgUserInfo> findByCondition(Map<String,String> param);

	public void changeOrgUser(String userId,String orgId,String userType); 


}
