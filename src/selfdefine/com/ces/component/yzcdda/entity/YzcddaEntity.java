package com.ces.component.yzcdda.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;
@Table
@Entity(name = "T_YZ_CDDA")
public class YzcddaEntity extends StringIDEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8547819930953271470L;
	
	private	String	zhbh	;	//	账户编号
	private	String	yzcmc	;	//	养殖场名称
	private	String	qybm	;	//	企业编码
	private	String	qymc	;	//	企业名称
	private	String	gszcdjzh	;	//	工商注册登记证号
	private	String	lxdh	;	//	联系电话
	private	String	cdbm	;	//	产地编码
	private	String	cdmc	;	//	产地名称
	private	String	cdzsh	;	//	产地证书号
	private	String	dwfytjhgzh	;	//	动物防疫条件合格证号
	private	String	yzcmj	;	//	养殖场面积（平方米）
	private	String	yzcdz	;	//	养殖场地址
	private	String	cdms	;	//	产地描述
	public String getZhbh() {
		return zhbh;
	}
	public void setZhbh(String zhbh) {
		this.zhbh = zhbh;
	}
	public String getYzcmc() {
		return yzcmc;
	}
	public void setYzcmc(String yzcmc) {
		this.yzcmc = yzcmc;
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
	public String getGszcdjzh() {
		return gszcdjzh;
	}
	public void setGszcdjzh(String gszcdjzh) {
		this.gszcdjzh = gszcdjzh;
	}
	public String getLxdh() {
		return lxdh;
	}
	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}
	public String getCdbm() {
		return cdbm;
	}
	public void setCdbm(String cdbm) {
		this.cdbm = cdbm;
	}
	public String getCdmc() {
		return cdmc;
	}
	public void setCdmc(String cdmc) {
		this.cdmc = cdmc;
	}
	public String getCdzsh() {
		return cdzsh;
	}
	public void setCdzsh(String cdzsh) {
		this.cdzsh = cdzsh;
	}
	public String getDwfytjhgzh() {
		return dwfytjhgzh;
	}
	public void setDwfytjhgzh(String dwfytjhgzh) {
		this.dwfytjhgzh = dwfytjhgzh;
	}
	public String getYzcmj() {
		return yzcmj;
	}
	public void setYzcmj(String yzcmj) {
		this.yzcmj = yzcmj;
	}
	public String getYzcdz() {
		return yzcdz;
	}
	public void setYzcdz(String yzcdz) {
		this.yzcdz = yzcdz;
	}
	public String getCdms() {
		return cdms;
	}
	public void setCdms(String cdms) {
		this.cdms = cdms;
	}
	public YzcddaEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public YzcddaEntity(String zhbh, String yzcmc, String qybm, String qymc,
			String gszcdjzh, String lxdh, String cdbm, String cdmc,
			String cdzsh, String dwfytjhgzh, String yzcmj, String yzcdz,
			String cdms) {
		super();
		this.zhbh = zhbh;
		this.yzcmc = yzcmc;
		this.qybm = qybm;
		this.qymc = qymc;
		this.gszcdjzh = gszcdjzh;
		this.lxdh = lxdh;
		this.cdbm = cdbm;
		this.cdmc = cdmc;
		this.cdzsh = cdzsh;
		this.dwfytjhgzh = dwfytjhgzh;
		this.yzcmj = yzcmj;
		this.yzcdz = yzcdz;
		this.cdms = cdms;
	}



}
