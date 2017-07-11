package com.ces.component.qyda.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_PC_QYDA")
public class QydaEntity extends StringIDEntity {

    private static final long serialVersionUID = -1084471451507575346L;

    /** * 企业编码 */
    private String qybm;

    /** * 企业名称 */
    private String qymc;

    /** * 工商注册登记证号 */
    private String cszcdjzh;

    /** * 隶属行政区划代码 */
    private String lsxzqhdm;

    /** * 备案日期 */
    private String barq;

    /** * 法人代表 */
    private String frdb;

    /** * 经营地址 */
    private String jydz;

    /** * 联系电话 */
    private String lxdh;

    /** * 传真 */
    private String cz;

    /** * 组织机构代码 */
    private String zzjgdm;

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

	public String getCszcdjzh() {
		return cszcdjzh;
	}

	public void setCszcdjzh(String cszcdjzh) {
		this.cszcdjzh = cszcdjzh;
	}

	public String getLsxzqhdm() {
		return lsxzqhdm;
	}

	public void setLsxzqhdm(String lsxzqhdm) {
		this.lsxzqhdm = lsxzqhdm;
	}

	public String getBarq() {
		return barq;
	}

	public void setBarq(String barq) {
		this.barq = barq;
	}

	public String getFrdb() {
		return frdb;
	}

	public void setFrdb(String frdb) {
		this.frdb = frdb;
	}

	public String getJydz() {
		return jydz;
	}

	public void setJydz(String jydz) {
		this.jydz = jydz;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getCz() {
		return cz;
	}

	public void setCz(String cz) {
		this.cz = cz;
	}

	public String getZzjgdm() {
		return zzjgdm;
	}

	public void setZzjgdm(String zzjgdm) {
		this.zzjgdm = zzjgdm;
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
