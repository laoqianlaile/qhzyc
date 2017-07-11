package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 定时任务日志表
 * @author qiujinwei
 * 
 */
@Entity
@Table(name = "T_XTPZ_TIMING_LOG")
public class TimingLog extends StringIDEntity {

	private static final long serialVersionUID = 1L;

	/** 运行结果 0-失败 1-成功 **/
	private String success;
	/** 返回结果 **/
	private String message;
	/** 开始时间 **/
	private String startDate;
	/** 结束时间 **/
	private String endDate;
	/** 关联定时任务ID **/
	private String timingId;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTimingId() {
		return timingId;
	}

	public void setTimingId(String timingId) {
		this.timingId = timingId;
	}

}
