package com.ces.component.herb.dto;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/7/7.
 */
@XmlRootElement(name = "jysh")
public class JyshDto {

	private String token;
	/**
	 * 企业编码
	 */
	private String qybm;
	/**
	 * 企业名称
	 */
	private String qymc;
	/**
	 * 商户ID
	 */
	private String shid;
	/**
	 * 商户名称
	 */
	private String shmc;
	/**
	 * 手机号
	 */
	private String sjh;
	/**
	 * 经营品类
	 */
	private List<Map<String,String>> jypls;

	private String password;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getQybm() {
		return qybm;
	}

	public void setQybm(String qybm) {
		this.qybm = qybm;
	}

	public String getQymc() {
		return qymc;
	}

	public void setQymc(String qymc) {
		this.qymc = qymc;
	}

	public String getShid() {
		return shid;
	}

	public void setShid(String shid) {
		this.shid = shid;
	}

	public String getShmc() {
		return shmc;
	}

	public void setShmc(String shmc) {
		this.shmc = shmc;
	}

	public String getSjh() {
		return sjh;
	}

	public void setSjh(String sjh) {
		this.sjh = sjh;
	}

	public List<Map<String, String>> getJypls() {
		return jypls;
	}

	public void setJypls(List<Map<String, String>> jypls) {
		this.jypls = jypls;
	}

	@XmlTransient
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
