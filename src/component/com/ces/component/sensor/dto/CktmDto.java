package com.ces.component.sensor.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 出库条码
 * @author dell-pc
 * 2015-9-8
 */
@XmlRootElement(name="cktm")
public class CktmDto {
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


	//出库类型:1包装出库2散货出库
	private String cklx;

	
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
	public void setCklx(String cklx){
		this.cklx=cklx;
	}
	public String getCklx(){
		return this.cklx;
	}
}
