package com.ces.xarch.plugins.common.global;


/**
 * .
 * <p>描述:常量定义</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author 管俊
 * @date 2015-6-2 10:45:31
 * @version 1.0.2015.0602
 */
public final class Constants {
	
	/**
	 * 
	 * 组织级别常量定义
	 * <p>描述:组织级别常量定义</p>
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author 管俊 guan.jun@cesgroup.com.cn
	 * @date 2015-6-2 10:45:44
	 * @version 1.0.2015.0602
	 */
	public static interface OrgType{
		/** 根结点  */
		public static final String TOP = "-1";
	}
	
	
	/**
	 * wj
	 * 
	 * */
	public static interface Resource{
		/** 根结点  */
		public static final String TOP = "-1";
		/** 系统管理平台资源 */
		public static final String AUTHSYSTEM = "1";
		/** 用户能访问的资源 */
		public static final String USER_RESOURCES = "user_resources";
	}
	
	/**
	 * 系统管理常量定义
	 * @author 孙
	 *
	 */
	public static interface System{
		/** 根结点  */
		public static final String TOP = "-1";
		/** 系统管理平台 */
		public static final String AUTHSYSTEM = "1";
	}
	
	
	
	/**
	 * 
	 * 组织常量定义
	 * <p>描述:组织常量定义</p>
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author 管俊 guan.jun@cesgroup.com.cn
	 * @date 2015-6-9 19:21:04
	 * @version 1.0.2015.0609
	 */
	public static interface Org{
		/** 根结点  */
		public static final String TOP = "-1";
		/** 独立组织的节ID */
		public static final String INDEPENDENT_ORG = "1";
	}

	/**
	 * 
	 * 组织常量定义
	 * <p>描述:组织常量定义</p>
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author 管俊 guan.jun@cesgroup.com.cn
	 * @date 2015-6-9 19:21:04
	 * @version 1.0.2015.0609
	 */
	public static interface User{
		/** 根结点  */
		public static final String SUPER_ADMIN = "1";
	}


	/**
	 * 
	 * 角色常量定义
	 * <p>描述:角色常量定义</p>
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author 管俊 guan.jun@cesgroup.com.cn
	 * @date 2015-7-1 17:52:22
	 * @version 1.0.2015.0609
	 */
	public static interface Role{
		/** 默认角色id  */
		public static final String DEFAULT_ROLE_ID = "-1";
		
		/** 角色信息id  */
		public static final String ROLE_INFO_ID = "-2";
		
		/** 超级管理员的roleKey  */
		public static final String SUPER_ROLE = "superrole";
		
		/** 默认角色的roleKey  */
		public static final String DEFAULT_ROLE = "defaultrole";
		
		/** 角色信息的roleKey  */
		public static final String INFORMATION_ROLE = "roleInfo";
	}
}

