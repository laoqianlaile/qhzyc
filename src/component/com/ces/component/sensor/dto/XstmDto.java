package com.ces.component.sensor.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 销售条码
 * @author dell-pc
 * 2015-9-24
 */
@XmlRootElement(name="xstm")
public class XstmDto {
	/*
	 * 产品销售ID
	 */
	private String cpxxId;
	
	/*
	 * 追溯码
	 */
	private String zsm;
	
	/*
	 * 产品名称
	 */
	private String cpmc;
	
	/*
	 * 生产日期
	 */
	private String scrq;
	
	/*
	 * 产地
	 */
	private String cd;
	
	/*
	 * 产品种类ID
	 */
	private String cpzlId;
	
	
	public String getCpxxId() {
		return cpxxId;
	}

	public void setCpxxId(String cpxxId) {
		this.cpxxId = cpxxId;
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
