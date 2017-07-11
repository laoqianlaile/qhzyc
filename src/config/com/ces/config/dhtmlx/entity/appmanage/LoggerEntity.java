/**
 * <p>Copyright:Copyright(c) 2013</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.ces.config.dhtmlx.entity.appmanage</p>
 * <p>文件名:LoggerEntity.java</p>
 * <p>类更新历史信息</p>
 * @todo Administrator 创建于 2013-11-13 09:51:32
 */
package com.ces.config.dhtmlx.entity.appmanage;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * .
 * <p>描述:日志查看实体类</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Administrator
 * @date 2013-11-13  09:51:32
 * @version 1.0.2013.1113
 */
@Entity
@Table(name = "T_XARCH_BUSINESS_LOGS")
public class LoggerEntity extends StringIDEntity {
	/** serialVersionUID(long):. */
	private static final long serialVersionUID = -3810628188102896881L;
	/** 用户ID. */
	private String userId;
	/** 时间. */
	private Date time;
	/** 模块名称. */
	private String model;
	/** 操作类型. */
	private String action;
	/** 操作内容. */
	private String opTarget;
	/** 操作结果. */
	private String result;
	/** IP地址. */
	private String ip;
	/**
	 * <p>获取属性userId.</p>
	 * @return String
	 * @author Administrator
	 * @date 2013-11-13  11:34:15
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置属性userId.
	 * @param userId 
	 * @author Administrator
	 * @date 2013-11-13  11:34:15
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * <p>获取属性time.</p>
	 * @return Date
	 * @author Administrator
	 * @date 2013-11-13  11:34:15
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getTime() {
		return time;
	}
	/**
	 * 设置属性time.
	 * @param time 
	 * @author Administrator
	 * @date 2013-11-13  11:34:15
	 */
	public void setTime(Date time) {
		this.time = time;
	}
	/**
	 * <p>获取属性model.</p>
	 * @return String
	 * @author Administrator
	 * @date 2013-11-13  11:34:15
	 */
	public String getModel() {
		return model;
	}
	/**
	 * 设置属性model.
	 * @param model 
	 * @author Administrator
	 * @date 2013-11-13  11:34:15
	 */
	public void setModel(String model) {
		this.model = model;
	}
	/**
	 * <p>获取属性action.</p>
	 * @return String
	 * @author Administrator
	 * @date 2013-11-13  11:34:15
	 */
	public String getAction() {
		return action;
	}
	/**
	 * 设置属性action.
	 * @param action 
	 * @author Administrator
	 * @date 2013-11-13  11:34:15
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * <p>获取属性opTarget.</p>
	 * @return String
	 * @author Administrator
	 * @date 2013-11-13  11:34:15
	 */
	public String getOpTarget() {
		return opTarget;
	}
	/**
	 * 设置属性opTarget.
	 * @param opTarget 
	 * @author Administrator
	 * @date 2013-11-13  11:34:15
	 */
	public void setOpTarget(String opTarget) {
		this.opTarget = opTarget;
	}
	/**
	 * <p>获取属性result.</p>
	 * @return String
	 * @author Administrator
	 * @date 2013-11-13  11:34:15
	 */
	public String getResult() {
		return result;
	}
	/**
	 * 设置属性result.
	 * @param result 
	 * @author Administrator
	 * @date 2013-11-13  11:34:15
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * <p>获取属性ip.</p>
	 * @return String
	 * @author Administrator
	 * @date 2013-11-13  11:34:15
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * 设置属性ip.
	 * @param ip 
	 * @author Administrator
	 * @date 2013-11-13  11:34:15
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
