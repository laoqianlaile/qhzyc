package com.ces.component.farm.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="cptm")
public class CptmDto {
	//出库单ID
	private String ckdId;
	//产品出库ID
	private String cpckId;
	//追溯码
	private String zsm;
	//产品名称
	private String cpmc;
	//生产日期
	private String scrq;
	//产地
	private String cd;
	//产品种类ID
	private String cpzlId;
	
	public String getCkdId() {
		return ckdId;
	}
	public void setCkdId(String ckdId) {
		this.ckdId = ckdId;
	}
	public String getCpckId() {
		return cpckId;
	}
	public void setCpckId(String cpckId) {
		this.cpckId = cpckId;
	}
	public String getZsm() {
		return zsm;
	}
	public void setZsm(String zsm) {
		this.zsm = zsm;
	}
	public String getCpmc() {
		return cpmc;
	}
	public void setCpmc(String cpmc) {
		this.cpmc = cpmc;
	}
	public String getScrq() {
		return scrq;
	}
	public void setScrq(String scrq) {
		this.scrq = scrq;
	}
	public String getCd() {
		return cd;
	}
	public void setCd(String cd) {
		this.cd = cd;
	}
	public String getCpzlId() {
		return cpzlId;
	}
	public void setCpzlId(String cpzlId) {
		this.cpzlId = cpzlId;
	}
}
