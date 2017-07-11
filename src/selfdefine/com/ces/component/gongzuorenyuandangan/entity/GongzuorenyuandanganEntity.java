package com.ces.component.gongzuorenyuandangan.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_PC_GZRYDA")

public class GongzuorenyuandanganEntity extends StringIDEntity{
	
	private static final long serialVersionUID = -1084471451507575346L;
	
	/** * 员工编码 */
	private String ygbm;
	
	/** * 批发市场编码 */
	private String pfscbm;
	
	/** * 岗位 */
	private String gw;
	
	/** * 身份证号 */
	private String sfzh;
	
	/** * 姓名 */
	private String xm;
	
	/** * 性别 */
	private String xb;
	
	/** * 出生日期 */
	private String csrq;
	
	/** * 从业所需证件 */
	private String cysxzj;
	
	/** * 从业所需证件 */
	private String cysxzjh;
	
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

	public String getYgbm() {
		return ygbm;
	}

	public void setYgbm(String ygbm) {
		this.ygbm = ygbm;
	}

	public String getPfscbm() {
		return pfscbm;
	}

	public void setPfscbm(String pfscbm) {
		this.pfscbm = pfscbm;
	}

	public String getGw() {
		return gw;
	}

	public void setGw(String gw) {
		this.gw = gw;
	}

	public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getCsrq() {
		return csrq;
	}

	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}

	public String getCysxzj() {
		return cysxzj;
	}

	public void setCysxzj(String cysxzj) {
		this.cysxzj = cysxzj;
	}

	public String getCysxzjh() {
		return cysxzjh;
	}

	public void setCysxzjh(String cysxzjh) {
		this.cysxzjh = cysxzjh;
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
    
}
