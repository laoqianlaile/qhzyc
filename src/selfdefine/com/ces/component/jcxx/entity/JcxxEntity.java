package com.ces.component.jcxx.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name="T_PC_JCXX")
public class JcxxEntity extends StringIDEntity{
	private String pfscbm;
	private String pfsbm;
	private String jhpch;
	private String jypzh;
	private String spbm;
	private String 	ypbh;
	private String 	jcybh;
	private Date 	jcrq;
	private String 	jcjg;
	private String jcjgsm;
	@Column(name="PFSCBM")
	public String getPfscbm() {
		return pfscbm;
	}
	public void setPfscbm(String pfscbm) {
		this.pfscbm = pfscbm;
	}
	
	@Column(name="PFSBM")
	public String getPfsbm() {
		return pfsbm;
	}
	public void setPfsbm(String pfsbm) {
		this.pfsbm = pfsbm;
	}
	
	@Column(name="JHPCH")
	public String getJhpch() {
		return jhpch;
	}
	public void setJhpch(String jhpch) {
		this.jhpch = jhpch;
	}
	
	@Column(name="JYPZH")
	public String getJypzh() {
		return jypzh;
	}
	public void setJypzh(String jypzh) {
		this.jypzh = jypzh;
	}
	
	@Column(name="SPBM")
	public String getSpbm() {
		return spbm;
	}
	public void setSpbm(String spbm) {
		this.spbm = spbm;
	}
	
	@Column(name="YPBH")
	public String getYpbh() {
		return ypbh;
	}
	public void setYpbh(String ypbh) {
		this.ypbh = ypbh;
	}
	
	
	@Column(name="JCYBH")
	public String getJcybh() {
		return jcybh;
	}
	public void setJcybh(String jcybh) {
		this.jcybh = jcybh;
	}
	
	
	
	@JsonFormat(pattern="yyyy-mm-dd hh:mm:ss")
	@Column(name="JCRQ")
	public Date getJcrq() {
		return jcrq;
	}
	public void setJcrq(Date jcrq) {
		this.jcrq = jcrq;
	}
	
	
	@Column(name="JCJG")
	public String getJcjg() {
		return jcjg;
	}
	public void setJcjg(String jcjg) {
		this.jcjg = jcjg;
	}
	
	
	@Column(name="JCJGSM")
	public String getJcjgsm() {
		return jcjgsm;
	}
	public void setJcjgsm(String jcjgsm) {
		this.jcjgsm = jcjgsm;
	}
	
	public JcxxEntity() {
		super();
	}
	public JcxxEntity(String id, String pfscbm, String pfsbm, String jhpch,
			String jypzh, String spbm, String ypbh, String jcybh,
			 Date jcrq, String jcjg, String jcjgsm) {
		super();
		this.id = id;
		this.pfscbm = pfscbm;
		this.pfsbm = pfsbm;
		this.jhpch = jhpch;
		this.jypzh = jypzh;
		this.spbm = spbm;
		this.ypbh = ypbh;
		this.jcybh = jcybh;
		this.jcrq = jcrq;
		this.jcjg = jcjg;
		this.jcjgsm = jcjgsm;
	}
}
