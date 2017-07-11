package com.ces.component.sensor.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 采收作物
 * @author dell-pc
 *
 */
@XmlRootElement(name="cscp")
public class CscpDto {
	/*
	 * 采收ID
	 */
	private String csId;
	
	/*
	 * 产品等级
	 */
	private String cpdj;
	
	/*
	 * 产品名称
	 */
	private String cpmc;
	
	/*
	 * 采收时间
	 */
	private String cssj;

	
	public String getCsId() {
		return csId;
	}

	public void setCsId(String csId) {
		this.csId = csId;
	}

	public String getCpdj() {
		return cpdj;
	}

	public void setCpdj(String cpdj) {
		this.cpdj = cpdj;
	}

	public String getCpmc() {
		return cpmc;
	}

	public void setCpmc(String cpmc) {
		this.cpmc = cpmc;
	}

	public String getCssj() {
		return cssj;
	}

	public void setCssj(String cssj) {
		this.cssj = cssj;
	}
}
