package com.ces.component.farm.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="cddcpxx")
public class DdcpxxDto {
	/**
	 * 产品出库ID
	 */
	private String ccId;
	
	/**
	 * 产品追溯码
	 */
	private String cpzsm;
	
	/**
	 * 产品名称
	 */
	private String cpmc;
	
	/**
	 * 客户ID
	 */
	private String khId;

	
	public String getCcId() {
		return ccId;
	}

	public void setCcId(String ccId) {
		this.ccId = ccId;
	}

	public String getCpzsm() {
		return cpzsm;
	}

	public void setCpzsm(String cpzsm) {
		this.cpzsm = cpzsm;
	}

	public String getCpmc() {
		return cpmc;
	}

	public void setCpmc(String cpmc) {
		this.cpmc = cpmc;
	}

	public String getKhId() {
		return khId;
	}

	public void setKhId(String khId) {
		this.khId = khId;
	}
}
