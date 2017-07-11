package ces.sdk.system.dbbean;

import java.util.Date;

import ces.sdk.system.bean.OpLogInfo;

/**
 * 日志对象
 * @author Administrator
 *
 */
public class DbOpLogInfo implements OpLogInfo {
	private Integer id;

	/** 类型 */
	private String type;

	/** 用户id */
	private String userId;

	/** 用户名 */
	private String userName;

	/** 日志时间 */
	private Date logDate;

	/** 操作类型 */
	private String operate = "";

	/** 详细信息 */
	private String message="";
	
	/** 日志时间 */
	private Date startLogDate;
	
	/** 日志时间 */
	private Date endLogDate;
	
	/** 应用系统编号 */
	private String appKey = "系统管理";
	
	/** 统一资源地址 */
	private String url="";
	
	/** 备注 */
	private String note="";
	
	/*状态*/
	private Integer status = null;
	/** IP */
	private String ip = "";

	/** MAC */
	private String mac = "";
	
	private String logTypeFlag = "";

	private String bindOrgId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getEndLogDate() {
		return endLogDate;
	}

	public void setEndLogDate(Date endLogDate) {
		this.endLogDate = endLogDate;
	}

	public Date getStartLogDate() {
		return startLogDate;
	}

	public void setStartLogDate(Date startLogDate) {
		this.startLogDate = startLogDate;
	}

	public String getAppKey() {
		return appKey;
	}

	public String getNote() {
		return note;
	}

	public String getUrl() {
		return url;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	/**
	 * 获得日志的记录类型 1 为操作日志， 0 为系统日志
	 * @return 
	 */
	public String getLogTypeFlag() {
		return logTypeFlag;
	}

	/**
	 * 设置日志的记录类型 
	 * @param logTypeFlag 1 为操作日志， 0 为系统日志
	 * 
	 */
	public void setLogTypeFlag(String logTypeFlag) {
		this.logTypeFlag = logTypeFlag;
	}

	/**
	 * @return the bindOrgId
	 */
	public String getBindOrgId() {
		return bindOrgId;
	}

	/**
	 * @param bindOrgId the bindOrgId to set
	 */
	public void setBindOrgId(String bindOrgId) {
		this.bindOrgId = bindOrgId;
	}
	
	
}
