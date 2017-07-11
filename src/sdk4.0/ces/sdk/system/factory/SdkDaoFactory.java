package ces.sdk.system.factory;

import ces.sdk.system.dao.*;
import ces.sdk.system.dao.impl.*;

/**
 * sdk的数据访问层工厂类
 * 
 * <p>描述:用于获取各个数据访问层接口</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
 * @date 2015年6月26日 下午6:05:02
 * @version 1.0.2015.0601
 */
public abstract class SdkDaoFactory {

	
	private static OrgTypeInfoDao orgTypeInfoDao;
	private static OrgInfoDao orgInfoDao;
	private static OrgRoleInfoDao orgRoleInfoDao;
	private static UserInfoDao userInfoDao;
	private static OrgUserInfoDao orgUserInfoDao;
	private static RoleUserInfoDao roleUserInfoDao;
	private static SystemInfoDao systemInfoDao;
	private static SystemResInfoDao systemResInfoDao;
	private static SystemRoleInfoDao systemRoleInfoDao;
	private static ResourceInfoDao resourceInfoDao;
	private static RoleResInfoDao roleResInfoDao;
	private static RoleInfoDao roleInfoDao;

	/**
	 * 获取组织级别访问层
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月26日 下午6:05:30
	 * @return
	 */
	public static OrgTypeInfoDao createOrgTypeInfoDao(){
		if(orgTypeInfoDao == null){
			orgTypeInfoDao = new DBOrgTypeInfoDao();
		}
		return orgTypeInfoDao;
	}
	
	/**
	 * 获取组织数据访问层
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月26日 下午6:05:30
	 * @return
	 */
	public static OrgInfoDao createOrgInfoDao(){
		if(orgInfoDao == null){
			orgInfoDao = new DBOrgInfoDao();
		}
		return orgInfoDao;
	}

	/**
	 * 获取组织角色数据访问层
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月26日 下午6:05:30
	 * @return
	 */
	public static OrgRoleInfoDao createOrgRoleInfoDao(){
		if(orgRoleInfoDao == null){
			orgRoleInfoDao = new DBOrgRoleInfoDao();
		}
		return orgRoleInfoDao;
	}
	
	/**
	 * 获取用户数据访问层
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月26日 下午6:05:30
	 * @return
	 */
	public static UserInfoDao createUserInfoDao(){
		if(userInfoDao == null){
			userInfoDao = new DBUserInfoDao();
		}
		return userInfoDao;
	}
	
	/**
	 * 获取组织用户数据访问层
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月28日 下午3:04:15
	 * @return
	 */
	public static OrgUserInfoDao createOrgUserDao(){
		if(orgUserInfoDao == null){
			orgUserInfoDao = new DBOrgUserInfoDao();
		}
		return orgUserInfoDao;
	}
	
	/**
	 * 获取组织用户数据访问层
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月28日 下午3:04:15
	 * @return
	 */
	public static RoleUserInfoDao createRoleUserDao(){
		if(roleUserInfoDao == null){
			roleUserInfoDao = new DBRoleUserInfoDao();
		}
		return roleUserInfoDao;
	}

	/**
	 * 获取系统数据访问层
	 *
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月26日 下午6:05:30
	 * @return
	 */
	public static SystemInfoDao createSystemInfoDao(){
		if(systemInfoDao == null){
			systemInfoDao = new DBSystemInfoDao();
		}
		return systemInfoDao;
	}

	/**
	 * 获取系统资源数据访问层
	 *
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月26日 下午6:05:30
	 * @return
	 */
	public static SystemResInfoDao createSystemResInfoDao(){
		if(systemResInfoDao == null){
			systemResInfoDao = new DBSystemResInfoDao();
		}
		return systemResInfoDao;
	}

	/**
	 * 获取系统角色数据访问层
	 *
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月26日 下午6:05:30
	 * @return
	 */
	public static SystemRoleInfoDao createSystemRoleInfoDao(){
		if(systemRoleInfoDao == null){
			systemRoleInfoDao = new DBSystemRoleInfoDao();
		}
		return systemRoleInfoDao;
	}

	/**
	 * 获取资源数据访问层
	 *
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月26日 下午6:05:30
	 * @return
	 */
	public static ResourceInfoDao createResourceInfoDao(){
		if(resourceInfoDao == null){
			resourceInfoDao = new DBResourceInfoDao();
		}
		return resourceInfoDao;
	}

	/**
	 * 获取角色资源数据访问层
	 *
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月26日 下午6:05:30
	 * @return
	 */
	public static RoleResInfoDao createRoleResInfoDao(){
		if(roleResInfoDao == null){
			roleResInfoDao = new DBRoleResInfoDao();
		}
		return roleResInfoDao;
	}

	/**
	 * 获取角色数据访问层
	 *
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月26日 下午6:05:30
	 * @return
	 */
	public static RoleInfoDao createRoleInfoDao(){
		if(roleInfoDao == null){
			roleInfoDao = new DBRoleInfoDao();
		}
		return roleInfoDao;
	}

}
