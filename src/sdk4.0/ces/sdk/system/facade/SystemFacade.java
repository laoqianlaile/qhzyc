package ces.sdk.system.facade;

import ces.sdk.system.bean.SystemInfo;

import java.util.List;
import java.util.Map;

/**
 * 系统管理操作代理接口。<br>
 * @author Synge
 *
 */
/**
 * @author 孙
 *
 */
public interface SystemFacade {
	
	/**
	 * 获取系统
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Synge
	 * @date 2015年6月17日
	 * @return
	 */
	public List<SystemInfo> findSystems(Map<String, String> param);
	/**
	 * 分页查询
	 * <p>Company:上海中信信息发展股份有限公司</p>
	  * @author Synge
	 * @date 2015年6月17日
	 * @param param 分页查询条件 (key是查询关键字,value是关键字的值)
	 * @param currentPage 当前页
	 * @param pageSize 每页总量
	 */
	public List<SystemInfo> find(Map<String, String> param, int currentPage, int pageSize);

	/**
	 * 分页查询 总数量
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Synge
	 * @date 2015年6月17日
	 * @param param 分页查询条件 (key是查询关键字,value是关键字的值)
	 * @return
	 */
	public int findTotal(Map<String, String> param);

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
	public SystemInfo findByID(String id);

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
	public SystemInfo save(SystemInfo systemInfo);

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
	public SystemInfo update(SystemInfo systemInfo);

	/**
	 * 删除系统 (支持多条删除, 多条的话参数id的值用逗号分隔, 例:{id=101,102,103})
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Synge
	 * @comments:<p> </p>
	 * @date 2015年6月17日
	 * @param id 
	 */
	public void delete(String id);
	
	/**
	 * 删除组织级别 (支持多条删除, 多条的话参数id的值用逗号分隔, 例:{id=101,102,103})
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Synge
	 * @comments:<p> </p>
	 * @date 2015年6月17日
	 * @param systemInfo
	 */
	public void delete(SystemInfo systemInfo);
	
	/**
	 * 删除组织级别 
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Synge
	 * @comments:<p> </p>
	 * @date 2015年6月17日
	 * @param systemInfos
	 */
	public void delete(List<SystemInfo> systemInfos);

	/**
	 * 根据条件查询
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Synge
	 * @comments:<p> </p>
	 * @date 2015年6月17日
	 * @param param 查询条件(封装为map)
	 * @return
	 */
	public List<SystemInfo> findByCondition(Map<String, String> param);
	
	/**
	 * 获取最大值
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Synge
	 * @comments:<p> </p>
	 * @date 2015年6月19日
	 * @return
	 */
	public long findMaxOrderNo();

	/**
	 * 判断系统是否存在
	 * @param systemId
	 * @return
	 */
	boolean isSystemExists(String systemId);

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
    
    public List<SystemInfo> findAll();
    
    /**
     * 根据组织id查询绑定的系统
     * 
     * <p>Company:上海中信信息发展股份有限公司</p>
     * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
     * @comments:<p> </p>
     * @date 2015年7月1日 下午4:23:33
     * @param parentOrgId
     * @return
     */
	public List<SystemInfo> findSystemsByOrgId(String parentOrgId);
	
	/**
	 * 根据code查系统
	 * */
	public SystemInfo findSystemsByCode(String code);
}
