package com.ces.component.cspt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name="T_CSPT_JYXX")
public class TCsptZsxxEntity extends StringIDEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 创建人
	 */
	private String createTime;
	/**
	 * 创建时间
	 */
	private String createUser;
	/**
	 * 修改人
	 */
	private String updateTime;
	/**
	 * 修改时间
	 */
	private String updateUser;
	/**
	 * 删除人
	 */
	private String deleteTime;
	/**
	 * 删除时间
	 */
	private String deleteUser;
	/**
	 * 删除标识
	 */
	private String isDelete;
	/**
	 * 进货批次号
	 */
	private String jhpch;
	/**
	 * 交易凭证号
	 */
	private String jypzh;
	/**
	 * 追溯码
	 */
	private String zsm;
	/**
	 * 企业名称
	 */
	private String qymc;
	/**
	 * 企业编码
	 */
	private String qybm;
	/**
	 * 经营者编码
	 */
	private String jyzbm;
	/**
	 * 经营者名称
	 */
	private String jyzmc;
	/**
	 * 系统类型:
	 * 		1.种植场
	 *		2.养殖场
			3.蔬菜批发市场
			4.屠宰场
			5.猪肉批发市场
			6.团体采购
			7.超市
			8.零售市场
			9.餐饮
	 */
	private String xtlx;
	/**
	 * 种植养殖批次号
	 */
	private String ZZYZPCH;
	/**
	 * 关联ID
	 */
	private String refId;

	/**
	 * 买家名称
	 */
	private String mjmc;
	/**
	 * 买家编码
	 */
	private String mjbm;
	
	@Column(name="CREATE_TIME")
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Column(name="CREATE_USER")
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	@Column(name="UPDATE_TIME")
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name="UPDATE_USER")
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	@Column(name="DELETE_TIME")
	public String getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(String deleteTime) {
		this.deleteTime = deleteTime;
	}
	@Column(name="DELETE_USER")
	public String getDeleteUser() {
		return deleteUser;
	}
	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}
	@Column(name="IS_DELETE")
	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
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
	@Column(name="ZSM")
	public String getZsm() {
		return zsm;
	}
	public void setZsm(String zsm) {
		this.zsm = zsm;
	}
	@Column(name="QYMC")
	public String getQymc() {
		return qymc;
	}
	public void setQymc(String qymc) {
		this.qymc = qymc;
	}
	@Column(name="QYBM")
	public String getQybm() {
		return qybm;
	}
	public void setQybm(String qybm) {
		this.qybm = qybm;
	}
	@Column(name="JYZBM")
	public String getJyzbm() {
		return jyzbm;
	}
	public void setJyzbm(String jyzbm) {
		this.jyzbm = jyzbm;
	}
	@Column(name="JYZMC")
	public String getJyzmc() {
		return jyzmc;
	}
	public void setJyzmc(String jyzmc) {
		this.jyzmc = jyzmc;
	}
	@Column(name="XTLX")
	public String getXtlx() {
		return xtlx;
	}
	public void setXtlx(String xtlx) {
		this.xtlx = xtlx;
	}
	@Column(name="REFID")
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	@Column(name="ZZYZPCH")
	public String getZZYZPCH() {
		return ZZYZPCH;
	}

	public void setZZYZPCH(String ZZYZPCH) {
		this.ZZYZPCH = ZZYZPCH;
	}
	@Column(name="MJMC")
	public String getMjmc() {
		return mjmc;
	}

	public void setMjmc(String mjmc) {
		this.mjmc = mjmc;
	}
	@Column(name="MJBM")
	public String getMjbm() {
		return mjbm;
	}

	public void setMjbm(String mjbm) {
		this.mjbm = mjbm;
	}
}
