package ces.sdk.system.common;

/**
 * 常量接口
 */
public interface CommonConst {

	public static interface ShareCache{
		public static String NEED_CLEAR_CACHE = "1";
		public static String NOT_NEED_CLEAR_CACHE = "0";
	}
	
	/** * 成功 */
	public static final int SUCCESS = 0;

	/** * 失败 */
	public static final int FAILURE = 1;

	/** * 存在 */
	public static final int EXIST = 0;

	/** * 不存在 */
	public static final int NOTEXIST = 1;

	/**
	 * 数据库表名
	 */
	public interface TableName {
		/** * account table */
		public static String T_ACCOUNT = "t_acc";

		/** * id sequence table */
		public static String T_ID_SEQUENCE = "t_id_sequence";

		/** * operate log table */
		public static String T_OPERATE_LOG = "t_operatelog";

		/** * organization table */
		public static String T_ORGANIZATION = "t_org";

		/** * organization user table */
		public static String T_ORGANIZATION_USER = "t_org_user";

		/** * resource table */
		public static String T_RESOURCE = "t_resource";

		/** * role table */
		public static String T_ROLE = "t_role";

		/** * role resource table */
		public static String T_ROLE_RESOURCE = "t_role_res";

		/** * role user table */
		public static String T_ROLE_USER = "t_role_user";

		/** * system child code table */
		public static String T_SYSTEM_CHILD_CODE = "t_sys_child_code";

		/** * system code table */
		public static String T_SYSTEM_CODE = "t_sys_code";

		/** * system user session table */
		public static String T_SYSTEM_USER_SESSION = "t_sys_user_session";

		/** * system log table */
		public static String T_SYSTEM_LOG = "t_syslog";

		/** * user_group role table */
		public static String T_USERGROUP_ROLE = "t_ug_role";

		/** * user_group user table */
		public static String T_USERGROUP_USER = "t_ug_user";

		/** * user table */
		public static String T_USER = "t_user";

		/** * user group table */
		public static String T_USER_GROUP = "t_user_group";

		/** * user resource table */
		public static String T_USER_RESOURCE = "t_user_resource";
	}
	
	/**
	 * 组织常量接口
	 * @author leon
	 *
	 */
	public interface OrgInfo{
		/** 根结点  */
		public static final String TOP = "-1";
		/** 独立组织的节ID */
		public static final String INDEPENDENT_ORG = "1";
	}
	
	/**
	 * 用户常量接口
	 * <p>描述:用户常量接口</p>
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @date 2015年6月28日 下午3:23:49
	 * @version 1.0.2015.0601
	 */
	public interface UserInfo{
		/** 在职  */
		public static String ONLINE = "0";
		/** 离职  */
		public static String OFFLINE = "1";
		/** 专职 */
		public static String FULL_TIME = "0";
		/** 兼职 */
		public static String PART_TIME = "1";
		/** 默认密码 */
		public static String DEFAULT_PASSWORD = "000000";
	}
	
	/**
	 * 角色常量接口
	 * <p>描述:角色常量接口</p>
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @date 2015年6月28日 下午3:23:49
	 * @version 1.0.2015.0601
	 */
	public interface RoleInfo{
		/** 默认角色 */
		public static String DEFAULT_ROLE = "-1";
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
}
