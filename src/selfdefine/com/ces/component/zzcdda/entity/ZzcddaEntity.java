package com.ces.component.zzcdda.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Table
@Entity(name = "T_ZZ_CDDA")
public class ZzcddaEntity extends StringIDEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5314852897855229530L;

	/** 种植基地名称 */
	private String zzjdmc;
	/** 企业名称 */
	private String qymc;
	/** 企业编码 */
	private String qybm;
	/** 工商注册登记证号 */
	private String gszcdjzh;
	/** 联系电话 */
	private String lxdh;
	/** 产地编码 */
	private String cdbm;
	/** 产地名称 */
	private String cdmc;
	/** 产地证书号 */
	private String cdzsh;
	/** 种植基地面积（亩） */
	private String zzjdmj;
	/** 种植基地地址 */
	private String zzjddz;
	/** 产地描述 */
	private String cdms;
	/** 账户编码 */
	private String zhbh;

	/** * 删除标示 */
	private String isDelete;

	/** * 删除时间 */
	private String deleteTime;

	/** * 删除人 */
	private String deleteUser;

	/** * 修改时间 */
	private String updateTime;

	/** * 修改人 */
	private String updateUser;

	/** * 创建人 */
	private String createUser;

	/** * 创建时间 */
	private String createTime;

	

	public String getZzjdmc() {
		return zzjdmc;
	}

	public void setZzjdmc(String zzjdmc) {
		this.zzjdmc = zzjdmc;
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

	public String getZzjdmj() {
		return zzjdmj;
	}

	public void setZzjdmj(String zzjdmj) {
		this.zzjdmj = zzjdmj;
	}

	public String getZzjddz() {
		return zzjddz;
	}

	public void setZzjddz(String zzjddz) {
		this.zzjddz = zzjddz;
	}

	public String getCdms() {
		return cdms;
	}

	public void setCdms(String cdms) {
		this.cdms = cdms;
	}

	public String getZhbh() {
		return zhbh;
	}

	public void setZhbh(String zhbh) {
		this.zhbh = zhbh;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(String deleteTime) {
		this.deleteTime = deleteTime;
	}

	public String getDeleteUser() {
		return deleteUser;
	}

	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getQybm() {
		return qybm;
	}

	public void setQybm(String qybm) {
		this.qybm = qybm;
	}

	

	public ZzcddaEntity(String zzjdmc, String qymc, String qybm,
			String gszcdjzh, String lxdh, String cdbm, String cdmc,
			String cdzsh, String zzjdmj, String zzjddz, String cdms,
			String zhbh, String isDelete, String deleteTime, String deleteUser,
			String updateTime, String updateUser, String createUser,
			String createTime) {
		super();
		this.zzjdmc = zzjdmc;
		this.qymc = qymc;
		this.qybm = qybm;
		this.gszcdjzh = gszcdjzh;
		this.lxdh = lxdh;
		this.cdbm = cdbm;
		this.cdmc = cdmc;
		this.cdzsh = cdzsh;
		this.zzjdmj = zzjdmj;
		this.zzjddz = zzjddz;
		this.cdms = cdms;
		this.zhbh = zhbh;
		this.isDelete = isDelete;
		this.deleteTime = deleteTime;
		this.deleteUser = deleteUser;
		this.updateTime = updateTime;
		this.updateUser = updateUser;
		this.createUser = createUser;
		this.createTime = createTime;
	}

	public ZzcddaEntity() {
		super();
	}
}
