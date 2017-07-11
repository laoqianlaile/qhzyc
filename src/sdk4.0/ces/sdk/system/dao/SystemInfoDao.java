package ces.sdk.system.dao;

import ces.sdk.system.bean.SystemInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/6/29.
 */
public interface SystemInfoDao {

	/**
	 * 分页查询
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Synge
	 * @date 2015年6月17日
	 * @param param 分页查询条件 (key是查询关键字,value是关键字的值)
	 * @param currentPage 当前页
	 * @param pageSize 每页总量
	 */
	List<SystemInfo> find(Map<String, String> param, int currentPage, int pageSize);

	/**
	 * 分页查询 总数量
	 *
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Synge
	 * @date 2015年6月17日
	 * @param param 分页查询条件 (key是查询关键字,value是关键字的值)
	 * @return
	 */
	int findTotal(Map<String, String> param);

	/**
	 * 根据ID查询唯一对象
	 *
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Synge
	 * @comments:<p> </p>
	 * @date 2015年6月17日
	 * @param id
	 * @return
	 */
	SystemInfo findByID(String id);

	/**
	 * 保存实体对象
	 *
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Synge
	 * @comments:<p> </p>
	 * @date 2015年6月17日
	 * @param systemInfo
	 * @return
	 */
	SystemInfo save(SystemInfo systemInfo);

	/**
	 * 更新实体对象
	 *
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Synge
	 * @comments:<p> </p>
	 * @date 2015年6月17日
	 * @param systemInfo
	 * @return
	 */
	SystemInfo update(SystemInfo systemInfo);

	/**
	 * 删除系统 (支持多条删除, 多条的话参数id的值用逗号分隔, 例:{id=101,102,103})
	 *
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Synge
	 * @comments:<p> </p>
	 * @date 2015年6月17日
	 * @param id
	 */
	void delete(String id);


	/**
	 * 根据条件查询
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Synge
	 * @comments:<p> </p>
	 * @date 2015年6月17日
	 * @param param 查询条件(封装为map)
	 * @return
	 */
	List<SystemInfo> findByCondition(Map<String, String> param);

	/**
	 * 获取最大值
	 *
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Synge
	 * @comments:<p> </p>
	 * @date 2015年6月19日
	 * @return
	 */
	long findMaxOrderNo();

	/**
	 * 系统底下是否存在资源
	 * @return
	 */
	boolean hasResource(String systemId);

	/**
	 * 系统下是否存在角色
	 * @return
	 */
	boolean hasRole(String systemId);

	/**
	 * 查询全部系统
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月1日 上午10:45:12
	 * @return
	 */
	public List<SystemInfo> findAll();

	/**
	 * 根据组织id查询绑定的系统id
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月1日 下午4:24:46
	 * @param parentOrgId
	 * @return
	 */
	public List<SystemInfo> findSystemsByOrgId(String parentOrgId);
	
	/**
	 * 根据code查系统
	 * */
	public SystemInfo findSystemsByCode(String code);
}
