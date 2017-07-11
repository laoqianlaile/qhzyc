package com.ces.component.farm.dto;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 田园用户
 * @author dell-pc
 */
@XmlRootElement(name="tyyh")
public class TyyhDto {
	
	private String token;
	
	/*
	 * 用户名
	 */
	private String yhm;
	
	/*
	 * 单位ID
	 */
	private String qyId;
	
	/*
	 * 用户类型
	 */
	private String userType;
	
	/*
	 * 单位名称
	 */
	private String qymc;
	
	/*
	 * 单位编码
	 */
	private String qybm;
	
	/*
	 * 单位类型
	 */
	private String qylx;
	
	/*
	 * 门店ID
	 */
	private String mdId;
	
	private String password;

	
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getQyId() {
		return qyId;
	}

	public void setQyId(String qyId) {
		this.qyId = qyId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getQymc() {
		return qymc;
	}

	public void setQymc(String qymc) {
		this.qymc = qymc;
	}

	public String getQybm() {
		return qybm;
	}

	public void setQybm(String qybm) {
		this.qybm = qybm;
	}

	public String getQylx() {
		return qylx;
	}

	public void setQylx(String qylx) {
		this.qylx = qylx;
	}

	public String getMdId() {
		return mdId;
	}

	public void setMdId(String mdId) {
		this.mdId = mdId;
	}

	@XmlTransient
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
