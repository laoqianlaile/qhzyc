package ces.sdk.system.bean;

import java.util.Date;
/**
 * 日志对象接口.<br>
 * @author wanglei
 *
 */
public interface OpLogInfo {
	
	/**
	 * 日志的记录类型 为操作日志
	 */
	public static String OPERATE_LOG_TYPE_FLAG = "1";
	
	/**
	 * 日志的记录类型 为系统日志
	 */
	public static String SYSTEM_LOG_TYPE_FLAG = "0";
	
	/**
	 * 系统日志日志的记录  登录管理系统
	 */
	public static String SYSTEM_LOG_TYPE_AUTHSYSTEM_LOGIN= "1";
	
	/**
	 * 系统日志日志的记录  登录业务系统
	 */
	public static String SYSTEM_LOG_TYPE_BUSINESS_LOGIN = "2";
	
	/**
	 * 系统日志类型对应的显示标题
	 */
	public static String[] LOG_TYPE_TITLE = {"登录管理系统", "登录业务系统"};

	/**LogTypeFlag
	 * 获取ID号
	 * 
	 * @return ID号
	 */
	public Integer getId();

	/**
	 * 设置ID号
	 * 
	 * @param id
	 *            ID号
	 */
	public void setId(Integer id);

	/**
	 * 获取类型
	 * 
	 * @return type
	 * 			类型
	 */
	public String getType();

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(String type);

	/**
	 * 获取用户id
	 * 
	 * @return userId
	 * 			用户id
	 */
	public String getUserId();

	/**
	 * 设置用户id
	 * 
	 * @param userId
	 *            用户id
	 */
	public void setUserId(String userId);

	/**
	 * 获取用户名
	 * 
	 * @return userName
	 * 			用户名
	 */
	public String getUserName();

	/**
	 * 设置用户名
	 * 
	 * @param userName
	 *            用户名
	 */
	public void setUserName(String userName);

	/**
	 * 获取日志时间
	 * 
	 * @return logDate
	 * 			日志时间
	 */
	public Date getLogDate();

	/**
	 * 设置日志时间
	 * 
	 * @param logDate
	 *           日志时间
	 */
	public void setLogDate(Date logDate);

	/**
	 * 获取操作类型
	 * 
	 * @return operate
	 * 			操作类型
	 */
	public String getOperate();

	/**
	 * 设置操作类型
	 * 
	 * @param operate
	 *           操作类型
	 */
	public void setOperate(String operate);

	/**
	 * 获取详细信息
	 * 
	 * @return message
	 * 			详细信息
	 */
	public String getMessage();

	/**
	 * 设置详细信息
	 * 
	 * @param message
	 *           详细信息
	 */
	public void setMessage(String message);

	/**
	 * 获取日志开始时间
	 * 
	 * @return startLogDate
	 * 			日志开始时间
	 */
	public Date getStartLogDate();

	/**
	 * 设置日志开始时间
	 * 
	 * @param startLogDate
	 *           日志开始时间
	 */
	public void setStartLogDate(Date startLogDate);

	/**
	 * 获取日志结束时间
	 * 
	 * @return endLogDate
	 * 			日志结束时间
	 */
	public Date getEndLogDate();

	/**
	 * 设置日志结束时间
	 * 
	 * @param endLogDate
	 *           日志结束时间
	 */
	public void setEndLogDate(Date startLogDate);
	
	/**
	 * 获取 应用系统编号 
	 * @return appKey
	 */
	public String getAppKey();

	/**
	 * 设置 应用系统编号
	 * @param appKey
	 */
	public void setAppKey(String appKey);

	/**
	 * 获取 统一资源地址
	 * @return url
	 */
	public String getUrl();

	/**
	 * 设置 统一资源地址
	 * @param url
	 */
	public void setUrl(String url);

	/** 
	 * 获取备注
	 * 
	 * @return note
	 */
	public String getNote();

	/**
	 * 设置 备注
	 * @param note
	 */
	public void setNote(String note);
	/** 
	 * 获取状态
	 * 
	 * @return note
	 */
	public Integer getStatus();

	/**
	 * 设置 状态
	 * @param note
	 */
	public void setStatus(Integer status);
	
	/**
	 * 获取ip
	 * @return
	 */
	
	public String getIp();

	/**
	 * 设置ip
	 * @param ip
	 */
	public void setIp(String ip);

	/**
	 * 获取mac
	 * @return
	 */
	public String getMac();

	/**
	 * 设置mac
	 * @param mac
	 */
	public void setMac(String mac);
	
	/**
	 * 获得日志的记录类型 
	 * OpLogInfo.OPERATE_LOG_TYPE_FLAG 为操作日志， 
	 * OpLogInfo.SYSTEM_LOG_TYPE_FLAG 为系统日志
	 * @return 
	 */
	public String getLogTypeFlag();

	/**
	 * 设置日志的记录类型 
	 * @param logTypeFlag 当返回值为 OpLogInfo.OPERATE_LOG_TYPE_FLAG 操作日志，
	 * OpLogInfo.SYSTEM_LOG_TYPE_FLAG 为系统日志
	 * 
	 */
	public void setLogTypeFlag(String logTypeFlag);
	
	
	public String getBindOrgId() ;
	
	public void setBindOrgId(String bindOrgId) ;

}
