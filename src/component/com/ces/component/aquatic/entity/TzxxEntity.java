package com.ces.component.aquatic.entity;

import com.ces.xarch.core.entity.StringIDEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 黄翔宇 on 15/7/7.
 * 台帐图片详细信息
 */
@Entity
@Table(name="t_sc_sctzxx")
public class TzxxEntity extends StringIDEntity {

	/**
	 * 企业编码
	 */
	private String qybm;
	/**
	 * 图片名称
	 */
	private String tpmc;
	/**
	 * 图片存储文件名
	 */
	private String tplj;
	/**
	 * 上传日期
	 */
	private String scrq;
	/**
	 * 台帐id
	 */
	private String tzid;

	public String getQybm() {
		return qybm;
	}

	public void setQybm(String qybm) {
		this.qybm = qybm;
	}

	public String getTpmc() {
		return tpmc;
	}

	public void setTpmc(String tpmc) {
		this.tpmc = tpmc;
	}

	public String getTplj() {
		return tplj;
	}

	public void setTplj(String tplj) {
		this.tplj = tplj;
	}

	public String getScrq() {
		return scrq;
	}

	public void setScrq(String scrq) {
		this.scrq = scrq;
	}

	public String getTzid() {
		return tzid;
	}

	public void setTzid(String tzid) {
		this.tzid = tzid;
	}
}
